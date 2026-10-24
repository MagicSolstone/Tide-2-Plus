## Tide+ 1.0.7 - 10/23/26

Catches are easier to read. Install the same way: the jar in `mods/` on the server and every client (replaces 1.0.6).

## Changes
- **The catch card stays twice as long** (about 10 seconds instead of 5).
- **The catch line in chat says what you caught.** The old "+18 fishing XP · perfect hook!" line is now, for example, "Good Bluegill 3.4 lb · Perfect hook · +18 XP", with the fish in its quality's colour and the weight in gold. Still one line per catch, and only you see it. Hover it for the rest of the card: length, anything else that came up, and what your rod did.

## Tide+ 1.0.6 - 10/23/26

Another one for Solstone. Install the same way: the jar in `mods/` on the server and every client (replaces 1.0.5).

## New
- **`/fishing guide`** (ops and RCON): every piece of tackle in one reply: its name, kind, tooltip, the places and times its perks work, its rig, and its recipe or where it's found, plus all the rigs. Solstone uses it for the **Tackle guide** on the Fishing page.

## Tide+ 1.0.5 - 10/23/26

A small one for Solstone. Install the same way: the jar in `mods/` on the server and every client (replaces 1.0.4).

## New
- **`/fishing describe <item>`** (ops and RCON): sends an item's tooltip as text, the same lines you see in game, Tide's stats, perks and rigs included. Solstone uses it to show what an item does when you pick it to give to a player or as a challenge prize.

## Tide+ 1.0.4 - 10/23/26

A Rigs section in the journal and clearer quality names. Install the same way: the jar in `mods/` on the server and every client (replaces 1.0.3).

