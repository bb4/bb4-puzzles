// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.model

import org.scalatest.funsuite.AnyFunSuite

class SolvabilitySuite extends AnyFunSuite {

  test("solved boards are solvable") {
    for (size <- 2 to 5)
      assert(Solvability.isSolvable(size, (1 until size * size) :+ 0))
  }

  test("swapping two tiles of the solved 4 by 4 board makes it unsolvable") {
    val tiles = Seq(2, 1, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0)
    assert(!Solvability.isSolvable(4, tiles))
  }

  test("swapping two tiles of the solved 3 by 3 board makes it unsolvable") {
    assert(!Solvability.isSolvable(3, Seq(2, 1, 3, 4, 5, 6, 7, 8, 0)))
  }

  test("one slide from solved is solvable") {
    assert(Solvability.isSolvable(4, Seq(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 0, 15)))
    assert(Solvability.isSolvable(3, Seq(1, 2, 3, 4, 5, 6, 7, 0, 8)))
  }

  test("exactly half of the arrangements of the 2 by 2 board are solvable") {
    assertResult(12)((0 to 3).toList.permutations.count(Solvability.isSolvable(2, _)))
  }
}
