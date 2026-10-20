// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.shapeassign

import org.scalatest.funsuite.AnyFunSuite

class LoopCellsSuite extends AnyFunSuite {

  private val grid = new HexGrid(6)

  /** Cells 0, 1 and 2 are all next to each other. */
  private def triangle(): LoopCells = {
    val cells = new LoopCells(6, grid)
    cells.place(0, grid.origin, 4)
    cells.setExitSide(0, 0)
    cells.placeBeyond(0, 0)
    cells.setExitSide(1, 2)
    cells.placeBeyond(1, 2)
    cells
  }

  test("a cell placed beyond the exit of another is next to it and is entered from the opposite side") {
    val cells = triangle()
    assertResult(grid.neighbor(grid.origin, 0))(cells.position(1))
    assertResult(3)(cells.entrySide(1))
    assertResult(grid.neighbor(cells.position(1), 2))(cells.position(2))
  }

  test("only the positions of cells that are placed are taken") {
    val cells = triangle()
    assert(!cells.isFree(cells.position(2)))
    cells.remove(2)
    assert(cells.isFree(cells.position(2)))
    assert(cells.isFree(grid.neighbor(grid.origin, 3)))
  }

  test("a touching cell is found on the side of a cell if it is earlier in the loop") {
    val cells = triangle()
    assertResult(0)(cells.earlierCellOnSide(2, 4))
    assertResult(-1)(cells.earlierCellOnSide(0, 0)) // cell 1 is later than cell 0
    assertResult(-1)(cells.earlierCellOnSide(2, 1)) // empty
  }

  test("removing all the cells frees every position") {
    val cells = triangle()
    cells.removeAll()
    assert((0 until 3).forall(cell => cells.isFree(cells.position(cell))))
  }
}
