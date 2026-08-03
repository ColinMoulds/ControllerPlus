# Controller+

Controller+ is an Applied Energistics 2 add-on for Minecraft 26.1.2 that introduces specialized ME Controller variants. The current implementation focuses on a self-powered controller that can generate AE energy internally and feed it into a connected ME network.

> Add a more flexible controller layer to your AE2 setup without losing the standard controller behavior you already rely on.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Building](#building)
- [Configuration](#configuration)
- [Compatibility](#compatibility)
- [Contributing](#contributing)
- [Links](#links)
- [License](#license)

## Overview

Version 0.2.0 introduces a self-powered ME Controller that participates in AE2's normal controller validation and channel pathing while adding an internal generator, configurable energy buffer, and network-aware behavior.

## Features

- **Configurable generation and output** — tune how much AE the controller produces and how quickly it can deliver it.
- **Persistent internal buffer** — store energy inside the controller for later use across your network.
- **Proper controller integration** — participates in AE2's controller validation and dense channel behavior.
- **Mixed multiblock support** — works alongside standard AE2 controllers in compatible structures.
- **Optional external input** — allows the connected grid to charge the internal buffer when enabled.
- **Visual and gameplay states** — includes offline, online, and conflicted states with matching effects and particles.
- **Data-driven content** — recipes, loot tables, and unlocks are driven by data rather than hardcoded logic.

## Requirements

| Component | Version |
|---|---|
| Minecraft | 26.1.2 |
| NeoForge | 26.1.2.21-beta or compatible tested build |
| Applied Energistics 2 | 26.1.8-alpha |
| Java | 25 (64-bit) |

## Installation

1. Install a supported NeoForge version.
2. Install a compatible Applied Energistics 2 build.
3. Place Controller+ in your `mods` folder.
4. Launch Minecraft and verify the controller behavior in-game.

## Building

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

Build artifacts are written to `build/libs/`. Data generation is available through `./gradlew runData` or `./gradlew.bat runData`.

## Configuration

NeoForge writes `controllerplus-server.toml` into the world's `serverconfig` directory.

| Option | Default | Purpose |
| --- | ---: | --- |
| `selfPoweredControllerEnabled` | `true` | Enables passive generation |
| `generationRate` | `16` | AE generated per server tick |
| `internalBufferCapacity` | `100000` | Maximum stored AE |
| `maximumOutputRate` | `64` | Maximum AE supplied per server tick |
| `generateWithoutGrid` | `true` | Charges even without an adjacent grid connection |
| `allowExternalEnergyInput` | `true` | Allows the connected grid to charge the buffer |
| `enableParticles` | `true` | Enables online sparks and conflicted smoke effects |

Server-side config values are authoritative in multiplayer and should be applied with a restart.

## Compatibility

Controller+ is currently tied to AE2 26.1.8-alpha and should be tested carefully whenever AE2 changes. Do not update AE2 independently unless a Controller+ release explicitly supports the new version.

## Known Limitations

- Controller integration is tightly coupled to AE2 26.1.8-alpha.
- Standard AE2 controllers keep their own visual casing in mixed structures.
- AE2 26.1.8-alpha is a pre-release dependency.
- Full multiplayer, channel-count, and conflict-shape validation still requires manual in-game testing.

## Contributing

Contributions are welcome. Please keep changes focused, and include build and manual test results with pull requests. See [CONTRIBUTING.md](CONTRIBUTING.md) for workflow details.

## Links

- [CurseForge](https://www.curseforge.com/minecraft/mc-mods/controllerplus/)

## License

Controller+ code is licensed under the [GNU General Public License v3.0](LICENSE).

Original textures, models, and other visual assets are licensed under [Creative Commons Attribution-NonCommercial-ShareAlike 3.0](licenses/LICENSE-ASSETS). See [NOTICE](licenses/NOTICE) for the exact scope and attribution information, including the AE2-derived controller texture template.
