/*
 * // Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
 */

package com.barrybecker4.puzzle.tantrix.solver.path.permuting

import com.barrybecker4.common.geometry.Location
import com.barrybecker4.puzzle.tantrix.model.PathColor
import com.barrybecker4.puzzle.tantrix.model.{HexUtil, TilePlacement}
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath

/**
  * Swap a subpath from on outgoing primary path to the other on the pivot tile.
  *
  * @author Barry Becker
  */
class SubPathSwapper private[permuting](primaryColor: PathColor) extends SubPathMutator(primaryColor) {

  /**
    * Only one tile in the subPath is touching the pivotTile. When we are done swapping,
    * the same path tile will be rotated and translated (as well as all the tiles connected to it) so that
    * it connects to the other outgoing path on the pivot tile.
    *
    * @param subPath the subpath to swap to the other outgoing path on the pivot tile.
    * @return the whole path rotated and translated so that the same end is connected at
    *         the a different point the pivot tile. There is only one other valid point that it can connect to.
    */
  def mutate(pivotTile: TilePlacement, subPath: TantrixPath): TantrixPath = {
    val subPathTiles = subPath.tiles
    val firstTile = subPathTiles.head
    // direction from the pivot to the other place where the primary path leaves the pivot tile
    val swapDirection = findRotationsToSwapLocation(firstTile.location, pivotTile)
    val directionToPivot = findOutgoingDirection(firstTile, pivotTile.location)
    val numRotations = swapDirection + 3 - directionToPivot
    val newLocation = HexUtil.getNeighborLocation(pivotTile.location, swapDirection)
    val first = TilePlacement(firstTile.tile, newLocation, firstTile.rotation.rotateBy(numRotations))
    val tiles = layOutChain(first, pivotTile.location, subPathTiles.tail, numRotations,
      s"when swapping $subPath at pivot = $pivotTile with primColor = $primaryColor")
    new TantrixPath(tiles, primaryColor, subPath.desiredLength)
  }

  /**
    * There are two outgoing paths from the pivot tile. The firstTileLocation is at one of them. We want the other one.
    * @return the number of rotations to get to the swap location.
    */
  private def findRotationsToSwapLocation(firstTileLocation: Location, pivotTile: TilePlacement): Int = {
    val outgoingPathLocations = pivotTile.getOutgoingPathLocations(primaryColor)
    outgoingPathLocations.iterator.collectFirst { case (key, loc) if firstTileLocation != loc => key } match
      case Some(key) => key
      case None =>
        assert(false)
        0
  }
}
