// Copyright by Barry G. Becker, 2013-2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.hiq.ui

import java.awt._

import com.barrybecker4.puzzle.common.ui.PuzzleRenderer
import com.barrybecker4.puzzle.hiq.model.PegBoard

/**
  * Singleton class that takes a PieceList and renders it for the PegBoardViewer1.
  * Having the renderer separate from the viewer helps to separate out the rendering logic
  * from other features of the PegBoardViewer1.
  * @author Barry Becker
  */
object PegBoardRenderer {

  val INC = 10
  private val LEFT_MARGIN = 50
  private val TOP_MARGIN = 55
  private val FILLED_HOLE_COLOR = new Color(120, 0, 190)
  private val EMPTY_HOLE_COLOR = new Color(55, 55, 65, 150)
  private val FILLED_HOLE_RAD = 16
  private val EMPTY_HOLE_RAD = 9
}

/**
  * private constructor because this class is a singleton.
  * Use getPieceRenderer instead.
  */
class PegBoardRenderer extends PuzzleRenderer[PegBoard] {

  /** This renders the current state of the Slider to the screen. */
  def render(g: Graphics, board: PegBoard, width: Int, height: Int) {
    val size = PegBoard.SIZE
    val rightEdgePos = PegBoardRenderer.LEFT_MARGIN + 3 * PegBoardRenderer.INC * size
    val bottomEdgePos = PegBoardRenderer.TOP_MARGIN + 3 * PegBoardRenderer.INC * size
    drawGrid(g, size, rightEdgePos, bottomEdgePos)
    // now draw the pieces that we have so far
    for (row <- 0 until size; col <- 0 until size if PegBoard.isValidPosition(row, col))
      drawPegLocation(g, board, row.toByte, col.toByte)
  }

  /** draw the hatches which delineate the cells */
  private def drawGrid(g: Graphics, size: Int, rightEdgePos: Int, bottomEdgePos: Int) {
    var i = 0
    var ypos = 0
    var xpos = 0
    g.setColor(Color.darkGray)
    for (i <- 0 to size) { //   -----
        ypos = PegBoardRenderer.TOP_MARGIN + i * 3 * PegBoardRenderer.INC
        g.drawLine(PegBoardRenderer.LEFT_MARGIN, ypos, rightEdgePos, ypos)
    }
    for (i <- 0 to size) { //   ||||
        xpos = PegBoardRenderer.LEFT_MARGIN + i * 3 * PegBoardRenderer.INC
        g.drawLine(xpos, PegBoardRenderer.TOP_MARGIN, xpos, bottomEdgePos)
    }
  }

  private def drawPegLocation(g: Graphics, board: PegBoard, row: Byte, col: Byte) {
    var xpos = 0
    var ypos = 0
    xpos = PegBoardRenderer.LEFT_MARGIN + col * 3 * PegBoardRenderer.INC + PegBoardRenderer.INC / 3
    ypos = PegBoardRenderer.TOP_MARGIN + row * 3 * PegBoardRenderer.INC + 2 * PegBoardRenderer.INC / 3
    val empty = board.isEmpty(row, col)
    val c = if (empty) PegBoardRenderer.EMPTY_HOLE_COLOR
    else PegBoardRenderer.FILLED_HOLE_COLOR
    val r = if (empty) PegBoardRenderer.EMPTY_HOLE_RAD
    else PegBoardRenderer.FILLED_HOLE_RAD
    g.setColor(c)
    val rr = r / 2
    g.fillOval(xpos + PegBoardRenderer.INC - rr, ypos + PegBoardRenderer.INC - rr, r, r)
  }
}