// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver

import com.barrybecker4.optimization.{OptimizationListener, Optimizer}
import com.barrybecker4.optimization.optimizee.Optimizee
import com.barrybecker4.optimization.parameter.ParameterArray
import com.barrybecker4.optimization.strategy.OptimizationStrategyType
import com.barrybecker4.puzzle.common.PuzzleController
import com.barrybecker4.puzzle.tantrix.model.{TantrixBoard, TilePlacement}
import com.barrybecker4.puzzle.tantrix.solver.path.{PathEvaluator, TantrixPath}
import com.barrybecker4.puzzle.tantrix1.solver.path.PathEvaluator.SOLVED_THRESH

import scala.collection.Seq

/**
  * Solve the Tantrix puzzle using a genetic search algorithm.
  *
  * @author Barry Becker
  */
class GeneticSearchSolver(var controller: PuzzleController[TantrixBoard, TilePlacement], val useConcurrency: Boolean)
  extends TantrixSolver(controller.initialState) with Optimizee with OptimizationListener {

  /** either genetic or concurrent genetic strategy. */
  private val strategy = if (useConcurrency) OptimizationStrategyType.CONCURRENT_GENETIC_SEARCH
                         else OptimizationStrategyType.GENETIC_SEARCH
  private var evaluator = new PathEvaluator
  private var numTries: Int = 0
  private var currentBestFitness = SOLVED_THRESH

  /**
    * @return list of moves to a solution.
    */
  def solve: Option[Seq[TilePlacement]] = {
    val initialGuess = new TantrixPath(board)
    assert(initialGuess.size > 0, "The random path should have some tiles!")
    val startTime = System.currentTimeMillis
    val optimizer = new Optimizer(this)
    optimizer.setListener(this)
    val foundSolution = optimizer.doOptimization(strategy, initialGuess, SOLVED_THRESH)
    solution = new TantrixBoard(solution.asInstanceOf[TantrixPath].getTilePlacements, board.primaryColor)
    val tilePlacements =
      if (evaluateFitness(foundSolution) <= 0) Option.apply(foundSolution.asInstanceOf[TantrixPath].tiles)
      else Option.empty
    val elapsedTime = System.currentTimeMillis - startTime
    controller.finalRefresh(tilePlacements, Option.apply(solution), numTries, elapsedTime)
    tilePlacements
  }

  def getName = "Genetic Search Solver for Tantrix Puzzle"

  /** terminate the solver if we find a solution with this fitness. */
  def getOptimalFitness = 0 /* SOLVED_THRESH*/

  def evaluateByComparison = false

  /**
    * Return 0 or less if a perfect solution has been found.
    *
    * @param params parameters
    * @return fitness value. High is good.
    */
  def evaluateFitness(params: ParameterArray): Double = {
    val fitness = evaluator.evaluateFitness(params.asInstanceOf[TantrixPath])
    params.setFitness(fitness)
    if (fitness < currentBestFitness) currentBestFitness = fitness
    fitness
  }

  def compareFitness(params1: ParameterArray, params2: ParameterArray): Double = {
    assert(assertion = false, "compareFitness not used since we evaluate in an absolute way.")
    0
  }

  /**
    * Called when the optimizer has made some progress optimizing.
    * Shows the current status.
    *
    * @param params optimized array of parameters representing tiles
    */
  def optimizerChanged(params: ParameterArray) {
    // update our current best guess at the solution.
    val path = params.asInstanceOf[TantrixPath]
    solution = new TantrixBoard(path.getTilePlacements, path.primaryPathColor)
    controller.refresh(solution, {
      numTries += 1; numTries - 1
    })
    System.out.println("current best fitness = " + currentBestFitness)
  }
}
