# Tide+ code guide

A map of the mod for someone who wants to read it, change numbers, or add things. It points at real files and
methods, so search for the names. (`PORTING.md` is the companion for moving to a new Minecraft version and for what
Solstone relies on.)

All paths below start at `src/main/java/com/li64/tide/` (code) or `src/main/resources/` (data and art).

---

## 1. The life of one catch

Follow a fish from cast to journal and you will have visited most of the mod.

| Step | What happens | Where |
|---|---|---|
| Cast | Holding right-click charges the rod; letting go throws the hook. Level, line and rod add cast range. | `registries/items/TideFishingRodItem.java` (`castHook`) |
| Wait for a bite | The hook sits in water; a timer counts down. Lure/bait speed, rod perks and fishing level shorten it. | `registries/entities/misc/fishing/TideFishingHook.java` (`catchingFish`) and `data/rods/RodEffects.java` (`lureWaitFactor`) |
| Choose the fish | The fishing context (biome, time, weather, bait, lure...) picks a loot entry. | `TideFishingHook.selectCatch`, `data/fishing/`, `data/TideFishingManager.java` |
| Bite and quality | When a fish bites its quality (Poor..Mythic) is rolled, which makes the fight harder. | `data/minigame/FishCatchMinigame.java` (constructor), `data/angler/CatchQuality.java` |
| Minigame | The bar on screen; the click lands in a zone (Perfect/Great/Good/Bad/Terrible). | `client/gui/overlays/CatchMinigameOverlay.java`, `FishCatchMinigame.handleHook`, `Angler.Zones` / `Angler.Hook` |
| Landing | Win or lose. A perfect hook can bump the quality one tier. | `FishCatchMinigame.onWin` |
| Reel in | The fish becomes an item thrown at the angler; bonus drops from the rod roll here too. | `TideFishingHook.retrieve(rod, level, player)` and `pullToward` |
| Size and grade | Length, weight and quality are written onto the fish item. | `CatchQuality.apply`, `data/angler/FishWeight.java` |
| XP | The catch gives XP, with a breakdown for the card. | `Angler.onFishCaught` |
| Records and journal | Server record check, then the player's journal entry. | `data/angler/ServerRecords.java`, `data/player/TidePlayerData.java` |
| The card | Sent to the player: the card above the hotbar plus a line in chat. | `Angler.banner`, `client/gui/overlays/CatchBannerOverlay.java` |

## 2. The map

```
Tide.java                       the entry point and shared singletons (config, network, logger)
loaders/fabric/                 Fabric glue: entry points, events, loot-table edits
registries/                     everything the game registers
  TideItems.java                every item (and the creative tab at the bottom)
  TideFish.java                 every fish and its entity
  items/                        item classes (rods, hooks, tackle box, satchel...)
  blocks/ entities/             blocks (angling table, fish display) and their block entities
  entities/misc/fishing/        the fishing hook entity (the heart of a cast)
data/
  rods/                         TACKLE: RodEffects (all perks), RigSets (rigs), CustomRodManager (what is on a rod)
  angler/                       LEVELS AND CATCHES: Angler (XP, levels, catch card), CatchQuality, FishWeight, ServerRecords
  minigame/                     the catch minigame (server side)
  tackle/                       the Angling Table: TackleRecipes (grid recipes), TackleUnlocks (discovery), TackleLog (journal tackle page)
  fishing/                      which fish bite where: data classes for the JSON in resources/data/tide/fishing
  player/ journal/              the player's journal and stats
  commands/                     /fishing and /fishinglevel
client/                         everything that only exists on the player's screen
  gui/overlays/                 on-screen extras: rod panel, catch card, minigame, display tip
  gui/screens/                  Angling Table, tackle box, satchel, the journal
  gui/menus/                    the server-side menu logic behind those screens
network/messages/               every packet (one small class each)
config/                         tide.json settings (TideServerConfig / TideClientConfig)
mixin/                          small patches into vanilla code (rods never break, item stats...)
ci/GearCheck.java               a self-test that runs on the CI server (see section 6)
```

Data you edit without touching Java lives in `src/main/resources/`:

