// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.verification

import com.barrybecker4.common.geometry.{ByteLocation, Location}
import com.barrybecker4.puzzle.tantrix.model.{HexTile, HexUtil, Tantrix}

import scala.collection.mutable

/**
  * Used to determine if a candidate solution has empty spaces within the tantrix.
  * A valid solution cannot have such spaces.
  * @param tantrix the tantrix state to test for solution.
  * @author Barry Becker
  */
class InnerSpaceDetector(var tantrix: Tantrix) {

  /** Start with an empty position on the border of the bbox.
    * Do a seed fill to visit all the spaces connected to that.
    * Finally, if there are any empty spaces inside the bbox that are not visited,
    * then there are inner spaces and it is not a valid solution.
    * @return true if there are no inner empty spaces.
    */
  def hasInnerSpaces: Boolean = {
    val seedEmpties = findEmptyBorderPositions
    val visited = findConnectedEmpties(seedEmpties)
    !allEmptiesVisited(visited)
  }

  /** @return all the empty positions on the border  */
  private def findEmptyBorderPositions = {
    val bbox = tantrix.getBoundingBox
    var empties: Set[Location] = Set()
    for (i <- bbox.getMinCol to bbox.getMaxCol) {
      var loc = new ByteLocation(bbox.getMinRow, i) // top border
      if (tantrix(loc).isEmpty) empties += loc
      loc = new ByteLocation(bbox.getMaxRow, i)  // bottom border
      if (tantrix(loc).isEmpty) empties += loc
    }

    for (i <- bbox.getMinRow until bbox.getMaxRow) {
      var loc = new ByteLocation(i, bbox.getMinCol)  // left border
      if (tantrix(loc).isEmpty) empties += loc
      loc = new ByteLocation(i, bbox.getMaxCol)   // right border
      if (tantrix(loc).isEmpty) empties += loc
    }

    val totalLocs = (bbox.getHeight + 1) * (bbox.getWidth + 1)
    assert(totalLocs == tantrix.size || empties.nonEmpty,
      "We should have found at least one empty position on the border. Num Tiles = " + tantrix.size +
        " bbox area = " + bbox.getArea + " totalLocs = " + totalLocs + " numEmpties = " + empties.size)
    empties
  }

  /** @return all empty region connected to a set of seed positions */
  private def findConnectedEmpties(seedEmpties: Set[Location]) = {
    var visited: Set[Location] = Set()
    val searchQueue: mutable.Queue[Location] = mutable.Queue()
    searchQueue ++= seedEmpties
    visited ++= seedEmpties
    while (searchQueue.nonEmpty) {
      val loc: Location = searchQueue.dequeue()
      val nbrEmpties = findEmptyNeighborLocations(loc)
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
  private def findEmptyNeighborLocations(loc: Location) = {
    var emptyNbrLocations: List[Location] = List()
    val bbox = tantrix.getBoundingBox

    for (i <- 0 until HexTile.NUM_SIDES) {
      val nbrLoc = HexUtil.getNeighborLocation(loc, i)
      if (tantrix(nbrLoc).isEmpty && bbox.contains(nbrLoc)) emptyNbrLocations +:= nbrLoc
    }
    emptyNbrLocations
  }

  /** @param visited set of visited empties.
    * @return true if any empties in the tantrix bbox are not visited
    */
  private def allEmptiesVisited(visited: Set[Location]): Boolean = {
    val bbox = tantrix.getBoundingBox
    for (i <- bbox.getMinRow until bbox.getMaxRow) {
      for (j <- bbox.getMinCol to bbox.getMaxCol) {
        val loc = new ByteLocation(i, j)
        if (tantrix(loc).isEmpty && !visited.contains(loc))
          return false
      }
    }
    true
  }
}
