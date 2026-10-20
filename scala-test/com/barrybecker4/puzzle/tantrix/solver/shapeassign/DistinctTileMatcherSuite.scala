// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.shapeassign

import org.scalatest.funsuite.AnyFunSuite

class DistinctTileMatcherSuite extends AnyFunSuite {

  private val matcher = new DistinctTileMatcher

  private def tiles(numbers: Int*): Long = numbers.map(tile => OptionSet.single(OptionSet.optionOf(tile, 0))).reduce(_ | _)

  test("cells that each have their own tile can be assigned") {
    assert(matcher.canAssignDistinctTiles(Array(1L, tiles(1), tiles(2), tiles(3)), 3))
  }

  test("two cells that can only have the same tile can not be assigned") {
    assert(!matcher.canAssignDistinctTiles(Array(1L, tiles(2), tiles(2), tiles(1, 3)), 3))
  }

  test("a cell that has taken a tile moves on to another when that lets a later cell have it") {
    assert(!matcher.canAssignDistinctTiles(Array(1L, tiles(1, 2), tiles(1, 2), tiles(2)), 3))
    assert(matcher.canAssignDistinctTiles(Array(1L, tiles(1, 2), tiles(1), tiles(3)), 3))
  }

  test("three cells that share two tiles between them can not be assigned") {
    assert(!matcher.canAssignDistinctTiles(Array(1L, tiles(1, 2), tiles(1, 2), tiles(1, 2)), 3))
  }

  test("cells after the last one to check are ignored") {
    assert(matcher.canAssignDistinctTiles(Array(1L, tiles(1), 0L, 0L), 1))
  }
}
