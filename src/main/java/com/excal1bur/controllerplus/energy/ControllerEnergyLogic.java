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
 * Pure calculations used by the controller's server tick.
 */
public final class ControllerEnergyLogic {
    private ControllerEnergyLogic() {
    }

    public static double generatedAmount(double stored, double capacity, double generationRate, boolean enabled) {
        if (!enabled || generationRate <= 0 || stored >= capacity) {
            return 0;
        }
        return Math.min(generationRate, capacity - stored);
    }

    public static double outputAmount(double requested, double stored, double remainingOutputBudget) {
        if (requested <= 0 || stored <= 0 || remainingOutputBudget <= 0) {
            return 0;
        }
        return Math.min(requested, Math.min(stored, remainingOutputBudget));
    }

    public static double clampStored(double stored, double capacity) {
        if (!Double.isFinite(stored) || stored <= 0) {
            return 0;
        }
        return Math.min(stored, Math.max(1, capacity));
    }
}

