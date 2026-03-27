package com.barrybecker4.puzzle.common.solver

import com.barrybecker4.puzzle.common.ui.AbstractPuzzleController

/**
  * Trivial search space with no legal moves from the start — concurrent solver exhausts with no solution.
  */
class DeadEndTestController extends AbstractPuzzleController[Int, String](null) {

  override def initialState: Int = 0

  override def isGoal(position: Int): Boolean = false

  override def legalTransitions(position: Int): Seq[String] = Seq.empty

  override def transition(position: Int, move: String): Int = position

  override def animateTransition(trans: String): Int =
    throw new UnsupportedOperationException("not used in solver tests")
}
