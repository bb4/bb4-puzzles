// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.ui

import com.barrybecker4.puzzle.common.ui.PuzzleRenderer
import com.barrybecker4.puzzle.rubixcube.model._
import com.barrybecker4.puzzle.rubixcube.ui.CubeRenderer._
import com.barrybecker4.puzzle.rubixcube.model.FaceColor.{FaceColor, _}
import com.barrybecker4.puzzle.rubixcube.ui.util.{CubeMoveTransition, FaceColorMap, RotatedEllipse}

import java.awt.{BasicStroke, Color, Font, Graphics, Graphics2D}
import com.barrybecker4.puzzle.rubixcube.model.UP

import scala.math.BigDecimal.double2bigDecimal


/**
  * Singleton class that renders a Rubix cube in the CubeViewer.
  * It draws 2 orthogonal projections of the cube - one from the front and one from back
  * That way all 6 sides can be seen at once.
  */
object CubeRenderer {

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
  
  // these points form a hexagon with ULF at the center
  private val ULF: Point = Point(LEFT_MARGIN + 2, TOP_MARGIN + 2)
  private val URB: Point = Point(LEFT_MARGIN + 2, TOP_MARGIN)
  private val DLB: Point = Point(LEFT_MARGIN, TOP_MARGIN + 1 + EDGE_HT)
  private val DRF: Point = Point(LEFT_MARGIN + 4, TOP_MARGIN + 1 + EDGE_HT)
  private val ULB: Point = Point(LEFT_MARGIN, TOP_MARGIN + 1)
  private val URF: Point = Point(LEFT_MARGIN + 4, TOP_MARGIN + 1)
  private val DLF: Point = Point(LEFT_MARGIN + 2, TOP_MARGIN + 2 + EDGE_HT)

  private val DRB2: Point = Point(CUBE2_X + 2, TOP_MARGIN + EDGE_HT)
  private val DLF2: Point = Point(CUBE2_X + 2, TOP_MARGIN + 2 + EDGE_HT)
  private val URF2: Point = Point(CUBE2_X + 4, TOP_MARGIN + 1)
  private val ULB2: Point = Point(CUBE2_X, TOP_MARGIN + 1)
  private val DRF2: Point = Point(CUBE2_X + 4, TOP_MARGIN + 1 + EDGE_HT)
  private val DLB2: Point = Point(CUBE2_X, TOP_MARGIN + 1 + EDGE_HT)
  private val URB2: Point = Point(CUBE2_X + 2, TOP_MARGIN)

  private var g2: Graphics2D = _

  // points go counter clockwise around the square
  private val ORIENTATION_TO_FACE: Map[Orientation, Array[Point]] = Map(
    UP -> Array(ULF, URF, URB, ULB),
    LEFT -> Array(ULF, DLF, DLB, ULB),
    FRONT -> Array(ULF, DLF, DRF, URF),
    DOWN -> Array(DRB2, DRF2, DLF2, DLB2),
    BACK -> Array(URB2, DRB2, DLB2, ULB2),
    RIGHT -> Array(URB2, DRB2, DRF2, URF2)
  )

  private val ORIENTATION_TO_EDGE: Map[Orientation, (Array[Point], Array[Orientation])] = Map(
    UP -> (Array(ULF, URF, ULB), Array(FRONT, LEFT)), // rm 3rd pt
    LEFT -> (Array(ULF, DLF, ULB), Array(FRONT, UP)), // rm 3rd
    FRONT -> (Array(ULF, DLF, URF), Array(LEFT, UP)), // rm 3rd
    DOWN -> (Array(DRB2, DRF2, DLB2), Array(RIGHT, BACK)), // rm 3rd
    BACK -> (Array(DRB2, URB2, DLB2), Array(RIGHT, DOWN)), // rm 4th
    RIGHT -> (Array(DRB2, URB2, DRF2), Array(BACK, DOWN)), // rm 4th
  )

  private val ORIENTATION_TO_ANGLE: Map[Orientation, Double] = Map(
    UP -> -HALF_PI,
    LEFT -> Math.PI / 6,
    FRONT -> 5 * Math.PI / 6.0,
    DOWN -> HALF_PI,
    BACK -> -5 * Math.PI / 6.0,
    RIGHT -> -Math.PI / 6
  )

  private val POINTS = (for (i <- -Math.PI to Math.PI by 0.1) yield i.toDouble).toArray

