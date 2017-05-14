// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.verification

import com.barrybecker4.puzzle.tantrix.PathTstUtil._
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil._
import com.barrybecker4.puzzle.tantrix.model.PathColor._
import com.barrybecker4.puzzle.tantrix.model.TantrixBoard
import org.junit.Assert.{assertFalse, assertTrue}
import org.scalatest.FunSuite

/**
  * @author Barry Becker
  */
class LoopDetectorSuite extends FunSuite {

  /** instance under test */
  private var detector: LoopDetector = _
  private var board: TantrixBoard = _

  /**
    * Two tiles do not have a loop
    */
  test("ThatTwoTilesDoNotHaveLoop") {
    board = place2of3Tiles_OneThenTwo
    verifyHasLoop(false)
  }

  /**
    * The first three tiles are a yellow loop.
    */
  test("FitOnThreeLoop(") {
    board = place3SolvedTiles
    verifyHasLoop(true)
  }

  test("PlacementDoesNotFit0") {
    board = place2of3Tiles_OneThenThree
    verifyHasLoop(false)
  }

  /** one tile cannot be a loop. */
  test("OneTileNotALoop") {
    board = place1of3Tiles_startingWithTile2
    verifyHasLoop(false)
  }

  /** 10 tiles that are in a loop, but have inner space. Still a loop though. */
  test("LoopPath10LoopWithSpace(") {
    board = place10LoopWithInnerSpace
    verifyHasLoop(true)
  }

  /** Three loop has 3 fitting tiles. */
  test("LoopPath3HasLoop") {
    val path = LOOP_PATH3
    board = new TantrixBoard(path.getTilePlacements, YELLOW)
    verifyHasLoop(true)
    // though there is a yellow loop, there should not be a blue loop
    board = new TantrixBoard(path.getTilePlacements, BLUE)
    verifyHasLoop(false)
  }

  /** Three tiles that are not in a loop. */
  test("Path3WithNoLoop") {
    val path = NON_LOOP_PATH3
    board = new TantrixBoard(path.getTilePlacements, YELLOW)
    verifyHasLoop(false)
  }

  /** Three loop has 3 fitting tiles. */
  test("LoopPath4HasLoop") {
    val path = LOOP_PATH4
    board = new TantrixBoard(path.getTilePlacements, RED)
    verifyHasLoop(true)
  }

  /** Three tiles that are not in a loop. */
  test("LoopPath4NoLoop(") {
    val path = NON_LOOP_PATH4
    board = new TantrixBoard(path.getTilePlacements, RED)
    verifyHasLoop(false)
  }

  /**
    * @param expHasLoop true if we expect a loop.
    */
  private def verifyHasLoop(expHasLoop: Boolean) {
    detector = new LoopDetector(board)
    if (expHasLoop) assertTrue("Unexpectedly did not have loop.", detector.hasLoop)
    else assertFalse("Unexpectedly had loop.", detector.hasLoop)
  }
}