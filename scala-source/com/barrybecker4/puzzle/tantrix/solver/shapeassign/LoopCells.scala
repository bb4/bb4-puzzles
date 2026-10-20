// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.shapeassign

object LoopCells {
  /** The first cell of the loop, where the tile that is already placed is. */
  val START_CELL = 0

  /** The first cell that a tile has to be chosen for. */
  val FIRST_NEW_CELL = 1
}

/**
  * The cells of the loop that have been placed so far, in the order that they are along the loop. Each cell is entered
  * from the side that the loop comes from, and left by the side it goes on by. A cell is numbered by its place in the loop.
  */
class LoopCells(numCells: Int, grid: HexGrid) {

  private val positions, entrySides, exitSides = new Array[Int](numCells)
  private val cellAtPosition = Array.fill(grid.size)(-1)

  def position(cell: Int): Int = positions(cell)
  def entrySide(cell: Int): Int = entrySides(cell)
  def exitSide(cell: Int): Int = exitSides(cell)
  def setExitSide(cell: Int, side: Int): Unit = exitSides(cell) = side

  /** @return true if no cell of the loop is at the position. */
  def isFree(position: Int): Boolean = cellAtPosition(position) == -1

  /** @return the position the loop goes to if the cell is left by the given side. */
  def positionBeyond(cell: Int, exitSide: Int): Int = grid.neighbor(positions(cell), exitSide)

  def place(cell: Int, position: Int, entrySide: Int): Unit = {
    positions(cell) = position
    entrySides(cell) = entrySide
    cellAtPosition(position) = cell
  }

  /** Place the cell after this one, in the position that is beyond the given exit of this one. */
  def placeBeyond(cell: Int, exitSide: Int): Unit =
    place(cell + 1, positionBeyond(cell, exitSide), HexGrid.oppositeSide(exitSide))

  def remove(cell: Int): Unit = cellAtPosition(positions(cell)) = -1

  /** Take all the cells off. */
  def removeAll(): Unit = java.util.Arrays.fill(cellAtPosition, -1)

  /** @return the cell on the given side of this one if it is earlier in the loop, otherwise -1. Positions
    * that are free hold -1, which is also less than the cell, so they give -1 as well.
    */
  def earlierCellOnSide(cell: Int, side: Int): Int = {
    val other = cellAtPosition(grid.neighbor(positions(cell), side))
    if (other < cell) other else -1
  }
}
