// Copyright by Barry G. Becker, 20013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.twopails.model

import com.barrybecker4.puzzle.common.model.Move
import PourOperation.Action
import com.barrybecker4.puzzle.twopails.model.PourOperation.Action.Action
import com.barrybecker4.puzzle.twopails.model.PourOperation.Container.Container

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

  object Action extends Enumeration {
    type Action = Value
    val FILL, EMPTY, TRANSFER = Value
  }

  object Container extends Enumeration {
    type Container = Value
    val FIRST, SECOND = Value
  }
}

case class PourOperation(var action: Action, var container: Container) extends Move {

  /** @return the reverse of this operation*/
  private[model] def reverse = {
    var newAction = action
    var newContainer = container
    action match {
      case Action.FILL =>
        newAction = PourOperation.Action.EMPTY
      case Action.EMPTY =>
        newAction = PourOperation.Action.FILL
      case Action.TRANSFER =>
        newContainer = if (container eq PourOperation.Container.FIRST) PourOperation.Container.SECOND
                       else PourOperation.Container.FIRST
    }
    new PourOperation(newAction, newContainer)
  }

  override def toString: String = {
    val s = new StringBuilder
    s.append(action).append(" ").append(container)
    s.toString
  }
}

