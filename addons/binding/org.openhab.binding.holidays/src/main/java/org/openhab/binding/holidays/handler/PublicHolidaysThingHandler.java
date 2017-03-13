/**
 * Copyright (c) 2014-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.holidays.handler;

import java.time.LocalDate;

import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.eclipse.smarthome.core.types.State;
import org.openhab.binding.holidays.internal.EventManager;
import org.openhab.binding.holidays.internal.impl.PublicHolidaysManager;

/**
 * The {@link PublicHolidaysThingHandler} is responsible for handling public holidays.
 *
 * @author Martin Renner
 */
public class PublicHolidaysThingHandler extends AbstractHolidaysThingHandler implements EventManager {

    // static instance because public holidays are "constant".
    private static final PublicHolidaysManager publicHolidaysManager = new PublicHolidaysManager();

    public PublicHolidaysThingHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void initialize() {
        final ThingUID thingUID = getThing().getUID();

        // Daily job to update the channel at midnight.
        deleteDailyJob();
        addDailyJob();

        updateStatus(ThingStatus.ONLINE);

        logger.debug("Initialized thing {} with status {}", thingUID, getThing().getStatus());
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.debug("handleCommand for channel {} with command {}", channelUID, command);
        if (command == RefreshType.REFRESH) {
            // Refresh the (newly) linked channel.
            State state = isPublicHoliday() ? OnOffType.ON : OnOffType.OFF;
            publishChannelState(channelUID, state);
        }
    }

    private boolean isPublicHoliday() {
        LocalDate today = LocalDate.now();
        return publicHolidaysManager.isPublicHoliday(today);
    }

    @Override
    public void fireEvents() {
        State state = isPublicHoliday() ? OnOffType.ON : OnOffType.OFF;
        for (Channel channel : thing.getChannels()) {
            publishChannelState(channel.getUID(), state);
        }
    }
}
