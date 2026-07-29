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

/**
 * Small mutable AE buffer with simulation-aware insert and extraction.
 */
public final class ControllerEnergyStorage {
    private double stored;

    public double stored() {
        return stored;
    }

    public void clampTo(double capacity) {
        stored = ControllerEnergyLogic.clampStored(stored, capacity);
    }

    public void setStored(double amount, double capacity) {
        stored = ControllerEnergyLogic.clampStored(amount, capacity);
    }

    public double insert(double requested, double capacity, boolean modify) {
        if (!Double.isFinite(requested) || requested <= 0) {
            return 0;
        }
        double inserted = Math.min(requested, Math.max(0, capacity - stored));
        if (modify) {
            stored += inserted;
        }
        return inserted;
    }

    public double extract(double requested, double maximum, boolean modify) {
        double extracted = ControllerEnergyLogic.outputAmount(requested, stored, maximum);
        if (modify) {
            stored -= extracted;
        }
        return extracted;
    }
}

