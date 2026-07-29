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
package com.excal1bur.controllerplus.registry;

import com.excal1bur.controllerplus.ControllerPlus;

import net.minecraft.world.item.BlockItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Item registrations.
 */
public final class ModItems {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ControllerPlus.MOD_ID);

    public static final DeferredItem<BlockItem> SELF_POWERED_CONTROLLER =
            ITEMS.registerSimpleBlockItem("self_powered_controller", ModBlocks.SELF_POWERED_CONTROLLER);

    private ModItems() {
    }

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }
}

