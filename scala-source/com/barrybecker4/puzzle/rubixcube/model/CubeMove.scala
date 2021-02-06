// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.model

import com.barrybecker4.puzzle.rubixcube.model.Direction._
import com.barrybecker4.puzzle.common.model.Move

/**
  * The Rubix cube can be rotated in 3 * baseSize * baseSize ways. Immutable.
  * The rotation is defined by one of 3 orientations, the level/layer, and direction
  */
case class CubeMove(orientation: Orientation, level: Int, direction: Direction = CLOCKWISE) extends Move {

  override def toString: String = s"rotating $orientation $level $direction"
}

