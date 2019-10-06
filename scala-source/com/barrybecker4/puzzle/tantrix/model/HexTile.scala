// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model

import com.barrybecker4.puzzle.tantrix.model.PathColor.PathColor

object HexTile {
  /** number of sides on a hex tile. */
  val NUM_SIDES = 6
}

/**
  * Valid rotations for the hexagonal tiles.
  * @author Barry Becker
  */
object RotationEnum  {

  sealed abstract class Rotation(val ordinal: Int) {
    def rotateBy(numRotations: Int): Rotation = {
      var rot = numRotations
      while (rot < 0) rot += HexTile.NUM_SIDES
      RotationEnum.values((this.ordinal + rot) % HexTile.NUM_SIDES)
    }
  }

  case object ANGLE_0 extends Rotation(0)
  case object ANGLE_60 extends Rotation(1)
  case object ANGLE_120 extends Rotation(2)
  case object ANGLE_180 extends Rotation(3)
  case object ANGLE_240 extends Rotation(4)
  case object ANGLE_300 extends Rotation(5)

  val values = Array(ANGLE_0, ANGLE_60, ANGLE_120, ANGLE_180, ANGLE_240, ANGLE_300)
}

/** Represents a single tantrix tile. Immutable.
  * @param tantrixNumber the number on the back of the tile
  * @param primaryColor  The primary path color on the back of the tile
  * @param edgeColors outgoing path colors for each orientation index
  * @author Barry Becker
  */
case class HexTile(tantrixNumber: Byte, primaryColor: PathColor, edgeColors: PathColors) {
  override def toString: String = "tileNum=" + tantrixNumber + " colors: " + edgeColors
}
