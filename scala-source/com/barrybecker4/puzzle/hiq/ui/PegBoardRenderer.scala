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
