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
  * The algorithms are listed in the order of how reliably and quickly they solve the standard 9 piece puzzle, the best
  * first. That is the order they have in the user interface. The order comes from 12 runs of each on one machine, with
  * the median time of the runs that solved it in brackets. Every exact search and the first two strategies from
  * bb4-optimization solved it in all 12: the fast backtracking search written for this puzzle (2 ms), the hand
  * crafted brute force (3 ms), the generic brute force (7 ms), iterative deepening A* (8 ms), the A* searches
  * (14 and 18 ms), the concurrent search that is mostly depth first (22 ms), min-conflicts (24 ms, which repairs the
  * pieces that do not fit), the other concurrent searches (36 and 37 ms) and tabu search (51 ms). Global simulated
  * annealing is next (9 of 12), then the genetic searches (about 2 in 3, in about a second), the iterated local search,
  * variable neighborhood search and simulated annealing (about 1 in 3, but quickly). Hill climbing and sampling get
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
    case MIN_CONFLICTS => new GeneticSearchSolver(controller, OptimizationStrategyType.MIN_CONFLICTS)
    case TABU_SEARCH => new GeneticSearchSolver(controller, OptimizationStrategyType.TABU_SEARCH)
    case SIMULATED_ANNEALING => new GeneticSearchSolver(controller, OptimizationStrategyType.SIMULATED_ANNEALING)
    case GLOBAL_HILL_CLIMBING => new GeneticSearchSolver(controller, OptimizationStrategyType.GLOBAL_HILL_CLIMBING)
    case HILL_CLIMBING => new GeneticSearchSolver(controller, OptimizationStrategyType.HILL_CLIMBING)
    case GLOBAL_SAMPLING => new GeneticSearchSolver(controller, OptimizationStrategyType.GLOBAL_SAMPLING)
    case BRUTE_FORCE => new GeneticSearchSolver(controller, OptimizationStrategyType.BRUTE_FORCE)
  }

  // The cases are in order of how well they do on this puzzle, most effective first (see the documentation above).
  case FAST_BACKTRACKING extends Algorithm
  case BRUTE_FORCE_ORIGINAL extends Algorithm
  case BRUTE_FORCE_SEQUENTIAL extends Algorithm
  case IDA_STAR extends Algorithm
  case A_STAR_CONCURRENT extends Algorithm
  case A_STAR_SEQUENTIAL extends Algorithm
  case CONCURRENT_DEPTH extends Algorithm
  case MIN_CONFLICTS extends Algorithm
  case CONCURRENT_OPTIMUM extends Algorithm
  case CONCURRENT_BREADTH extends Algorithm
  case TABU_SEARCH extends Algorithm
  case GLOBAL_SIMULATED_ANNEALING extends Algorithm
  case CONCURRENT_GENETIC_SEARCH extends Algorithm
  case GENETIC_SEARCH extends Algorithm
  case ITERATED_LOCAL_SEARCH extends Algorithm
  case VARIABLE_NEIGHBORHOOD_SEARCH extends Algorithm
  case SIMULATED_ANNEALING extends Algorithm
  case GLOBAL_HILL_CLIMBING extends Algorithm
  case HILL_CLIMBING extends Algorithm
  case GLOBAL_SAMPLING extends Algorithm
  case BRUTE_FORCE extends Algorithm
}
