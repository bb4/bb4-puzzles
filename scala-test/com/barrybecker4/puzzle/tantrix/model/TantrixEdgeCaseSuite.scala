// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model

import com.barrybecker4.common.geometry.ByteLocation
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil._
import org.scalatest.funsuite.AnyFunSuite

class TantrixEdgeCaseSuite extends AnyFunSuite {

  test("getNeighborWithNonePlacement") {
    val t = place3SolvedTiles.tantrix
    assert(t.getNeighbor(None, 0).isEmpty)
    assert(t.getNeighbor(None, 5).isEmpty)
  }

  test("applyMissingLocation") {
    val t = new TantrixBoard(HexTiles.TILES.createOrderedList(3)).tantrix
    assert(t(ByteLocation(0, 0)).isEmpty)
    assert(t(5, 99).isEmpty)
  }

  test("sizeEqualsTileMapCount") {
    val t = place7SolvedTiles.tantrix
    assert(t.size == t.tiles.size)
    assert(t.size == t.tileMap.size)
  }

  test("getEdgeLengthNonNegative") {
    Seq(place3SolvedTiles, place6UnsolvedTiles, place10LoopWithInnerSpace).foreach { b =>
      assert(b.tantrix.getEdgeLength >= 1)
    }
  }
}
