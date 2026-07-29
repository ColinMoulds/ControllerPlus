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
package com.excal1bur.controllerplus;

import com.excal1bur.controllerplus.config.ControllerPlusConfig;
import com.excal1bur.controllerplus.registry.ModBlockEntities;
import com.excal1bur.controllerplus.registry.ModBlocks;
import com.excal1bur.controllerplus.registry.ModCreativeTabs;
import com.excal1bur.controllerplus.registry.ModItems;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

/**
 * Common entry point for Controller+.
 */
@Mod(ControllerPlus.MOD_ID)
public final class ControllerPlus {
    public static final String MOD_ID = "controllerplus";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ControllerPlus(IEventBus modBus, ModContainer modContainer) {
        ModBlocks.register(modBus);
        ModItems.register(modBus);
        ModBlockEntities.register(modBus);
        ModCreativeTabs.register(modBus);
        ControllerPlusConfig.register(modContainer);

        modBus.addListener(ModBlockEntities::registerCapabilities);

        LOGGER.info("Initialising Controller+");
        LOGGER.info("AE2 integration mode: version-locked ME Controller subclass with channel lookup bridge");
    }
}
