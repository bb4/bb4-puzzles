// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.verification

import com.barrybecker4.puzzle.tantrix.TantrixTstUtil
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil._
import com.barrybecker4.puzzle.tantrix.model.Tantrix
import org.junit.Assert.{assertFalse, assertTrue}
import org.scalatest.FunSuite

/**
  * @author Barry Becker
  */
class InnerSpaceDetectorSuite extends FunSuite {

  /** instance under test */
  private var detector: InnerSpaceDetector = _
  private var tantrix: Tantrix = _

  /** Two tiles do not have a loop */
  test("ThatTwoTilesDoNotHaveInnerSpace") {
    tantrix = place2of3Tiles_OneThenTwo.tantrix
    verifyHasInnerSpaces(false)
  }

  /** The first three tiles are a yellow loop.  */
  test("ThreeLoopDoesNotHaveInnerSpace") {
    tantrix = place3SolvedTiles.tantrix
    verifyHasInnerSpaces(false)
  }

  test("PlacementDoesNotHaveInnerSpace") {
    tantrix = place2of3Tiles_OneThenThree.tantrix
    verifyHasInnerSpaces(false)
  }

  /** one tile cannot be a loop. */
  test("OneTileNoInnerSpace") {
    tantrix = place1of3Tiles_startingWithTile2.tantrix
    verifyHasInnerSpaces(false)
  }

  /** This is a loop, but it has inner space. */
  test("10TileLoopWithInnerSpace") {
    tantrix = place10LoopWithInnerSpace.tantrix
    verifyHasInnerSpaces(true)
  }

  /** This is almost a loop, but missing the tile that would complete it. */
  test("Jumbled9") {
    tantrix = TantrixTstUtil.placeJumbled9.tantrix
    verifyHasInnerSpaces(false)
  }

  test("place7LoopWrongColorTiles") {
    tantrix = TantrixTstUtil.place7LoopWrongColorTiles.tantrix
    verifyHasInnerSpaces(false)
  }

  test("place7SolvedTiles") {
    tantrix = TantrixTstUtil.place7SolvedTiles.tantrix
    verifyHasInnerSpaces(false)
  }

  test("place3of6UnsolvedTiles") {
    tantrix = TantrixTstUtil.place3of6UnsolvedTiles.tantrix
    verifyHasInnerSpaces(false)
  }

  test("place9AlmostLoop") {
    tantrix = TantrixTstUtil.place9AlmostLoop.tantrix
    verifyHasInnerSpaces(false)
  }

  /**
    * @param expHasInnerSpace true if we expect one or more inner spaces.
    */
  private def verifyHasInnerSpaces(expHasInnerSpace: Boolean): Unit = {
    detector = new InnerSpaceDetector(tantrix)
    if (expHasInnerSpace) assertTrue("Unexpectedly did not have inner spaces.", detector.hasInnerSpaces)
    else assertFalse("Unexpectedly had inner spaces.", detector.hasInnerSpaces)
  }
}
