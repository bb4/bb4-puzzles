// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.solver.fastsearch

import org.scalatest.funsuite.AnyFunSuite

class LineConflictsSuite extends AnyFunSuite {

  private val conflicts = new LineConflicts(new GridGeometry(4))

  test("a solved board has no conflicts") {
    val solved = (1 to 15).toArray :+ 0
    assertResult(0)((0 until 4).map(conflicts.rowExtra(solved, _)).sum + (0 until 4).map(conflicts.colExtra(solved, _)).sum)
  }

  test("two tiles in their row in the wrong order cost 2 extra slides") {
    val tiles = Array(2, 1, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0)
    assertResult(2)(conflicts.rowExtra(tiles, 0))
  }

  test("three tiles reversed in their row need two of them to step out") {
    val tiles = Array(3, 2, 1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0)
    assertResult(4)(conflicts.rowExtra(tiles, 0))
  }

  test("tiles that are in a row but do not belong in it do not conflict") {
    val tiles = Array(8, 7, 3, 4, 5, 6, 1, 2, 9, 10, 11, 12, 13, 14, 15, 0)
    assertResult(0)(conflicts.rowExtra(tiles, 0))
  }

  test("two tiles in their column in the wrong order cost 2 extra slides") {
    val tiles = Array(5, 2, 3, 4, 1, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0)
    assertResult(2)(conflicts.colExtra(tiles, 0))
  }

  test("a tile in between that belongs elsewhere does not hide a conflict") {
    val tiles = Array(3, 9, 1, 4, 5, 6, 7, 8, 2, 10, 11, 12, 13, 14, 15, 0)
    assertResult(2)(conflicts.rowExtra(tiles, 0))
  }

  test("the blank is ignored") {
    val tiles = Array(0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 1)
    assertResult(0)(conflicts.rowExtra(tiles, 0))
  }
}
