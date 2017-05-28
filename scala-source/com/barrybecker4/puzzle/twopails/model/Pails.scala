// Copyright by Barry G. Becker, 20013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.twopails.model

import com.barrybecker4.puzzle.twopails.model.PourOperation.Container.FIRST
import PourOperation.Action._


/**
  * Immutable representation of the two Pails and the amount of liquid they each contain at the moment.
  *
  * @author Barry Becker
  */
class Pails {
  private var params: PailParams = _
  var fill1 = 0
  var fill2 = 0

  /**
    * Constructor to create two empty pails.
    */
  def this(params: PailParams) {
    this()
    this.params = params
    fill1 = 0
    fill2 = 0
  }

  /**
    * Copy constructor.
    */
  def this(pails: Pails) {
    this()
    params = pails.params
    fill1 = pails.fill1
    fill2 = pails.fill2
  }


  private[model] def pail1HasRoom = fill1 < params.pail1Size

  private[model] def pail2HasRoom = fill2 < params.pail2Size

  def getParams: PailParams = params

  /**
    * creates a new Pails by applying a move to another Pails.
    * Does not violate immutability.
    */
  def doMove(move: PourOperation, undo: Boolean) = new Pails(this, move, undo)

  /**
    * @param pos  current state
    * @param move transition to apply to it
    * @param undo if true, then undoes the transition rather than applying it.
    */
  def this(pos: Pails, move: PourOperation, undo: Boolean) {
    this(pos)
    applyMove(move, undo)
  }

  private def applyMove(move: PourOperation, undo: Boolean) = {
    val op = if (undo) move.reverse
    else move
    op.action match {
      case FILL =>
        if (op.container eq FIRST) fill1 = params.pail1Size
        else fill2 = params.pail2Size
      case EMPTY =>
        if (op.container eq FIRST) fill1 = 0
        else fill2 = 0
      case TRANSFER =>
        if (op.container eq FIRST) { // transfer from first container to second
          val space = params.pail2Size - fill2
          fill2 = Math.min(fill1 + fill2, params.pail2Size).toByte
          fill1 = Math.max(0, fill1 - space).toByte
        }
        else { // transfer from second container to first
          val space = params.pail1Size - fill1
          fill1 = Math.min(fill1 + fill2, params.pail1Size).toByte
          fill2 = Math.max(0, fill2 - space).toByte
        }
    }
  }

  /**
    * @return true if either container has exactly the target measure.
    */
  def isSolved: Boolean = {
    val target = params.targetMeasureSize
    fill1 == target || fill2 == target
  }

  override def toString: String = {
    val builder = new StringBuilder("Pails:")
    builder.append('[').append(fill1).append(" ").append(fill2).append(']')
    builder.toString
  }

  override def equals(o: Any): Boolean = {
    //if (this eq o) return true
    if (o == null || (getClass ne o.getClass)) return false
    val pails = o.asInstanceOf[Pails]
    fill1 == pails.fill1 && fill2 == pails.fill2
  }

  override def hashCode: Int = {
    var result = fill1
    result = 31 * result + fill2
    result
  }
}
