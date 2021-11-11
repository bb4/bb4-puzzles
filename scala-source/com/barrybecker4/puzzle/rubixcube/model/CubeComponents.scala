// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.model

import com.barrybecker4.puzzle.rubixcube.Location
import com.barrybecker4.puzzle.rubixcube.model.FaceColor
import com.barrybecker4.puzzle.rubixcube.model.Orientation._


object CubeComponents {

  val numMinicubesToBaseSize: Map[Int, Int] = Map(8 -> 2, 26 -> 3, 56 -> 4, 98 -> 5)

  /** static because they are the same for every board. */
  private val COMPONENTS: Array[CubeComponents] = (2 to 5).map(i => CubeComponents(i)).toArray

  def getCompsForSize(size: Int): CubeComponents = COMPONENTS(size - 2)
}

/**
  * The internal structures for a board of a specified size.
  * @author Barry Becker
  */
case class CubeComponents(baseSize: Int = 3) {

  assert (baseSize <= 5, "baseSize = " + baseSize)
  val faceSize: Int = baseSize * baseSize

  // Map from an orientation to the minicube locations for that face */
  val faceToLocations: Map[Orientation, Seq[Location]] = Map(
    UP -> (for (i <- 1 to baseSize; j <- 1 to baseSize) yield (1, i, j)),
    LEFT -> (for (i <- 1 to baseSize; j <- 1 to baseSize ) yield (i, 1, j)),
    FRONT -> (for (i <- 1 to baseSize; j <- 1 to baseSize ) yield (i, j, 1)),
    DOWN -> (for (i <- 1 to baseSize; j <- 1 to baseSize) yield (baseSize, i, j)),
    RIGHT -> (for (i <- 1 to baseSize; j <- 1 to baseSize ) yield (i, baseSize, j)),
    BACK -> (for (i <- 1 to baseSize; j <- 1 to baseSize ) yield (i, j, baseSize))
  )

  def locationsForSlice(orientation: Orientation, layer: Int): Seq[Location] = {
    orientation match {
      case UP => for (i <- 1 to baseSize; j <- 1 to baseSize) yield (layer, i, j)
      case LEFT => for (i <- 1 to baseSize; j <- 1 to baseSize ) yield (i, layer, j)
      case FRONT => for (i <- 1 to baseSize; j <- 1 to baseSize ) yield (i, j, layer)
      case _ => throw new IllegalArgumentException("unexpected orientation = " + orientation)
    }
  }

  /** A cube slice can be rotated. It is defined by the positions at a given orientation and level. */
  lazy val sliceLocations: Map[(Orientation, Int), Seq[Location]] = {
    (for (orientation <- Orientation.PRIMARY_ORIENTATIONS; level <- 1 to baseSize)
        yield (orientation, level) -> getSlicePositions(orientation, level)).toMap
  }

  private def getSlicePositions(orientation: Orientation, level: Int): Seq[Location] = {

    def getLoc(orientation: Orientation, i: Int, j: Int): Location = {
      orientation match {
        case UP => (level, i, j)
        case LEFT => (i, level, j)
        case FRONT => (i, j, level)
        case _ => throw IllegalArgumentException("Unexpected case: " + orientation)
      }
    }

    if (level == 1 || level == baseSize) {
      for (i <- 1 to baseSize; j <- 1 to baseSize)
        yield getLoc(orientation, i, j)
    } else {
      for (i <- 1 to baseSize; j <- 1 to baseSize; if i == 1 || i == baseSize || j == 1 || j == baseSize)
        yield getLoc(orientation, i, j)
    }
  }

  val initialCubeMap: Map[Location, Minicube] = {
    var initialMap: Map[Location, Minicube] = Map()

    def getTopBottom(topLevel: Int): (Orientation, FaceColor) =
      if (topLevel == 1) UP -> UP.goalColor else DOWN -> DOWN.goalColor

    def getFrontBack(frontLevel: Int): (Orientation, FaceColor) =
        if (frontLevel == 1) FRONT -> FRONT.goalColor else BACK -> BACK.goalColor

    def getLeftRight(leftLevel: Int): (Orientation, FaceColor) =
      if (leftLevel == 1) LEFT -> LEFT.goalColor else RIGHT -> RIGHT.goalColor

    for (top <- 1 to baseSize) {
      for (left <- 1 to baseSize) {
        for (front <- 1 to baseSize) {
          var orientationToColor: Map[Orientation, FaceColor] = Map()
          if (top == 1 || top == baseSize) {
            orientationToColor += getTopBottom(top)
            if (left == 1 || left == baseSize)
              orientationToColor += getLeftRight(left)
            if (front == 1 || front == baseSize)
              orientationToColor += getFrontBack(front)
          }
          else if (left == 1 || left == baseSize) {
            orientationToColor += getLeftRight(left)
            if (front == 1 || front == baseSize)
              orientationToColor += getFrontBack(front)
          }
          else if (front == 1 || front == baseSize) {
            orientationToColor += getFrontBack(front)
            if (left == 1 || left == baseSize)
              orientationToColor += getLeftRight(left)
          }
          if (orientationToColor.nonEmpty) {
            initialMap += (top, left, front) -> Minicube(orientationToColor, (top, left, front))
          }
        }
      }
    }
    initialMap
  }
}
