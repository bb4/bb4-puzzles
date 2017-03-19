// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model.analysis

import com.barrybecker4.puzzle.tantrix.model.HexTile.NUM_SIDES
import com.barrybecker4.puzzle.tantrix.model.PathColor.PathColor
import com.barrybecker4.puzzle.tantrix.model.{Tantrix, TilePlacement}

/**
  * Determines valid primary path fits for a specified tile relative to an existing set at a specific location.
  * This is less strict than tile fitter - which checks all paths.
  *
  * @author Barry Becker
  */
class PrimaryPathFitter(tantrix: Tantrix, primaryColor: PathColor) extends AbstractFitter(primaryColor) {

  def this(tiles: Seq[TilePlacement], primaryColor: PathColor) {
    this(new Tantrix(tiles), primaryColor)
  }

  /**
    * The tile fits if the primary path fits against all neighbors.
    * All adjacent primary paths must match an edge on the tile being fit.
    * Check all the neighbors (that exist) and verify that if that direction is a primary path output, then it matches.
    * If none of the neighbor edges has a primary path, then rotations where non-primary path edges touch those neighbors
    * are allowed as fits.
    *
    * @param placement the tile to check for a valid fit.
    * @return true of the tile fits
    */
  def isFit(placement: TilePlacement): Boolean = {
    var fits = true
    for (i <- 0 until NUM_SIDES) {
      val nbr = tantrix.getNeighbor(placement, i)
      if (nbr.isDefined) {
        val pathColor = placement.getPathColor(i)
        val nbrColor = nbr.get.getPathColor(i + 3)
        if (((pathColor == primaryColor) || (nbrColor == primaryColor)) && (pathColor == nbrColor))
          fits = false
      }
    }
    fits
  }

  /**
    * @param placement the tile to check for a valid fit.
    * @return the number of primary path matches to neighboring tiles.
    */
  private def numPrimaryFits(placement: TilePlacement) = {
    var numFits = 0
    var i = 0
    for (i <- 0 until NUM_SIDES) {
        val nbr = tantrix.getNeighbor(placement, i)
        if (nbr.isDefined) {
          val pathColor = placement.getPathColor(i)
          if ((pathColor == primaryColor) && (pathColor == nbr.get.getPathColor(i + 3)))
            numFits += 1
        }
    }
    assert(numFits <= 2, "There cannot be more than 2 primary path fits.")
    numFits
  }

  /** @return total number of primary path fits for the whole tantrix. */
  def numPrimaryFits: Int = {
    if (tantrix.size < 2) 0
    else tantrix.tiles.map(numPrimaryFits).sum
  }

  /** used only for debugging */
  def getTantrix: Tantrix = tantrix
}
