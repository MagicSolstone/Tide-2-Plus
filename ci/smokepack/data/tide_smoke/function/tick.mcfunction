execute as @a[tag=!tide_smoke] at @s run function tide_smoke:setup
# the client test can't run /function (no cheats), but anyone can /trigger
execute as @a[scores={tide_displays=1..}] at @s run function tide_smoke:displays
scoreboard players reset @a[scores={tide_displays=1..}] tide_displays
scoreboard players enable @a tide_displays
