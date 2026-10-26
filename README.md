# Tide+

**Tide+** builds on **[Tide 2](https://github.com/Lightning-64/Tide-2) by Lightning64**, the fishing overhaul that
adds over a hundred fish, lava and void fishing, the fishing minigame, the Angling Table, crates and the fishing
journal. Tide was the launching point: everything it does is still here, and every fish, model and texture from Tide
is Lightning64's work. On top of it, Tide+ adds fishing levels, catch quality, a full tackle system and a workbench to
build it with, made for a family server. Licensed MPL-2.0, like Tide.

**Minecraft 26.3, Fabric.** Put the jar in `mods/` on the server and every client, with Fabric Loader 0.19.5+ and
Fabric API. It uses Tide's mod ID and replaces Tide (worlds and items carry over), so don't install both.
Downloads are on the [releases page](https://github.com/MagicSolstone/Tide-2-Plus/releases); what changed in each
version is in the **[CHANGELOG](CHANGELOG.md)**.

---

## What Tide+ adds

### Fishing levels
- **Levels 1 to 99** from fishing XP, with a new title every 10 levels (Novice Angler to Legendary Angler). A new title
  gets a big on-screen announcement and a firework; level-ups are announced in chat.
- **Your level is your skill**: longer casts, quicker bites, wider hook zones, a little more luck, and calmer fish.
  Level 40 fishes exactly like original Tide.
- **Rare fish stay hard.** A high level calms common fish, but rare, very rare and legendary fish always fight at least
  as hard as in original Tide. To take on the hard ones, bring the right tackle.
- **XP for every catch**, more for rarer fish, better quality, a new species, your own record or a perfect hook. The
  catch card shows where it came from: `+13 Fishing XP (+3 perfect hook)`.
- **Fair play** (`/fishinglevel fair`) puts a player at level 40 for contests.

### Catch quality and size
- **Every fish has a quality**: Poor, Average, Good, Great, Trophy or Mythic, decided when it bites. Better fish are
  bigger, heavier and fight harder. Trophy and Mythic catches are announced to the whole server (everyone hears a
  Mythic).
- **Weight is what counts.** Each quality is its own bell curve of sizes, and they overlap: a Mythic can be small and an
  Average fish can, now and then, be a whopper. There is no hard top size, so no record is ever unbeatable.
- **You can see quality at a glance**: a coloured dog-ear on the icon, a dull look for Poor fish, a shimmer for Trophy
  (purple) and Mythic (gold), and the name in its colour.
- **Cooked fish look like the fish they were**, for every species.
- **Resource fish give more when they are better** (an iron tetra makes more raw iron as a Trophy than as a Poor fish).

### The catch
- **Hook zones**: the minigame bar has Perfect, Great, Good, Bad and miss zones, each with its own chance to land the fish.
  A perfect hook earns extra XP and sometimes raises the fish a tier (never to Mythic).
- **The catch card** above the hotbar: the fish in its quality colour, its weight, anything special (new species, your
  record, a server record, two on the hook) and what came up with it. A short line in chat keeps it; hover it to see
  how it compares with the server record.
- **Caught fish arc down to your feet**, however far you cast.

### Server records
- The server keeps the **heaviest fish of each kind**: who caught it, the weight and the quality, shown in each fish's
  journal page. Beating one is announced to everyone with a gong.

### Rods and tackle
A rod holds a **line, hook, bobber and lure**, plus **bait**. Every piece does something, and many are made for a place,
a time or the weather.
- **31 rods**, from the kid's rod to netherite and the wayfinder, including six rare **found rods** that drop from mobs,
  wheat and leaves, each with its own trick (fishing up mob drops, sunken loot, saplings; growing crops; potion effects).
- **18 lines, 23 hooks, 33 bobbers, 15 lures and 25 baits.** Hooks come in **Tier 1** (made from raw materials) and
  **Tier 2** (made on a Tier 1 hook, keeping its perks). Any line can be dyed.
- **Tackle with tricks**, for example: the stone hook drags up seaweed, the emerald hook the odd emerald, the barbed hook
  lands almost anything but tears the fish, the heart bobber heals you, the lichen and glow pieces light the water,
  the crystal rod tells you what bit and how good it is, the village rod gives nearby anglers extra XP, and the
  wayfinder rod grows stronger the farther you are from spawn.
- **Plain numbers you can compare.** Every stat is a percentage: cast range, bite speed, hook window, rare finds, catch
  quality, fishing XP, landing, fish fight, catch difficulty, second fish, comes up cooked. **Bite speed** and
  **rare finds** stack with diminishing returns, so a great rod is better than a good one but never instant.
- **The rod panel** on the right while you hold a rod: your level and progress, the stats your rod, tackle, gear and
  level change, its rigs (in colour when they are working) and the times, places and weather its pieces care about
  (bright when they apply, grey when they don't).
- **Rods never break**: a worn-out rod stops at broken until you repair it, keeping its tackle and enchantments.
- **Angler's gear** (bucket hat, vest, waders, wading boots) with a full-set bonus.

### Rigs
Pieces made for the same place or time form a **rig**. **Tier 1** (2 pieces) gives a small bonus, **Tier 2** (3 or more)
the full one. Each piece belongs to one rig, and a rod carries at most **three rigs** (three only ever as three Tier 1s: six slots, bait included).
- **Place and time rigs**, strongest where they belong: Ocean, River, Night, Twilight, Storm, Swamp, Jungle, Desert,
  Frost, Cave, Fire (lava) and Void.
- **Sky rigs** that follow the sky: the **Sunlight rig** bites faster toward noon and brings up seeds; the **Moonlight
  rig** grows toward a full moon, finds rare fish and better quality, and brings up amethyst.
- **Material rigs** that work anywhere, each with a twist: **Stone** (the starter rig: plain tackle, quicker bites and a rod that barely wears), **Iron** (the rod fights like
  a sword), **Gold** (XP and gold nuggets), **Diamond** (catch quality), **Honey** (honeycombs and honey bottles) and
  **Lucky** (rare finds).
- **Each rig has a colour**, used for its name, its card at the Angling Table and the rod panel.

### Bait
- **Region baits** let a region's fish bite anywhere; **session baits** boost XP, landing, quality or a second fish.
- **Cut bait remembers the fish it was cut from**: the better the fish, the more of your bites are rare or rarer, up to
  every bite with Mythic cut bait. Poor and Average fish finally have a use.
- **Chum the water**: a hook with chum on the rod chums the water around it for two minutes, and everyone fishing there
  bites faster.
- **Favorite lures**: every fish has one and is twice as likely to bite on it. Each cast tells you how many fish there
  favor your lure.

### The Angling Table: a fishing workbench
- **All fishing gear is made here**: a 3x3 grid like a crafting table, plus rod upgrades (rod, upgrade and extra in any
  arrangement).
- **A fishing recipe book** beside the grid, filtered by kind or by rig. Recipes are **discovered** from their materials.
  Click one to fill the grid; if you are short, it shows what it takes.
- **The Assemble tab** puts a line, hook, bobber and lure on a rod, with a card for each rig the rod has pieces of: its
  colour, its tier, what it gives now and what the next piece adds.
- Dressed as a proper tackle bench: a wooden frame, brass corners, and labels and outlines for each slot.

### Storage
- **Tackle box**: 27 slots of tackle and a rack for three rods, with an *Assemble rod* button to rig a rod straight from
  the box.
- **Fish satchel**: 64 slots of fish; open it to take out the one you want.

### Finding tackle
- **Fishing crates** hold tackle, string and bait now and then: basic pieces often, golden ones rarely, and a diamond or
  netherite piece only once in a long while. Village fishermen, shipwrecks and Tide's fishing boats have a little too.
- **Fishermen sell tackle** as they level up.

### The journal and the wall
- A **Tackle** section in the journal (everything you have found, what it does, its recipe and its rig), an **Angler**
  page (level, title and stats), and each fish's best quality, heaviest catch and server record.
- **Mounted fish** are sized by their weight; sneak and look at one to see its name, quality and weight.

### For server admins
- **Commands** (ops and RCON): `/fishinglevel get | set | addxp | reset [everything] | fair`, `/fishing describe <item>`,
  `/fishing guide`.
- A **`[Tide catch]` line in the server log** for every fish, a `[Tide record]` line for every record, and the
  `tide_level` / `tide_xp` scoreboards. Solstone, the server's companion app, reads these for contests and its Fishing
  page.
- **Settings** are plain JSON in `config/tide.json` (levels, the XP curve, quality, crates and more). No Cloth Config or
  Mod Menu needed.

---

## Differences from Tide

- **Fabric only, Minecraft 26.x only.** Forge, NeoForge and older Minecraft versions were dropped.
- **No third-party compatibility** (Stardew Fishing, Starcatcher, seasons mods, Hybrid Aquatic and others).
- Fishing gear is crafted at the Angling Table instead of the crafting table.

## Building and contributing

- **Every push is built and tested on GitHub Actions** (`.github/workflows/build.yml`): it compiles the jar, checks every
  item has its art and name, starts a real server and a real client on a virtual screen, fishes, opens the journal and
  the table, and checks every piece of tackle and many thousands of tackle combinations. A new version number on `main`
  publishes a release.
- **[CODE_GUIDE.md](CODE_GUIDE.md)**: where everything lives, and how to change numbers or add a hook, rig or setting.
- **[PORTING.md](PORTING.md)**: moving to a new Minecraft version, and what Solstone relies on.

## Credits

**Tide** and everything it brought (its fish, entities, art, the minigame, the Angling Table, crates and the journal) is
by **Lightning64**: [Tide 2 on GitHub](https://github.com/Lightning-64/Tide-2) and its
[Discord](https://discord.gg/773R6cruH6). Tide+ would not exist without it. Licensed under the
[Mozilla Public License 2.0](LICENSE.txt).
