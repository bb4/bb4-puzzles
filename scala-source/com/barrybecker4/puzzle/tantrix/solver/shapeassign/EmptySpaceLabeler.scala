// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.shapeassign

object EmptySpaceLabeler {
  private val UNLABELED = 0
  private val OCCUPIED = -1

  /** The number of empty cells between the loop and the edge of the box. */
  private val RING_WIDTH = 1
  private val OUTSIDE = 1
  private val ENCLOSED = 2
}

/**
  * Finds the groups of empty cells next to the cells of the loop, which are connected to each other. The
  * cells of the loop and a ring of empty cells around them are put in a box. The ring is all connected,
  * and is called the outside. The cells are labeled by filling out from a starting cell.
  * The box is kept in arrays that are reused, since this is done for every cell that is added to the loop.
  */
class EmptySpaceLabeler(grid: HexGrid) {
  import EmptySpaceLabeler.*

  private val labels = new Array[Int](grid.size)
  private val stack = new Array[Int](grid.size)
  private var stackSize = 0
  private var minColumn, minRow, boxWidth, boxHeight = 0

  /** The number of cells in the box, including the ring around the cells of the loop. */
  def area: Int = boxWidth * boxHeight

  /** Put the cells of the loop up to the given one in an empty box that is just big enough. */
  def mark(cells: LoopCells, lastCell: Int): Unit = {
    measureBox(cells, lastCell)
    java.util.Arrays.fill(labels, 0, area, UNLABELED)
    (LoopCells.START_CELL to lastCell).foreach(cell => labels(indexInBox(cells.position(cell))) = OCCUPIED)
  }

  /** Label the empty cells that are connected to the ring around the loop. The corner of the box is in the ring.
    * @return how many there are.
    */
  def fillOutside(): Int = fill(0, OUTSIDE)

  /** Label the empty cells that are connected to the one at the position, unless they are already labeled as outside.
    * @return how many there are.
    */
  def fillAround(position: Int): Int = fill(indexInBox(position), ENCLOSED)

  def isOutside(position: Int): Boolean = labels(indexInBox(position)) == OUTSIDE

  def inSameSpace(position1: Int, position2: Int): Boolean =
    labels(indexInBox(position1)) == labels(indexInBox(position2))

  private def measureBox(cells: LoopCells, lastCell: Int): Unit = {
    minColumn = lowest(cells, lastCell, grid.column)
    minRow = lowest(cells, lastCell, grid.row)
    boxWidth = highest(cells, lastCell, grid.column) - minColumn + 1 + 2 * RING_WIDTH
    boxHeight = highest(cells, lastCell, grid.row) - minRow + 1 + 2 * RING_WIDTH
  }

  /** The highest is the lowest of the negatives. */
  private def highest(cells: LoopCells, lastCell: Int, coordinate: Int => Int): Int =
    -lowest(cells, lastCell, position => -coordinate(position))

  /** @return the lowest value of the coordinate over the cells of the loop up to the last cell */
  private def lowest(cells: LoopCells, lastCell: Int, coordinate: Int => Int): Int = {
    var best = Int.MaxValue
    for (cell <- 0 to lastCell) best = Math.min(best, coordinate(cells.position(cell)))
    best
  }

  private def indexInBox(position: Int): Int =
    (grid.row(position) - minRow + RING_WIDTH) * boxWidth + grid.column(position) - minColumn + RING_WIDTH

  private def fill(startIndex: Int, label: Int): Int =
    if (labels(startIndex) != UNLABELED) 0 else floodFrom(startIndex, label)

  private def floodFrom(startIndex: Int, label: Int): Int = {
    stackSize = 0
    push(startIndex, label)
    var count = 0
    while (hasCellsToVisit) { count += 1; pushUnlabeledNeighbors(pop(), label) }
    count
  }

  /** Cells are pushed when they are labeled and popped when their neighbors are looked at, so this runs out. */
  private def hasCellsToVisit: Boolean = stackSize > 0

  private def push(index: Int, label: Int): Unit = {
    labels(index) = label
    stack(stackSize) = index
    stackSize += 1
  }

  private def pop(): Int = {
    stackSize -= 1
    stack(stackSize)
  }

  private def pushUnlabeledNeighbors(index: Int, label: Int): Unit =
    for (side <- 0 until HexGrid.NUM_SIDES) {
      val neighbor = neighborInBox(index, side)
      if (neighbor >= 0 && labels(neighbor) == UNLABELED) push(neighbor, label)
    }

  /** @return the index of the neighbor in the box, or -1 if it would be outside the box */
  private def neighborInBox(index: Int, side: Int): Int = {
    val column = index % boxWidth + HexGrid.columnStep(side)
    val row = index / boxWidth + HexGrid.rowStep(side)
    if (column >= 0 && column < boxWidth && row >= 0 && row < boxHeight) row * boxWidth + column else -1
  }
}
