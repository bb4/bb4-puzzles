// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model.analysis.fitting

import com.barrybecker4.puzzle.tantrix.model.HexTile.NUM_SIDES
import com.barrybecker4.puzzle.tantrix.model.HexUtil.getNeighborLocation
import com.barrybecker4.puzzle.tantrix.model.PathColor
import com.barrybecker4.puzzle.tantrix.model.TilePlacement

import scala.util.boundary
import scala.util.boundary.break



/**
  * Used to check the consistency of all the paths.
  * If you have the tantrix, use TantrixTileFitter instead of this class.
  * @param tiles Current set of placed tiles
  */
class TileFitter(tiles: Iterable[TilePlacement], primaryColor: PathColor) extends AbstractFitter(primaryColor) {

  /** Placed tiles by location, so that neighbor lookups are O(1) instead of a scan of all tiles.
    * Lazy because TantrixTileFitter overrides getNeighbor and never needs it. Locations are unique.
    */
  private lazy val placementByLocation = tiles.map(tp => tp.location -> tp).toMap

  /** The tile fits if the primary path and all the other paths match for edges that have neighbors.
    * @param placement the tile to check for a valid fit.
    * @return true of the tile fits
    */
  def isFit(placement: TilePlacement): Boolean = boundary:
    var primaryPathMatched = false

    for (i <- 0 until NUM_SIDES) {
      val nbr = getNeighbor(Some(placement), i)
      if (nbr.isDefined) {
        val pathColor = placement.getPathColor(i)
        if (pathColor == nbr.get.getPathColor(i + 3)) {
          if (pathColor == primaryColor)
            primaryPathMatched = true
        }
        else break(false)
      }
    }
    primaryPathMatched

  /** @param currentPlacement where we are now
    * @param direction    side to navigate in order to find the neighbor. 0 is to the right.
    * @return the indicated neighbor of the specified tile if it exists, else None.
    */
  protected def getNeighbor(currentPlacement: Option[TilePlacement], direction: Int): Option[TilePlacement] =
    placementByLocation.get(getNeighborLocation(currentPlacement.get.location, direction))
}
