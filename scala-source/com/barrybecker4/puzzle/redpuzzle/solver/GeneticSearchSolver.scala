// Copyright by Barry G. Becker, 2017 - 2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.solver

import com.barrybecker4.math.MathUtil
import com.barrybecker4.optimization.OptimizationListener
import com.barrybecker4.optimization.optimizee.Optimizee
import com.barrybecker4.optimization.parameter.{Move, ParameterArray, ParameterArrayWithFitness}
import com.barrybecker4.optimization.strategy.{OptimizationStrategy, OptimizationStrategyType}
import com.barrybecker4.optimization.strategy.annealing.{GlobalSimulatedAnnealingStrategy, SimulatedAnnealingStrategy}
import com.barrybecker4.optimization.strategy.genetic.GeneticSearchStrategy
import com.barrybecker4.puzzle.common.*
import com.barrybecker4.puzzle.redpuzzle.model.{OrientedPiece, PieceList, PieceParameterArray}
import com.barrybecker4.puzzle.redpuzzle.solver.FitnessFinder.MAX_FITS

import scala.util.Random

object GeneticSearchSolver {
  /**
    * How often a move comes from [[GuidedSwap]] rather than being random. Over 20 puzzles, genetic search solved
    * 9 with none, 12 with 0.6 and 15 with 1.0; annealing solved 1, 3 and 8. So always use it.
    */
  val GUIDED_MOVE_PROBABILITY = 1.0
}

/**
  * Solve the red puzzle using one of the bb4-optimization strategies, such as a genetic search.
  * The genetic search solves the puzzle in 3.5 seconds on Core2 duo system (6 generations).
  * @param puzzle the controller of the puzzle to solve
  * @param strategy the optimization strategy to use. It needs to work with permutations of the pieces.
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
    val theSolution = optimize(initialGuess)
    solution = theSolution.pa.asInstanceOf[PieceParameterArray].getPieceList
    val moves = if (evaluateFitness(theSolution.pa) == 0) Some(solution.pieces) else None
    val elapsedTime = System.currentTimeMillis - startTime
    puzzle.finalRefresh(moves, Some(solution), numTries, elapsedTime)
    moves
  }

  /**
    * Runs the strategy, which is told to prefer the guided moves of [[proposeGuidedMove]]. This is what the Optimizer
    * facade does, except that it has no way to configure the strategy it makes.
    */
  private def optimize(initialGuess: ParameterArray): ParameterArrayWithFitness = {
    val optimization = strategy.getStrategy(this, MAX_FITS, MathUtil.RANDOM)
    preferGuidedMoves(optimization)
    optimization.setListener(this)
    val best = optimization.doOptimization(initialGuess, MAX_FITS)
    optimization.bestSoFar.filter(_.fitness < best.fitness).getOrElse(best)
  }

  /** The strategies that make moves, and so can use guided ones. The others just do not use them. */
  private def preferGuidedMoves(optimization: OptimizationStrategy): Unit = optimization match {
    case search: GeneticSearchStrategy =>
      search.setConfig(search.getConfig.copy(guidedMoveProbability = GeneticSearchSolver.GUIDED_MOVE_PROBABILITY))
    case annealing: SimulatedAnnealingStrategy =>
      annealing.setConfig(annealing.getConfig.copy(guidedMoveProbability = GeneticSearchSolver.GUIDED_MOVE_PROBABILITY))
    case global: GlobalSimulatedAnnealingStrategy =>
      val annealing = global.getConfig.annealingConfig
      global.setConfig(global.getConfig.copy(annealingConfig =
        annealing.copy(guidedMoveProbability = GeneticSearchSolver.GUIDED_MOVE_PROBABILITY)))
    case _ =>
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

  /** Swap a piece that fits its neighbor into a place where it does not fit, instead of a random swap. */
  override def proposeGuidedMove(current: ParameterArray, radius: Double, rnd: Random): Option[Move] =
    current.asInstanceOf[PieceParameterArray].proposeGuidedMove(rnd)

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
