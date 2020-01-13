/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.evohome.internal.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.http.HttpMethod;
import org.openhab.binding.evohome.internal.api.models.v2.request.HeatSetPoint;
import org.openhab.binding.evohome.internal.api.models.v2.request.HeatSetPointBuilder;
import org.openhab.binding.evohome.internal.api.models.v2.request.Mode;
import org.openhab.binding.evohome.internal.api.models.v2.request.ModeBuilder;
import org.openhab.binding.evohome.internal.api.models.v2.response.Authentication;
import org.openhab.binding.evohome.internal.api.models.v2.response.Location;
import org.openhab.binding.evohome.internal.api.models.v2.response.LocationStatus;
import org.openhab.binding.evohome.internal.api.models.v2.response.Locations;
import org.openhab.binding.evohome.internal.api.models.v2.response.LocationsStatus;
import org.openhab.binding.evohome.internal.api.models.v2.response.UserAccount;
import org.openhab.binding.evohome.internal.configuration.EvohomeAccountConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the evohome client V2 api
 *
 * @author Jasper van Zuijlen - Initial contribution
 *
 */
public class EvohomeApiClient {

    private static final String APPLICATION_ID = "b013aa26-9724-4dbd-8897-048b9aada249";
    private static final String CLIENT_ID = "4a231089-d2b6-41bd-a5eb-16a0a422b999";
    private static final String CLIENT_SECRET = "1a15cdb8-42de-407b-add0-059f92c530cb";

    private final Logger logger = LoggerFactory.getLogger(EvohomeApiClient.class);
    private final HttpClient httpClient;
    private final EvohomeAccountConfiguration configuration;
    private final ApiAccess apiAccess;

    private Locations locations = new Locations();
    private UserAccount useraccount;
    private LocationsStatus locationsStatus;

    /**
     * Creates a new API client based on the V2 API interface
     *
     * @param configuration The configuration of the account to use
     * @throws Exception
     */
    public EvohomeApiClient(EvohomeAccountConfiguration configuration, HttpClient httpClient) throws Exception {
        this.configuration = configuration;
        this.httpClient = httpClient;

        try {
            httpClient.start();
        } catch (Exception e) {
            logger.error("Could not start http client", e);
            throw new EvohomeApiClientException("Could not start http client", e);
        }

        apiAccess = new ApiAccess(httpClient);
        apiAccess.setApplicationId(APPLICATION_ID);
    }

    /**
     * Closes the current connection to the API
     */
    public void close() {
        apiAccess.setAuthentication(null);
        useraccount = null;
        locations = null;
        locationsStatus = null;

        if (httpClient.isStarted()) {
            try {
                httpClient.stop();
            } catch (Exception e) {
                logger.debug("Could not stop http client.", e);
            }
        }
    }

    public boolean login() {
        boolean success = authenticateWithUsername();

        // If the authentication succeeded, gather the basic intel as well
        if (success) {
            try {
                useraccount = requestUserAccount();
                locations = requestLocations();
            } catch (TimeoutException e) {
                logger.warn("Timeout while retrieving user and location information. Failing loging.");
                success = false;
            }
        } else {
            apiAccess.setAuthentication(null);
            logger.debug("Authorization failed");
        }

        return success;
    }

    public void logout() {
        close();
    }

    public void update() {
        updateAuthentication();
        try {
            locationsStatus = requestLocationsStatus();
        } catch (TimeoutException e) {
            logger.info("Timeout on update");
        }
    }

    public Locations getInstallationInfo() {
        return locations;
    }

    public LocationsStatus getInstallationStatus() {
        return locationsStatus;
    }

    public void setTcsMode(String tcsId, String mode) throws TimeoutException {
        String url = String.format(EvohomeApiConstants.URL_V2_BASE + EvohomeApiConstants.URL_V2_MODE, tcsId);
        Mode modeCommand = new ModeBuilder().setMode(mode).build();
        apiAccess.doAuthenticatedPut(url, modeCommand);
    }

    public void setHeatingZoneOverride(String zoneId, double setPoint) throws TimeoutException {
        HeatSetPoint setPointCommand = new HeatSetPointBuilder().setSetPoint(setPoint).build();
        setHeatingZoneOverride(zoneId, setPointCommand);
    }

    public void cancelHeatingZoneOverride(String zoneId) throws TimeoutException {
        HeatSetPoint setPointCommand = new HeatSetPointBuilder().setCancelSetPoint().build();
        setHeatingZoneOverride(zoneId, setPointCommand);
    }

