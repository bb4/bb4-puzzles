// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.optimization.strategy.OptimizationStrategyType
import com.barrybecker4.puzzle.common.solver._
import com.barrybecker4.puzzle.common.{AlgorithmEnum, PuzzleController}
import com.barrybecker4.puzzle.tantrix.model.{TantrixBoard, TilePlacement}

/**
  * Enum for type of solver to employ when solving the puzzle.
  *
  * The algorithms are listed in the order of how well they work, with the most effective first. That is the order they
  * have in the user interface. It is based on measurements on the first 12 to 20 tiles, so it will not hold for
  * every puzzle:
  *  - The shape then assign search builds the shape of the loop and keeps the possible tiles for each cell as it
  *    goes. It solves puzzles of 30 tiles in a fraction of a second, where nothing else can. It handles up to 32 tiles.
  *  - The restarting depth first search solves puzzles of up to about 26 tiles. The concurrent searches that
  *    are mostly breadth first or balanced are next, then the sequential depth first search. Depth first does well on
  *    small puzzles, but falls behind on larger ones.
  *  - Of the strategies from bb4-optimization, the ones that anneal do best, then the genetic ones,
  *    then iterated local search and variable neighborhood search, and then tabu search, which solves small
  *    puzzles but fails at 9 tiles within 15 seconds. They find solutions of 12 to 15 tiles in
  *    seconds, but not reliably.
  *  - A*, which gets nothing from its estimate of the distance to the goal because every solution is as deep as
  *    the number of tiles, comes after them.
  *  - Hill climbing and sampling get stuck, and brute force over the orderings of the tiles can only
  *    finish for a few tiles. They are listed for completeness.
  *
  */
enum Algorithm extends AlgorithmEnum[TantrixBoard, TilePlacement] {

  private val label = AppContext.getLabel(this.toString)
  def getLabel: String = label


  /**
    * Create an instance of the algorithm given the controller and a refreshable.
    */
  def createSolver(controller: PuzzleController[TantrixBoard, TilePlacement]): PuzzleSolver[TilePlacement] = this match {
    case SIMPLE_SEQUENTIAL => new SequentialPuzzleSolver[TantrixBoard, TilePlacement](controller)
    case SHAPE_THEN_ASSIGN => new ShapeThenAssignSolver(controller)
    case RESTARTING_DEPTH_FIRST => new RestartingDepthFirstSolver(controller)
    case BRUTE_FORCE_SEQUENTIAL => new AStarPuzzleSolver[TantrixBoard, TilePlacement](controller)
    case A_STAR_SEQUENTIAL => new AStarPuzzleSolver[TantrixBoard, TilePlacement](controller)
    case A_STAR_CONCURRENT => new AStarConcurrentPuzzleSolver[TantrixBoard, TilePlacement](controller)
    case IDA_STAR => new IDAStarPuzzleSolver[TantrixBoard, TilePlacement](controller)
    case CONCURRENT_BREADTH => new ConcurrentPuzzleSolver[TantrixBoard, TilePlacement](controller, 0.4f)
    case CONCURRENT_DEPTH => new ConcurrentPuzzleSolver[TantrixBoard, TilePlacement](controller, 0.12f)
    case CONCURRENT_OPTIMUM => new ConcurrentPuzzleSolver[TantrixBoard, TilePlacement](controller, 0.2f)
    case GENETIC_SEARCH => new GeneticSearchSolver(controller, OptimizationStrategyType.GENETIC_SEARCH)
    case CONCURRENT_GENETIC_SEARCH =>
      new GeneticSearchSolver(controller, OptimizationStrategyType.CONCURRENT_GENETIC_SEARCH)
    case SIMULATED_ANNEALING =>
      new GeneticSearchSolver(controller, OptimizationStrategyType.SIMULATED_ANNEALING)
    case GLOBAL_HILL_CLIMBING =>
      new GeneticSearchSolver(controller, OptimizationStrategyType.GLOBAL_HILL_CLIMBING)
    case GLOBAL_SIMULATED_ANNEALING =>
      new GeneticSearchSolver(controller, OptimizationStrategyType.GLOBAL_SIMULATED_ANNEALING)
    case ITERATED_LOCAL_SEARCH =>
      new GeneticSearchSolver(controller, OptimizationStrategyType.ITERATED_LOCAL_SEARCH)
    case VARIABLE_NEIGHBORHOOD_SEARCH =>
      new GeneticSearchSolver(controller, OptimizationStrategyType.VARIABLE_NEIGHBORHOOD_SEARCH)
    case TABU_SEARCH => new GeneticSearchSolver(controller, OptimizationStrategyType.TABU_SEARCH)
    case HILL_CLIMBING => new GeneticSearchSolver(controller, OptimizationStrategyType.HILL_CLIMBING)
    case GLOBAL_SAMPLING => new GeneticSearchSolver(controller, OptimizationStrategyType.GLOBAL_SAMPLING)
    case BRUTE_FORCE => new GeneticSearchSolver(controller, OptimizationStrategyType.BRUTE_FORCE)
  }

  // The cases are in order of how well they do on this puzzle, most effective first (see the documentation above).
  case SHAPE_THEN_ASSIGN extends Algorithm
  case RESTARTING_DEPTH_FIRST extends Algorithm
  case CONCURRENT_OPTIMUM extends Algorithm
  case CONCURRENT_BREADTH extends Algorithm
  case SIMPLE_SEQUENTIAL extends Algorithm
  case CONCURRENT_DEPTH extends Algorithm
  case GLOBAL_SIMULATED_ANNEALING extends Algorithm
  case SIMULATED_ANNEALING extends Algorithm
  case GENETIC_SEARCH extends Algorithm
  case CONCURRENT_GENETIC_SEARCH extends Algorithm
  case ITERATED_LOCAL_SEARCH extends Algorithm
  case VARIABLE_NEIGHBORHOOD_SEARCH extends Algorithm
  case TABU_SEARCH extends Algorithm
  case IDA_STAR extends Algorithm
  case A_STAR_CONCURRENT extends Algorithm
  case A_STAR_SEQUENTIAL extends Algorithm
  case BRUTE_FORCE_SEQUENTIAL extends Algorithm
  case GLOBAL_HILL_CLIMBING extends Algorithm
  case HILL_CLIMBING extends Algorithm
  case GLOBAL_SAMPLING extends Algorithm
  case BRUTE_FORCE extends Algorithm
}
