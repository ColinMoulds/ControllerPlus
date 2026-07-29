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
package com.excal1bur.controllerplus.config;

import com.excal1bur.controllerplus.ControllerPlus;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Authoritative server settings for self-powered controller cores.
 */
public final class ControllerPlusConfig {
    public static final long MAX_RATE = 1_000_000;
    public static final long MAX_CAPACITY = 9_000_000_000_000L;

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue SELF_POWERED_CONTROLLER_ENABLED = BUILDER
            .comment("Enables self-powered controller generation.")
            .define("selfPoweredControllerEnabled", true);

    public static final ModConfigSpec.LongValue GENERATION_RATE = BUILDER
            .comment("AE generated per server tick. Zero disables passive generation.")
            .defineInRange("generationRate", 16L, 0L, MAX_RATE);

    public static final ModConfigSpec.LongValue INTERNAL_BUFFER_CAPACITY = BUILDER
            .comment("Maximum AE stored by each self-powered controller core.")
            .defineInRange("internalBufferCapacity", 100_000L, 1L, MAX_CAPACITY);

    public static final ModConfigSpec.LongValue MAXIMUM_OUTPUT_RATE = BUILDER
            .comment("Maximum AE that each core may supply to its grid per server tick.")
            .defineInRange("maximumOutputRate", 64L, 0L, MAX_RATE);

    public static final ModConfigSpec.BooleanValue GENERATE_WITHOUT_GRID = BUILDER
            .comment("Allows generation while the core has no adjacent AE2 grid connection.")
            .define("generateWithoutGrid", true);

    public static final ModConfigSpec.BooleanValue ALLOW_EXTERNAL_ENERGY_INPUT = BUILDER
            .comment("Allows the connected AE2 grid to charge the internal buffer.")
            .define("allowExternalEnergyInput", true);

    public static final ModConfigSpec.BooleanValue ENABLE_PARTICLES = BUILDER
            .comment("Reserved for optional client particles. No custom particles are emitted in 0.1.0.")
            .define("enableParticles", true);

    public static final ModConfigSpec.BooleanValue ACTIVE_STATE_REQUIRES_GRID = BUILDER
            .comment("Requires an adjacent AE2 connection before the active model is shown.")
            .define("activeStateRequiresGrid", false);

    public static final ModConfigSpec SPEC = BUILDER.build();

    private ControllerPlusConfig() {
    }

    public static void register(ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.SERVER, SPEC, "controllerplus-server.toml");
        ControllerPlus.LOGGER.info("Registered Controller+ server configuration");
    }
}

