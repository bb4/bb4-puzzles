// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.verification

import com.barrybecker4.common.geometry.{Box, ByteLocation, Location}
import com.barrybecker4.puzzle.tantrix.model.{HexTile, HexUtil, Tantrix}

import scala.collection.mutable

/**
  * Used to determine if a candidate solution has empty spaces within the tantrix.
  * A valid solution cannot have such spaces.
  * @param tantrix the tantrix state to test for solution.
  */
case class InnerSpaceDetector(tantrix: Tantrix) {

  /** Start with the empty positions that are on the border of the bbox, or that touch the outside of it.
    * Do a seed fill to visit all the spaces connected to those.
    * Finally, if there are any empty spaces inside the bbox that are not visited,
    * then there are inner spaces and it is not a valid solution.
    * @return the number of empty spaces that are enclosed by tiles. 0 means there are none.
    */
  def numInnerSpaces(): Int = {
    val bbox = tantrix.getBoundingBox
    val seedEmpties = findEmptyBorderPositions(bbox)
    val visited = findConnectedEmpties(seedEmpties, bbox)
    cellsIn(bbox).count(loc => isEmpty(loc) && !visited.contains(loc))
  }

  /**
    * Seeds for flood fill: empty cells that can reach the exterior of the bbox.
    * Includes the cells on the axis-aligned rectangle edges plus any empty hex in the bbox that has a
    * [[HexUtil]] neighbor strictly outside the bbox — needed on hex grids where every empty can lie
    * off the top/bottom/left/right lines of the bounding rectangle but still touch outside (otherwise
    * we would assert incorrectly and miss exterior-connected voids).
    */
  private def findEmptyBorderPositions(bbox: Box): Set[Location] =
    cellsIn(bbox)
      .filter(loc => isEmpty(loc) && (isOnEdge(loc, bbox) || hasNeighborOutside(loc, bbox)))
      .toSet

  /** @return all empty region connected to a set of seed positions */
  private def findConnectedEmpties(seedEmpties: Set[Location], bbox: Box): Set[Location] = {
    val visited = mutable.Set.from(seedEmpties)
    val searchQueue = mutable.Queue.from(seedEmpties)
    while (searchQueue.nonEmpty) {
      val newlyFound = findEmptyNeighborLocations(searchQueue.dequeue(), bbox).filter(visited.add)
      searchQueue ++= newlyFound
    }
    visited.toSet
  }

  /** @return all the empty neighbor positions next to the current one that are within the bbox. */
  private def findEmptyNeighborLocations(loc: Location, bbox: Box): List[Location] =
    neighborsOf(loc).filter(nbr => isEmpty(nbr) && bbox.contains(nbr)).toList

  private def isEmpty(loc: Location): Boolean = tantrix(loc).isEmpty

  private def neighborsOf(loc: Location): Seq[Location] =
    (0 until HexTile.NUM_SIDES).map(side => HexUtil.getNeighborLocation(loc, side))

  private def hasNeighborOutside(loc: Location, bbox: Box): Boolean =
    neighborsOf(loc).exists(nbr => !bbox.contains(nbr))

  private def isOnEdge(loc: Location, bbox: Box): Boolean =
    loc.row == bbox.getMinRow || loc.row == bbox.getMaxRow || loc.col == bbox.getMinCol || loc.col == bbox.getMaxCol

  /** @return every location in the bounding box, whether or not there is a tile there. */
  private def cellsIn(bbox: Box): Seq[Location] =
    for {
      row <- bbox.getMinRow to bbox.getMaxRow
      col <- bbox.getMinCol to bbox.getMaxCol
    } yield new ByteLocation(row, col)
}
