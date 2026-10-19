// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.generation

import com.barrybecker4.puzzle.tantrix.model.analysis.fitting.TantrixTileFitter
import com.barrybecker4.puzzle.tantrix.model.{TantrixBoard, TilePlacement}

/**
  * Tantrix puzzle move generator. Generates valid next moves given the current state.
  * @author Barry Becker
  */
class MoveGenerator(val board: TantrixBoard) {
  /** a set of all the places that a tile might be placed next. */
  private val borderSpaces = new BorderFinder(board.tantrix, board.numTiles, board.primaryColor).findBorderPositions

  /** For each unplaced tile, find all valid placements given current configuration.
    * Valid placements must extend the primary path.
    * Tiles are ordered by MRV (fewest fitting placements first) to fail fast in backtracking search.
    * @return List of all valid tile placements for the current tantrix state.
    */
  def generateMoves: List[TilePlacement] = {
    val fitter = new TantrixTileFitter(board.tantrix, board.primaryColor)
    val borderList = borderSpaces.toList
    // finding the fitting placements is the expensive part, so it is only done once for each tile
    val placementsByTile = board.unplacedTiles.map(tile => borderList.flatMap(loc => fitter.getFittingPlacements(tile, loc)))
    placementsByTile.sortBy(_.size).flatten.toList
  }
}
