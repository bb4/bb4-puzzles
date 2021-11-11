// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.model

import com.barrybecker4.puzzle.rubixcube.model.Direction
import com.barrybecker4.puzzle.rubixcube.model.Orientation.PRIMARY_ORIENTATIONS


/**
  * Rubix cube move generator. Generates valid next moves from a given cube state.
  */
class MoveGenerator {

  /** @return List of valid next moves. There should be 18 for a cube of size 3. */
  def generateMoves(cube: Cube): Seq[CubeMove] = {
    val moves =
      for (orientation <- PRIMARY_ORIENTATIONS;
        layer <- 1 to cube.size;
        direction <- Direction.values)
          yield CubeMove(orientation, layer, direction)

    moves.toIndexedSeq
  }
}
