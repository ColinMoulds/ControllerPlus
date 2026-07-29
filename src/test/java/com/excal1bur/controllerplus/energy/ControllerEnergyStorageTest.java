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

class ControllerEnergyStorageTest {
    @Test
    void simulationDoesNotMutateStorage() {
        ControllerEnergyStorage storage = new ControllerEnergyStorage();

        assertEquals(16, storage.insert(16, 100, false));
        assertEquals(0, storage.stored());
    }

    @Test
    void insertionAndExtractionDoNotOverflow() {
        ControllerEnergyStorage storage = new ControllerEnergyStorage();
        storage.insert(120, 100, true);

        assertEquals(100, storage.stored());
        assertEquals(12, storage.extract(64, 12, true));
        assertEquals(88, storage.stored());
    }
}

