// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.solver.fastsearch

import org.scalatest.funsuite.AnyFunSuite

class GridGeometrySuite extends AnyFunSuite {

  private val geometry = new GridGeometry(4)

  test("rows and columns of cells") {
    assertResult((2, 1))((geometry.row(9), geometry.col(9)))
  }

  test("the blank belongs in the last cell and tile t belongs in cell t - 1") {
    assertResult(15)(geometry.goalCell(0))
    assertResult(0)(geometry.goalCell(1))
    assertResult(14)(geometry.goalCell(15))
  }

  test("corner cells have two neighbors, edge cells three, and inner cells four") {
    assertResult(Seq(2, 3, 4))((geometry.neighbors(0).length, geometry.neighbors(1).length, geometry.neighbors(5).length).toList)
  }

  test("neighbors are next to the cell") {
    assertResult(Set(1, 4, 6, 9))(geometry.neighbors(5).toSet)
  }

  test("distance of a tile to the cell where it belongs") {
    assertResult(0)(geometry.distanceToGoal(1, 0))
    assertResult(5)(geometry.distanceToGoal(1, 14))
    assertResult(0)(geometry.distanceToGoal(0, 3))
  }
}
