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
package org.openhab.binding.velbus.internal.handler;

import static org.openhab.binding.velbus.internal.VelbusBindingConstants.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.CommonTriggerEvents;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.velbus.internal.VelbusChannelIdentifier;
import org.openhab.binding.velbus.internal.packets.VelbusFeedbackLEDPacket;
import org.openhab.binding.velbus.internal.packets.VelbusPacket;

/**
 * The {@link VelbusSensorHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Cedric Boon - Initial contribution
 */
@NonNullByDefault
public class VelbusSensorHandler extends VelbusThingHandler {
    public static final Set<ThingTypeUID> SUPPORTED_THING_TYPES = new HashSet<>(
            Arrays.asList(THING_TYPE_VMB6IN, THING_TYPE_VMB8IR, THING_TYPE_VMB8PB));

    private static final StringType SET_LED = new StringType("SET_LED");
    private static final StringType SLOW_BLINK_LED = new StringType("SLOW_BLINK_LED");
    private static final StringType FAST_BLINK_LED = new StringType("FAST_BLINK_LED");
    private static final StringType VERY_FAST_BLINK_LED = new StringType("VERY_FAST_BLINK_LED");
    private static final StringType CLEAR_LED = new StringType("CLEAR_LED");

    public VelbusSensorHandler(Thing thing) {
        this(thing, 0);
    }

    public VelbusSensorHandler(Thing thing, int numberOfSubAddresses) {
        super(thing, numberOfSubAddresses);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        VelbusBridgeHandler velbusBridgeHandler = getVelbusBridgeHandler();
        if (velbusBridgeHandler == null) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.BRIDGE_OFFLINE);
            return;
        }

        if (isFeedbackChannel(channelUID) && command instanceof StringType) {
            byte commandByte;

            StringType stringTypeCommand = (StringType) command;
            if (stringTypeCommand.equals(SET_LED)) {
                commandByte = COMMAND_SET_LED;
            } else if (stringTypeCommand.equals(SLOW_BLINK_LED)) {
                commandByte = COMMAND_SLOW_BLINK_LED;
            } else if (stringTypeCommand.equals(FAST_BLINK_LED)) {
                commandByte = COMMAND_FAST_BLINK_LED;
            } else if (stringTypeCommand.equals(VERY_FAST_BLINK_LED)) {
                commandByte = COMMAND_VERY_FAST_BLINK_LED;
            } else if (stringTypeCommand.equals(CLEAR_LED)) {
                commandByte = COMMAND_CLEAR_LED;
            } else {
                throw new UnsupportedOperationException(
                        "The command '" + command + "' is not supported on channel '" + channelUID + "'.");
            }

            VelbusFeedbackLEDPacket packet = new VelbusFeedbackLEDPacket(
                    getModuleAddress().getChannelIdentifier(channelUID), commandByte);

            byte[] packetBytes = packet.getBytes();
            velbusBridgeHandler.sendPacket(packetBytes);
        }
    }

    private boolean isFeedbackChannel(ChannelUID channelUID) {
        return "feedback".equals(channelUID.getGroupId());
    }

    @Override
    public void onPacketReceived(byte[] packet) {
        logger.trace("onPacketReceived() was called");

        if (packet[0] == VelbusPacket.STX && packet.length >= 5) {
            byte address = packet[2];
            byte command = packet[4];

            if (command == COMMAND_PUSH_BUTTON_STATUS && packet.length >= 6) {
                byte channelJustPressed = packet[5];
                if (channelJustPressed != 0) {
                    VelbusChannelIdentifier velbusChannelIdentifier = new VelbusChannelIdentifier(address,
                            channelJustPressed);
                    triggerChannel("input#" + getModuleAddress().getChannelId(velbusChannelIdentifier),
                            CommonTriggerEvents.PRESSED);
                }

                byte channelJustReleased = packet[6];
                if (channelJustReleased != 0) {
                    VelbusChannelIdentifier velbusChannelIdentifier = new VelbusChannelIdentifier(address,
                            channelJustReleased);
                    triggerChannel("input#" + getModuleAddress().getChannelId(velbusChannelIdentifier),
                            CommonTriggerEvents.RELEASED);
                }

                byte channelLongPressed = packet[7];
                if (channelLongPressed != 0) {
                    VelbusChannelIdentifier velbusChannelIdentifier = new VelbusChannelIdentifier(address,
                            channelLongPressed);
                    triggerChannel("input#" + getModuleAddress().getChannelId(velbusChannelIdentifier),
                            CommonTriggerEvents.LONG_PRESSED);
                }
            }
        }
    }
}
