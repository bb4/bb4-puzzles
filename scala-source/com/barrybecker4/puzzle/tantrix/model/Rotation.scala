package com.barrybecker4.puzzle.tantrix.model

/**
  * Valid rotations for the hexagonal tiles.
  */
enum Rotation(rotation: Int) {
  case ANGLE_0 extends Rotation(0)
  case ANGLE_60 extends Rotation(1)
  case ANGLE_120 extends Rotation(2)
  case ANGLE_180 extends Rotation(3)
  case ANGLE_240 extends Rotation(4)
  case ANGLE_300 extends Rotation(5)

  def rotateBy(numRotations: Int): Rotation = {
    var rot = numRotations
    while (rot < 0) rot += HexTile.NUM_SIDES
    Rotation.values((this.ordinal + rot) % HexTile.NUM_SIDES)
  }

  override def toString: String = s"${60 * rotation}\u00B0"
}