  val FRONT_CENTER_Y: Float = TOP_MARGIN + 1 + (EDGE_HT + 1) / 2f
  val FRONT_ELLIPSE_A: Double = Math.sqrt(10 + 2 * EDGE_HT) / 2
  val FRONT_ELLIPSE_B: Double = Math.sqrt(10 - 2 * EDGE_HT) / 2

  private val UP_ROTATION_CENTER: Point = Point(LEFT_MARGIN + 2, TOP_MARGIN + 1)
  private val LEFT_ROTATION_CENTER: Point = Point(LEFT_MARGIN + 1, FRONT_CENTER_Y)
  private val FRONT_ROTATION_CENTER: Point = Point(LEFT_MARGIN + 3, FRONT_CENTER_Y)
}


/**
  * For rotating point in a slice, I use this equation for a rotated ellipse
  * https://math.stackexchange.com/questions/426150/what-is-the-general-equation-of-the-ellipse-that-is-not-in-the-origin-and-rotate
  */
class CubeRenderer extends PuzzleRenderer[Cube] {

  private var size: Int = 0
  private var scaleX: Float = 0
  private var scaleY: Float = 0

  /** This renders the current state of the Cube to the screen. */
  def render(g: Graphics, cube: Cube, width: Int, height: Int): Unit = {
    render(g, cube, width, height, None)
  }

  /** This renders the current state of the Cube to the screen. */
  def render(g: Graphics, cube: Cube, width: Int, height: Int, transition: Option[CubeMoveTransition]): Unit = {
    size = cube.size
    scaleX = width / 10.0f
    scaleY = height / 5.0f
    g2 = g.asInstanceOf[Graphics2D]

    // draw slices based on 1 of the 3 orientations. that way one of the N slices can be rotating
    for (i <- cube.size to 1 by -1) {
      drawSlice(transition, i, cube)
    }

    if (transition.nonEmpty) {
      val topEllipse = RotatedEllipse(UP_ROTATION_CENTER, 2, 1, 0)
      drawPointsOnEllipse(topEllipse, transition.get)
      val leftEllipse = RotatedEllipse(LEFT_ROTATION_CENTER, FRONT_ELLIPSE_A, FRONT_ELLIPSE_B, THIRD_PI)
      drawPointsOnEllipse(leftEllipse, transition.get)
      val frontEllipse = RotatedEllipse(FRONT_ROTATION_CENTER, FRONT_ELLIPSE_A, FRONT_ELLIPSE_B, -THIRD_PI)
      drawPointsOnEllipse(frontEllipse, transition.get)
    }
  }

  private def drawSlice(transition: Option[CubeMoveTransition], layer: Int, cube: Cube): Unit = {

    val orientation = if (transition.nonEmpty) transition.get.move.orientation else UP
    val slice: Map[Location, Minicube] = cube.getSlice(orientation, layer)

    if (layer == 1) {
      val face: Map[(Int, Int), FaceColor] = orientation match {
        case UP => slice.map { case (loc, minicube) => (loc._2, loc._3) -> minicube.orientationToColor(orientation) }
        case LEFT => slice.map { case (loc, minicube) => (loc._1, loc._3) -> minicube.orientationToColor(orientation) }
        case FRONT => slice.map { case (loc, minicube) => (loc._1, loc._2) -> minicube.orientationToColor(orientation) }
        case _ => throw new IllegalArgumentException("Unexpected orientation: " + orientation)
      }
      drawFaceSquares(face, ORIENTATION_TO_FACE(orientation))
    }
    drawSliceEdge(slice, orientation, transition, layer, ORIENTATION_TO_FACE(orientation))
  }


