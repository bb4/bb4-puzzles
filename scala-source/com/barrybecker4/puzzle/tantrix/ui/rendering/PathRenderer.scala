// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.ui.rendering

import java.awt._
import java.awt.geom.Point2D

import com.barrybecker4.puzzle.tantrix.model.PathColor.PathColor
import com.barrybecker4.puzzle.tantrix.model.{HexTile, TilePlacement}
import com.barrybecker4.puzzle.tantrix.ui.rendering.HexUtil._
import com.barrybecker4.puzzle.tantrix.ui.rendering.PathColorInterpreter.getColorForPathColor

/**
  * Renders a single tantrix tile.
  *
  * @author Barry Becker
  */
object PathRenderer {
  private val PATH_BORDER_COLOR = new Color(10, 10, 10)
  private val PATH_FRAC = 0.8f

  private def getPathStroke(thickness: Double) = {
    new BasicStroke((PATH_FRAC * thickness).toInt, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL)
  }

  private def getPathBGStroke(thickness: Double) = new BasicStroke(thickness.toInt, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL)
}

class PathRenderer private[rendering]() {
  /**
    * Draw one of the tile paths which takes one of three forms: corner, curved, or straight
    */
  private[rendering] def drawPath(g2: Graphics2D, pathNumber: Int,
                                  tilePlacement: TilePlacement, position: Point, size: Double) {
    val tile: HexTile = tilePlacement.tile
    var pathStartIndex = getPathStartIndex(tile, pathNumber)
    var i = pathStartIndex + 1
    val pathColor = tile.edgeColors(pathStartIndex)
    while (pathColor != tile.edgeColors(i)) {
      assert(i < 6, "Should never exceed 6")
      i += 1
    }

    var pathEndIndex = i - 1
    val diff = pathEndIndex - pathStartIndex
    val color = getColorForPathColor(pathColor)
    // account for the rotation.
    pathStartIndex += tilePlacement.rotation.ordinal
    pathEndIndex += tilePlacement.rotation.ordinal
    diff match {
      case 0 => println("Diff unexpectedly 0. PathStartIdx = " + pathStartIndex +
        " pathEndIdx = " + pathEndIndex + " Position = " + position + " pathColor = " + pathColor)
      case 1 => drawCornerPath(g2, position, pathStartIndex, color, size)
      case 5 => drawCornerPath(g2, position, pathEndIndex, color, size)
      case 2 => drawCurvedPath(g2, position, pathStartIndex, color, size)
      case 4 => drawCurvedPath(g2, position, pathEndIndex, color, size)
      case 3 => drawStraightPath(g2, position, pathStartIndex, color, size)
    }
  }

  /**
    * @return index corresponding to the side that the path starts on.
    */
  private def getPathStartIndex(tile: HexTile, pathNumber: Int) = {
    var set: Set[PathColor] = Set()
    var i = 0
    do {
      val c = tile.edgeColors(i)
      set += c
      i += 1
    } while (set.size <= pathNumber)
    i - 1
  }

  private def drawCornerPath(g2: Graphics2D, position: Point, firstIndex: Int, color: Color, radius: Double) {
    val startAngle = firstIndex * 60 + 60
    val angle = 120
    val rstartAng = rad(startAngle - 30)
    val center = new Point((position.getX + radius * Math.cos(rstartAng)).toInt, (position.getY - radius * Math.sin(rstartAng)).toInt)
    drawPathArc(g2, center, color, radius, radius / 3.0, startAngle + 90, angle)
  }

  private def drawCurvedPath(g2: Graphics2D, position: Point, firstIndex: Int, color: Color, radius: Double) {
    val startAngle = firstIndex * 60 + 60
    val angle = 60
    val rstartAng = HexUtil.rad(startAngle)
    val rad = 2 * radius * ROOT3D2
    val center = new Point((position.getX + rad * Math.cos(rstartAng)).toInt, (position.getY - rad * Math.sin(rstartAng)).toInt)
    drawPathArc(g2, center, color, ROOT3 * rad, radius / 3.0, startAngle + 150, angle)
  }

  private def drawPathArc(g2: Graphics2D, center: Point, color: Color, radius: Double, thickness: Double, startAngle: Int, angle: Int) {
    // the black border for the path
    g2.setColor(PathRenderer.PATH_BORDER_COLOR)
    g2.setStroke(PathRenderer.getPathBGStroke(thickness))
    val s = radius.toInt
    val c = new Point((center.getX - radius / 2).toInt, (center.getY - radius / 2).toInt)
    g2.drawArc(c.getX.toInt, c.getY.toInt, s, s, startAngle, angle)
    // now the colored path
    g2.setColor(color)
    g2.setStroke(PathRenderer.getPathStroke(thickness))
    //g2.drawLine((int)center.getX(), (int)center.getY(),(int)center.getX(), (int)center.getY());
    g2.drawArc(c.getX.toInt, c.getY.toInt, s, s, startAngle, angle)
  }

  private def drawStraightPath(g2: Graphics2D, position: Point2D, firstIndex: Int, color: Color, radius: Double) {
    val theta1 = rad(-firstIndex * 60)
    val theta2 = rad(-firstIndex * 60 + 180)
    val halfWidth = radius * ROOT3D2
    val startX = (position.getX + halfWidth * Math.cos(theta1)).toInt
    val startY = (position.getY + halfWidth * Math.sin(theta1) - 1).toInt
    val endX = (position.getX + halfWidth * Math.cos(theta2)).toInt
    val endY = (position.getY + halfWidth * Math.sin(theta2) - 1).toInt
    g2.setColor(PathRenderer.PATH_BORDER_COLOR)
    g2.setStroke(PathRenderer.getPathBGStroke(radius / 3.0))
    g2.drawLine(startX, startY, endX, endY)
    g2.setColor(color)
    g2.setStroke(PathRenderer.getPathStroke(radius / 3.0))
    g2.drawLine(startX, startY, endX, endY)
  }
}
