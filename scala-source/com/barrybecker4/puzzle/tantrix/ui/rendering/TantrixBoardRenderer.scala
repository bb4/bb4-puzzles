// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.ui.rendering

import java.awt._

import com.barrybecker4.puzzle.common.PuzzleRenderer
import com.barrybecker4.puzzle.tantrix.model.{TantrixBoard, TilePlacement}
import com.barrybecker4.puzzle.tantrix.ui.rendering.HexUtil.ROOT3

/**
  * Renders the the tantrix puzzle onscreen.
  *
  * @author Barry Becker
  */
object TantrixBoardRenderer {
  private val MARGIN_FRAC = 0.2
  private[rendering] val TOP_MARGIN = 15
  private val GRID_COLOR = new Color(130, 140, 170)
}

class TantrixBoardRenderer() extends PuzzleRenderer[TantrixBoard] {

  private var tileRenderer = new HexTileRenderer
  private var hexRadius = .0

  /**
    * This renders the current state of the TantrixBoard to the screen.
    */
  def render(g: Graphics, board: TantrixBoard, width: Int, height: Int) {
    if (board == null) return
    val g2 = g.asInstanceOf[Graphics2D]
    val minEdge = Math.min(width, height)
    hexRadius = (1.0 - TantrixBoardRenderer.MARGIN_FRAC) * minEdge / (board.getEdgeLength * ROOT3 * .9)
    setHints(g2)
    drawGrid(g2, board)
    val topLeftCorner = board.getBoundingBox.getTopLeftCorner
    for (loc <- board.getTantrixLocations) {
      val placement: Option[TilePlacement] = board.getTilePlacement(loc)
      tileRenderer.render(g2, placement.get, topLeftCorner, hexRadius)
    }
  }

  private def setHints(g2: Graphics2D) {
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
  }

  /**
    * Draw the gridlines over the background.
    */
  protected def drawGrid(g2: Graphics2D, board: TantrixBoard) {
    val edgeLen = board.getEdgeLength
    var xpos = 0
    var ypos = 0
    var i = 0
    val start = 0
    val margin = (hexRadius / 2.0).toInt
    val hexWidth = ROOT3 * hexRadius
    val rightEdgePos = (margin + hexWidth * edgeLen).toInt
    val bottomEdgePos = (TantrixBoardRenderer.TOP_MARGIN + margin + hexWidth * edgeLen).toInt
    g2.setColor(TantrixBoardRenderer.GRID_COLOR)
    for (i <- start to edgeLen) {
      //   -----
      ypos = (TantrixBoardRenderer.TOP_MARGIN + margin + i * hexWidth).toInt
      g2.drawLine(margin, ypos, rightEdgePos, ypos)
    }
    for (i <- start to edgeLen) {
      //   ||||
      xpos = (margin + i * hexWidth).toInt
      g2.drawLine(xpos, TantrixBoardRenderer.TOP_MARGIN + margin, xpos, bottomEdgePos)
    }
  }
}