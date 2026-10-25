// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.solver

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.math.MathUtil
import com.barrybecker4.common.testsupport.i18n.StubMessageContext
import com.barrybecker4.optimization.strategy.OptimizationStrategyType
import com.barrybecker4.puzzle.redpuzzle.{RedPuzzleController, StubRefreshable}
import com.barrybecker4.puzzle.redpuzzle.model.{PieceList, PieceParameterArray}
import com.barrybecker4.puzzle.redpuzzle.model.PieceLists.RED_INITIAL_PIECES_9
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

class AlgorithmSuite extends AnyFunSuite {

  AppContext.injectMessageContext(new StubMessageContext)
  private val controller = new RedPuzzleController(new StubRefreshable)

  private def optimizerSolvers: Seq[(Algorithm, GeneticSearchSolver)] =
    Algorithm.values.toSeq.flatMap { a =>
      a.createSolver(controller) match {
        case solver: GeneticSearchSolver => Some((a, solver))
        case _ => None
      }
    }

  test("each optimization algorithm uses the optimization strategy that has the same name") {
    val solvers = optimizerSolvers
    assert(solvers.nonEmpty)
    solvers.foreach { case (algorithm, solver) => assertResult(algorithm.toString)(solver.strategy.toString) }
  }

  test("every optimization strategy that can be used with the pieces is an algorithm") {
    val pieces = new PieceParameterArray(new PieceList(RED_INITIAL_PIECES_9), new Random(1))
    val used = optimizerSolvers.map(_._2.strategy).toSet
    for (strategy <- OptimizationStrategyType.values) {
      val solver = new GeneticSearchSolver(controller, strategy)
      if (strategy.appliesTo(solver, pieces))
        assert(used.contains(strategy), s"$strategy can be used for the red puzzle, but is not one of the algorithms")
    }
  }

  test("strategies that do not work with a permutation of pieces are not algorithms") {
    val used = optimizerSolvers.map(_._2.strategy).toSet
    List(OptimizationStrategyType.DIFFERENTIAL_EVOLUTION, OptimizationStrategyType.PARTICLE_SWARM,
      OptimizationStrategyType.STATE_SPACE_SEARCH).foreach(strategy => assert(!used.contains(strategy)))
  }

  test("the genetic search can still be made by saying if it is concurrent") {
    assertResult(OptimizationStrategyType.GENETIC_SEARCH)(new GeneticSearchSolver(controller, false).strategy)
    assertResult(OptimizationStrategyType.CONCURRENT_GENETIC_SEARCH)(new GeneticSearchSolver(controller, true).strategy)
  }

  test("the exact searches come before the optimization strategies, and the weakest strategies come last") {
    val values = Algorithm.values.toSeq
    val firstOptimizer = values.indexWhere(a => a.createSolver(controller).isInstanceOf[GeneticSearchSolver])
    assert(values.drop(firstOptimizer).forall(a => a.createSolver(controller).isInstanceOf[GeneticSearchSolver]))
    assertResult(Algorithm.BRUTE_FORCE)(values.last)
  }

  test("min-conflicts and tabu search solve the puzzle, which the other strategies do not always do") {
    List(Algorithm.MIN_CONFLICTS, Algorithm.TABU_SEARCH).foreach { algorithm =>
      MathUtil.RANDOM.setSeed(1)
      assert(algorithm.createSolver(new RedPuzzleController(new StubRefreshable)).solve.isDefined, algorithm.toString)
    }
  }

  test("the solver reports the pieces that do not fit as the conflicted positions") {
    val pieces = new PieceParameterArray(new PieceList(RED_INITIAL_PIECES_9), new Random(1))
    val solver = new GeneticSearchSolver(controller, OptimizationStrategyType.MIN_CONFLICTS)
    assertResult(pieces.getPieceList.conflictedPositions)(solver.conflictedPositions(pieces))
  }
}