| Folder | What it is |
|---|---|
| `data/tide/recipe/` | crafting recipes (the table's grid uses these) |
| `data/tide/tags/item/` | item groups (`hooks.json`, `lines.json`, `fishing_rods.json`...). **A new tackle item must be in its tag.** |
| `data/tide/bait/` | bait speed and luck numbers, one file per bait |
| `data/tide/rod_accessories/` | how a line/bobber/lure is drawn on the rod (colour, width) |
| `data/tide/fishing/` | which fish and loot can come up where |
| `data/tide/advancement/recipes/` | recipe-book unlocks (generated, see section 6) |
| `assets/tide/lang/en_us.json` | the English names |
| `assets/tide/items/`, `models/`, `textures/` | how items look |

## 3. "I want to change..." (the cheat sheet)

| I want to change | Edit | Notes |
|---|---|---|
| What a hook/line/bobber/lure/rod gives always | `data/rods/RodEffects.java`: the `RODS`, `HOOKS`, `LINES`, `BOBBERS`, `LURES`, `BAITS` tables | One line per item. `bite(0.10)` is +10% bite speed. The helper names (`bite`, `window`, `luck`, `quality`, `xp`, `land`, `fight`, `cast`, `ease`, `upgrade`) are defined near the bottom of the file. |
| What an item gives only in a place or time | same file, the `SITUATIONAL` table | `new Perk(When.NIGHT, bite(0.25))`. The places and times are the `When` list. |
| What a rig gives | `data/rods/RigSets.java`: `SETS`, and the shapes (`QUALITY_2`, `LUCK_QUALITY_3`...) above it | Each rig lists its members by item name. 2 pieces give the small bonus, 3 the full one. |
| How fast lure/bait speed shortens the wait | `RodEffects.lureWaitFactor` | One formula. 6 / (speed + 6), never below 25%. |
| The minimum time to a bite | `TideFishingHook.catchingFish` (`Math.max(..., 30)` ticks) | 20 ticks = 1 second. |
| XP per rarity, new species, records | `Angler.rarityXp`, `Angler.NEW_SPECIES_XP`, `Angler.RECORD_XP`, bonuses in `Angler.onFishCaught` | |
| The XP curve | `config/tide.json` (`leveling`), the maths in `Angler.xpToNext` | Solstone mirrors this: see PORTING.md section 4. |
| What each fishing level gives | `Angler.Stats.of` | `curve(level, atLevel1, atLevel99)`; level 40 is 1.0 (original Tide). |
| Quality odds | `CatchQuality` (the weights at the top, `roll`) | |
| The chance a perfect hook raises the quality | `FishCatchMinigame.PERFECT_BUMP` | |
| How big the minigame zones are | `Angler.Zones.of` | |
| Server record announcement and effects | `ServerRecords.announce` | sound, chat line, particles |
| The text on the catch card | `Angler.banner` (what goes on it), `CatchBannerOverlay` (how it is drawn) | A line starting `*` is purple, `~` yellow, otherwise white. |
| The rod stats panel on the right | `client/gui/overlays/RodStatsOverlay.java` | |
| Which hooks are Tier 1 / Tier 2 | `RodEffects.BASE_HOOKS` / `UPGRADED_HOOKS` | |
| Iron rig sword damage | `RigSets.refreshMelee` | +3 at two pieces, +5 at three. |
| Where fish land when reeled in | `TideFishingHook.pullToward` | |
| Angling Table screens | `client/gui/screens/AnglingCraftScreen.java` (Craft tab and recipe book), `AnglingTableScreen.java` (Assemble tab), `TackleBoxScreen.java` / `TackleRigScreen.java` | |
| A recipe | `data/tide/recipe/<name>.json` | Then run `python3 ci/recipe_unlocks.py` to add its book unlock. |

## 4. Recipes for common jobs

### Add a new hook (the same steps apply to lines, bobbers, lures and bait)

1. **Register it** in `registries/TideItems.java`: `public static final Item MY_HOOK = register("my_hook", FishingHookItem::new);` (lines use `FishingLineItem`, bobbers `FishingBobberItem`, lures `FishingLureItem`). Also add it to the creative-tab list at the bottom of that file.
2. **Tag it**: add `"tide:my_hook"` to `data/tide/tags/item/hooks.json` (lines to `lines.json`, and so on). Without the tag it is not tackle: the table, journal and tackle box will ignore it.
3. **Give it a name and look**: a line in `assets/tide/lang/en_us.json`, an item definition in `assets/tide/items/my_hook.json`, a model in `assets/tide/models/item/` and a texture in `assets/tide/textures/item/`. Copy an existing hook's files and rename. Hooks also need a small in-water texture (the asset check tells you which).
4. **Give it perks**: an entry in `RodEffects.HOOKS` (and `SITUATIONAL` if it has place-and-time perks). If it is made on top of another hook, add it to `UPGRADED_HOOKS`; if it is made from raw materials, `BASE_HOOKS`.
5. **Make it obtainable**: a recipe in `data/tide/recipe/my_hook.json`, then `python3 ci/recipe_unlocks.py`. Things found rather than crafted go in `TackleLog.FOUND` with a note about where.
6. **Optionally put it in a rig**: add its name to a rig's list in `RigSets.SETS`.
7. Run `python3 ci/check_assets.py` (it lists anything missing), then push and let the build tell you the rest.

### Add a rig

In `RigSets.SETS` add a line: `set("Name rig", When.SOMETHING, TWO_PIECE_BONUS, FULL_BONUS, "member_one", "member_two", ...)`. Use `When.ALWAYS` for a rig that works anywhere. A rig counts at most one piece per kind (rod, line, hook, bobber, lure, bait), so it needs members of at least three different kinds to ever reach three pieces. The recipe book, rod tooltips, the rod panel and `/fishing guide` pick it up automatically.

### Add a fish

Fish are the biggest part of the code (`registries/TideFish.java`, entity models, loot JSON in `data/tide/fishing/fish/`). The simplest route is to copy an existing fish of the same kind (salt/fresh/lava) across all of those places. The build's asset check and server start-up will complain about whatever you forgot.

### Add a setting

Server settings are fields in `config/TideServerConfig.java`; the player edits them in `config/tide.json`. Read one with `Tide.SERVER_CONFIG.<group>.<field>`.

## 5. Where 1.0.17 and 1.0.18 landed

| Change | Files |
|---|---|
| Angling Table (Craft tab, recipe book, discovery) | `client/gui/menus/AnglingCraftMenu.java`, `client/gui/screens/AnglingCraftScreen.java`, `data/tackle/*`, `network/messages/Tackle*Msg.java`, `BenchTabMsg.java` |
| Tab switching keeps the cursor | `client/MouseKeep.java`, used by the four screens' buttons |
| Catch card (XP breakdown, bumps) | `Angler.onFishCaught`, `Angler.banner`, `Angler.markBump`, `CatchBannerOverlay` |
| Perfect hook / tackle quality bumps | `FishCatchMinigame` (constructor and `onWin`), `Angler.markBump` |
| Softer lure speed | `RodEffects.lureWaitFactor`, `TideFishingHook.catchingFish`, `RodStatsOverlay` |
| Iron rig, Circle and Treble hooks | `RigSets` (`SETS`, `refreshMelee`), `RodEffects.HOOKS`, `CustomRodManager.setAccessory` |
| Fish landing arc | `TideFishingHook.pullToward` |
| Mounted fish tip when sneaking | `client/gui/overlays/DisplayTipOverlay.java` |
| Server record effects | `ServerRecords.announce` |
| Rods never break (they wear to broken) | `mixin/ItemStackMixin.java` |

## 6. Building, testing and releasing

* **There is nothing to install.** A push to GitHub starts the build (`.github/workflows/build.yml`). A branch is only built when you press "Run workflow" for it in the Actions tab; `main` and pull requests build by themselves.
* A build compiles the mod, checks that every item has art and a name (`ci/check_assets.py`), starts a real server and a real client on a virtual screen, takes screenshots, and runs `ci/GearCheck.java` (every item and combination of tackle is checked for obvious breakage). The last run's logs and screenshots are on the `ci-results` branch.
* **Releasing:** raise `mod_version` in `gradle.properties`, write the notes in `RELEASE_NOTES.md` (it becomes the release text), and add the same text to the top of `CHANGELOG.md`. A green build on `main` with a new version number publishes the release and its jar automatically. A green build of `main` with the same version only refreshes the `dev` prerelease.
* **After a Minecraft update**, follow `PORTING.md`. The `Minecraft sources` workflow puts the decompiled game on the `mc-sources` branch so you can read how vanilla code works.

## 7. Things that will trip you up

* **Tags decide what counts as tackle.** If a new item does nothing in the table, it is probably missing from its tag.
* **Fractions, not percents.** `0.10` is 10% everywhere in `RodEffects`; `luck` and `speed` are whole numbers.
* **Text colours need an alpha** in this Minecraft version. Use `Gui26.text(...)` instead of the raw call: it fixes that for you.
* **The server decides, the client displays.** Anything that changes a catch must happen on the server (`ServerPlayer`, `ServerLevel`). If the client needs to know, it gets a packet from `network/messages/`, registered in `network/TideMessages.java`.
* **Solstone reads the log and commands.** Do not change the `[Tide catch]` / `[Tide record]` log lines, the `/fishing` commands' output or the scoreboard names without checking PORTING.md section 4.
* **New rig `When` values show up in `/fishing guide`.** Solstone may not know a brand-new one (`ALWAYS` is the latest).
* **Language files:** only `en_us.json` is required. Other languages fall back to English.
