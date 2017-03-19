// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model.analysis

import com.barrybecker4.common.geometry.Location
import com.barrybecker4.puzzle.tantrix.model.HexTile.NUM_SIDES
import com.barrybecker4.puzzle.tantrix.model.PathColor.PathColor
import com.barrybecker4.puzzle.tantrix.model.RotationEnum._
import com.barrybecker4.puzzle.tantrix.model.{HexTile, TilePlacement}

/**
  * Used to check the consistency of all the paths.
  * @param primaryColor color of the loop path
  * @author Barry Becker
  */
abstract class AbstractFitter(val primaryColor: PathColor) {

  /**
    * The number of placements can be 0, 1, 2, or 3 (rare).
    * PrimaryPathFitter can never have just one, because there are two outputs for every path on a tile.
    *
    * @param tile the tile to place.
    * @param loc  the location to try and place it at.
    * @return the placements (at most 3) if any could be found, else an empty list.
    */
  def getFittingPlacements(tile: HexTile, loc: Location): Seq[TilePlacement] = {
    var placement = TilePlacement(tile, loc, ANGLE_300)
    for (i <- 0 until NUM_SIDES; if isFit(placement.rotate())) yield placement
  }

  /**
    * The tile fits if the primary path and all the other paths match for edges that have neighbors.
    *
    * @param placement the tile to check for a valid fit.
    * @return true of the tile fits
    */
  def isFit(placement: TilePlacement): Boolean
}
