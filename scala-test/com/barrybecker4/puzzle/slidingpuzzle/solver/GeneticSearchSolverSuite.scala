// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.solver

import com.barrybecker4.optimization.strategy.OptimizationStrategyType
import com.barrybecker4.puzzle.slidingpuzzle.Algorithm
import com.barrybecker4.puzzle.slidingpuzzle.solver.SliderTestSupport.*
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

class GeneticSearchSolverSuite extends AnyFunSuite {

  private val hardest3 = Seq(8, 6, 7, 2, 5, 4, 3, 0, 1)
  private val easy3 = Seq(1, 2, 3, 4, 5, 6, 7, 0, 8)

  test("min-conflicts solves an easy 3 by 3 board with a legal slide path") {
    val board = boardOf(easy3)
    val moves = new GeneticSearchSolver(controllerFor(board), OptimizationStrategyType.MIN_CONFLICTS).solve
    assert(moves.isDefined)
    assert(isSolvedBy(board, moves.get))
  }

  test("genetic search solves a scrambled 3 by 3 board with a legal slide path") {
    val board = boardOf(hardest3)
    val moves = Algorithm.GENETIC_SEARCH.createSolver(controllerFor(board)).solve
    assert(moves.isDefined)
    assert(isSolvedBy(board, moves.get))
  }

  test("min-conflicts solves random solvable 3 by 3 boards") {
    val rnd = new Random(11)
    for (_ <- 1 to 5) {
      val board = boardOf(randomSolvableTiles(3, rnd))
      val moves = new GeneticSearchSolver(controllerFor(board), OptimizationStrategyType.MIN_CONFLICTS).solve
      assert(moves.isDefined, s"expected a solution for $board")
      assert(isSolvedBy(board, moves.get))
    }
  }

  test("returns none for a board that cannot be slid into order") {
    val unsolvable = boardOf(Seq(2, 1, 3, 4, 5, 6, 7, 8, 0))
    // Metaheuristic can still rearrange via swaps to the goal; path extraction must refuse the unsolvable start.
    val solver = new GeneticSearchSolver(controllerFor(unsolvable), OptimizationStrategyType.MIN_CONFLICTS)
    assert(solver.solve.isEmpty)
  }

  test("the boolean constructor selects genetic or concurrent genetic") {
    val board = boardOf(easy3)
    assertResult(OptimizationStrategyType.GENETIC_SEARCH)(
      new GeneticSearchSolver(controllerFor(board), false).strategy)
    assertResult(OptimizationStrategyType.CONCURRENT_GENETIC_SEARCH)(
      new GeneticSearchSolver(controllerFor(board), true).strategy)
  }
}
