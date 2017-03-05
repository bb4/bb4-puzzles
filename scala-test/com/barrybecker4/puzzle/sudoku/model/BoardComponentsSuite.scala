/*
 * // Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
 */
package com.barrybecker4.puzzle.sudoku.model

import org.scalatest.{BeforeAndAfter, FunSuite}


class BoardComponentsSuite extends FunSuite with BeforeAndAfter {


  private val bc = new BoardComponents(3)

  test("Check squares") {
    assertResult(81) { bc.squares.length }
  }

  test("Check unitList") {
    assertResult(27) { bc.unitList.length }
  }

  test("Check units") {
    for (s <- bc.squares)
      assert(bc.units(s).length == 3)
  }

  test("Check peers") {
    for (s <- bc.squares)
      assert(bc.peers(s).size == 20)
  }

  test("Check specific set of units") {
    assertResult(Seq(
      Seq((1,2), (2,2), (3,2), (4,2), (5,2), (6,2), (7,2), (8,2), (9,2)),
      Seq((3,1), (3,2), (3,3), (3,4), (3,5), (3,6), (3,7), (3,8), (3,9)),
      Seq((1,1), (1,2), (1,3), (2,1), (2,2), (2,3), (3,1), (3,2), (3,3)))
    ) {
      bc.units((3, 2))
    }
  }

  test("Check specific set of peers") {
    assertResult(Set((3,9), (5,2), (3,4), (3,1), (6,2), (3,6), (1,1), (3,5), (8,2), (1,3), (2,2), (4,2),
      (3,7), (3,3), (2,3), (1,2), (2,1), (7,2), (3,8), (9,2))) {
      bc.peers((3, 2))
    }
  }

  /*
  //assert len(squares) == 81
  //assert len(unitlist) == 27
  //assert all(len(units[s]) == 3 for s in squares)
  //assert all(len(peers[s]) == 20 for s in squares)
  assert units['C2'] == [['A2', 'B2', 'C2', 'D2', 'E2', 'F2', 'G2', 'H2', 'I2'],
  ['C1', 'C2', 'C3', 'C4', 'C5', 'C6', 'C7', 'C8', 'C9'],
  ['A1', 'A2', 'A3', 'B1', 'B2', 'B3', 'C1', 'C2', 'C3']]
  assert peers['C2'] == set(['A2', 'B2', 'D2', 'E2', 'F2', 'G2', 'H2', 'I2',
  'C1', 'C3', 'C4', 'C5', 'C6', 'C7', 'C8', 'C9',
  'A1', 'A3', 'B1', 'B3'])
  print 'All tests pass.'*/
}
