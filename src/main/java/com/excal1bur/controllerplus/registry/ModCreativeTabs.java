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

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Dedicated creative inventory tab.
 */
public final class ModCreativeTabs {
    private static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ControllerPlus.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CONTROLLER_PLUS = TABS.register(
            "controllerplus",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.controllerplus"))
                    .icon(() -> ModItems.SELF_POWERED_CONTROLLER.get().getDefaultInstance())
                    .displayItems((parameters, output) -> output.accept(ModItems.SELF_POWERED_CONTROLLER.get()))
                    .build());

    private ModCreativeTabs() {
    }

    public static void register(IEventBus modBus) {
        TABS.register(modBus);
    }
}

