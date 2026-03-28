// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.verification

import com.barrybecker4.common.geometry.{Box, ByteLocation, Location}
import com.barrybecker4.puzzle.tantrix.model.{HexTile, HexUtil, Tantrix}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * Used to determine if a candidate solution has empty spaces within the tantrix.
  * A valid solution cannot have such spaces.
  * @param tantrix the tantrix state to test for solution.
  * @author Barry Becker
  */
case class InnerSpaceDetector(tantrix: Tantrix) {

  /** Start with an empty position on the border of the bbox.
    * Do a seed fill to visit all the spaces connected to that.
    * Finally, if there are any empty spaces inside the bbox that are not visited,
    * then there are inner spaces and it is not a valid solution.
    * @return true if there are no inner empty spaces.
    */
  def numInnerSpaces(): Int = {
    val bbox = tantrix.getBoundingBox
    val seedEmpties = findEmptyBorderPositions(bbox)
    val visited = findConnectedEmpties(seedEmpties, bbox)
    numInternalEmpties(visited, bbox)
  }

  /**
    * Seeds for flood fill: empty cells that can reach the exterior of the bbox.
    * Includes axis-aligned rectangle edges (original) plus any empty hex in the bbox that has a
    * [[HexUtil]] neighbor strictly outside the bbox — needed on hex grids where every empty can lie
    * off the top/bottom/left/right lines of the bounding rectangle but still touch outside (otherwise
    * we would assert incorrectly and miss exterior-connected voids).
    */
  private def findEmptyBorderPositions(bbox: Box): Set[Location] = {
    val seeds = mutable.Set.empty[Location]
    for (i <- bbox.getMinCol to bbox.getMaxCol) {
      val top = new ByteLocation(bbox.getMinRow, i)
      if (tantrix(top).isEmpty) seeds += top
      val bottom = new ByteLocation(bbox.getMaxRow, i)
      if (tantrix(bottom).isEmpty) seeds += bottom
    }
    for (i <- bbox.getMinRow until bbox.getMaxRow) {
      val left = new ByteLocation(i, bbox.getMinCol)
      if (tantrix(left).isEmpty) seeds += left
      val right = new ByteLocation(i, bbox.getMaxCol)
      if (tantrix(right).isEmpty) seeds += right
    }
    for (r <- bbox.getMinRow to bbox.getMaxRow; c <- bbox.getMinCol to bbox.getMaxCol) {
      val loc = new ByteLocation(r, c)
      if (tantrix(loc).isEmpty) {
        var i = 0
        while (i < HexTile.NUM_SIDES) {
          val nbr = HexUtil.getNeighborLocation(loc, i)
          if (!bbox.contains(nbr)) seeds += loc
          i += 1
        }
      }
    }
    seeds.toSet
  }

  /** @return all empty region connected to a set of seed positions */
  private def findConnectedEmpties(seedEmpties: Set[Location], bbox: Box) = {
    var visited: Set[Location] = Set()
    val searchQueue: mutable.Queue[Location] = mutable.Queue()
    searchQueue ++= seedEmpties
    visited ++= seedEmpties
    while (searchQueue.nonEmpty) {
      val loc: Location = searchQueue.dequeue()
      val nbrEmpties = findEmptyNeighborLocations(loc, bbox)
      for (empty <- nbrEmpties) {
        if (!visited.contains(empty)) {
          visited += empty
          searchQueue.enqueue(empty)
        }
      }
    }
    visited
  }

  /** @return all the empty neighbor positions next to the current one. */
  private def findEmptyNeighborLocations(loc: Location, bbox: Box): List[Location] = {
    val buf = ListBuffer.empty[Location]
    for (i <- 0 until HexTile.NUM_SIDES) {
      val nbrLoc = HexUtil.getNeighborLocation(loc, i)
      if (tantrix(nbrLoc).isEmpty && bbox.contains(nbrLoc)) buf += nbrLoc
    }
    buf.toList
  }

  /** Count empty hexes inside the bbox that are not reached from border empty cells.
    * Rows run `minRow until maxRow` so the bottom bbox row is omitted here: those cells are on the border
    * and were seeded in [[findEmptyBorderPositions]], so any empty there is either a seed or was classified
    * via the side/corner border sweeps — not an enclosed cavity counted as "interior".
    *
    * @param visited set of visited empties
    */
  private def numInternalEmpties(visited: Set[Location], bbox: Box): Int = {
    val interiorRows = bbox.getMinRow until bbox.getMaxRow
    val interiorCols = bbox.getMinCol to bbox.getMaxCol
    var numEmpties = 0
    for (i <- interiorRows; j <- interiorCols) {
      val loc = new ByteLocation(i, j)
      if (tantrix(loc).isEmpty && !visited.contains(loc)) numEmpties += 1
    }
    numEmpties
  }
}
