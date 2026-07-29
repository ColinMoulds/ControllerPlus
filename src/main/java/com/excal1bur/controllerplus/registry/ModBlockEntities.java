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
import com.excal1bur.controllerplus.blockentity.SelfPoweredControllerBlockEntity;

import appeng.api.AECapabilities;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Block entity and capability registrations.
 */
public final class ModBlockEntities {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ControllerPlus.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SelfPoweredControllerBlockEntity>>
            SELF_POWERED_CONTROLLER = BLOCK_ENTITIES.register(
                    "self_powered_controller",
                    () -> {
                        var type = new BlockEntityType<>(
                                SelfPoweredControllerBlockEntity::new,
                                ModBlocks.SELF_POWERED_CONTROLLER.get());
                        ModBlocks.SELF_POWERED_CONTROLLER.get().setBlockEntity(
                                SelfPoweredControllerBlockEntity.class,
                                type,
                                null,
                                SelfPoweredControllerBlockEntity::serverTick);
                        return type;
                    });

    private ModBlockEntities() {
    }

    public static void register(IEventBus modBus) {
        BLOCK_ENTITIES.register(modBus);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                AECapabilities.IN_WORLD_GRID_NODE_HOST,
                SELF_POWERED_CONTROLLER.get(),
                (blockEntity, context) -> blockEntity);
    }
}
