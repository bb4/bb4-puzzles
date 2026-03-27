// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.solver

import com.barrybecker4.math.MathUtil
import com.barrybecker4.puzzle.common.PuzzleController
import com.barrybecker4.puzzle.redpuzzle.model.{OrientedPiece, PieceList, PieceLists}

/**
  * Works really well in spite of being brute force.
  * Solves the puzzle in 10 seconds on Core2Duo sequentially.
  * @author Barry Becker
  */
class BruteForceSolver(
  override val puzzle: PuzzleController[PieceList, OrientedPiece],
  override val pieces: PieceList = PieceLists.getInitialPuzzlePieces(MathUtil.RANDOM)
) extends RedPuzzleSolver(puzzle, pieces) {

  private val Rotations = 4

  puzzle.refresh(pieces, 0)

  /** @return true if a solution is found. */
  def solve: Option[Seq[OrientedPiece]] = {
    val startTime = System.currentTimeMillis
    val moves =
      if solvePuzzle(pieces, 0).isEmpty then Some(solution.pieces)
      else None
    val elapsedTime = System.currentTimeMillis - startTime
    puzzle.finalRefresh(moves, Some(solution), numTries, elapsedTime)
    moves
  }

  /** Implements the main recursive algorithm for solving the red puzzle.
    * @param thePieces the pieces that have yet to be fitted.
    * @param insertIndex index used when backtracking to restore an unplaced piece to its prior slot.
    * @return remaining unplaced pieces; empty when solved or when backtracking has restored state.
    */
  private def solvePuzzle(thePieces: PieceList, insertIndex: Int): PieceList = {
    if thePieces.size == 0 then return thePieces

    var solved = false
    var pool = thePieces
    var k = 0
    while !solved && k < pool.size do
      val result = tryRotationsForPiece(k, pool, insertIndex)
      solved = result._1
      pool = result._2
      if !solved then k += 1
    end while

    if !solved && solution.size > 0 then pool = backtrackLastPlaced(insertIndex, pool)
    pool
  }

  /** Try each rotation of piece at index `k`; recurse when a placement fits. */
  private def tryRotationsForPiece(k: Int, pool: PieceList, insertIndex: Int): (Boolean, PieceList) = {
    var p = pool.get(k)
    var r = 0
    var solved = false
    var curPool = pool
    while !solved && r < Rotations do
      numTries += 1
      if solution.fits(p) then
        solution = solution.add(p)
        curPool = curPool.remove(p.piece)
        puzzle.refresh(solution, numTries)
        curPool = solvePuzzle(curPool, k)
        solved = curPool.isEmpty
      if !solved then p = p.rotate()
      r += 1
    end while
    (solved, curPool)
  }

  private def backtrackLastPlaced(insertIndex: Int, pool: PieceList): PieceList = {
    val piece = solution.getLast
    solution = solution.removeLast()
    pool.add(insertIndex, piece)
  }
}
