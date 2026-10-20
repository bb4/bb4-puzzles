// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver

import com.barrybecker4.math.MathUtil
import com.barrybecker4.puzzle.common.PuzzleController
import com.barrybecker4.puzzle.common.solver.PuzzleSolver
import com.barrybecker4.puzzle.tantrix.model.{TantrixBoard, TilePlacement}
import com.barrybecker4.puzzle.tantrix.solver.RestartingDepthFirstSolver.luby
import com.barrybecker4.puzzle.tantrix.solver.ShapeAssignSearch.*

import scala.util.Random

object ShapeThenAssignSolver {
  /** The number of cells that may be added to the shape in the first attempt. Later attempts get multiples of this. */
  val DEFAULT_BASE_BUDGET = 100
}

/**
  * Solves the puzzle by choosing the shape of the loop and the tiles that go in it together
  * (see [[ShapeAssignSearch]]), starting over with new random choices when an attempt does not find a solution within
  * its budget. The budgets follow the Luby sequence, as in [[RestartingDepthFirstSolver]].
  * Puzzles with more tiles than [[ShapeAssignSearch.MAX_TILES]] are given to [[RestartingDepthFirstSolver]].
  * @author Barry Becker
  */
class ShapeThenAssignSolver(
    puzzle: PuzzleController[TantrixBoard, TilePlacement],
    baseBudget: Int = ShapeThenAssignSolver.DEFAULT_BASE_BUDGET,
    rnd: Random = MathUtil.RANDOM
) extends PuzzleSolver[TilePlacement] {

  require(baseBudget > 0, "The budget must be positive, but was " + baseBudget)

  private var attempts = 0

  /** The number of restarts that were needed by the last call to solve. */
  def numRestarts: Int = Math.max(0, attempts - 1)

  override def solve: Option[Seq[TilePlacement]] = {
    val initial = puzzle.initialState
    if (initial.numTiles > MAX_TILES || initial.tantrix.size != 1) new RestartingDepthFirstSolver(puzzle, rnd = rnd).solve
    else if (!ShapeAssignSearch.allHavePath(initial.tantrix.tiles.head.tile +: initial.unplacedTiles, initial.primaryColor)) {
      // Every tile is in the loop, so a tile without the color means there is no solution.
      puzzle.finalRefresh(None, None, 0, 0)
      None
    }
    else {
      val startTime = System.currentTimeMillis
      val first = initial.tantrix.tiles.head
      var totalSteps = 0L
      val search = new ShapeAssignSearch(first.tile +: initial.unplacedTiles.toIndexedSeq, initial.primaryColor, first, rnd,
        steps => puzzle.refresh(initial, totalSteps + steps))
      var moves: Option[Seq[TilePlacement]] = None
      var finished = false
      attempts = 0
      while (!finished) {
        attempts += 1
        puzzle.refresh(initial, totalSteps)
        val outcome = search.run(baseBudget.toLong * luby(attempts))
        totalSteps += search.numSteps
        outcome match {
          case Found(found) =>
            moves = Some(found)
            finished = true
          case Exhausted => finished = true
          case OutOfSteps =>
        }
      }
      val solution = moves.map(_.foldLeft(initial)(puzzle.transition))
      puzzle.finalRefresh(moves, solution, totalSteps, System.currentTimeMillis - startTime)
      moves
    }
  }
}
