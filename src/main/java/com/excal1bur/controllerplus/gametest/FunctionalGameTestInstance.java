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
package com.excal1bur.controllerplus.gametest;

import java.util.function.Consumer;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * A {@link GameTestInstance} registered directly from code rather than a datapack. Controller+
 * registers these through {@link net.neoforged.neoforge.event.RegisterGameTestsEvent}, so they
 * never need to round-trip through {@link #codec()}.
 */
final class FunctionalGameTestInstance extends GameTestInstance {
    private final Consumer<GameTestHelper> function;

    FunctionalGameTestInstance(
            Consumer<GameTestHelper> function,
            TestData<Holder<TestEnvironmentDefinition<?>>> info) {
        super(info);
        this.function = function;
    }

    @Override
    public void run(GameTestHelper helper) {
        function.accept(helper);
    }

    @Override
    public MapCodec<? extends GameTestInstance> codec() {
        throw new UnsupportedOperationException("Controller+ game tests are code-registered, not data-driven");
    }

    @Override
    protected MutableComponent typeDescription() {
        return Component.literal("Controller+ function test");
    }
}
