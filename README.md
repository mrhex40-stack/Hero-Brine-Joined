# Hero Brine Join The Game
Minecraft Java 1.20.1 Forge mod project.

Build:
1. Install Java 17.
2. Download the Minecraft Forge 1.20.1 MDK.
3. Extract this project into/over the MDK directory.
4. Run `gradlew genIntellijRuns` or `gradlew genEclipseRuns`.
5. Run `gradlew build`.
6. The jar will be in `build/libs`.

Features:
- First join: 5-second delayed Herobrine chat message and nearby leaf clearing.
- First-join event is saved per world/player.
- Later joins use a different random encounter.
- Random fog/darkness, distant Herobrine sightings and jumpscares.
- Rare Herobrine attack: approaches, damages once, then disappears.
- Custom Herobrine-style entity with classic blank-white-eye appearance.
- Events have cooldowns and do not fire continuously.

Note: the project uses an original Herobrine-inspired texture/model rather than packaging a third-party skin.
