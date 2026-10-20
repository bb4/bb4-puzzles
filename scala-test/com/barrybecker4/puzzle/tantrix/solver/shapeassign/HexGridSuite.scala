// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.shapeassign

import com.barrybecker4.puzzle.tantrix.model.HexUtil
import com.barrybecker4.common.geometry.ByteLocation
import org.scalatest.funsuite.AnyFunSuite

class HexGridSuite extends AnyFunSuite {

  private val grid = new HexGrid(10)
  private val startLocation = new ByteLocation(21, 21)

  test("the origin is the start location") {
    val location = grid.toLocation(grid.origin, startLocation)
    assertResult((21, 21))((location.row, location.col))
  }

  test("stepping to a neighbor agrees with the neighbors of a location in the tantrix, on even and odd rows") {
    for (side <- 0 until 6; from <- Seq(grid.origin, grid.neighbor(grid.origin, 1), grid.neighbor(grid.origin, 4))) {
      val expected = HexUtil.getNeighborLocation(grid.toLocation(from, startLocation), side)
      val actual = grid.toLocation(grid.neighbor(from, side), startLocation)
      assertResult((expected.row, expected.col), s"side $side")((actual.row, actual.col))
    }
  }

  test("the opposite side is three sides around, and going out and back returns to the same cell") {
    for (side <- 0 until 6) {
      assertResult((side + 3) % 6)(HexGrid.oppositeSide(side))
      assertResult(grid.origin)(grid.neighbor(grid.neighbor(grid.origin, side), HexGrid.oppositeSide(side)))
    }
  }

  test("the distance is the number of steps between neighbors") {
    assertResult(0)(grid.distance(grid.origin, grid.origin))
    assertResult(1)(grid.distance(grid.origin, grid.neighbor(grid.origin, 2)))
    val twoSteps = grid.neighbor(grid.neighbor(grid.origin, 0), 1)
    assertResult(2)(grid.distance(grid.origin, twoSteps))
    assertResult(3)(grid.distance(grid.origin, grid.neighbor(grid.neighbor(grid.neighbor(grid.origin, 3), 3), 3)))
  }
}
