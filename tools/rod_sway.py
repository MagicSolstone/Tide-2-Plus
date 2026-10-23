"""Gives every rod's in-hand/inventory texture a gentle swaying line: the hanging line and hook (the pixels the
uncast texture has and the cast one doesn't) swing a pixel each way, more toward the hook, like a pendulum.
Run after changing any rod art: python3 tools/rod_sway.py  (reads <rod>_still.png if present, writes <rod>.png + .mcmeta)."""
import glob, json, os
from PIL import Image

T = os.path.join(os.path.dirname(os.path.abspath(__file__)), '..', 'src/main/resources/assets/tide/textures/item')
SWING = [0, 0, 1, 1, 1, 0, 0, -1, -1, -1]          # one slow back-and-forth
TICKS = 3                                           # per frame


def still(path):
    """The un-animated texture: the first frame if it's already animated."""
    im = Image.open(path).convert('RGBA')
    return im.crop((0, 0, 16, 16)) if im.height > 16 else im


def sway(name):
    base = os.path.join(T, name + '.png')
    up, cast = still(base), still(os.path.join(T, name + '_cast.png'))
    hanging = {(x, y) for y in range(3, 16) for x in range(16)
               if up.getpixel((x, y))[3] and up.getpixel((x, y)) != cast.getpixel((x, y)) and x >= 17 - y}
    if not hanging:
        return False
    frames = Image.new('RGBA', (16, 16 * len(SWING)), (0, 0, 0, 0))
    for f, s in enumerate(SWING):
        im = up.copy()
        for (x, y) in hanging: im.putpixel((x, y), cast.getpixel((x, y)))       # take the line away
        moved = {}
        for (x, y) in sorted(hanging, key=lambda p: p[1]):                         # and hang it back, swung
            dx = round(s * (y - 3) / 9)
            if 0 <= x + dx < 16:
                im.putpixel((x + dx, y), up.getpixel((x, y)))
                moved.setdefault(y, []).append((x + dx, up.getpixel((x, y))))
        # where a swung row lands two pixels away from the row above, fill the step so the line stays unbroken
        for y in sorted(moved):
            if y - 1 not in moved: continue
            above = [x for x, _ in moved[y - 1]]
            for x, c in list(moved[y]):
                near = min(above, key=lambda a: abs(a - x))
                if abs(near - x) > 1:
                    step = 1 if near > x else -1
                    for fx in range(x + step, near, step): im.putpixel((fx, y), c)
        frames.paste(im, (0, 16 * f))
    frames.save(base)
    json.dump({"animation": {"frametime": TICKS, "interpolate": False}}, open(base + '.mcmeta', 'w'), indent=2)
    return True


if __name__ == '__main__':
    done = [os.path.basename(p)[:-4] for p in sorted(glob.glob(os.path.join(T, '*_fishing_rod.png')) + glob.glob(os.path.join(T, 'kids_fishing_pole.png')))]
    print(sum(sway(n) for n in done), 'of', len(done), 'rods sway')
