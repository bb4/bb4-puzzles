// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model

import com.barrybecker4.common.geometry.Box
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil._
import com.barrybecker4.puzzle.tantrix.model.HexTiles.TILES
import com.barrybecker4.puzzle.tantrix.model.Rotation._
import org.scalatest.funsuite.AnyFunSuite

class BoundingBoxCalculatorSuite extends AnyFunSuite {

  private val calc = BoundingBoxCalculator()

  test("singleTile") {
    val p = TilePlacement(TILES.getTile(1), TantrixBoard.INITIAL_LOCATION, ANGLE_0)
    assert(calc.getBoundingBox(Seq(p)) == new Box(p.location))
  }

  test("threeSolvedOrderingsGiveSameBox") {
    val board = place3SolvedTiles
    val tiles = board.tantrix.tileMap.values.toSeq
    val boxes = tiles.permutations.map(pts => calc.getBoundingBox(pts.toSeq)).toSeq
    assert(boxes.distinct.size == 1)
    assert(boxes.head == board.getBoundingBox)
  }

  test("sevenTileBlueLoopMatchesBoardBounds") {
    val board = place7SolvedTiles
    val fromSeq = calc.getBoundingBox(board.tantrix.tileMap.values.toSeq)
    assert(fromSeq == board.getBoundingBox)
  }

  test("expandSequenceMatchesIncrementalTantrixBox") {
    val placements = sevenTilesInABlueLoop
    var t = new Tantrix(Seq(placements.head))
    assert(t.getBoundingBox == calc.getBoundingBox(Seq(placements.head)))
    for (p <- placements.tail) {
      t = t.placeTile(p)
      val cumulative = placements.takeWhile(_ != p).appended(p)
      assert(t.getBoundingBox == calc.getBoundingBox(cumulative))
    }
  }
}
