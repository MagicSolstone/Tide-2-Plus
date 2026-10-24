"""Poor fish look poor, and Trophy and Mythic ones shimmer: a dulled version of every fish (raw), and of every cooked fish with a faint sickly green.

    python3 tools/poor_art.py                 textures in textures/item/poor/ (raw) and poor/cooked/ (cooked), their models,
                                              and item definitions that pick them when tide:fish_quality is 0 (Poor)
    python3 tools/poor_art.py --vanilla JAR   the same for Minecraft's cod, salmon, tropical fish and pufferfish (and cooked
                                              cod and salmon), from the textures in the Minecraft jar

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


# ---- Trophy and Mythic: a band of light in the quality's colour sweeps across the fish now and then
SHIMMER = {4: 0xD88CFF, 5: 0xFFD54A}           # Trophy purple, Mythic gold (the dog-ear's colours)
SWEEP, PAUSE = 14, 22                            # frames with the band crossing, then frames without


def shimmer_mask(img: Image.Image) -> Image.Image:
    """An animated strip: the fish's shape, white where the band is (alpha), for tinting in the item model."""
    src = img.convert('RGBA').crop((0, 0, 16, 16))
    sp = src.load()
    frames = SWEEP + PAUSE
    out = Image.new('RGBA', (16, 16 * frames), (0, 0, 0, 0))
    op = out.load()
    for f in range(SWEEP):
        pos = -3 + f * (22 / (SWEEP - 1))         # along the diagonal, bottom-left to top-right
        for y in range(16):
            for x in range(16):
                if sp[x, y][3] == 0:
                    continue
                a = max(0.0, 1 - abs((x + (15 - y)) / 2 - pos) / 2.0)
                if a > 0:
                    op[x, f * 16 + y] = (255, 255, 255, int(a * 220))
    return out


def save_shimmer(src: str, name: str) -> str:
    os.makedirs(os.path.dirname(T + 'shimmer/' + name), exist_ok=True)
    shimmer_mask(Image.open(T + src + '.png')).save(T + 'shimmer/' + name + '.png')
    with open(T + 'shimmer/' + name + '.png.mcmeta', 'w') as f:
        json.dump({"animation": {"frametime": 2}}, f)
    os.makedirs(os.path.dirname(A + 'models/item/shimmer/' + name), exist_ok=True)
    with open(A + 'models/item/shimmer/' + name + '.json', 'w') as f:
        json.dump({"parent": "minecraft:item/generated", "textures": {"layer0": tex_ref(src), "layer1": "tide:item/shimmer/" + name}}, f, indent=2)
    return 'tide:item/shimmer/' + name


def tex_ref(src: str) -> str:
    return src if ':' in src else 'tide:item/' + src


def argb(rgb: int) -> int:
    """An opaque colour as the signed ARGB int item tints take (alpha 0 would hide the layer)."""
    v = 0xFF000000 | rgb
    return v - (1 << 32) if v >= 1 << 31 else v


def shimmered(model_id: str, quality: int) -> dict:
    return {"type": "minecraft:model", "model": model_id,
            "tints": [{"type": "minecraft:constant", "value": -1}, {"type": "minecraft:constant", "value": argb(SHIMMER[quality])}]}


def quality_select(poor: dict, shimmer: dict | None, normal: dict) -> dict:
    cases = [{"when": 0, "model": poor}]
    if shimmer:
        cases += [{"when": q, "model": m} for q, m in shimmer.items()]
    return {"type": "minecraft:select", "property": "minecraft:component", "component": "tide:fish_quality",
            "cases": cases, "fallback": normal}


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
        sh = save_shimmer(n, n)
        d['model'] = quality_select({"type": "minecraft:model", "model": model('poor/' + n, 'poor/' + n)},
                                    {q: shimmered(sh, q) for q in SHIMMER}, normal)
        with open(item, 'w') as f:
            json.dump(d, f, indent=2)
    cooked = sorted(f[:-4] for f in os.listdir(T + 'cooked') if f.endswith('.png'))
    for n in cooked:
        save_tex('cooked/' + n, 'poor/cooked/' + n, True)
        model('poor/cooked/' + n, 'poor/cooked/' + n)
        save_shimmer('cooked/' + n, 'cooked/' + n)
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
        plain_sh = save_shimmer(item, item)
        shimmer = {q: {"type": "minecraft:select", "property": "minecraft:component", "component": "tide:cooked_from",
                       "cases": [{"when": n, "model": shimmered("tide:item/shimmer/cooked/" + n, q)} for n in cooked],
                       "fallback": shimmered(plain_sh, q)} for q in SHIMMER}
        d['model'] = quality_select(poor, shimmer, normal)
        with open(path, 'w') as f:
            json.dump(d, f, indent=2)
    print(len(fish), 'poor and shimmering raw fish,', len(cooked), 'cooked')


VANILLA = {"cod": False, "salmon": False, "tropical_fish": False, "pufferfish": False, "cooked_cod": True, "cooked_salmon": True}


def vanilla(jar: str) -> None:
    import io
    import zipfile
    mc_items = os.path.join(os.path.dirname(__file__), '..', 'src/main/resources/assets/minecraft/items/')
    os.makedirs(mc_items, exist_ok=True)
    os.makedirs(T + 'poor/minecraft', exist_ok=True)
    with zipfile.ZipFile(jar) as z:
        for n, cooked in VANILLA.items():
            img = Image.open(io.BytesIO(z.read(f'assets/minecraft/textures/item/{n}.png')))
            dull(img, cooked).save(T + 'poor/minecraft/' + n + '.png')
            img.save(T + 'poor/minecraft/_src_' + n + '.png')            # for the shimmer's shape (removed below)
            normal = json.loads(z.read(f'assets/minecraft/items/{n}.json'))['model']
            poor = {"type": "minecraft:model", "model": model('poor/minecraft/' + n, 'poor/minecraft/' + n)}
            os.makedirs(T + 'shimmer/minecraft', exist_ok=True)
            shimmer_mask(img).save(T + 'shimmer/minecraft/' + n + '.png')
            with open(T + 'shimmer/minecraft/' + n + '.png.mcmeta', 'w') as f:
                json.dump({"animation": {"frametime": 2}}, f)
            os.remove(T + 'poor/minecraft/_src_' + n + '.png')
            os.makedirs(A + 'models/item/shimmer/minecraft', exist_ok=True)
            with open(A + 'models/item/shimmer/minecraft/' + n + '.json', 'w') as f:
                json.dump({"parent": "minecraft:item/generated", "textures": {"layer0": "minecraft:item/" + n,
                           "layer1": "tide:item/shimmer/minecraft/" + n}}, f, indent=2)
            sh = 'tide:item/shimmer/minecraft/' + n
            with open(mc_items + n + '.json', 'w') as f:
                json.dump({"model": quality_select(poor, {q: shimmered(sh, q) for q in SHIMMER}, normal)}, f, indent=2)
    print(len(VANILLA), 'poor vanilla fish')


if __name__ == '__main__':
    import sys
    if sys.argv[1:2] == ['--vanilla']:
        vanilla(sys.argv[2])
    else:
        main()
