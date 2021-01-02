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
package org.openhab.binding.airquality.internal.json;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.gson.annotations.SerializedName;

/**
 * The {@link AirQualityJsonTime} is responsible for storing
 * the "time" node from the waqi.org JSON response
 *
 * @author Kuba Wolanin - Initial contribution
 * @author Gaël L'hopital - Use ZonedDateTime instead of Calendar
 */
@NonNullByDefault
public class AirQualityJsonTime {

    @SerializedName("s")
    private String dateString = "";

    @SerializedName("tz")
    private String timeZone = "";

    private String iso = "";

    /**
     * Get observation time
     *
     * @return {ZonedDateTime}
     */
    public ZonedDateTime getObservationTime() throws DateTimeParseException {
        return ZonedDateTime.parse(iso);
    }
}
