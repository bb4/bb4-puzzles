// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.model

import com.barrybecker4.common.math.MathUtil
import scala.util.Random


/**
  * Some standard puzzle configurations to try.
  * @author Barry Becker
  */
object PieceLists {

  /** this defines the puzzle pieces for the standard 9x9 puzzle (not sorted). */
  val RED_INITIAL_PIECES_9 = Array(
    Piece(Nub.OUTY_SPADE, Nub.OUTY_DIAMOND, Nub.INNY_HEART, Nub.INNY_DIAMOND, 1), // 0
    Piece(Nub.OUTY_CLUB, Nub.OUTY_HEART, Nub.INNY_DIAMOND, Nub.INNY_CLUB, 2), // 1
    Piece(Nub.OUTY_HEART, Nub.OUTY_SPADE, Nub.INNY_SPADE, Nub.INNY_CLUB, 3), // 2
    Piece(Nub.OUTY_CLUB, Nub.OUTY_HEART, Nub.INNY_SPADE, Nub.INNY_HEART, 4), // 3
    Piece(Nub.INNY_SPADE, Nub.INNY_HEART, Nub.OUTY_SPADE, Nub.OUTY_DIAMOND, 5),
    Piece(Nub.OUTY_HEART, Nub.OUTY_DIAMOND, Nub.INNY_DIAMOND, Nub.INNY_HEART, 6),
    Piece(Nub.OUTY_HEART, Nub.OUTY_DIAMOND, Nub.INNY_CLUB, Nub.INNY_CLUB, 7),
    Piece(Nub.OUTY_DIAMOND, Nub.OUTY_CLUB, Nub.INNY_CLUB, Nub.INNY_DIAMOND, 8),
    Piece(Nub.OUTY_SPADE, Nub.OUTY_SPADE, Nub.INNY_HEART, Nub.INNY_CLUB, 9))

  /** Mapping from Lynette's puzzle */
  val LYNETTE_INITIAL_PIECES_9 = Array(
    Piece(Nub.INNY_HEART, Nub.OUTY_SPADE, Nub.OUTY_DIAMOND, Nub.OUTY_CLUB, 1), // 0
    Piece(Nub.OUTY_SPADE, Nub.INNY_CLUB, Nub.INNY_HEART, Nub.INNY_DIAMOND, 2),  // 1
    Piece(Nub.OUTY_HEART, Nub.OUTY_DIAMOND, Nub.INNY_SPADE, Nub.INNY_HEART, 3), // 2
    Piece(Nub.INNY_HEART, Nub.INNY_CLUB, Nub.OUTY_SPADE, Nub.OUTY_DIAMOND, 4),  // 3
    Piece(Nub.INNY_SPADE, Nub.OUTY_DIAMOND, Nub.INNY_HEART, Nub.OUTY_CLUB, 5),
    Piece(Nub.OUTY_DIAMOND, Nub.INNY_HEART, Nub.INNY_CLUB, Nub.INNY_SPADE, 6),
    Piece(Nub.OUTY_HEART, Nub.OUTY_SPADE, Nub.OUTY_DIAMOND, Nub.OUTY_CLUB, 7),
    Piece(Nub.OUTY_HEART, Nub.INNY_DIAMOND, Nub.INNY_SPADE, Nub.OUTY_CLUB, 8),
    Piece(Nub.OUTY_SPADE, Nub.INNY_DIAMOND, Nub.INNY_CLUB, Nub.INNY_SPADE, 9))

  /** This defines the puzzle pieces for a simpler 4x4 puzzle. */
  val INITIAL_PIECES_4 = Array(
    RED_INITIAL_PIECES_9(0),
    RED_INITIAL_PIECES_9(1),
    RED_INITIAL_PIECES_9(2),
    RED_INITIAL_PIECES_9(3)
  )

  def getInitialPuzzlePieces(rnd: Random): PieceList =
    getInitialPuzzlePieces(PieceList.DEFAULT_NUM_PIECES, rnd)

  /** Factory method for creating the initial puzzle pieces.
    * @return the initial 9 pieces (in random order) to use when solving.
    */
  def getInitialPuzzlePieces(numPieces: Int, rnd: Random = MathUtil.RANDOM): PieceList = {
    val initialPieces = numPieces match {
      case 4 => PieceLists.INITIAL_PIECES_4
      case 9 => PieceLists.RED_INITIAL_PIECES_9
      case _ => throw new IllegalArgumentException("We only support 4 or 9 piece red puzzles at this time.")
    }
    val pieces = new PieceList(initialPieces)
    // shuffle the pieces so we get difference solutions - or at least different approaches.
    pieces.shuffle(rnd)
  }
}
