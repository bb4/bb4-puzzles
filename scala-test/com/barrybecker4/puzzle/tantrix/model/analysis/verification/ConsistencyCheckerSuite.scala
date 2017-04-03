// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model.analysis.verification

import com.barrybecker4.puzzle.tantrix.PathTstUtil
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil._
import com.barrybecker4.puzzle.tantrix.model.PathColor._
import com.barrybecker4.puzzle.tantrix.model.Tantrix
import com.barrybecker4.puzzle.tantrix.solver.verification.ConsistencyChecker
import org.scalatest.FunSuite


/**
  * @author Barry Becker
  */
class ConsistencyCheckerSuite extends FunSuite {
  /** instance under test */
  private var checker: ConsistencyChecker = _
  private var tantrix: Tantrix = _

  /**
    * Check consistency of the first two placed tiles.
    */
  test("FitOnTwoWhereOnePossible") {
    tantrix = place2of3Tiles_OneThenTwo.tantrix
    checker = new ConsistencyChecker(tantrix.tiles, YELLOW)
    assertResult(2) { checker.numFittingTiles }
  }

  /**
    * Test consistency of loop formed by first 3 tiles.
    */
  test("FitOnThreeLoop") {
    tantrix = place3SolvedTiles.tantrix
    checker = new ConsistencyChecker(tantrix.tiles, YELLOW)
    assertResult(3) { checker.numFittingTiles }
  }

  /**
    * Consistency of tiles one and three.
    * 1
    * (2)  3
    */
  test("PlacementDoesNotFit0") {
    tantrix = place2of3Tiles_OneThenThree.tantrix
    checker = new ConsistencyChecker(tantrix.tiles, YELLOW)
    assertResult(2) { checker.numFittingTiles }
  }

  /** if only one tile placed, it is consistent but has not fits. */
  test("OneTilePlacementConsistency") {
    tantrix = place1of3Tiles_startingWithTile2.tantrix
    checker = new ConsistencyChecker(tantrix.tiles, YELLOW)
    assertResult(0) { checker.numFittingTiles }
  }

  /** Three loop has 3 fitting tiles. */
  test("LoopPath3Consistency") {
    val path = PathTstUtil.LOOP_PATH3
    checker = new ConsistencyChecker(path.getTilePlacements, YELLOW)
    assertResult(3) { checker.numFittingTiles }
  }

  /** Three tiles that are not in a loop. */
  test("Path3Consistency") {
    val path = PathTstUtil.NON_LOOP_PATH3
    checker = new ConsistencyChecker(path.getTilePlacements, YELLOW)
    assertResult(1) { checker.numFittingTiles }
    checker = new ConsistencyChecker(path.getTilePlacements, BLUE)
    assertResult(0) { checker.numFittingTiles }
  }

  /** Three loop has 3 fitting tiles. */
  test("LoopPath4Consistency") {
    val path = PathTstUtil.LOOP_PATH4
    checker = new ConsistencyChecker(path.getTilePlacements, RED)
    assertResult(4) { checker.numFittingTiles }
    checker = new ConsistencyChecker(path.getTilePlacements, YELLOW)
    assertResult(0) { checker.numFittingTiles }
  }

  /** Three tiles that are not in a loop. */
  test("Path4Consistency") {
    val path = PathTstUtil.NON_LOOP_PATH4
    checker = new ConsistencyChecker(path.getTilePlacements, RED)
    assertResult(2) { checker.numFittingTiles }
    checker = new ConsistencyChecker(path.getTilePlacements, YELLOW)
    assertResult(1) { checker.numFittingTiles }
  }
}