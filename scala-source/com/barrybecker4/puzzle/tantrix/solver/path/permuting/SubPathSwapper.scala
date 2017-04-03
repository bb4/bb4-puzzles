/*
 * // Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
 */

package com.barrybecker4.puzzle.tantrix.solver.path.permuting

import com.barrybecker4.common.geometry.Location
import com.barrybecker4.puzzle.tantrix.model.PathColor.PathColor
import com.barrybecker4.puzzle.tantrix.model.{HexUtil, TilePlacement}
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath

import scala.collection.mutable.ListBuffer

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
    val tiles: ListBuffer[TilePlacement]  = new ListBuffer[TilePlacement]
    val subPathTiles = subPath.getTilePlacements
    val firstTile: TilePlacement = subPathTiles.head
    val firstTileLocation = firstTile.location
    var numRotations = findRotationsToSwapLocation(firstTileLocation, pivotTile)
    val directionToPivot = findOutgoingDirection(firstTile, pivotTile.location)
    var newLocation = HexUtil.getNeighborLocation(pivotTile.location, numRotations)
    var origLocation = pivotTile.location
    numRotations = numRotations + 3 - directionToPivot
    val tileRotation = firstTile.rotation.rotateBy(numRotations)
    var previousTilePlacement = TilePlacement(firstTile.tile, newLocation, tileRotation)
    tiles.append(previousTilePlacement)
    // this part almost the same as reverser
    for (i <- 1 until subPathTiles.size) {
        val currentTile = subPathTiles(i)
        newLocation = findOtherOutgoingLocation(previousTilePlacement, origLocation).get
        val tileRotation1 = currentTile.rotation.rotateBy(numRotations)
        val currentTilePlacement = TilePlacement(currentTile.tile, newLocation, tileRotation1)
        assert(fits(currentTilePlacement, previousTilePlacement), " current=" +
          currentTilePlacement + " (" + i + ") did not fit with " + previousTilePlacement +
          " when swapping " + subPath + " at pivot = " + pivotTile + " with primColor = " + primaryColor +
          " The outgoing locations from curent are " + currentTilePlacement.getOutgoingPathLocations(primaryColor))
        tiles.append(currentTilePlacement)
        origLocation = previousTilePlacement.location //currentTilePlacement.getLocation();
        previousTilePlacement = currentTilePlacement
    }
    new TantrixPath(tiles, primaryColor)
  }

  /**
    * There are two outgoing paths from the pivot tile. The firstTileLocation is at one of them. We want the other one.
    * @return the number of rotations to get to the swap location.
    */
  private def findRotationsToSwapLocation(firstTileLocation: Location, pivotTile: TilePlacement): Int = {
    val outgoingPathLocations = pivotTile.getOutgoingPathLocations(primaryColor)
    val keys = outgoingPathLocations.keySet
    for (key <- keys) {
      val loc = outgoingPathLocations(key)
      if (firstTileLocation != loc) return key
    }
    assert(false)
    0
  }
}
