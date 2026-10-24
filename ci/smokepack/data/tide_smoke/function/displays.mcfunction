# Fish displays up close (CI): cod, salmon and tropical fish, each "swimming" (the default) and flopping, to compare.
fill ~-5 ~ ~-5 ~5 ~4 ~-5 minecraft:stone
fill ~-5 ~ ~-4 ~5 ~4 ~-1 minecraft:air
setblock ~-3 ~1 ~-4 tide:fish_display[facing=south]{fish:{id:"minecraft:cod",count:1,components:{"tide:fish_quality":5}}}
setblock ~-2 ~1 ~-4 tide:fish_display[facing=south]{fish:{id:"minecraft:cod",count:1,components:{"tide:fish_quality":5,"minecraft:custom_data":{tide_display:{swim:0b}}}}}
setblock ~-1 ~1 ~-4 tide:fish_display[facing=south]{fish:{id:"minecraft:salmon",count:1,components:{"tide:fish_quality":5}}}
setblock ~0 ~1 ~-4 tide:fish_display[facing=south]{fish:{id:"minecraft:salmon",count:1,components:{"tide:fish_quality":5,"minecraft:custom_data":{tide_display:{swim:0b}}}}}
setblock ~1 ~1 ~-4 tide:fish_display[facing=south]{fish:{id:"minecraft:tropical_fish",count:1,components:{"tide:fish_quality":5}}}
setblock ~2 ~1 ~-4 tide:fish_display[facing=south]{fish:{id:"minecraft:tropical_fish",count:1,components:{"tide:fish_quality":5,"minecraft:custom_data":{tide_display:{swim:0b}}}}}
tp @s ~-0.5 ~ ~-1 180 0
