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
package com.excal1bur.controllerplus.integration;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

class ControllerIntegrationContractTest {
    @Test
    void requiredControllerLookupMixinIsPackaged() throws IOException {
        try (var stream = getClass().getResourceAsStream("/controllerplus.mixins.json")) {
            assertTrue(stream != null, "Mixin configuration must be present on the runtime classpath");
            String config = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            assertTrue(config.contains("\"required\": true"));
            assertTrue(config.contains("\"GridControllerLookupMixin\""));
        }
    }
}
