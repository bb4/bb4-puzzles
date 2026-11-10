// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.solver

import com.barrybecker4.optimization.OptimizationListener
import com.barrybecker4.optimization.optimizee.Optimizee
import com.barrybecker4.optimization.parameter.{Move, ParameterArray, ParameterArrayWithFitness}
import com.barrybecker4.optimization.strategy.annealing.{GlobalSimulatedAnnealingStrategy, SimulatedAnnealingStrategy}
import com.barrybecker4.optimization.strategy.genetic.GeneticSearchStrategy
import com.barrybecker4.optimization.strategy.{OptimizationStrategy, OptimizationStrategyType}
import com.barrybecker4.puzzle.common.PuzzleController
import com.barrybecker4.puzzle.common.solver.PuzzleSolver
import com.barrybecker4.puzzle.slidingpuzzle.SlidingPuzzleController
import com.barrybecker4.puzzle.slidingpuzzle.model.{SlideMove, SliderBoard, TileParameterArray}
import com.barrybecker4.search.Refreshable

import scala.util.Random

object GeneticSearchSolver {
  /**
    * How often a move comes from [[com.barrybecker4.puzzle.slidingpuzzle.model.GuidedTileSwap]] rather than being
    * random. Start at 1.0 to prefer guided repairs; tune if reliability measurements change.
    */
  val GUIDED_MOVE_PROBABILITY = 1.0

  /**
    * Upper bound on Manhattan distance for an n by n board: each of the n²−1 tiles can be at most 2(n−1) steps
    * from home. Used as the optimizer's fitness range.
    */
  def maxDistance(edge: Int): Double = 2.0 * (edge - 1) * (edge * edge - 1)
}

/**
  * Solve the sliding puzzle using a bb4-optimization strategy on a permutation of the tiles.
  * When the metaheuristic reaches the goal layout (fitness 0), [[FastSliderSolver]] finds a legal sequence of slides
  * from the original board so the UI can animate real moves rather than abstract swaps.
  *
  * @param puzzle the controller of the puzzle to solve
  * @param strategy the optimization strategy to use; it needs to work with permutations of the tiles
  */
class GeneticSearchSolver(
    val puzzle: PuzzleController[SliderBoard, SlideMove],
    val strategy: OptimizationStrategyType
) extends PuzzleSolver[SlideMove] with Optimizee with OptimizationListener {

  /** @param useConcurrency true for concurrent genetic search, false for plain genetic search. */
  def this(puzzle: PuzzleController[SliderBoard, SlideMove], useConcurrency: Boolean) =
    this(puzzle,
      if (useConcurrency) OptimizationStrategyType.CONCURRENT_GENETIC_SEARCH
      else OptimizationStrategyType.GENETIC_SEARCH)

  private var numTries: Int = 0
  private var currentBestFitness: Double = Double.MaxValue
  private var currentBest: SliderBoard = puzzle.initialState

  def solve: Option[Seq[SlideMove]] = {
    val start = puzzle.initialState
    val fitnessRange = GeneticSearchSolver.maxDistance(start.size)
    currentBestFitness = fitnessRange
    currentBest = start
    numTries = 0
    val initialGuess = new TileParameterArray(start, new Random(1))
    val startTime = System.currentTimeMillis
    val best = optimize(initialGuess, fitnessRange)
    val foundGoal = evaluateFitness(best.pa) == 0
    val moves =
      if (foundGoal) extractSlidePath(start)
      else None
    val elapsedTime = System.currentTimeMillis - startTime
    puzzle.finalRefresh(moves, moves.map(_ => new SliderBoard(start.size)), numTries, elapsedTime)
    moves
  }

  /**
    * Runs the strategy, which is told to prefer the guided moves of [[proposeGuidedMove]]. This is what the Optimizer
    * facade does, except that it has no way to configure the strategy it makes.
    */
  private def optimize(initialGuess: ParameterArray, fitnessRange: Double): ParameterArrayWithFitness = {
    val optimization = strategy.getStrategy(this, fitnessRange, new Random(1))
    preferGuidedMoves(optimization)
    optimization.setListener(this)
    val best = optimization.doOptimization(initialGuess, fitnessRange)
    optimization.bestSoFar.filter(_.fitness < best.fitness).getOrElse(best)
  }

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

  /** Find legal slides from the original board without refreshing during IDA* (metaheuristic already showed progress). */
  private def extractSlidePath(start: SliderBoard): Option[Seq[SlideMove]] = {
    val quiet = new QuietRefreshable
    val controller = new SlidingPuzzleController(quiet) {
      override def initialState: SliderBoard = start
    }
    new FastSliderSolver(controller).solve
  }

  def getName = s"Sliding Puzzle solver using $strategy"

  def getOptimalFitness = 0

  def evaluateByComparison = false

  /** Manhattan distance to the goal; 0 when every tile is home. */
  def evaluateFitness(params: ParameterArray): Double = {
    val board = params.asInstanceOf[TileParameterArray].getBoard
    val fitness = if (board.isSolved) 0.0 else board.distanceToGoal.toDouble
    if (fitness < currentBestFitness) currentBestFitness = fitness
    fitness
  }

  override def proposeGuidedMove(current: ParameterArray, radius: Double, rnd: Random): Option[Move] =
    current.asInstanceOf[TileParameterArray].proposeGuidedMove(rnd)

  override def conflictedPositions(current: ParameterArray): Seq[Int] =
    current.asInstanceOf[TileParameterArray].conflictedPositions

  def compareFitness(params1: ParameterArray, params2: ParameterArray): Double =
    throw new UnsupportedOperationException("compareFitness not used since we evaluate in an absolute way.")

  def optimizerChanged(params: ParameterArrayWithFitness): Unit = {
    currentBest = params.pa.asInstanceOf[TileParameterArray].getBoard
    numTries += 1
    puzzle.refresh(currentBest, numTries)
  }

  private final class QuietRefreshable extends Refreshable[SliderBoard, SlideMove] {
    override def refresh(state: SliderBoard, numTries: Long): Unit = {}
    override def animateTransition(transition: SlideMove): SliderBoard = null
    override def finalRefresh(path: Option[Seq[SlideMove]], state: Option[SliderBoard],
                              numTries: Long, elapsedMillis: Long): Unit = {}
  }
}
