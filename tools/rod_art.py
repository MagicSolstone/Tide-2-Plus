"""Hand-tuned 16x16 rod textures (uncast + cast) from a shared layout, each with its own details.
Run: python3 tools/rod_art.py src/main/resources/assets/tide/textures/item  then  python3 tools/rod_sway.py  (re-adds the swaying line)."""
from PIL import Image
import sys
OUT = sys.argv[1] if len(sys.argv) > 1 else 'out'

def hx(s):
    s = s.lstrip('#'); return tuple(int(s[i:i+2], 16) for i in (0, 2, 4)) + (255,)

class Rod:
    def __init__(self, cast):
        self.im = Image.new('RGBA', (16, 16), (0, 0, 0, 0)); self.cast = cast
    def px(self, x, y, c):
        if 0 <= x < 16 and 0 <= y < 16 and c: self.im.putpixel((x, y), hx(c) if isinstance(c, str) else c)
    def get(self, x, y):
        return self.im.getpixel((x, y))

# pole rows 1..8 (tip..grip top), grip rows 9..14; row y: outline 13-y, mid 14-y, light 15-y, dark 16-y
POLE = range(2, 9)
def pole(r, out, mid, light, dark, top=2, bottom=9):
    # three wide (outline, body, shadow) with a highlight running down the body every other pixel: reads as round
    for y in range(top, bottom):
        x = 13 - y
        r.px(x, y, out); r.px(x + 1, y, light if y % 2 == 0 else mid); r.px(x + 2, y, mid if y % 2 == 0 else dark); r.px(x + 3, y, dark if y >= 5 else None)
        if y < 5: r.px(x + 3, y, dark)
def tip(r, a, b):
    r.px(12, 1, a); r.px(13, 1, b); r.px(14, 1, b); r.px(14, 2, b)
def grip(r, out, mid, dark, end):
    # rows 9..13: a wrapped handle (light and dark turns), then a 2x2 butt cap
    for y in range(9, 14):
        x = 13 - y
        r.px(x, y, out); r.px(x + 1, y, mid if y % 2 else dark); r.px(x + 2, y, dark if y % 2 else out)
    r.px(0, 14, end); r.px(1, 14, end); r.px(0, 13, end)
def reel(r, light, mid, dark):
    # a round reel on the lower side of the handle
    for (x, y, c) in [(7, 10, mid), (8, 10, dark), (6, 11, mid), (7, 11, light), (8, 11, mid), (9, 11, dark), (7, 12, dark), (8, 12, dark)]:
        r.px(x, y, c)
def line(r, c='#9a9a9a', d='#5a5a5a'):
    if r.cast:
        for y in range(3, 10):
            r.px(18 - y, y, d if y < 5 else c)
    else:
        for y in range(3, 9): r.px(15, y, d if y < 5 else c)
        for y in range(9, 12): r.px(14, y, c)
def hook(r, w='#ffffff', g='#a0a0a0'):
    if r.cast: return
    r.px(13, 12, w); r.px(13, 13, w); r.px(11, 13, w); r.px(12, 14, w); r.px(11, 14, g)

def base(r, P):
    line(r, *P.get('line', ('#9a9a9a', '#5a5a5a')))
    pole(r, P['out'], P['mid'], P['light'], P['dark'])
    tip(r, P.get('tipa', P['out']), P.get('tipb', P['dark']))
    grip(r, P.get('gout', '#2a1d0b'), P.get('gmid', '#6b4e1e'), P.get('gdark', '#3f2e0e'), P.get('end', '#2a1d0b'))
    reel(r, *P.get('reel', ('#d8d8d8', '#9a9a9a', '#4a4a4a')))
    hook(r, *P.get('hook', ('#ffffff', '#a0a0a0')))

# ---------------------------------------------------------------- rods
def kids(r):
    base(r, dict(out='#7a1c12', mid='#e8452c', light='#ff8a64', dark='#a82a18', tipa='#1f4fa8', tipb='#1f4fa8',
                 gout='#1b3a7a', gmid='#3a74d8', gdark='#24529e', end='#ffd23f', reel=('#ffe066', '#f2b81c', '#9a6a08')))
    # a classic red-and-white float on the line
    if not r.cast:
        for (x, y, c) in [(14, 8, '#e8452c'), (15, 8, '#e8452c'), (14, 9, '#ffffff'), (15, 9, '#e0e0e0')]: r.px(x, y, c)
    for y in (4, 7): r.px(14 - y + 1, y, '#ffd23f'); r.px(14 - y + 2, y, '#ffe98a')   # tape stripes
