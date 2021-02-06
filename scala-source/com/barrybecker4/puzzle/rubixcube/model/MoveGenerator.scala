// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.model

import com.barrybecker4.common.geometry.ByteLocation
import MoveGenerator.OFFSETS


object MoveGenerator {
  val OFFSETS: Array[ByteLocation] = Array(
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
  def generateMoves(board: Cube): Seq[CubeMove] = {
    Seq()
  }
}
