// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver

import com.barrybecker4.math.MathUtil
import com.barrybecker4.puzzle.common.PuzzleController
import com.barrybecker4.puzzle.common.solver.PuzzleSolver
import com.barrybecker4.puzzle.tantrix.model.{HexTile, TantrixBoard, TilePlacement}
import com.barrybecker4.puzzle.tantrix.solver.RestartingDepthFirstSolver.luby
import com.barrybecker4.puzzle.tantrix.solver.shapeassign.{LoopTiles, ShapeAssignSearch}
import com.barrybecker4.puzzle.tantrix.solver.shapeassign.ShapeAssignSearch.*

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
  */
class ShapeThenAssignSolver(
    puzzle: PuzzleController[TantrixBoard, TilePlacement],
    baseBudget: Int = ShapeThenAssignSolver.DEFAULT_BASE_BUDGET,
    rnd: Random = MathUtil.RANDOM
) extends PuzzleSolver[TilePlacement] {

  require(baseBudget > 0, "The budget must be positive, but was " + baseBudget)

  private var attempts = 0
  private var totalSteps = 0L

  /** The number of restarts that were needed by the last call to solve. */
  def numRestarts: Int = Math.max(0, attempts - 1)

  override def solve: Option[Seq[TilePlacement]] = {
    val initial = puzzle.initialState
    if (initial.numTiles > MAX_TILES || initial.tantrix.size != 1)
      new RestartingDepthFirstSolver(puzzle, rnd = rnd).solve
    else if (!LoopTiles.allHavePrimaryPath(allTiles(initial), initial.primaryColor)) report(initial, None, 0, 0)
    else solveFromFirstTile(initial)
  }

  private def allTiles(initial: TantrixBoard): IndexedSeq[HexTile] =
    (initial.tantrix.tiles.head.tile +: initial.unplacedTiles).toIndexedSeq

  private def solveFromFirstTile(initial: TantrixBoard): Option[Seq[TilePlacement]] = {
    val startTime = System.currentTimeMillis
    val search = createSearch(initial)
    val moves = searchUntilDone(search, initial)
    report(initial, moves, totalSteps, System.currentTimeMillis - startTime)
  }

  private def createSearch(initial: TantrixBoard): ShapeAssignSearch = {
    totalSteps = 0
    attempts = 0
    new ShapeAssignSearch(allTiles(initial), initial.primaryColor, initial.tantrix.tiles.head, rnd,
      steps => puzzle.refresh(initial, totalSteps + steps))
  }

  /** Make attempts, each with a bigger budget, until one finds a solution or shows that there is none. */
  private def searchUntilDone(search: ShapeAssignSearch, initial: TantrixBoard): Option[Seq[TilePlacement]] =
    Iterator.continually(makeAttempt(search, initial)).collectFirst {
      case Found(moves) => Some(moves)
      case Exhausted => None
    }.get

  private def makeAttempt(search: ShapeAssignSearch, initial: TantrixBoard): Outcome = {
    attempts += 1
    puzzle.refresh(initial, totalSteps)
    val outcome = search.run(baseBudget.toLong * luby(attempts))
    totalSteps += search.numSteps
    outcome
  }

  private def report(initial: TantrixBoard, moves: Option[Seq[TilePlacement]], numSteps: Long, millis: Long) = {
    puzzle.finalRefresh(moves, moves.map(_.foldLeft(initial)(puzzle.transition)), numSteps, millis)
    moves
  }
}