def bamboo(r):
    base(r, dict(out='#2f4a12', mid='#7aa83a', light='#a8d462', dark='#4c7020', gmid='#8a6a2e', gdark='#5a4418'))
    for y in (3, 6):   # nodes
        x = 13 - y; r.px(x + 1, y, '#c8e08a'); r.px(x + 2, y, '#d8ec9c'); r.px(x + 3, y, '#3a5a16')
    for (x, y) in [(10, 1), (11, 0), (9, 2)]: r.px(x, y, '#5c9a2a')      # leaf at the tip
    r.px(10, 0, '#7cc23a')
def bone(r):
    base(r, dict(out='#8a8270', mid='#e8e2cc', light='#fffcf0', dark='#b8ae90', gmid='#d0c8ac', gdark='#9a9078', gout='#6a6250', end='#fffcf0',
                 reel=('#fffcf0', '#d0c8ac', '#8a8270')))
    for y in (4, 7):   # knobbly joints
        x = 13 - y; r.px(x, y - 1, '#e8e2cc'); r.px(x + 4, y, '#d0c8ac'); r.px(x + 4, y + 1, '#8a8270')
    r.px(0, 12, '#e8e2cc'); r.px(2, 15, '#d0c8ac')
def copper(r):
    base(r, dict(out='#5a2a12', mid='#c46a3a', light='#f0a26a', dark='#8a4220', tipa='#5a2a12', tipb='#f8c89a',
                 gmid='#7a4a2a', gdark='#4a2a14', reel=('#ffd0a0', '#d88048', '#7a3a18')))
    # thick shiny ferrule at the tip, wire wraps, a bit of green patina near the grip
    for (x, y, c) in [(11, 1, '#8a4220'), (12, 0, '#c46a3a'), (13, 0, '#f8c89a'), (15, 1, '#8a4220'), (15, 2, '#5a2a12'), (14, 1, '#ffe0c0')]: r.px(x, y, c)
    for y in (3, 6): x = 13 - y; r.px(x + 1, y, '#ffd8b0'); r.px(x + 2, y, '#ffffff')
    for (x, y) in [(8, 8), (6, 8)]: r.px(x, y, '#4aa88a')
    r.px(7, 7, '#6ac8a8')
def amethyst(r):
    base(r, dict(out='#3a1a6a', mid='#8a5ad0', light='#c8a0f8', dark='#5a32a0', tipa='#3a1a6a', tipb='#5a32a0',
                 reel=('#f0d8ff', '#b88af0', '#5a32a0')))
    # crystal cluster at the tip
    for (x, y, c) in [(13, 0, '#e8ccff'), (14, 0, '#b88af0'), (15, 1, '#e8ccff'), (12, 0, '#8a5ad0'), (15, 0, '#5a32a0'), (11, 1, '#b88af0')]: r.px(x, y, c)
    for (x, y, c) in [(4, 7, '#c8a0f8'), (3, 8, '#8a5ad0'), (9, 3, '#e8ccff')]: r.px(x, y, c)
def mangrove(r):
    base(r, dict(out='#2a0e0a', mid='#7a2e22', light='#a84a38', dark='#4e1a14', gmid='#5a3a28', gdark='#3a2216'))
    # roots curling off the butt, a propagule leaf at the tip
    for (x, y) in [(0, 15), (1, 15), (2, 14), (3, 15), (0, 12), (0, 11)]: r.px(x, y, '#4e1a14')
    for (x, y, c) in [(10, 1, '#3a7a2a'), (11, 0, '#5aa83a'), (10, 0, '#3a7a2a')]: r.px(x, y, c)
def cherry(r):
    base(r, dict(out='#4a1e22', mid='#c4707a', light='#f0a8b0', dark='#8a4650', gmid='#6a3a3e', gdark='#4a2428'))
    for (cx, cy) in [(10, 1), (6, 5), (3, 8)]:   # blossoms
        for (dx, dy, c) in [(0, -1, '#ffd0e0'), (-1, 0, '#ffd0e0'), (1, 0, '#ffb0cc'), (0, 1, '#ffb0cc'), (0, 0, '#ffe860')]: r.px(cx + dx, cy + dy, c)