## Rigs in the journal
- The Tackle section of the fishing journal now ends with **Rigs** pages: all 12 rigs, four to a spread.
- Each rig shows its name, where or when it works, its pieces (blacked out until you've found them), and what **2 pieces** and **3+ pieces** on one rod give.
- Hover a piece for its name; click a found piece to open its tackle page.

## Quality names
- The middle qualities are renamed: **Fine is now Good** and **Prime is now Great**. The ladder is Poor, Average, Good, Great, Trophy, Mythic.
- Nothing about the odds or values changed. Fish caught before the update keep the name they were caught with.

## Tide+ 1.0.3 - 10/23/26

Bait gets a pass: three new baits for the newer systems, bait in every crate, and golden bait made rarer. Install the same way: the jar in `mods/` on the server and every client (replaces 1.0.2).

## New baits
Used up one per catch like all bait, so they're for the sessions that count.
- **Glistering Bait** (2 bait + glistering melon slice, makes 2): +25% fishing XP. The levelling bait.
- **Shiner Bait** (bait + any Tide fish + iron nugget, makes 2): +15% chance to land Good and Bad hooks and a wider hook window, so perfect hooks come easier.
- **Feast Bait** (2 bait + cooked fish + honey bottle, makes 3): +10% chance of a second fish (still under the 35% cap).

## Bait changes
- **Plain bait** has a second recipe with no mob drops: 2 seeds + a plant (crop, mushroom or small flower) + dirt makes 2. The original recipe (with rotten flesh and bone meal) still makes 4.
- **Golden bait** makes 2 per craft instead of 4: it's the strongest bait (+2 luck, +20% quality, rarer fish, a chance to bump the quality) and was cheap enough to use all the time.
- **Every fishing crate** has a 30% chance of a stack of plain bait (3 to 8) and a 5% chance of 1 to 3 special baits (golden, lucky or magnetic; End crates can also have moon bait).


---

## Tide+ 1.0.2 - 10/23/26

More ways to find tackle, and rigs that are easier to read. Install the same way: the jar in `mods/` on the server and every client (replaces 1.0.1).

## Finding tackle
- **Fishing crates** have a 15% chance of a piece of tackle that suits where they came from: lines, hooks, bobbers, lures or bait from lakes and rivers, ocean gear from the sea, cave gear from underground, fire gear from lava and the Nether, void gear from the End.
- **Fishermen** sell a little tackle as they level up: bait and silk line, then a feather bobber and dough balls, a fly lure and braided line, a popper lure, and at master level a golden spoon lure or a lucky hook. Vanilla fisherman trades are all still there.
- **Chests**: village fishermen's chests and shipwreck supply chests have a 1 in 4 chance of a piece of tackle, and Tide's fishing boat chests a little more.

## Rigs
- Every rig piece's tooltip says which rig it's part of ("Part of: Storm rig"), so you can see what pairs up without the angling table.
- A rod that counts toward a rig shows it: "Jungle rig 2/3: +5% better catch quality in jungles".

## Catches
- A **perfect hook** has a 5% chance to raise the catch one quality tier. The catch card says so when it happens.


---

## Tide+ 1.0.1 - 10/23/26

Fixes and polish after 1.0.0, mostly from the first round of family testing. Install the same way: the jar in `mods/` on the server and every client (replaces 1.0.0).

## Catches
- **The catch card** puts the weight first, in gold, right after the fish's name (length is next, as "35 in long"). Fish tooltips and big-catch announcements lead with the weight too.
- The card lists anything else that came up with the fish (honeycomb, saplings, cooked on the line) and, in purple, what your rod did: the hex rod's potion effect and how many crops the sickle rod grew.
- **Fish from crates** now get a quality and size, like any catch.
- Each fish's save now remembers the quality of its heaviest catch (Solstone shows it next to family records).

## Rigs
- **Two pieces** of a rig now give a small bonus; **three or more** give the full bonus, which is bigger than before (e.g. jungle: +5% quality at 2, +1 luck and +15% quality at 3). Pairs from different rigs both count.
- **The angling table** (and the tackle box's rig view) shows a card beside the rod for each rig it has pieces of: what it gives now, what the next piece adds, and which kinds of piece could still go on.
- Rig details moved off the item tooltips (they're in the journal and on the rig card), so rod tooltips stay short. The stats panel shows 2-piece rigs too.

## Crates
- Luck helps crates only up to luck 10 (about 1 catch in 10 at most), and crate-hunting gear (magnet lure, magnetic bait) is capped at about 1 in 5, so crates stay a treat.

## Storage and items
- **Fish satchel:** right-click it in your inventory to open it (taking fish out one at a time with right-click is gone).
- Found-rod tooltips are one short line each.
- "Bucketable" now reads "Alive (can go in a bucket)".

## Looks
- Rods' swaying lines no longer show a one-pixel gap.
- Blacked-out rods in the journal show one clean silhouette instead of a squashed animation strip.


---

## Tide+ 1.0.0 - 10/23/26

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


---

## Tide v2.1.1 - 8/03/26

- Fixed a crash when fishing with Starcatcher installed
- Added optional support for Starcatcher's fish data
- The server config now appears in the config screen
- Fixed a bug where the server config file would sometimes reset itself
- Re-added compatibility with Stardew Fishing Fabric
- Reduced the difficulty of Stardew Fishing minigames for Tide fish

## Tide v2.1 - 6/21/26

---
### Changes
**Accessories**
- Accessories and other items that modify fishing have been reworked to fill designated categories. The general rule of thumb is that hooks modify fish availability, lines modify the minigame, bobbers are cosmetic, bait modifies fishing stats, and fishing rods apply miscellaneous bonuses.
- Hooks, lines, and bobbers no longer have the text "Fishing" in their name. For example, the "Lavaproof Fishing Hook" is now called "Lavaproof Hook". This is also reflected in item IDs.
  - This eliminates the need for `accessory.item.tide` translations, instead using the direct item name

**Bait**
- Each fishing rod now has a variable number of bait slots, loosely corresponding to the rod's rarity
- Instead of applying the affects of only the bait item in the first slot, bait effects for all bait equipped in a fishing rod will be applied at once. This allows for bait "stacking", where you can combine the effects of multiple bait items.
- Multiple stacks of the same bait type cannot be equipped in the same fishing rod
- One bait item from each slot will be consumed upon catching a fish
- The effectiveness of higher levels of fishing speed has been slightly increased to counteract the `biteTimeMultiplier` of different fishing mediums

**Fishy Notes**
- Fish silhouettes are now shown in the fishy note GUI, helping you to identify which fish it might be.
- Reading a Fishy Note will now unlock it in the fishing journal under its corresponding fish.

**Items**
- Added 3 new fishing hooks:
  - Fiery Hook
  - Permafrost Hook
  - Twilight Hook
- Added 3 new fishing lines:
  - Copper Line
  - Iron Line
  - Diamond Line
- Added 6 new themed fishing rods:
  - Rod of Resonance (ancient city chests)
  - Prismarine Fishing Rod (drops from elder guardians)
  - The Suncatcher (rare catch in plains biomes)
  - Rod of the Hero (purchased from master fishermen villagers)
  - Beeline (rare drop from shearing beehives)
  - Blazing Fishing Rod (nether fortress chests)
- Added 2 new bait items:
  - Incandescent Bait
  - Abyss Bait
- Added Fish Finder (see below)

**Informational Items**
- Informational items such as the pocket watch and climate gauge now display their stats as a GUI overlay instead of requiring the player to right-click them
- The Lunar Calendar and Weather Radio can now be placed, showing their info when nearby the player
- Their information will be displayed when held in the player's mainhand, offhand, or equipped in a Curios/Trinkets slot.
- The Fish Finder can be created via a crafting recipe, allowing multiple stats to be viewed at once

**Compatibility**
- The Starcatcher minigame will be used instead of the Tide minigame if it's installed

**Config**
- The config has been split into a client and server file, with only the client file being changeable via the config screen
- Comments have been added to properties in the main config file
- NOTE: it's recommended to take a look at the new generated config file "tide.json5" and make sure that none of your config settings were lost (if you changed anything, they probably were).

**Miscellaneous**
- The Midas Fishing Rod can now reel in a bonus golden apple, golden carrot, and _very rarely_ an enchanted golden apple or totem of undying (in addition to the regular gold ingots and blocks)
- Caught fish will no longer become "unbucketable" after a certain amount of time
- Lowered Midas Fish luck requirement from 7 to 5
- Fish Displays will scale the fish up or down depending on its length

---
### Fixes
- Items crafted from netherite will no longer be destroyed when dropped in fire or lava [(#18)](https://github.com/Lightning-64/Tide-2/issues/18)
- Fixed a bug where removing a fish from a fish display wouldn't update for other players in the world [(#25)](https://github.com/Lightning-64/Tide-2/issues/25)
- Fixed a bug where ocean monument themed loot could not be obtained with YUNG's Better Ocean Monuments installed [(#47)](https://github.com/Lightning-64/Tide-2/issues/47)
- Fixed a bug where certain fish would despawn even if they were released from a bucket or given a custom name
- Fixed some conflicting conditions for the Sand Tiger Shark, it now only requires a desert biome
- Fixed many bugs related to the Fish Satchel

---

## Tide v2.0.3 - 2/06/26

---
### Changes
- Optional `associated_mods` property added to all fishing data files. If populated with mod IDs, the data file will only be loaded when all the specified mods are present.

---
### Fixes
- **Fixed a critical bug where fish with time conditions were uncatchable after Day 0**
- Fish caught with Fishing Real will now be properly logged in the journal on forge and neoforge
- Fixed a bug where eating the Voidseeker would separate the player's soul from their body

---

## Tide v2.0.2 - 1/19/26

---
### Changes
- Added a few new fish finding items
  - Added the Lunar Calendar for displaying the moon phase
  - Added the Depth Meter for displaying the depth below sea level
  - Added the Weather Radio for displaying upcoming weather conditions
- Renamed the Thermometer to "Climate Gauge"

---
### Fixes
- Fixed a bug where fish could not be used to tame cats on 1.20.1
- Fixed a bug where void fish would take fall damage while swimming
- Fixed a bug where fish could overflow a fish satchel when it was opened
- Fixed a periodic crash with ItemModelProperties on neo/forge
- Fixed a NoSuchMethodError crash on Fabric 1.20.1
- Fixed a server startup crash on Fabric 1.20.1
- Fixed a server startup crash on Forge 1.20.1

---

## Tide v2.0.1 - 1/13/26

---
### Changes
- Names of unlocked fish in the journal can be viewed by hovering over their icons
- Fish size data is now entirely optional instead of defaulting to predefined parameters
- Aquathorn can now be caught in the ocean monument structure from Better Ocean Monuments
- Improved display positioning of the Nether Depths Upgrade fish
- Added `tide:has_enchantments` fishing condition
- Added optional `show_in_journal` property to the fish data JSON format
- Added optional `nbt` property to the `display` section in the fish data JSON format
- Added a blacklist for auto-generated fish data in the config
- Added a blacklist for the enchanted pocket watch in the config
  - Also added the `tide:ignores_pocket_watch` entity tag, an additional blacklist
- Added a toggle for the Chasm Eel item's functionality in the config

---
### Fixes
- Fixed a crash when reeling in a fishing rod with Valkyrian Skies present
- Fixed a bug where the Starlight Bow's ammo conservation was applied to other weapons

---

## Tide v2.0 - Playtest 6

---
### Changes
- The alternate textures displayed for large fish in the journal are now data-driven (defined in the "journal" section of the fish data file)
- Catch dates are now displayed in the system date time format
- Text entries from the fishing stats section now have proper translation keys
- Pressing an arrow key while viewing a fish profile no longer kicks you out
- ItemFishedEvent is now called on both Forge and Neoforge (read-only)
- Added a period at the end of the smallmouth bass description
- Added an item tooltip for the new Chasm Eel item
- Added a weather info area for the Coelacanth
- Added a luck info area for the Midas Fish
- Fixed a texturing bug on certain crates when opened

---
### Known Issues
- Fish display animation issues with vanilla entities when using Fresh Animations
- Issues with some fish entities spinning really quickly?
- Animation issue with the fishing rods when using Hold My Items

## Tide v2.0 - Playtest 5

---
### Changes
- Added the Chasm Eel, a fish that allows the player to remove a single block of bedrock
- Reintroduced the Fish Satchel, a utility item for portable fish storage
- Escape or E can be pressed in the journal to go back or exit
- The height of the fishable void is now configurable per-dimension
- Fixed a crash when putting certain modded items in fish displays
- Fixed incorrect display data for Nether Depths Upgrade fish
- Semi-transparent entities now render properly on 1.20.1
- Chain effects of the pocket watch are no longer invisible on 1.20.1
- Limbs of enemies frozen by the pocket watch no longer shake on forge 1.20.1

---
### Known Issues
- Texturing bug on certain crates
- Fish display animation issues with vanilla entities when using Fresh Animations

---

## Tide v2.0 - Playtest 4

---
### Changes
- Added compatibility with Nether Depths Upgrade
- Added compatibility with Fishing Real
- Added compatibility with Fish of Thieves
- Fixed compatibility with Stardew Fishing
- Fixed compatibility with Hybrid Aquatic
- Fixed a rendering bug with emissive texture parts
- Fixed a rendering bug with dying entities frozen by the enchanted pocket watch
- Fixed a bug where length wouldn't display on bucket tooltips in 1.20.1
- Fixed some grammar errors in fish descriptions
- Fixed voidseeker not being able to be displayed
- Added some miscellaneous fish crafting recipes
- Gave additional eating effects for some fish
- The Midas Fishing Rod is now created via a smithing recipe
- Dragonfin Boots are now created via a smithing recipe
- The Netherite Fishing Rod now requires a netherite upgrade template
- Added "Override Vanilla Fishing Rod" config option
- Made the Sleepy Carp entity sleepier
- Snook is now caught on beaches

---
### Known Issues
- On 1.20.1, chain effects of the Enchanted Pocket Watch don't appear
- On forge 1.20.1, limbs of enemies frozen by the Enchanted Pocket Watch sometimes shake rapidly
- Fish satchel doesn't do anything
- Golden fishing line doesn't do anything

---

## Tide v2.0 - Playtest 3

---
### Changes
- Fixed a crash when using the Little Joys mod (and likely many others)
- Fixed several crashes upon dedicated server startup
- Changed the format of the `tide:found_in` fishing condition to mirror biome tag JSONs

---
### Known Issues
Same as previous

---

## Tide v2.0 - Playtest 2

---
### Changes
- Added support for 1.20.1 (forge and fabric)
- Added compatibility with Ecliptic Seasons
- Bait is no longer consumed when catching crates or loot items
- Added shooting star projectile to the `minecraft:arrows` tag
- Adjusted poses of the fish display to match the vanilla item frame
- Added a yellow highlight around newly discovered fish in the journal
- Removed unused convention tags from the data folder

---
### Known Issues
- Chain effects of the Enchanted Pocket Watch don't appear
- Limbs of enemies frozen by the Enchanted Pocket Watch on forge will sometimes shake rapidly
- I still don't have a name for the void fishing advancement
- Fish satchel doesn't do anything
- Golden fishing line doesn't do anything

---

## Tide v2.0 - 1/9/26

---
### Fishing Loot
- Fishing loot is now controlled by a custom data-driven system, consisting of the following:

**Fish Data**
- Fish catch conditions, rarities, rarity modifiers, journal data, minigame stats, display data, and more are now controlled using fish-specific JSON files located at `data/tide/fishing/fish`

**Crate Data**
- Crate catch conditions, rarities, rarity modifiers, and more are now controlled using crate-specific JSON files located at `data/tide/fishing/crates`

**Fishing Loot Data**
- External loot tables for fishing (such as the vanilla junk and treasure tables) can now be configured or added via JSON files located at `data/tide/fishing/loot`

More extensive documentation for these new data-driven formats will be available later after the full release.

---
### Fish
- Significant changes to the fish lineup have taken place. These include:
  - A few fish have been removed that either didn't fit with the mod's theme or were originally added to serve as filler content.
  - Added many new fish to discover throughout the world, bringing the total to around 100 fish in total.
- The vast majority of existing fish items were retextured (finally).
- Each fish can now be cooked and eaten, regardless of how healthy it might seem.
- Every single fish in the mod now has a corresponding entity version to go with it.
- Reworked the entity spawning system to be partially based off of the fish's Fish Data to automatically determine spawn conditions.
- Added natural spawning for each fish entity (except for a select few).
- By default, fish items can no longer be stacked up to 64 like before (see below, or look at the config tab titled "Items" for more information).

**Fish Lengths**
- Each fish you catch is now assigned a length in centimeters based on configurable parameters from the fish's Fish Data.
- Config options have been created to allow the user to disable this mechanic entirely, or show their extreme catches only in the journal and not affect item stacks.
- The fish's length will serve as an extra collectible aspect, but may affect other things (such as the quality of the fish food item if Quality Food is installed).

**Bucketable Fish**
- Each fish item you catch is now assigned a timestamp that is used to determine if the fish is "alive" or not.
- If the fish item is "alive", you can click on the fish in the inventory GUI with a water bucket (or lava bucket) in order to transform the items into a fish bucket item to allow access to the fish's entity form.
- If the fish is "alive" and bucketable, a tooltip saying "Bucketable" will be shown below the fish's name.
- Config options have been added so users can either never allow bucketable fish, allow them all the time, or change how long a fish item should be "alive" for after being caught.

---
### Fishing Journal
- Mechanics and visuals for the Fishing Journal have been reworked. Some of the major differences are listed below
- The fishing journal is now deeply integrated into the aforementioned "Fish Data" system to dynamically create profiles for any fish with a proper data file. Developers will no longer have to create separate data files for journal entries if a fish data file is defined.
- The fish "profile" GUI has also been overhauled with a fresh new layout.
- Removed the old "page" system in favor of categorizing fish more loosely. This also means that all fish, whether locked or unlocked, are visible in the journal GUI at all times.
- Additionally, many fish that were originally split up into smaller categories have been merged into corresponding larger categories.
- Fish can be assigned rarities, profile descriptions, and location summaries via the Fish Data system.
- **Simply collecting a fish item will no longer unlock its profile in the fishing journal!** Each fish you unlock _must_ be caught while fishing in order to unlock the profile.

---
### Mechanics
- The time it takes to catch a fish now also varies based on the type of liquid you're fishing in (ex. x1.0 for water, x1.6 for lava, x1.8 for void). This multiplier is applied before fishing speed modifications like bait or lure enchantments.
- Fish caught in specific biomes will be more lenient about where they are catchable. For example, if you're fishing in a river biome that cuts through a desert biome, you're still able to catch desert-themed fish.
- The temperature preferences of fish are no longer based on specific biomes. Instead, they are based on the internal temperature values from the worldgen noise router. This means that fishing in the middle of a desert may favor more warm fish than if you were to fish on the border between a desert and a more temperate biome.
- A few additional core mechanics have been added or modified:

**Minigame**
- The fishing minigame has received many tweaks, such as the following:
- The minigame no longer operates on a probability-based catch system, instead having two distinct zones on the minigame bar.
- The lighter zone in the bar will successfully catch the fish if clicked over, but the darker zone will cause the catch to fail and the minigame to end.
- Each fish has a configurable "strength" and "speed" parameter in their Fish Data that influences how much space the dark zone covers and how quickly the vertical bar moves, respectively.
- Not every fish will move the vertical bar in a "sine wave" pattern anymore, with some fish having much stranger movements. Stay alert!
- The fish's movement pattern is defined in its Fish Data as well, being set to one of the following:
  - `sine`
  - `plateau`
  - `jitter`
  - `darts`
  - `linear`
  - `linear_wrap`
- Additionally, the lighter bar will change color depending on what liquid you're fishing in (ex. blue for water, orange for lava, purple for void).

**Void Fishing**
- In order to fish in the void, you must first craft a Hook of the Depths and apply it to any fishing rod via the Angling Table.
- With the hook applied, fishing in the void operates as if the entire void is a fishable liquid. Note that the fishable void does not line up with the death barrier, and may extend significantly farther up in the world (ex. the End dimension)
- New fish have been added for both the End void and the void beneath the overworld bedrock.
- A few new particles have been added that are displayed on the fishable void surface.

---
### Items
- Added a few "profiling" items such as the thermometer and pocket watch, which can assist the user in finding the precise locations of fish when paired with the fishing journal or fishy notes.
- Most legendary tier fish have late-game craftable items, each filling a different role in player progression.
- Some other utility items were also added:

**Fish Satchel**
- Added the Fish Satchel, which is basically a bundle that can store fish.
- Right-clicking while holding the fish satchel will open or close the satchel. When open, any fish you catch or pick up from the ground will be automatically transferred to the fish satchel.
- This item should be especially helpful for dealing with the new unstackable fish.

**Fishy Notes**
- Pre-2.0 "Torn Notes" have been replaced by a new item, called the Fishy Note.
- Fishy Notes can be found in crates, with similar drop chances compared to the aforementioned Torn Notes.
- A Fishy Note for a specific fish can also be crafted using the desired fish and a single piece of paper.
- Each Fishy Note will display the catch conditions, rarity, and relative size of a single fish, usually one that has not been discovered yet.
- The conditions are displayed in the same manner as you would see them in the Fishing Journal, except the name and sprite of the fish are not visible.
- A checkmark will be displayed in the Fishy Note GUI if the fish it references is already discovered.

**Fish Display**
- The Fish Display is a placeable block that allows the user to mount a fish item to the wall for decoration purposes.
- Once a display is placed, right-clicking it with any fish item that has display data defined will display the entity version of the fish item on the wall. Right-clicking again will retrieve the item stack.
- Display data such as rotation and offsets are defined in the Fish Data files.

---
### Miscellaneous
- Fishing rods can now be repaired at an anvil using string in the same way that vanilla tools and armor are upgraded.
- Most of the old advancements have been renamed, and many new advancements have been added.
- Two new utility commands have been added, `/journal` and `/fishing`.
- Removed the old fishing boat structure, I don't think it's really necessary.
- Many item and block IDs have been changed to reflect updated English translation names. For example, the Angling Table (previously having an ID of `tide:angler_workshop`) has been renamed to `tide:angling_table`. **This may cause issues if Tide 2 is loaded on a world that was previously loaded with Tide 1!**

I won't list bug fixes here because so many things got reworked, and it's likely that many old bugs are now patched.