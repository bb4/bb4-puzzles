// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.generation

import com.barrybecker4.common.geometry.{IntLocation, Location}
import com.barrybecker4.puzzle.tantrix.model.HexTile.NUM_SIDES
import com.barrybecker4.puzzle.tantrix.model.{HexTile, HexUtil, PathColor, TantrixBoard, TilePlacement}
import LoopFeasibilityChecker.*

import scala.collection.mutable


object LoopFeasibilityChecker {

  /** An end of the primary path.
    * @param placement the tile at the end
    * @param exits the sides of the tile that the primary path leaves through that lead to an empty space.
    *              There is one unless the path is just a single tile, in which case there are two.
    */
  case class OpenEnd(placement: TilePlacement, exits: Seq[Int])

  /** @return each placement that has a primary path side leading to an empty space, along with those sides. */
  def findOpenEnds(board: TantrixBoard): Seq[OpenEnd] = {
    val tantrix = board.tantrix
    tantrix.tiles.iterator.flatMap { placement =>
      val (d1, d2) = primaryDirections(placement, board.primaryColor)
      val open = Seq(d1, d2).filter(d => tantrix(HexUtil.getNeighborLocation(placement.location, d)).isEmpty)
      Option.when(open.nonEmpty)(OpenEnd(placement, open))
    }.toSeq
  }

  /** The same as finding the ends of the board after the move is made, but faster since only the ends of the path
    * next to the move can change.
    * @param board the board before the move
    * @param endsBefore the open ends of the board before the move
    * @param move the placement to add to the board. It must extend the path from one of its ends.
    */
  def endsAfterPlacing(board: TantrixBoard, endsBefore: Seq[OpenEnd], move: TilePlacement): Seq[OpenEnd] = {
    val stillOpen = endsBefore.flatMap { end =>
      val exits = end.exits.filter(HexUtil.getNeighborLocation(end.placement.location, _) != move.location)
      Option.when(exits.nonEmpty)(OpenEnd(end.placement, exits))
    }
    val (d1, d2) = primaryDirections(move, board.primaryColor)
    val moveExits = Seq(d1, d2).filter(d => board.tantrix(HexUtil.getNeighborLocation(move.location, d)).isEmpty)
    if (moveExits.isEmpty) stillOpen else stillOpen :+ OpenEnd(move, moveExits)
  }

  /** @return the two sides of the placed tile that the primary path leaves through, in ascending order. */
  private[generation] def primaryDirections(placement: TilePlacement, primaryColor: PathColor): (Int, Int) = {
    var first = -1
    var second = -1
    var numFound = 0
    var side = 0
    while (side < NUM_SIDES) {
      if (placement.getPathColor(side) == primaryColor) {
        if (numFound == 0) first = side else if (numFound == 1) second = side
        numFound += 1
      }
      side += 1
    }
    assert(numFound == 2, s"Expected 2 primary path sides on $placement, but found $numFound")
    (first, second)
  }

  /** The tile turns the primary path by this many 60 degree steps. It is positive if the turn is to the left, and
    * negative if it is to the right, relative to the direction of travel.
    * Zero for a straight tile, 1 for the wide curve and 2 for the tight curve.
    * @param incoming side of the tile that the path comes in through
    * @param outgoing side of the tile that the path leaves through.
    */
  private[generation] def turn(incoming: Int, outgoing: Int): Int = {
    // The heading when we arrive is opposite to the side we came in on.
    val t = Math.floorMod(outgoing - incoming - NUM_SIDES / 2, NUM_SIDES)
    if (t > NUM_SIDES / 2) t - NUM_SIDES else t
  }

  /** How much a tile turns the primary path, ignoring which way it turns (0, 1 or 2).
    * The amount does not depend on how the tile is rotated, but which way it turns does.
    */
  private[generation] def turnMagnitude(tile: HexTile, primaryColor: PathColor): Int = {
    val sides = (0 until NUM_SIDES).filter(tile.edgeColors(_) == primaryColor)
    assert(sides.size == 2, s"Expected 2 primary path sides on $tile, but found $sides")
    val gap = sides(1) - sides.head
    Math.min(gap, NUM_SIDES - gap) match {
      case 1 => 2
      case 2 => 1
      case _ => 0
    }
  }

