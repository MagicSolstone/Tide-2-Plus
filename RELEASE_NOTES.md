# Tide+ 1.0.0

The first full release of **Tide+**, a Fabric fork of [Tide 2](https://github.com/Lightning-64/Tide-2) (2.1.1) by Lightning64 for **Minecraft 26.3**. Everything Tide does is still here; Tide+ adds a fishing level, catch quality, and a whole tackle system on top.

**Install:** put the jar in `mods/` on the server and every player's client, with Fabric Loader 0.19.5+ and Fabric API for 26.3. It replaces Tide (same mod ID, so worlds and items carry over); don't install both.

## Fishing levels
- Levels 1 to 99 from fishing XP, with a new title every 10 levels. Level raises cast distance, bite speed, hook zones, luck and makes fish fight a bit easier; level 40 fishes like original Tide.
- A new title gets the big on-screen announcement and a firework; ordinary level-ups show on the catch card. Level-ups are announced in chat.
- `/fishinglevel get | set | addxp | reset [everything] | fair` for ops. *Fair play* puts everyone at level 40 for contests.

## Catches
- **Catch quality**: Poor, Average, Fine, Prime, Trophy, Mythic, decided at the bite. Better fish are bigger, heavier and fight harder; Trophy and Mythic catches are announced to everyone. Fish from crates get a quality too.
- **Hook zones** in the minigame: perfect, great, good, bad, miss, each with its own chance to land the fish. A perfect hook gives extra XP.
- Every fish has a length and a weight (US units by default, `useImperial` in the client config).
- **Catch card** above the hotbar for five seconds: the fish in its quality colour, size, anything special (new species, new record, two on the hook, perfect hook) and anything else that came up with it, plus the XP. Chat keeps just the XP line.
- **Favorite lures**: every fish has one, and it's twice as likely to bite on it. The lure's tooltip names the ones you've caught; the rest you find by fishing. Each cast tells you how many fish there favor your lure.
- The journal records best quality, heaviest, longest and shortest of each fish, and shows each fish's favorite lure.

## Tackle
Rods hold a line, hook, bobber and lure, plus bait. Each piece does something, and many work best in a place, time or weather (shown in purple).
- **31 rods**: Tide's 13 plus a kid's rod, bamboo, bone, copper, amethyst, mangrove, cherry, frost, cactus, conduit, clockwork and wayfinder rods, and six rare **found rods**:
  - Rotten (zombies and husks): fishes up mob drops
  - Widow (spiders): strong at night, sluggish by day
  - Drowned (drowned): fishes up sunken loot
  - Hex (witches): a chance of a helpful potion effect
  - Sickle (harvested wheat): makes nearby crops grow
  - Grove (leaves): fishes up saplings, sticks and apples
- **18 lines** (any line can be dyed any colour: line + dye), **23 hooks** (upgrades made on a base hook keep its perks), **33 bobbers** (made from sticks, a plank and the item that gives them their look), **15 lures**, and **22 baits** including region baits.
- **Lava fishing**: the lavaproof hook, blazing and netherite rods, netherite line and bobber, fiery hook and magma lure all have lava perks.
- **Matched rigs**: three or more pieces of one family (ocean, river, night, twilight, storm, swamp, jungle, desert, frost, cave, fire, void) add a bonus where that family is made for.
- **Rod stats panel** at the right edge while a rod is in hand: everything your rod, tackle, gear and level add up to, and which perks are working right now.
- **Angler's gear** (bucket hat, vest, waders, wading boots) with a full-set bonus.
- Breaking a rod drops what was on it. Rods' hanging lines sway in the hand and inventory, and the glow hook lights the water around it.

## Storage
- **Tackle box**: 27 slots of tackle plus a rack for 3 rods. A *Rig a rod* button (or sneak + right-click) opens an angling table that uses the box's tackle directly.
- **Fish satchel**: right-click it in your inventory (or sneak + right-click in hand) to see all 64 places and take out the fish you want. Right-click in hand still turns fish collecting on and off.

## Journal
- A **Tackle** section: every piece of tackle, blacked out until you find it, with what it does and its recipe. Recipes appear in the recipe book once you hold a related item.
- An **Angler** page with your level, title and stats.

## For server admins
- A `[Tide catch]` line in the server log for every fish (Solstone reads it for contests and the Fishing tab).
- Settings are plain JSON in `config/tide.json`; no Cloth Config or Mod Menu needed.
- Fabric only, 26.x only; third-party compat from Tide (Stardew Fishing, Starcatcher, seasons mods) isn't included.

Licensed MPL-2.0, like Tide. Credit for Tide itself, its fish, art and mechanics goes to Lightning64.
