// Copyright by Barry G. Becker, 2013-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.hiq

import com.barrybecker4.search.Refreshable
import com.barrybecker4.puzzle.common.ui.AbstractPuzzleController
import com.barrybecker4.puzzle.hiq.model.MoveGenerator
import com.barrybecker4.puzzle.hiq.model.PegBoard
import com.barrybecker4.puzzle.hiq.model.PegMove
import com.barrybecker4.puzzle.hiq.Algorithm.CONCURRENT_OPTIMUM
import collection.mutable

/**
  * HiQ Puzzle Controller.
  * See puzzle.common for puzzle framework classes.
  * @param ui shows the current state on the screen.
  * @author Barry Becker
  */
class HiQController(ui: Refreshable[PegBoard, PegMove])
  extends AbstractPuzzleController[PegBoard, PegMove](ui) {
  algorithm = CONCURRENT_OPTIMUM

  def initialState: PegBoard = PegBoard.INITIAL_BOARD_POSITION

  def isGoal(position: PegBoard): Boolean = position.isSolved

  def legalTransitions(position: PegBoard): Seq[PegMove] = new MoveGenerator(position).generateMoves

  def transition(position: PegBoard, move: PegMove): PegBoard = position.doMove(move)

  /** A simple estimate of the future cost to the goal is the number of pegs remaining.
    * Other secondary factors like how spread out the remaining pegs are may be used to improve this estimate.
    * @return estimate of the cost to reach the a single bag remaining
    */
  override def distanceFromGoal(position: PegBoard): Int = position.getNumPegsLeft

  /** Check all board symmetries to be sure it has or has not been seen. If it was never seen before add it.
    * Must be synchronized because some solvers use concurrency.
    */
  override def alreadySeen(position: PegBoard, seen: mutable.Set[PegBoard]): Boolean = {
    val wasSeen = position.containedIn(seen)
    if (!wasSeen) seen.add(position)
    wasSeen
  }
}
