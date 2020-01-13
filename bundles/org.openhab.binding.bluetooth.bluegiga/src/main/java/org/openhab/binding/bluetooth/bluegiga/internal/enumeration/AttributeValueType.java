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
package org.openhab.binding.bluetooth.bluegiga.internal.enumeration;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to implement the BlueGiga Enumeration <b>AttributeValueType</b>.
 * <p>
 * These enumerations are in the Attribute Client class
 * <p>
 * Note that this code is autogenerated. Manual changes may be overwritten.
 *
 * @author Chris Jackson - Initial contribution of Java code generator
 */
public enum AttributeValueType {
    /**
     * Default unknown value
     */
    UNKNOWN(-1),

    /**
     * [0] Value was read
     */
    ATTCLIENT_ATTRIBUTE_VALUE_TYPE_READ(0x0000),

    /**
     * [1] Value was notified
     */
    ATTCLIENT_ATTRIBUTE_VALUE_TYPE_NOTIFY(0x0001),

    /**
     * [2] Value was indicated
     */
    ATTCLIENT_ATTRIBUTE_VALUE_TYPE_INDICATE(0x0002),

    /**
     * [3] Value was read
     */
    ATTCLIENT_ATTRIBUTE_VALUE_TYPE_READ_BY_TYPE(0x0003),

    /**
     * [4] Value was part of a long attribute
     */
    ATTCLIENT_ATTRIBUTE_VALUE_TYPE_READ_BLOB(0x0004),

    /**
     * [5] Value was indicated and the remote device is waiting for a confirmation. Indicate
     * Confirm command can be used to send a confirmation.
     */
    ATTCLIENT_ATTRIBUTE_VALUE_TYPE_INDICATE_RSP_REQ(0x0005);

    /**
     * A mapping between the integer code and its corresponding type to
     * facilitate lookup by code.
     */
    private static Map<Integer, AttributeValueType> codeMapping;

    private int key;

    private AttributeValueType(int key) {
        this.key = key;
    }

    private static void initMapping() {
        codeMapping = new HashMap<Integer, AttributeValueType>();
        for (AttributeValueType s : values()) {
            codeMapping.put(s.key, s);
        }
    }

    /**
     * Lookup function based on the type code. Returns null if the code does not exist.
     *
     * @param attributeValueType
     *            the code to lookup
     * @return enumeration value.
     */
    public static AttributeValueType getAttributeValueType(int attributeValueType) {
        if (codeMapping == null) {
            initMapping();
        }

        if (codeMapping.get(attributeValueType) == null) {
            return UNKNOWN;
        }

        return codeMapping.get(attributeValueType);
    }

    /**
     * Returns the BlueGiga protocol defined value for this enum
     *
     * @return the BGAPI enumeration key
     */
    public int getKey() {
        return key;
    }
}
