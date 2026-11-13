# Hi-Q board window scaling

**Date:** 2026-11-13  
**Status:** Approved for planning

## Problem

The Hi-Q peg board is drawn with fixed pixel constants in `PegBoardRenderer` (`INC = 10`, fixed margins and peg radii). `render` receives the viewer panel’s `width` and `height` but ignores them, so the board stays small and top-left anchored when the window is resized.

## Goal

When the Hi-Q app window (viewer panel) is resized, the board stays **square**, **scales to fit** the smaller usable dimension, and stays **centered** in the remaining area under the status text.

## Non-goals

- Scaling other puzzles’ renderers (e.g. sliding puzzle)
- Stretching the board into a non-square rectangle
- Scaling status-text font size
- Changing colors, peg appearance style, or solver behavior

## Approach

Compute layout from panel size inside `PegBoardRenderer.render` (same pattern as Sudoku / Two Pails). Do not use `Graphics2D` affine transforms for this change.

## Layout

1. **Status inset:** Reserve a fixed top band so the board does not overlap status text drawn by `PuzzleViewer` (use `TOP_STATUS_INSET = 50`, clearing the status baselines at roughly y≈29–43). Also keep a small uniform outer margin (`OUTER_MARGIN = 16`) on left/right/bottom.
2. **Usable rectangle:**  
   - `usableWidth = width - 2 * OUTER_MARGIN`  
   - `usableHeight = height - TOP_STATUS_INSET - OUTER_MARGIN`  
   Clamp both to at least 1 so a tiny window does not divide by zero or draw negative sizes.
3. **Cell pitch:** `cellSize = min(usableWidth, usableHeight) / PegBoard.SIZE` (integer division). Current code uses `3 * INC` as the pitch between grid lines; replace that with `cellSize`.
4. **Board extent:** `boardPixels = cellSize * PegBoard.SIZE`.
5. **Origin (centering):**  
   - `left = OUTER_MARGIN + (usableWidth - boardPixels) / 2`  
   - `top = TOP_STATUS_INSET + (usableHeight - boardPixels) / 2`
6. **Peg sizes (proportional to today’s look):**  
   - Filled peg diameter ≈ `0.53 * cellSize` (today: 16 of ~30 pitch)  
   - Empty hole diameter ≈ `0.30 * cellSize` (today: 9 of ~30 pitch)  
   Enforce a minimum diameter of 1 pixel when `cellSize` is very small.

## Drawing

- `drawGrid` draws the 8×8 hatch lines using `left`, `top`, and `cellSize`.
- `drawPegLocation` places each peg/hole centered in its cell using the scaled diameters.
- Colors stay as today (`FILLED_HOLE_COLOR`, `EMPTY_HOLE_COLOR`, dark gray grid).

## Change surface

| File | Change |
|------|--------|
| `scala-source/.../hiq/ui/PegBoardRenderer.scala` | Replace fixed layout constants with size-derived layout; use `width`/`height`. |
| Optional test | Pure layout helper test only if geometry is extracted to a testable function/object; not required if math stays private inside the renderer. |

`PegBoardViewer` already calls `renderer.render(g, board, getWidth, getHeight)` — no viewer API change.

## Verification

1. Launch Hi-Q, confirm initial board still looks correct (cross shape, center empty, purple pegs).
2. Enlarge the window: board grows, remains square, stays centered under status text.
3. Shrink the window: board shrinks without overlapping status text; no exceptions on very small sizes.
4. Existing Hi-Q unit tests still pass (`./gradlew test --tests 'com.barrybecker4.puzzle.hiq.*'`).
