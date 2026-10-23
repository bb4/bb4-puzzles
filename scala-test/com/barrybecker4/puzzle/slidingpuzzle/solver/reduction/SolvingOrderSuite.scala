// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.solver.reduction

import com.barrybecker4.puzzle.slidingpuzzle.solver.fastsearch.GridGeometry
import org.scalatest.funsuite.AnyFunSuite

class SolvingOrderSuite extends AnyFunSuite {

  test("every tile is placed exactly once, for several sizes") {
    for (size <- 2 to 6)
      assertResult((1 until size * size).toList)(SolvingOrder.steps(new GridGeometry(size)).flatten.sorted.toList)
  }

  test("the 3 by 3 puzzle is done top row, then the left column of the last two rows, then the corner") {
    assertResult(Seq(Seq(1), Seq(2, 3), Seq(4, 7), Seq(5, 6, 8)))(SolvingOrder.steps(new GridGeometry(3)))
  }

  test("the 2 by 2 puzzle is only the corner") {
    assertResult(Seq(Seq(1, 2, 3)))(SolvingOrder.steps(new GridGeometry(2)))
  }

  test("in the 4 by 4 puzzle the last two tiles of a row are placed together") {
    val steps = SolvingOrder.steps(new GridGeometry(4))
    assert(steps.contains(Seq(3, 4)) && steps.contains(Seq(7, 8)))
    assert(steps.contains(Seq(9, 13)) && steps.contains(Seq(10, 14)))
  }
}
