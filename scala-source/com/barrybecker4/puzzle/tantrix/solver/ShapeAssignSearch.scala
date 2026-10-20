// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver

import com.barrybecker4.common.geometry.{ByteLocation, Location}
import com.barrybecker4.puzzle.tantrix.model.{HexTile, PathColor, Rotation, TilePlacement}
import ShapeAssignSearch.*

import scala.util.Random

object ShapeAssignSearch {

  /** The domains of the cells are bit sets over (tile, rotation) pairs, with two bits per tile, so they fit in a long. */
  val MAX_TILES = 32

  /** Number of nodes the labeling of one finished shape is allowed before giving up on that shape. */
  private val LABELING_BUDGET = 3000

  /** The offsets of the six neighbors in axial coordinates, indexed by side. These agree with
    * [[com.barrybecker4.puzzle.tantrix.model.HexUtil.getNeighborLocation]].
    */
  private val DQ = Array(1, 1, 0, -1, -1, 0)
  private val DR = Array(0, -1, -1, 0, 1, 1)

  private val EVEN_BITS = 0x5555555555555555L

  /** @return true if every tile has a path of the color, which every tile of a loop of that color needs. */
  def allHavePath(tiles: Seq[HexTile], color: PathColor): Boolean =
    tiles.forall(tile => (0 until 6).count(i => tile.edgeColors(i) == color) == 2)

  /** How a search attempt ended. */
  sealed trait Outcome
  case class Found(moves: Seq[TilePlacement]) extends Outcome
  /** The whole space was searched and there is no solution. */
  case object Exhausted extends Outcome
  /** The step limit was reached. */
  case object OutOfSteps extends Outcome

  /** Tiles turn the primary path in steps of 60 degrees: 0 for a straight tile, 1 for a wide curve, and 2 for a tight one. */
  def turnMagnitude(d: Int): Int = d match {
    case 3 => 0
    case 2 | 4 => 1
    case _ => 2
  }
}

/**
  * Finds a solution by building the shape of the loop first and choosing the tiles for it second, while letting each
  * of those decisions prune the other.
  *
  * Every solution is a loop of the primary color through all of the tiles, where no empty space is enclosed. The shape
  * is built one cell at a time around the loop, starting from the first tile. The shape alone says what kind of
  * primary path (straight, wide curve, or tight curve) each cell needs, so only the tiles of that kind can go there, and the
  * numbers of cells of each kind used must match the numbers of tiles of each kind. The sides of a cell that touch cells
  * other than the ones before and after it in the loop must have the same colors as the sides they touch. That is
  * where the difficulty is, and it is the reason this does not choose a tile for a cell when it adds the cell.
  * Every cell keeps the set of all the tiles (and rotations) that are still possible for it, and
  * the sets are reduced each time the shape touches itself in a new place. They are also reduced when a cell is left
  * with a single tile, since no other cell can use that. Shapes where some cell has no tile left, or where
  * the cells can not all get different tiles, are abandoned long before they are complete. When the shape is complete, the
  * tiles are chosen by a small search over what is left.
  *
  * The first tile stays where it is, so the loop is built from it in the direction of its first primary path side.
  * Because a loop can be walked in either direction, that loses no solutions.
  *
  * @param tiles the tiles to use, where the first is already placed at the start location.
  * @param primaryColor color of the loop
  * @param start the placement of the first tile
  * @param rnd source of randomness for the order in which the shapes are tried
  * @param onProgress called now and then with the number of steps taken in the attempt. It may throw to stop the search.
  */
