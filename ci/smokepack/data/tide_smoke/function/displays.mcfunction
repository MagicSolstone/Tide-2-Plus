# Fish displays up close, on a wall behind the player (away from the pool): a big Mythic tropical fish between a cod
# and a salmon, a pufferfish above it, to compare how they hang.
fill ~-4 ~ ~-5 ~4 ~4 ~-5 minecraft:stone
fill ~-4 ~ ~-4 ~4 ~4 ~-1 minecraft:air
setblock ~-2 ~1 ~-4 tide:fish_display[facing=south]{fish:{id:"minecraft:cod",count:1}}
setblock ~0 ~1 ~-4 tide:fish_display[facing=south]{fish:{id:"minecraft:tropical_fish",count:1,components:{"tide:fish_quality":5,"tide:fish_length":30.0,"tide:fish_weight":900.0}}}
setblock ~2 ~1 ~-4 tide:fish_display[facing=south]{fish:{id:"minecraft:salmon",count:1}}
setblock ~0 ~2 ~-4 tide:fish_display[facing=south]{fish:{id:"minecraft:pufferfish",count:1}}
tp @s ~ ~ ~ 180 8
