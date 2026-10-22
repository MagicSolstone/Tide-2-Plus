# Tide (Fabric 26 fork)

A Fabric-only fork of [Tide 2](https://github.com/Lightning-64/Tide-2) by Lightning64, ported to
**Minecraft 26.3**, for a family server run alongside [Solstone](https://github.com/TJusczak/Solstone).

Added on top of Tide:

- **Fishing levels 1–99** with XP, titles every 10 levels and an angler page in the journal. Level
  changes cast distance, bite speed, hook zones, luck and how hard fish fight; level 40 fishes like
  original Tide. `/fishinglevel` shows yours; ops can set levels and turn on *fair play* (everyone
  at level 40, for contests).
- **Hook zones** in the minigame (perfect, great, good, bad, miss) with a chance to land each.
- **Catch quality** (Poor, Average, Fine, Prime, Trophy, Mythic), decided at the bite: better fish
  are bigger, heavier and fight harder. Shown with ◆ marks, kept through buckets.
- **Weight** from length and body shape; US units by default (`useImperial` in the client config).
- **Journal records:** best quality, heaviest, longest, shortest, and how many of each quality.
- A `[Tide catch]` line in the server log for every fish, which Solstone reads for contests.

Porting to a new Minecraft version: see [PORTING.md](PORTING.md).

- **Fabric only, 26.x only.** Forge, NeoForge, 1.20.1/1.21.1 and the Stonecutter multi-version setup
  were removed (the sources were baked to their 1.21.1 Fabric form, then ported).
- **No third-party compat** (Stardew Fishing, Starcatcher, seasons mods, Trinkets/Curios, Fishing Real,
  Hybrid Aquatic). The hooks in `CompatHelper` remain as no-ops.
- **No Cloth Config / Mod Menu:** settings are plain JSON in `config/tide.json`.
- **Builds and tests on GitHub Actions** (`.github/workflows/build.yml`). Each push builds the jar, boots
  a dedicated server with it, then the real client on a virtual screen (it loads a test world, casts the
  rod and opens the journal). Logs and screenshots go to the `ci-results` branch; a jar that builds goes
  to the [`dev` release](https://github.com/TJusczak/Tide-2-fabric26plus-levels/releases/tag/dev).

**Install:** put the jar in `mods/` on the server and every client, with Fabric Loader 0.19.5+ and
Fabric API for 26.3. Worlds/players that had Tide data from 1.21.1 aren't a goal of this fork.

What changed for 26.3, roughly: renderers use render states, GUI drawing uses the new extractor API
(journal silhouettes have their own render pipeline), item models are defined in `assets/tide/items/`,
armor/swords/food/fuel/trades are components or data, and the data files use 26.3's formats.

Licensed MPL-2.0, like upstream. Original README below.

---

# Tide-2
A Minecraft mod that expands the fishing experience, adding many new fish, mechanics, and rewards.

Join the discord server if you have a question, feedback, or want to see what's happening!

[![Discord](https://img.shields.io/discord/1306794817396805712?style=for-the-badge&logo=discord&logoSize=0&label=discord&labelColor=000000&color=3067bf)](https://discord.gg/773R6cruH6)

---
## The Mechanics

To get started, craft a fishing rod. This can be either the regular fishing rod from the base game, or one of the new fishing rods added by Tide.

<p align="center">
<img src="https://cdn.modrinth.com/data/cached_images/0e488efb856acb81e451706532c90278634cc230.png" alt="fishing-rods"/>
</p>

Cast your line by holding right-click and releasing, then wait patiently for a fish to arrive. Fish not biting? Try crafting some bait to increase your fishing speed or provide special bonuses.

Tide also adds a simple fishing minigame for each fish you catch. Right click when the bar is inside the colored region to reel in the fish, but be careful not to lose it! Each fish has its own unique movement behavior and strength, so stay alert.

<p align="center">
  <img src="https://cdn.modrinth.com/data/cached_images/b2f81cc4bf8c0a9e96dfc15ddf25397e9b63c029.png" alt="Minigame 1" width="380"/>
  <img src="https://cdn.modrinth.com/data/cached_images/9fe368bf906dabc716263867b3b3b5b847e3a0cf.png" alt="Minigame 2" width="380"/>
</p>

_Some fish are easy to catch, while others aren't..._

---
## Fishing Rod Customization

You can add hooks, lines, and cosmetic bobbers to any fishing rod using a new block, the **Angling Table**.

A variety of accessories can be created with unique effects. Some of the bonuses include easier catches, extra luck, or even **lava and void fishing**!

(or making your fishing rod look cool, in the case of the bobbers)

<p align="center">
  <img src="https://cdn.modrinth.com/data/cached_images/c4f1cde91876c4935727ae43e184b6a8320e6f56_0.webp" alt="Angling Table GUI" width="580"/>
</p>

_You can create any combination of hooks, lines, and bobbers at the Angling Table_

---
## The Journal

Keep track of all the fish you’ve caught via the **fishing journal**, a special book that gets updated every time you catch a fish. You can use it to track your completion progress, biggest catches, fish habitats, and much more.

<p align="center">
  <img src="https://cdn.modrinth.com/data/cached_images/de364f376cc0126704b46c0e8ca050340fdea33c.png" alt="Fishing Journal" width="380"/>
  <img src="https://cdn.modrinth.com/data/cached_images/c0bd594fb53af8d08864ddcf0b56ae5774a9f19a.png" alt="Salmon Profile" width="380"/>
</p>

_Can you catch them all?_

Want to know where you can catch a fish? You can find **fishy notes** in crates that hint at the locations of individual fish.

---
## Customizability
Many aspects of the mod can be changed to your liking.

**Need to tweak something?**
You might be able to find what you need in the config screen.

**Looking to add a fish or modify existing ones?**
Tide has a versatile **fish data** system, allowing mod or datapack developers to add their own fish to find in the world via JSON files.

In addition to fish, you can also use datapacks to insert custom loot tables or crates to the fishing loot. Need help customizing something? Feel free to ask in the discord server.

---
## Compatibility
Tide is compatible with many amazing mods, including:
- Hybrid Aquatic
- Nether Depths Upgrade
- Stardew Fishing
- Fishing Real
- Fish of Thieves
- Serene Seasons
- Fabric Seasons
- Ecliptic Seasons
- …and more!
