// Copyright by Barry G. Becker, 2017 - 2023. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.verification

import com.barrybecker4.puzzle.tantrix.PathTstUtil
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil.*
import com.barrybecker4.puzzle.tantrix.solver.path.PathVerificationCase
import com.barrybecker4.puzzle.tantrix.model.PathColor.*
import com.barrybecker4.puzzle.tantrix.model.{PathColor, Tantrix}
import org.scalatest.funsuite.AnyFunSuite


/**
  * @author Barry Becker
  */
class ConsistencyCheckerSuite extends AnyFunSuite {
  /** instance under test */
  private var checker: ConsistencyChecker = _
  private var tantrix: Tantrix = _

  case class TestCase(pathCase: PathVerificationCase, color: PathColor)

  test("Check consistency of paths") {
    var result: String = ""
    for (testCase <- PathVerificationCase.cases) {
      val numFits =
        List(RED, YELLOW, BLUE)
          .map(color => (color, ConsistencyChecker(testCase.path.tiles, color).numFittingTiles))
          .toMap

      val equality = if (testCase.numFits.equals(numFits)) "matched" else s"!= $numFits"
      result += s"${testCase.name} = exp: ${testCase.numFits} $equality \n"
    }
    assert(!result.contains("!="))
  }

  /**
    * Check consistency of the first two placed tiles.
    */
  test("FitOnTwoWhereOnePossible") {
    tantrix = place2of3Tiles_OneThenTwo.tantrix
    checker = ConsistencyChecker(tantrix.tiles, YELLOW)
    assertResult(2) {
      checker.numFittingTiles
    }
  }

  /**
    * Test consistency of loop formed by first 3 tiles.
    */
  test("FitOnThreeLoop") {
    tantrix = place3SolvedTiles.tantrix
    checker = ConsistencyChecker(tantrix.tiles, YELLOW)
    assertResult(3) {
      checker.numFittingTiles
    }
  }

  /**
    * Consistency of tiles one and three.
    * 1
    * (2)  3
    */
  test("PlacementDoesNotFit0") {
    tantrix = place2of3Tiles_OneThenThree.tantrix
    checker = ConsistencyChecker(tantrix.tiles, YELLOW)
    assertResult(2) {
      checker.numFittingTiles
    }
  }

  /** if only one tile placed, it is consistent but has no fits. */
  test("OneTilePlacementConsistency") {
    tantrix = place1of3Tiles_startingWithTile2.tantrix
    checker = ConsistencyChecker(tantrix.tiles, YELLOW)
    assertResult(0) {
      checker.numFittingTiles
    }
  }
}