// Copyright by Barry G. Becker, 20013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.twopails.model

import com.barrybecker4.puzzle.twopails.model.PourOperation.Container.FIRST
import PourOperation.Action._


/** Immutable representation of the two Pails and the amount of liquid they each contain at the moment.
  * @author Barry Becker
  */
case class Pails(fill1: Int, fill2: Int, params: PailParams) {

  /** Constructor to create two empty pails. */
  def this(params: PailParams) {this(0, 0, params)}

  private[model] def pail1HasRoom = fill1 < params.pail1Size
  private[model] def pail2HasRoom = fill2 < params.pail2Size

  /** @return a new Pails by applying a move to another Pails. */
  def doMove(move: PourOperation, undo: Boolean): Pails = this.applyMove(move, undo)

  def applyMove(move: PourOperation, undo: Boolean): Pails = {
    val op = if (undo) move.reverse
    else move
    op.action match {
      case FILL =>
        if (op.container eq FIRST) Pails(params.pail1Size, fill2, params)
        else Pails(fill1, params.pail2Size, params)
      case EMPTY =>
        if (op.container eq FIRST) Pails(0, fill2, params)
        else Pails(fill1, 0, params)
      case TRANSFER =>
        if (op.container eq FIRST) { // transfer from first container to second
          val space = params.pail2Size - fill2
          Pails(Math.max(0, fill1 - space).toByte, Math.min(fill1 + fill2, params.pail2Size).toByte, params)
        }
        else { // transfer from second container to first
          val space = params.pail1Size - fill1
          Pails(Math.min(fill1 + fill2, params.pail1Size).toByte, Math.max(0, fill2 - space).toByte, params)
        }
    }
  }

  /** @return true if either container has exactly the target measure.*/
  def isSolved: Boolean = {
    val target = params.targetMeasureSize
    fill1 == target || fill2 == target
  }
}
