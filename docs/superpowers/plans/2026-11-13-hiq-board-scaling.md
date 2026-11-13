# Hi-Q Board Window Scaling Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Make the Hi-Q peg board stay square, scale to fit the viewer panel, and stay centered under the status text when the window resizes.

**Architecture:** Extract pure layout math (`BoardLayout` + `layoutFor`) on `PegBoardRenderer`’s companion object. `render` uses that layout for grid and peg drawing. Unit-test the layout helper; keep drawing in the existing renderer class.

**Tech Stack:** Scala 3, Java AWT `Graphics`, ScalaTest `AnyFunSuite`, Gradle.

**Spec:** `docs/superpowers/specs/2026-11-13-hiq-board-scaling-design.md`

## Global Constraints

- Square board; scale from `min(usableWidth, usableHeight)`; center in usable area
- `TOP_STATUS_INSET = 50`, `OUTER_MARGIN = 16`
- No affine transforms; no changes to other puzzles; colors unchanged
- Clamp usable dimensions to at least 1; peg diameters at least 1px

---

## File Structure

| File | Responsibility |
|------|----------------|
| `scala-source/com/barrybecker4/puzzle/hiq/ui/PegBoardRenderer.scala` | Layout math + drawing |
| `scala-test/com/barrybecker4/puzzle/hiq/ui/PegBoardRendererSuite.scala` | Layout unit tests |

---

### Task 1: Layout helper + failing tests

**Files:**
- Modify: `scala-source/com/barrybecker4/puzzle/hiq/ui/PegBoardRenderer.scala`
- Create: `scala-test/com/barrybecker4/puzzle/hiq/ui/PegBoardRendererSuite.scala`

**Interfaces:**
- Produces: `case class BoardLayout(left: Int, top: Int, cellSize: Int, filledDiameter: Int, emptyDiameter: Int)`
- Produces: `def layoutFor(width: Int, height: Int): BoardLayout` on `object PegBoardRenderer`

- [ ] **Step 1: Add `BoardLayout` and stub `layoutFor` that returns zeros (so tests compile and fail)**

In `PegBoardRenderer.scala` companion object, add:

```scala
case class BoardLayout(
  left: Int,
  top: Int,
  cellSize: Int,
  filledDiameter: Int,
  emptyDiameter: Int
)

val TOP_STATUS_INSET = 50
val OUTER_MARGIN = 16

/** Compute centered square board layout for the given panel size. */
def layoutFor(width: Int, height: Int): BoardLayout =
  BoardLayout(0, 0, 0, 0, 0)
```

Keep existing color constants. Leave `INC` and old margin/radius vals until Task 2 removes them (or remove unused ones when updating render).

- [ ] **Step 2: Write the failing tests**

Create `scala-test/com/barrybecker4/puzzle/hiq/ui/PegBoardRendererSuite.scala`:

```scala
package com.barrybecker4.puzzle.hiq.ui

import com.barrybecker4.puzzle.hiq.model.PegBoard
import org.scalatest.funsuite.AnyFunSuite

class PegBoardRendererSuite extends AnyFunSuite {

  test("layoutFor is square and centered in a wide panel") {
    val width = 800
    val height = 500
    val layout = PegBoardRenderer.layoutFor(width, height)
    val usableW = width - 2 * PegBoardRenderer.OUTER_MARGIN
    val usableH = height - PegBoardRenderer.TOP_STATUS_INSET - PegBoardRenderer.OUTER_MARGIN
    val expectedCell = math.min(usableW, usableH) / PegBoard.SIZE
    val boardPx = expectedCell * PegBoard.SIZE
    assert(layout.cellSize == expectedCell)
    assert(layout.left == PegBoardRenderer.OUTER_MARGIN + (usableW - boardPx) / 2)
    assert(layout.top == PegBoardRenderer.TOP_STATUS_INSET + (usableH - boardPx) / 2)
    assert(layout.filledDiameter == math.max(1, (0.53 * expectedCell).toInt))
    assert(layout.emptyDiameter == math.max(1, (0.30 * expectedCell).toInt))
  }

  test("layoutFor is square and centered in a tall panel") {
    val width = 400
    val height = 900
    val layout = PegBoardRenderer.layoutFor(width, height)
    val usableW = width - 2 * PegBoardRenderer.OUTER_MARGIN
    val usableH = height - PegBoardRenderer.TOP_STATUS_INSET - PegBoardRenderer.OUTER_MARGIN
    val expectedCell = math.min(usableW, usableH) / PegBoard.SIZE
    val boardPx = expectedCell * PegBoard.SIZE
    assert(layout.cellSize == expectedCell)
    assert(layout.left == PegBoardRenderer.OUTER_MARGIN + (usableW - boardPx) / 2)
    assert(layout.top == PegBoardRenderer.TOP_STATUS_INSET + (usableH - boardPx) / 2)
  }

  test("layoutFor clamps tiny panels without throwing") {
    val layout = PegBoardRenderer.layoutFor(10, 10)
    assert(layout.cellSize >= 0)
    assert(layout.filledDiameter >= 1 || layout.cellSize == 0)
    assert(layout.emptyDiameter >= 1 || layout.cellSize == 0)
  }
}
```

