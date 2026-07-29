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

import java.util.Objects;

import com.excal1bur.controllerplus.block.SelfPoweredControllerBlock;
import com.excal1bur.controllerplus.config.ControllerPlusConfig;
import com.excal1bur.controllerplus.energy.ControllerEnergyLogic;
import com.excal1bur.controllerplus.energy.ControllerEnergyStorage;
import com.excal1bur.controllerplus.registry.ModBlockEntities;
import com.excal1bur.controllerplus.registry.ModItems;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.networking.GridHelper;
import appeng.api.networking.IGridNode;
import appeng.api.networking.IGridNodeListener;
import appeng.api.networking.IInWorldGridNodeHost;
import appeng.api.networking.IManagedGridNode;
import appeng.api.networking.energy.IAEPowerStorage;
import appeng.api.networking.events.GridPowerStorageStateChanged;
import appeng.api.networking.events.GridPowerStorageStateChanged.PowerEventType;
import appeng.api.util.AECableType;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * A supported AE2 grid machine that generates and stores AE power.
 *
 * <p>This is intentionally not an AE2 controller multiblock member. It exposes
 * a normal in-world grid node and the public {@link IAEPowerStorage} service.
 */
public final class SelfPoweredControllerBlockEntity extends BlockEntity
        implements IInWorldGridNodeHost, IAEPowerStorage {
    private static final String STORED_ENERGY_KEY = "storedEnergy";
    private static final String GRID_NODE_TAG = "gridNode";

    private static final IGridNodeListener<SelfPoweredControllerBlockEntity> NODE_LISTENER =
            new IGridNodeListener<>() {
                @Override
                public void onSaveChanges(SelfPoweredControllerBlockEntity owner, IGridNode node) {
                    owner.setChanged();
                }
            };

    private final ControllerEnergyStorage energyStorage = new ControllerEnergyStorage();
    private final IManagedGridNode gridNode;

    private long outputWindowGameTime = Long.MIN_VALUE;
    private double outputUsedThisTick;
    private boolean outputLimitReached;
    private boolean pendingProvideEvent;
    private boolean pendingReceiveEvent;

    public SelfPoweredControllerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SELF_POWERED_CONTROLLER.get(), pos, state);
        gridNode = GridHelper.createManagedNode(this, NODE_LISTENER)
                .setIdlePowerUsage(0)
                .setFlags()
                .setInWorldNode(true)
                .setTagName(GRID_NODE_TAG)
                .setVisualRepresentation(ModItems.SELF_POWERED_CONTROLLER.get())
                .addService(IAEPowerStorage.class, this);
    }

    /**
     * Performs server-authoritative generation and deferred AE2 notifications.
     */
    public static void serverTick(
            Level level,
            BlockPos pos,
            BlockState state,
            SelfPoweredControllerBlockEntity blockEntity) {
        blockEntity.tickServer(level, state);
    }

    private void tickServer(Level level, BlockState state) {
        resetOutputWindow(level.getGameTime());
        flushPendingPowerEvents();

        double capacity = capacity();
        double before = energyStorage.stored();
        energyStorage.clampTo(capacity);

        boolean connected = hasAdjacentGridConnection();
        boolean generationAllowed = ControllerPlusConfig.SELF_POWERED_CONTROLLER_ENABLED.getAsBoolean()
                && (ControllerPlusConfig.GENERATE_WITHOUT_GRID.getAsBoolean() || connected);
        double generated = ControllerEnergyLogic.generatedAmount(
                energyStorage.stored(),
                capacity,
                ControllerPlusConfig.GENERATION_RATE.getAsLong(),
                generationAllowed);

        if (generated > 0) {
            boolean wasEmpty = energyStorage.stored() <= 0;
            energyStorage.insert(generated, capacity, true);
            if (wasEmpty) {
                pendingProvideEvent = true;
            }
        }

        boolean active = generated > 0
                && (!ControllerPlusConfig.ACTIVE_STATE_REQUIRES_GRID.getAsBoolean() || connected);
        updateActiveState(state, active);

        if (Double.compare(before, energyStorage.stored()) != 0) {
            setChanged();
        }

        flushPendingPowerEvents();
    }

    private void resetOutputWindow(long gameTime) {
        if (gameTime == outputWindowGameTime) {
            return;
        }
        outputWindowGameTime = gameTime;
        outputUsedThisTick = 0;
        if (outputLimitReached && energyStorage.stored() > 0) {
            pendingProvideEvent = true;
        }
        outputLimitReached = false;
    }

    private void updateActiveState(BlockState state, boolean active) {
        if (state.getValue(SelfPoweredControllerBlock.ACTIVE) != active && level != null) {
            level.setBlock(worldPosition, state.setValue(SelfPoweredControllerBlock.ACTIVE, active), Block.UPDATE_CLIENTS);
        }
    }

    private boolean hasAdjacentGridConnection() {
        IGridNode node = gridNode.getNode();
        return node != null && !node.getConnections().isEmpty();
    }

    private void flushPendingPowerEvents() {
        if (!gridNode.isReady()) {
            return;
        }
        if (pendingProvideEvent) {
            gridNode.ifPresent(grid -> grid.postEvent(
                    new GridPowerStorageStateChanged(this, PowerEventType.PROVIDE_POWER)));
            pendingProvideEvent = false;
        }
        if (pendingReceiveEvent) {
            gridNode.ifPresent(grid -> grid.postEvent(
                    new GridPowerStorageStateChanged(this, PowerEventType.RECEIVE_POWER)));
            pendingReceiveEvent = false;
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        gridNode.deserialize(input);
        energyStorage.setStored(input.getDoubleOr(STORED_ENERGY_KEY, 0), capacity());
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        gridNode.serialize(output);
        output.putDouble(STORED_ENERGY_KEY, energyStorage.stored());
    }

    @Override
    public void clearRemoved() {
        super.clearRemoved();
        GridHelper.onFirstTick(this, blockEntity -> {
            Level currentLevel = Objects.requireNonNull(blockEntity.getLevel(), "Block entity level");
            blockEntity.gridNode.create(currentLevel, blockEntity.getBlockPos());
        });
    }

    @Override
    public void setRemoved() {
        gridNode.destroy();
        super.setRemoved();
    }

    @Override
    public void onChunkUnloaded() {
        gridNode.destroy();
        super.onChunkUnloaded();
    }

    @Override
    public IGridNode getGridNode(Direction direction) {
        return gridNode.getNode();
    }

    @Override
    public AECableType getCableConnectionType(Direction direction) {
        return AECableType.COVERED;
    }

    @Override
    public double injectAEPower(double amount, Actionable mode) {
        if (!ControllerPlusConfig.ALLOW_EXTERNAL_ENERGY_INPUT.getAsBoolean()) {
            return amount;
        }

        double before = energyStorage.stored();
        double inserted = energyStorage.insert(amount, capacity(), mode == Actionable.MODULATE);
        if (mode == Actionable.MODULATE && inserted > 0) {
            if (before <= 0) {
                pendingProvideEvent = true;
            }
            setChanged();
        }
        return amount - inserted;
    }

    @Override
    public double extractAEPower(double amount, Actionable mode, PowerMultiplier multiplier) {
        if (level != null) {
            resetOutputWindow(level.getGameTime());
        }

        double requested = multiplier.multiply(amount);
        double remainingBudget = Math.max(0,
                ControllerPlusConfig.MAXIMUM_OUTPUT_RATE.getAsLong() - outputUsedThisTick);
        boolean wasFull = energyStorage.stored() >= capacity();
        double extracted = energyStorage.extract(requested, remainingBudget, mode == Actionable.MODULATE);

        if (mode == Actionable.MODULATE && extracted > 0) {
            outputUsedThisTick += extracted;
            outputLimitReached = outputUsedThisTick >= ControllerPlusConfig.MAXIMUM_OUTPUT_RATE.getAsLong();
            if (wasFull) {
                pendingReceiveEvent = true;
            }
            setChanged();
        }

        return multiplier.divide(extracted);
    }

    @Override
    public double getAEMaxPower() {
        return capacity();
    }

    @Override
    public double getAECurrentPower() {
        double remainingBudget = Math.max(0,
                ControllerPlusConfig.MAXIMUM_OUTPUT_RATE.getAsLong() - outputUsedThisTick);
        return Math.min(energyStorage.stored(), remainingBudget);
    }

    @Override
    public boolean isAEPublicPowerStorage() {
        return true;
    }

    @Override
    public AccessRestriction getPowerFlow() {
        return ControllerPlusConfig.ALLOW_EXTERNAL_ENERGY_INPUT.getAsBoolean()
                ? AccessRestriction.READ_WRITE
                : AccessRestriction.READ;
    }

    public double getStoredEnergyForTesting() {
        return energyStorage.stored();
    }

    private static double capacity() {
        return ControllerPlusConfig.INTERNAL_BUFFER_CAPACITY.getAsLong();
    }
}
