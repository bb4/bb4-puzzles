// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.benchmark

import com.barrybecker4.puzzle.redpuzzle.RedPuzzleController
import com.barrybecker4.puzzle.redpuzzle.model.{OrientedPiece, PieceList}
import com.barrybecker4.puzzle.redpuzzle.solver.{Algorithm as RedAlgorithm, FastBacktrackingSolver as RedFastSolver, GeneticSearchSolver as RedOptimizerSolver}
import com.barrybecker4.puzzle.slidingpuzzle.SlidingPuzzleController
import com.barrybecker4.puzzle.slidingpuzzle.model.{SlideMove, SliderBoard, Solvability, Tiles}
import com.barrybecker4.puzzle.slidingpuzzle.{Algorithm as SlidingAlgorithm}
import com.barrybecker4.puzzle.slidingpuzzle.solver.{FastSliderSolver, GeneticSearchSolver as SlidingOptimizerSolver}
import com.barrybecker4.puzzle.tantrix.TantrixController
import com.barrybecker4.puzzle.tantrix.model.{HexTiles, TantrixBoard, TilePlacement}
import com.barrybecker4.puzzle.common.solver.SequentialPuzzleSolver
import com.barrybecker4.puzzle.tantrix.solver.{Algorithm as TantrixAlgorithm, GeneticSearchSolver as TantrixOptimizerSolver, RestartingDepthFirstSolver, ShapeThenAssignSolver}
import com.barrybecker4.search.Refreshable

import scala.util.Random

/**
  * One algorithm on one puzzle variant.
  *
 * @param prepare builds a ready-to-run solve for the given repetition seed. Callers use `new Random(seed)` so
 *                repetition N of a cell starts from the same state in every version.
  */
final case class BenchmarkCase(puzzle: String, variant: String, algorithm: String, prepare: Long => PreparedRun)

/**
  * The puzzles and algorithms that are benchmarked: every solver that is driven by bb4-optimization, and for Tantrix
  * the exact depth first solvers too, so that the heuristics can be compared with a search that is guaranteed to find
  * a solution if it is given enough time. The exact solvers do not use bb4-optimization, so they give the same result
  * for every library version. Sliding Puzzle benchmarks the metaheuristics and the custom fast IDA* baseline.
  *
  * The algorithms are discovered from each puzzle's `Algorithm` enum rather than listed here, so when a new
  * bb4-optimization strategy is added to a puzzle's enum (for example in 2.1), it shows up in the next run with
  * no change to the benchmark.
  */
object BenchmarkCases {

  val TANTRIX = "Tantrix"
  val RED_PUZZLE = "Red Puzzle"
  val SLIDING_PUZZLE = "Sliding Puzzle"

  def all(config: BenchmarkConfig): List[BenchmarkCase] = {
    BenchmarkAppContext.ensureInitialized()
    (if (config.puzzles.contains("tantrix")) tantrixCases(config.tantrixSizes) else Nil) ++
      (if (config.puzzles.contains("redpuzzle")) redPuzzleCases() else Nil) ++
      (if (config.puzzles.contains("slidingpuzzle")) slidingPuzzleCases(config.slidingSizes) else Nil)
  }

  private val TantrixVariant = """(\d+) tiles""".r
  private val SlidingVariant = """(\d+)x\1""".r

  /** Looks up one case by name; used by the worker JVM that runs a single solve. */
  def find(puzzle: String, variant: String, algorithm: String): Option[BenchmarkCase] = {
    BenchmarkAppContext.ensureInitialized()
    val candidates = puzzle match {
      case TANTRIX =>
        variant match {
          case TantrixVariant(numTiles) => tantrixCases(List(numTiles.toInt))
          case _ => Nil
        }
      case RED_PUZZLE => redPuzzleCases()
      case SLIDING_PUZZLE =>
        variant match {
          case SlidingVariant(edge) => slidingPuzzleCases(List(edge.toInt))
          case _ => Nil
        }
      case _ => Nil
    }
    candidates.find(c => c.variant == variant && c.algorithm == algorithm)
  }

  /** Tantrix variants are the first n tiles in numeric order, so they are the same in every run and version. */
  def tantrixCases(sizes: List[Int]): List[BenchmarkCase] = {
    BenchmarkAppContext.ensureInitialized()
    val probe = new TantrixController(new ProbeRefreshable[TantrixBoard, TilePlacement])
    val algorithms = TantrixAlgorithm.values.toList.filter(a => isBenchmarkedTantrixSolver(a.createSolver(probe)))
    for {
      size <- sizes
      alg <- algorithms
    } yield BenchmarkCase(TANTRIX, s"$size tiles", alg.toString, seed => prepareTantrix(alg, size, seed))
  }

