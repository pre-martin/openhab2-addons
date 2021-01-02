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
package org.openhab.binding.teleinfo.internal.reader.io;

import java.io.FileInputStream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.junit.Assert;
import org.junit.Test;
import org.openhab.binding.teleinfo.internal.dto.Frame;
import org.openhab.binding.teleinfo.internal.dto.cbemm.evoicc.FrameCbemmEvolutionIccBaseOption;
import org.openhab.binding.teleinfo.internal.dto.cbemm.evoicc.FrameCbemmEvolutionIccHcOption;
import org.openhab.binding.teleinfo.internal.dto.cbemm.evoicc.FrameCbemmEvolutionIccTempoOption;
import org.openhab.binding.teleinfo.internal.dto.cbetm.FrameCbetmLongBaseOption;
import org.openhab.binding.teleinfo.internal.dto.cbetm.FrameCbetmLongEjpOption;
import org.openhab.binding.teleinfo.internal.dto.common.FrameTempoOption.ProgrammeCircuit1;
import org.openhab.binding.teleinfo.internal.dto.common.FrameTempoOption.ProgrammeCircuit2;
import org.openhab.binding.teleinfo.internal.dto.common.Hhphc;
import org.openhab.binding.teleinfo.internal.dto.common.Ptec;
import org.openhab.binding.teleinfo.util.TestUtils;

/**
 *
 * @author Nicolas SIBERIL - Initial contribution
 */
@NonNullByDefault
public class TeleinfoInputStreamTest {

    @Test
    public void testReadNextFrameCbetmBase1() throws Exception {
        try (TeleinfoInputStream in = new TeleinfoInputStream(
                new FileInputStream(TestUtils.getTestFile("cbetm-base-option-1.raw")))) {
            Frame frame = in.readNextFrame();

            Assert.assertNotNull(frame);
            Assert.assertEquals(FrameCbetmLongBaseOption.class, frame.getClass());
            FrameCbetmLongBaseOption frameCbetmLongBaseOption = (FrameCbetmLongBaseOption) frame;
            Assert.assertEquals("XXXXXXXXXXXX", frameCbetmLongBaseOption.getAdco());
            Assert.assertEquals(20, frameCbetmLongBaseOption.getIsousc());
            Assert.assertEquals(1181243, frameCbetmLongBaseOption.getBase());
            Assert.assertEquals(Ptec.TH, frameCbetmLongBaseOption.getPtec());
            Assert.assertEquals(0, frameCbetmLongBaseOption.getIinst1());
            Assert.assertEquals(2, frameCbetmLongBaseOption.getIinst2());
            Assert.assertEquals(0, frameCbetmLongBaseOption.getIinst3());
            Assert.assertEquals(26, frameCbetmLongBaseOption.getImax1().intValue());
            Assert.assertEquals(18, frameCbetmLongBaseOption.getImax2().intValue());
            Assert.assertEquals(27, frameCbetmLongBaseOption.getImax3().intValue());
            Assert.assertEquals(7990, frameCbetmLongBaseOption.getPmax());
            Assert.assertEquals(540, frameCbetmLongBaseOption.getPapp());
            Assert.assertEquals("00", frameCbetmLongBaseOption.getPpot());
        }
    }

    @Test
    public void testReadNextFrameCbemmEvoIccHc1() throws Exception {
        try (TeleinfoInputStream in = new TeleinfoInputStream(
                new FileInputStream(TestUtils.getTestFile("cbemm-evo-icc-hc-option-1.raw")))) {
            Frame frame = in.readNextFrame();

            Assert.assertNotNull(frame);
            Assert.assertEquals(FrameCbemmEvolutionIccHcOption.class, frame.getClass());
            FrameCbemmEvolutionIccHcOption frameCbemmEvolutionIccHcOption = (FrameCbemmEvolutionIccHcOption) frame;
            Assert.assertEquals("XXXXXXXXXXXX", frameCbemmEvolutionIccHcOption.getAdco());
            Assert.assertEquals(30, frameCbemmEvolutionIccHcOption.getIsousc());
            Assert.assertEquals(6906827, frameCbemmEvolutionIccHcOption.getHchc());
            Assert.assertEquals(7617931, frameCbemmEvolutionIccHcOption.getHchp());
            Assert.assertEquals(Ptec.HP, frameCbemmEvolutionIccHcOption.getPtec());
            Assert.assertEquals(3, frameCbemmEvolutionIccHcOption.getIinst());
            Assert.assertEquals(44, frameCbemmEvolutionIccHcOption.getImax().intValue());
            Assert.assertEquals(680, frameCbemmEvolutionIccHcOption.getPapp());
            Assert.assertNull(frameCbemmEvolutionIccHcOption.getAdps());
            Assert.assertEquals(Hhphc.A, frameCbemmEvolutionIccHcOption.getHhphc());
        }
    }

