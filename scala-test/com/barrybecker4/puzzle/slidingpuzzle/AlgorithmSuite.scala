// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle

import com.barrybecker4.optimization.strategy.OptimizationStrategyType
import com.barrybecker4.puzzle.slidingpuzzle.model.SliderBoard
import com.barrybecker4.puzzle.slidingpuzzle.model.TileParameterArray
import com.barrybecker4.puzzle.slidingpuzzle.solver.{FastSliderSolver, GeneticSearchSolver}
import com.barrybecker4.puzzle.slidingpuzzle.solver.SliderTestSupport.*
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

class AlgorithmSuite extends AnyFunSuite {

  private val controller = controllerFor(new SliderBoard(3))

  private def optimizerSolvers: Seq[(Algorithm, GeneticSearchSolver)] =
    Algorithm.values.toSeq.flatMap { a =>
      a.createSolver(controller) match {
        case solver: GeneticSearchSolver => Some((a, solver))
        case _ => None
      }
    }

  test("the fast algorithms use the fast solver") {
    assert(Algorithm.FAST_SHORTEST.createSolver(controller).isInstanceOf[FastSliderSolver])
    assert(Algorithm.FAST_SHORTEST_IF_QUICK.createSolver(controller).isInstanceOf[FastSliderSolver])
  }

  test("every algorithm can make a solver") {
    Algorithm.values.foreach(a => assert(a.createSolver(controller) != null))
  }

  test("each optimization algorithm uses the optimization strategy that has the same name") {
    val solvers = optimizerSolvers
    assert(solvers.nonEmpty)
    solvers.foreach { case (algorithm, solver) => assertResult(algorithm.toString)(solver.strategy.toString) }
  }

  test("every optimization strategy that can be used with the tiles is an algorithm") {
    val tiles = new TileParameterArray(new SliderBoard(3), new Random(1))
    val used = optimizerSolvers.map(_._2.strategy).toSet
    for (strategy <- OptimizationStrategyType.values) {
      val solver = new GeneticSearchSolver(controller, strategy)
      if (strategy.appliesTo(solver, tiles))
        assert(used.contains(strategy), s"$strategy can be used for the sliding puzzle, but is not one of the algorithms")
    }
  }

  test("strategies that do not work with a permutation of tiles are not algorithms") {
    val used = optimizerSolvers.map(_._2.strategy).toSet
    List(OptimizationStrategyType.DIFFERENTIAL_EVOLUTION, OptimizationStrategyType.PARTICLE_SWARM,
      OptimizationStrategyType.STATE_SPACE_SEARCH).foreach(strategy => assert(!used.contains(strategy)))
  }

  test("the genetic search can still be made by saying if it is concurrent") {
    assertResult(OptimizationStrategyType.GENETIC_SEARCH)(new GeneticSearchSolver(controller, false).strategy)
    assertResult(OptimizationStrategyType.CONCURRENT_GENETIC_SEARCH)(new GeneticSearchSolver(controller, true).strategy)
  }

  test("the fastest custom searches come first, and brute force comes last") {
    val values = Algorithm.values.toSeq
    assertResult(Algorithm.FAST_SHORTEST_IF_QUICK)(values.head)
    assertResult(Algorithm.BRUTE_FORCE)(values.last)
    assert(values.indexOf(Algorithm.MIN_CONFLICTS) < values.indexOf(Algorithm.HILL_CLIMBING))
    assert(values.indexOf(Algorithm.FAST_SHORTEST) < values.indexOf(Algorithm.GENETIC_SEARCH))
  }
}
