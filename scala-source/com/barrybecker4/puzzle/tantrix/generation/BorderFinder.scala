// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.generation

import com.barrybecker4.common.geometry.{Box, Location}
import com.barrybecker4.puzzle.tantrix.model.HexTile.NUM_SIDES
import com.barrybecker4.puzzle.tantrix.model.PathColor
import com.barrybecker4.puzzle.tantrix.model._
import scala.collection.mutable

/**
  * Finds the set of moves on the border of the current 'tantrix'.
  * The 'tantrix' is the set of currently played consistent tiles.
  * The moves on the border will extend the primary path, but not such that
  * its width or height is more than half the length of the total finished path length.
  *
  * BFS carries `(TilePlacement, Box)` so bbox checks use the correct working box for each path
  * prefix. The previous implementation mutated a single global box while dequeuing tiles; that
  * could disagree with how far the path had actually grown in another branch of the walk.
  *
  * For each dequeued tile, empty primary neighbors are considered first (sides `0 .. NUM_SIDES-1`
  * in order), updating the working box; then primary path steps to occupied neighbors use the
  * same `getMaxDimension <= maxHalfPathLength` rule as empty slots (the old code used `<=` for
  * empty neighbors but `<` for stepping along existing tiles, which was asymmetric).
  *
  * Invariants covered by [[com.barrybecker4.puzzle.tantrix.generation.BorderFinderCorrectnessSuite]]:
  * every result cell is empty, touches the primary color from some placed tile, lies in the
  * naive "occupied bbox union that one hex" half-length cap, and borders grow monotonically when
  * `numTiles` (the cap) increases.
  *
  * @author Barry Becker
  */
class BorderFinder private[generation](var tantrix: Tantrix, val numTiles: Int, var primaryColor: PathColor) {
  private val maxHalfPathLength = (numTiles + 1) / 2

  /** Travel the primary path in both directions, adding all adjacent empty placements
    * as long as they do not push either boundingBox dimension beyond maxHalfPathLength.
    * @return list of legal next placements
    */
  private[generation] def findBorderPositions = {
    val positions: mutable.LinkedHashSet[Location] = mutable.LinkedHashSet.empty
    val initialBox = tantrix.getBoundingBox
    val queue = mutable.Queue((tantrix.lastTile, initialBox))
    val enqueued = mutable.Set((tantrix.lastTile.location, initialBox))

    while (queue.nonEmpty) {
      val (placement, box) = queue.dequeue()
      var workBox = box
      for (i <- 0 until NUM_SIDES) {
        val nbrLoc = HexUtil.getNeighborLocation(placement.location, i)
        if (tantrix(nbrLoc).isEmpty && placement.getPathColor(i) == primaryColor) {
          val newBox = Box(workBox, nbrLoc)
          if (newBox.getMaxDimension <= maxHalfPathLength) {
            positions += nbrLoc
            workBox = newBox
          }
        }
      }
      for (i <- 0 until NUM_SIDES) if (placement.getPathColor(i) == primaryColor) {
        tantrix.getNeighbor(Some(placement), i).foreach { nbr =>
          val newBox = Box(workBox, nbr.location)
          if (newBox.getMaxDimension <= maxHalfPathLength) {
            val key = (nbr.location, newBox)
            if (!enqueued.contains(key)) {
              enqueued += key
              queue.enqueue((nbr, newBox))
            }
          }
        }
      }
    }
    positions
  }
}
