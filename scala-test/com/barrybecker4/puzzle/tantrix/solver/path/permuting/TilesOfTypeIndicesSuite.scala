// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path.permuting

import com.barrybecker4.puzzle.tantrix.PathTstUtil._
import com.barrybecker4.puzzle.tantrix.solver.path.PathType._
import org.junit.Assert.assertTrue
import org.scalatest.FunSuite
/**
  * @author Barry Becker
  */
class TilesOfTypeIndicesSuite extends FunSuite {

  /** instance under test */
  private var indices: TilesOfTypeIndices = _


  test("TIGHTIndicesIn3TileLoop") {
    indices = new TilesOfTypeIndices(TIGHT_CURVE, LOOP_PATH3)
    assertResult(Seq(0, 1, 2)) { indices.list }
  }

  test("WIDEIndicesIn3TileLoop") {
    indices = new TilesOfTypeIndices(WIDE_CURVE, LOOP_PATH3)
    assertTrue(indices.list.isEmpty)
  }

  test("TIGHTIndicesIn4TileNonLoopPath") {
    indices = new TilesOfTypeIndices(TIGHT_CURVE, NON_LOOP_PATH4)
    assertResult(Seq(1, 3)) {indices.list }
  }

  test("WIDEIndicesIn4TileNonLoopPath") {
    indices = new TilesOfTypeIndices(WIDE_CURVE, NON_LOOP_PATH4)
    assertResult(Seq(0, 2)) { indices.list }
  }

  //private static TantrixPath createPath(TilePlacement placement1, TilePlacement placement2, TilePlacement placement3) {
  //    return  new TantrixPath(new TilePlacementList(placement1, placement2, placement3), PathColor.YELLOW);
  //}
}