    private void setHeatingZoneOverride(String zoneId, HeatSetPoint heatSetPoint) throws TimeoutException {
        String url = EvohomeApiConstants.URL_V2_BASE + EvohomeApiConstants.URL_V2_HEAT_SETPOINT;
        url = String.format(url, zoneId);
        apiAccess.doAuthenticatedPut(url, heatSetPoint);
    }

    private UserAccount requestUserAccount() throws TimeoutException {
        String url = EvohomeApiConstants.URL_V2_BASE + EvohomeApiConstants.URL_V2_ACCOUNT;
        return apiAccess.doAuthenticatedGet(url, UserAccount.class);
    }

    private Locations requestLocations() throws TimeoutException {
        Locations locations = new Locations();
        if (useraccount != null) {
            String url = EvohomeApiConstants.URL_V2_BASE + EvohomeApiConstants.URL_V2_INSTALLATION_INFO;
            url = String.format(url, useraccount.getUserId());

            locations = apiAccess.doAuthenticatedGet(url, Locations.class);
        }
        return locations;
    }

    private LocationsStatus requestLocationsStatus() throws TimeoutException {
        LocationsStatus locationsStatus = new LocationsStatus();

        if (locations != null) {
            for (Location location : locations) {
                String url = EvohomeApiConstants.URL_V2_BASE + EvohomeApiConstants.URL_V2_LOCATION_STATUS;
                url = String.format(url, location.getLocationInfo().getLocationId());
                LocationStatus status = apiAccess.doAuthenticatedGet(url, LocationStatus.class);
                locationsStatus.add(status);
            }
        }
        return locationsStatus;
    }

    private boolean authenticate(String credentials, String grantType) {

        String data = credentials + "&" + "Host=rs.alarmnet.com%2F&" + "Pragma=no-cache&"
                + "Cache-Control=no-store+no-cache&"
                + "scope=EMEA-V1-Basic+EMEA-V1-Anonymous+EMEA-V1-Get-Current-User-Account&" + "grant_type=" + grantType
                + "&" + "Content-Type=application%2Fx-www-form-urlencoded%3B+charset%3Dutf-8&"
                + "Connection=Keep-Alive";

        Map<String, String> headers = new HashMap<>();
        String basicAuth = Base64.getEncoder().encodeToString((CLIENT_ID + ":" + CLIENT_SECRET).getBytes());
        headers.put("Authorization", "Basic " + basicAuth);
        headers.put("Accept", "application/json, application/xml, text/json, text/x-json, text/javascript, text/xml");

        Authentication authentication;
        try {
            authentication = apiAccess.doRequest(HttpMethod.POST, EvohomeApiConstants.URL_V2_AUTH, headers, data,
                    "application/x-www-form-urlencoded", Authentication.class);
        } catch (TimeoutException e) {
            // A timeout is not a successful login as well
            authentication = null;
        }

        apiAccess.setAuthentication(authentication);

        if (authentication != null) {
            authentication.setSystemTime(System.currentTimeMillis() / 1000);
        }

        return (authentication != null);
    }

    private boolean authenticateWithUsername() {
        boolean result = false;

        try {
            String credentials = "Username=" + URLEncoder.encode(configuration.username, "UTF-8") + "&" + "Password="
                    + URLEncoder.encode(configuration.password, "UTF-8");
            result = authenticate(credentials, "password");
        } catch (UnsupportedEncodingException e) {
            logger.error("Credential conversion failed", e);
        }

        return result;
    }

    private boolean authenticateWithToken(String accessToken) {
        String credentials = "refresh_token=" + accessToken;
        return authenticate(credentials, "refresh_token");
    }

    private void updateAuthentication() {
        Authentication authentication = apiAccess.getAuthentication();
        if (authentication == null) {
            authenticateWithUsername();
        } else {
            // Compare current time to the expiration time minus four intervals for slack
            long currentTime = System.currentTimeMillis() / 1000;
            long expiration = authentication.getSystemTime() + authentication.getExpiresIn();
            expiration -= 4 * configuration.refreshInterval;

            // Update the access token just before it expires, but fall back to username and password
            // when it fails (i.e. refresh token had been invalidated)
            if (currentTime > expiration) {
                authenticateWithToken(authentication.getRefreshToken());
                if (apiAccess.getAuthentication() == null) {
                    authenticateWithUsername();
                }
            }
        }
    }

}
