# An open pool in front of the player with Tide fish in it, and Tide items in the hotbar.
tag @s add tide_smoke
time set noon
weather clear
gamerule advance_time false
gamerule spawn_mobs false
fill ~-6 ~-1 ~-2 ~6 ~4 ~14 minecraft:air
fill ~-6 ~-1 ~-2 ~6 ~-1 ~1 minecraft:grass_block
fill ~-6 ~-5 ~2 ~6 ~-1 ~14 minecraft:stone
fill ~-5 ~-4 ~3 ~5 ~-1 ~13 minecraft:water
tp @s ~ ~ ~ 0 35
summon tide:largemouth_bass ~-2 ~-2 ~5 {NoAI:1b,Rotation:[90f,0f]}
summon tide:rainbow_trout ~0 ~-2 ~6 {NoAI:1b,Rotation:[90f,0f]}
summon tide:guppy ~2 ~-2 ~5 {NoAI:1b,Rotation:[90f,0f]}
summon tide:catfish ~-1 ~-3 ~7 {NoAI:1b,Rotation:[90f,0f]}
summon tide:angelfish ~1 ~-2 ~7 {NoAI:1b,Rotation:[90f,0f]}
summon tide:pike ~0 ~-3 ~4 {NoAI:1b,Rotation:[90f,0f]}
clear @s
item replace entity @s hotbar.0 with tide:diamond_fishing_rod
item replace entity @s hotbar.1 with tide:fishing_journal
item replace entity @s hotbar.2 with tide:largemouth_bass
item replace entity @s hotbar.3 with tide:wooden_crate
item replace entity @s hotbar.4 with tide:starlight_bow
item replace entity @s hotbar.5 with tide:fish_satchel
item replace entity @s hotbar.6 with tide:cooked_fish
item replace entity @s hotbar.7 with tide:pocket_watch
item replace entity @s hotbar.8 with tide:angling_table
