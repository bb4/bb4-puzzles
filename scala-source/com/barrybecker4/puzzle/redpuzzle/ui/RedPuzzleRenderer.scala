// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT

package com.barrybecker4.puzzle.redpuzzle.ui

import com.barrybecker4.common.format.FormatUtil
import com.barrybecker4.puzzle.common.ui.PuzzleRenderer
import com.barrybecker4.puzzle.redpuzzle.model._
import com.barrybecker4.ui.util.GUIUtil
import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.Graphics2D

/**
  * Singleton class that takes a PieceList and renders it for the RedPuzzleViewer.
  * Having the renderer separate from the viewer helps to separate out the rendering logic
  * from other features of the RedPuzzleViewer.
  * @author Barry Becker
  */
object RedPuzzleRenderer {
  /** size of piece in pixels. */
  private[ui] val PIECE_SIZE = 90
  private val THIRD_SIZE = PIECE_SIZE / 3
  private val MARGIN = 75
  private val ORIENT_ARROW_LEN = PIECE_SIZE >> 2
  private val ARROW_HEAD_RAD = 4
  private val PIECE_TEXT_COLOR = new Color(200, 0, 0)
  private val PIECE_BACKGROUND_COLOR = new Color(255, 205, 215, 55)
  private val GRID_COLOR = new Color(10, 0, 100)
  private val TEXT_COLOR = new Color(0, 0, 0)
  private val NUB_FONT = new Font(GUIUtil.DEFAULT_FONT_FAMILY, Font.PLAIN, 12)
  private val TEXT_FONT = new Font(GUIUtil.DEFAULT_FONT_FAMILY, Font.BOLD, 18)
  // put this here to avoid reallocation during rendering.
  private val symb: Array[Char] = new Array[Char](1)

  /** draw the borders around each piece. */
  private def drawPieceBoundaryGrid(g: Graphics2D, dim: Int): Unit = {
    var xpos = 0
    var ypos = 0
    val rightEdgePos = MARGIN + PIECE_SIZE * dim
    val bottomEdgePos = MARGIN + PIECE_SIZE * dim
    // draw the hatches which delineate the cells
    g.setColor(GRID_COLOR)
    for (i <- 0 to dim) { //   -----
        ypos = MARGIN + i * PIECE_SIZE
        g.drawLine(MARGIN, ypos, rightEdgePos, ypos)
    }
    for (i <- 0 to dim) { //   ||||
        xpos = MARGIN + i * PIECE_SIZE
        g.drawLine(xpos, MARGIN, xpos, bottomEdgePos)
    }
  }

  /** Draw a puzzle piece at the specified location. */
  private def drawPiece(g: Graphics, p: OrientedPiece, col: Int, row: Int, nubChecks: Array[Array[Char]]): Unit = {
    val xpos = MARGIN + col * PIECE_SIZE + PIECE_SIZE / 9
    val ypos = MARGIN + row * PIECE_SIZE + 2 * PIECE_SIZE / 9
    g.setColor(PIECE_BACKGROUND_COLOR)
    g.fillRect(xpos - PIECE_SIZE / 9 + 2, ypos - 2 * PIECE_SIZE / 9 + 1, PIECE_SIZE - 3, PIECE_SIZE - 2)
    g.setColor(PIECE_TEXT_COLOR)
    g.setFont(NUB_FONT)
    // now draw the pieces that we have so far.
    drawNub(g, p.getTopNub, xpos, ypos, Direction.TOP, col, row, nubChecks)
    drawNub(g, p.getRightNub, xpos, ypos, Direction.RIGHT, col, row, nubChecks)
    drawNub(g, p.getBottomNub, xpos, ypos, Direction.BOTTOM, col, row, nubChecks)
    drawNub(g, p.getLeftNub, xpos, ypos, Direction.LEFT, col, row, nubChecks)
    drawOrientationMarker(g, p, xpos, ypos)
    // draw the number in the middle
    g.setColor(TEXT_COLOR)
    g.setFont(TEXT_FONT)
    val num = p.piece.pieceNumber
    g.drawString(FormatUtil.formatNumber(num), xpos + THIRD_SIZE, ypos + THIRD_SIZE)
  }

