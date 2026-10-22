# A glass tank of water in front of the player with Tide fish in it, and Tide items in the hotbar.
tag @s add tide_smoke
time set noon
weather clear
gamerule advance_time false
gamerule spawn_mobs false
tp @s ~ ~ ~ 0 10
fill ~-4 ~-1 ~2 ~4 ~3 ~8 minecraft:glass
fill ~-3 ~ ~3 ~3 ~3 ~7 minecraft:water
summon tide:largemouth_bass ~-2 ~1 ~4 {NoAI:1b,Rotation:[90f,0f]}
summon tide:rainbow_trout ~0 ~2 ~5 {NoAI:1b,Rotation:[90f,0f]}
summon tide:guppy ~2 ~1 ~4 {NoAI:1b,Rotation:[90f,0f]}
summon tide:catfish ~-1 ~2 ~6 {NoAI:1b,Rotation:[90f,0f]}
summon tide:angelfish ~1 ~1 ~6 {NoAI:1b,Rotation:[90f,0f]}
summon tide:pike ~0 ~0 ~4 {NoAI:1b,Rotation:[90f,0f]}
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
