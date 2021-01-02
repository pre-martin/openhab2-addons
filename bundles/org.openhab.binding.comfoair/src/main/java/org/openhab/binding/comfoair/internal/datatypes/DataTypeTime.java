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
package org.openhab.binding.comfoair.internal.datatypes;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.library.types.QuantityType;
import org.eclipse.smarthome.core.library.unit.SmartHomeUnits;
import org.eclipse.smarthome.core.types.State;
import org.eclipse.smarthome.core.types.UnDefType;
import org.openhab.binding.comfoair.internal.ComfoAirCommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to handle time values
 *
 * @author Hans Böhm - Initial Contribution
 */
@NonNullByDefault
public class DataTypeTime implements ComfoAirDataType {
    private static final DataTypeTime SINGLETON_INSTANCE = new DataTypeTime();

    private DataTypeTime() {
    }

    private final Logger logger = LoggerFactory.getLogger(DataTypeTime.class);

    public static DataTypeTime getInstance() {
        return SINGLETON_INSTANCE;
    }

    @Override
    public State convertToState(int @Nullable [] data, ComfoAirCommandType commandType) {
        if (data == null) {
            logger.trace("\"DataTypeTime\" class \"convertToState\" method parameter: null");
            return UnDefType.NULL;
        } else {
            int value = calculateNumberValue(data, commandType);

            if (value < 0) {
                return UnDefType.NULL;
            }

            return new QuantityType<>(value, SmartHomeUnits.HOUR);
        }
    }

    @Override
    public int @Nullable [] convertFromState(State value, ComfoAirCommandType commandType) {
        int[] template = commandType.getChangeDataTemplate();
        int[] possibleValues = commandType.getPossibleValues();
        int position = commandType.getChangeDataPos();
        QuantityType<?> hours = ((QuantityType<?>) value).toUnit(SmartHomeUnits.HOUR);

        if (hours != null) {
            int intValue = hours.intValue();

            if (possibleValues == null) {
                template[position] = intValue;
            } else {
                for (int i = 0; i < possibleValues.length; i++) {
                    if (possibleValues[i] == intValue) {
                        template[position] = intValue;
                        break;
                    }
                }
            }
            return template;
        } else {
            logger.trace("\"DataTypeTime\" class \"convertFromState\" undefined state");
            return null;
        }
    }
}
