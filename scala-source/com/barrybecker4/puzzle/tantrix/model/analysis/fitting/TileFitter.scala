// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model.analysis.fitting

import com.barrybecker4.puzzle.tantrix.model.HexTile.NUM_SIDES
import com.barrybecker4.puzzle.tantrix.model.HexUtil._
import com.barrybecker4.puzzle.tantrix.model.PathColor.PathColor
import com.barrybecker4.puzzle.tantrix.model.TilePlacement

/**
  * Used to check the consistency of all the paths.
  * If you have the tantrix, use TantrixTileFitter instead of this class.
  *
  * @param tiles Current set of placed tiles
  * @author Barry Becker
  */
class TileFitter(var tiles: Iterable[TilePlacement], primaryColor: PathColor) extends AbstractFitter(primaryColor) {

  /**
    * The tile fits if the primary path and all the other paths match for edges that have neighbors.
    * @param placement the tile to check for a valid fit.
    * @return true of the tile fits
    */
  def isFit(placement: TilePlacement): Boolean = {
    var primaryPathMatched = false

    for (i <- 0 until NUM_SIDES) {
      val nbr = getNeighbor(Some(placement), i)
      if (nbr.isDefined) {
        val pathColor = placement.getPathColor(i)
        if (pathColor == nbr.get.getPathColor(i + 3)) {
          if (pathColor == primaryColor)
            primaryPathMatched = true
        }
        else return false
      }
    }
    primaryPathMatched
  }

  /**
    * @param currentPlacement where we are now
    * @param direction    side to navigate in order to find the neighbor. 0 is to the right.
    * @return the indicated neighbor of the specified tile if it exists, else None.
    */
  protected def getNeighbor(currentPlacement: Option[TilePlacement], direction: Int): Option[TilePlacement] = {
    val loc = getNeighborLocation(currentPlacement.get.location, direction)
    tiles.find(_.location == loc)
  }
}