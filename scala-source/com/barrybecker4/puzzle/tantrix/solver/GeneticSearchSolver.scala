// Copyright by Barry G. Becker, 2017 - 2023. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver

import com.barrybecker4.math.MathUtil
import com.barrybecker4.optimization.optimizee.Optimizee
import com.barrybecker4.optimization.parameter.{Move, ParameterArray, ParameterArrayWithFitness}
import com.barrybecker4.optimization.strategy.annealing.{
  SimulatedAnnealingConfig,
  SimulatedAnnealingStepMode,
  SimulatedAnnealingStrategy
}
import com.barrybecker4.optimization.strategy.hillclimbing.{GlobalHillClimbingConfig, GlobalHillClimbingStrategy, SeedSelection}
import com.barrybecker4.optimization.strategy.{OptimizationStrategy, OptimizationStrategyType}
import com.barrybecker4.optimization.{OptimizationListener, Optimizer}
import com.barrybecker4.puzzle.common.PuzzleController
import com.barrybecker4.puzzle.tantrix.model.{TantrixBoard, TilePlacement}
import com.barrybecker4.puzzle.tantrix.solver.path.{PathEvaluator, TantrixPath}
import com.barrybecker4.puzzle.tantrix.solver.path.PathEvaluator.FITNESS_RANGE
import scala.util.Random


object GeneticSearchSolver {
  /**
    * How often the tuned simulated annealing makes a rearrangement next to a tile that does not fit (see
    * [[GuidedPivot]]) rather than a random one. It is 0 because the measurements are mixed: with 0.5, 40 runs on 9
    * tiles solved 40 instead of 37, and on 12 tiles 40 instead of 35, but the median time of a solved run went from
    * 42 to 104 ms and from 70 to 616 ms. Genetic search got no better. Change it if reliability matters more than time.
    */
  val GUIDED_MOVE_PROBABILITY: Double = 0.0
}

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
            radiusMultiplier = 10.0,
            stepMode = SimulatedAnnealingStepMode.AdaptiveAcceptance(
              targetRate = 0.42,
              windowProposals = math.max(64, 4 * n),
              initialScale = 1.0
            ),
            guidedMoveProbability = GeneticSearchSolver.GUIDED_MOVE_PROBABILITY
          )
        )
        s
      case OptimizationStrategyType.GLOBAL_HILL_CLIMBING =>
        val s = new GlobalHillClimbingStrategy(this)
        s.setConfig(
          GlobalHillClimbingConfig(
            multistartCount = math.min(28, math.max(12, n)),
            globalSampleBudget = math.max(4000, 200 * n),
            // bb4-optimization 2.0.0 climbed from the best-fitness samples; 2.1 defaults to diverse seeds.
            seedSelection = SeedSelection.TopFitness
          )
        )
        s
      case other =>
        throw new IllegalStateException(s"Not a tuned metaheuristic: $other")

  /** @return list of moves to a solution. */
  def solve: Option[Seq[TilePlacement]] = {
    val startTime = System.currentTimeMillis
    try {
      val bestPath = tantrixPath(findBestParams().pa)
      solution = new TantrixBoard(bestPath.tiles, board.primaryColor)
      val tilePlacements = Option.when(evaluateFitness(bestPath) <= 0)(bestPath.tiles)
      controller.finalRefresh(tilePlacements, Some(solution), numTries, System.currentTimeMillis - startTime)
      tilePlacements
    } catch {
      case _: InterruptedException =>
        Thread.currentThread().interrupt()
        None
    }
  }

  /** @return the best parameters found by the tuned strategy, or by the standard optimizer for the other strategies. */
  private def findBestParams() =
    if useTantrixTunedMetaheuristic then runTunedStrategy() else runStandardOptimizer()

  private def runTunedStrategy() = {
    val rnd = new Random()
    val guess = createInitialGuess(rnd)
    val strategy = createTantrixTunedStrategy(rnd)
    strategy.setListener(this)
    strategy.doOptimization(guess, FITNESS_RANGE)
  }

  private def runStandardOptimizer() = {
    val optimizer = new Optimizer(this)
    optimizer.setListener(this)
    optimizer.doOptimization(optimizationStrategy, createInitialGuess(new Random(1)), FITNESS_RANGE, MathUtil.RANDOM).best
  }

  /** @return a random path through all the tiles to start the search from. */
  private def createInitialGuess(rnd: Random): TantrixPath = {
    val guess = new TantrixPath(board, rnd)
    assert(guess.size > 0, "The random path should have some tiles!")
    guess
  }

  def getName = s"Tantrix metaheuristic solver (${optimizationStrategy.toString})"

  /** terminate the solver if we find a solution with this fitness. */
  def getOptimalFitness = 0

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

  /** Rearrange the path next to a tile that does not fit, instead of at a random place. */
  override def proposeGuidedMove(current: ParameterArray, radius: Double, rnd: Random): Option[Move] =
    tantrixPath(current).proposeGuidedMove(rnd)

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
    controller.refresh(solution, nextTryNumber())
  }

  /** @return the number of the try that is starting (the first is 0) */
  private def nextTryNumber(): Int = {
    numTries += 1
    numTries - 1
  }
}
