// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.solver

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.optimization.strategy.OptimizationStrategyType
import com.barrybecker4.puzzle.common.{AlgorithmEnum, PuzzleController}
import com.barrybecker4.puzzle.common.solver._
import com.barrybecker4.puzzle.redpuzzle.model.{OrientedPiece, PieceList}


/**
  * Enum for type of solver to employ when solving the puzzle.
  *
  * The algorithms are listed in the order of how well they work, with the most effective first. That is the order they
  * have in the user interface. Every one of the exact searches finds the solution of the standard 9 piece puzzle in
  * well under a second. The fast backtracking search, which is written for this puzzle, is the quickest by far, and
  * iterative deepening A* is the quickest of the generic ones. The strategies from bb4-optimization
  * come after them. They find it in a second or less when they succeed, which is about two times in three for the
  * iterated local search and the genetic searches, and less often for the others. Hill climbing and sampling get
  * stuck, and brute force over the orderings of the pieces is too slow to finish.
  */
enum Algorithm extends AlgorithmEnum[PieceList, OrientedPiece] {

  private val label = AppContext.getLabel(this.toString)
  def getLabel: String = label

  /** Create an instance of the algorithm given the controller and a refreshable. */
  def createSolver(controller: PuzzleController[PieceList, OrientedPiece]): PuzzleSolver[OrientedPiece] = this match {
    case FAST_BACKTRACKING => new FastBacktrackingSolver(controller)
    case CONCURRENT_BREADTH => new ConcurrentPuzzleSolver[PieceList, OrientedPiece](controller, 0.3f)
    case CONCURRENT_OPTIMUM => new ConcurrentPuzzleSolver[PieceList, OrientedPiece](controller, 0.2f)
    case CONCURRENT_DEPTH => new ConcurrentPuzzleSolver[PieceList, OrientedPiece](controller, 0.1f)
    case BRUTE_FORCE_ORIGINAL => new BruteForceSolver(controller)
    case BRUTE_FORCE_SEQUENTIAL => new SequentialPuzzleSolver[PieceList, OrientedPiece](controller)
    case A_STAR_CONCURRENT => new AStarConcurrentPuzzleSolver[PieceList, OrientedPiece](controller)
    case A_STAR_SEQUENTIAL => new AStarPuzzleSolver[PieceList, OrientedPiece](controller)
    case IDA_STAR => new IDAStarPuzzleSolver[PieceList, OrientedPiece](controller)
    case GENETIC_SEARCH => new GeneticSearchSolver(controller, OptimizationStrategyType.GENETIC_SEARCH)
    case CONCURRENT_GENETIC_SEARCH => new GeneticSearchSolver(controller, OptimizationStrategyType.CONCURRENT_GENETIC_SEARCH)
    case ITERATED_LOCAL_SEARCH => new GeneticSearchSolver(controller, OptimizationStrategyType.ITERATED_LOCAL_SEARCH)
    case GLOBAL_SIMULATED_ANNEALING =>
      new GeneticSearchSolver(controller, OptimizationStrategyType.GLOBAL_SIMULATED_ANNEALING)
    case VARIABLE_NEIGHBORHOOD_SEARCH =>
      new GeneticSearchSolver(controller, OptimizationStrategyType.VARIABLE_NEIGHBORHOOD_SEARCH)
    case SIMULATED_ANNEALING => new GeneticSearchSolver(controller, OptimizationStrategyType.SIMULATED_ANNEALING)
    case GLOBAL_HILL_CLIMBING => new GeneticSearchSolver(controller, OptimizationStrategyType.GLOBAL_HILL_CLIMBING)
    case HILL_CLIMBING => new GeneticSearchSolver(controller, OptimizationStrategyType.HILL_CLIMBING)
    case GLOBAL_SAMPLING => new GeneticSearchSolver(controller, OptimizationStrategyType.GLOBAL_SAMPLING)
    case BRUTE_FORCE => new GeneticSearchSolver(controller, OptimizationStrategyType.BRUTE_FORCE)
  }

  // The cases are in order of how well they do on this puzzle, most effective first (see the documentation above).
  case FAST_BACKTRACKING extends Algorithm
  case IDA_STAR extends Algorithm
  case BRUTE_FORCE_ORIGINAL extends Algorithm
  case CONCURRENT_DEPTH extends Algorithm
  case BRUTE_FORCE_SEQUENTIAL extends Algorithm
  case A_STAR_CONCURRENT extends Algorithm
  case A_STAR_SEQUENTIAL extends Algorithm
  case CONCURRENT_BREADTH extends Algorithm
  case CONCURRENT_OPTIMUM extends Algorithm
  case ITERATED_LOCAL_SEARCH extends Algorithm
  case CONCURRENT_GENETIC_SEARCH extends Algorithm
  case GENETIC_SEARCH extends Algorithm
  case GLOBAL_SIMULATED_ANNEALING extends Algorithm
  case VARIABLE_NEIGHBORHOOD_SEARCH extends Algorithm
  case SIMULATED_ANNEALING extends Algorithm
  case GLOBAL_HILL_CLIMBING extends Algorithm
  case HILL_CLIMBING extends Algorithm
  case GLOBAL_SAMPLING extends Algorithm
  case BRUTE_FORCE extends Algorithm
}
