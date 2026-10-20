// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.shapeassign

/**
  * Checks the empty cells around the cells of the loop. A solution can not have an empty cell with the loop all around it.
  * While the loop is being built, the cells that are left must be able to fill the space that the loop goes into next.
  * @param lastCellPosition where the last cell of the loop will be, which is next to the first cell
  */
class EmptySpaceCheck(grid: HexGrid, cells: LoopCells, numCells: Int, lastCellPosition: Int) {

  private val labeler = new EmptySpaceLabeler(grid)

  /** The loop goes on from the given cell to an empty cell, and on from there to the last cell. If that empty cell is not
    * connected to the outside, the loop is going into a space it can not get out of. Then that space has to
    * be exactly filled by the cells that are left. In any case, there can be no other space that the loop has shut off.
    */
  def canBeFilledByRemainingCells(cell: Int): Boolean = {
    labeler.mark(cells, cell)
    val outsideSize = labeler.fillOutside()
    val next = cells.positionBeyond(cell, cells.exitSide(cell))
    val enclosedSize = if (labeler.isOutside(next)) 0 else labeler.fillAround(next)
    spaceIsFilledExactly(next, enclosedSize, numCells - 1 - cell) && outsideSize + enclosedSize == labeler.area - (cell + 1)
  }

  /** The empty cell the loop goes to next has to be in the same space as the last cell, and a space that is enclosed has
    * to have just as many cells as the loop has left.
    */
  private def spaceIsFilledExactly(next: Int, enclosedSize: Int, numCellsLeft: Int): Boolean =
    labeler.inSameSpace(next, lastCellPosition) && (labeler.isOutside(next) || enclosedSize == numCellsLeft)

  /** @return true if all the empty cells next to the complete loop are outside of it */
  def noEmptyCellIsEnclosed(): Boolean = {
    labeler.mark(cells, numCells - 1)
    labeler.fillOutside() == labeler.area - numCells
  }
}
