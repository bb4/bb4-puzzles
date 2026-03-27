// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.solver

import com.barrybecker4.math.MathUtil
import com.barrybecker4.puzzle.common.PuzzleController
import com.barrybecker4.puzzle.common.solver.PuzzleSolver
import com.barrybecker4.puzzle.redpuzzle.model.{OrientedPiece, PieceList, PieceLists}


/**
  * Abstract base class for puzzle solver strategies (see strategy pattern).
  * Subclasses do the hard work of actually solving the puzzle.
  * @param puzzle the puzzle to solve.
  * @param pieces initial pool of shuffled pieces to place (defaults to a random standard set).
  * @author Barry Becker
  */
abstract class RedPuzzleSolver(
  val puzzle: PuzzleController[PieceList, OrientedPiece],
  val pieces: PieceList = PieceLists.getInitialPuzzlePieces(MathUtil.RANDOM)
) extends PuzzleSolver[OrientedPiece] {

  /** the pieces we have correctly fitted so far. */
  var solution: PieceList = PieceList(List.empty[OrientedPiece], pieces.numTotal)

  /** some measure of the number of iterations the solver needs to solve the puzzle. */
  var numTries: Int = 0

  /** Solve the puzzle
    * @return true if a solution is found.
    */
  def solve: Option[Seq[OrientedPiece]]
}
