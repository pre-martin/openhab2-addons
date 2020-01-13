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
package org.openhab.binding.evohome.internal.handler;

import org.eclipse.smarthome.core.thing.ThingStatus;

/**
 * Interface for a listener of the evohome account status
 *
 * @author Jasper van Zuijlen - Initial contribution
 *
 */
public interface AccountStatusListener {

    /**
     * Notifies the client that the status has changed.
     * 
     * @param status The new status of the account thing
     */
    public void accountStatusChanged(ThingStatus status);

}
