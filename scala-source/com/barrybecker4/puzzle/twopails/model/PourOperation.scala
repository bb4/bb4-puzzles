// Copyright by Barry G. Becker, 20017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.twopails.model

import com.barrybecker4.puzzle.common.model.Move
import PourOperation.{Action, Container}

/**
  * There are 6 legal pour operations:
  * 1) fill first container to top
  * 2) fill second container to top
  * 3) empty first container
  * 4) empty second container
  * 5) pour everything in first container to second.
  * 6) pour everything in second container to first.
  * Immutable.
  *
  * @author Barry Becker
  */
object PourOperation {

  enum Action:
    case FILL, EMPTY, TRANSFER

  enum Container:
    case FIRST, SECOND
}

case class PourOperation(action: Action, container: Container) extends Move {

  /** The reverse of this operation (undo step). */
  private[model] def reverse: PourOperation =
    action match
      case Action.FILL => PourOperation(Action.EMPTY, container)
      case Action.EMPTY => PourOperation(Action.FILL, container)
      case Action.TRANSFER =>
        val src = if container == Container.FIRST then Container.SECOND else Container.FIRST
        PourOperation(Action.TRANSFER, src)

  override def toString: String =
    action.toString + " " + container.toString
}
