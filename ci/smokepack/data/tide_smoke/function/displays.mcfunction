# Fish displays up close (CI): a cod, then tropical fish as they are, and turned four ways to find the right turn.
fill ~-5 ~ ~-5 ~5 ~4 ~-5 minecraft:stone
fill ~-5 ~ ~-4 ~5 ~4 ~-1 minecraft:air
setblock ~-3 ~1 ~-4 tide:fish_display[facing=south]{fish:{id:"minecraft:cod",count:1}}
setblock ~-2 ~1 ~-4 tide:fish_display[facing=south]{fish:{id:"minecraft:tropical_fish",count:1,components:{"tide:fish_quality":5,"tide:fish_length":30.0,"tide:fish_weight":900.0}}}
setblock ~-1 ~1 ~-4 tide:fish_display[facing=south]{fish:{id:"minecraft:tropical_fish",count:1,components:{"tide:fish_quality":5,"tide:fish_length":30.0,"tide:fish_weight":900.0,"minecraft:custom_data":{tide_display:{pitch:90f}}}}}
setblock ~0 ~1 ~-4 tide:fish_display[facing=south]{fish:{id:"minecraft:tropical_fish",count:1,components:{"tide:fish_quality":5,"tide:fish_length":30.0,"tide:fish_weight":900.0,"minecraft:custom_data":{tide_display:{pitch:-90f}}}}}
setblock ~1 ~1 ~-4 tide:fish_display[facing=south]{fish:{id:"minecraft:tropical_fish",count:1,components:{"tide:fish_quality":5,"tide:fish_length":30.0,"tide:fish_weight":900.0,"minecraft:custom_data":{tide_display:{yaw:90f}}}}}
setblock ~2 ~1 ~-4 tide:fish_display[facing=south]{fish:{id:"minecraft:tropical_fish",count:1,components:{"tide:fish_quality":5,"tide:fish_length":30.0,"tide:fish_weight":900.0,"minecraft:custom_data":{tide_display:{yaw:-90f}}}}}
tp @s ~-0.5 ~ ~-1 180 0
