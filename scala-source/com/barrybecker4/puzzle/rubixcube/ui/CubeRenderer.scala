// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.ui

import com.barrybecker4.puzzle.common.ui.PuzzleRenderer
import com.barrybecker4.puzzle.rubixcube.model.{Cube, FRONT, LEFT, TOP}
import com.barrybecker4.puzzle.rubixcube.ui.CubeRenderer._
import com.barrybecker4.puzzle.rubixcube.model.FaceColor._

import java.awt.{BasicStroke, Color, Font, Graphics, Graphics2D}


/**
  * Singleton class that renders a Rubix cube in the CubeViewer.
  * It draws 2 orthogonal projections of the cube - one from the front and one from back
  * That way all 6 sides can be seen at once.
  */
object CubeRenderer {
  type Point = (Float, Float)

  private val INC = 60
  private val LEFT_MARGIN = 40
  private val TOP_MARGIN = 55
  private val LINE_COLOR = new Color(5, 25, 25)
  private val LINE_STROKE = new BasicStroke(2)
  private val FONT = new Font("Sans Serif", Font.PLAIN, INC / 2)
  private val root5 = Math.sqrt(5).toFloat
  
  // these points form a hexagon with A at the center
  private val A: Point = (2, 2)
  private val B: Point = (2, 0)
  private val C: Point = (0, 1 + root5)
  private val D: Point = (4, 1 + root5)
  private val E: Point = (0, 1)
  private val F: Point = (4, 1)
  private val G: Point = (2, 2 + root5)

  private val TOP_FACE_POLY: Array[Point] = Array(A, E, B, F)
  private val LEFT_FACE_POLY: Array[Point] = Array(A, G, C, E)
  private val FRONT_FACE_POLY: Array[Point] = Array(A, F, D, G)
}



class CubeRenderer extends PuzzleRenderer[Cube] {

  private var size: Int = 0
  private var scaleX: Float = 0
  private var scaleY: Float = 0

  /** This renders the current state of the Cube to the screen. */
  def render(g: Graphics, cube: Cube, width: Int, height: Int): Unit = {
    size = cube.size
    scaleX = width / 10.0f
    scaleY = height / 5.0f
    
    // draw 3 sides (their outline forms a hexagon)
    val g2 = g.asInstanceOf[Graphics2D]
    drawFace(g2, cube.getFace(TOP), TOP_FACE_POLY)
    drawFace(g2, cube.getFace(LEFT), LEFT_FACE_POLY)
    drawFace(g2, cube.getFace(FRONT), FRONT_FACE_POLY)

    // draw the 3 sides on the other side (facing away)
  }

  /** @param face the 2d positions and colors of the squares on the face
    * @param points 4 points, in clockwise order, starting from the origin,
    *               which define a quadrilateral in which the grid of colored squares will be drawn.
    */
  private def drawFace(g2: Graphics2D, face: Map[(Int, Int), FaceColor], points: Array[Point]): Unit = {

    // fill the face's colored squares
    drawFaceSquares(g2, face, points)

    // f draw grid based on the 4 points
    drawFaceLines(g2, points.head, points(3), points(1)) // horz lines that interpolate from A->E and F->B
    drawFaceLines(g2, points(1), points(0), points(2)) // vert lines
  }

  private def drawFaceLines(g2: Graphics2D,
    leftStartPt: Point, rightStartPt: Point, upperLeftPt: Point): Unit = {

    val x1 = leftStartPt._1
    val y1 = leftStartPt._2
    val x2 = upperLeftPt._1
    val y2 = upperLeftPt._2
    val delta = getDelta(leftStartPt, rightStartPt)
    g2.setStroke(LINE_STROKE)
    g2.setColor(LINE_COLOR)

    for (i <- 0 to size) {
      val xinc: Float = i * delta._1
      val yinc: Float = i * delta._2
      val xx1 = LEFT_MARGIN + (scaleX * (x1 + xinc)).toInt
      val yy1 = TOP_MARGIN + (scaleY * (y1 + yinc)).toInt
      val xx2 = LEFT_MARGIN + (scaleX * (x2 + xinc)).toInt
      val yy2 = TOP_MARGIN + (scaleY * (y2 + yinc)).toInt
      g2.drawLine(xx1, yy1, xx2, yy2)
    }
  }

  private def drawFaceSquares(g2: Graphics2D, face: Map[(Int, Int), FaceColor], points: Array[Point]): Unit = {

    val rowDelta = getDelta(points.head, points(3))
    val colDelta = getDelta(points.head, points(1))
    val baseX = points.head._1
    val baseY = points.head._2

    for (i <- 0 until size) {
      for (j <- 0 until size) {

        val x1 = LEFT_MARGIN + (scaleX * (baseX + i * colDelta._1 + j * rowDelta._1 )).toInt
        val x2 = LEFT_MARGIN + (scaleX * (baseX + (i + 1) * colDelta._1 + j * rowDelta._1)).toInt
        val x3 = (x2 + scaleX * rowDelta._1).toInt
        val x4 = (x1 + scaleX * rowDelta._1).toInt

        val y1 = TOP_MARGIN + (scaleY * (baseY + i * colDelta._2 + j * rowDelta._2)).toInt
        val y2 = TOP_MARGIN + (scaleY * (baseY + (i + 1) * colDelta._2 + j * rowDelta._2)).toInt
        val y3 = (y2 + scaleY * rowDelta._2).toInt
        val y4 = (y1 + scaleY * rowDelta._2).toInt

        val xpoints = Array(x1, x2, x3, x4)
        val ypoints = Array(y1, y2, y3, y4)

        val loc = (i + 1, j + 1)
        g2.setColor(FaceColorMap.getColor(face(loc)))
        g2.fillPolygon(xpoints, ypoints, 4)
      }
    }
  }

  private def getDelta(pt1: Point, pt2: Point): Point = {
    val x1 = pt1._1
    val y1 = pt1._2
    val deltaX = (pt2._1 - x1) / size
    val deltaY = (pt2._2 - y1) / size
    (deltaX, deltaY)
  }

}


