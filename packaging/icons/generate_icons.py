#!/usr/bin/env python3
"""Generate a family of app icons for bb4-puzzles (PNG + ICO + ICNS).

Visual language (keep this consistent across other bb4 installer repos):
- 1024 canvas, macOS-style rounded squircle
- Deep navy fill (#0D2438), 1px inner rim in muted teal
- Cream glyphs (#F4F1DE) with a shared accent palette (cyan / gold / coral)
- No wordmarks; one distinctive geometric motif per app
"""

from __future__ import annotations

import math
import os
import shutil
import subprocess
import tempfile
from pathlib import Path

from PIL import Image, ImageDraw, ImageFont

ROOT = Path(__file__).resolve().parent
SIZE = 1024
NAVY = (13, 36, 56, 255)
TEAL_RIM = (58, 140, 150, 255)
CREAM = (244, 241, 222, 255)
CYAN = (78, 205, 196, 255)
GOLD = (232, 184, 74, 255)
CORAL = (224, 122, 95, 255)
SOFT = (90, 140, 170, 255)


def new_canvas() -> tuple[Image.Image, ImageDraw.ImageDraw]:
    img = Image.new("RGBA", (SIZE, SIZE), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    pad = 48
    radius = 220
    draw.rounded_rectangle([pad, pad, SIZE - pad, SIZE - pad], radius=radius, fill=NAVY)
    draw.rounded_rectangle(
        [pad + 18, pad + 18, SIZE - pad - 18, SIZE - pad - 18],
        radius=radius - 12,
        outline=TEAL_RIM,
        width=8,
    )
    return img, draw


def save_all(app_id: str, img: Image.Image) -> None:
    out = ROOT / app_id
    out.mkdir(parents=True, exist_ok=True)
    png_path = out / "icon.png"
    img.save(png_path, "PNG")

    ico_sizes = [(16, 16), (32, 32), (48, 48), (64, 64), (128, 128), (256, 256)]
    img.save(out / "icon.ico", sizes=ico_sizes)

    if shutil.which("iconutil") and shutil.which("sips"):
        write_icns(img, out / "icon.icns")
    else:
        print(f"warning: iconutil/sips not available; skipped icns for {app_id}")


def write_icns(img: Image.Image, dest: Path) -> None:
    with tempfile.TemporaryDirectory() as tmp:
        iconset = Path(tmp) / "icon.iconset"
        iconset.mkdir()
        specs = [
            ("icon_16x16.png", 16),
            ("icon_16x16@2x.png", 32),
            ("icon_32x32.png", 32),
            ("icon_32x32@2x.png", 64),
            ("icon_128x128.png", 128),
            ("icon_128x128@2x.png", 256),
            ("icon_256x256.png", 256),
            ("icon_256x256@2x.png", 512),
            ("icon_512x512.png", 512),
            ("icon_512x512@2x.png", 1024),
        ]
        master = Path(tmp) / "master.png"
        img.save(master)
        for name, px in specs:
            subprocess.check_call(
                ["sips", "-z", str(px), str(px), str(master), "--out", str(iconset / name)],
                stdout=subprocess.DEVNULL,
            )
        subprocess.check_call(["iconutil", "-c", "icns", str(iconset), "-o", str(dest)])


def try_font(size: int) -> ImageFont.ImageFont:
    for name in (
        "/System/Library/Fonts/Supplemental/Arial Bold.ttf",
        "/System/Library/Fonts/Helvetica.ttc",
        "/Library/Fonts/Arial.ttf",
        "/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf",
    ):
        if os.path.exists(name):
            try:
                return ImageFont.truetype(name, size=size)
            except OSError:
                continue
    return ImageFont.load_default()


def draw_hiq(draw: ImageDraw.ImageDraw) -> None:
    # English peg-solitaire cross with center empty.
    holes = []
    for r in range(7):
        for c in range(7):
            if (r < 2 or r > 4) and (c < 2 or c > 4):
                continue
            holes.append((c, r))
    origin, step = 250, 75
    for c, r in holes:
        x = origin + c * step
        y = origin + r * step
        draw.ellipse([x - 18, y - 18, x + 18, y + 18], outline=SOFT, width=4)
        if not (c == 3 and r == 3):
            color = GOLD if (c, r) == (3, 1) else CYAN
            draw.ellipse([x - 14, y - 14, x + 14, y + 14], fill=color)


def draw_slidingpuzzle(draw: ImageDraw.ImageDraw) -> None:
    origin, cell, gap = 250, 150, 14
    tiles = [
        (0, 0, "1", CYAN),
        (1, 0, "2", GOLD),
        (2, 0, "3", CORAL),
        (0, 1, "4", GOLD),
        (1, 1, "5", CYAN),
        (2, 1, "6", CREAM),
        (0, 2, "7", CORAL),
        (1, 2, "8", CYAN),
    ]
    font = try_font(72)
    for c, r, label, color in tiles:
        x0 = origin + c * (cell + gap)
        y0 = origin + r * (cell + gap)
        draw.rounded_rectangle([x0, y0, x0 + cell, y0 + cell], radius=24, fill=color)
        bbox = draw.textbbox((0, 0), label, font=font)
        tw, th = bbox[2] - bbox[0], bbox[3] - bbox[1]
        draw.text((x0 + (cell - tw) / 2, y0 + (cell - th) / 2 - 6), label, fill=NAVY, font=font)
    # empty slot outline
    x0 = origin + 2 * (cell + gap)
    y0 = origin + 2 * (cell + gap)
    draw.rounded_rectangle([x0, y0, x0 + cell, y0 + cell], radius=24, outline=SOFT, width=6)


def draw_rubixcube(draw: ImageDraw.ImageDraw) -> None:
    # Isometric cube with three face colors.
    cx, cy = 512, 540
    s = 160
    top = [(cx, cy - s), (cx + s, cy - s // 2), (cx, cy), (cx - s, cy - s // 2)]
    right = [(cx, cy), (cx + s, cy - s // 2), (cx + s, cy + s // 2), (cx, cy + s)]
    left = [(cx, cy), (cx - s, cy - s // 2), (cx - s, cy + s // 2), (cx, cy + s)]
    draw.polygon(top, fill=GOLD)
    draw.polygon(right, fill=CORAL)
    draw.polygon(left, fill=CYAN)
    for face in (top, right, left):
        draw.line(face + [face[0]], fill=NAVY, width=8)
    # face subdivisions
    draw.line([(cx - s // 2, cy - s // 4), (cx + s // 2, cy - s // 4)], fill=NAVY, width=5)
    draw.line([(cx - s // 2, cy - 3 * s // 4), (cx + s // 2, cy - 3 * s // 4)], fill=NAVY, width=5)
    draw.line([(cx - s // 2, cy - 3 * s // 4), (cx - s // 2, cy + s // 4)], fill=NAVY, width=5)
    draw.line([(cx + s // 2, cy - 3 * s // 4), (cx + s // 2, cy + s // 4)], fill=NAVY, width=5)


def draw_sudoku(draw: ImageDraw.ImageDraw) -> None:
    origin, span = 240, 544
    cell = span / 9
    draw.rounded_rectangle([origin - 12, origin - 12, origin + span + 12, origin + span + 12], radius=28, fill=SOFT)
    draw.rectangle([origin, origin, origin + span, origin + span], fill=CREAM)
    for i in range(10):
        w = 8 if i % 3 == 0 else 3
        x = origin + i * cell
        draw.line([(x, origin), (x, origin + span)], fill=NAVY, width=w)
        draw.line([(origin, x), (origin + span, x)], fill=NAVY, width=w)
    font = try_font(42)
    clues = {(0, 0, "5"), (1, 2, "7"), (2, 4, "3"), (3, 1, "9"), (4, 4, "1"), (5, 7, "6"), (6, 3, "8"), (7, 6, "2"), (8, 8, "4")}
    for c, r, n in clues:
        x = origin + c * cell + cell / 2
        y = origin + r * cell + cell / 2
        bbox = draw.textbbox((0, 0), n, font=font)
        tw, th = bbox[2] - bbox[0], bbox[3] - bbox[1]
        draw.text((x - tw / 2, y - th / 2 - 4), n, fill=CORAL if (c + r) % 2 == 0 else CYAN, font=font)


def draw_tantrix(draw: ImageDraw.ImageDraw) -> None:
    def hex_pts(cx, cy, r):
        return [
            (cx + r * math.cos(math.radians(a)), cy + r * math.sin(math.radians(a)))
            for a in range(0, 360, 60)
        ]

    tiles = [(512, 380), (400, 560), (624, 560)]
    colors = [CYAN, GOLD, CORAL]
    for (cx, cy), color in zip(tiles, colors):
        pts = hex_pts(cx, cy, 130)
        draw.polygon(pts, fill=CREAM, outline=NAVY)
        draw.line(pts + [pts[0]], fill=NAVY, width=6)
        draw.arc([cx - 70, cy - 70, cx + 70, cy + 70], start=30, end=210, fill=color, width=18)
        draw.arc([cx - 40, cy - 40, cx + 40, cy + 40], start=200, end=380, fill=SOFT, width=12)


def draw_maze(draw: ImageDraw.ImageDraw) -> None:
    # Stylized maze corridors with a gold path through.
    walls = [
        (260, 260, 760, 300),
        (260, 260, 300, 760),
        (720, 260, 760, 560),
        (260, 720, 760, 760),
        (400, 400, 620, 440),
        (400, 400, 440, 620),
        (580, 480, 620, 720),
        (440, 580, 560, 620),
    ]
    for box in walls:
        draw.rectangle(list(box), fill=SOFT)
    path = [(340, 340), (680, 340), (680, 500), (500, 500), (500, 680), (680, 680)]
    draw.line(path, fill=GOLD, width=28, joint="curve")
    draw.ellipse([320, 320, 360, 360], fill=CYAN)
    draw.ellipse([660, 660, 700, 700], fill=CORAL)


def draw_redpuzzle(draw: ImageDraw.ImageDraw) -> None:
    # 3x3 interlocking piece silhouettes with suit-like nubs.
    origin, cell, gap = 260, 150, 18
    accents = [CYAN, GOLD, CORAL, GOLD, CYAN, CREAM, CORAL, CYAN, GOLD]
    for i, color in enumerate(accents):
        c, r = i % 3, i // 3
        x0 = origin + c * (cell + gap)
        y0 = origin + r * (cell + gap)
        draw.rounded_rectangle([x0, y0, x0 + cell, y0 + cell], radius=28, fill=color)
        # nub / notch accents
        draw.ellipse([x0 + cell - 22, y0 + cell / 2 - 18, x0 + cell + 14, y0 + cell / 2 + 18], fill=color)
        draw.ellipse([x0 + cell / 2 - 18, y0 - 14, x0 + cell / 2 + 18, y0 + 22], fill=NAVY)


def draw_bridge(draw: ImageDraw.ImageDraw) -> None:
    # Simple arched bridge + torch/flashlight beam.
    draw.polygon([(220, 700), (320, 520), (700, 520), (800, 700)], fill=SOFT)
    draw.arc([300, 480, 720, 760], start=200, end=340, fill=CREAM, width=28)
    draw.line([(260, 700), (760, 700)], fill=NAVY, width=10)
    # pillars
    draw.rectangle([360, 560, 400, 700], fill=GOLD)
    draw.rectangle([620, 560, 660, 700], fill=GOLD)
    # flashlight
    draw.ellipse([470, 400, 550, 480], fill=CORAL)
    draw.polygon([(510, 440), (780, 300), (780, 520)], fill=(232, 184, 74, 120))


def draw_twopails(draw: ImageDraw.ImageDraw) -> None:
    # Two containers at different fill levels with a pour arc.
    def pail(x, fill_h, rim_w=160):
        draw.polygon([(x, 320), (x + rim_w, 320), (x + rim_w - 30, 760), (x + 30, 760)], outline=CREAM, width=10)
        y_fill = 760 - fill_h
        draw.polygon(
            [(x + 18, y_fill), (x + rim_w - 18, y_fill), (x + rim_w - 36, 750), (x + 36, 750)],
            fill=CYAN,
        )

    pail(260, 280)
    pail(580, 140)
    draw.arc([400, 240, 620, 420], start=200, end=340, fill=GOLD, width=16)
    draw.ellipse([590, 300, 630, 340], fill=GOLD)


DRAWERS = {
    "hiq": draw_hiq,
    "slidingpuzzle": draw_slidingpuzzle,
    "rubixcube": draw_rubixcube,
    "sudoku": draw_sudoku,
    "tantrix": draw_tantrix,
    "maze": draw_maze,
    "redpuzzle": draw_redpuzzle,
    "bridge": draw_bridge,
    "twopails": draw_twopails,
}


def main() -> None:
    os.chdir(ROOT)
    for app_id, drawer in DRAWERS.items():
        img, draw = new_canvas()
        drawer(draw)
        save_all(app_id, img)
        print(f"wrote {app_id}")


if __name__ == "__main__":
    main()
