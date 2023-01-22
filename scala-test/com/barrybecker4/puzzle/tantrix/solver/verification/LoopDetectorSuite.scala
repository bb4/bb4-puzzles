// Copyright by Barry G. Becker, 2017 - 2023. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.verification

import com.barrybecker4.puzzle.tantrix.PathTstUtil.*
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil.*
import com.barrybecker4.puzzle.tantrix.model.PathColor.*
import com.barrybecker4.puzzle.tantrix.model.TantrixBoard
import com.barrybecker4.puzzle.tantrix.solver.path.PathVerificationCase
import org.junit.Assert.{assertFalse, assertTrue}
import org.scalatest.funsuite.AnyFunSuite


class LoopDetectorSuite extends AnyFunSuite {

  /** instance under test */
  private var detector: LoopDetector = _

  test("Loop Detection") {
    var result: String = ""
    for (testCase <- PathVerificationCase.cases) {
      val hasLoop =
        List(RED, YELLOW, BLUE)
          .map(color => (color, LoopDetector(new TantrixBoard(testCase.path.tiles, color)).hasLoop))
          .toMap
      val equality = if (hasLoop.equals(testCase.hasLoop)) "matched" else s"!= $hasLoop"
      result += s"${testCase.name} exp ${testCase.hasLoop} $equality \n"
    }
    assert(!result.contains("!="))
  }

  /**
    * Two tiles do not have a loop
    */
  test("ThatTwoTilesDoNotHaveLoop") {
    verifyHasLoop(place2of3Tiles_OneThenTwo, false)
  }

  /**
    * The first three tiles are a yellow loop.
    */
  test("FitOnThreeLoop(") {
    verifyHasLoop(place3SolvedTiles, true)
  }

  test("PlacementDoesNotFit0") {
    verifyHasLoop(place2of3Tiles_OneThenThree, false)
  }

  /** one tile cannot be a loop. */
  test("OneTileNotALoop") {
    verifyHasLoop(place1of3Tiles_startingWithTile2, false)
  }

  /** 10 tiles that are in a loop, but have inner space. Still a loop though. */
  test("LoopPath10LoopWithSpace(") {
    verifyHasLoop(place10LoopWithInnerSpace, true)
  }

  /**
    * @param expHasLoop true if we expect a loop.
    */
  private def verifyHasLoop(board: TantrixBoard, expHasLoop: Boolean): Unit = {
    detector = LoopDetector(board)
    if (expHasLoop) assertTrue("Unexpectedly did not have loop.", detector.hasLoop)
    else assertFalse("Unexpectedly had loop.", detector.hasLoop)
  }
}
