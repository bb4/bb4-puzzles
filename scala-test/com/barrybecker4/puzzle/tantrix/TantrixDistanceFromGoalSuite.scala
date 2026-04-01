// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix

import com.barrybecker4.math.MathUtil
import com.barrybecker4.puzzle.tantrix.model.TantrixBoard
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil._
import com.barrybecker4.puzzle.tantrix.solver.path.{PathEvaluator, TantrixPath}
import org.scalatest.funsuite.AnyFunSuite

/**
  * [[TantrixController.distanceFromGoal]] scales [[PathEvaluator]] fitness and must stay consistent
  * with it for [[com.barrybecker4.puzzle.common.solver.AStarPuzzleSolver]] ordering.
  */
class TantrixDistanceFromGoalSuite extends AnyFunSuite {

  private val ui = new StubTantrixRefreshable
  private val c = new TantrixController(ui)
  private val eval = PathEvaluator()

  private def scaledFitness(board: TantrixBoard): Int = {
    val path = new TantrixPath(board.tantrix, board.primaryColor, board.numTiles, MathUtil.RANDOM)
    (10.0 * Math.max(0, eval.evaluateFitness(path))).toInt
  }

  test("distanceFromGoal zero for solved fixtures") {
    assert(c.distanceFromGoal(place3SolvedTiles) == 0)
    assert(c.distanceFromGoal(place4SolvedTiles) == 0)
    assert(c.distanceFromGoal(place7SolvedTiles) == 0)
  }

  test("distanceFromGoal matches ten times PathEvaluator fitness") {
    // Requires a continuous primary path through all placed tiles (see [[Pathifier]]).
    val boards = Seq(place3UnsolvedTiles, place3of6UnsolvedTiles)
    boards.foreach { b =>
      assert(c.distanceFromGoal(b) == scaledFitness(b))
    }
  }

  test("distanceFromGoal is nonnegative and bounded by ten times FITNESS_RANGE") {
    val boards = Seq(
      place3SolvedTiles,
      place3UnsolvedTiles,
      place2of3Tiles_OneThenTwo,
      place3of6UnsolvedTiles
    )
    val maxH = (10.0 * com.barrybecker4.puzzle.tantrix.solver.path.PathEvaluator.FITNESS_RANGE).toInt
    boards.foreach { b =>
      val h = c.distanceFromGoal(b)
      assert(h >= 0, s"board=${b.primaryColor} size=${b.tantrix.size}")
      assert(h <= maxH, s"h=$h exceeds cap $maxH")
    }
  }

  test("distanceFromGoal improves along a solving prefix when path exists") {
    val start = place2of3Tiles_OneThenTwo
    val move = new com.barrybecker4.puzzle.tantrix.generation.MoveGenerator(start).generateMoves.head
    val solved = start.placeTile(move)
    assert(c.distanceFromGoal(start) > c.distanceFromGoal(solved))
    assert(c.distanceFromGoal(solved) == 0)
  }

  test("distanceFromGoal throws when tiles do not admit a primary path (Pathifier)") {
    assertThrows[IllegalStateException] {
      c.distanceFromGoal(place6UnsolvedTiles)
    }
  }

  test("distanceFromGoal returns same value on repeat for one board (heuristic cache)") {
    val b = place3UnsolvedTiles
    val h1 = c.distanceFromGoal(b)
    val h2 = c.distanceFromGoal(b)
    assert(h1 == h2)
  }
}