    @Test
    public void testReadNextFrameCbetmEjp1() throws Exception {
        try (TeleinfoInputStream in = new TeleinfoInputStream(
                new FileInputStream(TestUtils.getTestFile("cbetm-ejp-option-1.raw")))) {
            Frame frame = in.readNextFrame();

            Assert.assertNotNull(frame);
            Assert.assertEquals(FrameCbetmLongEjpOption.class, frame.getClass());
            FrameCbetmLongEjpOption frameCbetmLongEjpOption = (FrameCbetmLongEjpOption) frame;
            Assert.assertEquals("XXXXXXXXXX", frameCbetmLongEjpOption.getAdco());
            Assert.assertEquals(30, frameCbetmLongEjpOption.getIsousc());
            Assert.assertEquals(1111111, frameCbetmLongEjpOption.getEjphn());
            Assert.assertEquals(2222222, frameCbetmLongEjpOption.getEjphpm());
            Assert.assertNull(frameCbetmLongEjpOption.getPejp());
            Assert.assertEquals(Ptec.HN, frameCbetmLongEjpOption.getPtec());
            Assert.assertEquals(10, frameCbetmLongEjpOption.getIinst1());
            Assert.assertEquals(5, frameCbetmLongEjpOption.getIinst2());
            Assert.assertEquals(8, frameCbetmLongEjpOption.getIinst3());
            Assert.assertEquals(38, frameCbetmLongEjpOption.getImax1().intValue());
            Assert.assertEquals(42, frameCbetmLongEjpOption.getImax2().intValue());
            Assert.assertEquals(44, frameCbetmLongEjpOption.getImax3().intValue());
            Assert.assertEquals(17480, frameCbetmLongEjpOption.getPmax());
            Assert.assertEquals(5800, frameCbetmLongEjpOption.getPapp());
            Assert.assertEquals("00", frameCbetmLongEjpOption.getPpot());
        }
    }

    @Test
    public void testReadNextFrameCbemmEvoIccTempo1() throws Exception {
        try (TeleinfoInputStream in = new TeleinfoInputStream(
                new FileInputStream(TestUtils.getTestFile("cbemm-evo-icc-tempo-option-1.raw")))) {
            Frame frame = in.readNextFrame();

            Assert.assertNotNull(frame);
            Assert.assertEquals(FrameCbemmEvolutionIccTempoOption.class, frame.getClass());
            FrameCbemmEvolutionIccTempoOption frameCbemmEvolutionIccTempoOption = (FrameCbemmEvolutionIccTempoOption) frame;
            Assert.assertEquals("XXXXXXXXXXXX", frameCbemmEvolutionIccTempoOption.getAdco());
            Assert.assertEquals(45, frameCbemmEvolutionIccTempoOption.getIsousc());
            Assert.assertEquals(2697099, frameCbemmEvolutionIccTempoOption.getBbrhcjb());
            Assert.assertEquals(3494559, frameCbemmEvolutionIccTempoOption.getBbrhpjb());
            Assert.assertEquals(41241, frameCbemmEvolutionIccTempoOption.getBbrhcjw());
            Assert.assertEquals(194168, frameCbemmEvolutionIccTempoOption.getBbrhpjw());
            Assert.assertEquals(0, frameCbemmEvolutionIccTempoOption.getBbrhcjr());
            Assert.assertEquals(89736, frameCbemmEvolutionIccTempoOption.getBbrhpjr());
            Assert.assertEquals(Ptec.HPJR, frameCbemmEvolutionIccTempoOption.getPtec());
            Assert.assertNull(frameCbemmEvolutionIccTempoOption.getDemain());
            Assert.assertEquals(3, frameCbemmEvolutionIccTempoOption.getIinst());
            Assert.assertEquals(37, frameCbemmEvolutionIccTempoOption.getImax().intValue());
            Assert.assertEquals(620, frameCbemmEvolutionIccTempoOption.getPapp());
            Assert.assertNull(frameCbemmEvolutionIccTempoOption.getAdps());
            Assert.assertEquals(Hhphc.Y, frameCbemmEvolutionIccTempoOption.getHhphc());
            Assert.assertEquals(ProgrammeCircuit1.B, frameCbemmEvolutionIccTempoOption.getProgrammeCircuit1());
            Assert.assertEquals(ProgrammeCircuit2.P2, frameCbemmEvolutionIccTempoOption.getProgrammeCircuit2());
        }
    }

    @Test
    public void testReadNextFrameCbemmEvoIccBase1() throws Exception {
        try (TeleinfoInputStream in = new TeleinfoInputStream(
                new FileInputStream(TestUtils.getTestFile("cbemm-evo-icc-base-option-1.raw")))) {
            Frame frame = in.readNextFrame();
            Assert.assertNotNull(frame);
            Assert.assertEquals(FrameCbemmEvolutionIccBaseOption.class, frame.getClass());
            FrameCbemmEvolutionIccBaseOption frameCbemmEvolutionIccBaseOption = (FrameCbemmEvolutionIccBaseOption) frame;
            Assert.assertEquals("031762120162", frameCbemmEvolutionIccBaseOption.getAdco());
            Assert.assertEquals(30, frameCbemmEvolutionIccBaseOption.getIsousc());
            Assert.assertEquals(190575, frameCbemmEvolutionIccBaseOption.getBase());
            Assert.assertEquals(Ptec.TH, frameCbemmEvolutionIccBaseOption.getPtec());
            Assert.assertEquals(1, frameCbemmEvolutionIccBaseOption.getIinst());
            Assert.assertEquals(90, frameCbemmEvolutionIccBaseOption.getImax().intValue());
            Assert.assertEquals(270, frameCbemmEvolutionIccBaseOption.getPapp());
            Assert.assertNull(frameCbemmEvolutionIccBaseOption.getAdps());
        }
    }

    @Test
    public void testInvalidADPSgrouplineWithAutoRepairActivated() throws Exception {
        try (TeleinfoInputStream in = new TeleinfoInputStream(
                new FileInputStream(TestUtils.getTestFile("invalid-adps-groupline.raw")), true)) {
            Frame frame = in.readNextFrame();

            Assert.assertNotNull(frame);
            Assert.assertEquals(FrameCbemmEvolutionIccBaseOption.class, frame.getClass());
            FrameCbemmEvolutionIccBaseOption frameCbemmEvolutionIccBaseOption = (FrameCbemmEvolutionIccBaseOption) frame;
            Assert.assertEquals(37, frameCbemmEvolutionIccBaseOption.getAdps().intValue());
        }
    }
}
