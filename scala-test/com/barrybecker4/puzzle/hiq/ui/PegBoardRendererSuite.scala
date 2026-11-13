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
    assert(layout.filledDiameter >= 1)
    assert(layout.emptyDiameter >= 1)
  }
}
