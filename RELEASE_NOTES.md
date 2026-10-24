# Tide+ 1.0.10

Quality at a glance, records in the journal, and fish displays that behave under Fresh Animations. Install the same way: the jar in `mods/` on the server and every client (replaces 1.0.9).

## New
- **Quality mark on fish icons.** Graded fish (raw and cooked) get a small diamond in their quality colour in the top-right of the icon: in inventories, the hotbar, the satchel and the journal. Average fish get none, so the mark means something. (Top-right because the bottom-right is the stack count's.)
- **Server records in the journal.** Each fish's journal page shows the server's record for it: weight, quality and who caught it. Kept up to date as records are beaten.
- **Cooked fish that look like their fish (first five).** A cooked bluegill, largemouth bass, angelfish, anglerfish or arapaima looks like that fish, cooked. The rest still use the plain cooked fish for now. Only fish cooked from now on remember their species.

## Fixes
- **Fish displays with Fresh Animations** (or other animation packs): cod, salmon and tropical fish no longer lie on their belly, wiggle or blink. The pack now holds them still in their swimming pose. Take a fish off its display and put it back if it still looks off.

## Under the hood
- Each Tide+ extra around a catch (grading, the rod's extras and drops, the server record, fishing XP, the catch log) now fails on its own if it ever goes wrong: the fish still lands and still goes in your journal.
