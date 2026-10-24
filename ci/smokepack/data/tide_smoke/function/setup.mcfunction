# An open pool in front of the player with Tide fish in it, and Tide items in the hotbar.
scoreboard objectives add tide_displays trigger
scoreboard objectives add tide_side trigger
scoreboard objectives add tide_bench trigger
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
setblock ~2 ~1 ~3 minecraft:stone
setblock ~2 ~1 ~2 tide:fish_display[facing=north]{fish:{id:"tide:catfish",count:1}}
setblock ~-2 ~1 ~3 minecraft:stone
setblock ~-2 ~1 ~2 tide:fish_display[facing=north]{fish:{id:"tide:largemouth_bass",count:1}}
summon tide:largemouth_bass ~-2 ~-2 ~5 {NoAI:1b,Rotation:[90f,0f]}
summon tide:rainbow_trout ~0 ~-2 ~6 {NoAI:1b,Rotation:[90f,0f]}
summon tide:guppy ~2 ~-2 ~5 {NoAI:1b,Rotation:[90f,0f]}
summon tide:catfish ~-1 ~-3 ~7 {NoAI:1b,Rotation:[90f,0f]}
summon tide:angelfish ~1 ~-2 ~7 {NoAI:1b,Rotation:[90f,0f]}
summon tide:pike ~0 ~-3 ~4 {NoAI:1b,Rotation:[90f,0f]}
clear @s
item replace entity @s hotbar.0 with tide:diamond_fishing_rod[enchantments={"minecraft:lure":5}]
item replace entity @s hotbar.1 with tide:fishing_journal
item replace entity @s hotbar.2 with tide:largemouth_bass[tide:fish_quality=5,tide:fish_length=89.0,tide:fish_weight=10800.0,item_name={"translate":"item.tide.quality_name","with":["Mythic",{"translate":"item.tide.largemouth_bass"}],"color":"#FFB020","italic":false}]
item replace entity @s hotbar.3 with tide:wooden_crate
item replace entity @s hotbar.4 with tide:starlight_bow
item replace entity @s hotbar.5 with tide:fish_satchel
item replace entity @s hotbar.6 with tide:cooked_fish[tide:cooked_from="bluegill",tide:fish_quality=4]
item replace entity @s hotbar.7 with tide:tackle_box[container=[{slot:0,item:{id:"tide:glow_lure",count:1}},{slot:1,item:{id:"tide:amethyst_hook",count:1}},{slot:2,item:{id:"tide:chum",count:16}},{slot:3,item:{id:"tide:slime_line",count:1}},{slot:27,item:{id:"tide:bamboo_fishing_rod",count:1}}]]
item replace entity @s hotbar.8 with tide:angling_table
fishinglevel set @s 28
item replace entity @s armor.head with tide:angler_hat
item replace entity @s armor.chest with tide:fishing_vest
item replace entity @s armor.legs with tide:waders
item replace entity @s armor.feet with tide:wading_boots
item replace entity @s weapon.offhand with minecraft:salmon[tide:fish_quality=0]
