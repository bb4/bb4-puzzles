// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.model

import com.barrybecker4.puzzle.rubixcube.model.Direction._
import Orientation.invalid
import com.barrybecker4.puzzle.rubixcube.model.FaceColor._


object Orientation {
  def invalid(): Orientation = {
    throw new IllegalArgumentException("Orientation must be one of TOP, LEFT, or FRONT")
  }

  val PRIMARY_ORIENTATIONS: Array[Orientation] = Array(TOP, LEFT, FRONT)
}

/**
  * Note: Blue is opposite Green; Red opposite Orange; White opposite Yellow.
  */
sealed trait Orientation {
  /** @return the new orientation after rotating 'direction' from the specified orientation */
  def rotate(orientation: Orientation, direction: Direction): Orientation
  def goalColor(): FaceColor
}

case object TOP extends Orientation {
  override def rotate(orientation: Orientation, direction: Direction): Orientation =
    orientation match {
      case TOP => TOP
      case LEFT if direction == CLOCKWISE => FRONT
      case LEFT if direction == COUNTER_CLOCKWISE => BACK
      case FRONT if direction == CLOCKWISE => RIGHT
      case FRONT if direction == COUNTER_CLOCKWISE => LEFT
      case _ => invalid()
    }
  override def goalColor(): FaceColor = BLUE
}

case object LEFT extends Orientation {
  override def rotate(orientation: Orientation, direction: Direction): Orientation =
    orientation match {
      case TOP if direction == CLOCKWISE => BACK
      case TOP if direction == COUNTER_CLOCKWISE => FRONT
      case LEFT  => LEFT
      case FRONT if direction == CLOCKWISE => TOP
      case FRONT if direction == COUNTER_CLOCKWISE => BOTTOM
      case _ => invalid()
    }
  override def goalColor(): FaceColor = YELLOW
}

case object FRONT extends Orientation {
  override def rotate(orientation: Orientation, direction: Direction): Orientation =
    orientation match {
      case TOP if direction == CLOCKWISE => LEFT
      case TOP if direction == COUNTER_CLOCKWISE => RIGHT
      case LEFT if direction == CLOCKWISE => BOTTOM
      case LEFT if direction == COUNTER_CLOCKWISE => TOP
      case FRONT => FRONT
      case _ => invalid()
    }
  override def goalColor(): FaceColor = RED
}

case object BACK extends Orientation {
  override def rotate(orientation: Orientation, direction: Direction): Orientation =
    orientation match {
      case TOP if direction == CLOCKWISE => RIGHT
      case TOP if direction == COUNTER_CLOCKWISE => LEFT
      case LEFT if direction == CLOCKWISE => TOP
      case LEFT if direction == COUNTER_CLOCKWISE => BOTTOM
      case FRONT => BACK
      case _ => invalid()
    }
    override def goalColor(): FaceColor = ORANGE
}

case object BOTTOM extends Orientation {
  override def rotate(orientation: Orientation, direction: Direction): Orientation =
    orientation match {
      case TOP => BOTTOM
      case LEFT if direction == CLOCKWISE => BACK
      case LEFT if direction == COUNTER_CLOCKWISE => FRONT
      case FRONT if direction == CLOCKWISE => LEFT
      case FRONT if direction == COUNTER_CLOCKWISE => RIGHT
      case _ => invalid()
    }
  override def goalColor(): FaceColor = GREEN
}

case object RIGHT extends Orientation {
  override def rotate(orientation: Orientation, direction: Direction): Orientation =
    orientation match {
      case TOP if direction == CLOCKWISE => FRONT
      case TOP if direction == COUNTER_CLOCKWISE => BACK
      case LEFT => RIGHT
      case FRONT if direction == CLOCKWISE => BOTTOM
      case FRONT if direction == COUNTER_CLOCKWISE => TOP
      case _ => invalid()
    }
  override def goalColor(): FaceColor = WHITE
}

/**
  * There are 6 different orientations for the face of a minicube
  *
object Orientation extends Enumeration {
  type Orientation = Value
  val TOP, FRONT, LEFT, BOTTOM, BACK, RIGHT = Value
}*/