// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.twopails.model

import com.barrybecker4.puzzle.twopails.model.PourOperation.Container.FIRST
import PourOperation.Action._


/** Immutable representation of the two Pails and the amount of liquid they each contain at the moment.
  * @author Barry Becker
  */
case class Pails(fill1: Int, fill2: Int, params: PailParams) {

  /** Constructor to create two empty pails. */
  def this(params: PailParams) = this(0, 0, params)

  private[model] def pail1HasRoom: Boolean = fill1 < params.pail1Size
  private[model] def pail2HasRoom: Boolean = fill2 < params.pail2Size

  /** @return a new Pails by applying a move to another Pails. */
  def doMove(move: PourOperation, undo: Boolean): Pails = applyMove(move, undo)

  def applyMove(move: PourOperation, undo: Boolean): Pails = {
    val op = if undo then move.reverse else move
    op.action match {
      case FILL =>
        if op.container == FIRST then Pails(params.pail1Size, fill2, params)
        else Pails(fill1, params.pail2Size, params)
      case EMPTY =>
        if op.container == FIRST then Pails(0, fill2, params)
        else Pails(fill1, 0, params)
      case TRANSFER =>
        if op.container == FIRST then transferFirstToSecond
        else transferSecondToFirst
    }
  }

  private def transferFirstToSecond: Pails = {
    val space = params.pail2Size - fill2
    Pails(
      math.max(0, fill1 - space),
      math.min(fill1 + fill2, params.pail2Size),
      params)
  }

  private def transferSecondToFirst: Pails = {
    val space = params.pail1Size - fill1
    Pails(
      math.min(fill1 + fill2, params.pail1Size),
      math.max(0, fill2 - space),
      params)
  }

  /** @return true if either container has exactly the target measure.*/
  def isSolved: Boolean = {
    val target = params.targetMeasureSize
    fill1 == target || fill2 == target
  }
}
