# The Angling Table (CI): a table in front of the player and some materials, to open the Craft tab and make a diamond hook on a bone hook.
fill ~-1 ~ ~1 ~1 ~2 ~2 minecraft:air
setblock ~ ~ ~2 tide:angling_table
tp @s ~ ~ ~ facing ~ ~0.5 ~2.5
clear @s minecraft:diamond
give @s tide:bone_hook 1
give @s minecraft:diamond 2
give @s minecraft:string 9
give @s minecraft:stick 8
give @s minecraft:iron_ingot 3
