// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.generation

import com.barrybecker4.common.geometry.Location
import com.barrybecker4.puzzle.tantrix.model.HexTile.NUM_SIDES
import com.barrybecker4.puzzle.tantrix.model.{HexTile, HexUtil, PathColor, Tantrix, TantrixBoard, TilePlacement}
import LoopFeasibilityChecker.*


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
      val d1 = primaryDirection(placement, board.primaryColor, 0)
      val d2 = primaryDirection(placement, board.primaryColor, 1)
      val open = emptyExits(tantrix, placement.location, d1, d2)
      Option.when(open.nonEmpty)(OpenEnd(placement, open))
    }.toSeq
  }

  /** @return those of the two sides that lead to an empty space */
  private def emptyExits(tantrix: Tantrix, location: Location, side1: Int, side2: Int): List[Int] = {
    val empty1 = tantrix(HexUtil.getNeighborLocation(location, side1)).isEmpty
    val empty2 = tantrix(HexUtil.getNeighborLocation(location, side2)).isEmpty
    if (empty1) { if (empty2) List(side1, side2) else List(side1) }
    else if (empty2) List(side2)
    else Nil
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
    val d1 = primaryDirection(move, board.primaryColor, 0)
    val d2 = primaryDirection(move, board.primaryColor, 1)
    val moveExits = emptyExits(board.tantrix, move.location, d1, d2)
    if (moveExits.isEmpty) stillOpen else stillOpen :+ OpenEnd(move, moveExits)
  }

  /** @return the end that the walk of the path stops at (the one the last tile was placed at, if it is an end), and the
    *         end that it starts from. This is only for paths with two ends that have more than one tile.
    */
  private[generation] def orientEnds(board: TantrixBoard, openEnds: Seq[OpenEnd]): Option[(OpenEnd, OpenEnd)] =
    if (openEnds.size != 2 || board.tantrix.size == 1) None
    else {
      val tail = openEnds.find(_.placement.location == board.tantrix.lastTile.location).getOrElse(openEnds.head)
      val other = openEnds.find(_.placement.location != tail.placement.location).get
      Some((tail, other))
    }

  /** Add up the turns of the tiles of the path, walking from the start to the tail. The sum changes sign if the path is
    * walked the other way. A tile turns the path to the left or right by the amount given by [[turn]].
    * @param start the placement at the end of the path that the walk starts from
    * @param startExit the side of start that leads out of the path
    * @param tail the placement at the end of the path that the walk stops at
    * @param tailExit the side of the tail that leads out of the path
    * @return None if the tiles are not a simple path from start to tail.
    */
  private[generation] def walkTurns(board: TantrixBoard, start: TilePlacement, startExit: Int,
                                    tail: TilePlacement, tailExit: Int): Option[Int] = {
    val tantrix = board.tantrix
    var current = start
    var incoming = startExit
    var turns = 0
    var count = 1
    var ok = true
    while (ok && current.location != tail.location) {
      val d1 = primaryDirection(current, board.primaryColor, 0)
      val d2 = primaryDirection(current, board.primaryColor, 1)
      val outgoing = if (d1 == incoming) d2 else d1
      turns += turn(incoming, outgoing)
      tantrix(HexUtil.getNeighborLocation(current.location, outgoing)) match {
        case Some(next) if count < tantrix.size =>
          current = next
          incoming = (outgoing + NUM_SIDES / 2) % NUM_SIDES
          count += 1
        case _ => ok = false
      }
    }
    if (!ok || count != tantrix.size) None
    else Some(turns + turn(incoming, tailExit))
  }

  /** @return the sum of the turns of the tiles of the path, in one of the two directions it can be walked in.
    *         This is None if the path does not have two ends or only has one tile.
    */
  def turnsOfPath(board: TantrixBoard, openEnds: Seq[OpenEnd]): Option[Int] =
    orientEnds(board, openEnds).flatMap { case (tail, other) =>
      walkTurns(board, other.placement, other.exits.head, tail.placement, tail.exits.head)
    }

  /** The same as [[turnsOfPath]] for the board after the move is made, but without walking the path again.
    * Adding a tile only adds its own turn, because the turns of all the other tiles stay as they were.
    * The direction of the path is the same before and after, so the result is on the same footing as `turnsBefore`
    * (the sign does not matter to the feasibility check).
    * @param board the board before the move
    * @param endsBefore the open ends of the board before the move
    * @param turnsBefore the sum of the turns of the board before the move, from [[turnsOfPath]]
    * @param move the placement to add to the board. It must extend the path from one of its ends.
    * @return None if it can't be told where on the path the move is made
    */
  def turnsAfterPlacing(board: TantrixBoard, endsBefore: Seq[OpenEnd], turnsBefore: Int, move: TilePlacement): Option[Int] =
    orientEnds(board, endsBefore).flatMap { case (tail, other) =>
      val atTail = HexUtil.getNeighborLocation(tail.placement.location, tail.exits.head) == move.location
      val atOther = HexUtil.getNeighborLocation(other.placement.location, other.exits.head) == move.location
      if (atTail == atOther) None // not at an end, or at both of them, in which case it closes the loop
      else {
        val d1 = primaryDirection(move, board.primaryColor, 0)
        val d2 = primaryDirection(move, board.primaryColor, 1)
        val anchor = if (atTail) tail.placement else other.placement
        val towardPath = directionTo(move.location, anchor.location)
        val away = if (d1 == towardPath) d2 else d1
        // the path arrives at the move from the tail, but leaves the move toward the start if the move is at the start
        Some(turnsBefore + (if (atTail) turn(towardPath, away) else turn(away, towardPath)))
      }
    }

  /** Taking an empty cell away from an empty space leaves what is left of it in one piece if the empty cells next to it
    * are in one run, going around it. That is because each of them is next to the ones on either side of it in the
    * run, so anything that went through the cell can go around it.
    * @param board the board before a tile is placed at the location
    * @param location an empty location
    * @return true if filling the location cannot split the empty space that it is part of (or leave it empty)
    */
  def keepsEmptySpaceTogether(board: TantrixBoard, location: Location): Boolean = {
    var empty = 0 // a bit for each side that leads to an empty location
    var side = 0
    while (side < NUM_SIDES) {
      if (board.tantrix(HexUtil.getNeighborLocation(location, side)).isEmpty) empty |= 1 << side
      side += 1
    }
    // the number of runs of empty sides is the number of empty sides that follow a side that is not empty
    val rotated = ((empty << 1) | (empty >> (NUM_SIDES - 1))) & ((1 << NUM_SIDES) - 1) // side i-1 moved to side i
    Integer.bitCount(empty & ~rotated) == 1
  }

  /** @return the side of the first location that the second location is next to. */
  private def directionTo(from: Location, to: Location): Int = {
    var side = 0
    while (side < NUM_SIDES && HexUtil.getNeighborLocation(from, side) != to) side += 1
    assert(side < NUM_SIDES, s"$to is not next to $from")
    side
  }

  /** @param which 0 for the first side of the placed tile that the primary path leaves through, and 1 for the second
    *              (the sides are in ascending order). This does not allocate, since it is done for every move.
    */
  private[generation] def primaryDirection(placement: TilePlacement, primaryColor: PathColor, which: Int): Int = {
    var numFound = 0
    var side = 0
    while (side < NUM_SIDES) {
      if (placement.getPathColor(side) == primaryColor) {
        if (numFound == which) return side
        numFound += 1
      }
      side += 1
    }
    throw new AssertionError(s"Expected 2 primary path sides on $placement, but found $numFound")
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
    var first = -1
    var second = -1
    var numFound = 0
    var side = 0
    while (side < NUM_SIDES) {
      if (tile.edgeColors(side) == primaryColor) {
        if (numFound == 0) first = side else if (numFound == 1) second = side
        numFound += 1
      }
      side += 1
    }
    assert(numFound == 2, s"Expected 2 primary path sides on $tile, but found $numFound")
    val gap = second - first
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
  * @param openEnds the ends of the primary path of the board
  * @param knownTurns the sum of the turns of the tiles of the path, if it is already known (see
  *                   [[LoopFeasibilityChecker.turnsAfterPlacing]]). It saves walking the path to find it. Its sign does not matter.
  * @param emptySpacesKnownToBeOk true if it is already known that the empty spaces pass their check (see
  *                   [[LoopFeasibilityChecker.keepsEmptySpaceTogether]]), which saves doing that check.
  */
class LoopFeasibilityChecker(board: TantrixBoard, openEnds: Seq[OpenEnd], knownTurns: Option[Int] = None,
                             emptySpacesKnownToBeOk: Boolean = false) {

  /** @param board the state after a placement */
  def this(board: TantrixBoard) = this(board, LoopFeasibilityChecker.findOpenEnds(board), None, false)

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
    else orientEnds(board, openEnds) match {
      // The checks are the same going around the loop in either direction, so it does not matter which end is the tail.
      case Some((tailEnd, otherEnd)) =>
        isFeasible(tailEnd.placement, tailEnd.exits.head, otherEnd.placement, otherEnd.exits.head)
      case None => true // path is not what we expect, so do not claim anything
    }
  }

  /** The two empty spaces that the ends of the path lead to, if the path is not closed. */
  lazy val endCells: Option[(Location, Location)] =
    if (openEnds.isEmpty || numRemaining == 0) None
    else if (tantrix.size == 1) {
      val Seq(exit1, exit2) = openEnds.head.exits
      val loc = openEnds.head.placement.location
      Some((HexUtil.getNeighborLocation(loc, exit1), HexUtil.getNeighborLocation(loc, exit2)))
    }
    else orientEnds(board, openEnds).map { case (tail, other) =>
      (HexUtil.getNeighborLocation(tail.placement.location, tail.exits.head),
        HexUtil.getNeighborLocation(other.placement.location, other.exits.head))
    }

  /** @return true if the empty spaces are such that the remaining tiles can fill the one the path leads to, and no
    *         other is enclosed. This is false if the path does not have two ends to lead to empty spaces.
    */
  lazy val emptySpacesAreValid: Boolean = endCells.exists { case (cellE, cellX) => emptySpacesAreOk(cellE, cellX) }

  private def isFeasible(tail: TilePlacement, tailExit: Int, other: TilePlacement, otherExit: Int): Boolean = {
    val cellE = HexUtil.getNeighborLocation(tail.location, tailExit)
    val cellX = HexUtil.getNeighborLocation(other.location, otherExit)
    closureIsPossible(cellE, cellX) &&
      turningIsPossible(other, otherExit, tail, tailExit) &&
      (emptySpacesKnownToBeOk || emptySpacesAreOk(cellE, cellX))
  }

  /** There need to be enough tiles to get from the empty space the tail leads to, to the one the other end leads to. */
  private def closureIsPossible(cellE: Location, cellX: Location): Boolean =
    if (cellE == cellX) numRemaining == 1
    else HexUtil.hexDistance(cellE, cellX) <= numRemaining - 1

  /** Add up the turns from the other end of the path to the tail, unless that is already known.
    * @param start the placement at the other end of the path
    * @param startExit the side of start that leads out of the path
    * @param tail the placement at the end of the path that the walk stops at
    * @param tailExit the side of the tail that leads out of the path
    */
  private def turningIsPossible(start: TilePlacement, startExit: Int, tail: TilePlacement, tailExit: Int): Boolean =
    knownTurns.orElse(walkTurns(board, start, startExit, tail, tailExit)) match {
      case Some(placedTurns) => canTurnToFullCircle(placedTurns)
      case None => true // not a simple path, so do not claim anything
    }

  /** Each remaining tile contributes its amount of turn in either direction. See if some choice reaches a full circle. */
  private def canTurnToFullCircle(placedTurns: Int): Boolean = {
    var numWide = 0
    var numTight = 0
    board.unplacedTiles.foreach { tile =>
      turnMagnitude(tile, color) match {
        case 1 => numWide += 1
        case 2 => numTight += 1
        case _ =>
      }
    }
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

  /** Label the empty spaces next to the placed tiles as either connected to the outside or enclosed.
    * This is the most expensive check and it is done for every move that is tried, so it works on arrays of
    * ints that are indexed by the position in a grid that covers the tiles and a ring of empty space around them.
    */
  private def emptySpacesAreOk(cellE: Location, cellX: Location): Boolean = {
    val box = tantrix.getBoundingBox
    val minRow = box.getMinRow - 1
    val minCol = box.getMinCol - 1
    val height = box.getMaxRow - box.getMinRow + 3
    val width = box.getMaxCol - box.getMinCol + 3
    val labeler = new EmptySpaceLabeler(minRow, minCol, height, width)
    tantrix.tiles.foreach(placement => labeler.markTile(placement.location))

    // The ring around the bounding box is all empty and all connected.
    val outsideSize = labeler.fill(0, EmptySpaceLabeler.OUTSIDE)
    val indexE = labeler.indexOf(cellE)
    val eIsOutside = labeler.labelAt(indexE) == EmptySpaceLabeler.OUTSIDE
    val enclosedSize = if (eIsOutside) 0 else labeler.fill(indexE, EmptySpaceLabeler.WITH_E)
    val sameSpace = labeler.labelAt(labeler.indexOf(cellX)) == labeler.labelAt(indexE)
    val fillsExactly = eIsOutside || enclosedSize == numRemaining
    // Any empty space that is not labeled is enclosed and cannot be reached by the remaining tiles.
    sameSpace && fillsExactly && outsideSize + enclosedSize == height * width - tantrix.size
  }
}

private object EmptySpaceLabeler {
  val OUTSIDE = 1
  val WITH_E = 2
  private val TILE = -1
}

/** Labels the connected empty spaces of a grid of hex cells in offset coordinates, where odd rows are shifted back by
  * half a cell (see [[HexUtil.getNeighborLocation]]).
  * @param minRow row of the first row of the grid
  * @param minCol column of the first column of the grid
  */
private class EmptySpaceLabeler(minRow: Int, minCol: Int, height: Int, width: Int) {
  import EmptySpaceLabeler.*

  private val label = new Array[Int](height * width) // 0 = unlabeled
  private val queue = new Array[Int](height * width)
  private var tail = 0

  def indexOf(loc: Location): Int = (loc.row - minRow) * width + loc.col - minCol
  def labelAt(index: Int): Int = label(index)
  def markTile(loc: Location): Unit = label(indexOf(loc)) = TILE

  /** Label every empty cell that can be reached from the start cell.
    * @return the number of cells labeled.
    */
  def fill(start: Int, newLabel: Int): Int = {
    var head = 0
    tail = 0
    label(start) = newLabel
    queue(tail) = start
    tail += 1
    while (head < tail) {
      val index = queue(head)
      head += 1
      val row = index / width
      val col = index - row * width
      val shift = if (((minRow + row) & 1) == 1) -1 else 0
      visit(row, col + 1, newLabel)
      visit(row - 1, col + shift + 1, newLabel)
      visit(row - 1, col + shift, newLabel)
      visit(row, col - 1, newLabel)
      visit(row + 1, col + shift, newLabel)
      visit(row + 1, col + shift + 1, newLabel)
    }
    tail
  }

  private def visit(row: Int, col: Int, newLabel: Int): Unit =
    if (row >= 0 && row < height && col >= 0 && col < width) {
      val index = row * width + col
      if (label(index) == 0) {
        label(index) = newLabel
        queue(tail) = index
        tail += 1
      }
    }
}
