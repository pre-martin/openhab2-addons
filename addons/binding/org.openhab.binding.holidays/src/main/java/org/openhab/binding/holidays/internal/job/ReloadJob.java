/**
 * Copyright (c) 2014-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.holidays.internal.job;

import org.openhab.binding.holidays.internal.HolidaysInitializationException;
import org.openhab.binding.holidays.internal.impl.SchoolHolidaysManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * This job triggers the reload mechanism of the School holidays thing.
 *
 * @author Martin Renner
 */
public class ReloadJob implements Job {

    public static final String SCHOOL_HOLIDAYS_MANAGER_PROPERTY = "schoolHolidaysManager";

    @Override
    public void execute(JobExecutionContext jobContext) throws JobExecutionException {
        SchoolHolidaysManager schoolHolidaysManager = (SchoolHolidaysManager) jobContext.getMergedJobDataMap()
                .get(SCHOOL_HOLIDAYS_MANAGER_PROPERTY);
        if (schoolHolidaysManager == null) {
            throw new HolidaysInitializationException("Property 'schoolHolidaysManager' must not be null");
        }

        schoolHolidaysManager.readFileIfChanged();
    }

}
