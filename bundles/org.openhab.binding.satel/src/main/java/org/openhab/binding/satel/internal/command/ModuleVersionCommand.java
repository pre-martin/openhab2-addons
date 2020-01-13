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
package org.openhab.binding.satel.internal.command;

import org.openhab.binding.satel.internal.event.EventDispatcher;
import org.openhab.binding.satel.internal.event.ModuleVersionEvent;
import org.openhab.binding.satel.internal.protocol.SatelMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command class for command that returns communication module version.
 *
 * @author Krzysztof Goworek - Initial contribution
 */
public class ModuleVersionCommand extends SatelCommandBase {

    private final Logger logger = LoggerFactory.getLogger(ModuleVersionCommand.class);

    public static final byte COMMAND_CODE = 0x7c;

    /**
     * Creates new command class instance.
     */
    public ModuleVersionCommand() {
        super(COMMAND_CODE, false);
    }

    /**
     * @return communication module firmware version and release date
     */
    public String getVersion() {
        return getVersion(0);
    }

    /**
     * @return <code>true</code> if the module supports extended (32-bit) payload for zones/outputs
     */
    public boolean hasExtPayloadSupport() {
        return (response.getPayload()[11] & 0x01) != 0;
    }

    @Override
    public boolean handleResponse(EventDispatcher eventDispatcher, SatelMessage response) {
        if (super.handleResponse(eventDispatcher, response)) {
            // dispatch version event
            eventDispatcher.dispatchEvent(new ModuleVersionEvent(getVersion(), hasExtPayloadSupport()));
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean isResponseValid(SatelMessage response) {
        // validate response
        if (response.getCommand() != COMMAND_CODE) {
            logger.debug("Invalid response code: {}", response.getCommand());
            return false;
        }
        if (response.getPayload().length != 12) {
            logger.debug("Invalid payload length: {}", response.getPayload().length);
            return false;
        }
        return true;
    }

}
