"""Recipe-book unlocks for every Tide recipe: a recipe shows in the book once you hold one of its
ingredients, or anything of the same kind (any hook shows every hook upgrade and lure, any line every
line...). Writes data/tide/advancement/recipes/<tools|misc>/<recipe>.json for recipes that have none.
Run with --check to only report recipes that have no unlock (CI)."""
import json, sys
from pathlib import Path

D = Path("src/main/resources/data/tide")
GENERIC = {"#minecraft:planks", "#c:slime_balls", "#c:strings", "#c:cobblestones", "minecraft:stick"}


def tag(name):
    p = D / "tags/item" / f"{name}.json"
    return {v for v in json.loads(p.read_text())["values"] if isinstance(v, str)} if p.exists() else set()


KINDS = [("hooks", ["#tide:hooks"]), ("lures", ["#tide:hooks", "#tide:lures"]), ("lines", ["#tide:lines"]),
         ("bobbers", ["#tide:bobbers", "#tide:fishing_rods"]), ("fishing_rods", ["#tide:fishing_rods"]), ("bait_items", ["#tide:bait_items"])]
members = {k: tag(k) for k, _ in KINDS}


def ingredients(r):
    t = r.get("type", "")
    if t.endswith("crafting_shaped"):
        vals = list(r["key"].values())
    elif t.endswith("crafting_shapeless"):
        vals = r["ingredients"]
    elif t.endswith("crafting_dye"):
        vals = [r["target"]]
    elif t.endswith("smithing_transform"):
        vals = [r["addition"]] + (r["base"] if isinstance(r["base"], list) else [r["base"]])
    else:
        return []
    out = []
    for v in vals:
        if isinstance(v, list): v = v[0]
        if isinstance(v, dict): v = v.get("item") or "#" + v["tag"]
        out.append(v)
    return out


rewarded = set()
for f in (D / "advancement/recipes").rglob("*.json"):
    rewarded.update(json.loads(f.read_text()).get("rewards", {}).get("recipes", []))

missing, written = [], 0
for f in sorted((D / "recipe").rglob("*.json")):
    rid = "tide:" + f.relative_to(D / "recipe").with_suffix("").as_posix()
    if rid in rewarded:
        continue
    r = json.loads(f.read_text())
    result = r.get("result", {}).get("id")
    ings = ingredients(r)
    if not result or not ings:
        continue
    missing.append(rid)
    if "--check" in sys.argv:
        continue
    triggers = [i for i in dict.fromkeys(ings) if i not in GENERIC] or list(dict.fromkeys(ings))
    for k, extra in KINDS:
        if result in members[k]:
            triggers += [e for e in extra if e not in triggers]
    criteria = {f"has_{i}": {"trigger": "minecraft:inventory_changed", "conditions": {"items": [{"items": t}]}}
                for i, t in enumerate(triggers)}
    criteria["has_the_recipe"] = {"trigger": "minecraft:recipe_unlocked", "conditions": {"recipes": rid}}
    folder = "misc" if result in members["bait_items"] else "tools"
    out = D / "advancement/recipes" / folder / (f.relative_to(D / "recipe").with_suffix(".json"))
    out.parent.mkdir(parents=True, exist_ok=True)
    out.write_text(json.dumps({"parent": "minecraft:recipes/root", "criteria": criteria,
                               "requirements": [list(criteria)], "rewards": {"recipes": [rid]}}, indent=2) + "\n")
    written += 1

if "--check" in sys.argv:
    for m in missing:
        print("no recipe-book unlock for", m)
    sys.exit(1 if missing else 0)
print(f"wrote {written} unlocks")
