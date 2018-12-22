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
  * @author Barry Becker
  */
class MoveGenerator {

  /** @return List of valid next moves - all the tiles that can slide into the current empty position. */
  def generateMoves(board: SliderBoard): Seq[SlideMove] = {
    val blankLocation = board.getEmptyLocation
    OFFSETS.map(loc => {
      new ByteLocation(blankLocation.row + loc.row, blankLocation.col + loc.col)
    }).filter(board.isValidPosition).map(SlideMove(_, blankLocation))
  }
}
