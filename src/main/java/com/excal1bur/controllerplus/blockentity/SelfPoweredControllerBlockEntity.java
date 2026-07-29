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
package com.excal1bur.controllerplus.blockentity;

import com.excal1bur.controllerplus.config.ControllerPlusConfig;
import com.excal1bur.controllerplus.energy.ControllerEnergyLogic;
import com.excal1bur.controllerplus.registry.ModBlockEntities;
import com.excal1bur.controllerplus.registry.ModItems;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.networking.GridHelper;
import appeng.api.networking.events.GridControllerChange;
import appeng.blockentity.networking.ControllerBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * An AE2-recognized ME Controller that also generates and stores AE power.
 *
 * <p>This class intentionally subclasses AE2's implementation-level
 * {@link ControllerBlockEntity}. AE2 therefore applies its own controller
 * shape validation, conflict handling, dense channel capacity, and pathing
 * rules to this block. The accompanying grid-lookup mixin supplies the one
 * subclass-aware controller query AE2's channel path calculation requires.
 */
public final class SelfPoweredControllerBlockEntity extends ControllerBlockEntity {
    static {
        // AE2 intentionally dispatches node-owner events by exact runtime class.
        GridHelper.addNodeOwnerEventHandler(
                GridControllerChange.class,
                SelfPoweredControllerBlockEntity.class,
                SelfPoweredControllerBlockEntity::updateState);
    }

    private long outputWindowGameTime = Long.MIN_VALUE;
    private double outputUsedThisTick;

    public SelfPoweredControllerBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.SELF_POWERED_CONTROLLER.get(), pos, state);
    }

    private SelfPoweredControllerBlockEntity(
            BlockEntityType<?> blockEntityType,
            BlockPos pos,
            BlockState state) {
        super(blockEntityType, pos, state);
        setInternalMaxPower(ControllerPlusConfig.INTERNAL_BUFFER_CAPACITY.getAsLong());
        setInternalPublicPowerStorage(true);
        updateConfiguredPowerFlow();
    }

    /**
     * Performs server-authoritative generation and output-budget maintenance.
     */
    public static void serverTick(
            Level level,
            BlockPos pos,
            BlockState state,
            SelfPoweredControllerBlockEntity blockEntity) {
        blockEntity.tickServer(level);
    }

    private void tickServer(Level level) {
        resetOutputWindow(level.getGameTime());

        double configuredCapacity = ControllerPlusConfig.INTERNAL_BUFFER_CAPACITY.getAsLong();
        setInternalMaxPower(configuredCapacity);
        updateConfiguredPowerFlow();

        boolean connected = getMainNode().getNode() != null
                && !getMainNode().getNode().getConnections().isEmpty();
        boolean generationAllowed = ControllerPlusConfig.SELF_POWERED_CONTROLLER_ENABLED.getAsBoolean()
                && (ControllerPlusConfig.GENERATE_WITHOUT_GRID.getAsBoolean() || connected);
        double generated = ControllerEnergyLogic.generatedAmount(
                getInternalCurrentPower(),
                configuredCapacity,
                ControllerPlusConfig.GENERATION_RATE.getAsLong(),
                generationAllowed);

        if (generated > 0) {
            setInternalCurrentPower(getInternalCurrentPower() + generated);
            setChanged();
        }
    }

    private void updateConfiguredPowerFlow() {
        setInternalPowerFlow(ControllerPlusConfig.ALLOW_EXTERNAL_ENERGY_INPUT.getAsBoolean()
                ? AccessRestriction.READ_WRITE
                : AccessRestriction.READ);
    }

    private void resetOutputWindow(long gameTime) {
        if (outputWindowGameTime != gameTime) {
            outputWindowGameTime = gameTime;
            outputUsedThisTick = 0;
        }
    }

    @Override
    protected double extractAEPower(double amount, Actionable mode) {
        if (level != null) {
            resetOutputWindow(level.getGameTime());
        }

        double remainingBudget = Math.max(
                0,
                ControllerPlusConfig.MAXIMUM_OUTPUT_RATE.getAsLong() - outputUsedThisTick);
        double extracted = super.extractAEPower(Math.min(amount, remainingBudget), mode);

        if (mode == Actionable.MODULATE && extracted > 0) {
            outputUsedThisTick += extracted;
            setChanged();
        }
        return extracted;
    }

    @Override
    protected Item getItemFromBlockEntity() {
        return ModItems.SELF_POWERED_CONTROLLER.get();
    }

    public double getStoredEnergyForTesting() {
        return getInternalCurrentPower();
    }
}
