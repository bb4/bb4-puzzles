// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.shapeassign

import org.scalatest.funsuite.AnyFunSuite

class LoopKindSuite extends AnyFunSuite {

  test("the number of sides from entry to exit wraps around the tile") {
    assertResult(1)(LoopKind.sidesBetween(5, 0))
    assertResult(5)(LoopKind.sidesBetween(1, 0))
    assertResult(3)(LoopKind.sidesBetween(4, 1))
  }

  test("paths three sides apart are straight, two or four are wide curves, and one or five are tight curves") {
    assertResult(Seq(2, 1, 0, 1, 2))((1 to 5).map(LoopKind.kindOf))
  }

  test("straights do not turn, wide curves turn one step, and tight curves turn two, in either direction") {
    assertResult(Seq(-2, -1, 0, 1, 2))((1 to 5).map(LoopKind.headingChange))
  }
}
