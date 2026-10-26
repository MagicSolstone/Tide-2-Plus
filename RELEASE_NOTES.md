# Tide+ 1.0.20

Install the same way: the jar in `mods/` on the server and every client (replaces 1.0.19).

## Rod panel
- **Every stat is always shown** (no more sneaking): cast range, bite speed, hook window, luck, catch quality, fishing XP, land Good/Bad, fish fight, catch difficulty, second fish and comes up cooked. A dash means the gear gives none of that, so two rods are easy to compare. Enchantments get no rows of their own.
- **Lure folds into bite speed.** The Lure enchantment, bait and a rod's own speed count as bite speed, added to the bite speed from your tackle, rigs and level. One number, +% like everything else.

## Bite speed
- **One stacked number with diminishing returns** (no hard cap). Each point of Lure/bait speed is worth +6% (it used to cut the wait by a lot more). Bite speed counts in full up to +50%, half as much from +50% to +100%, and a quarter as much past that, so the fastest possible rod is still well short of instant and most of the speed comes from your tackle.

## Catch quality
- **"Quality bump" is gone.** Gear that had a chance to raise the catch a tier (amethyst rod and bobber, barbed hook, golden bait, and every full rig) now gives more catch quality instead (about 60% of the old bump chance, added to its quality). Mythic stays as rare as it was meant to be.
- **A perfect hook can still raise a fish a tier, but never to Mythic** (Trophy is the top).

## Amethyst is a daytime set
- **The amethyst rod, hook, line and bobber are strongest by day** (crystals catch the light) and only a little useful at night. By day a full amethyst set gives more catch quality than before; at night about a fifth as much. Place-and-time rigs still beat it where they apply.

## Luck is now "Rare finds"
- **Luck shows as a percentage**: "Rare finds +15%" per point, on the rod panel, item tooltips and the angler page (the old "+1 luck" is the same thing). It makes the rare fish (the ones with a luck bonus) more likely by that share of their own chance, so every rare fish benefits the same.
- **Crates are less luck-driven**: luck now adds +4% a point to the crate chance, up to +40% (it was +10% a point, up to +100%).
- The journal's luck requirement (the Midas fish) is shown as a percentage too.

## Fish sizes overlap
- **Each quality is its own bell curve of sizes**, centred higher for better fish but overlapping: a Mythic can be a small fish, and an Average one can, rarely, be a big one. Sizes keep growing slowly past the old top instead of stopping at a hard cap, so there is no size that can never be beaten. For a trout-shaped fish (typically 30-60 cm): an Average one is usually 0.7-4 lb, a Mythic usually 4.6-23 lb, about 13 lb at the median.
- **The catch line in chat shows how your fish compares with the server record** when you hover it.

## Iron rig
- **Hitting mobs wears the rod down** (1 durability per hit, like a sword). A rod worn out by other things stops at broken until repaired, but:
- **An iron-rig rod at its last point of durability breaks for real** when it hits something (its line, hook, bobber and lure drop).

## Rare finds and bite speed both soften
- **Rare finds use the same diminishing returns as bite speed**: full value up to +50%, half from +50% to +100%, a quarter past that. "+150%" never means a guaranteed rare fish: it means each rare fish is that much more likely than its own normal chance (a 2-in-100 fish at +100% is about 4-in-100).
- **Every luck and speed number is a percentage now**: bait tooltips ("+30% rare finds", "+12% bite speed"), rod tooltips, `/fishinglevel` and the angler page. Nothing says "luck" or "fishing speed" points any more.

## Balance
- **Pearl bobber**: +10% catch quality (was +20%; it costs one ender pearl). Its full-moon quality and beach rare finds are unchanged.
- **Diamond hook**: +15% hook window, +5% catch quality, +5% landing (was +12% window, +5% landing), so it is worth more than the circle hook for anything but landing.
- **Circle hook**: +5% hook window (was +10%); still +40% to land Good and Bad hooks and an easier fight.
- **Diamond bobber**: +15% bite speed (was +12%). **Lichen bobber**: +6% (was +10%; glow lichen is easy to find).
- **Cherry rod**: +40% bite speed at dawn and dusk (was +60%).
