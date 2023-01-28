// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.ui.rendering

import java.awt.*
import com.barrybecker4.common.geometry.{Box, IntLocation, Location}
import com.barrybecker4.puzzle.common.ui.PuzzleRenderer
import com.barrybecker4.puzzle.tantrix.model.{BoundingBoxCalculator, TantrixBoard, TilePlacement}
import com.barrybecker4.puzzle.tantrix.ui.rendering.HexUtil.ROOT3
import com.barrybecker4.puzzle.tantrix.ui.rendering.TantrixBoardRenderer.*
import scala.Iterable

/**
  * Renders the the tantrix puzzle onscreen.
  *
  * @author Barry Becker
  */
object TantrixBoardRenderer {
  private val MARGIN_FRAC = 0.2
  private[rendering] val TOP_MARGIN = 30
  private val GRID_COLOR = new Color(130, 140, 170)
  private val MIN_EDGE_LEN = 5
}

class TantrixBoardRenderer() extends PuzzleRenderer[TantrixBoard] {

  private val tileRenderer = new HexTileRenderer
  private var hexRadius = .0
  private var edgeLen: Int = _
  private val boundingBoxCalculator = BoundingBoxCalculator()

  def render(g: Graphics, board: TantrixBoard, width: Int, height: Int): Unit = {
    if (board != null)
      render(g, board.tantrix.tiles, width, height)
  }

  /**
    * This renders the current state of the TantrixBoard to the screen.
    */
  def render(g: Graphics, tiles: Iterable[TilePlacement], width: Int, height: Int): Unit = {
    if (tiles == null) return
    val g2 = g.asInstanceOf[Graphics2D]
    val bbox: Box = boundingBoxCalculator.getBoundingBox(tiles.toSeq)
    val boardEdgeLength = bbox.getMaxDimension
    val minEdge = Math.min(width, height)

    edgeLen = Math.max(MIN_EDGE_LEN, bbox.getMaxDimension)
    val padding = Math.max(0, MIN_EDGE_LEN - boardEdgeLength) / 2
    hexRadius = (1.0 - MARGIN_FRAC) * minEdge / (edgeLen * ROOT3 * .9)
    setHints(g2)

    val topLeftCorner = bbox.getTopLeftCorner.incrementOnCopy(-padding, -padding)
    drawGrid(g2, topLeftCorner)

    for (tile <- tiles) {
      tileRenderer.renderBorder(g2, tile, topLeftCorner, hexRadius)
    }
  }

  private def setHints(g2: Graphics2D): Unit = {
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
  }

  /**
    * Draw the gridlines over the background.
    */
  protected def drawGrid(g2: Graphics2D, topLeftCorner: Location): Unit = {
    //val margin = (hexRadius / 2.0).toInt
    //val hexWidth = ROOT3 * hexRadius
    //val rightEdgePos = (margin + hexWidth * edgeLen).toInt
    //val bottomEdgePos = (TOP_MARGIN + margin + hexWidth * edgeLen).toInt

    g2.setColor(GRID_COLOR)

    val bottomRightCorner = topLeftCorner.incrementOnCopy(edgeLen, edgeLen)
    for {
      i <- topLeftCorner.getY to bottomRightCorner.getY
      j <- topLeftCorner.getX to bottomRightCorner.getX
    } {
      tileRenderer.renderBorder(g2, IntLocation(i, j), topLeftCorner, hexRadius)
    }
  }
}
