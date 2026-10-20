// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.shapeassign

import com.barrybecker4.puzzle.tantrix.model.{HexTile, PathColor, TilePlacement}
import com.barrybecker4.puzzle.tantrix.solver.shapeassign.ShapeAssignSearch.*

import scala.util.Random

object ShapeAssignSearch {

  /** The most tiles that can be used. The options for a cell are a set of bits in a long, with two bits for each tile. */
  val MAX_TILES: Int = OptionSet.MAX_TILES

  /** A step is one more cell added to the loop. The search reports its progress this often. */
  private val STEPS_BETWEEN_PROGRESS_REPORTS = 256

  /** How a search attempt ended. */
  sealed trait Outcome
  case class Found(moves: Seq[TilePlacement]) extends Outcome
  /** The whole space was searched and there is no solution. */
  case object Exhausted extends Outcome
  /** The step limit was reached. */
  case object OutOfSteps extends Outcome
}

/**
  * Finds a solution by building the shape of the loop and choosing the tiles for it together, so that each
  * decision prunes the other.
  *
  * Every solution is a loop of the primary color through all of the tiles, where no empty space is enclosed. The shape
  * is built one cell at a time around the loop, starting from the first tile. The shape alone says what kind of
  * primary path (straight, wide curve, or tight curve) each cell needs, so only the tiles of that kind can go there, and the
  * numbers of cells of each kind used must match the numbers of tiles of each kind (see [[TurnBudget]]).
  * The sides of a cell that touch cells other than the ones before and after it in the loop must have the same colors
  * as the sides they touch (see [[ContactList]]). That is where the difficulty is, and it is the reason this does not
  * choose a tile for a cell when it adds the cell. Every cell keeps the set of all the tiles (and rotations) that are
  * still possible for it, and the sets are reduced each time the shape touches itself in a new place
  * (see [[DomainPropagator]]). Shapes where some cell has no tile left, or where the cells can not all get different
  * tiles (see [[DistinctTileMatcher]]), are abandoned long before they are complete. When the shape is complete, the
  * tiles are chosen by a small search over what is left (see [[TileAssigner]]).
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

  private val numCells = tiles.size
  require(numCells >= 3 && numCells <= MAX_TILES, s"Need between 3 and $MAX_TILES tiles, but got $numCells")

  private val grid = new HexGrid(numCells)
  private val loopTiles = new LoopTiles(tiles, primaryColor)
  private val cells = new LoopCells(numCells, grid)
  private val options = new CellOptions(numCells, loopTiles)
  private val contacts = new ContactList(HexGrid.NUM_SIDES * numCells)
  private val turnBudget = new TurnBudget(loopTiles)
  private val propagator = new DomainPropagator(numCells, options, contacts)
  private val matcher = new DistinctTileMatcher
  private val assigner = new TileAssigner(numCells, propagator, matcher)
  private val placements = new PlacementBuilder(grid, cells, options, loopTiles, start.location)

  // The first tile is placed, and the loop leaves it by one primary side and comes back to it by the other.
  private val startRotation = start.rotation.ordinal
  private val startExitSide = loopTiles.rotatedSide(0, 0, startRotation)
  private val startEntrySide = loopTiles.rotatedSide(0, 1, startRotation)
  private val startHeading = LoopKind.headingChange(LoopKind.sidesBetween(startEntrySide, startExitSide))
  private val lastCellPosition = grid.neighbor(grid.origin, startEntrySide)
  private val lastCellExitSide = HexGrid.oppositeSide(startEntrySide)

  private val shapeFeasibility = new ShapeFeasibility(grid, cells, turnBudget, numCells, lastCellPosition)
  private val emptySpaces = new EmptySpaceCheck(grid, cells, numCells, lastCellPosition)

  /** domainsAfterCell(i) are the options of all the cells, once cell i has been given its exit. */
  private val domainsAfterCell = Array.ofDim[Long](numCells, numCells)

  private var steps = 0L
  private var stepLimit = 0L
  private var stoppedForLackOfSteps = false
  private var solution: Option[Seq[TilePlacement]] = None

  def numSteps: Long = steps

  /** Make one attempt. The cells are tried in a random order, so another attempt can end differently.
    * @param maxSteps number of cells that can be added before the attempt is ended
    */
  def run(maxSteps: Long): Outcome = {
    startAttempt(maxSteps)
    try outcomeOf(extend(1, startHeading))
    finally cells.removeAll()
  }

  private def outcomeOf(found: Boolean): Outcome =
    if (found) Found(solution.get) else if (stoppedForLackOfSteps) OutOfSteps else Exhausted

  private def startAttempt(maxSteps: Long): Unit = {
    resetCounters(maxSteps)
    turnBudget.reset()
    contacts.rollbackTo(0)
    placeStartCell()
  }

  private def resetCounters(maxSteps: Long): Unit = {
    stepLimit = maxSteps
    steps = 0
    stoppedForLackOfSteps = false
    solution = None
  }

  private def placeStartCell(): Unit = {
    cells.place(0, grid.origin, startEntrySide)
    cells.setExitSide(0, startExitSide)
    domainsAfterCell(0)(0) = options.setPlacedTile(startRotation)
    cells.placeBeyond(0, startExitSide)
  }

  /** @param cell the cell that has been positioned, with the side that it is entered by, but not yet its exit
    * @param headingSoFar the sum of turns of the cells before this one, in steps of 60 degrees
    * @return true if a solution was found
    */
  private def extend(cell: Int, headingSoFar: Int): Boolean =
    if (countStep()) tryExits(cell, headingSoFar) else false

  /** @return false if there are no steps left */
  private def countStep(): Boolean = {
    stoppedForLackOfSteps = steps >= stepLimit
    if (!stoppedForLackOfSteps) { steps += 1; reportProgressSometimes() }
    !stoppedForLackOfSteps
  }

  private def reportProgressSometimes(): Unit =
    if (steps % STEPS_BETWEEN_PROGRESS_REPORTS == 0L) onProgress(steps)

  private def tryExits(cell: Int, headingSoFar: Int): Boolean =
    candidateExitSides(cell).exists(exitSide => !stoppedForLackOfSteps && tryExit(cell, exitSide, headingSoFar))

  /** The last cell must lead back to the first. The other cells can leave by any side but the one they are entered by. */
  private def candidateExitSides(cell: Int): Array[Int] =
    if (cell == numCells - 1) Array(lastCellExitSide)
    else rnd.shuffle(Seq(0, 1, 2, 3, 4, 5).filter(_ != cells.entrySide(cell))).toArray

  private def tryExit(cell: Int, exitSide: Int, headingSoFar: Int): Boolean = {
    val sidesBetween = LoopKind.sidesBetween(cells.entrySide(cell), exitSide)
    val kind = LoopKind.kindOf(sidesBetween)
    turnBudget.isAvailable(kind) && withKindUsed(kind) {
      tryShape(cell, exitSide, headingSoFar + LoopKind.headingChange(sidesBetween))
    }
  }

  private def withKindUsed(kind: Int)(search: => Boolean): Boolean = {
    turnBudget.use(kind)
    val found = search
    turnBudget.giveBack(kind)
    found
  }

  /** Try the exit for the cell, if the shape could still be completed with it. */
  private def tryShape(cell: Int, exitSide: Int, heading: Int): Boolean =
    shapeFeasibility.canBeCompleted(cell, exitSide, heading) && withContactsKept {
      fixCell(cell, exitSide) && completeOrContinue(cell, exitSide, heading)
    }

  /** The contacts that the search adds are removed again after it. */
  private def withContactsKept(search: => Boolean): Boolean = {
    val contactsBefore = contacts.count
    val found = search
    contacts.rollbackTo(contactsBefore)
    found
  }

  /** Give the cell its exit and reduce the options of the cells because of it.
    * @return false if that leaves some cell with nothing, or if the cells can not all have different tiles.
    */
  private def fixCell(cell: Int, exitSide: Int): Boolean = {
    cells.setExitSide(cell, exitSide)
    copyDomainsBefore(cell)
    domainsAfterCell(cell)(cell) = options.optionsFor(cell, cells.entrySide(cell), exitSide)
    addContactsOf(cell)
    isConsistent(cell)
  }

  private def copyDomainsBefore(cell: Int): Unit =
    System.arraycopy(domainsAfterCell(cell - 1), 0, domainsAfterCell(cell), 0, numCells)

  /** The sides of the cell that touch cells other than the ones before and after it in the loop. */
  private def addContactsOf(cell: Int): Unit =
    for (side <- 0 until HexGrid.NUM_SIDES if side != cells.entrySide(cell) && side != cells.exitSide(cell)) {
      val touchingCell = cells.earlierCellOnSide(cell, side)
      if (touchingCell >= 0) contacts.add(cell, side, touchingCell, HexGrid.oppositeSide(side))
    }

  private def isConsistent(cell: Int): Boolean =
    propagator.propagate(domainsAfterCell(cell)) &&
      matcher.canAssignDistinctTiles(domainsAfterCell(cell), cell) &&
      (cell == numCells - 1 || emptySpaces.canBeFilledByRemainingCells(cell))

  private def completeOrContinue(cell: Int, exitSide: Int, heading: Int): Boolean =
    if (cell == numCells - 1) assignTiles() else continueBeyond(cell, exitSide, heading)

  private def continueBeyond(cell: Int, exitSide: Int, heading: Int): Boolean = {
    cells.placeBeyond(cell, exitSide)
    val found = extend(cell + 1, heading)
    cells.remove(cell + 1)
    found
  }

  /** The shape is complete and the options that are left for the cells are consistent. @return true if it is a solution. */
  private def assignTiles(): Boolean = {
    val decided = if (emptySpaces.noEmptyCellIsEnclosed()) assigner.assign(domainsAfterCell(numCells - 1)) else None
    solution = decided.map(placements.placementsFor)
    solution.isDefined
  }
}
