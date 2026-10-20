// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.shapeassign

import org.scalatest.funsuite.AnyFunSuite

class EmptySpaceCheckSuite extends AnyFunSuite {

  private val grid = new HexGrid(7)
  private val center = grid.origin

  /** Cells around the center, one on each side of it. Cell 5 leaves toward the center. */
  private def ringAround(center: Int): LoopCells = {
    val cells = new LoopCells(7, grid)
    for (side <- 0 until 6) cells.place(side, grid.neighbor(center, side), 0)
    cells.setExitSide(5, HexGrid.oppositeSide(5))
    cells
  }

  test("an empty cell surrounded by the loop is enclosed") {
    val cells = ringAround(center)
    assert(!new EmptySpaceCheck(grid, cells, 6, grid.neighbor(center, 5)).noEmptyCellIsEnclosed())
  }

  test("three cells that touch each other enclose nothing") {
    val cells = new LoopCells(3, grid)
    cells.place(0, center, 0)
    cells.place(1, grid.neighbor(center, 0), 0)
    cells.place(2, grid.neighbor(center, 1), 0)
    assert(new EmptySpaceCheck(grid, cells, 3, grid.neighbor(center, 1)).noEmptyCellIsEnclosed())
  }

  test("the loop can go into a space that it encloses if the cells that are left fill it exactly and it ends there") {
    val cells = ringAround(center)
    assert(new EmptySpaceCheck(grid, cells, 7, center).canBeFilledByRemainingCells(5))
  }

  test("the loop can not go into a space that it encloses if the last cell is not in it") {
    val cells = ringAround(center)
    val outside = grid.neighbor(grid.neighbor(center, 0), 0)
    assert(!new EmptySpaceCheck(grid, cells, 7, outside).canBeFilledByRemainingCells(5))
  }

  test("the loop can not go into a space that it encloses if there are more cells left than the space holds") {
    val cells = ringAround(center)
    assert(!new EmptySpaceCheck(grid, cells, 9, center).canBeFilledByRemainingCells(5))
  }
}
