// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.shapeassign

import com.barrybecker4.common.geometry.{ByteLocation, Location}

object HexGrid {
  val NUM_SIDES = 6

  /** How many sides around a cell it is to the side that is directly opposite. */
  val SIDES_TO_OPPOSITE: Int = NUM_SIDES / 2

  /** The change in column and row for a step through each side, in axial coordinates. In axial coordinates the six
    * steps are the same for every cell. They agree with [[com.barrybecker4.puzzle.tantrix.model.HexUtil.getNeighborLocation]].
    */
  private val COLUMN_STEPS = Array(1, 1, 0, -1, -1, 0)
  private val ROW_STEPS = Array(0, -1, -1, 0, 1, 1)

  def oppositeSide(side: Int): Int = (side + SIDES_TO_OPPOSITE) % NUM_SIDES
  def columnStep(side: Int): Int = COLUMN_STEPS(side)
  def rowStep(side: Int): Int = ROW_STEPS(side)
}

/**
  * A hexagonal grid big enough that a loop of the given length, which starts in the middle of it, can not leave it.
  * Cells are identified by a single number (the index) that is easy to use in arrays. Cells are described
  * with axial coordinates, relative to the cell where the loop starts.
  * @param maxLoopLength the greatest number of cells in a loop
  */
class HexGrid(maxLoopLength: Int) {
  import HexGrid.*

  /** Empty cells between the furthest the loop could reach and the edge of the grid. One is for the ring of empty
    * cells around the loop that [[EmptySpaceLabeler]] needs, and one is so that stepping to a neighbor stays on the
    * grid. The other is spare.
    */
  private val EMPTY_MARGIN = 3

  /** The origin is in the middle, so the grid reaches as far each way as the loop could, plus the margin. */
  private val margin = maxLoopLength + EMPTY_MARGIN
  val width: Int = 2 * margin + 1
  val size: Int = width * width

  /** The index of the cell where the loop starts. */
  val origin: Int = margin * width + margin
  private val indexSteps = Array.tabulate(NUM_SIDES)(side => rowStep(side) * width + columnStep(side))

  def neighbor(index: Int, side: Int): Int = index + indexSteps(side)
  def column(index: Int): Int = index % width - margin
  def row(index: Int): Int = index / width - margin

  /** @return the least number of steps between neighbors it takes to get from one cell to the other. */
  def distance(first: Int, second: Int): Int = {
    val columns = column(first) - column(second)
    val rows = row(first) - row(second)
    Math.max(Math.abs(columns), Math.max(Math.abs(rows), Math.abs(columns + rows)))
  }

  /** In the tantrix, the odd rows are shifted back half a cell and the columns are counted along each row. In axial
    * coordinates the columns slant with the rows, so that every neighbor is the same step away. Going between them
    * takes one column off for every two rows.
    * @param startLocation where the cell at the origin is in the tantrix
    * @return the location in the tantrix of the cell at the index
    */
  def toLocation(index: Int, startLocation: Location): Location = {
    val startColumn = startLocation.col - Math.floorDiv(startLocation.row + 1, 2)
    val tantrixRow = startLocation.row + row(index)
    new ByteLocation(tantrixRow, startColumn + column(index) + Math.floorDiv(tantrixRow + 1, 2))
  }
}