class ShapeAssignSearch(tiles: IndexedSeq[HexTile], primaryColor: PathColor, start: TilePlacement,
                        rnd: Random, onProgress: Long => Unit = _ => ()) {

  private val n = tiles.size
  require(n >= 3 && n <= MAX_TILES, s"Need between 3 and $MAX_TILES tiles, but got $n")

  private val width = 2 * n + 7
  private val off = n + 3
  private val delta = (0 until 6).map(d => DR(d) * width + DQ(d)).toArray

  // ---- what the tiles look like
  private val primaryEdges: Array[Array[Int]] = tiles.map { tile =>
    val edges = (0 until 6).filter(i => tile.edgeColors(i) == primaryColor).toArray
    require(edges.length == 2, s"Tile $tile does not have a path of color $primaryColor")
    edges
  }.toArray
  private val tileKind: Array[Int] = primaryEdges.map(e => turnMagnitude(Math.floorMod(e(1) - e(0), 6)))
  private val tileColor: Array[Array[Int]] = tiles.map(t => (0 until 6).map(i => t.edgeColors(i).ordinal).toArray).toArray

  // ---- the start
  private val startRotation = start.rotation.ordinal
  private val exit0 = (primaryEdges(0)(0) + startRotation) % 6
  private val entry0 = (primaryEdges(0)(1) + startRotation) % 6
  private val startHeading = Math.floorMod(exit0 - entry0, 6) - 3
  /** Where the last cell of the loop is. It is next to the first cell, on the side that the loop comes back in on. */
  private val origin = off * width + off
  private val lastPos = origin + delta(entry0)
  /** The side of the last cell that leads back to the first cell. */
  private val lastExit = (entry0 + 3) % 6

  // ---- the state of the search
  private val pos = new Array[Int](n)
  private val entry = new Array[Int](n)
  private val exit = new Array[Int](n)
  private val occupant = Array.fill(width * width)(-1)
  /** domains(i) are the domains of all the cells after cell i has been fixed. */
  private val domains = Array.ofDim[Long](n, n)
  private val rotationOf = Array.ofDim[Byte](n, 64)
  /** The color of each side of every possible placement of the cell, 3 bits per side. */
  private val sideColors = Array.ofDim[Int](n, 64)
  private val kindsLeft = new Array[Int](3)
  private val contactA, contactB, contactSideA, contactSideB = new Array[Int](8 * n)
  private var numContacts = 0
  private var steps = 0L
  private var stepLimit = 0L
  private var aborted = false
  private var solution: Option[Seq[TilePlacement]] = None
  private val labelGrid = new Array[Int](width * width)
  private val floodStack = new Array[Int](width * width)

  /** Make one attempt. The cells are tried in a random order, so another attempt can end differently.
    * @param maxSteps number of cells that can be added before the attempt is ended
    */
  def run(maxSteps: Long): Outcome = {
    stepLimit = maxSteps
    steps = 0
    aborted = false
    solution = None
    java.util.Arrays.fill(kindsLeft, 0)
    for (t <- 1 until n) kindsLeft(tileKind(t)) += 1

    pos(0) = origin
    entry(0) = entry0
    exit(0) = exit0
    occupant(origin) = 0
    numContacts = 0
    rotationOf(0)(0) = startRotation.toByte
    sideColors(0)(0) = packColors(0, startRotation)
    java.util.Arrays.fill(domains(0), 0L)
    domains(0)(0) = 1L

    val next = origin + delta(exit0)
    pos(1) = next
    occupant(next) = 1
    entry(1) = (exit0 + 3) % 6
    val found = extend(1, startHeading)
    occupant(next) = -1
    occupant(origin) = -1
    if (found) Found(solution.get)
    else if (aborted) OutOfSteps
    else Exhausted
  }

  def numSteps: Long = steps

  private def packColors(tile: Int, rotation: Int): Int = {
    var packed = 0
    for (side <- 0 until 6) packed |= tileColor(tile)(Math.floorMod(side - rotation, 6)) << (3 * side)
    packed
  }

  private def colorAt(cell: Int, value: Int, side: Int): Int = (sideColors(cell)(value) >> (3 * side)) & 7

  /** @param i the cell that has been positioned, with the side that it is entered by, but not yet its exit
    * @param headingSoFar the sum of turns of the cells before i, in steps of 60 degrees
    * @return true if a solution was found
    */
  private def extend(i: Int, headingSoFar: Int): Boolean = {
    if (steps >= stepLimit) {
      aborted = true
      return false
    }
    steps += 1
    if ((steps & 255L) == 0L) onProgress(steps)
    val e = entry(i)
    val exits = if (i == n - 1) Array(lastExit) else rnd.shuffle(Seq(0, 1, 2, 3, 4, 5).filter(_ != e)).toArray
    var found = false
    var k = 0
    while (!found && !aborted && k < exits.length) {
      val x = exits(k)
      k += 1
      val d = Math.floorMod(x - e, 6)
      val kind = turnMagnitude(d)
      if (kindsLeft(kind) > 0) {
        kindsLeft(kind) -= 1
        val heading = headingSoFar + d - 3
        if (shapeIsPossible(i, x, heading)) {
          val contactsBefore = numContacts
          if (fixCell(i, x, kind)) {
            if (i == n - 1) found = finish()
            else {
              val next = pos(i) + delta(x)
              pos(i + 1) = next
              occupant(next) = i + 1
              entry(i + 1) = (x + 3) % 6
              found = extend(i + 1, heading)
              occupant(next) = -1
            }
          }
          numContacts = contactsBefore
        }
        kindsLeft(kind) += 1
      }
    }
    found
  }

  /** @return false if the shape can not be completed to a loop with the cells that are left, whatever tiles are used. */
  private def shapeIsPossible(i: Int, x: Int, heading: Int): Boolean = {
    val remaining = n - 1 - i
    if (remaining == 0) Math.abs(heading) == 6
    else {
      val next = pos(i) + delta(x)
      if (occupant(next) != -1) false
      else if (next == lastPos) {
        // the next cell must be the last
        remaining == 1 && (turnsCanReach(6 - heading) || turnsCanReach(-6 - heading))
      }
      else remaining > 1 &&
        hexDistance(next, lastPos) <= remaining - 1 &&
        (turnsCanReach(6 - heading) || turnsCanReach(-6 - heading))
    }
  }

  /** Each of the cells that is left turns the loop by 2, 1 or 0 steps (tight, wide or straight), to the left or the right. */
  private def turnsCanReach(amount: Int): Boolean = {
    val tight = kindsLeft(2)
    val wide = kindsLeft(1)
    var m = 0
    var possible = false
    while (!possible && m <= wide) {
      val rest = amount - (wide - 2 * m)
      possible = Math.abs(rest) <= 2 * tight && Math.floorMod(2 * tight - rest, 4) == 0
      m += 1
    }
    possible
  }

  private def hexDistance(a: Int, b: Int): Int = {
    val dq = (a % width) - (b % width)
    val dr = (a / width) - (b / width)
    Math.max(Math.abs(dq), Math.max(Math.abs(dr), Math.abs(dq + dr)))
  }

  /** Give cell i its exit, work out which tiles could go there, and reduce the possibilities of all the cells because of it.
    * @return false if that leaves some cell with nothing, or if the cells that are left can not all have different tiles.
    */
  private def fixCell(i: Int, x: Int, kind: Int): Boolean = {
    exit(i) = x
    val e = entry(i)
    var mask = 0L
    var t = 1
    while (t < n) {
      if (tileKind(t) == kind) {
        var count = 0
        for (r <- 0 until 6) {
          val a = (primaryEdges(t)(0) + r) % 6
          val b = (primaryEdges(t)(1) + r) % 6
          if ((a == e && b == x) || (a == x && b == e)) {
            val value = 2 * t + count
            count += 1
            mask |= 1L << value
            rotationOf(i)(value) = r.toByte
            sideColors(i)(value) = packColors(t, r)
          }
        }
      }
      t += 1
    }
    val dom = domains(i)
    System.arraycopy(domains(i - 1), 0, dom, 0, n)
    dom(i) = mask

    // the sides that touch cells other than the ones before and after in the loop
    var side = 0
    while (side < 6) {
      if (side != e && side != x) {
        val j = occupant(pos(i) + delta(side))
        if (j >= 0 && j < i) {
          contactA(numContacts) = i
          contactSideA(numContacts) = side
          contactB(numContacts) = j
          contactSideB(numContacts) = (side + 3) % 6
          numContacts += 1
        }
      }
      side += 1
    }
    propagate(dom) && hasDistinctTiles(dom, i) && (if (i == n - 1) true else emptySpacesAreOk(i))
  }

  /** Remove the placements of cells that do not have a matching placement in each of the cells that they touch,
    * and remove a tile from all the other cells when a cell can only have that tile. Repeat until nothing changes.
    * @return false if some cell has no placements left.
    */
  private def propagate(dom: Array[Long]): Boolean = {
    var changed = true
    while (changed) {
      changed = false
      var c = 0
      while (c < numContacts) {
        val a = contactA(c)
        val b = contactB(c)
        val newA = restrict(dom, a, contactSideA(c), b, contactSideB(c))
        if (newA == 0L) return false
        if (newA != dom(a)) { dom(a) = newA; changed = true }
        val newB = restrict(dom, b, contactSideB(c), a, contactSideA(c))
        if (newB == 0L) return false
        if (newB != dom(b)) { dom(b) = newB; changed = true }
        c += 1
      }
      var cell = 1
      while (cell < n) {
        val tilesOfCell = tileMask(dom(cell))
        if (tilesOfCell != 0L && (tilesOfCell & (tilesOfCell - 1)) == 0L) { // exactly one tile is possible
          val both = 3L << java.lang.Long.numberOfTrailingZeros(tilesOfCell)
          var other = 1
          while (other < n) {
            if (other != cell && (dom(other) & both) != 0L) {
              dom(other) &= ~both
              if (dom(other) == 0L) return false
              changed = true
            }
            other += 1
          }
        }
        cell += 1
      }
    }
    true
  }

  /** @return the placements of cell a that have, on the side that touches b, a color that some placement of b has there. */
  private def restrict(dom: Array[Long], a: Int, sideA: Int, b: Int, sideB: Int): Long = {
    var present = 0
    var m = dom(b)
    while (m != 0L) {
      present |= 1 << colorAt(b, java.lang.Long.numberOfTrailingZeros(m), sideB)
      m &= m - 1
    }
    var result = 0L
    m = dom(a)
    while (m != 0L) {
      val value = java.lang.Long.numberOfTrailingZeros(m)
      if ((present & (1 << colorAt(a, value, sideA))) != 0) result |= 1L << value
      m &= m - 1
    }
    result
  }

  /** One bit (the even one) for each tile that has a placement in the domain. */
  private def tileMask(domain: Long): Long = (domain | (domain >>> 1)) & EVEN_BITS

  private var visitedTiles = 0L
  private val tileOwner = new Array[Int](MAX_TILES)

  /** @return true if every cell up to the given one can be given a different tile. */
  private def hasDistinctTiles(dom: Array[Long], upTo: Int): Boolean = {
    java.util.Arrays.fill(tileOwner, -1)
    var cell = 1
    while (cell <= upTo) {
      visitedTiles = 0L
      if (!assignTile(dom, cell)) return false
      cell += 1
    }
    true
  }

  private def assignTile(dom: Array[Long], cell: Int): Boolean = {
    var m = tileMask(dom(cell))
    while (m != 0L) {
      val bit = java.lang.Long.lowestOneBit(m)
      m &= m - 1
      if ((visitedTiles & bit) == 0L) {
        visitedTiles |= bit
        val t = java.lang.Long.numberOfTrailingZeros(bit) >> 1
        if (tileOwner(t) < 0 || assignTile(dom, tileOwner(t))) {
          tileOwner(t) = cell
          return true
        }
      }
    }
    false
  }

  private def qOf(index: Int): Int = index % width - off
  private def rOf(index: Int): Int = index / width - off

  /** After cell i, the cell that the loop leads to next is empty, and the remaining cells must be able to fill the empty
    * space that it is in, up to the last cell. No empty cell may be enclosed by the loop so far where the rest of the
    * loop can not get to it. This is the same check that is used for the search that places a tile at a time.
    */
  private def emptySpacesAreOk(i: Int): Boolean = {
    val next = pos(i) + delta(exit(i))
    val numRemaining = n - 1 - i
    val (minQ, maxQ, minR, maxR) = bounds(i)
    val boxWidth = maxQ - minQ + 3
    val boxHeight = maxR - minR + 3
    val size = boxWidth * boxHeight
    java.util.Arrays.fill(labelGrid, 0, size, 0)
    for (c <- 0 to i) labelGrid(localIndex(pos(c), minQ, minR, boxWidth)) = -1
    val outsideSize = fill(0, 1, boxWidth, boxHeight)
    val indexE = localIndex(next, minQ, minR, boxWidth)
    val eIsOutside = labelGrid(indexE) == 1
    val enclosedSize = if (eIsOutside) 0 else fill(indexE, 2, boxWidth, boxHeight)
    val sameSpace = labelGrid(localIndex(lastPos, minQ, minR, boxWidth)) == labelGrid(indexE)
    val fillsExactly = eIsOutside || enclosedSize == numRemaining
    sameSpace && fillsExactly && outsideSize + enclosedSize == size - (i + 1)
  }

  private def bounds(i: Int): (Int, Int, Int, Int) = {
    var minQ, minR = Int.MaxValue
    var maxQ, maxR = Int.MinValue
    var c = 0
    while (c <= i) {
      val q = qOf(pos(c))
      val r = rOf(pos(c))
      minQ = Math.min(minQ, q); maxQ = Math.max(maxQ, q)
      minR = Math.min(minR, r); maxR = Math.max(maxR, r)
      c += 1
    }
    (minQ, maxQ, minR, maxR)
  }

  /** Index in the grid that covers the cells and a ring around them. */
  private def localIndex(index: Int, minQ: Int, minR: Int, boxWidth: Int): Int =
    (rOf(index) - minR + 1) * boxWidth + qOf(index) - minQ + 1

  /** Label the empty cells that can be reached from the start. @return how many there are. */
  private def fill(startIndex: Int, label: Int, boxWidth: Int, boxHeight: Int): Int = {
    if (labelGrid(startIndex) != 0) return 0
    var top = 0
    floodStack(top) = startIndex
    top += 1
    labelGrid(startIndex) = label
    var count = 0
    while (top > 0) {
      top -= 1
      val cell = floodStack(top)
      count += 1
      val q = cell % boxWidth
      val r = cell / boxWidth
      var d = 0
      while (d < 6) {
        val nq = q + DQ(d)
        val nr = r + DR(d)
        if (nq >= 0 && nq < boxWidth && nr >= 0 && nr < boxHeight) {
          val neighbor = nr * boxWidth + nq
          if (labelGrid(neighbor) == 0) {
            labelGrid(neighbor) = label
            floodStack(top) = neighbor
            top += 1
          }
        }
        d += 1
      }
    }
    count
  }

  /** The shape is complete and the possibilities that are left for the cells are consistent. @return true if it is a solution. */
  private def finish(): Boolean = {
    val (minQ, maxQ, minR, maxR) = bounds(n - 1)
    val boxWidth = maxQ - minQ + 3
    val boxHeight = maxR - minR + 3
    val size = boxWidth * boxHeight
    java.util.Arrays.fill(labelGrid, 0, size, 0)
    for (c <- 0 until n) labelGrid(localIndex(pos(c), minQ, minR, boxWidth)) = -1
    if (fill(0, 1, boxWidth, boxHeight) != size - n) false // some empty cell is enclosed
    else {
      val dom = domains(n - 1).clone()
      var budget = LABELING_BUDGET
      def label(domain: Array[Long]): Boolean = {
        var best = -1
        var bestSize = Int.MaxValue
        var cell = 1
        while (cell < n) {
          val s = java.lang.Long.bitCount(domain(cell))
          if (s > 1 && s < bestSize) { best = cell; bestSize = s }
          cell += 1
        }
        if (best < 0) {
          solution = Some(placementsFor(domain))
          true
        }
        else {
          var m = domain(best)
          var done = false
          while (!done && m != 0L && budget > 0) {
            budget -= 1
            val value = java.lang.Long.numberOfTrailingZeros(m)
            m &= m - 1
            val copy = domain.clone()
            copy(best) = 1L << value
            if (propagate(copy) && hasDistinctTiles(copy, n - 1)) done = label(copy)
          }
          done
        }
      }
      label(dom)
    }
  }

  private def placementsFor(domain: Array[Long]): Seq[TilePlacement] =
    (1 until n).map { cell =>
      val value = java.lang.Long.numberOfTrailingZeros(domain(cell))
      TilePlacement(tiles(value >> 1), locationOf(pos(cell)), Rotation.values(rotationOf(cell)(value)))
    }

  private def locationOf(index: Int): Location = {
    val startQ = start.location.col - Math.floorDiv(start.location.row + 1, 2)
    val row = start.location.row + rOf(index)
    val col = startQ + qOf(index) + Math.floorDiv(row + 1, 2)
    new ByteLocation(row, col)
  }
}
