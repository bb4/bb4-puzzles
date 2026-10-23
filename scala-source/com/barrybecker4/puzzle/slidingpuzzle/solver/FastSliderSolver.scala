// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.solver

import com.barrybecker4.common.geometry.ByteLocation
import com.barrybecker4.puzzle.common.PuzzleController
import com.barrybecker4.puzzle.common.solver.PuzzleSolver
import com.barrybecker4.puzzle.slidingpuzzle.model.{SlideMove, SliderBoard, Solvability}
import com.barrybecker4.puzzle.slidingpuzzle.solver.fastsearch.*
import com.barrybecker4.puzzle.slidingpuzzle.solver.reduction.ReductionSolver

object FastSliderSolver {
  /** One attempt at a solution: the search gives up after looking at `maxPositions` positions. */
  case class SearchStage(weightPercent: Int, maxPositions: Long)

  /** Searches for as long as it takes to be sure of a shortest solution. */
  val SHORTEST_ONLY: Seq[SearchStage] = Seq(SearchStage(IdaStarSearch.OPTIMAL_WEIGHT_PERCENT, Long.MaxValue))

  /** A shortest solution if one is found quickly, otherwise a longer one that is found sooner. */
  val SHORTEST_IF_QUICK: Seq[SearchStage] =
    Seq(SearchStage(IdaStarSearch.OPTIMAL_WEIGHT_PERCENT, 5_000_000L), SearchStage(200, 2_000_000L))
}

/**
  * Solves a sliding puzzle with an iterative deepening A* that is written to be fast and to use almost no memory.
  * The generic solvers make a new immutable board for every position and remember the ones they have seen, which is
  * what makes them slow and run out of memory on the 15 and 24 puzzles. This one changes a single array in place
  * (see [[fastsearch.SliderState]]), and uses the linear conflict estimate which is much sharper than the Manhattan
  * distance alone. It does not use bb4-optimization.
  *
  * A shortest solution to a 5 by 5 puzzle can take minutes or longer to prove, so a list of stages can be given.
  * Each stage is tried in turn, and gives up after looking at its number of positions. A stage with a weight above
  * 100 percent finishes sooner, but its solution may be longer. If every stage gives up, and `reduceIfGivenUp` is set,
  * the puzzle is solved tile by tile (see [[reduction.ReductionSolver]]), which always works and is quick, but gives
  * a much longer solution.
  * @param stages the attempts to make, in order
  * @param reduceIfGivenUp true to finish with the tile by tile solver when every stage has given up.
  * @author Barry Becker
  */
class FastSliderSolver(
  puzzle: PuzzleController[SliderBoard, SlideMove],
  stages: Seq[FastSliderSolver.SearchStage] = FastSliderSolver.SHORTEST_ONLY,
  reduceIfGivenUp: Boolean = false
) extends PuzzleSolver[SlideMove] {

  @throws[InterruptedException]
  def solve: Option[Seq[SlideMove]] = {
    val startTime = System.currentTimeMillis
    val start = puzzle.initialState
    val (moves, numTries) = searchFrom(start)
    puzzle.finalRefresh(moves, moves.map(_ => goalBoard(start)), numTries, System.currentTimeMillis - startTime)
    moves
  }

  private def searchFrom(start: SliderBoard): (Option[Seq[SlideMove]], Long) = {
    val geometry = new GridGeometry(start.size)
    val tiles = Seq.tabulate(geometry.numCells)(cell => start.tiles.tiles(cell).toInt)
    if (!Solvability.isSolvable(start.size, tiles)) (None, 0L)
    else {
      val (cells, numTries) = findSlides(start, geometry, tiles)
      (cells.map(toMoves(_, tiles.indexOf(0), geometry)), numTries)
    }
  }

  /** Tries each stage until one finds a solution, and then the tile by tile solver if allowed. */
  private def findSlides(start: SliderBoard, geometry: GridGeometry, tiles: Seq[Int]): (Option[Array[Int]], Long) = {
    var numTries = 0L
    var cells: Option[Array[Int]] = None
    for (stage <- stages if cells.isEmpty) {
      val search = new IdaStarSearch(new SliderState(geometry, tiles), geometry,
        visited => puzzle.refresh(start, numTries + visited), stage.weightPercent, stage.maxPositions)
      cells = search.solve()
      numTries += search.positionsVisited
    }
    if (cells.isEmpty && reduceIfGivenUp) cells = Some(new ReductionSolver(geometry).solve(tiles))
    (cells, numTries)
  }

  /** The tile in each cell is slid into the blank, which then takes the cell the tile left. */
  private def toMoves(slidCells: Array[Int], startBlank: Int, geometry: GridGeometry): Seq[SlideMove] = {
    val blanks = startBlank +: slidCells.dropRight(1).toSeq
    slidCells.toSeq.zip(blanks).map((cell, blank) => SlideMove(location(cell, geometry), location(blank, geometry)))
  }

  private def location(cell: Int, geometry: GridGeometry) = ByteLocation(geometry.row(cell).toByte, geometry.col(cell).toByte)

  private def goalBoard(start: SliderBoard): SliderBoard = new SliderBoard(start.size)
}
