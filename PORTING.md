# Porting this fork to a new Minecraft version

Minecraft changes its code every release, and Fabric mods have to follow. This is the checklist for
moving the fork from 26.3 to the next version (26.4, 27.x, ...), plus the things that must not break
because Solstone depends on them.

## 0. Before you start

- **Don't update the server or the family's clients first.** Keep them on the working version until
  this fork has a green CI run on the new one.
- Look up the new versions on <https://fabricmc.net/develop>: Minecraft, Fabric Loader, Loom and
  Fabric API. Fabric API usually lands a few days after a Minecraft release.
- Skim the Fabric blog post for the release (<https://fabricmc.net/blog/>). It lists the big
  renames, and it's where most "why doesn't this compile" answers are.

## 1. Bump the versions

Edit `gradle.properties`:

```properties
minecraft_version=26.3          # -> new version
loader_version=0.19.5           # -> newest loader
loom_version=1.18-SNAPSHOT      # -> the Loom the Fabric page says
fabric_api_version=0.163.0+26.3 # -> the Fabric API built for the new version
```

Also bump `mod_version` (for example `2.1.1+fork.1`). `fabric.mod.json` picks up the Minecraft version
from here (`"minecraft": "~${minecraft_version}"`). If the new version needs a newer Java, update
`build.gradle` (`JavaVersion.VERSION_25`) and `java-version: 25` in `.github/workflows/build.yml`.

## 2. Build and fix, in this order

Push and let CI build. Each run writes its results to the `ci-results` branch (`latest.txt` for the
errors, `server-full.log` and `client-full.log`, and `shots/*.png`). Or run `./gradlew build` locally
with the right JDK.

Fix in roughly this order. Each layer hides the errors in the next:

1. **Compile errors.** Most are renames: a class moved package, a method was renamed or now takes a
   different argument. The decompiled Minecraft source (`./gradlew genSources`, then browse it in the
   IDE) is the reference. Search for the old name in the Fabric blog and Fabric API's changelog.
2. **Mixins** (`src/main/java/com/li64/tide/mixin/`, listed in `tide.mixins.json`). These compile
   fine and then fail when the game starts ("Invalid descriptor", "@Inject target not found") because
   the method they hook was renamed or changed signature. The server log in CI shows these within a
   few seconds of startup. Most fragile: `ItemsMixin` (replaces the vanilla rod), `ItemStackMixin`
   (rod durability), `MobBucketItemMixin`, `AbstractFishMixin`, `ItemMixin` (fish into buckets),
   `TidePlayerMixin` (where all of Tide's player data is saved).
3. **Access widener** (`src/main/resources/tide.accesswidener`, 19 lines). A field or method it
   opens up was renamed or removed; Loom fails the build and names the line.
4. **Data files** (`src/main/resources/data/`). Mojang changes JSON formats for recipes, loot tables,
   advancements and so on most releases. The server smoke test runs with `logDataErrors` on, so every
   file the game rejects is listed in `server.txt` with the reason. The vanilla data in the new
   client jar is the reference for the new format.
5. **Rendering and GUI** (`client/`). Render states, the GUI extractor (`GuiGraphicsExtractor`) and the
   render pipelines changed a lot in 26.x and will keep changing. The client smoke test screenshots
   (`shots/1-world.png` to `6-tooltip.png`) show whether the fish, the journal, the angler page and
   tooltips still draw.

What was ported for 26.3, to recognise the same kind of change next time:

| Area | 26.3 way |
|---|---|
| Ids | `Identifier` (was `ResourceLocation`) |
| Saving | `ValueInput` / `ValueOutput` instead of `CompoundTag` on entities and block entities |
| Registering | items, blocks and entities need `setId(ResourceKey)` on their properties |
| Food, armor, swords, fuel | components (`Consumables`, `humanoidArmor`, `sword`, `COOKING_FUEL`) |
| Item models | definitions in `assets/tide/items/*.json` |
| Entity and block entity rendering | render states, then `submit(...)` with a `SubmitNodeCollector` |
| GUI | `GuiGraphicsExtractor` (`text`, `blit(RenderPipelines.GUI_TEXTURED, ...)`), colours need alpha |
| Particles | `SingleQuadParticle` with `extract` |
| Keys | SDL key codes: use `KeyEvent.isEscape()/isLeft()/isRight()` and key mappings, never raw numbers |
| Villager trades, loot tables, recipes | data-driven, new JSON shapes (see `data/`) |
| Block items | `properties.useBlockDescriptionPrefix()` or they show `item.tide.x` instead of their name |
| Fabric API | `FabricCreativeModeTab`, `HudElementRegistry`, `PayloadTypeRegistry`, `ClientTooltipComponentCallback`, `ResourceLoader` |

## 3. Test before anyone updates

CI must be green **and** the screenshots must look right. Then, on a copy of the real world:

- Cast, play the minigame, land a fish: the tooltip shows quality, length and weight.
- Open the journal, the angler page (the "Lv" button) and a fish's page.
- Put a graded fish in a bucket, place it, bucket it again: it keeps its quality.
- Fish display, satchel, angling table, crafting a rod with line, bobber and hook.
- `/fishinglevel` and `/fishinglevel fair <you> true|false`.
- In Solstone: the Fishing tab, a player's fishing chip, and the Check page (it tries every command
  Solstone uses).

Then update the server and the clients together (all clients need the same jar as the server).

## 4. What Solstone relies on (don't change without changing Solstone)

Solstone reads Tide's data; it never writes to the server folder. If any of these change, change
`app/tide.py` in Solstone at the same time:

- **The catch log line.** One per caught fish, on the server thread:
  `[Tide catch] {"player", "uuid", "fish", "rarity", "quality", "quality_name", "length_cm", "weight_g", "level", "fair", "hook", "xp", "new_species", "dim", "x", "y", "z"}`
  (`Angler.logCatch`). Contests and the "Latest catches" feed come from these.
- **Player data**, in each player's save file under `TidePersistentData`:
  - `TidePlayerData.fish_data[] = {fish, data: {stats: {amount_caught, largest_catch, smallest_catch, best_quality, heaviest_catch, quality_counts}}}`
  - `TideAngler = {xp, fair, backfilled, version}`
- **The XP curve**, mirrored in Solstone: XP to the next level = `xpPerLevel + xpGrowth × level²`
  (`config/tide.json`, `server.leveling`), titles every 10 levels, Legendary at 99.
- **Commands**, run over RCON: `/fishinglevel fair <player> true|false`, `/fishinglevel reset <player> [everything]`.
- **Scoreboards:** `tide_level`, `tide_xp`.
- **Quality numbers:** 0 Poor, 1 Average, 2 Fine, 3 Prime, 4 Trophy, 5 Mythic.
- **The mod id** `tide` in `fabric.mod.json` (that's how Solstone finds the jar and its fish list:
  `data/*/fishing/fish/**/*.json` and `assets/tide/lang/en_us.json`).

## 5. Where the fork's own features live

| Feature | Code |
|---|---|
| Fishing levels, XP, titles, stats, fair play | `data/angler/Angler.java`, `data/commands/FishingLevelCommand.java` |
| Catch quality, size bands, odds | `data/angler/CatchQuality.java` |
| Weight from length | `data/angler/FishWeight.java` |
| Hook zones and quality in the minigame | `data/minigame/FishCatchMinigame.java`, `client/gui/overlays/CatchMinigameOverlay.java` |
| Angler page in the journal | `client/gui/screens/journal/AnglerCard.java` |
| Journal records (best quality, heaviest, counts) | `data/player/FishStats.java` |
| Quality and weight through buckets | `data/FishLengthHolder.java` and its three implementers |
| Units (inches, pounds, °F) | `util/TideUtils.java` (`useImperial`) |
