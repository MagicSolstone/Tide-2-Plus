"""Ask the test server for what Solstone asks for, over RCON with Solstone's own client (ci/solstone_rcon.py, a copy)."""
import asyncio, json, sys, time
sys.path.insert(0, sys.path[0])
from solstone_rcon import Rcon


async def main():
    r = Rcon("127.0.0.1", 25575, "ci", timeout=15)
    one = await r.command("fishing describe tide:golden_hook")
    print("describe:", one.replace("\n", " | ")[:300])
    t = time.monotonic()
    raw = await r.command("fishing guide")
    print(f"guide took {time.monotonic() - t:.2f}s")
    assert raw.startswith("tackleguide "), raw[:200]
    g = json.loads(raw[len("tackleguide "):])
    with open("guide.json", "w") as f:      # kept with the results, for the tackle guide pages
        f.write(raw[len("tackleguide "):])
    print(f"guide over rcon: {len(raw)} characters, {len(g['items'])} items, {len(g['rigs'])} rigs")
    await r.close()

asyncio.run(main())
