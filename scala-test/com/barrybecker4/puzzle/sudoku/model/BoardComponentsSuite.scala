// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model

import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import BoardComponents.COMPONENTS


class BoardComponentsSuite extends AnyFunSuite with BeforeAndAfter {

  // For sudoku, the most commong baseSize is 3 (for 9x9 board).
  private val bc3 = COMPONENTS(3)
  private val bc4 = COMPONENTS(4)

  test("Check squares for bc3") {
    assertResult(81) { bc3.squares.length }
  }

  test("Check unitList for bc3") {
    assertResult(27) { bc3.unitList.length }
  }

  test("Check units for bc3") {
    for (s <- bc3.squares)
      assert(bc3.units(s).length == 3)
  }

  test("Check peers for bc3") {
    for (s <- bc3.squares)
      assert(bc3.peers(s).size == 20)
  }

  test("Check specific set of units for cell(3, 2) for bc3") {
    assertResult(Seq(
      Seq((1,2), (2,2), (3,2), (4,2), (5,2), (6,2), (7,2), (8,2), (9,2)), // row
      Seq((3,1), (3,2), (3,3), (3,4), (3,5), (3,6), (3,7), (3,8), (3,9)), // col
      Seq((1,1), (1,2), (1,3), (2,1), (2,2), (2,3), (3,1), (3,2), (3,3))) // square
    ) {
      bc3.units((3, 2))
    }
  }

  // Should have all the peers for unit, row, and column.
  test("Check specific set of peers for cell(3, 2) for bc3") {
    assertResult(Set((3,9), (5,2), (3,4), (3,1), (6,2), (3,6), (1,1), (3,5), (8,2), (1,3), (2,2), (4,2),
      (3,7), (3,3), (2,3), (1,2), (2,1), (7,2), (3,8), (9,2))) {
      bc3.peers((3, 2))
    }
  }

  test("Check squares for bc4") {
    assertResult(256) { bc4.squares.length }
  }

  test("Check specific set of units for cell('C2') for bc4") {
    assertResult(Seq(
      Seq((1,2), (2,2), (3,2), (4,2), (5,2), (6,2), (7,2), (8,2), (9,2), (10,2), (11,2), (12,2), (13,2), (14,2), (15,2), (16,2)),
      Seq((3,1), (3,2), (3,3), (3,4), (3,5), (3,6), (3,7), (3,8), (3,9), (3,10), (3,11), (3,12), (3,13), (3,14), (3,15), (3,16)),
      Seq((1,1), (1,2), (1,3), (1,4), (2,1), (2,2), (2,3), (2,4), (3,1), (3,2), (3,3), (3,4), (4,1), (4,2), (4,3), (4,4)))
    ) {
      bc4.units((3, 2))
    }
  }
}
