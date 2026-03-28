// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.generation

import com.barrybecker4.puzzle.tantrix.model.analysis.fitting.TantrixTileFitter
import com.barrybecker4.puzzle.tantrix.model.{TantrixBoard, TilePlacement}

import scala.collection.mutable.ListBuffer

/**
  * Tantrix puzzle move generator. Generates valid next moves given the current state.
  * @author Barry Becker
  */
class MoveGenerator(var board: TantrixBoard) {
  /** a set of all the places that a tile might be placed next. */
  private val borderSpaces = new BorderFinder(board.tantrix, board.numTiles, board.primaryColor).findBorderPositions

  /** For each unplaced tile, find all valid placements given current configuration.
    * Valid placements must extend the primary path.
    * @return List of all valid tile placements for the current tantrix state.
    */
  def generateMoves: List[TilePlacement] = {
    val fitter = new TantrixTileFitter(board.tantrix, board.primaryColor)
    val buf = ListBuffer.empty[TilePlacement]
    for {
      tile <- board.unplacedTiles
      loc <- borderSpaces
      p <- fitter.getFittingPlacements(tile, loc)
    } buf += p
    buf.toList
  }
}
