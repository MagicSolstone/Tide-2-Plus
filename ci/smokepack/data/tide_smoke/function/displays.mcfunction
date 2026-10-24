# Fish displays up close: a (Mythic, big) tropical fish between a cod and a salmon, to compare how they hang.
fill ~-3 ~ ~2 ~3 ~3 ~2 minecraft:air
fill ~-3 ~ ~3 ~3 ~3 ~3 minecraft:stone
setblock ~-2 ~1 ~2 tide:fish_display[facing=north]{fish:{id:"minecraft:cod",count:1}}
setblock ~0 ~1 ~2 tide:fish_display[facing=north]{fish:{id:"minecraft:tropical_fish",count:1,components:{"tide:fish_quality":5,"tide:fish_length":30.0,"tide:fish_weight":900.0}}}
setblock ~2 ~1 ~2 tide:fish_display[facing=north]{fish:{id:"minecraft:salmon",count:1}}
setblock ~0 ~2 ~2 tide:fish_display[facing=north]{fish:{id:"minecraft:pufferfish",count:1}}
tp @s ~ ~ ~ 0 5
