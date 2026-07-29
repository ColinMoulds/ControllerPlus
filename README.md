# Controller+

Controller+ is an Applied Energistics 2 add-on for Minecraft 26.1.2 that
introduces specialised ME Controller variants.

The first planned controller is the Self-Powered ME Controller, which generates
configurable AE energy internally and supplies it to its connected ME network.

## Overview

Version 0.1.0 implements the safe initial architecture as a
**Self-Powered Controller Core**. It is a standalone AE2 grid machine with an
internal generator and energy buffer. It uses supported AE2 APIs and original
artwork.

## Features

- Configurable generation, capacity, and maximum output
- Persistent internal AE buffer
- Real AE2 grid node and power-storage integration
- Optional external AE input from the connected grid
- Active and inactive block models
- Data-driven recipe, loot table, and recipe unlock
- Server-authoritative configuration
- Focused unit tests for energy calculations and storage

## Current status

Controller+ is an early alpha intended for development and in-game validation.
The project compiles against AE2 `26.1.8-alpha`. It does not claim true ME
Controller multiblock compatibility.

## Requirements

- Minecraft 26.1.2
- NeoForge 26.1.2.21-beta or a tested compatible build
- Java 25 (64-bit)
- Applied Energistics 2 26.1.8-alpha for Minecraft 26.1.2
- An IDE with Java 25 support, such as IntelliJ IDEA 2025.2 or newer, a
  Java 25-compatible Eclipse release, or VS Code with compatible Java extensions

## Installation

1. Install the supported NeoForge version.
2. Install a compatible Applied Energistics 2 build.
3. Place Controller+ in the `mods` folder.
4. Start Minecraft.

## Building from source

No global Gradle installation is required.

```bash
git clone https://github.com/USERNAME/controller-plus.git
cd controller-plus
./gradlew clean build
```

On Windows:

```powershell
git clone https://github.com/USERNAME/controller-plus.git
cd controller-plus
.\gradlew.bat clean build
```

Build output appears under `build/libs/`. Run data generation with
`./gradlew runData` or `.\gradlew.bat runData`.

After creating the GitHub repository, configure and push the remote with:

```bash
git remote add origin https://github.com/USERNAME/controller-plus.git
git push -u origin main
```

## Configuration

NeoForge creates `controllerplus-server.toml` in the world's `serverconfig`
directory.

| Option | Default | Purpose |
| --- | ---: | --- |
| `selfPoweredControllerEnabled` | `true` | Enables passive generation |
| `generationRate` | `16` | AE generated per server tick |
| `internalBufferCapacity` | `100000` | Maximum stored AE |
| `maximumOutputRate` | `64` | Maximum AE supplied per server tick |
| `generateWithoutGrid` | `true` | Charges while no adjacent grid connection exists |
| `allowExternalEnergyInput` | `true` | Allows the AE2 grid to charge the buffer |
| `enableParticles` | `true` | Reserved for optional particles |
| `activeStateRequiresGrid` | `false` | Requires a grid connection for the active model |

Numeric values are range-validated by NeoForge. Server configuration is
authoritative in multiplayer. Restart after changing it.

## Compatibility

The implementation uses AE2's public managed-node, in-world node host,
capability, and AE power-storage APIs. See
[`docs/AE2_INTEGRATION.md`](docs/AE2_INTEGRATION.md) for the inspected classes
and exact boundary.

## Known limitations

- The current block is a controller-style grid machine, not an ME Controller
  multiblock member.
- It does not provide controller channels.
- Particles are reserved by configuration but not implemented in 0.1.0.
- AE2 26.1.8-alpha is a pre-release dependency.
- Client, dedicated-server, persistence, and multiplayer behaviour require
  manual in-game validation before a public release.

## Roadmap

- Complete in-game validation of the Self-Powered Controller Core
- Revisit true controller support if AE2 publishes a supported extension point
- Consider energy, solar, resonant, creative, compact, and wireless variants in
  later releases

## Contributing

Read [`CONTRIBUTING.md`](CONTRIBUTING.md), keep changes focused, and include
build and manual test results with pull requests.

## Licence

Controller+ code and original assets are licensed under the GNU General Public
License v3.0 only (`GPL-3.0-only`). See [`LICENSE`](LICENSE).

