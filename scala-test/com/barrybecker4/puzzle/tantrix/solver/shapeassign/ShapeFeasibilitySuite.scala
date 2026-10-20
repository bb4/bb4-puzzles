// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.shapeassign

import com.barrybecker4.puzzle.tantrix.model.HexTiles.TILES
import org.scalatest.funsuite.AnyFunSuite

class ShapeFeasibilitySuite extends AnyFunSuite {

  private val grid = new HexGrid(6)
  private val tiles = new LoopTiles(TILES.createOrderedList(6).toIndexedSeq, TILES.getTile(6).primaryColor)
  private val budget = new TurnBudget(tiles)
  budget.reset()
  private val available = tiles.numberOfKind(LoopKind.WIDE_CURVE) + 2 * tiles.numberOfKind(LoopKind.TIGHT_CURVE)
  private val lastCellPosition = grid.neighbor(grid.origin, 3)

  private def twoCells(): LoopCells = {
    val cells = new LoopCells(6, grid)
    cells.place(0, grid.origin, 3)
    cells.place(1, grid.neighbor(grid.origin, 0), 3)
    cells
  }

  private def feasibility(cells: LoopCells) = new ShapeFeasibility(grid, cells, budget, 6, lastCellPosition)

  test("the last cell must bring the total turn to a full circle, in either direction") {
    val cells = twoCells()
    cells.place(5, lastCellPosition, 0)
    assert(feasibility(cells).canBeCompleted(5, 0, 6))
    assert(feasibility(cells).canBeCompleted(5, 0, -6))
    assert(!feasibility(cells).canBeCompleted(5, 0, 4))
  }

  test("the loop can not go on to a cell that is taken") {
    val cells = twoCells()
    assert(!feasibility(cells).canBeCompleted(1, 3, 0))
  }

  test("the loop can not go on if the last cell is farther away than the cells that are left can reach") {
    val cells = twoCells()
    cells.place(2, grid.neighbor(grid.neighbor(grid.origin, 0), 0), 3)
    cells.place(3, grid.neighbor(cells.position(2), 0), 3)
    val farAway = new ShapeFeasibility(grid, cells, budget, 6, grid.neighbor(grid.neighbor(grid.neighbor(grid.origin, 3), 3), 3))
    assert(!farAway.canBeCompleted(3, 0, 0))
  }

  test("the loop can go on when the last cell is close enough and the remaining cells can turn enough") {
    val cells = twoCells()
    assert(feasibility(cells).canBeCompleted(1, 0, LoopKind.FULL_TURN - available))
  }

  test("the loop can not go on if the remaining cells can not turn enough") {
    val cells = twoCells()
    assert(!feasibility(cells).canBeCompleted(1, 0, 100))
  }

  test("only the last cell can be next to the first when it is the only cell left") {
    val cells = twoCells()
    cells.place(2, grid.neighbor(cells.position(1), 5), 3)
    assert(!feasibility(cells).canBeCompleted(2, 4, 0)) // next is the last position but 3 cells are left
  }
}
