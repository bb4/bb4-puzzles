// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model.analysis.fitting

import com.barrybecker4.common.geometry.Location
import com.barrybecker4.puzzle.tantrix.model.HexTile.NUM_SIDES
import com.barrybecker4.puzzle.tantrix.model.HexUtil.getNeighborLocation
import com.barrybecker4.puzzle.tantrix.model.PathColor
import com.barrybecker4.puzzle.tantrix.model.{HexTile, Rotation, Tantrix, TilePlacement}

/**
  * Used to check the consistency of all the paths.
  * The process of finding neighbors is a bit more efficient if we have the tantrix.
  */
class TantrixTileFitter(tantrix: Tantrix, primaryColor: PathColor) extends TileFitter(tantrix.tiles, primaryColor) {

  /** Finds the same placements as the general approach, but looks at the neighbors of the location once instead of
    * for every rotation of every tile. When many tiles are tried at the same location, use [[fitterAt]]
    * so that the neighbors are only looked at once for all of them.
    * @return the rotations of the tile (at most 3) that fit at the location, in increasing order of rotation.
    */
  override def getFittingPlacements(tile: HexTile, loc: Location): Seq[TilePlacement] =
    fitterAt(loc).getFittingPlacements(tile)

  /** @return a fitter for placing different tiles at the same location. Looking up the neighbors of the location in the
    *         tantrix is the slowest part of checking whether a tile fits, and this does it only once.
    */
  def fitterAt(loc: Location): LocationFitter = new LocationFitter(loc, colorsFacingLocation(loc))

  /** Finds the placements of tiles at one location, whose neighbors have already been looked up. */
  class LocationFitter private[TantrixTileFitter](loc: Location, neighborColors: Array[PathColor]) {

    /** @return the rotations of the tile (at most 3) that fit at the location, in increasing order of rotation. */
    def getFittingPlacements(tile: HexTile): Seq[TilePlacement] =
      Rotation.values.iterator
        .filter(rotation => fitsNeighborColors(tile, rotation, neighborColors))
        .map(TilePlacement(tile, loc, _))
        .toSeq
  }

  /** @return for each side of the location, the color of the edge of the neighboring tile that touches it. null if no neighbor. */
  private def colorsFacingLocation(loc: Location): Array[PathColor] = {
    val colors = new Array[PathColor](NUM_SIDES)
    for (side <- 0 until NUM_SIDES)
      tantrix.tileMap.get(getNeighborLocation(loc, side)).foreach(nbr => colors(side) = nbr.getPathColor(side + NUM_SIDES / 2))
    colors
  }

  /** The tile fits if every side with a neighbor has the same color as the neighbor, and the primary path is
    * matched on at least one of them. Same as [[TileFitter.isFit]].
    */
  private def fitsNeighborColors(tile: HexTile, rotation: Rotation, neighborColors: Array[PathColor]): Boolean = {
    var primaryPathMatched = false
    var side = 0
    var fits = true
    while (fits && side < NUM_SIDES) {
      val neighborColor = neighborColors(side)
      if (neighborColor != null) {
        val color = tile.edgeColors((NUM_SIDES + side - rotation.ordinal) % NUM_SIDES) // same as TilePlacement.getPathColor
        if (color == neighborColor) primaryPathMatched ||= color == primaryColor
        else fits = false
      }
      side += 1
    }
    fits && primaryPathMatched
  }

  /** @param currentPlacement where we are now
    * @param direction        side to navigate to to find the neighbor. 0 is to the right.
    * @return the indicated neighbor of the specified tile.
    */
  override protected def getNeighbor(currentPlacement: Option[TilePlacement], direction: Int): Option[TilePlacement] =
    tantrix.getNeighbor(currentPlacement, direction)
}
