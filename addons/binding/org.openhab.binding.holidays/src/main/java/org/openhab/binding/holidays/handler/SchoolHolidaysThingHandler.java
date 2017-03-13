/**
 * Copyright (c) 2014-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.holidays.handler;

import static org.quartz.TriggerBuilder.newTrigger;

import java.time.LocalDate;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.eclipse.smarthome.core.types.State;
import org.openhab.binding.holidays.internal.impl.SchoolHolidaysManager;
import org.openhab.binding.holidays.internal.job.ReloadJob;
import org.quartz.JobDataMap;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;

/**
 * The {@link SchoolHolidaysThingHandler} is responsible for handling school holidays.
 *
 * @author Martin Renner
 */
public class SchoolHolidaysThingHandler extends AbstractHolidaysThingHandler {

    private SchoolHolidaysManager schoolHolidaysManager;

    public SchoolHolidaysThingHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void initialize() {
        final ThingUID thingUID = getThing().getUID();
        Configuration configuration = getThing().getConfiguration();

        logger.info("Initializing thing {} with configuration {}", thingUID, configuration);

        boolean configComplete = true;

        final String holidaysSchoolFile = StringUtils.trimToNull((String) configuration.get("file"));
        if (holidaysSchoolFile == null) {
            logger.error("Holidays parameter 'file' is mandatory, thing {} will be disabled.", thingUID);
            configComplete = false;
        }

        if (configComplete) {
            // Daily job to update the channel at midnight.
            deleteDailyJob();
            addDailyJob();

            schoolHolidaysManager = new SchoolHolidaysManager(this, holidaysSchoolFile);
            // Reload job to check if holdays file was modified.
            deleteReloadJob();
            addReloadJob();

            updateStatus(ThingStatus.ONLINE);
        } else {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR);
        }

        logger.debug("Initialized thing {} with status {}", thingUID, getThing().getStatus());
    }

    @Override
    protected void disposeInternal() {
        deleteReloadJob();
    }

    private void addReloadJob() {
        final String name = buildJobName(ReloadJob.class);

        JobDataMap jobData = new JobDataMap();
        jobData.put(ReloadJob.SCHOOL_HOLIDAYS_MANAGER_PROPERTY, schoolHolidaysManager);

        // Trigger "ReloadJob" every minute.
        Date triggerStartTime = new Date(System.currentTimeMillis() + 60 * 1000);
        Trigger trigger = newTrigger().withIdentity(name + "-Trigger", JOB_GROUP).startAt(triggerStartTime)
                .withSchedule(SimpleScheduleBuilder.repeatMinutelyForever()).build();

        addJob(ReloadJob.class, jobData, trigger);
    }

    private void deleteReloadJob() {
        deleteJob(ReloadJob.class);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.debug("handleCommand for channel {} with command {}", channelUID, command);
        if (command == RefreshType.REFRESH) {
            // Refresh the (newly) linked channel.
            State state = isSchoolHoliday() ? OnOffType.ON : OnOffType.OFF;
            publishChannelState(channelUID, state);
        }
    }

    private boolean isSchoolHoliday() {
        LocalDate today = LocalDate.now();
        return schoolHolidaysManager.isSchoolHoliday(today);
    }

    @Override
    public void fireEvents() {
        State state = isSchoolHoliday() ? OnOffType.ON : OnOffType.OFF;
        for (Channel channel : thing.getChannels()) {
            publishChannelState(channel.getUID(), state);
        }
    }
}
