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
package org.openhab.binding.somfytahoma.internal;

import static org.openhab.binding.somfytahoma.internal.SomfyTahomaBindingConstants.*;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.eclipse.smarthome.io.net.http.HttpClientFactory;
import org.openhab.binding.somfytahoma.internal.handler.*;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link SomfyTahomaHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Ondrej Pecta - Initial contribution
 */
@NonNullByDefault
@Component(service = ThingHandlerFactory.class, configurationPid = "binding.somfytahoma")
public class SomfyTahomaHandlerFactory extends BaseThingHandlerFactory {

    private final Logger logger = LoggerFactory.getLogger(SomfyTahomaHandlerFactory.class);

    private final HttpClientFactory httpClientFactory;

    @Activate
    public SomfyTahomaHandlerFactory(@Reference HttpClientFactory httpClientFactory) {
        this.httpClientFactory = httpClientFactory;
    }

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return THING_TYPE_BRIDGE.equals(thingTypeUID) || SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    @Nullable
    protected ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        logger.debug("Creating handler for {}", thing.getThingTypeUID().getId());

        if (thingTypeUID.equals(THING_TYPE_BRIDGE)) {
            return new SomfyTahomaBridgeHandler((Bridge) thing, httpClientFactory);
        } else if (thingTypeUID.equals(THING_TYPE_GATEWAY)) {
            return new SomfyTahomaGatewayHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_ROLLERSHUTTER)) {
            return new SomfyTahomaRollerShutterHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_ROLLERSHUTTER_SILENT)) {
            return new SomfyTahomaSilentRollerShutterHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_ROLLERSHUTTER_UNO)) {
            return new SomfyTahomaUnoRollerShutterHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_SCREEN) || thingTypeUID.equals(THING_TYPE_EXTERIORSCREEN)) {
            return new SomfyTahomaRollerShutterHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_VENETIANBLIND)
                || thingTypeUID.equals(THING_TYPE_EXTERIORVENETIANBLIND)) {
            return new SomfyTahomaVenetianBlindHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_GARAGEDOOR)) {
            return new SomfyTahomaRollerShutterHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_AWNING)) {
            return new SomfyTahomaAwningHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_ACTIONGROUP)) {
            return new SomfyTahomaActionGroupHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_ONOFF)) {
            return new SomfyTahomaOnOffHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_LIGHT)) {
            return new SomfyTahomaOnOffHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_DIMMER_LIGHT)) {
            return new SomfyTahomaDimmerLightHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_LIGHTSENSOR)) {
            return new SomfyTahomaLightSensorHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_SMOKESENSOR)) {
            return new SomfyTahomaSmokeSensorHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_OCCUPANCYSENSOR)) {
            return new SomfyTahomaOccupancySensorHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_CONTACTSENSOR)) {
            return new SomfyTahomaContactSensorHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_WATERSENSOR)) {
            return new SomfyTahomaWaterSensorHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_HUMIDITYSENSOR)) {
            return new SomfyTahomaHumiditySensorHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_WINDOW)) {
            return new SomfyTahomaWindowHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_INTERNAL_ALARM)) {
            return new SomfyTahomaInternalAlarmHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_EXTERNAL_ALARM)) {
            return new SomfyTahomaExternalAlarmHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_POD)) {
            return new SomfyTahomaPodHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_HEATING_SYSTEM)) {
            return new SomfyTahomaHeatingSystemHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_ONOFF_HEATING_SYSTEM)) {
            return new SomfyTahomaOnOffHeatingSystemHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_EXTERIOR_HEATING_SYSTEM)) {
            return new SomfyTahomaExteriorHeatingSystemHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_DOOR_LOCK)) {
            return new SomfyTahomaDoorLockHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_PERGOLA)) {
            return new SomfyTahomaPergolaHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_WINDOW_HANDLE)) {
            return new SomfyTahomaWindowHandleHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_TEMPERATURESENSOR)) {
            return new SomfyTahomaTemperatureSensorHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_GATE)) {
            return new SomfyTahomaGateHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_CURTAIN)) {
            return new SomfyTahomaCurtainHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_ELECTRICITYSENSOR)) {
            return new SomfyTahomaElectricitySensorHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_DOCK)) {
            return new SomfyTahomaDockHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_SIREN)) {
            return new SomfyTahomaSirenHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_ADJUSTABLE_SLATS_ROLLERSHUTTER)) {
            return new SomfyTahomaAdjustableSlatsRollerShutterHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_MYFOX_CAMERA)) {
            return new SomfyTahomaMyfoxCameraHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_MYFOX_ALARM)) {
            return new SomfyTahomaMyfoxAlarmHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_THERMOSTAT)) {
            return new SomfyTahomaThermostatHandler(thing);
        } else {
            return null;
        }
    }
}
