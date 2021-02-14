// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.ui

import com.barrybecker4.puzzle.common.ui.PuzzleRenderer
import com.barrybecker4.puzzle.rubixcube.model._
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
  private val LEFT_MARGIN = 0.5f
  private val TOP_MARGIN = 0.5f
  private val LINE_COLOR = new Color(25, 35, 35)
  private val LINE_STROKE = new BasicStroke(3)

  private val THIN_LINE_COLOR = new Color(15, 15, 25)
  private val THIN_LINE_STROKE = new BasicStroke(2)

  private val FONT = new Font("Sans Serif", Font.PLAIN, INC / 2)
  private val EDGE_HT = Math.sqrt(5.0).toFloat
  private val CUBE2_X = LEFT_MARGIN + 4.5f
  
  // these points form a hexagon with A at the center
  private val A1: Point = (LEFT_MARGIN + 2, TOP_MARGIN + 2)
  private val B1: Point = (LEFT_MARGIN + 2, TOP_MARGIN)
  private val C1: Point = (LEFT_MARGIN, TOP_MARGIN + 1 + EDGE_HT)
  private val D1: Point = (LEFT_MARGIN + 4, TOP_MARGIN + 1 + EDGE_HT)
  private val E1: Point = (LEFT_MARGIN, TOP_MARGIN + 1)
  private val F1: Point = (LEFT_MARGIN + 4, TOP_MARGIN + 1)
  private val G1: Point = (LEFT_MARGIN + 2, TOP_MARGIN + 2 + EDGE_HT)

  private val TOP_FACE_POLY: Array[Point] = Array(A1, E1, B1, F1)
  private val LEFT_FACE_POLY: Array[Point] = Array(A1, G1, C1, E1)
  private val FRONT_FACE_POLY: Array[Point] = Array(A1, F1, D1, G1)

  private val A2: Point = (CUBE2_X + 2, TOP_MARGIN + EDGE_HT)
  private val B2: Point = (CUBE2_X + 2, TOP_MARGIN + 2 + EDGE_HT)
  private val C2: Point = (CUBE2_X + 4, TOP_MARGIN + 1)
  private val D2: Point = (CUBE2_X, TOP_MARGIN + 1)
  private val E2: Point = (CUBE2_X + 4, TOP_MARGIN + 1 + EDGE_HT)
  private val F2: Point = (CUBE2_X, TOP_MARGIN + 1 + EDGE_HT)
  private val G2: Point = (CUBE2_X + 2, TOP_MARGIN)

  private val BOTTOM_FACE_POLY: Array[Point] = Array(B2, F2, A2, E2)
  private val RIGHT_FACE_POLY: Array[Point] = Array(G2, A2, F2, D2)
  private val BACK_FACE_POLY: Array[Point] = Array(G2, A2, E2, C2)
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
    drawFace(g2, cube.getFace(BOTTOM), BOTTOM_FACE_POLY)
    drawFace(g2, cube.getFace(RIGHT), RIGHT_FACE_POLY)
    drawFace(g2, cube.getFace(BACK), BACK_FACE_POLY)
  }

  /** @param face the 2d positions and colors of the squares on the face
    * @param points 4 points, in clockwise order, starting from the origin,
    *               which define a quadrilateral in which the grid of colored squares will be drawn.
    */
  private def drawFace(g2: Graphics2D, face: Map[(Int, Int), FaceColor], points: Array[Point]): Unit = {

    // fill the face's colored squares
    drawFaceSquares(g2, face, points)

    // draw grid based on the 4 points
    drawFaceLines(g2, points(0), points(3), points(1)) // horz lines
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
      val xx1 = (scaleX * (x1 + xinc)).toInt
      val yy1 = (scaleY * (y1 + yinc)).toInt
      val xx2 = (scaleX * (x2 + xinc)).toInt
      val yy2 = (scaleY * (y2 + yinc)).toInt
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

        val jDelta1 = j * rowDelta._1
        val jDelta2 = j * rowDelta._2

        val x1 = (scaleX * (baseX + i * colDelta._1 + jDelta1 )).toInt
        val x2 = (scaleX * (baseX + (i + 1) * colDelta._1 + jDelta1)).toInt
        val x3 = (x2 + scaleX * rowDelta._1).toInt
        val x4 = (x1 + scaleX * rowDelta._1).toInt

        val y1 = (scaleY * (baseY + i * colDelta._2 + jDelta2)).toInt
        val y2 = (scaleY * (baseY + (i + 1) * colDelta._2 + jDelta2)).toInt
        val y3 = (y2 + scaleY * rowDelta._2).toInt
        val y4 = (y1 + scaleY * rowDelta._2).toInt

        val xpoints = Array(x1, x2, x3, x4)
        val ypoints = Array(y1, y2, y3, y4)

        val loc = (i + 1, j + 1)
        g2.setColor(FaceColorMap.getColor(face(loc)))
        g2.fillPolygon(xpoints, ypoints, 4)

        g2.setStroke(THIN_LINE_STROKE)
        g2.setColor(THIN_LINE_COLOR)
        g2.drawPolygon(xpoints, ypoints, 4)
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


