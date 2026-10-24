# Tide+ 1.0.9

Better fish, more loot; and tropical fish look right on the wall. Install the same way: the jar in `mods/` on the server and every client (replaces 1.0.8).

## Resource fish scale with quality
Fish crafted into resources give more for a better fish. The old amount is now an Average fish's; Poor gives half (at least 1), Good x1.5, Great x2, Trophy x3, Mythic x4 (rounded up).

| Fish | Poor | Average | Good | Great | Trophy | Mythic |
|---|---|---|---|---|---|---|
| Iron tetra → raw iron | 1 | 2 | 3 | 4 | 6 | 8 |
| Glowfish → glow ink sacs | 1 | 2 | 3 | 4 | 6 | 8 |
| Gilded minnow → raw gold | 1 | 2 | 3 | 4 | 6 | 8 |
| Lapis lanternfish → lapis | 1 | 2 | 3 | 4 | 6 | 8 |
| Crystal shrimp → amethyst shards | 2 | 4 | 6 | 8 | 12 | 16 |
| Enderfin, endergazer, elytrout | 1 | 1 | 2 | 2 | 3 | 4 |

The recipe book still shows the Average amount; the crafting result shows the real one. Fish caught before qualities existed give the old amount.

## Fixes
- **Tropical fish on a fish display** showed as a plain white fish (a caught tropical fish item doesn't remember its colours). They now show one of the game's named tropical fish patterns, always the same for the same fish, or the bucket's pattern if it came from a bucket. Take one off the wall and put it back to update it.
