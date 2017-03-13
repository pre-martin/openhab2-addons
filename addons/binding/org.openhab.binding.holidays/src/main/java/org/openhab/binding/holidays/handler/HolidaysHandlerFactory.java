/**
 * Copyright (c) 2014-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.holidays.handler;

import static org.openhab.binding.holidays.HolidaysBindingConstants.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;

/**
 * The {@link HolidaysHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Martin Renner
 */
public class HolidaysHandlerFactory extends BaseThingHandlerFactory {

    /** Set of Things that we support. */
    private final static Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = new HashSet<>(
            Arrays.asList(THING_TYPE_PUBLIC_HOLIDAYS, THING_TYPE_SCHOOL_HOLIDAYS));

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected ThingHandler createHandler(Thing thing) {

        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (thingTypeUID.equals(THING_TYPE_PUBLIC_HOLIDAYS)) {
            return new PublicHolidaysThingHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_SCHOOL_HOLIDAYS)) {
            return new SchoolHolidaysThingHandler(thing);
        }

        return null;
    }
}
