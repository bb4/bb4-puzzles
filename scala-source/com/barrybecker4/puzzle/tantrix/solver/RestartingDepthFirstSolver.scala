// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver

import com.barrybecker4.math.MathUtil
import com.barrybecker4.puzzle.common.PuzzleController
import com.barrybecker4.puzzle.common.solver.PuzzleSolver
import com.barrybecker4.puzzle.tantrix.model.{TantrixBoard, TilePlacement}
import RestartingDepthFirstSolver.*

import scala.collection.mutable
import scala.util.Random
import scala.util.boundary
import scala.util.boundary.break

object RestartingDepthFirstSolver {

  /** The number of search steps allowed in the first attempt. Later attempts get multiples of this.
    * Short attempts work best here. A search that is going to find a solution usually does so quickly, and
    * if it does not, a different order of the tiles is likely to do better than going further. Trying budgets
    * from 30 to 2000 on puzzles of 20 tiles, the ones in the range 30 to 300 solved them all, in about one third of
    * the time of 1000, and 2000 failed to solve a quarter of the puzzles in 30 seconds.
    */
  val DEFAULT_BASE_BUDGET = 100

  /** Subtrees smaller than this are cheap to search again, so they are not remembered. */
  private val MIN_SUBTREE_SIZE_TO_REMEMBER = 16

  /** Limit on the number of failed states to remember. */
  private val MAX_REMEMBERED = 500000

  /** The Luby sequence: 1, 1, 2, 1, 1, 2, 4, 1, 1, 2, 1, 1, 2, 4, 8, ...
    * Using it to set the limits on the restarts is within a log factor of the best possible schedule for any
    * distribution of run times, and it does not need to know what that distribution is.
    * @param i the position in the sequence, starting from 1.
    */
  def luby(i: Int): Int = {
    require(i >= 1, "The Luby sequence starts at 1, but got " + i)
    var k = 1
    while ((1L << k) - 1 < i) k += 1 // smallest k with 2^k - 1 >= i
    if ((1L << k) - 1 == i) 1 << (k - 1)
    else luby(i - (1 << (k - 1)) + 1)
  }

  private sealed trait Result
  private case class Solved(moves: List[TilePlacement]) extends Result
  /** Everything below this state was searched, and there is no solution there. */
  private case object Refuted extends Result
  /** The search ran out of steps before knowing whether there is a solution below this state. */
  private case object OutOfSteps extends Result
}

/**
  * Depth first search that starts over, with a different random order of the moves, when it has gone a
  * number of steps without finding a solution. Backtracking search can get stuck for a very long time below an early bad
  * choice, even though there are plenty of solutions elsewhere. Restarting limits how much time gets
  * wasted that way. The limits grow (following the Luby sequence), so that if the puzzle is
  * hard but solvable, the search will still have a long enough run to finish. It also means that when there is
  * no solution, an attempt eventually has enough steps to prove that.
  *
  * Work is not repeated from one attempt to the next. When all of the states below a state have been searched without
  * finding a solution, the state is remembered. The attempts that follow skip it.
  *
  * Pair this with a puzzle controller that prunes the moves it returns (like [[com.barrybecker4.puzzle.tantrix.TantrixController]])
  * @param puzzle the puzzle to solve
  * @param baseBudget the number of steps allowed in the first attempt.
  * @param rnd the source of randomness for ordering moves. It defaults to the shared generator, so that seeding that
  *            generator makes a search repeatable (the benchmarks do that).
  */
class RestartingDepthFirstSolver(
    puzzle: PuzzleController[TantrixBoard, TilePlacement],
    baseBudget: Int = DEFAULT_BASE_BUDGET,
    rnd: Random = MathUtil.RANDOM
) extends PuzzleSolver[TilePlacement] {

  require(baseBudget > 0, "The budget must be positive, but was " + baseBudget)

  private val refuted = mutable.HashSet.empty[TantrixBoard]
  private var numTries = 0L
  private var stepsInAttempt = 0
  private var attemptBudget = 0
  private var attemptNumber = 0

  /** The number of restarts that were needed by the last call to solve. */
  def numRestarts: Int = Math.max(0, attemptNumber - 1)

  override def solve: Option[Seq[TilePlacement]] = {
    val startTime = System.currentTimeMillis
    val initial = puzzle.initialState
    var outcome: Option[Result] = None
    attemptNumber = 0
    while (outcome.isEmpty) {
      attemptNumber += 1
      attemptBudget = baseBudget * luby(attemptNumber)
      stepsInAttempt = 0
      search(initial, Nil, shuffle = attemptNumber > 1) match {
        case OutOfSteps => // start over
        case result => outcome = Some(result)
      }
    }
    val moves = outcome.get match {
      case Solved(path) => Some(path)
      case _ => None
    }
    val solution = moves.map(_.foldLeft(initial)(puzzle.transition))
    puzzle.finalRefresh(moves, solution, numTries, System.currentTimeMillis - startTime)
    moves
  }

  /**
    * @param board the state to search below
    * @param movesSoFar the moves that got us here, in reverse order
    * @param shuffle if true, try the moves in a random order. Otherwise use the order the puzzle suggests.
    */
  private def search(board: TantrixBoard, movesSoFar: List[TilePlacement], shuffle: Boolean): Result =
    if (puzzle.isGoal(board)) Solved(movesSoFar.reverse)
    else if (refuted.contains(board)) Refuted
    else if (stepsInAttempt >= attemptBudget) OutOfSteps
    else {
      stepsInAttempt += 1
      numTries += 1
      puzzle.refresh(board, numTries)
      val stepsBefore = stepsInAttempt
      val moves = puzzle.legalTransitions(board)
      val orderedMoves = if (shuffle) rnd.shuffle(moves) else moves
      boundary[Result] {
        for (move <- orderedMoves) {
          search(puzzle.transition(board, move), move :: movesSoFar, shuffle) match {
            case solved: Solved => break(solved)
            case OutOfSteps => break(OutOfSteps) // the budget is used up, so nothing more can be done below here
            case Refuted =>
          }
        }
        if (stepsInAttempt - stepsBefore >= MIN_SUBTREE_SIZE_TO_REMEMBER && refuted.size < MAX_REMEMBERED)
          refuted += board
        Refuted
      }
    }
}
