// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.ui.rendering

import java.awt._

import com.barrybecker4.common.format.FormatUtil
import com.barrybecker4.common.geometry.Location
import com.barrybecker4.puzzle.tantrix.model.TilePlacement
import com.barrybecker4.puzzle.tantrix.ui.rendering.TantrixBoardRenderer.TOP_MARGIN
import com.barrybecker4.ui.util.GUIUtil

object HexTileRenderer {
  private val TILE_FONT = new Font(GUIUtil.DEFAULT_FONT_FAMILY, Font.PLAIN, 9)
  private val TILE_STROKE = new BasicStroke(1)
  private val TILE_BORDER_COLOR = new Color(70, 70, 70)
  private val TILE_BG_COLOR = new Color(200, 200, 200)
}

/**
  * Renders a single tantrix tile.
  *
  * @author Barry Becker
  */
class HexTileRenderer() {
  private val pathRenderer = new PathRenderer

  /**
    * Draw the poker hand (the cards are all face up or all face down)
    */
  def render(g2: Graphics2D, tilePlacement: TilePlacement, topLeftCorner: Location, radius: Double) {
    if (tilePlacement == null) return
    val isOddRow = tilePlacement.location.getRow % 2 == 1
    val location = tilePlacement.location.decrementOnCopy(topLeftCorner)
    val x = radius / 2 + ((location.getCol - (if (isOddRow) -0.25
    else -0.75)) * 2 * radius * HexUtil.ROOT3D2)
    val y = radius / 2 + TOP_MARGIN + ((location.getRow + 0.6) * 3.0 * radius / 2.0)
    val point = new Point(x.toInt, y.toInt)
    drawHexagon(g2, point, radius)
    pathRenderer.drawPath(g2, 0, tilePlacement, point, radius)
    pathRenderer.drawPath(g2, 1, tilePlacement, point, radius)
    pathRenderer.drawPath(g2, 2, tilePlacement, point, radius)
    drawTileNumber(g2, tilePlacement, radius, x, y)
  }

  private def drawTileNumber(g2: Graphics2D, tilePlacement: TilePlacement, radius: Double, x: Double, y: Double) {
    g2.setColor(Color.BLACK)
    g2.setFont(HexTileRenderer.TILE_FONT)
    g2.drawString(FormatUtil.formatNumber(tilePlacement.tile.tantrixNumber), x.toInt, (y + radius / 2).toInt)
  }

  private def drawHexagon(g2: Graphics2D, point: Point, radius: Double) {
    val numPoints = 7
    val xpoints = new Array[Int](numPoints)
    val ypoints = new Array[Int](numPoints)
    for (i <- 0 to 6) {
        val angStart = HexUtil.rad(30 + 60 * i)
        xpoints(i) = (point.getX + radius * Math.cos(angStart)).toInt
        ypoints(i) = (point.getY + radius * Math.sin(angStart)).toInt
    }
    val poly = new Polygon(xpoints, ypoints, numPoints)
    g2.setColor(HexTileRenderer.TILE_BG_COLOR)
    g2.fillPolygon(poly)
    g2.setColor(HexTileRenderer.TILE_BORDER_COLOR)
    g2.setStroke(HexTileRenderer.TILE_STROKE)
    g2.drawPolygon(poly)
  }
}