  /** The turns of a closed loop that does not cross itself add up to a full circle, which is 6 steps of 60 degrees. */
  private val FULL_CIRCLE = NUM_SIDES
}

/**
  * Quickly determines whether a partially built tantrix could still be completed to a solution.
  * It returns false only when it can show that no sequence of the remaining tiles can work.
  * A true result does not mean there is a solution. The checks are cheap compared to searching below the state,
  * so searches can skip whole branches. They are necessary conditions for a solution, in that every
  * solved puzzle passes at every step of the way that it is built up in.
  *
  * The tiles placed so far form a primary path with an open end at each side. The remaining tiles must form
  * a path between those two open ends so that it closes into a loop containing every tile.
  * Call the open cell that one end of the path leads to `e`, and the open cell that the other end leads to `x`.
  * Then, with `r` tiles remaining
  *
  *  - **Closure** The remaining tiles must be a path of `r` cells that starts at `e` and ends at `x`. That is not
  *    possible if `x` is more than `r - 1` steps from `e`. If `e` is `x`, there must be exactly one tile left.
  *  - **Turning** The path through the centers of the tiles of a loop that does not cross itself turns through
  *    exactly one full circle. The turns of the placed tiles are known. The remaining tiles each have a fixed
  *    amount of turn (0, 1 or 2 steps of 60 degrees) but can go either way. If the total cannot be made
  *    to equal one circle, the loop cannot be closed.
  *  - **Holes** A solution has no empty spaces inside it. The remaining tiles are a path of consecutive cells
  *    that begins at `e` (and ends at `x`), so they can never be placed in an enclosed space that does not contain `e`. Any such
  *    empty space will remain a hole. If `e` itself is in an enclosed space, then all of the remaining tiles
  *    have to fit and exactly fill it. Also, `x` has to be in the same empty space as `e`.
  *
  * @param board the state after a placement. The checks are the same from either end of the primary path.
  * @author Barry Becker
  */
class LoopFeasibilityChecker(board: TantrixBoard, openEnds: Seq[OpenEnd]) {

  /** @param board the state after a placement */
  def this(board: TantrixBoard) = this(board, LoopFeasibilityChecker.findOpenEnds(board))

  private val tantrix = board.tantrix
  private val color = board.primaryColor
  private val numRemaining = board.unplacedTiles.size

  /** @return false if there is no way that the remaining tiles can complete the tantrix to a solution. */
  def isFeasible: Boolean = {
    if (openEnds.isEmpty) numRemaining == 0 // closed loop. Nothing more can be added.
    else if (numRemaining == 0) false // not closed, and no more tiles to close it with.
    else if (tantrix.size == 1) {
      val tail = openEnds.head.placement
      val Seq(tailExit, otherExit) = openEnds.head.exits
      isFeasible(tail, tailExit, tail, otherExit)
    }
    else if (openEnds.size == 2) {
      // The checks are the same going around the loop in either direction, so it does not matter which end is the tail.
      val tailEnd = openEnds.find(_.placement.location == tantrix.lastTile.location).getOrElse(openEnds.head)
      val otherEnd = openEnds.find(_.placement.location != tailEnd.placement.location).get
      isFeasible(tailEnd.placement, tailEnd.exits.head, otherEnd.placement, otherEnd.exits.head)
    }
    else true // path is not what we expect, so do not claim anything
  }

  private def isFeasible(tail: TilePlacement, tailExit: Int, other: TilePlacement, otherExit: Int): Boolean = {
    val cellE = HexUtil.getNeighborLocation(tail.location, tailExit)
    val cellX = HexUtil.getNeighborLocation(other.location, otherExit)
    closureIsPossible(cellE, cellX) &&
      turningIsPossible(other, otherExit, tail, tailExit) &&
      emptySpacesAreOk(cellE, cellX)
  }