  /** Text position, grid check indices, and circle center for nub rendering. */
  private def nubLayout(dir: Direction, outy: Boolean, xpos: Int, ypos: Int, col: Int, row: Int)
  : (Int, Int, Int, Int, Int, Int) =
    dir match {
      case Direction.TOP =>
        (xpos + THIRD_SIZE,
          if (outy) ypos - THIRD_SIZE else ypos,
          2 * col + 1, 2 * row,
          xpos + THIRD_SIZE + 2, ypos - 2 * PIECE_SIZE / 9)
      case Direction.RIGHT =>
        ((if (outy) xpos + PIECE_SIZE else xpos + 2 * THIRD_SIZE),
          ypos + THIRD_SIZE,
          2 * col + 2, 2 * row + 1,
          xpos + 8 * PIECE_SIZE / 9, ypos + 2 * PIECE_SIZE / 9)
      case Direction.BOTTOM =>
        (xpos + THIRD_SIZE,
          if (outy) ypos + PIECE_SIZE else ypos + 2 * THIRD_SIZE,
          2 * col + 1, 2 * row + 2,
          xpos + THIRD_SIZE + 2, ypos + PIECE_SIZE)
      case Direction.LEFT =>
        ((if (outy) xpos - THIRD_SIZE else xpos),
          ypos + THIRD_SIZE,
          2 * col, 2 * row + 1,
          xpos - PIECE_SIZE / 9, ypos + 2 * PIECE_SIZE / 9)
    }

  private def drawNub(g: Graphics, nub: Nub,
                      xpos: Int, ypos: Int, dir: Direction, col: Int, row: Int, nubChecks: Array[Array[Char]]): Unit = {
    symb(0) = nub.getSuitSymbol
    val (x, y, ncx, ncy, cx, cy) = nubLayout(dir, nub.isOuty, xpos, ypos, col, row)
    if (nubChecks(ncx)(ncy) == 0) nubChecks(ncx)(ncy) = symb(0)
    g.drawChars(symb, 0, 1, x, y)
    if (nubChecks(ncx)(ncy) != symb(0)) {
      val diameter = PIECE_SIZE >> 1
      val rad = diameter >> 1
      g.drawOval(cx - rad, cy - rad, diameter, diameter)
    }
  }

  /** Line endpoints and arrowhead center for orientation marker. */
  private def orientationMarkerGeometry(orientation: Direction, xpos: Int, ypos: Int): (Int, Int, Int, Int, Int, Int) = {
    val len2 = ORIENT_ARROW_LEN >> 1
    val f = PIECE_SIZE / 7
    orientation match {
      case Direction.TOP =>
        val x1 = xpos - len2 + 3 * f
        val y1 = ypos + f
        val x2 = xpos + len2 + 3 * f
        val y2 = ypos + f
        (x1, y1, x2, y2, x2, y2)
      case Direction.RIGHT =>
        val x1 = xpos + 4 * f
        val y1 = ypos - len2 + 2 * f
        val x2 = xpos + 4 * f
        val y2 = ypos + len2 + 2 * f
        (x1, y1, x2, y2, x2, y2)
      case Direction.BOTTOM =>
        val x1 = xpos - len2 + 3 * f
        val y1 = ypos + 3 * f
        val x2 = xpos + len2 + 3 * f
        val y2 = ypos + 3 * f
        (x1, y1, x2, y2, x1, y1)
      case Direction.LEFT =>
        val x1 = xpos + 2 * f
        val y1 = ypos - len2 + 2 * f
        val x2 = xpos + 2 * f
        val y2 = ypos + len2 + 2 * f
        (x1, y1, x2, y2, x1, y1)
    }
  }

  /** draw a marker line to indicate the orientation. */
  private def drawOrientationMarker(g: Graphics, p: OrientedPiece, xpos: Int, ypos: Int): Unit = {
    val (x1, y1, x2, y2, cx, cy) = orientationMarkerGeometry(p.orientation, xpos, ypos)
    g.drawLine(x1, y1, x2, y2)
    val ahd2 = ARROW_HEAD_RAD >> 1
    g.drawOval(cx - ahd2, cy - ahd2, ARROW_HEAD_RAD, ARROW_HEAD_RAD)
  }
}

/** private constructor because this class is a singleton.
  * Use getPieceRenderer instead.
  */
class RedPuzzleRenderer private[ui]() extends PuzzleRenderer[PieceList] {

  /** Renders the current state of the Slider to the screen. */
  def render(g: Graphics, board: PieceList, width: Int, height: Int): Unit = {
    if (board == null) return
    val dim = board.edgeLength
    RedPuzzleRenderer.drawPieceBoundaryGrid(g.asInstanceOf[Graphics2D], dim)
    // use this to determine of there is a nub mismatch a a given location
    // allocates a little more space tha we actually use, but simpler this way.
    val nubChecks = Array.ofDim[Char](7, 7)
    for (i <- 0 until board.size) {
        val p = board.get(i)
        val row = i / dim
        val col = i % dim
        RedPuzzleRenderer.drawPiece(g, p, col, row, nubChecks)
    }
  }
}

