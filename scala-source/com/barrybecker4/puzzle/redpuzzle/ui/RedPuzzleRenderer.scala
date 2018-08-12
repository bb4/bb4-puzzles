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

import com.barrybecker4.puzzle.redpuzzle.model.Direction.Direction

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
  private val ARROW_HEAD_RAD = 2
  private val PIECE_TEXT_COLOR = new Color(200, 0, 0)
  private val PIECE_BACKGROUND_COLOR = new Color(255, 205, 215, 55)
  private val GRID_COLOR = new Color(10, 0, 100)
  private val TEXT_COLOR = new Color(0, 0, 0)
  private val NUB_FONT = new Font(GUIUtil.DEFAULT_FONT_FAMILY, Font.PLAIN, 12)
  private val TEXT_FONT = new Font(GUIUtil.DEFAULT_FONT_FAMILY, Font.BOLD, 18)
  // put this here to avoid reallocation during rendering.
  private val symb: Array[Char] = new Array[Char](1)
  // num pieces on edge
  private val DIM = Math.sqrt(PieceList.DEFAULT_NUM_PIECES).toInt

  /** draw the borders around each piece. */
  private def drawPieceBoundaryGrid(g: Graphics2D, dim: Int) {
    var xpos = 0
    var ypos = 0
    val rightEdgePos = MARGIN + PIECE_SIZE * dim
    val bottomEdgePos = MARGIN + PIECE_SIZE * dim
    // draw the hatches which deliniate the cells
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
  private def drawPiece(g: Graphics, p: OrientedPiece, col: Int, row: Int, nubChecks: Array[Array[Char]]) {
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

  private def drawNub(g: Graphics, nub: Nub,
                      xpos: Int, ypos: Int, dir: Direction, col: Int, row: Int, nubChecks: Array[Array[Char]]) {
    var x = 0
    var y = 0
    val outy = nub.isOuty
    symb(0) = nub.getSuitSymbol
    var ncx = 0
    var ncy = 0
    var cx = 0
    var cy = 0
    dir match {
      case Direction.TOP =>
        x = xpos + THIRD_SIZE
        y = if (outy) ypos - THIRD_SIZE
        else ypos
        ncx = 2 * col + 1
        ncy = 2 * row
        cx = xpos + THIRD_SIZE + 2
        cy = ypos - 2 * PIECE_SIZE / 9
      case Direction.RIGHT =>
        x = if (outy) xpos + PIECE_SIZE
        else xpos + 2 * THIRD_SIZE
        y = ypos + THIRD_SIZE
        ncx = 2 * col + 2
        ncy = 2 * row + 1
        cx = xpos + 8 * PIECE_SIZE / 9
        cy = ypos + 2 * PIECE_SIZE / 9
      case Direction.BOTTOM =>
        x = xpos + THIRD_SIZE
        y = if (outy) ypos + PIECE_SIZE
        else ypos + 2 * THIRD_SIZE
        ncx = 2 * col + 1
        ncy = 2 * row + 2
        cx = xpos + THIRD_SIZE + 2
        cy = ypos + PIECE_SIZE
      case Direction.LEFT =>
        x = if (outy) xpos - THIRD_SIZE
        else xpos
        y = ypos + THIRD_SIZE
        ncx = 2 * col
        ncy = 2 * row + 1
        cx = xpos - PIECE_SIZE / 9
        cy = ypos + 2 * PIECE_SIZE / 9
    }
    if (nubChecks(ncx)(ncy) == 0) nubChecks(ncx)(ncy) = symb(0)
    g.drawChars(symb, 0, 1, x, y)
    // draw a circle around nubs that are in conflict.
    if (nubChecks(ncx)(ncy) != symb(0)) {
      val diameter = PIECE_SIZE >> 1
      val rad = diameter >> 1
      g.drawOval(cx - rad, cy - rad, diameter, diameter)
    }
  }

  /** draw a marker line to indicate the orientation. */
  private def drawOrientationMarker(g: Graphics, p: OrientedPiece, xpos: Int, ypos: Int) {
    val len2 = ORIENT_ARROW_LEN >> 1
    var x1 = 0
    var y1 = 0
    var x2 = 0
    var y2 = 0
    var cx = 0
    var cy = 0
    val f = PIECE_SIZE / 7
    p.orientation match {
      case Direction.TOP =>
        x1 = xpos - len2 + 3 * f
        y1 = ypos + f
        x2 = xpos + len2 + 3 * f
        cx = x2
        y2 = ypos + f
        cy = y2
      case Direction.RIGHT =>
        x1 = xpos + 4 * f
        y1 = ypos - len2 + 2 * f
        x2 = xpos + 4 * f
        cx = x2
        y2 = ypos + len2 + 2 * f
        cy = y2
      case Direction.BOTTOM =>
        x1 = xpos - len2 + 3 * f
        cx = x1
        y1 = ypos + 3 * f
        cy = y1
        x2 = xpos + len2 + 3 * f
        y2 = ypos + 3 * f
      case Direction.LEFT =>
        x1 = xpos + 2 * f
        cx = x1
        y1 = ypos - len2 + 2 * f
        cy = y1
        x2 = xpos + 2 * f
        y2 = ypos + len2 + 2 * f
    }
    g.drawLine(x1, y1, x2, y2)
    val ahd2 = ARROW_HEAD_RAD >> 1
    g.drawOval(cx - ahd2, cy - ahd2, ARROW_HEAD_RAD, ARROW_HEAD_RAD)
  }
}

/**
  * private constructor because this class is a singleton.
  * Use getPieceRenderer instead.
  */
class RedPuzzleRenderer private[ui]() extends PuzzleRenderer[PieceList] {

  /** Renders the current state of the Slider to the screen. */
  def render(g: Graphics, board: PieceList, width: Int, height: Int) {
    RedPuzzleRenderer.drawPieceBoundaryGrid(g.asInstanceOf[Graphics2D], RedPuzzleRenderer.DIM)
    var i = 0
    // use this to determine of there is a nub mismatch a a given location
    // allocates a little more space tha we actually use, but simpler this way.
    val nubChecks = Array.ofDim[Char](7, 7)
    if (board == null) return
    for (i <- 0 until board.size) {
        val p = board.get(i)
        val row = i / RedPuzzleRenderer.DIM
        val col = i % RedPuzzleRenderer.DIM
        RedPuzzleRenderer.drawPiece(g, p, col, row, nubChecks)
    }
  }
}


