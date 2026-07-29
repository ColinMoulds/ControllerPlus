/*
 * Controller+
 * Copyright (C) 2026 Controller+ contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * SPDX-License-Identifier: GPL-3.0-only
 */
package com.excal1bur.controllerplus.energy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ControllerEnergyLogicTest {
    @Test
    void generationIsClampedToCapacity() {
        assertEquals(5, ControllerEnergyLogic.generatedAmount(95, 100, 16, true));
    }

    @Test
    void disabledAndZeroGenerationProduceNothing() {
        assertEquals(0, ControllerEnergyLogic.generatedAmount(0, 100, 16, false));
        assertEquals(0, ControllerEnergyLogic.generatedAmount(0, 100, 0, true));
    }

    @Test
    void outputRespectsStorageAndPerTickBudget() {
        assertEquals(12, ControllerEnergyLogic.outputAmount(64, 100, 12));
        assertEquals(7, ControllerEnergyLogic.outputAmount(64, 7, 12));
    }

    @Test
    void corruptStoredValuesAreClamped() {
        assertEquals(0, ControllerEnergyLogic.clampStored(Double.NaN, 100));
        assertEquals(0, ControllerEnergyLogic.clampStored(-5, 100));
        assertEquals(100, ControllerEnergyLogic.clampStored(150, 100));
    }
}

