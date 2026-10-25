// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver

import com.barrybecker4.puzzle.tantrix.{FixedStartTantrixController, StubTantrixRefreshable, TantrixTstUtil}
import org.scalatest.funsuite.AnyFunSuite

class TantrixMetaheuristicSolverSuite extends AnyFunSuite {

  private val ui = new StubTantrixRefreshable

  test("Simulated annealing and global hill climbing are registered on Algorithm enum") {
    assert(Algorithm.values.contains(Algorithm.SIMULATED_ANNEALING))
    assert(Algorithm.values.contains(Algorithm.GLOBAL_HILL_CLIMBING))
  }

  test("The strategies added in bb4-optimization 2.1 are registered on the Algorithm enum") {
    List(Algorithm.GLOBAL_SIMULATED_ANNEALING, Algorithm.ITERATED_LOCAL_SEARCH, Algorithm.VARIABLE_NEIGHBORHOOD_SEARCH)
      .foreach(a => assert(Algorithm.values.contains(a)))
  }

  test("each 2.1 algorithm constructs a GeneticSearchSolver with the matching optimization strategy") {
    import com.barrybecker4.optimization.strategy.OptimizationStrategyType
    val ctrl = new FixedStartTantrixController(ui, TantrixTstUtil.place2of3Tiles_OneThenTwo)
    def strategyOf(a: Algorithm) = a.createSolver(ctrl).asInstanceOf[GeneticSearchSolver].optimizationStrategy
    assert(strategyOf(Algorithm.GLOBAL_SIMULATED_ANNEALING) == OptimizationStrategyType.GLOBAL_SIMULATED_ANNEALING)
    assert(strategyOf(Algorithm.ITERATED_LOCAL_SEARCH) == OptimizationStrategyType.ITERATED_LOCAL_SEARCH)
    assert(strategyOf(Algorithm.VARIABLE_NEIGHBORHOOD_SEARCH) == OptimizationStrategyType.VARIABLE_NEIGHBORHOOD_SEARCH)
  }

  test("tabu search is an algorithm, and a path has no single swap moves so min-conflicts is not one") {
    import com.barrybecker4.optimization.strategy.OptimizationStrategyType
    val ctrl = new FixedStartTantrixController(ui, TantrixTstUtil.place2of3Tiles_OneThenTwo)
    val tabu = Algorithm.TABU_SEARCH.createSolver(ctrl).asInstanceOf[GeneticSearchSolver]
    assert(tabu.optimizationStrategy == OptimizationStrategyType.TABU_SEARCH)
    val path = new com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath(ctrl.initialState, new scala.util.Random(1))
    assert(!path.supportsLocalMoves)
    assert(!OptimizationStrategyType.MIN_CONFLICTS.appliesTo(tabu, path))
  }

  test("SA, global HC, and genetic construct GeneticSearchSolver with correct optimization strategy") {
    val ctrl = new FixedStartTantrixController(ui, TantrixTstUtil.place2of3Tiles_OneThenTwo)
    val sa = Algorithm.SIMULATED_ANNEALING.createSolver(ctrl).asInstanceOf[GeneticSearchSolver]
    val ghc = Algorithm.GLOBAL_HILL_CLIMBING.createSolver(ctrl).asInstanceOf[GeneticSearchSolver]
    val ga = Algorithm.GENETIC_SEARCH.createSolver(ctrl).asInstanceOf[GeneticSearchSolver]
    import com.barrybecker4.optimization.strategy.OptimizationStrategyType
    assert(sa.optimizationStrategy == OptimizationStrategyType.SIMULATED_ANNEALING)
    assert(ghc.optimizationStrategy == OptimizationStrategyType.GLOBAL_HILL_CLIMBING)
    assert(ga.optimizationStrategy == OptimizationStrategyType.GENETIC_SEARCH)
  }

  test("every optimization strategy that can be used with a tantrix path is an algorithm") {
    import com.barrybecker4.optimization.strategy.OptimizationStrategyType
    val ctrl = new FixedStartTantrixController(ui, TantrixTstUtil.place2of3Tiles_OneThenTwo)
    val used = Algorithm.values.toSeq.flatMap(a => a.createSolver(ctrl) match {
      case solver: GeneticSearchSolver => Some(solver.optimizationStrategy)
      case _ => None
    }).toSet
    val path = new com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath(ctrl.initialState, new scala.util.Random(1))
    for (strategy <- OptimizationStrategyType.values) {
      val solver = new GeneticSearchSolver(ctrl, strategy)
      if (strategy.appliesTo(solver, path))
        assert(used.contains(strategy), s"$strategy can be used for tantrix, but is not one of the algorithms")
    }
  }

  test("each optimization algorithm uses the optimization strategy that has the same name") {
    val ctrl = new FixedStartTantrixController(ui, TantrixTstUtil.place2of3Tiles_OneThenTwo)
    Algorithm.values.foreach { a =>
      a.createSolver(ctrl) match {
        case solver: GeneticSearchSolver => assertResult(a.toString)(solver.optimizationStrategy.toString)
        case _ =>
      }
    }
  }

  test("the exact searches that work best come first, and the weakest strategies come last") {
    val values = Algorithm.values.toSeq
    assertResult(Algorithm.SHAPE_THEN_ASSIGN)(values.head)
    assertResult(Algorithm.RESTARTING_DEPTH_FIRST)(values(1))
    assertResult(Algorithm.BRUTE_FORCE)(values.last)
    assert(values.indexOf(Algorithm.GLOBAL_SIMULATED_ANNEALING) < values.indexOf(Algorithm.HILL_CLIMBING))
    assert(values.indexOf(Algorithm.RESTARTING_DEPTH_FIRST) < values.indexOf(Algorithm.SIMPLE_SEQUENTIAL))
  }
}
