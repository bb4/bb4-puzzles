// Copyright by Barry G. Becker, 2017 - 2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.solver

import com.barrybecker4.optimization.OptimizationListener
import com.barrybecker4.optimization.Optimizer
import com.barrybecker4.optimization.optimizee.Optimizee
import com.barrybecker4.optimization.parameter.{ParameterArray, ParameterArrayWithFitness}
import com.barrybecker4.optimization.strategy.OptimizationStrategyType
import com.barrybecker4.puzzle.common.*
import com.barrybecker4.puzzle.redpuzzle.model.{OrientedPiece, PieceList, PieceParameterArray}
import com.barrybecker4.puzzle.redpuzzle.solver.FitnessFinder.MAX_FITS

import scala.util.Random

/**
  * Solve the red puzzle using one of the bb4-optimization strategies, such as a genetic search.
  * The genetic search solves the puzzle in 3.5 seconds on Core2 duo system (6 generations).
  * @param puzzle the controller of the puzzle to solve
  * @param strategy the optimization strategy to use. It needs to work with permutations of the pieces.
  * @author Barry Becker
  */
class GeneticSearchSolver(override val puzzle: PuzzleController[PieceList, OrientedPiece],
                          val strategy: OptimizationStrategyType)
                   extends RedPuzzleSolver(puzzle) with Optimizee with OptimizationListener {

  /** @param useConcurrency true to use the concurrent genetic search, and false for the plain genetic search. */
  def this(puzzle: PuzzleController[PieceList, OrientedPiece], useConcurrency: Boolean) =
    this(puzzle,
      if (useConcurrency) OptimizationStrategyType.CONCURRENT_GENETIC_SEARCH else OptimizationStrategyType.GENETIC_SEARCH)

  private val fitnessFinder = new FitnessFinder
  private var currentBestFitness = 10 + MAX_FITS

  /** @return list of moves to a solution. */
  def solve: Option[Seq[OrientedPiece]] = {
    val initialGuess = new PieceParameterArray(pieces, new Random(1))
    val startTime = System.currentTimeMillis
    val optimizer = new Optimizer(this)
    optimizer.setListener(this)
    val theSolution = optimizer.doOptimization(strategy, initialGuess, MAX_FITS).best
    solution = theSolution.pa.asInstanceOf[PieceParameterArray].getPieceList
    val moves = if (evaluateFitness(theSolution.pa) == 0) Some(solution.pieces) else None
    val elapsedTime = System.currentTimeMillis - startTime
    puzzle.finalRefresh(moves, Some(solution), numTries, elapsedTime)
    moves
  }

  def getName = s"Red Puzzle solver using $strategy"

  /** terminate the solver if we find a solution with this fitness. */
  def getOptimalFitness = 0

  def evaluateByComparison = false

  /** Return a low score if there are a lot of fits among the pieces.
    * @param params parameters
    * @return fitness value. Low is good.
    */
  def evaluateFitness(params: ParameterArray): Double = {
    val pieces = params.asInstanceOf[PieceParameterArray].getPieceList
    val fitness = fitnessFinder.calculateFitness(pieces)
    if (fitness < currentBestFitness) {
      //println("better fitness = " + fitness)
      currentBestFitness = fitness
    }
    fitness
  }

  def compareFitness(params1: ParameterArray, params2: ParameterArray): Double =
    throw new UnsupportedOperationException("compareFitness not used since we evaluate in an absolute way.")

  /** Called when the optimizer has made some progress optimizing.
    * Shows the current status. Update our current best guess at the solution.
    * @param params optimization parameters
    */
  def optimizerChanged(params: ParameterArrayWithFitness): Unit = {
    solution = params.pa.asInstanceOf[PieceParameterArray].getPieceList
    numTries += 1
    puzzle.refresh(solution, numTries)
  }
}
