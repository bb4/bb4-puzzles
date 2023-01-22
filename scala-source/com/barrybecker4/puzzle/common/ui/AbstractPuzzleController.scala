// Copyright by Barry G. Becker, 2000-2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.common.ui

import com.barrybecker4.common.concurrency.Worker
import com.barrybecker4.search.Refreshable
import com.barrybecker4.puzzle.common.AlgorithmEnum
import com.barrybecker4.puzzle.common.PuzzleController
import com.barrybecker4.puzzle.common.solver.PuzzleSolver
import scala.collection.mutable


/**
  * Provides default implementation for a PuzzleController.
  * The puzzle controller is what will solve a puzzle using some provided strategy.
  * It updates the ui (refreshable) and determines what algorithm is used to solve it.
  * If a non-null Refreshable is pass into the constructor that that will be delegated
  * to when the controller is asked to do a refresh.
  *
  * @author Barry Becker
  */
abstract class AbstractPuzzleController[P, M](val ui: Refreshable[P, M])
  extends PuzzleController[P, M] {

  /**
    * If this puzzle position was never seen before add it.
    * Must be synchronized because some solvers use concurrency.
    *
    * @return true if this position was already seen while searching.
    */
  override def alreadySeen(position: P, seen: mutable.Set[P] ): Boolean = {
    if (!seen.contains(position)) {
      seen += position
      return false
    }
    true
  }

  /**
    * Override this to help some search algorithms prioritize the order in which they search.
    * By default this is provides no information.
    * It can only be used for puzzles that have a path from an initial state to a solution.
    *
    * @return estimate of the cost to reach the goal from the specified position
    */
  override def distanceFromGoal (position: P): Int = 1

  /**
    * Override this when moves have varying cost for a move (rare - see bridge problem for example).
    *
    * @param move the move to determine cost of
    * @return cost of a single move
    */
  override def getCost (move: M): Int = 1

  /** Called when the puzzle solver wants to show progress to the user somehow */
  override def refresh (pos: P, numTries: Long): Unit = {
    if (ui != null) {
      ui.refresh (pos, numTries)
    }
  }

  override def animateTransition(trans: M): P = ???

  /** Once the puzzle search is done, this is called to show the solution (or lack thereof). */
  override def finalRefresh (path: Option[Seq[M]], position: Option[P], numTries: Long, elapsedMillis: Long): Unit = {
    if (path.isEmpty) {
      println ("No Solution found!")
    }
    else {
      System.out.println ("The number of steps in path to solution = " + path.get.size)
      if (ui != null) {
        ui.finalRefresh(path, position, numTries, elapsedMillis)
        print(position.get)
      }
    }
  }

  /**
    * Begin the process of solving.
    * Do it in a separate worker thread so the UI is not blocked.
    */
  override def startSolving (): Unit = {
    // Use either concurrent or sequential solver strategy
    val solver: PuzzleSolver[M] = algorithm.createSolver(this)

    val worker: Worker = new Worker() {
      override def construct: AnyRef = {
        try {
          // this does all the heavy work of solving it.
          solver.solve
        } catch {
          case e: InterruptedException =>
            assert(assertion = false, "Thread interrupted. " + e.getMessage)
        }
        "true"
      }
    }
    worker.start ()
  }
}
