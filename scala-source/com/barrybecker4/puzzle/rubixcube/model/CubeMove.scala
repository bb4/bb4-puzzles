// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.model

import com.barrybecker4.puzzle.rubixcube.model.Direction._
import com.barrybecker4.puzzle.common.model.Move
import com.barrybecker4.puzzle.rubixcube.Location

/**
  * The Rubix cube can be rotated in 3 * baseSize * baseSize ways. Immutable.
  * The rotation is defined by one of 3 orientations, the level/layer, and direction
  */
case class CubeMove(orientation: Orientation, level: Int, direction: Direction = CLOCKWISE) extends Move {

  def rotateMinicube(loc: Location, minicube: Minicube, size: Int): (Location, Minicube) = {
    val sizeP1 = size + 1

    val rotatedLocation = orientation match {
      case UP => if (direction == CLOCKWISE) (loc._1,  loc._3, sizeP1 - loc._2) else (loc._1, sizeP1 - loc._3, loc._2)
      case LEFT => if (direction == CLOCKWISE) (sizeP1 - loc._3,  loc._2, loc._1) else (loc._3, loc._2, sizeP1 - loc._1)
      case FRONT => if (direction == CLOCKWISE) (loc._2, sizeP1 - loc._1, loc._3) else (sizeP1 - loc._2, loc._1, loc._3)
      case _ => throw new IllegalArgumentException("Unexpted orientation: " + orientation)
    }
    rotatedLocation -> minicube.rotate(orientation, direction)
  }

  def reverse: CubeMove =
    CubeMove(orientation, level, if (direction == CLOCKWISE) COUNTER_CLOCKWISE else CLOCKWISE)

  override def toString: String = s"rotating $orientation $level $direction"
}

