// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.common.model

import scala.collection.Seq


/**
  * Link node for the puzzle solving framework.
  * Contains a puzzle position (immutable state) and a move
  * (transition that got us to that position from the previous state).
  * The estimated future cost is used for sorting nodes.
  * P is the Puzzle type
  * M is the move type
  *
  * @param position the current position state
  * @param move the transformation that got to this state
  * @param previous the previous state
  * @param estimatedFutureCost the cost of getting here plus the estimated future cost to get to the finish.
  * @author Brian Goetz and Tim Peierls
  * @author Barry Becker
  */

case class PuzzleNode[P, M](position: P, move: Option[M] = None,
                            var previous: Option[PuzzleNode[P, M]] = None, estimatedFutureCost: Int = 1)
  extends Comparable[PuzzleNode[P, M]] {

  /** @return an instance of the puzzle at this state */
  def getPosition: P = position

  /** @return An estimate of how much it will cost to go from this position to the goal state */
  private def getEstimatedFutureCost = estimatedFutureCost

  /** @return a list of nodes from the start state to this state. */
  def asMoveList: Seq[M] = {
    var solution = List[M]()
    var n: PuzzleNode[P, M] = this
    while (n.move.isDefined) {
      solution +:= n.move.get
      n = n.previous.get
    }
    solution
  }

  override def compareTo(otherNode: PuzzleNode[P, M]): Int =
    getEstimatedFutureCost - otherNode.getEstimatedFutureCost
}
