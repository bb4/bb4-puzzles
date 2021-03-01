// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.model

import com.barrybecker4.puzzle.rubixcube.model.Direction._
import Orientation.invalid
import com.barrybecker4.puzzle.rubixcube.model.FaceColor._


object Orientation {
  def invalid(): Orientation = {
    throw new IllegalArgumentException("Orientation must be one of TOP, LEFT, or FRONT")
  }

  val PRIMARY_ORIENTATIONS: Array[Orientation] = Array(UP, LEFT, FRONT)
}

/**
  * Note: Blue is opposite Green; Red opposite Orange; White opposite Yellow.
  */
sealed trait Orientation {
  /** @return the new orientation after rotating 'direction' from the specified orientation */
  def rotate(orientation: Orientation, direction: Direction): Orientation
  def goalColor(): FaceColor
}

case object UP extends Orientation {
  override def rotate(orientation: Orientation, direction: Direction): Orientation =
    orientation match {
      case UP => UP
      case LEFT if direction == CLOCKWISE => FRONT
      case LEFT if direction == COUNTER_CLOCKWISE => BACK
      case FRONT if direction == CLOCKWISE => RIGHT
      case FRONT if direction == COUNTER_CLOCKWISE => LEFT
      case _ => invalid()
    }
  override def goalColor(): FaceColor = GREEN
}

case object LEFT extends Orientation {
  override def rotate(orientation: Orientation, direction: Direction): Orientation =
    orientation match {
      case UP if direction == CLOCKWISE => BACK
      case UP if direction == COUNTER_CLOCKWISE => FRONT
      case LEFT  => LEFT
      case FRONT if direction == CLOCKWISE => UP
      case FRONT if direction == COUNTER_CLOCKWISE => DOWN
      case _ => invalid()
    }
  override def goalColor(): FaceColor = RED
}

case object FRONT extends Orientation {
  override def rotate(orientation: Orientation, direction: Direction): Orientation =
    orientation match {
      case UP if direction == CLOCKWISE => LEFT
      case UP if direction == COUNTER_CLOCKWISE => RIGHT
      case LEFT if direction == CLOCKWISE => DOWN
      case LEFT if direction == COUNTER_CLOCKWISE => UP
      case FRONT => FRONT
      case _ => invalid()
    }
  override def goalColor(): FaceColor = WHITE
}

case object BACK extends Orientation {
  override def rotate(orientation: Orientation, direction: Direction): Orientation =
    orientation match {
      case UP if direction == CLOCKWISE => RIGHT
      case UP if direction == COUNTER_CLOCKWISE => LEFT
      case LEFT if direction == CLOCKWISE => UP
      case LEFT if direction == COUNTER_CLOCKWISE => DOWN
      case FRONT => BACK
      case _ => invalid()
    }
    override def goalColor(): FaceColor = YELLOW
}

case object DOWN extends Orientation {
  override def rotate(orientation: Orientation, direction: Direction): Orientation =
    orientation match {
      case UP => DOWN
      case LEFT if direction == CLOCKWISE => BACK
      case LEFT if direction == COUNTER_CLOCKWISE => FRONT
      case FRONT if direction == CLOCKWISE => LEFT
      case FRONT if direction == COUNTER_CLOCKWISE => RIGHT
      case _ => invalid()
    }
  override def goalColor(): FaceColor = BLUE
}

case object RIGHT extends Orientation {
  override def rotate(orientation: Orientation, direction: Direction): Orientation =
    orientation match {
      case UP if direction == CLOCKWISE => FRONT
      case UP if direction == COUNTER_CLOCKWISE => BACK
      case LEFT => RIGHT
      case FRONT if direction == CLOCKWISE => DOWN
      case FRONT if direction == COUNTER_CLOCKWISE => UP
      case _ => invalid()
    }
  override def goalColor(): FaceColor = ORANGE
}
