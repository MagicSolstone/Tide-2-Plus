# Tide+ 1.1.0

Install the same way: the jar in `mods/` on the server and every client (replaces 1.0.20).

## Material rigs
New always-on rigs that work anywhere (2 pieces give the small bonus, 3 or more the full one), so the basic tackle has a role:
- **Stone rig** (stone rod, stone hook, plain line): the rod wears half as fast with two pieces, and barely at all with three.
- **Gold rig** (golden rod or Midas rod, golden line, golden hook, golden spoon lure): +10% fishing XP; with three, +20% XP and a 25% chance of a gold nugget with each fish.
- **Diamond rig** (diamond rod, line, hook, bobber): the catch-quality rig. +10% catch quality, +25% with three. The diamond bobber now gives catch quality (+8%) instead of bite speed.
- **Honey rig** (honeycomb rod, honey line, honey bobber): a 15% chance of a honeycomb with each fish; with three, a 25% chance of a honey bottle and an easier landing.
- Iron (the sword), Copper (Storm rig) and Netherite (Fire rig) are unchanged.
- **Lucky rig** (golden apple bobber, enchanted golden apple bobber, lucky hook, magnet lure, lucky bait): +15% rare finds, +30% with three.

## Sunlight and Moonlight rigs
- **Sunlight rig** (sunflower rod, spinner lure, golden bobber), by day: bite speed that follows the sun (up to +20% at Tier 1, +40% at Tier 2, strongest at noon, nothing at dawn and dusk), and **seeds** come up with fish: wheat and beetroot at Tier 1, plus pumpkin and melon at Tier 2.
- **Moonlight rig** (amethyst rod, line, hook and bobber, pearl bobber, mushroom bobber, moon bait), at night: rare finds, and at Tier 2 catch quality too, strongest on a full moon and fading to nothing on a new moon. It also brings up an amethyst shard 10% of the time at Tier 1, 20% at Tier 2.
- **Amethyst moved to the moon**: amethyst catches moonlight now. Each piece gives catch quality at night that grows toward a full moon, and the pieces count toward the Moonlight rig. (Its sunlight bite speed went to the Sunlight rig.)

## Rigs are easier to read
- **Tier 1 and Tier 2**: every rig now says Tier 1 (2+) and Tier 2 (3+) on tooltips, the recipe book, the rod panel and the Angling Table.
- **Every piece belongs to one rig at most.** Moved: moon bait (Moonlight rig only), slime line (Swamp rig only), golden line and golden hook (Gold rig only), golden bobber (Sunlight rig only), honey bobber (Honey rig only); the old Amethyst set became part of the Moonlight rig. New homes: the spider line joins the Cave rig (the Night rig keeps the phantom line), and the redstone line joins the Desert rig.
- **A rod carries two rigs at most**: if it has pieces of three (bait counts too), the two with the most pieces count and the third says "not counted".
- **Each rig has its own colour**, used for its name, its card at the table and its gem.
- **Rods show their rigs on the icon**: a small gem in the corner of the rod, in the rig's colour, one for each rig it carries (bright for Tier 2, dimmer for Tier 1). A rod picks up its gems the next time you change anything on it.

## The Angling Table and tackle box look the part
- **The table has a wooden frame with brass corners** (both tabs), each empty slot shows a faded picture of what goes there with a small label (Rod, Line, Bobber, Hook, Lure), and the rig cards beside it are one card per rig: its colour, its tier, dots for the pieces it has, what it gives now and what the next piece adds.
- **The tackle box has a carrying handle and tray dividers.**

## Cut bait and chum
- **Cut bait remembers the fish it was cut from**, and better fish make better bait: with it on the rod, at least this share of bites are rare, very rare or legendary (where rare fish live at all): Poor 15%, Average 20%, Good 30%, Great 45%, Trophy 75%, Mythic every time. Cut it at the Angling Table from a fish and flint (4 per fish).
- **Chum the water**: when a hook with chum on the rod settles, the water around it is chummed for 2 minutes, and **everyone** fishing within 12 blocks bites 25% faster.

## Tackle with a trick
- **Fish bone hook takes the bone hook's place** (its stats, its Desert rig spot, and it is a Tier 1 hook). The bone hook is no longer made; an old one still works on a rod and can be turned into a fish bone hook at the table.
- **Stone hook** sinks to the bottom: a 15% chance of kelp and 10% of seagrass with each fish.
- **Emerald hook**: +10% fishing XP and a 3% chance of an emerald with each fish.
- **Barbed hook** lands almost anything (Good and Bad hooks always, even a Terrible one half the time), but it tears the fish: never better than Great, and half the time a tier worse. The card says when it did.
- **Heart bobber** heals you three hearts with each fish.
- **Lichen bobber** glows (lights the water like the glow hook), bites 20% faster underground, and joins the Cave rig.
- **Village rod** teaches: players fishing within 16 blocks of you get +10% fishing XP.
- **Crystal rod** tells you what bit, how good it is and how rare, the moment it bites (handy for a fish you keep losing), and gives +10% hook window and +10% to land Good and Bad hooks.

## Levels and rare fish
- **A high fishing level no longer makes rare fish easy.** Levels still widen the hook zones and calm common and uncommon fish, but rare, very rare and legendary fish fight at least as hard, with hook zones no wider, than in original Tide, however high your level. Low levels are still harder on them. To take on a hard fish, use tackle: a wider hook window, an easier fight, a landing hook, or the barbed hook to land it at the cost of its quality.

## Coal line merged
- **The coal line is retired**; the redstone line took in its hook window (+5%). An old coal line still works on a rod and turns back into a fishing line at the table.

## Crates
- **Crates may hold string (2-5) or a fishing line** (35% of crates), to help with crafting.
- **Basic tackle turns up more, the best tackle very rarely.** Fishing crates hold tackle a little more often (30%, was 15%), now including plain lines and hooks, stone and iron pieces; golden pieces are rare, and a diamond hook, line or bobber (or netherite in Nether crates) is about 1 crate in 200. The fishing boat chest has a few basics too.

## Rod panel
- **Shows only what your rod changes.** A stat the rod, its tackle, your gear and your level leave alone is not listed.
- **Rigs and conditions at a glance.** Under the stats: the rigs the rod carries (with their tier), in the rig's colour when they are working here and now and grey when not; then every time, place and weather the rod's own pieces care about (night, rain, oceans...), bright when it applies and grey when it doesn't. The Sunlight and Moonlight rigs, amethyst and the wayfinder rod show how strong they are right now as a percentage.
