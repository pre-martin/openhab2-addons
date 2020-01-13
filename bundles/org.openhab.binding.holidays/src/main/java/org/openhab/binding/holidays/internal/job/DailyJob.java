/**
 * Copyright (c) 2014-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.holidays.internal.job;

import org.openhab.binding.holidays.internal.EventManager;
import org.openhab.binding.holidays.internal.HolidaysInitializationException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Daily job of the Holidays binding.
 *
 * @author Martin Renner
 */
public class DailyJob implements Job {

    public static final String EVENT_MANAGER_PROPERTY = "eventManager";
    private static final Logger logger = LoggerFactory.getLogger(DailyJob.class);

    @Override
    public void execute(JobExecutionContext jobContext) throws JobExecutionException {
        logger.info("DailyJob is executing");
        EventManager eventManager = (EventManager) jobContext.getMergedJobDataMap().get(EVENT_MANAGER_PROPERTY);
        if (eventManager == null) {
            throw new HolidaysInitializationException("Property 'eventManager' must not be null");
        }

        // Fire events if necessary.
        eventManager.fireEvents();
    }
}
