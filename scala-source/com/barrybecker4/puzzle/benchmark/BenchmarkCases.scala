// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.benchmark

import com.barrybecker4.math.MathUtil
import com.barrybecker4.puzzle.redpuzzle.RedPuzzleController
import com.barrybecker4.puzzle.redpuzzle.model.{OrientedPiece, PieceList}
import com.barrybecker4.puzzle.redpuzzle.solver.{Algorithm as RedAlgorithm, GeneticSearchSolver as RedOptimizerSolver}
import com.barrybecker4.puzzle.tantrix.TantrixController
import com.barrybecker4.puzzle.tantrix.model.{HexTiles, TantrixBoard, TilePlacement}
import com.barrybecker4.puzzle.tantrix.solver.{Algorithm as TantrixAlgorithm, GeneticSearchSolver as TantrixOptimizerSolver}
import com.barrybecker4.search.Refreshable

/**
  * One algorithm on one puzzle variant.
  *
  * @param prepare builds a ready-to-run solve for the given repetition seed. The seed is applied to the shared
  *                random number generator, so repetition N of a cell starts from the same state in every version.
  */
final case class BenchmarkCase(puzzle: String, variant: String, algorithm: String, prepare: Long => PreparedRun)

/**
  * The puzzles and algorithms that are benchmarked: every solver that is driven by bb4-optimization.
  *
  * The algorithms are discovered from each puzzle's `Algorithm` enum rather than listed here, so when a new
  * bb4-optimization strategy is added to a puzzle's enum (for example in 2.1), it shows up in the next run with
  * no change to the benchmark.
  */
object BenchmarkCases {

  val TANTRIX = "Tantrix"
  val RED_PUZZLE = "Red Puzzle"

  def all(config: BenchmarkConfig): List[BenchmarkCase] =
    (if (config.puzzles.contains("tantrix")) tantrixCases(config.tantrixSizes) else Nil) ++
      (if (config.puzzles.contains("redpuzzle")) redPuzzleCases() else Nil)

  private val TantrixVariant = """(\d+) tiles""".r

  /** Looks up one case by name; used by the worker JVM that runs a single solve. */
  def find(puzzle: String, variant: String, algorithm: String): Option[BenchmarkCase] = {
    val candidates = puzzle match {
      case TANTRIX =>
        variant match {
          case TantrixVariant(numTiles) => tantrixCases(List(numTiles.toInt))
          case _ => Nil
        }
      case RED_PUZZLE => redPuzzleCases()
      case _ => Nil
    }
    candidates.find(c => c.variant == variant && c.algorithm == algorithm)
  }

  /** Tantrix variants are the first n tiles in numeric order, so they are the same in every run and version. */
  def tantrixCases(sizes: List[Int]): List[BenchmarkCase] = {
    val probe = new TantrixController(new ProbeRefreshable[TantrixBoard, TilePlacement])
    val algorithms = TantrixAlgorithm.values.toList.filter(a => a.createSolver(probe).isInstanceOf[TantrixOptimizerSolver])
    for {
      size <- sizes
      alg <- algorithms
    } yield BenchmarkCase(TANTRIX, s"$size tiles", alg.toString, seed => prepareTantrix(alg, size, seed))
  }

  /** The red puzzle only has its standard 9 piece variant. */
  def redPuzzleCases(): List[BenchmarkCase] = {
    val probe = new RedPuzzleController(new ProbeRefreshable[PieceList, OrientedPiece])
    val algorithms = RedAlgorithm.values.toList.filter(a => a.createSolver(probe).isInstanceOf[RedOptimizerSolver])
    algorithms.map(alg => BenchmarkCase(RED_PUZZLE, "9 pieces", alg.toString, seed => prepareRedPuzzle(alg, seed)))
  }

  private def prepareTantrix(alg: TantrixAlgorithm, numTiles: Int, seed: Long): PreparedRun = {
    MathUtil.RANDOM.setSeed(seed)
    val ui = new ProbeRefreshable[TantrixBoard, TilePlacement]
    val start = new TantrixBoard(new HexTiles().createOrderedList(numTiles))
    val controller = new FixedStartTantrixController(ui, start)
    val solver = alg.createSolver(controller)
    new PreparedRun(() => ui.cancel(), () => ui.numTries, () => solver.solve.isDefined)
  }

  private def prepareRedPuzzle(alg: RedAlgorithm, seed: Long): PreparedRun = {
    MathUtil.RANDOM.setSeed(seed)
    val ui = new ProbeRefreshable[PieceList, OrientedPiece]
    val controller = new RedPuzzleController(ui)
    val solver = alg.createSolver(controller)
    new PreparedRun(() => ui.cancel(), () => ui.numTries, () => solver.solve.isDefined)
  }

  /** A controller that always starts from the same board so that runs are comparable. */
  private final class FixedStartTantrixController(
    refreshable: Refreshable[TantrixBoard, TilePlacement],
    private val startBoard: TantrixBoard
  ) extends TantrixController(refreshable) {
    override def initialState: TantrixBoard = startBoard
  }
}
