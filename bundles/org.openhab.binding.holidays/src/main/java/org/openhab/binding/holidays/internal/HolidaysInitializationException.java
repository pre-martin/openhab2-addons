/**
 * Copyright (c) 2014-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.holidays.internal;

/**
 * @author Martin Renner
 *
 */
public class HolidaysInitializationException extends RuntimeException {

    private static final long serialVersionUID = -8656046515106178819L;

    public HolidaysInitializationException(String message) {
        super(message);
    }

    public HolidaysInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
