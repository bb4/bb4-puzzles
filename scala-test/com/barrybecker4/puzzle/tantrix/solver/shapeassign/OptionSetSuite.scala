// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.shapeassign

import org.scalatest.funsuite.AnyFunSuite

class OptionSetSuite extends AnyFunSuite {

  test("the options of a tile are two bits next to each other") {
    assertResult(6)(OptionSet.optionOf(3, 0))
    assertResult(7)(OptionSet.optionOf(3, 1))
    assertResult(3)(OptionSet.tileOf(7))
  }

  test("a set of options has a bit for each tile that has any option") {
    val options = OptionSet.single(OptionSet.optionOf(1, 0)) | OptionSet.single(OptionSet.optionOf(1, 1)) |
      OptionSet.single(OptionSet.optionOf(4, 1))
    assertResult((1L << 2) | (1L << 8))(OptionSet.tilesOf(options))
    assert(!OptionSet.hasExactlyOneTile(options))
  }

  test("both rotations of the same tile count as one tile") {
    val options = OptionSet.single(OptionSet.optionOf(5, 0)) | OptionSet.single(OptionSet.optionOf(5, 1))
    assert(OptionSet.hasExactlyOneTile(options))
    assertResult(options)(OptionSet.optionsOfTile(OptionSet.tilesOf(options)))
  }

  test("an empty set has no tile") {
    assert(!OptionSet.hasExactlyOneTile(0L))
  }

  test("options come out lowest first") {
    val options = OptionSet.single(9) | OptionSet.single(4)
    assertResult(4)(OptionSet.firstOption(options))
    assertResult(OptionSet.single(9))(OptionSet.withoutFirst(options))
    assertResult(2)(OptionSet.size(options))
  }
}
