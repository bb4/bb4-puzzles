// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.model

import com.barrybecker4.common.geometry.ByteLocation
import MoveGenerator.OFFSETS


object MoveGenerator {
  val OFFSETS: Array[ByteLocation] = Array(
    ByteLocation(-1, 0),
    ByteLocation(1, 0),
    ByteLocation(0, -1),
    ByteLocation(0, 1)
  )
}

/**
  * Sliding Puzzle move generator. Generates valid next moves.
  * @author Barry Becker
  */
class MoveGenerator {

  /** @return List of valid next moves - all the tiles that can slide into the current empty position. */
  def generateMoves(board: SliderBoard): Seq[SlideMove] = {
    val blankLocation = board.getEmptyLocation
    OFFSETS.iterator
      .map(d =>
        ByteLocation(
          (blankLocation.row + d.row).toByte,
          (blankLocation.col + d.col).toByte))
      .filter(board.isValidPosition)
      .map(adj => SlideMove(fromPosition = adj, toPosition = blankLocation))
      .toIndexedSeq
  }
}
