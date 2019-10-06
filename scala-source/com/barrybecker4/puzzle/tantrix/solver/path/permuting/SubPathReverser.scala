// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path.permuting

import com.barrybecker4.puzzle.tantrix.model.PathColor.PathColor
import com.barrybecker4.puzzle.tantrix.model.TilePlacement
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath

import scala.collection.mutable.ListBuffer

/**
  * Reverse a subpath.
  *
  * @author Barry Becker
  */
class SubPathReverser private[permuting](primaryColor: PathColor) extends SubPathMutator(primaryColor) {
  /**
    * Only one tile in the subPath is touching the pivotTile. When we are done reversing,
    * the tile at the other end of the path will be touching the pivotTile at the same location.
    *
    * @param subPath the subpath to reverse relative to the pivot tile.
    * @return the whole path rotated and translated so that the other end is connected at
    *         the same point on the pivot tile.
    */
  def mutate(pivotTile: TilePlacement, subPath: TantrixPath): TantrixPath = {
    val tiles: ListBuffer[TilePlacement] = new ListBuffer[TilePlacement]()
    val subPathTiles = subPath.tiles //.to[ListBuffer]
    val lastTile = subPathTiles.last
    val outgoingDirection = findDirectionAwayFromLast(subPathTiles, lastTile, pivotTile)
    var newLocation = subPathTiles.head.location
    var startDir = 0
    startDir = findOutgoingDirection(pivotTile, newLocation)
    val numRotations = startDir - 3 - outgoingDirection
    var origLocation = pivotTile.location
    var tileRotation = lastTile.rotation.rotateBy(numRotations)
    var previousTilePlacement = TilePlacement(lastTile.tile, newLocation, tileRotation)
    tiles.append(previousTilePlacement)

    // this part is almost the same as in swapper
    for ( i<- subPathTiles.size - 2 to 0 by -1) {
        val currentTile = subPathTiles(i)
        newLocation = findOtherOutgoingLocation(previousTilePlacement, origLocation).get
        tileRotation = currentTile.rotation.rotateBy(numRotations)
        val currentTilePlacement = TilePlacement(currentTile.tile, newLocation, tileRotation)
        assert(fits(currentTilePlacement, previousTilePlacement), " current=" + currentTilePlacement +
          " (" + i + ") did not fit with " + previousTilePlacement + " when reversing "
          + subPath + " at pivot = " + pivotTile)
        tiles.append(currentTilePlacement)
        origLocation = previousTilePlacement.location
        previousTilePlacement = currentTilePlacement
    }
    new TantrixPath(tiles.toSeq, primaryColor)
  }

  /**
    * @param subPathTiles other path tiles
    * @param lastTile     the last tile in the path
    * @return the direction leading away from the tile right before it in the path.
    */
  private def findDirectionAwayFromLast(subPathTiles: Seq[TilePlacement], lastTile: TilePlacement,
                                        pivotTile: TilePlacement): Int = {
    var outgoing = lastTile.getOutgoingPathLocations(primaryColor)
    val directionToPrev = if (subPathTiles.size > 1)
        findOutgoingDirection(lastTile, subPathTiles(subPathTiles.size - 2).location)
      else findOutgoingDirection(lastTile, pivotTile.location)
    // after removing, there will be only one outgoing path - the one that is free.
    outgoing -= directionToPrev
    outgoing.keySet.head
  }
}
