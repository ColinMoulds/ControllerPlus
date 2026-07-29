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
package com.excal1bur.controllerplus.mixin;

import java.util.List;

import com.excal1bur.controllerplus.ControllerPlus;
import com.excal1bur.controllerplus.blockentity.SelfPoweredControllerBlockEntity;
import com.google.common.collect.SetMultimap;

import appeng.api.networking.IGridNode;
import appeng.blockentity.networking.ControllerBlockEntity;
import appeng.me.Grid;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Includes controller subclasses in AE2's exact-class controller lookup.
 *
 * <p>AE2's controller validator uses {@code instanceof}, while channel path
 * calculation calls {@code getMachineNodes(ControllerBlockEntity.class)}.
 * AE2's grid index is keyed by each owner's exact class, so this lookup bridge
 * returns every controller subclass without modifying the index, node count,
 * pivot selection, or any of AE2's controller algorithms.
 */
@Mixin(value = Grid.class, remap = false)
abstract class GridControllerLookupMixin {
    @Unique
    private static boolean controllerplus$loggedControllerLookup;

    @Shadow
    @Final
    private SetMultimap<Class<?>, IGridNode> machines;

    @Inject(method = "getMachineNodes", at = @At("HEAD"), cancellable = true, remap = false)
    private void controllerplus$includeControllerSubclasses(
            Class<?> machineClass,
            CallbackInfoReturnable<Iterable<IGridNode>> callbackInfo) {
        if (machineClass == ControllerBlockEntity.class) {
            List<IGridNode> controllers = machines.values().stream()
                    .filter(node -> node.getOwner() instanceof ControllerBlockEntity)
                    .toList();
            if (!controllerplus$loggedControllerLookup
                    && controllers.stream().anyMatch(
                            node -> node.getOwner() instanceof SelfPoweredControllerBlockEntity)) {
                controllerplus$loggedControllerLookup = true;
                ControllerPlus.LOGGER.info("AE2 channel pathing discovered a Controller+ ME Controller node");
            }
            callbackInfo.setReturnValue(controllers);
        }
    }
}
