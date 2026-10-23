# Tide+ 1.0.1

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