  /** There need to be enough tiles to get from the empty space the tail leads to, to the one the other end leads to. */
  private def closureIsPossible(cellE: Location, cellX: Location): Boolean =
    if (cellE == cellX) numRemaining == 1
    else HexUtil.hexDistance(cellE, cellX) <= numRemaining - 1

  /** Walk from the other end of the path to the tail, adding up the turns.
    * @param start the placement at the other end of the path
    * @param startExit the side of start that leads out of the path
    * @param tail the placement at the end of the path that the walk stops at
    * @param tailExit the side of the tail that leads out of the path
    */
  private def turningIsPossible(start: TilePlacement, startExit: Int, tail: TilePlacement, tailExit: Int): Boolean = {
    var current = start
    var incoming = startExit
    var placedTurns = 0
    var count = 1
    var ok = true
    while (ok && current.location != tail.location) {
      val (d1, d2) = primaryDirections(current, color)
      val outgoing = if (d1 == incoming) d2 else d1
      placedTurns += turn(incoming, outgoing)
      tantrix(HexUtil.getNeighborLocation(current.location, outgoing)) match {
        case Some(next) if count < tantrix.size =>
          current = next
          incoming = (outgoing + NUM_SIDES / 2) % NUM_SIDES
          count += 1
        case _ => ok = false
      }
    }
    if (!ok || count != tantrix.size) true // not a simple path, so do not claim anything
    else {
      placedTurns += turn(incoming, tailExit)
      canTurnToFullCircle(placedTurns)
    }
  }

  /** Each remaining tile contributes its amount of turn in either direction. See if some choice reaches a full circle. */
  private def canTurnToFullCircle(placedTurns: Int): Boolean = {
    val magnitudes = board.unplacedTiles.map(turnMagnitude(_, color))
    val numWide = magnitudes.count(_ == 1)
    val numTight = magnitudes.count(_ == 2)
    var possible = false
    var wide = -numWide
    while (!possible && wide <= numWide) {
      var tight = -numTight
      while (!possible && tight <= numTight) {
        possible = Math.abs(placedTurns + wide + 2 * tight) == FULL_CIRCLE
        tight += 2
      }
      wide += 2
    }
    possible
  }

  /** Label the empty spaces next to the placed tiles as either connected to the outside or enclosed. */
  private def emptySpacesAreOk(cellE: Location, cellX: Location): Boolean = {
    val box = tantrix.getBoundingBox
    val minRow = box.getMinRow - 1
    val minCol = box.getMinCol - 1
    val height = box.getMaxRow - box.getMinRow + 3
    val width = box.getMaxCol - box.getMinCol + 3
    val region = new Array[Int](height * width) // 0 = unvisited, 1 = outside, 2 = same space as cellE, -1 = tile
    for (placement <- tantrix.tiles)
      region((placement.location.row - minRow) * width + placement.location.col - minCol) = -1

    def index(loc: Location) = (loc.row - minRow) * width + loc.col - minCol
    def inGrid(loc: Location) =
      loc.row >= minRow && loc.row < minRow + height && loc.col >= minCol && loc.col < minCol + width

    def fill(start: Location, label: Int): Int = {
      var size = 0
      val queue = mutable.Queue(start)
      region(index(start)) = label
      while (queue.nonEmpty) {
        val loc = queue.dequeue()
        size += 1
        for (d <- 0 until NUM_SIDES) {
          val nbr = HexUtil.getNeighborLocation(loc, d)
          if (inGrid(nbr) && region(index(nbr)) == 0) {
            region(index(nbr)) = label
            queue.enqueue(nbr)
          }
        }
      }
      size
    }

    // The ring around the bounding box is all empty and all connected.
    fill(IntLocation(minRow, minCol), 1)
    val eIsOutside = region(index(cellE)) == 1
    val enclosedSize = if (eIsOutside) 0 else fill(cellE, 2)
    val sameSpace = region(index(cellX)) == region(index(cellE))
    val fillsExactly = eIsOutside || enclosedSize == numRemaining
    // Any empty space that is still unlabeled is enclosed and cannot be reached by the remaining tiles.
    sameSpace && fillsExactly && !region.contains(0)
  }
}
