// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.solver

import com.barrybecker4.puzzle.common.PuzzleController
import com.barrybecker4.puzzle.redpuzzle.model.Piece
import com.barrybecker4.puzzle.redpuzzle.model.PieceList
import scala.collection.Seq

/**
  * Works really well in spite of being brute force.
  * Solves the puzzle in 10 seconds on Core2Duo sequentially.
  *
  * @author Barry Becker
  */
class BruteForceSolver(override val puzzle: PuzzleController[PieceList, Piece]) extends RedPuzzleSolver(puzzle) {

  puzzle.refresh(pieces, 0)

  /** @return true if a solution is found. */
  def solve: Option[Seq[Piece]] = {
    var moves: Option[List[Piece]] = Option.empty
    val startTime = System.currentTimeMillis
    if (solvePuzzle(pieces, 0).size == 0)
      moves = Some(solution.pieces)
    val elapsedTime = System.currentTimeMillis - startTime
    puzzle.finalRefresh(moves, Option.apply(solution), numTries, elapsedTime)
    moves
  }

  /**
    * Solves the puzzle. This implements the main recursive algorithm for solving the red puzzle.
    *
    * @param thePieces the pieces that have yet to be fitted.
    * @param i      index of last placed piece. If we have to backtrack, we put it back where we got it.
    * @return true if successfully solved, false if no solution.
    */
  private def solvePuzzle(thePieces: PieceList, i: Int): PieceList = {
    var solved = false
    var pieces = thePieces

    // base case of the recursion. If no pieces left to place, the puzzle has been solved.
    if (pieces.size == 0) return pieces

    var k = 0
    while (!solved && k < pieces.size) {
      var p = pieces.get(k)
      //println("now trying " + p + " of " + pieces.size)
      var r = 0
      // try the 4 rotations
      while (!solved && r < 4) {
        numTries += 1
        if (solution.fits(p)) {
          solution = solution.add(p)
          val before = pieces.size
          pieces = pieces.remove(p)
          println("before = " + pieces)
          if (before - pieces.size > 1)
            println("ERROR: nump before = " + before + " after removing "+ p+ "  pieces=" + pieces)
          puzzle.refresh(solution, numTries)
          // call solvePuzzle with a simpler case (one less piece to solve)
          pieces = solvePuzzle(pieces, k)
          solved = pieces.size == 0
        }
        if (!solved) p = p.rotate
        r += 1
      }
      k += 1
    }
    if (!solved && solution.size > 0) {
      // backtrack.
      val p = solution.getLast
      solution = solution.removeLast()
      // put it back where we took it from, so the list of unplaced pieces is still in order.
      pieces = pieces.add(i, p)
      println("put " + p + " back on pieces = " + pieces.toString)
    }
    // if we get here and pieces is empty, we did not find a solution.
    pieces
  }
}
