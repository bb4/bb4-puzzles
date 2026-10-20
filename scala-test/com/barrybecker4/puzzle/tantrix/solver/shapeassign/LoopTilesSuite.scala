// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.shapeassign

import com.barrybecker4.puzzle.tantrix.model.HexTiles.TILES
import org.scalatest.funsuite.AnyFunSuite

class LoopTilesSuite extends AnyFunSuite {

  private val color = TILES.getTile(6).primaryColor
  private val tiles = new LoopTiles(TILES.createOrderedList(6).toIndexedSeq, color)

  test("every one of the first six tiles has a path of the color of the sixth") {
    assert(LoopTiles.allHavePrimaryPath(TILES.createOrderedList(6), color))
    assertResult(6)(tiles.count)
  }

  test("a tile without a path of the color means there can be no loop of it") {
    val many = TILES.createOrderedList(32)
    assert(!LoopTiles.allHavePrimaryPath(many, TILES.getTile(32).primaryColor))
  }

  test("a tile without the color can not be used") {
    val other = TILES.createOrderedList(32)
    assertThrows[IllegalArgumentException](new LoopTiles(other.toIndexedSeq, TILES.getTile(32).primaryColor))
  }

  test("the kinds of the tiles after the first add up to the number of those tiles") {
    assertResult(5)((0 until LoopKind.NUM_KINDS).map(tiles.numberOfKind).sum)
  }

  test("a tile fits where its primary path leaves by the sides it has, rotated") {
    for (index <- 0 until tiles.count; rotation <- 0 until 6) {
      val first = tiles.rotatedSide(index, 0, rotation)
      val second = tiles.rotatedSide(index, 1, rotation)
      assert(tiles.fits(index, rotation, first, second))
      assert(tiles.fits(index, rotation, second, first))
      assert(!tiles.fits(index, rotation, first, first))
    }
  }

  test("the packed colors give the color of each side of the rotated tile") {
    for (index <- 0 until tiles.count; rotation <- 0 until 6; side <- 0 until 6) {
      val packed = tiles.packedColors(index, rotation)
      val expected = tiles.tile(index).edgeColors(Math.floorMod(side - rotation, 6)).ordinal
      assertResult(expected)((packed >> (3 * side)) & 7)
    }
  }
}
