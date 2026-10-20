// Copyright by Barry G. Becker, 2017 - 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.generation

import com.barrybecker4.common.geometry.Location
import com.barrybecker4.puzzle.tantrix.generation.LoopFeasibilityChecker.OpenEnd
import com.barrybecker4.puzzle.tantrix.generation.MoveGenerator.GrowthMode
import com.barrybecker4.puzzle.tantrix.model.analysis.fitting.TantrixTileFitter
import com.barrybecker4.puzzle.tantrix.model.{HexUtil, TantrixBoard, TilePlacement}

object MoveGenerator {

  /** Where on the primary path new tiles can be added. */
  enum GrowthMode {
    /** Tiles can be added at either end of the path. The same arrangement of tiles can be built in many different
      * orders, and a search will go through all of them. */
    case BOTH_ENDS

    /** Tiles are only added at the end of the path that the last tile was placed at, so each arrangement of tiles can
      * only be reached in one order. This is complete because any loop that includes the path can be followed around
      * from one end of the path to the other. When there is only one tile, the path grows from the first of its two
      * primary path sides. It is ignored if the last tile placed is not at the end of the path. */
    case ONE_END

    /** Of the two empty spaces that the two ends of the path lead to, only the one with the fewest ways to place
      * a tile is used. Both spaces must be filled in any solution, so no solutions are lost, and the one with the
      * fewest choices is the best to branch on. A state with no way to fill either space is a dead end, and no
      * moves are returned for it. Like [[ONE_END]], each arrangement is not reached in many different orders. */
    case SCARCER_END
  }
}

/**
  * Tantrix puzzle move generator. Generates valid next moves given the current state.
  *
  * By default, a tile may be added at either end of the primary path. Searches for a solution should limit that
  * with the `growth` mode, and also `prune`, which together are much more efficient. They
  * will still find every solution that the default would.
  *
  * @param board the current state
  * @param growth which end(s) of the path new tiles are added to.
  * @param prune If true, moves that [[LoopFeasibilityChecker]] can show will never lead to a solution are left out.
  * @author Barry Becker
  */
class MoveGenerator(val board: TantrixBoard, growth: GrowthMode = GrowthMode.BOTH_ENDS, prune: Boolean = false) {

  private val borderFinder = new BorderFinder(board.tantrix, board.numTiles, board.primaryColor)
  private val fitter = new TantrixTileFitter(board.tantrix, board.primaryColor)

  /** For each unplaced tile, find all valid placements given current configuration.
    * Valid placements must extend the primary path.
    * Tiles are ordered by MRV (fewest fitting placements first) to fail fast in backtracking search.
    * @return List of all valid tile placements for the current tantrix state.
    */
  def generateMoves: List[TilePlacement] = growth match {
    case GrowthMode.BOTH_ENDS => movesAt(borderFinder.findBorderPositions)
    case GrowthMode.ONE_END =>
      openEnds.find(_.placement.location == board.getLastTile.location) match {
        case Some(tail) => movesAt(growthLocations(Seq(tail)).take(1))
        case None => movesAt(borderFinder.findBorderPositions) // the last tile is not at the end of the path
      }
    case GrowthMode.SCARCER_END => movesAtScarcerEnd
  }

  /** @return the empty locations the primary path leads to from the specified ends, if tiles are allowed there. */
  private def growthLocations(ends: Seq[OpenEnd]): Seq[Location] =
    ends.flatMap(end => end.exits.map(HexUtil.getNeighborLocation(end.placement.location, _)))
      .filter(borderFinder.isWithinSizeLimit)

  /** The tile placements at the specified locations, with the tiles that have the fewest placements first. */
  private def movesAt(locations: Iterable[Location]): List[TilePlacement] = {
    // The neighbors of each location are the same for every tile, so they are only looked at once.
    val fittersAtLocations = locations.toList.map(fitter.fitterAt)
    // finding the fitting placements is the expensive part, so it is only done once for each tile
    val placementsByTile = board.unplacedTiles.map(tile => fittersAtLocations.flatMap(_.getFittingPlacements(tile)))
    val moves = placementsByTile.sortBy(_.size).flatten.toList
    if (prune) moves.filter(isFeasible) else moves
  }

  /** The ends of the path of the current board. Only looked for if needed, and then only once. */
  private lazy val openEnds = LoopFeasibilityChecker.findOpenEnds(board)

  private def isFeasible(move: TilePlacement): Boolean =
    new LoopFeasibilityChecker(board.placeTile(move), LoopFeasibilityChecker.endsAfterPlacing(board, openEnds, move)).isFeasible

  /** Use the open end with the fewest moves. If there are no moves at one of them, then there is no solution. */
  private def movesAtScarcerEnd: List[TilePlacement] = {
    val ends = growthLocations(openEnds)
    // A single tile has two open sides, but both are not needed. The path can be followed around from just one.
    val locations = if (board.tantrix.size == 1) ends.take(1) else ends
    if (locations.isEmpty) movesAt(locations)
    else {
      val movesByLocation = locations.map(loc => movesAt(Seq(loc)))
      if (movesByLocation.exists(_.isEmpty)) Nil else movesByLocation.minBy(_.size)
    }
  }
}
