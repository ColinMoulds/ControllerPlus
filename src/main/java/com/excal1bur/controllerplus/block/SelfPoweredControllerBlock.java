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
package com.excal1bur.controllerplus.block;

import com.excal1bur.controllerplus.blockentity.SelfPoweredControllerBlockEntity;
import com.excal1bur.controllerplus.config.ControllerPlusConfig;

import appeng.block.AEBaseEntityBlock;
import appeng.block.networking.ControllerBlock;
import appeng.block.networking.ControllerBlock.ControllerBlockState;
import appeng.menu.MenuOpener;
import appeng.menu.locator.MenuLocators;
import appeng.menu.me.networktool.NetworkStatusMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

/**
 * A self-powered controller block backed by AE2's controller block entity
 * contract.
 *
 * <p>The block deliberately uses AE2's controller-state property so the
 * inherited controller logic can synchronize offline, online, and conflicted
 * states without a custom renderer.
 */
public final class SelfPoweredControllerBlock extends AEBaseEntityBlock<SelfPoweredControllerBlockEntity> {
    public SelfPoweredControllerBlock(Properties properties) {
        super(metalProps(properties).strength(6.0F, 30.0F));
        registerDefaultState(stateDefinition.any()
                .setValue(ControllerBlock.CONTROLLER_STATE, ControllerBlockState.offline));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ControllerBlock.CONTROLLER_STATE);
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof SelfPoweredControllerBlockEntity blockEntity) {
            if (!level.isClientSide()) {
                MenuOpener.open(NetworkStatusMenu.CONTROLLER_TYPE, player, MenuLocators.forBlockEntity(blockEntity));
            }
            return InteractionResult.SUCCESS;
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        ControllerBlockState controllerState = state.getValue(ControllerBlock.CONTROLLER_STATE);
        if (!ControllerPlusConfig.ENABLE_PARTICLES.getAsBoolean()
                || controllerState == ControllerBlockState.offline
                || random.nextFloat() >= 0.35F) {
            return;
        }

        Direction face = Direction.getRandom(random);
        double x = pos.getX() + 0.5 + face.getStepX() * 0.55 + (random.nextDouble() - 0.5) * 0.5;
        double y = pos.getY() + 0.5 + face.getStepY() * 0.55 + (random.nextDouble() - 0.5) * 0.5;
        double z = pos.getZ() + 0.5 + face.getStepZ() * 0.55 + (random.nextDouble() - 0.5) * 0.5;

        if (controllerState == ControllerBlockState.conflicted) {
            level.addParticle(ParticleTypes.SMOKE, x, y, z, 0, 0.015, 0);
        } else {
            level.addParticle(
                    ParticleTypes.ELECTRIC_SPARK,
                    x,
                    y,
                    z,
                    face.getStepX() * 0.015,
                    face.getStepY() * 0.015,
                    face.getStepZ() * 0.015);
        }
    }
}
