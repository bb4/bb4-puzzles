// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path.permuting

import com.barrybecker4.puzzle.tantrix.model.PathColor
import com.barrybecker4.puzzle.tantrix.model.TilePlacement
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath

/**
  * Reverse a subpath.
  *
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
    val subPathTiles = subPath.tiles
    val lastTile = subPathTiles.last
    val outgoingDirection = findDirectionAwayFromLast(subPathTiles, lastTile, pivotTile)
    // the reversed path starts where the original one did, which is the spot next to the pivot tile.
    val newLocation = subPathTiles.head.location
    val numRotations = findOutgoingDirection(pivotTile, newLocation) - 3 - outgoingDirection
    val first = TilePlacement(lastTile.tile, newLocation, lastTile.rotation.rotateBy(numRotations))
    // the remaining tiles are visited from the one next to the last, back to the original first
    val reversedRemaining = subPathTiles.init.reverse
    val tiles = layOutChain(first, pivotTile.location, reversedRemaining, numRotations,
      s"when reversing $subPath at pivot = $pivotTile")
    new TantrixPath(tiles, primaryColor, subPath.desiredLength)
  }

  /**
    * @param subPathTiles other path tiles
    * @param lastTile     the last tile in the path
    * @return the direction leading away from the tile right before it in the path.
    */
  private def findDirectionAwayFromLast(subPathTiles: Seq[TilePlacement], lastTile: TilePlacement,
                                        pivotTile: TilePlacement): Int = {
    val tileBefore = if (subPathTiles.size > 1) subPathTiles(subPathTiles.size - 2) else pivotTile
    val directionToPrev = findOutgoingDirection(lastTile, tileBefore.location)
    // after removing, there will be only one outgoing path - the one that is free.
    val freeOutgoing = lastTile.getOutgoingPathLocations(primaryColor) - directionToPrev
    freeOutgoing.keySet.head
  }
}
