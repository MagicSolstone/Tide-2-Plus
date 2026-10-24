# The Angling Table (CI): a table two blocks ahead, the player centred and looking at it, and the materials
# for a diamond hook on a bone hook (plus a few more, so the book has things to show).
execute align xyz run fill ~-1 ~ ~1 ~1 ~3 ~3 minecraft:air
execute align xyz run fill ~-1 ~-1 ~1 ~1 ~-1 ~3 minecraft:stone
execute align xyz run setblock ~ ~ ~2 tide:angling_table
execute align xyz positioned ~0.5 ~ ~0.5 run tp @s ~ ~ ~ facing ~ ~0.5 ~2
give @s tide:bone_hook 1
give @s minecraft:diamond 2
give @s minecraft:string 9
give @s minecraft:stick 8
give @s minecraft:iron_ingot 3
