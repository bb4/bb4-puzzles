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

  /** Draw the tile */
  def renderBorder(g2: Graphics2D, tilePlacement: TilePlacement, topLeftCorner: Location, radius: Double) {
    if (tilePlacement == null) return
    val isOddRow = tilePlacement.location.row % 2 == 1
    val location = tilePlacement.location.decrementOnCopy(topLeftCorner)
    val radD2 = radius / 2
    val xShift = location.col - (if (isOddRow) -0.25 else -0.75)
    val x = radD2 + xShift * 2 * radius * HexUtil.ROOT3D2
    val y = radD2 + TOP_MARGIN + ((location.row + 0.6) * 3.0 * radD2)
    val point = new Point(x.toInt, y.toInt)
    drawHexagon(g2, point, radius)
    drawPaths(g2, tilePlacement, point, radius)
    drawTileNumber(g2, tilePlacement, radius, x, y)
  }

  /** draw the outline for a tile */
  def renderBorder(g2: Graphics2D, loc: Location, topLeftCorner: Location, radius: Double) {

    val location = loc.decrementOnCopy(topLeftCorner)
    val isOddRow = loc.row % 2 == 1
    val radD2 = radius / 2
    val xShift = location.col - (if (isOddRow) -0.25 else -0.75)
    val x = radD2 + xShift * 2 * radius * HexUtil.ROOT3D2
    val y = radD2 + TOP_MARGIN + ((location.row + 0.6) * 3.0 * radD2)
    val point = new Point(x.toInt, y.toInt)
    drawHexagon(g2, point, 0.95 * radius, filled = false)
  }

  private def drawPaths(g2: Graphics2D, tilePlacement: TilePlacement, point: Point, radius: Double): Unit = {
    pathRenderer.drawPath(g2, 0, tilePlacement, point, radius)
    pathRenderer.drawPath(g2, 1, tilePlacement, point, radius)
    pathRenderer.drawPath(g2, 2, tilePlacement, point, radius)
  }

  private def drawTileNumber(g2: Graphics2D, tilePlacement: TilePlacement, radius: Double, x: Double, y: Double) {
    g2.setColor(Color.BLACK)
    g2.setFont(HexTileRenderer.TILE_FONT)
    val xpos = x.toInt - 2
    val ypos = (y +  0.95 * radius).toInt
    g2.drawString(FormatUtil.formatNumber(tilePlacement.tile.tantrixNumber), xpos, ypos)
    // also draw coordinate location
    val loc = tilePlacement.location
    g2.drawString(s"(${loc.row}, ${loc.col})", xpos - 15, ypos - 12)
  }

  private def drawHexagon(g2: Graphics2D, point: Point, radius: Double, filled: Boolean = true) {
    val numPoints = 7
    val xpoints = new Array[Int](numPoints)
    val ypoints = new Array[Int](numPoints)
    for (i <- 0 to 6) {
        val angStart = HexUtil.rad(30 + 60 * i) // was 30 +
        xpoints(i) = (point.getX + radius * Math.cos(angStart)).toInt
        ypoints(i) = (point.getY + radius * Math.sin(angStart)).toInt
    }
    val poly = new Polygon(xpoints, ypoints, numPoints)
    if (filled) {
      g2.setColor(HexTileRenderer.TILE_BG_COLOR)
      g2.fillPolygon(poly)
    }
    g2.setColor(HexTileRenderer.TILE_BORDER_COLOR)
    g2.setStroke(HexTileRenderer.TILE_STROKE)
    g2.drawPolygon(poly)
  }
}
