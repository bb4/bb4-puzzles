// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.shapeassign

/**
  * Rules about the shape of the loop alone, that say when the cells that are left can not possibly finish it.
  * @param lastCellPosition where the last cell of the loop will be, which is next to the first cell
  */
class ShapeFeasibility(grid: HexGrid, cells: LoopCells, turnBudget: TurnBudget, numCells: Int, lastCellPosition: Int) {

  /** @param cell the cell that has just been given its exit
    * @param exitSide the exit of the cell
    * @param heading the total turn of the cells up to and including this one, in steps of 60 degrees
    * @return false if the loop can not be completed from here, whatever tiles are used
    */
  def canBeCompleted(cell: Int, exitSide: Int, heading: Int): Boolean = {
    val cellsAfter = numCells - 1 - cell
    if (cellsAfter == 0) Math.abs(heading) == LoopKind.FULL_TURN
    else canContinue(cells.positionBeyond(cell, exitSide), cellsAfter, heading)
  }

  private def canContinue(next: Int, cellsAfter: Int, heading: Int): Boolean =
    cells.isFree(next) && canReachLastCell(next, cellsAfter) && turnBudget.canCompleteLoop(heading)

  /** The next cell is the last one only when it is the only cell left. Otherwise it needs to be close enough. */
  private def canReachLastCell(next: Int, cellsAfter: Int): Boolean =
    if (next == lastCellPosition) cellsAfter == 1
    else cellsAfter > 1 && grid.distance(next, lastCellPosition) <= cellsAfter - 1
}
