"""Poor fish look poor: a dulled version of every fish (raw), and of every cooked fish with a faint sickly green.

    python3 tools/poor_art.py      textures in textures/item/poor/ (raw) and poor/cooked/ (cooked), their models, and
                                   item definitions that pick them when tide:fish_quality is 0 (Poor)

Run after tools/cooked_art.py (it rewrites the cooked fish's item definitions, which this then wraps).
"""
import colorsys
import glob
import json
import os

from PIL import Image

A = os.path.join(os.path.dirname(__file__), '..', 'src/main/resources/assets/tide/')
T = A + 'textures/item/'
DATA = os.path.join(os.path.dirname(__file__), '..', 'src/main/resources/data/')
COOKED_ITEMS = ('cooked_fish', 'small_cooked_fish', 'large_cooked_fish')


def dull(img: Image.Image, cooked: bool) -> Image.Image:
    im = img.convert('RGBA').copy()
    px = im.load()
    for y in range(im.size[1]):
        for x in range(im.size[0]):
            r, g, b, a = px[x, y]
            if not a:
                continue
            h, l, s = colorsys.rgb_to_hls(r / 255, g / 255, b / 255)
            s *= 0.4
            l *= 0.9
            if cooked:                                   # a faint sickly green, still plainly a cooked fish
                h = h * 0.55 + 0.22 * 0.45
                s = max(s, 0.18)
            px[x, y] = tuple(int(v * 255) for v in colorsys.hls_to_rgb(h, l, s)) + (a,)
    return im


def save_tex(src: str, dst: str, cooked: bool) -> None:
    os.makedirs(os.path.dirname(T + dst), exist_ok=True)
    dull(Image.open(T + src + '.png'), cooked).save(T + dst + '.png')
    if os.path.exists(T + src + '.png.mcmeta'):
        with open(T + src + '.png.mcmeta') as s, open(T + dst + '.png.mcmeta', 'w') as d:
            d.write(s.read())


def model(name: str, tex: str) -> str:
    os.makedirs(os.path.dirname(A + 'models/item/' + name), exist_ok=True)
    with open(A + 'models/item/' + name + '.json', 'w') as f:
        json.dump({"parent": "minecraft:item/generated", "textures": {"layer0": "tide:item/" + tex}}, f, indent=2)
    return 'tide:item/' + name


def poor_select(poor: dict, normal: dict) -> dict:
    return {"type": "minecraft:select", "property": "minecraft:component", "component": "tide:fish_quality",
            "cases": [{"when": 0, "model": poor}], "fallback": normal}


def raw_fish() -> list[str]:
    out = set()
    for f in glob.glob(DATA + '*/fishing/fish/**/*.json', recursive=True):
        try:
            v = json.load(open(f)).get('fish')
        except ValueError:
            continue
        if isinstance(v, str) and v.startswith('tide:'):
            out.add(v[5:])
    return sorted(out)


def main() -> None:
    fish = raw_fish()
    for n in fish:
        save_tex(n, 'poor/' + n, False)
        item = A + 'items/' + n + '.json'
        d = json.load(open(item))
        normal = d['model']
        if normal.get('type') == 'minecraft:select' and normal.get('component') == 'tide:fish_quality':
            normal = normal['fallback']                  # already wrapped: rebuild from the plain one
        d['model'] = poor_select({"type": "minecraft:model", "model": model('poor/' + n, 'poor/' + n)}, normal)
        with open(item, 'w') as f:
            json.dump(d, f, indent=2)
    cooked = sorted(f[:-4] for f in os.listdir(T + 'cooked') if f.endswith('.png'))
    for n in cooked:
        save_tex('cooked/' + n, 'poor/cooked/' + n, True)
        model('poor/cooked/' + n, 'poor/cooked/' + n)
    for item in COOKED_ITEMS:
        save_tex(item, 'poor/' + item, True)
        model('poor/' + item, 'poor/' + item)
        path = A + 'items/' + item + '.json'
        d = json.load(open(path))
        normal = d['model']
        if normal.get('component') == 'tide:fish_quality':
            normal = normal['fallback']
        poor = {"type": "minecraft:select", "property": "minecraft:component", "component": "tide:cooked_from",
                "cases": [{"when": n, "model": {"type": "minecraft:model", "model": "tide:item/poor/cooked/" + n}} for n in cooked],
                "fallback": {"type": "minecraft:model", "model": "tide:item/poor/" + item}}
        d['model'] = poor_select(poor, normal)
        with open(path, 'w') as f:
            json.dump(d, f, indent=2)
    print(len(fish), 'poor raw fish,', len(cooked), 'poor cooked fish')


if __name__ == '__main__':
    main()
