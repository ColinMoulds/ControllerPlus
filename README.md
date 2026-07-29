# Controller+

Controller+ is an Applied Energistics 2 add-on for Minecraft 26.1.2 that
introduces specialised ME Controller variants.

The first planned controller is the Self-Powered ME Controller, which generates
configurable AE energy internally and supplies it to its connected ME network.

## Overview

Version 0.2.0 implements a **Self-Powered ME Controller**. It participates in
AE2's normal controller validation and channel pathing while adding an internal
generator and configurable energy buffer.

## Features

- Configurable generation, capacity, and maximum output
- Persistent internal AE buffer
- Real ME Controller membership and dense channel output
- Mixed multiblocks with standard AE2 controllers
- Optional external AE input from the connected grid
- Offline, online, and conflicted block models
- Animated full-bright rainbow energy paths while online
- Online light emission and restrained state-aware particles
- Data-driven recipe, loot table, and recipe unlock
- Server-authoritative configuration
- Focused unit tests for energy calculations and storage

## Current status

Controller+ is an early alpha intended for development and in-game validation.
The project is version-locked to AE2 `26.1.8-alpha` because controller
subclassing and channel lookup require implementation-level integration.

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
git clone https://github.com/ColinMoulds/ControllerPlus.git
cd ControllerPlus
./gradlew clean build
```

On Windows:

```powershell
git clone https://github.com/ColinMoulds/ControllerPlus.git
cd ControllerPlus
.\gradlew.bat clean build
```

Build output appears under `build/libs/`. Run data generation with
`./gradlew runData` or `.\gradlew.bat runData`.

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
| `enableParticles` | `true` | Displays online sparks and conflicted smoke |

Numeric values are range-validated by NeoForge. Server configuration is
authoritative in multiplayer. Restart after changing it.

## Compatibility

Controller+ requires the exact AE2 version listed above. Do not update AE2
independently unless a Controller+ release explicitly supports that version.

## Known limitations

- Controller integration is tied specifically to AE2 `26.1.8-alpha`; even a
  minor AE2 update must be retested before changing the dependency range.
- Standard AE2 controllers do not use Controller+'s texture when calculating
  their connected-texture render type. Mixed structures are functionally
  validated but retain each mod's own visual casing.
- AE2 26.1.8-alpha is a pre-release dependency.
- Full channel-count/device tests, maximum-size and conflict shapes,
  persistence across restart, particle appearance, and multiplayer behaviour
  require manual in-game validation before release.

## Contributing

Read [`CONTRIBUTING.md`](CONTRIBUTING.md), keep changes focused, and include
build and manual test results with pull requests.

## Licence

Controller+ code and original assets are licensed under the GNU General Public
License v3.0 only (`GPL-3.0-only`). See [`LICENSE`](LICENSE).
