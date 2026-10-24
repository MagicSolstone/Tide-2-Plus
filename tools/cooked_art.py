"""Cooked versions of raw fish textures: the same shape and shading, in the cooked fish's tan-brown ramp.

    python3 tools/cooked_art.py bluegill largemouth_bass ...   textures + models for these fish, and the cooked
                                                                items' model selects for every cooked texture there is
    python3 tools/cooked_art.py --sheet a b c                  a before/after preview sheet (scratch only)
"""
import json
import os
import sys
from PIL import Image

A = os.path.join(os.path.dirname(__file__), '..', 'src/main/resources/assets/tide/')
T = A + 'textures/item/'


def lum(c):
    return 0.299 * c[0] + 0.587 * c[1] + 0.114 * c[2]


def ramp():
    cols = set()
    for n in ('cooked_fish', 'large_cooked_fish', 'small_cooked_fish'):
        im = Image.open(T + n + '.png').convert('RGBA')
        cols |= {im.getpixel((x, y))[:3] for y in range(im.size[1]) for x in range(im.size[0]) if im.getpixel((x, y))[3] > 0}
    return sorted(cols, key=lum)


RAMP = ramp()


def cook(raw: Image.Image) -> Image.Image:
    raw = raw.convert('RGBA')
    px = [raw.getpixel((x, y)) for y in range(raw.size[1]) for x in range(raw.size[0]) if raw.getpixel((x, y))[3] > 0]
    ls = sorted(lum(p) for p in px)
    lo, hi = ls[int(len(ls) * 0.03)], ls[int(len(ls) * 0.97)]
    dark = ls[len(ls) // 2] < 70                 # a dark fish: spread its own range over the ramp, no forced outline
    if dark:
        lo, hi = ls[0], ls[-1]
    out = Image.new('RGBA', raw.size)
    w, h = raw.size
    for y in range(h):
        for x in range(w):
            p = raw.getpixel((x, y))
            if p[3] == 0:
                continue
            L = lum(p)
            t = 0.0 if hi <= lo else min(1.0, max(0.0, (L - lo) / (hi - lo)))
            # a little warmer in the middle tones, and keep the darkest (eyes, outline) dark
            i = round((t ** 1.15) * (len(RAMP) - 2))          # the lightest tone is kept for the odd highlight
            edge = any(not (0 <= x + dx < w and 0 <= y + dy < h) or raw.getpixel((x + dx, y + dy))[3] == 0
                       for dx, dy in ((1, 0), (-1, 0), (0, 1), (0, -1)))
            if edge:
                i = max(0, i - 3)                               # a browned, crisp edge
            c = RAMP[i]
            if L < 40 and not dark:
                c = tuple(int(v * 0.45) for v in RAMP[0])
            out.putpixel((x, y), (*c, p[3]))
    return out


def write(names):
    os.makedirs(T + 'cooked', exist_ok=True)
    os.makedirs(A + 'models/item/cooked', exist_ok=True)
    for n in names:
        cook(Image.open(T + n + '.png')).save(T + 'cooked/' + n + '.png')
        if os.path.exists(T + n + '.png.mcmeta'):                    # animated: the cooked one animates the same way
            with open(T + n + '.png.mcmeta') as src, open(T + 'cooked/' + n + '.png.mcmeta', 'w') as dst:
                dst.write(src.read())
        with open(A + 'models/item/cooked/' + n + '.json', 'w') as f:
            json.dump({"parent": "minecraft:item/generated", "textures": {"layer0": "tide:item/cooked/" + n}}, f, indent=2)
    have = sorted(f[:-4] for f in os.listdir(T + 'cooked') if f.endswith('.png'))
    for item in ('cooked_fish', 'small_cooked_fish', 'large_cooked_fish'):
        model = {"model": {"type": "minecraft:select", "property": "minecraft:component", "component": "tide:cooked_from",
                           "cases": [{"when": n, "model": {"type": "minecraft:model", "model": "tide:item/cooked/" + n}} for n in have],
                           "fallback": {"type": "minecraft:model", "model": "tide:item/" + item}}}
        with open(A + 'items/' + item + '.json', 'w') as f:
            json.dump(model, f, indent=2)
    print(len(have), 'cooked fish textures')


if __name__ == '__main__':
    if sys.argv[1:2] != ['--sheet']:
        write(sys.argv[1:])
        sys.exit()
    names = sys.argv[2:]
    S = 12
    sheet = Image.new('RGBA', (3 * 16 * S + 40, len(names) * (16 * S + 10)), (40, 40, 48, 255))
    generic = Image.open(T + 'cooked_fish.png').convert('RGBA')
    for r, n in enumerate(names):
        raw = Image.open(T + n + '.png')
        for c, im in enumerate([raw.convert('RGBA'), cook(raw), generic]):
            big = im.resize((16 * S, 16 * S), Image.NEAREST)
            sheet.alpha_composite(big, (c * (16 * S + 20), r * (16 * S + 10)))
    sheet.save(os.environ.get('SHEET', 'cooked-sheet.png'))
    print(len(RAMP), 'ramp colours')
