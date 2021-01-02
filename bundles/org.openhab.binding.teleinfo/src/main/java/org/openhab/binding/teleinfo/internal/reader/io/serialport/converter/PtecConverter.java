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
package org.openhab.binding.teleinfo.internal.reader.io.serialport.converter;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.teleinfo.internal.dto.common.Ptec;
import org.openhab.binding.teleinfo.internal.reader.io.serialport.ConversionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link PtecConverter} class defines a converter to translate a Teleinfo String value into
 * {@link Ptec} object.
 *
 * @author Nicolas SIBERIL - Initial contribution
 */
@NonNullByDefault
public class PtecConverter implements Converter {

    private final Logger logger = LoggerFactory.getLogger(PtecConverter.class);

    @Override
    public @Nullable Object convert(String value) throws ConversionException {
        logger.debug("convert(String) [start]");
        if (logger.isTraceEnabled()) {
            logger.trace("value = {}", value);
        }

        Ptec convertedValue = null;
        switch (value) {
            case "TH..":
                convertedValue = Ptec.TH;
                break;
            case "HC..":
                convertedValue = Ptec.HC;
                break;
            case "HP..":
                convertedValue = Ptec.HP;
                break;
            case "HN..":
                convertedValue = Ptec.HN;
                break;
            case "PM..":
                convertedValue = Ptec.PM;
                break;
            case "HCJB":
                convertedValue = Ptec.HCJB;
                break;
            case "HCJW":
                convertedValue = Ptec.HCJW;
                break;
            case "HCJR":
                convertedValue = Ptec.HCJR;
                break;
            case "HPJB":
                convertedValue = Ptec.HPJB;
                break;
            case "HPJW":
                convertedValue = Ptec.HPJW;
                break;
            case "HPJR":
                convertedValue = Ptec.HPJR;
                break;
            default:
                throw new ConversionException(value);
        }

        logger.debug("convert(String) [end]");
        return convertedValue;
    }
}
