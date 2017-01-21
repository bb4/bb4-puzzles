/*
 * // Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
 */
package com.barrybecker4.puzzle.redpuzzle

import com.barrybecker4.common.search.Refreshable
import com.barrybecker4.puzzle.common.ui.AbstractPuzzleController
import com.barrybecker4.puzzle.redpuzzle.model.{Piece, PieceList, PieceLists}
import com.barrybecker4.puzzle.redpuzzle.solver.BRUTE_FORCE_ORIGINAL
import com.barrybecker4.puzzle.redpuzzle.model.PieceList.DEFAULT_NUM_PIECES

import scala.collection.Seq

/**
  * The controller allows the solver to do its thing by providing the PuzzleController api.
  * Originally I had implemented solvers without trying to do concurrency, and those less generic
  * forms still exist, but do not require the PuzzleController api.
  *
  * The generic solvers (sequential and concurrent) expect the PieceList to represent the state of a board,
  * and the Piece to represent a move. The way a move is applied is simply to add the piece to the
  * end of the current list.
  *
  * @author Barry Becker
  */
class RedPuzzleController(val ui: Refreshable[PieceList, Piece])
  extends AbstractPuzzleController[PieceList, Piece](ui) {

  algorithm_ = BRUTE_FORCE_ORIGINAL
  final private val SHUFFLED_PIECES = PieceLists.getInitialPuzzlePieces

  def initialState: PieceList = new PieceList  // empty piece list

  /** @return true if we have 9 pieces that fit */
  def isGoal(position: PieceList): Boolean = position.size == DEFAULT_NUM_PIECES

  /**
    * The simplest estimate of the cost to reach the goal is 9 - number of pieces placed so far.
    *
    * @return estimate of the cost to reach the goal of all 9 pieces successfully placed
    */
  override def distanceFromGoal(position: PieceList): Int = DEFAULT_NUM_PIECES - position.size

  /**
    * For each piece that we have not tried yet, see if it fits.
    * If it does, add that to the set of legal next moves.
    *
    * @param position position to look from.
    * @return list of legal moves that can be made from current position.
    */
  def legalTransitions(position: PieceList): Seq[Piece] = {
    var moves = List[Piece]()

    for (i <- 0 until DEFAULT_NUM_PIECES) {
      var p = SHUFFLED_PIECES.get(i)
      if (!position.contains(p)) {
        var r = 0
        // see if any of the rotations fit.
        while (!position.fits(p) && r < 4) {
          p = p.rotate
          r += 1
        }
        if (r < 4) moves :+= p
      }
    }
    moves
  }

  def transition(position: PieceList, move: Piece): PieceList = {
    // To make a move, simple add the piece to the end of our list
    assert(position.fits(move), move + " does not fit in  " + position)
    position.add(move)
  }
}
