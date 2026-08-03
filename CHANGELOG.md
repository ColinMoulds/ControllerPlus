# Changelog

## 0.2.0

- Added a self-powered ME Controller that participates in AE2's real controller validation, maximum-size checks, conflict detection, and channel pathing.
- The controller works alongside standard AE2 controllers in mixed multiblock structures.
- Added configurable AE generation rate, internal energy buffer capacity, and maximum output rate.
- The internal energy buffer now persists across save/load and server restarts.
- Added optional external energy input, letting the connected grid charge the internal buffer.
- Added offline, online, and conflicted visual states with matching particles and block light.
- Added an animated, full-bright rainbow circuitry texture for the online state, and a final black casing look.
- Added network status screen support so the controller reports correctly in AE2's network tool.

## 0.1.0

- Initial project setup targeting Minecraft 26.1.2, NeoForge, and Applied Energistics 2 26.1.8-alpha.
