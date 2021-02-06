// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.model

import com.barrybecker4.puzzle.rubixcube.model.FaceColor.FaceColor


object CubeComponents {

  val numMinicubesToBaseSize: Map[Int, Int] = Map(4 -> 2, 26 -> 3, 60 -> 4, 98 -> 5)

  /** static because they are the same for every board. */
  val COMPONENTS: Array[CubeComponents] = (2 to 5).map(i => CubeComponents(i)).toArray
}

/**
  * The internal structures for a board of a specified size.
  * @author Barry Becker
  */
case class CubeComponents(baseSize: Int = 3) {

  assert (baseSize <= 5, "baseSize = " + baseSize)
  val faceSize: Int = baseSize * baseSize

  val faceToLocations: Map[Orientation, Seq[(Int, Int, Int)]] = Map(
    TOP -> (for (i <- 1 to baseSize; j <- 1 to baseSize ) yield (1, i, j)),
    LEFT -> (for (i <- 1 to baseSize; j <- 1 to baseSize ) yield (i, 1, j)),
    FRONT -> (for (i <- 1 to baseSize; j <- 1 to baseSize ) yield (i, j, 1)),
    BOTTOM -> (for (i <- 1 to baseSize; j <- 1 to baseSize ) yield (baseSize, i, j)),
    RIGHT -> (for (i <- 1 to baseSize; j <- 1 to baseSize ) yield (i, baseSize, j)),
    BACK -> (for (i <- 1 to baseSize; j <- 1 to baseSize ) yield (i, j, baseSize))
  )

  /** A cube slice can be rotated. It is defined by the positions at a given orientation and level. */
  val sliceLocations: Map[(Orientation, Int), Seq[(Int, Int, Int)]] = {
    var m: Map[(Orientation, Int), Seq[(Int, Int, Int)]] = Map()

    for (orientation <- Array(TOP, LEFT, FRONT)) {
      for (level <- 1 to baseSize) {
        m += (orientation, level) -> getSlicePositions(orientation, level)
      }
    }
    m
  }

  private def getSlicePositions(orientation: Orientation, level: Int): Seq[(Int, Int, Int)] = {

    def getLoc(orientation: Orientation, i: Int, j: Int): (Int, Int, Int) = {
      orientation match {
        case TOP => (level, i, j)
        case LEFT => (i, level, j)
        case FRONT => (i, j, level)
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

  val initialCubeMap: Map[(Int, Int, Int), Minicube] = {
    var initialMap: Map[(Int, Int, Int), Minicube] = Map()

    def getTopBottom(topLevel: Int): (Orientation, FaceColor) =
      if (topLevel == 1) TOP -> TOP.goalColor() else BOTTOM -> BOTTOM.goalColor()

    def getFrontBack(frontLevel: Int): (Orientation, FaceColor) =
        if (frontLevel == 1) FRONT -> FRONT.goalColor() else BACK -> BACK.goalColor()

    def getLeftRight(leftLevel: Int): (Orientation, FaceColor) =
      if (leftLevel == 1) LEFT -> LEFT.goalColor() else RIGHT -> RIGHT.goalColor()

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
          initialMap += (top, left, front) -> Minicube(orientationToColor)
        }
      }
    }
    initialMap
  }
}
