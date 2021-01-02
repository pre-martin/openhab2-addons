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
package org.openhab.binding.teleinfo.internal.dto.cbemm;

import org.openhab.binding.teleinfo.internal.dto.common.FrameHcOption;
import org.openhab.binding.teleinfo.internal.dto.common.Hhphc;

/**
 * The {@link FrameCbemmHcOption} class defines a CBEMM Teleinfo frame with HC/HP option.
 *
 * @author Nicolas SIBERIL - Initial contribution
 */
public class FrameCbemmHcOption extends FrameCbemm implements FrameHcOption {

    private static final long serialVersionUID = -1934715078822532494L;

    private int hchc;
    private int hchp;
    private Hhphc hhphc;

    public FrameCbemmHcOption() {
        // default constructor
    }

    @Override
    public int getHchc() {
        return hchc;
    }

    @Override
    public void setHchc(int hchc) {
        this.hchc = hchc;
    }

    @Override
    public int getHchp() {
        return hchp;
    }

    @Override
    public void setHchp(int hchp) {
        this.hchp = hchp;
    }

    @Override
    public Hhphc getHhphc() {
        return hhphc;
    }

    @Override
    public void setHhphc(Hhphc hhphc) {
        this.hhphc = hhphc;
    }
}
