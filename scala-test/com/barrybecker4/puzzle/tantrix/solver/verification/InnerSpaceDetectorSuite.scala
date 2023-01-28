// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.verification

import com.barrybecker4.puzzle.tantrix.TantrixTstUtil
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil._
import com.barrybecker4.puzzle.tantrix.model.Tantrix
import org.junit.Assert.{assertFalse, assertTrue}
import org.scalatest.funsuite.AnyFunSuite

/**
  * @author Barry Becker
  */
class InnerSpaceDetectorSuite extends AnyFunSuite {

  /** instance under test */
  private var detector: InnerSpaceDetector = _
  private var tantrix: Tantrix = _
  
  
  /** Two tiles do not have a loop */
  test("ThatTwoTilesDoNotHaveInnerSpace") {
    tantrix = place2of3Tiles_OneThenTwo.tantrix
    verifyInnerSpaces(0)
  }

  /** The first three tiles are a yellow loop.  */
  test("ThreeLoopDoesNotHaveInnerSpace") {
    tantrix = place3SolvedTiles.tantrix
    verifyInnerSpaces(0)
  }

  test("PlacementDoesNotHaveInnerSpace") {
    tantrix = place2of3Tiles_OneThenThree.tantrix
    verifyInnerSpaces(0)
  }

  /** one tile cannot be a loop. */
  test("OneTileNoInnerSpace") {
    tantrix = place1of3Tiles_startingWithTile2.tantrix
    verifyInnerSpaces(0)
  }

  /** This is a loop, but it has inner space. */
  test("10TileLoopWithInnerSpace") {
    tantrix = place10LoopWithInnerSpace.tantrix
    verifyInnerSpaces(2)
  }

  /** This is almost a loop, but missing the tile that would complete it. */
  test("Jumbled9") {
    tantrix = TantrixTstUtil.placeJumbled9.tantrix
    verifyInnerSpaces(0)
  }

  test("place7LoopWrongColorTiles") {
    tantrix = TantrixTstUtil.place7LoopWrongColorTiles.tantrix
    verifyInnerSpaces(0)
  }

  test("place7SolvedTiles") {
    tantrix = TantrixTstUtil.place7SolvedTiles.tantrix
    verifyInnerSpaces(0)
  }

  test("place3of6UnsolvedTiles") {
    tantrix = TantrixTstUtil.place3of6UnsolvedTiles.tantrix
    verifyInnerSpaces(0)
  }

  test("place9AlmostLoop") {
    tantrix = TantrixTstUtil.place9AlmostLoop.tantrix
    verifyInnerSpaces(0)
  }

  /**
    * @param expNumInnerSpaces true if we expect one or more inner spaces.
    */
  private def verifyInnerSpaces(expNumInnerSpaces: Int): Unit = {
    detector = InnerSpaceDetector(tantrix)
    assertResult(expNumInnerSpaces, "Unexpected number of iner spaces") {
      detector.numInnerSpaces()
    }
  }
}
