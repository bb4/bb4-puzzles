// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.generation

import com.barrybecker4.puzzle.tantrix.model.analysis.fitting.TantrixTileFitter
import com.barrybecker4.puzzle.tantrix.model.{HexTile, TantrixBoard, TilePlacement}

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
    var moves: List[TilePlacement] = List()
    val unplacedTiles = board.unplacedTiles
    for (tile <- unplacedTiles) {
      findPlacementsForTile(tile).foreach(moves +:= _)
    }
    moves
  }

  /** @return list of all the legal placements for the specified tile.*/
  private def findPlacementsForTile(tile: HexTile) = {
    var placements: List[TilePlacement] = List()
    val fitter = new TantrixTileFitter(board.tantrix, board.primaryColor)
    for (loc <- borderSpaces) {
      val validFits = fitter.getFittingPlacements(tile, loc)
      validFits.foreach(placements +:= _)
    }
    placements
  }
}
