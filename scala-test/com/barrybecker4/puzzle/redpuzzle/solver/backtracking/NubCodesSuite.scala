// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.solver.backtracking

import com.barrybecker4.puzzle.redpuzzle.model.Nub.*
import org.scalatest.funsuite.AnyFunSuite

class NubCodesSuite extends AnyFunSuite {

  private val allNubs =
    List(INNY_SPADE, OUTY_SPADE, INNY_CLUB, OUTY_CLUB, INNY_HEART, OUTY_HEART, INNY_DIAMOND, OUTY_DIAMOND)

  test("every kind of nub has its own code") {
    assertResult(allNubs.size)(allNubs.map(NubCodes.encode).distinct.size)
  }

  test("the complement of a nub's code is the code of the one nub that fits it") {
    for (a <- allNubs; b <- allNubs)
      assertResult(a.fitsWith(b), s"$a and $b")(NubCodes.complement(NubCodes.encode(a)) == NubCodes.encode(b))
  }

  test("the complement of the complement is the original code") {
    allNubs.map(NubCodes.encode).foreach(code => assertResult(code)(NubCodes.complement(NubCodes.complement(code))))
  }
}
