"""What Solstone's Adventures trail asks for: everyone's position and dimension in one command each."""
import asyncio, sys
sys.path.insert(0, sys.path[0])
from solstone_rcon import Rcon


async def main():
    r = Rcon("127.0.0.1", 25575, "ci", timeout=15)
    for what in ("Pos", "Dimension"):
        print(what + ":", (await r.command(f"execute as @a run data get entity @s {what}")).replace("\n", " | ")[:400])
    await r.close()

asyncio.run(main())