For the tiny-panel case, when `cellSize == 0`, diameters should still be `math.max(1, ...)` → 1. Prefer asserting:

```scala
assert(layout.filledDiameter >= 1)
assert(layout.emptyDiameter >= 1)
```

- [ ] **Step 3: Run tests to verify they fail**

Run: `./gradlew test --tests 'com.barrybecker4.puzzle.hiq.ui.PegBoardRendererSuite' --info`

Expected: FAIL (assertions on `cellSize` / `left` / `top` not matching stub zeros), or FAIL if suite not found until stub compiles.

- [ ] **Step 4: Implement `layoutFor`**

Replace the stub with:

```scala
def layoutFor(width: Int, height: Int): BoardLayout = {
  val usableWidth = math.max(1, width - 2 * OUTER_MARGIN)
  val usableHeight = math.max(1, height - TOP_STATUS_INSET - OUTER_MARGIN)
  val cellSize = math.min(usableWidth, usableHeight) / PegBoard.SIZE
  val boardPixels = cellSize * PegBoard.SIZE
  val left = OUTER_MARGIN + (usableWidth - boardPixels) / 2
  val top = TOP_STATUS_INSET + (usableHeight - boardPixels) / 2
  val filledDiameter = math.max(1, (0.53 * cellSize).toInt)
  val emptyDiameter = math.max(1, (0.30 * cellSize).toInt)
  BoardLayout(left, top, cellSize, filledDiameter, emptyDiameter)
}
```

- [ ] **Step 5: Run tests to verify they pass**

Run: `./gradlew test --tests 'com.barrybecker4.puzzle.hiq.ui.PegBoardRendererSuite'`

Expected: BUILD SUCCESSFUL, all tests PASS

- [ ] **Step 6: Commit**

```bash
git add scala-source/com/barrybecker4/puzzle/hiq/ui/PegBoardRenderer.scala \
        scala-test/com/barrybecker4/puzzle/hiq/ui/PegBoardRendererSuite.scala
git commit -m "$(cat <<'EOF'
Add Hi-Q board layout helper that scales with panel size.

EOF
)"
```

---

### Task 2: Wire `render` to use layout

**Files:**
- Modify: `scala-source/com/barrybecker4/puzzle/hiq/ui/PegBoardRenderer.scala`

**Interfaces:**
- Consumes: `PegBoardRenderer.layoutFor(width, height): BoardLayout`
- Produces: scaled drawing via existing `render(g, board, width, height)`

- [ ] **Step 1: Rewrite `PegBoardRenderer` class to use `BoardLayout`**

Replace the class body so `render` computes layout once and passes it to helpers. Full file target:

