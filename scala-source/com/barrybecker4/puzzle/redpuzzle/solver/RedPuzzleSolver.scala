// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.solver

import com.barrybecker4.puzzle.common.PuzzleController
import com.barrybecker4.puzzle.common.solver.PuzzleSolver
import com.barrybecker4.puzzle.redpuzzle.model.{OrientedPiece, Piece, PieceList, PieceLists}

import scala.collection.Seq

/**
  * Abstract base class for puzzle solver strategies (see strategy pattern).
  * Subclasses do the hard work of actually solving the puzzle.
  *
  * @param puzzle the puzzle to solve.
  * @author Barry Becker
  */
abstract class RedPuzzleSolver(val puzzle: PuzzleController[PieceList, OrientedPiece]) extends PuzzleSolver[OrientedPiece] {

  /** the unsorted pieces that we draw from and place in the solvedPieces list. */
  var pieces: PieceList = PieceLists.getInitialPuzzlePieces

  /** the pieces we have correctly fitted so far. */
  var solution: PieceList = new PieceList

  /** some measure of the number of iterations the solver needs to solve the puzzle. */
  var numTries: Int = 0

  /**
    * Solve the puzzle
    * @return true if a solution is found.
    */
  def solve: Option[Seq[OrientedPiece]]
}
