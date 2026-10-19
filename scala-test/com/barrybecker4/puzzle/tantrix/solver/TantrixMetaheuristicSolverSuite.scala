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
}
