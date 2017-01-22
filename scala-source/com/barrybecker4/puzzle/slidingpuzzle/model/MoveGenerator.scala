// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.model

import com.barrybecker4.common.geometry.ByteLocation
import scala.collection.Seq
import MoveGenerator.OFFSETS


object MoveGenerator {
  val OFFSETS = Array(
    new ByteLocation(-1, 0),
    new ByteLocation(1, 0),
    new ByteLocation(0, -1),
    new ByteLocation(0, 1)
  )
}

/**
  * Sliding Puzzle move generator. Generates valid next moves.
  *
  * @author Barry Becker
  */
class MoveGenerator {

  /** @return List of valid next moves - all the tiles that can slide into the current empty position. */
  def generateMoves(board: SliderBoard): Seq[SlideMove] = {
    var moves = List[SlideMove]()
    val blankLocation = board.getEmptyLocation
    for (loc <- OFFSETS) {
      val row = blankLocation.getRow + loc.getRow
      val col = blankLocation.getCol + loc.getCol
      val newLoc = new ByteLocation(row, col)
      if (board.isValidPosition(newLoc)) {
        moves :+= SlideMove(newLoc, blankLocation)
      }
    }
    moves
  }
}
