// Copyright by Barry G. Becker, 2017 - 2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.solver

import com.barrybecker4.optimization.OptimizationListener
import com.barrybecker4.optimization.Optimizer
import com.barrybecker4.optimization.optimizee.Optimizee
import com.barrybecker4.optimization.parameter.{ParameterArray, ParameterArrayWithFitness}
import com.barrybecker4.puzzle.common.PuzzleController
import com.barrybecker4.puzzle.redpuzzle.model.{OrientedPiece, PieceList, PieceParameterArray}

import scala.collection.Seq
import com.barrybecker4.puzzle.redpuzzle.solver.FitnessFinder.MAX_FITS

import scala.util.Random

/**
  * Solve the red puzzle using a genetic search algorithm.
  * Solves the puzzle in 3.5 seconds on Core2 duo system (6 generations).
  * @author Barry Becker
  */
class GeneticSearchSolver(override val puzzle: PuzzleController[PieceList, OrientedPiece], val useConcurrency: Boolean)
                   extends RedPuzzleSolver(puzzle) with Optimizee with OptimizationListener {

  private val strategy =
    if (useConcurrency) com.barrybecker4.optimization.strategy.CONCURRENT_GENETIC_SEARCH
    else com.barrybecker4.optimization.strategy.GENETIC_SEARCH
  private val fitnessFinder = new FitnessFinder
  private var currentBestFitness = 10 + MAX_FITS

  /** @return list of moves to a solution. */
  def solve: Option[Seq[OrientedPiece]] = {
    val initialGuess = new PieceParameterArray(pieces, new Random(1))
    val startTime = System.currentTimeMillis
    val optimizer = new Optimizer(this)
    optimizer.setListener(this)
    val theSolution = optimizer.doOptimization(strategy, initialGuess, MAX_FITS)
    solution = theSolution.pa.asInstanceOf[PieceParameterArray].getPieceList
    val moves = if (evaluateFitness(theSolution.pa) == 0) Some(solution.pieces) else Option.empty
    val elapsedTime = System.currentTimeMillis - startTime
    puzzle.finalRefresh(moves, Option.apply(solution), numTries, elapsedTime)
    moves
  }

  def getName = "Genetic Search Solver for Red Puzzle"

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
    println("fitness = " + fitness)
    if (fitness < currentBestFitness) currentBestFitness = fitness
    fitness
  }

  def compareFitness(params1: ParameterArray, params2: ParameterArray): Double =
    throw new UnsupportedOperationException("compareFitness not used since we evaluate in an absolute way.")

  /** Called when the optimizer has made some progress optimizing.
    * Shows the current status. Update our current best guess at the solution.
    * @param params optimization parameters
    */
  def optimizerChanged(params: ParameterArrayWithFitness) {
    solution = params.pa.asInstanceOf[PieceParameterArray].getPieceList
    numTries += 1
    puzzle.refresh(solution, numTries)
    //System.out.println("current best = " + currentBestFitness)
  }
}
