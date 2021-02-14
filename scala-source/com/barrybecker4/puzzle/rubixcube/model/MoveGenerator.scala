// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.model


/**
  * Rubix cube move generator. Generates valid next moves from a given cube state.
  */
class MoveGenerator {

  /** @return List of valid next moves. There should be 18 for a cube of size 3. */
  def generateMoves(cube: Cube): Seq[CubeMove] = {
    for (orientation <- Orientation.PRIMARY_ORIENTATIONS;
         layer <- 1 to cube.size;
         direction <- Direction.VALUES) yield CubeMove(orientation, layer, direction)
  }
}
