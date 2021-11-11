// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.model

import com.barrybecker4.puzzle.rubixcube.model.Direction
import com.barrybecker4.puzzle.rubixcube.model.Direction.*
import Orientation.invalid
import com.barrybecker4.puzzle.rubixcube.model.FaceColor
import com.barrybecker4.puzzle.rubixcube.model.FaceColor.{BLUE, GREEN, ORANGE, RED, WHITE, YELLOW}


object Orientation {
  def invalid(): Orientation = {
    throw new IllegalArgumentException("Orientation must be one of TOP, LEFT, or FRONT")
  }

  val PRIMARY_ORIENTATIONS: Array[Orientation] = Array(UP, LEFT, FRONT)
}

/**
  * Note: Blue is opposite Green; Red opposite Orange; White opposite Yellow.
  */
enum Orientation:
  case UP, LEFT, FRONT, BACK, DOWN, RIGHT

  /** @return the new orientation after rotating 'direction' from the specified orientation */
  def rotate(orientation: Orientation, direction: Direction): Orientation = this match {
    case UP => orientation match {
      case UP => UP
      case LEFT if direction == Direction.CLOCKWISE => FRONT
      case LEFT if direction == COUNTER_CLOCKWISE => BACK
      case FRONT if direction == CLOCKWISE => RIGHT
      case FRONT if direction == COUNTER_CLOCKWISE => LEFT
      case _ => invalid()
    }
    case LEFT => orientation match {
      case UP if direction == CLOCKWISE => BACK
      case UP if direction == COUNTER_CLOCKWISE => FRONT
      case LEFT  => LEFT
      case FRONT if direction == CLOCKWISE => UP
      case FRONT if direction == COUNTER_CLOCKWISE => DOWN
      case _ => invalid()
    }
    case FRONT => orientation match {
      case UP if direction == CLOCKWISE => LEFT
      case UP if direction == COUNTER_CLOCKWISE => RIGHT
      case LEFT if direction == CLOCKWISE => DOWN
      case LEFT if direction == COUNTER_CLOCKWISE => UP
      case FRONT => FRONT
      case _ => invalid()
    }
    case BACK => orientation match {
      case UP if direction == CLOCKWISE => RIGHT
      case UP if direction == COUNTER_CLOCKWISE => LEFT
      case LEFT if direction == CLOCKWISE => UP
      case LEFT if direction == COUNTER_CLOCKWISE => DOWN
      case FRONT => BACK
      case _ => invalid()
    }
    case DOWN => orientation match {
      case UP => DOWN
      case LEFT if direction == CLOCKWISE => BACK
      case LEFT if direction == COUNTER_CLOCKWISE => FRONT
      case FRONT if direction == CLOCKWISE => LEFT
      case FRONT if direction == COUNTER_CLOCKWISE => RIGHT
      case _ => invalid()
    }
    case RIGHT => orientation match {
      case UP if direction == CLOCKWISE => FRONT
      case UP if direction == COUNTER_CLOCKWISE => BACK
      case LEFT => RIGHT
      case FRONT if direction == CLOCKWISE => DOWN
      case FRONT if direction == COUNTER_CLOCKWISE => UP
      case _ => invalid()
    }
  }

  def goalColor: FaceColor = this match {
    case UP => GREEN
    case LEFT => RED
    case FRONT => WHITE
    case BACK => YELLOW
    case DOWN => BLUE
    case RIGHT => ORANGE
  }