  private def isBenchmarkedTantrixSolver(solver: AnyRef): Boolean = solver match {
    case _: TantrixOptimizerSolver | _: RestartingDepthFirstSolver | _: ShapeThenAssignSolver | _: SequentialPuzzleSolver[?, ?] => true
    case _ => false
  }

  /** The red puzzle only has its standard 9 piece variant. */
  def redPuzzleCases(): List[BenchmarkCase] = {
    BenchmarkAppContext.ensureInitialized()
    val probe = new RedPuzzleController(new ProbeRefreshable[PieceList, OrientedPiece])
    val algorithms = RedAlgorithm.values.toList.filter(a => isBenchmarkedRedSolver(a.createSolver(probe)))
    algorithms.map(alg => BenchmarkCase(RED_PUZZLE, "9 pieces", alg.toString, seed => prepareRedPuzzle(alg, seed)))
  }

  private def isBenchmarkedRedSolver(solver: AnyRef): Boolean = solver match {
    case _: RedOptimizerSolver | _: RedFastSolver => true
    case _ => false
  }

  /** Sliding puzzle variants are n by n boards; each repetition shuffles from the same seed. */
  def slidingPuzzleCases(sizes: List[Int]): List[BenchmarkCase] = {
    BenchmarkAppContext.ensureInitialized()
    val probe = new SlidingPuzzleController(new ProbeRefreshable[SliderBoard, SlideMove])
    val algorithms = SlidingAlgorithm.values.toList.filter(a => isBenchmarkedSlidingSolver(a.createSolver(probe)))
    for {
      size <- sizes
      alg <- algorithms
    } yield BenchmarkCase(SLIDING_PUZZLE, s"${size}x$size", alg.toString, seed => prepareSlidingPuzzle(alg, size, seed))
  }

  private def isBenchmarkedSlidingSolver(solver: AnyRef): Boolean = solver match {
    case _: SlidingOptimizerSolver | _: FastSliderSolver => true
    case _ => false
  }

  private def prepareTantrix(alg: TantrixAlgorithm, numTiles: Int, seed: Long): PreparedRun = {
    val rnd = new Random(seed)
    val ui = new ProbeRefreshable[TantrixBoard, TilePlacement]
    val start = new TantrixBoard(new HexTiles().createOrderedList(numTiles))
    val controller = new FixedStartTantrixController(ui, start, rnd)
    val solver = alg.createSolver(controller)
    new PreparedRun(() => ui.cancel(), () => ui.numTries, () => solver.solve.isDefined)
  }

  private def prepareRedPuzzle(alg: RedAlgorithm, seed: Long): PreparedRun = {
    val rnd = new Random(seed)
    val ui = new ProbeRefreshable[PieceList, OrientedPiece]
    val controller = new RedPuzzleController(ui, rnd)
    val solver = alg.createSolver(controller)
    new PreparedRun(() => ui.cancel(), () => ui.numTries, () => solver.solve.isDefined)
  }

  private def prepareSlidingPuzzle(alg: SlidingAlgorithm, edge: Int, seed: Long): PreparedRun = {
    val ui = new ProbeRefreshable[SliderBoard, SlideMove]
    val start = solvableBoard(edge, new Random(seed))
    val controller = new FixedStartSlidingPuzzleController(ui, start)
    val solver = alg.createSolver(controller)
    new PreparedRun(() => ui.cancel(), () => ui.numTries, () => solver.solve.isDefined)
  }

  private def solvableBoard(edge: Int, rnd: Random): SliderBoard = {
    val tiles = Iterator.continually(rnd.shuffle((0 until edge * edge).toList))
      .find(Solvability.isSolvable(edge, _)).get
    SliderBoard(Tiles(tiles.map(_.toByte).toIndexedSeq))
  }

  /** A controller that always starts from the same board so that runs are comparable. */
  private final class FixedStartTantrixController(
    refreshable: Refreshable[TantrixBoard, TilePlacement],
    private val startBoard: TantrixBoard,
    heuristicRnd: Random
  ) extends TantrixController(refreshable, heuristicRnd) {
    override def initialState: TantrixBoard = startBoard
  }

  private final class FixedStartSlidingPuzzleController(
    refreshable: Refreshable[SliderBoard, SlideMove],
    private val startBoard: SliderBoard
  ) extends SlidingPuzzleController(refreshable) {
    override def initialState: SliderBoard = startBoard
  }
}
