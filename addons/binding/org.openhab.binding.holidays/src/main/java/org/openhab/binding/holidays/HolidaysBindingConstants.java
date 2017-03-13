/**
 * Copyright (c) 2014-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.holidays;

import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link HolidaysBinding} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Martin Renner
 */
public class HolidaysBindingConstants {

    public static final String BINDING_ID = "holidays";

    // List of all Thing Type UIDs
    public final static ThingTypeUID THING_TYPE_PUBLIC_HOLIDAYS = new ThingTypeUID(BINDING_ID, "public");
    public final static ThingTypeUID THING_TYPE_SCHOOL_HOLIDAYS = new ThingTypeUID(BINDING_ID, "school");

    // List of all Channel ids
    public final static String CHANNEL_PUBLIC_HOLIDAY = "publicHoliday";
    public final static String CHANNEL_SCHOOL_HOLIDAY = "schoolHoliday";

}
