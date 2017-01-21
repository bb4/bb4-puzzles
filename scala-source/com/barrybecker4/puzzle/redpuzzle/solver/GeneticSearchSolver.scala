// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT

package com.barrybecker4.puzzle.redpuzzle.solver

import com.barrybecker4.optimization.OptimizationListener
import com.barrybecker4.optimization.Optimizer
import com.barrybecker4.optimization.optimizee.Optimizee
import com.barrybecker4.optimization.parameter.ParameterArray
import com.barrybecker4.optimization.strategy.OptimizationStrategyType
import com.barrybecker4.puzzle.common.PuzzleController
import com.barrybecker4.puzzle.redpuzzle.model.Piece
import com.barrybecker4.puzzle.redpuzzle.model.PieceList
import com.barrybecker4.puzzle.redpuzzle.model.PieceParameterArray
import scala.collection.Seq
import com.barrybecker4.puzzle.redpuzzle.solver.FitnessFinder.MAX_FITS

/**
  * Solve the red puzzle using a genetic search algorithm.
  * Solves the puzzle in 3.5 seconds on Core2 duo system (6 generations).
  *
  * @author Barry Becker
  */
class GeneticSearchSolver(override val puzzle: PuzzleController[PieceList, Piece], val useConcurrency: Boolean)
                   extends RedPuzzleSolver(puzzle) with Optimizee with OptimizationListener {

  private var strategy = if (useConcurrency) OptimizationStrategyType.CONCURRENT_GENETIC_SEARCH
                         else OptimizationStrategyType.GENETIC_SEARCH
  private var fitnessFinder = new FitnessFinder
  private var currentBestFitness = MAX_FITS

  /** @return list of moves to a solution. */
  def solve: Option[Seq[Piece]] = {
    val initialGuess = new PieceParameterArray(pieces)
    solution = pieces
    val startTime = System.currentTimeMillis
    val optimizer = new Optimizer(this)
    optimizer.setListener(this)
    val theSolution = optimizer.doOptimization(strategy, initialGuess, MAX_FITS)
    solution = theSolution.asInstanceOf[PieceParameterArray].getPieceList
    System.out.println("Solution = " + solution)
    var moves: Option[List[Piece]] = Option.empty
    if (evaluateFitness(theSolution) >= MAX_FITS) moves = Some(solution.pieces)
    val elapsedTime = System.currentTimeMillis - startTime
    puzzle.finalRefresh(moves, Option.apply(solution), numTries, elapsedTime)
    moves
  }

  def getName = "Genetic Search Solver for Red Puzzle"

  /** terminate the solver if we find a solution with this fitness. */
  def getOptimalFitness = 0

  def evaluateByComparison = false

  /**
    * Return a low score if there are a lot of fits among the pieces.
    *
    * @param params parameters
    * @return fitness value. Low is good.
    */
  def evaluateFitness(params: ParameterArray): Double = {
    val pieces = params.asInstanceOf[PieceParameterArray].getPieceList
    val fitness = fitnessFinder.calculateFitness(pieces)
    if (fitness < currentBestFitness) currentBestFitness = fitness
    params.setFitness(fitness)
    fitness
  }

  def compareFitness(params1: ParameterArray, params2: ParameterArray): Double = {
    assert(false, "compareFitness not used since we evaluate in an absolute way.")
    0
  }

  /**
    * Called when the optimizer has made some progress optimizing.
    * Shows the current status. Update our current best guess at the solution.
    *
    * @param params optimization parameters
    */
  def optimizerChanged(params: ParameterArray) {
    solution = params.asInstanceOf[PieceParameterArray].getPieceList
    numTries += 1
    puzzle.refresh(solution, numTries)
    System.out.println("current best = " + currentBestFitness)
  }
}