```scala
// Copyright by Barry G. Becker, 2013-2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.hiq.ui

import java.awt._

import com.barrybecker4.puzzle.common.ui.PuzzleRenderer
import com.barrybecker4.puzzle.hiq.model.PegBoard

/**
  * Layout constants and colors for drawing the peg board.
  */
object PegBoardRenderer {

  case class BoardLayout(
    left: Int,
    top: Int,
    cellSize: Int,
    filledDiameter: Int,
    emptyDiameter: Int
  )

  val TOP_STATUS_INSET = 50
  val OUTER_MARGIN = 16

  private val FILLED_HOLE_COLOR = new Color(120, 0, 190)
  private val EMPTY_HOLE_COLOR = new Color(55, 55, 65, 150)

  /** Compute centered square board layout for the given panel size. */
  def layoutFor(width: Int, height: Int): BoardLayout = {
    val usableWidth = math.max(1, width - 2 * OUTER_MARGIN)
    val usableHeight = math.max(1, height - TOP_STATUS_INSET - OUTER_MARGIN)
    val cellSize = math.min(usableWidth, usableHeight) / PegBoard.SIZE
    val boardPixels = cellSize * PegBoard.SIZE
    val left = OUTER_MARGIN + (usableWidth - boardPixels) / 2
    val top = TOP_STATUS_INSET + (usableHeight - boardPixels) / 2
    val filledDiameter = math.max(1, (0.53 * cellSize).toInt)
    val emptyDiameter = math.max(1, (0.30 * cellSize).toInt)
    BoardLayout(left, top, cellSize, filledDiameter, emptyDiameter)
  }
}

/** Renders a [[com.barrybecker4.puzzle.hiq.model.PegBoard]] to a [[java.awt.Graphics]] context. */
class PegBoardRenderer extends PuzzleRenderer[PegBoard] {

  /** Draws the board grid and pegs for the current state. */
  def render(g: Graphics, board: PegBoard, width: Int, height: Int): Unit = {
    val layout = PegBoardRenderer.layoutFor(width, height)
    val size = PegBoard.SIZE
    drawGrid(g, size, layout)
    for (row <- 0 until size; col <- 0 until size if PegBoard.isValidPosition(row, col))
      drawPegLocation(g, board, row.toByte, col.toByte, layout)
  }

  private def drawGrid(g: Graphics, size: Int, layout: PegBoardRenderer.BoardLayout): Unit = {
    val rightEdgePos = layout.left + layout.cellSize * size
    val bottomEdgePos = layout.top + layout.cellSize * size
    g.setColor(Color.DARK_GRAY)
    for (i <- 0 to size) {
      val ypos = layout.top + i * layout.cellSize
      g.drawLine(layout.left, ypos, rightEdgePos, ypos)
    }
    for (i <- 0 to size) {
      val xpos = layout.left + i * layout.cellSize
      g.drawLine(xpos, layout.top, xpos, bottomEdgePos)
    }
  }

  private def drawPegLocation(g: Graphics, board: PegBoard, row: Byte, col: Byte,
                              layout: PegBoardRenderer.BoardLayout): Unit = {
    val empty = board.isEmpty(row, col)
    val diameter = if (empty) layout.emptyDiameter else layout.filledDiameter
    val c = if (empty) PegBoardRenderer.EMPTY_HOLE_COLOR
            else PegBoardRenderer.FILLED_HOLE_COLOR
    val cx = layout.left + col * layout.cellSize + layout.cellSize / 2
    val cy = layout.top + row * layout.cellSize + layout.cellSize / 2
    val r = diameter / 2
    g.setColor(c)
    g.fillOval(cx - r, cy - r, diameter, diameter)
  }
}
```

Note: `FILLED_HOLE_COLOR` / `EMPTY_HOLE_COLOR` must be accessible from the class — either `private[ui]` or package-visible (`private` on the object still allows the companion class in Scala). Companion class can access object private members — keep them `private`.

- [ ] **Step 2: Re-run layout tests and full Hi-Q tests**

Run:

```bash
./gradlew test --tests 'com.barrybecker4.puzzle.hiq.*'
```

Expected: BUILD SUCCESSFUL

- [ ] **Step 3: Manual check (if desktop available)**

Run: `./gradlew runHiq` (or the project’s Hi-Q run task / `build/scripts/bb4-hiq`). Resize the window; board should grow/shrink, stay square and centered under status text.

- [ ] **Step 4: Commit**

```bash
git add scala-source/com/barrybecker4/puzzle/hiq/ui/PegBoardRenderer.scala
git commit -m "$(cat <<'EOF'
Scale Hi-Q peg board rendering with the window size.

EOF
)"
```

---

## Spec coverage (self-review)

| Spec requirement | Task |
|------------------|------|
| Square, fit min edge, centered | Task 1 `layoutFor` |
| TOP_STATUS_INSET=50, OUTER_MARGIN=16 | Task 1 |
| Clamp usable ≥ 1; diameter ≥ 1 | Task 1 |
| drawGrid / drawPeg use layout | Task 2 |
| Colors unchanged | Task 2 |
| PegBoardViewer unchanged | (no task needed) |
| Hi-Q tests pass | Task 2 Step 2 |