def frost(r):
    base(r, dict(out='#2a4a7a', mid='#8ac0f0', light='#e0f4ff', dark='#4a80c0', tipa='#2a4a7a', tipb='#e0f4ff',
                 gmid='#5a6a80', gdark='#3a4a60', reel=('#ffffff', '#b0d8f8', '#4a80c0')))
    for (x, y) in [(13, 4), (11, 6), (9, 8)]:   # icicles hanging under the pole
        r.px(x, y, '#c0e4ff'); r.px(x, y + 1, '#e0f4ff'); r.px(x, y + 2, '#ffffff')
    r.px(11, 0, '#ffffff'); r.px(8, 3, '#ffffff')
def cactus(r):
    base(r, dict(out='#1e4a1a', mid='#4a9a3a', light='#7ac85a', dark='#2e6a24', gmid='#a07a3a', gdark='#6a4e1e'))
    for (x, y) in [(9, 2), (7, 4), (5, 6), (12, 5), (10, 7)]: r.px(x, y, '#f0e8a0')     # spines
    for (x, y, c) in [(12, 0, '#ff7ab0'), (13, 0, '#ffb0d0'), (11, 1, '#ff7ab0')]: r.px(x, y, c)   # flower
def conduit(r):
    base(r, dict(out='#14403c', mid='#4aa8a0', light='#8ae0d4', dark='#2a7068', reel=('#f0c070', '#c08030', '#3a2a14'),
                 gmid='#3a6a64', gdark='#24484a'))
    for y in (3, 6): x = 13 - y; r.px(x + 1, y, '#2a7068'); r.px(x + 2, y, '#2a7068')
    r.px(6, 10, '#ffffff'); r.px(7, 11, '#14403c')    # the eye
def clockwork(r):
    base(r, dict(out='#4a3010', mid='#c8962a', light='#f0d070', dark='#8a6418', reel=('#d0d0d0', '#8a8a8a', '#3a3a3a')))
    # a gear for a reel: teeth around it, a redstone dot on the pole
    for (x, y) in [(5, 10), (8, 9), (8, 12), (5, 12)]: r.px(x, y, '#8a8a8a')
    r.px(6, 11, '#3a3a3a')
    r.px(10, 4, '#e83a2a'); r.px(9, 5, '#a01a10')
    for y in (2, 7): x = 13 - y; r.px(x + 2, y, '#fff0b0')
def wayfinder(r):
    base(r, dict(out='#141a30', mid='#3a4a80', light='#6a7ac0', dark='#222c58', tipa='#c8962a', tipb='#f0d070',
                 reel=('#f0f0f0', '#c0c0c0', '#c8962a')))
    for y in (3, 6): x = 13 - y; r.px(x + 1, y, '#c8962a'); r.px(x + 2, y, '#f0d070')   # gold bands
    r.px(7, 10, '#e83a2a'); r.px(6, 11, '#3a3a3a')                                       # compass needle
def rotten(r):
    base(r, dict(out='#2a3a1a', mid='#7a9a5a', light='#a0c07a', dark='#4e6a36', gmid='#e8e2cc', gdark='#a8a08a', gout='#6a6250',
                 reel=('#c8b090', '#8a6a4a', '#4a3020')))
    for (x, y, c) in [(10, 4, '#5a3a2a'), (8, 6, '#8a5a3a'), (11, 2, '#5a3a2a'), (6, 8, '#5a3a2a')]: r.px(x, y, c)   # rot patches
    for y in (3, 5, 7): x = 13 - y; r.px(x + 3, y, '#3a2a1a')            # stitches
def widow(r):
    base(r, dict(out='#0a0a0e', mid='#2a2630', light='#5a5266', dark='#16141a', gmid='#2a2630', gdark='#0a0a0e', gout='#000000',
                 reel=('#ff4a4a', '#c81e24', '#5a0a0e'), line=('#d8d8e0', '#a8a8b8')))
    r.px(4, 11, '#e81e24'); r.px(3, 12, '#e81e24')                       # red hourglass on the grip
    if not r.cast:
        for (x, y) in [(13, 3), (12, 4), (13, 5), (14, 6)]: r.px(x, y, '#e0e0ec')   # web strands to the line
    for (x, y) in [(15, 0), (14, 0)]: r.px(x, y, '#5a5266')