  private def drawSliceEdge(slice: Map[Location, Minicube],
    orientation: Orientation, transition: Option[CubeMoveTransition],
    layer: Int, points: Array[Point]): Unit = {

    val edgePoints: (Array[Point], Array[Orientation]) = ORIENTATION_TO_EDGE(orientation)
    val edgeOrientations = edgePoints._2
    val angle = ORIENTATION_TO_ANGLE(orientation)

    val delta = Array(
      getDelta(edgePoints._1(0), edgePoints._1(1)),
      getDelta(edgePoints._1(0), edgePoints._1(2))
    )
    val baseX = points.head.x
    val baseY = points.head.y

    val miniEdgeLen = delta(0).length()
    val edgeOffset = new Point(angle, miniEdgeLen)

    for (side <- 0 to 1) {
      for (i <- 1 to size) {
        val edgeOrientation = edgeOrientations(side)
        val loc = orientation match {
          case UP => if (edgeOrientation == FRONT) (layer, i, 1) else (layer, 1, i)
          case LEFT => if (edgeOrientation == UP) (1, layer, i) else (i, layer, 1)
          case FRONT => if (edgeOrientation == UP) (1, i, layer) else (i, 1, layer)
          case _ => throw new IllegalArgumentException("Unexpected orientation: " + orientation)
        }
        val color = slice(loc).orientationToColor(edgeOrientation)
        val d = delta(side)

        val xOffset1 = (layer - 1) * edgeOffset.x
        val xOffset2 = scaleX * edgeOffset.x
        val x1 = (scaleX * (baseX + (i - 1) * d.x + xOffset1)).toInt
        val x2 = (scaleX * (baseX + i * d.x + xOffset1)).toInt
        val x3 = (x2 + xOffset2).toInt
        val x4 = (x1 + xOffset2).toInt

        val yOffset1 = (layer - 1) * edgeOffset.y
        val yOffset2 = scaleY * edgeOffset.y
        val y1 = (scaleY * (baseY + (i - 1) * d.y - yOffset1)).toInt
        val y2 = (scaleY * (baseY + i * d.y - yOffset1)).toInt
        val y3 = (y2 - yOffset2).toInt
        val y4 = (y1 - yOffset2).toInt

        val xpoints = Array(x1, x2, x3, x4)
        val ypoints = Array(y1, y2, y3, y4)

        g2.setColor(FaceColorMap.getColor(color))
        g2.fillPolygon(xpoints, ypoints, 4)

        g2.setStroke(LINE_STROKE)
        g2.setColor(LINE_COLOR)
        g2.drawPolygon(xpoints, ypoints, 4)
      }
    }
  }

  private def drawPointsOnEllipse(ellipse: RotatedEllipse, transition: CubeMoveTransition): Unit = {
    val rot = transition.percentDone * HALF_PI / 100.0

    var xpts: Seq[Int] = Seq()
    var ypts: Seq[Int] = Seq()
    var ct = 0

    for (theta <- POINTS) {
      val angle = theta + rot
      val pt = ellipse.getPointAtAngle(angle)
      if (theta > HALF_PI)
        g2.setColor(Color.BLUE)
      else if (theta < -HALF_PI)
        g2.setColor(Color.RED)
      else g2.setColor(THIN_LINE_COLOR)

      ct += 1
      if (ct % 14 == 0) {
        xpts :+= (scaleX * pt.x).toInt
        ypts :+= (scaleY * pt.y).toInt
      }
      g2.drawOval((scaleX * pt.x).toInt, (scaleY * pt.y).toInt, 3, 3)
    }

    g2.setColor(THIN_LINE_COLOR)
    g2.drawPolygon(xpts.toArray, ypts.toArray, xpts.size)
  }


  /** @param face the 2d positions and colors of the squares on the face
    * @param points 4 points, in clockwise order, starting from the origin,
    *               which define a quadrilateral in which the grid of colored squares will be drawn.
    */
  private def drawFaceSquares(face: Map[(Int, Int), FaceColor], points: Array[Point]): Unit = {
    val rowDelta = getDelta(points.head, points(3))
    val colDelta = getDelta(points.head, points(1))
    val baseX = points.head.x
    val baseY = points.head.y

    for (i <- 0 until size) {
      for (j <- 0 until size) {

        val jDelta1 = j * rowDelta.x
        val jDelta2 = j * rowDelta.y

        val x1 = (scaleX * (baseX + i * colDelta.x + jDelta1 )).toInt
        val x2 = (scaleX * (baseX + (i + 1) * colDelta.x + jDelta1)).toInt
        val x3 = (x2 + scaleX * rowDelta.x).toInt
        val x4 = (x1 + scaleX * rowDelta.x).toInt

        val y1 = (scaleY * (baseY + i * colDelta.y + jDelta2)).toInt
        val y2 = (scaleY * (baseY + (i + 1) * colDelta.y + jDelta2)).toInt
        val y3 = (y2 + scaleY * rowDelta.y).toInt
        val y4 = (y1 + scaleY * rowDelta.y).toInt

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
    val x1 = pt1.x
    val y1 = pt1.y
    val deltaX = (pt2.x - x1) / size
    val deltaY = (pt2.y - y1) / size
    Point(deltaX, deltaY)
  }

}


