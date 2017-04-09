// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.generation

import com.barrybecker4.common.geometry.{Box, Location}
import com.barrybecker4.puzzle.tantrix.model.HexTile.NUM_SIDES
import com.barrybecker4.puzzle.tantrix.model.PathColor.PathColor
import com.barrybecker4.puzzle.tantrix.model._

import scala.collection.mutable

/**
  * Finds the set of moves on the border of the current 'tantrix'.
  * The 'tantrix' is the set of currently played consistent tiles.
  * The moves on the border will extend the primary path, but not such that
  * its width or height is more than half the length of the total finished path length.
  *
  * @author Barry Becker
  */
class BorderFinder private[generation](var tantrix: Tantrix, val numTiles: Int, var primaryColor: PathColor) {
  private val maxHalfPathLength = (numTiles + 1) / 2
  private var boundingBox = tantrix.getBoundingBox
  private var visited: Set[Location] = _

  /**
    * Travel the primary path in both directions, adding all adjacent empty placements
    * as long as they do not push either boundingBox dimension beyond maxHalfPathLength.
    *
    * @return list of legal next placements
    */
  private[generation] def findBorderPositions = {
    val positions: mutable.LinkedHashSet[Location] = mutable.LinkedHashSet[Location]()
    visited = Set()
    val lastPlaced = tantrix.lastTile
    val searchQueue: mutable.Queue[TilePlacement] = mutable.Queue()
    searchQueue.enqueue(lastPlaced)
    tantrix.tiles.foreach(visited += _.location)

    while (searchQueue.nonEmpty) {
      val placement = searchQueue.dequeue()
      findEmptyNeighborLocations(placement).foreach(positions.add)
      searchQueue.enqueue(findPrimaryPathNeighbors(placement):_*)
    }
    positions
  }

  /**
    * @return all the empty neighbor positions next to the specified placement with primary path match.
    */
  private def findEmptyNeighborLocations(placement: TilePlacement): List[Location] = {
    var emptyNbrLocations = List[Location]()

    for (i <- 0 until NUM_SIDES) {
      val nbrLoc = HexUtil.getNeighborLocation(placement.location, i)
      val nbr = tantrix(nbrLoc)
      if (nbr == null && (placement.getPathColor(i) == primaryColor)) {
        val newBox = new Box(boundingBox, nbrLoc)
        if (newBox.getMaxDimension <= maxHalfPathLength) {
          emptyNbrLocations +:= nbrLoc
          boundingBox = newBox
        }
      }
    }
    emptyNbrLocations
  }

  /**
    * @return the one or two neighbors that can be found by following the primary path.
    */
  private def findPrimaryPathNeighbors(previous: TilePlacement) = {
    var pathNbrs: List[TilePlacement] = List()
    for (i <- 0 until NUM_SIDES) {
      val color = previous.getPathColor(i)
      if (color == primaryColor) {
        val nbr = tantrix.getNeighbor(previous, i)
        if (nbr.isDefined && !visited.contains(nbr.get.location)) {
          val newBox = new Box(boundingBox, nbr.get.location)
          if (newBox.getMaxDimension < maxHalfPathLength) {
            pathNbrs +:= nbr.get
            visited += nbr.get.location
            boundingBox = newBox
          }
        }
      }
    }
    pathNbrs
  }
}