def sickle(r):
    base(r, dict(out='#5a4010', mid='#d8b04a', light='#f6e08a', dark='#9a7820', gmid='#7a5a2a', gdark='#4a3418'))
    # a curved iron blade sweeping back from the tip
    for (x, y, c) in [(15, 2, '#5a5a64'), (15, 1, '#8a8a94'), (14, 0, '#c0c0c8'), (13, 0, '#f4f4fc'), (12, 0, '#f4f4fc'), (11, 0, '#c0c0c8'),
                      (10, 1, '#c0c0c8'), (9, 1, '#8a8a94'), (8, 2, '#5a5a64'), (11, 1, '#5a5a64'), (12, 1, '#3a3a44'), (13, 1, '#5a5a64')]: r.px(x, y, c)
    for y in (4, 7): x = 13 - y; r.px(x + 1, y, '#f6e08a')               # straw binding
def grove(r):
    base(r, dict(out='#2a1a0a', mid='#7a5430', light='#a07848', dark='#4e341a', gmid='#5a4020', gdark='#3a2810'))
    for (cx, cy) in [(11, 1), (6, 5)]:                                     # leaf clusters
        for (dx, dy, c) in [(0, 0, '#4a9a2a'), (-1, 0, '#3a7a1e'), (0, -1, '#6ac23a'), (1, -1, '#4a9a2a'), (-1, -1, '#3a7a1e')]: r.px(cx + dx, cy + dy, c)
    r.px(12, 4, '#d8302a'); r.px(12, 5, '#a0201a')                         # an apple
def drowned(r):
    base(r, dict(out='#12302a', mid='#3a8a7a', light='#6ac0a8', dark='#1e5a4e', gmid='#2a5a4a', gdark='#183a30',
                 reel=('#f0b080', '#c86a3a', '#6a3418')))
    for y in range(2, 9, 2): x = 13 - y; r.px(x + 2, y, '#2a6a2a'); r.px(x + 1, y + 1, '#2a6a2a')   # kelp wrap
    r.px(10, 0, '#2a6a2a'); r.px(9, 0, '#4a9a3a'); r.px(11, 1, '#2a6a2a')
def hex_(r):
    base(r, dict(out='#1a0a26', mid='#5a2a7a', light='#8a52b0', dark='#34184a', tipa='#1a0a26', tipb='#34184a',
                 reel=('#b8ff9a', '#5ad83a', '#1e6a14')))
    # a crooked twig tip with a spark, a potion-bottle reel
    for (x, y, c) in [(15, 1, '#34184a'), (15, 0, '#5a2a7a'), (12, 0, '#e8b0ff'), (11, 0, '#ffffff')]: r.px(x, y, c)
    r.px(6, 12, '#d0d0d0')
    for (x, y) in [(8, 4), (5, 7)]: r.px(x, y, '#b070e0')

RODS = {'kids_fishing_pole': kids, 'bamboo_fishing_rod': bamboo, 'bone_fishing_rod': bone, 'copper_fishing_rod': copper,
        'amethyst_fishing_rod': amethyst, 'mangrove_fishing_rod': mangrove, 'cherry_fishing_rod': cherry, 'frost_fishing_rod': frost,
        'cactus_fishing_rod': cactus, 'conduit_fishing_rod': conduit, 'clockwork_fishing_rod': clockwork, 'wayfinder_fishing_rod': wayfinder,
        'rotten_fishing_rod': rotten, 'widow_fishing_rod': widow, 'sickle_fishing_rod': sickle, 'grove_fishing_rod': grove,
        'drowned_fishing_rod': drowned, 'hex_fishing_rod': hex_}

if __name__ == '__main__':
    import os
    os.makedirs(OUT, exist_ok=True)
    S = 8
    sheet = Image.new('RGBA', (len(RODS) * 18 * S, 2 * 18 * S), (60, 64, 76, 255))
    for i, (name, fn) in enumerate(RODS.items()):
        for j, cast in enumerate((False, True)):
            r = Rod(cast); fn(r)
            r.im.save(f'{OUT}/{name}{"_cast" if cast else ""}.png')
            big = r.im.resize((16 * S, 16 * S), Image.NEAREST); sheet.paste(big, (i * 18 * S + S, j * 18 * S + S), big)
    sheet.save(os.path.join(os.path.dirname(os.path.abspath(__file__)), 'rod_art_preview.png'))
