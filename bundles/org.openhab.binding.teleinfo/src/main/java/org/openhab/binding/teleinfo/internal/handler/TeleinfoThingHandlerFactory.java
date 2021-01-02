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
package org.openhab.binding.teleinfo.internal.handler;

import static org.openhab.binding.teleinfo.internal.TeleinfoBindingConstants.*;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.eclipse.smarthome.io.transport.serial.SerialPortManager;
import org.openhab.binding.teleinfo.internal.handler.cbemm.TeleinfoBaseCbemmElectricityMeterHandler;
import org.openhab.binding.teleinfo.internal.handler.cbemm.TeleinfoEjpCbemmElectricityMeterHandler;
import org.openhab.binding.teleinfo.internal.handler.cbemm.TeleinfoHcCbemmElectricityMeterHandler;
import org.openhab.binding.teleinfo.internal.handler.cbemm.TeleinfoTempoCbemmElectricityMeterHandler;
import org.openhab.binding.teleinfo.internal.handler.cbemm.evoicc.TeleinfoBaseCbemmEvoIccElectricityMeterHandler;
import org.openhab.binding.teleinfo.internal.handler.cbemm.evoicc.TeleinfoEjpCbemmEvoIccElectricityMeterHandler;
import org.openhab.binding.teleinfo.internal.handler.cbemm.evoicc.TeleinfoHcCbemmEvoIccElectricityMeterHandler;
import org.openhab.binding.teleinfo.internal.handler.cbemm.evoicc.TeleinfoTempoCbemmEvoIccElectricityMeterHandler;
import org.openhab.binding.teleinfo.internal.handler.cbetm.TeleinfoBaseCbetmLongElectricityMeterHandler;
import org.openhab.binding.teleinfo.internal.handler.cbetm.TeleinfoEjpCbetmLongElectricityMeterHandler;
import org.openhab.binding.teleinfo.internal.handler.cbetm.TeleinfoHcCbetmLongElectricityMeterHandler;
import org.openhab.binding.teleinfo.internal.handler.cbetm.TeleinfoTempoCbetmLongElectricityMeterHandler;
import org.openhab.binding.teleinfo.internal.serial.TeleinfoSerialControllerHandler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * The {@link TeleinfoThingHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Nicolas SIBERIL - Initial contribution
 */
@NonNullByDefault
@Component(configurationPid = "binding.teleinfo", service = ThingHandlerFactory.class)
public class TeleinfoThingHandlerFactory extends BaseThingHandlerFactory {

    private static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Stream.of(THING_TYPE_SERIAL_CONTROLLER,
            THING_HC_CBEMM_ELECTRICITY_METER_TYPE_UID, THING_BASE_CBEMM_ELECTRICITY_METER_TYPE_UID,
            THING_TEMPO_CBEMM_ELECTRICITY_METER_TYPE_UID, THING_EJP_CBEMM_ELECTRICITY_METER_TYPE_UID,
            THING_HC_CBEMM_EVO_ICC_ELECTRICITY_METER_TYPE_UID, THING_BASE_CBEMM_EVO_ICC_ELECTRICITY_METER_TYPE_UID,
            THING_TEMPO_CBEMM_EVO_ICC_ELECTRICITY_METER_TYPE_UID, THING_EJP_CBEMM_EVO_ICC_ELECTRICITY_METER_TYPE_UID,
            THING_HC_CBETM_ELECTRICITY_METER_TYPE_UID, THING_BASE_CBETM_ELECTRICITY_METER_TYPE_UID,
            THING_TEMPO_CBETM_ELECTRICITY_METER_TYPE_UID, THING_EJP_CBETM_ELECTRICITY_METER_TYPE_UID)
            .collect(Collectors.toSet());

    private final SerialPortManager serialPortManager;

    @Activate
    public TeleinfoThingHandlerFactory(@Reference SerialPortManager serialPortManager) {
        this.serialPortManager = serialPortManager;
    }

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (THING_TYPE_SERIAL_CONTROLLER.equals(thingTypeUID)) {
            return new TeleinfoSerialControllerHandler((Bridge) thing, serialPortManager);
        }

        if (THING_BASE_CBEMM_ELECTRICITY_METER_TYPE_UID.equals(thing.getThingTypeUID())) {
            return new TeleinfoBaseCbemmElectricityMeterHandler(thing);
        } else if (THING_HC_CBEMM_ELECTRICITY_METER_TYPE_UID.equals(thing.getThingTypeUID())) {
            return new TeleinfoHcCbemmElectricityMeterHandler(thing);
        } else if (THING_TEMPO_CBEMM_ELECTRICITY_METER_TYPE_UID.equals(thing.getThingTypeUID())) {
            return new TeleinfoTempoCbemmElectricityMeterHandler(thing);
        } else if (THING_EJP_CBEMM_ELECTRICITY_METER_TYPE_UID.equals(thing.getThingTypeUID())) {
            return new TeleinfoEjpCbemmElectricityMeterHandler(thing);
        } else if (THING_BASE_CBEMM_EVO_ICC_ELECTRICITY_METER_TYPE_UID.equals(thing.getThingTypeUID())) {
            return new TeleinfoBaseCbemmEvoIccElectricityMeterHandler(thing);
        } else if (THING_HC_CBEMM_EVO_ICC_ELECTRICITY_METER_TYPE_UID.equals(thing.getThingTypeUID())) {
            return new TeleinfoHcCbemmEvoIccElectricityMeterHandler(thing);
        } else if (THING_TEMPO_CBEMM_EVO_ICC_ELECTRICITY_METER_TYPE_UID.equals(thing.getThingTypeUID())) {
            return new TeleinfoTempoCbemmEvoIccElectricityMeterHandler(thing);
        } else if (THING_EJP_CBEMM_EVO_ICC_ELECTRICITY_METER_TYPE_UID.equals(thing.getThingTypeUID())) {
            return new TeleinfoEjpCbemmEvoIccElectricityMeterHandler(thing);
        } else if (THING_BASE_CBETM_ELECTRICITY_METER_TYPE_UID.equals(thing.getThingTypeUID())) {
            return new TeleinfoBaseCbetmLongElectricityMeterHandler(thing);
        } else if (THING_HC_CBETM_ELECTRICITY_METER_TYPE_UID.equals(thing.getThingTypeUID())) {
            return new TeleinfoHcCbetmLongElectricityMeterHandler(thing);
        } else if (THING_TEMPO_CBETM_ELECTRICITY_METER_TYPE_UID.equals(thing.getThingTypeUID())) {
            return new TeleinfoTempoCbetmLongElectricityMeterHandler(thing);
        } else if (THING_EJP_CBETM_ELECTRICITY_METER_TYPE_UID.equals(thing.getThingTypeUID())) {
            return new TeleinfoEjpCbetmLongElectricityMeterHandler(thing);
        } else {
            throw new IllegalStateException("Teleinfo frame type not supported: " + thing.getThingTypeUID());
        }
    }
}
