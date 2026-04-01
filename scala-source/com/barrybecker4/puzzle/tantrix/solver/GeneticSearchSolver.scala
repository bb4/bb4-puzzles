// Copyright by Barry G. Becker, 2017 - 2023. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver

import com.barrybecker4.math.MathUtil
import com.barrybecker4.optimization.optimizee.Optimizee
import com.barrybecker4.optimization.parameter.{ParameterArray, ParameterArrayWithFitness}
import com.barrybecker4.optimization.strategy.{
  GlobalHillClimbingStrategy,
  OptimizationStrategy,
  OptimizationStrategyType,
  SimulatedAnnealingConfig,
  SimulatedAnnealingStepMode,
  SimulatedAnnealingStrategy
}
import com.barrybecker4.optimization.{OptimizationListener, Optimizer}
import com.barrybecker4.puzzle.common.PuzzleController
import com.barrybecker4.puzzle.tantrix.model.{TantrixBoard, TilePlacement}
import com.barrybecker4.puzzle.tantrix.solver.path.{PathEvaluator, TantrixPath}
import com.barrybecker4.puzzle.tantrix.solver.path.PathEvaluator.FITNESS_RANGE
import scala.util.Random


/**
  * Solve the Tantrix puzzle using a bb4-optimization strategy (genetic, simulated annealing, hill climbing, etc.).
  */
class GeneticSearchSolver(
    var controller: PuzzleController[TantrixBoard, TilePlacement],
    val optimizationStrategy: OptimizationStrategyType
) extends TantrixSolver(controller.initialState) with Optimizee with OptimizationListener {
  private val evaluator = new PathEvaluator
  private var numTries: Int = 0
  private var currentBestFitness = FITNESS_RANGE

  private def tantrixPath(pa: ParameterArray): TantrixPath =
    pa match
      case p: TantrixPath => p
      case _ => throw new IllegalStateException("Tantrix genetic search expects TantrixPath parameters")

  /** SA / global hill climb need more exploration than default bb4-optimization presets for this puzzle. */
  private def useTantrixTunedMetaheuristic: Boolean =
    optimizationStrategy == OptimizationStrategyType.SIMULATED_ANNEALING ||
      optimizationStrategy == OptimizationStrategyType.GLOBAL_HILL_CLIMBING

  /**
    * Hotter SA, longer inner chains, slower cooling, adaptive step scaling; larger global sample + multistart for GHC.
    */
  private def createTantrixTunedStrategy(rnd: Random): OptimizationStrategy =
    val n = math.max(1, board.numTiles)
    optimizationStrategy match
      case OptimizationStrategyType.SIMULATED_ANNEALING =>
        val s = new SimulatedAnnealingStrategy(this, rnd)
        s.setMaxTemperature(FITNESS_RANGE * 0.55)
        s.setConfig(
          SimulatedAnnealingConfig(
            innerIterationsPerDimension = math.max(30, 2 * n),
            numTempIterations = 42,
            tempDropFactor = 0.88,
            defaultTempMax = 1000.0,
            radiusMultiplier = 10.0
          )
        )
        s.setStepMode(
          SimulatedAnnealingStepMode.AdaptiveAcceptance(
            targetRate = 0.42,
            windowProposals = math.max(64, 4 * n),
            initialScale = 1.0
          )
        )
        s
      case OptimizationStrategyType.GLOBAL_HILL_CLIMBING =>
        val s = new GlobalHillClimbingStrategy(this)
        s.setMultistartCount(math.min(28, math.max(12, n)))
        s.setGlobalSampleBudget(math.max(4000, 200 * n))
        s
      case other =>
        throw new IllegalStateException(s"Not a tuned metaheuristic: $other")

  /** @return list of moves to a solution. */
  def solve: Option[Seq[TilePlacement]] = {
    val startTime = System.currentTimeMillis
    val optimizer = new Optimizer(this)
    optimizer.setListener(this)

    try {
      val foundSolution =
        if useTantrixTunedMetaheuristic then
          val rnd = new Random()
          val guess = new TantrixPath(board, rnd)
          assert(guess.size > 0, "The random path should have some tiles!")
          val strategy = createTantrixTunedStrategy(rnd)
          strategy.setListener(this)
          strategy.doOptimization(guess, FITNESS_RANGE)
        else
          val guess = new TantrixPath(board, new Random(1))
          assert(guess.size > 0, "The random path should have some tiles!")
          optimizer.doOptimization(optimizationStrategy, guess, FITNESS_RANGE, MathUtil.RANDOM)

      val bestPath = tantrixPath(foundSolution.pa)
      solution = new TantrixBoard(bestPath.tiles, board.primaryColor)
      val tilePlacements =
        if (evaluateFitness(foundSolution.pa) <= 0) Some(bestPath.tiles)
        else None
      val elapsedTime = System.currentTimeMillis - startTime
      controller.finalRefresh(tilePlacements, Option.apply(solution), numTries, elapsedTime)
      tilePlacements
    } catch {
      case _: InterruptedException =>
        Thread.currentThread().interrupt()
        None
    }
  }

  def getName = s"Tantrix metaheuristic solver (${optimizationStrategy.toString})"

  /** terminate the solver if we find a solution with this fitness. */
  def getOptimalFitness = 0 /* SOLVED_THRESH*/

  def evaluateByComparison = false

  /** Return 0 or less if a perfect solution has been found.
    * @param params parameters
    * @return fitness value. Lower is better.
    */
  def evaluateFitness(params: ParameterArray): Double = {
    val fitness = evaluator.evaluateFitness(tantrixPath(params))
    if (fitness < currentBestFitness) currentBestFitness = fitness
    fitness
  }

  def compareFitness(params1: ParameterArray, params2: ParameterArray): Double = {
    assert(assertion = false, "compareFitness not used since we evaluate in an absolute way.")
    0
  }

  /** Called when the optimizer has made some progress optimizing.
    * Shows the current status.
    * @param params optimized array of parameters representing tiles
    */
  def optimizerChanged(params: ParameterArrayWithFitness): Unit = {
    val path = tantrixPath(params.pa)
    solution = new TantrixBoard(path.tiles, path.primaryPathColor)
    controller.refresh(solution, {
      numTries += 1; numTries - 1
    })
  }
}
