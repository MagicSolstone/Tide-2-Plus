"""Every item the mod registers has what the game needs to show it: an item definition, the models it
points at, their textures, and an English name. Every hook also needs the small texture used under the
bobber in the water. Prints the problems and exits 1 if there are any (run from the repo root)."""
import json, re, sys
from pathlib import Path

A = Path("src/main/resources/assets/tide")
src = Path("src/main/java/com/li64/tide/registries/TideItems.java").read_text()
items = re.findall(r'register\("([a-z0-9_]+)"', src)
lang = json.loads((A / "lang/en_us.json").read_text(encoding="utf-8"))
problems = []


def models_in(node, out):
    if isinstance(node, dict):
        if node.get("type") in ("minecraft:model", "model") and "model" in node:
            out.add(node["model"])
        for v in node.values():
            models_in(v, out)
    elif isinstance(node, list):
        for v in node:
            models_in(v, out)


def check_model(ref, seen=set()):
    if ref in seen:
        return
    seen.add(ref)
    ns, path = ref.split(":") if ":" in ref else ("minecraft", ref)
    if ns != "tide":
        return
    f = A / "models" / f"{path}.json"
    if not f.exists():
        problems.append(f"missing model {ref}")
        return
    m = json.loads(f.read_text())
    if "parent" in m:
        check_model(m["parent"])
    for key, t in m.get("textures", {}).items():
        if t.startswith("#"):
            continue
        tns, tp = t.split(":") if ":" in t else ("minecraft", t)
        if tns == "tide" and not (A / "textures" / f"{tp}.png").exists():
            problems.append(f"model {ref} uses missing texture {t}")


hooks = {v for v in json.loads(Path("src/main/resources/data/tide/tags/item/hooks.json").read_text())["values"] if isinstance(v, str)}
for name in items:
    d = A / "items" / f"{name}.json"
    if not d.exists():
        # block items use the block's definition under the same name
        problems.append(f"{name}: no item definition (assets/tide/items/{name}.json)")
        continue
    refs = set()
    models_in(json.loads(d.read_text()), refs)
    if not refs:
        problems.append(f"{name}: item definition names no model")
    for r in refs:
        check_model(r)
    if f"item.tide.{name}" not in lang and f"block.tide.{name}" not in lang:
        problems.append(f"{name}: no English name in en_us.json")
    if f"tide:{name}" in hooks and not (A / "textures/entity/fishing_hook" / f"{name}.png").exists():
        problems.append(f"{name}: no in-water hook texture (textures/entity/fishing_hook/{name}.png)")

print(f"checked {len(items)} items")
for p in problems:
    print("asset problem:", p)
sys.exit(1 if problems else 0)
