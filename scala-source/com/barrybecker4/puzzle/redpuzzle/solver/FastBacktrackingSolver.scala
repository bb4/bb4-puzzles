// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.solver

import com.barrybecker4.math.MathUtil
import com.barrybecker4.puzzle.common.PuzzleController
import com.barrybecker4.puzzle.redpuzzle.model.{OrientedPiece, PieceList, PieceLists}
import com.barrybecker4.puzzle.redpuzzle.solver.backtracking.{PieceEdgeTable, PlacementSearch}

/**
  * Solves the red puzzle with a depth first search that is written to be fast rather than general.
  * It always finds a solution if there is one, and takes well under a millisecond for the standard puzzle.
  *
  * The generic and handcrafted exact solvers copy immutable lists for every placement. This one turns the nubs into
  * integers up front (see [[backtracking.PlacementSearch]]) and searches without allocating anything.
  * It does not use bb4-optimization.
  */
class FastBacktrackingSolver(
  override val puzzle: PuzzleController[PieceList, OrientedPiece],
  override val pieces: PieceList = PieceLists.getInitialPuzzlePieces(MathUtil.RANDOM)
) extends RedPuzzleSolver(puzzle, pieces) {

  private val table = new PieceEdgeTable(pieces.pieces)

  def solve: Option[Seq[OrientedPiece]] = {
    val startTime = System.currentTimeMillis
    puzzle.refresh(pieces, 0)
    val search = new PlacementSearch(table, pieces.edgeLength, reportProgress)
    val slots = search.solve()
    numTries = search.triesSoFar.toInt
    val moves = slots.map(toOrientedPieces)
    solution = PieceList(moves.map(_.toList).getOrElse(List.empty), pieces.numTotal)
    puzzle.finalRefresh(moves, Some(solution), numTries, System.currentTimeMillis - startTime)
    moves
  }

  private def reportProgress(placedSlots: Array[Int], tries: Long): Unit =
    puzzle.refresh(PieceList(toOrientedPieces(placedSlots).toList, pieces.numTotal), tries)

  private def toOrientedPieces(slots: Array[Int]): Seq[OrientedPiece] = slots.toSeq.map(table.orientedPiece)
}
