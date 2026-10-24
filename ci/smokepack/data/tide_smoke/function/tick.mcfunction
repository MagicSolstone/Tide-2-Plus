execute as @a[tag=!tide_smoke] at @s run function tide_smoke:setup
# the client test can't run /function (no cheats), but anyone can /trigger
execute as @a[scores={tide_displays=1..}] at @s run function tide_smoke:displays
scoreboard players reset @a[scores={tide_displays=1..}] tide_displays
scoreboard players enable @a tide_displays
execute as @a[scores={tide_side=1..}] at @s run function tide_smoke:displays_side
scoreboard players reset @a[scores={tide_side=1..}] tide_side
scoreboard players enable @a tide_side
execute as @a[scores={tide_bench=1..}] at @s run function tide_smoke:bench
scoreboard players reset @a[scores={tide_bench=1..}] tide_bench
scoreboard players enable @a tide_bench
