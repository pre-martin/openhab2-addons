/**
 * Copyright (c) 2014-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.holidays.handler;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.State;
import org.openhab.binding.holidays.internal.EventManager;
import org.openhab.binding.holidays.internal.HolidaysInitializationException;
import org.openhab.binding.holidays.internal.job.DailyJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Martin Renner
 */
public abstract class AbstractHolidaysThingHandler extends BaseThingHandler implements EventManager {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected static final String JOB_GROUP = "Holidays";

    protected AbstractHolidaysThingHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void dispose() {
        logger.debug("Disposing thing {}", getThing().getUID());
        deleteDailyJob();
        disposeInternal();
        logger.debug("Disposed thing {}", getThing().getUID());
    }

    /**
     * Can be used to add additional actions to "dispose".
     */
    protected void disposeInternal() {
        // empty
    }

    protected void addDailyJob() {
        final String name = buildJobName(DailyJob.class);

        JobDataMap jobData = new JobDataMap();
        jobData.put(DailyJob.EVENT_MANAGER_PROPERTY, this);

        // Trigger "DailyJob" at midnight
        Trigger trigger = newTrigger().withIdentity(name + "-Trigger", JOB_GROUP).startNow()
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(0, 0)).build();

        addJob(DailyJob.class, jobData, trigger);
    }

    protected void deleteDailyJob() {
        deleteJob(DailyJob.class);
    }

    protected void addJob(Class<? extends Job> jobClass, JobDataMap jobDataMap, Trigger trigger) {
        final String name = buildJobName(jobClass);
        JobDetail jobDetail = newJob(jobClass).withIdentity(name, JOB_GROUP).usingJobData(jobDataMap).build();
        try {
            getScheduler().scheduleJob(jobDetail, trigger);
            logger.info("Scheduled job {} with next fire time {}", name, trigger.getNextFireTime());
        } catch (SchedulerException e) {
            logger.error("Exception while scheduling job", e);
            throw new HolidaysInitializationException("Exception while scheduling job", e);
        }
    }

    protected void deleteJob(Class<? extends Job> jobClass) {
        final String name = buildJobName(jobClass);
        JobKey jobKey = JobKey.jobKey(name, JOB_GROUP);
        try {
            boolean deleted = getScheduler().deleteJob(jobKey);
            if (deleted) {
                logger.info("Removed job {}", name);
            }
        } catch (SchedulerException e) {
            logger.error("Could not delete job", e);
        }
    }

    protected void publishChannelState(ChannelUID channelUID, State state) {
        logger.debug("Updating channel {} with state {}", channelUID, state);
        updateState(channelUID, state);
    }

    /**
     * Creates a job name for Quartz. It consists of the job class and the Thing UID.
     */
    protected String buildJobName(Class<? extends Job> jobClass) {
        return jobClass.getSimpleName() + "-" + getThing().getUID();
    }

    /**
     * Returns the Quartz scheduler.
     */
    protected Scheduler getScheduler() {
        try {
            return StdSchedulerFactory.getDefaultScheduler();
        } catch (SchedulerException e) {
            logger.error("Exception while getting quartz scheduler factory", e);
            throw new HolidaysInitializationException("Execption while getting quartz scheduler factory", e);
        }
    }
}
