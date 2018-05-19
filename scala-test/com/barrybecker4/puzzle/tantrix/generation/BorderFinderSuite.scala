// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.generation

import com.barrybecker4.common.geometry.IntLocation
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil._
import com.barrybecker4.puzzle.tantrix.model.{PathColor, Tantrix, TantrixBoard}
import org.junit.Assert.assertEquals
import org.scalatest.FunSuite

/**
  * @author Barry Becker
  */
class BorderFinderSuite extends FunSuite {

  /** instance under test */
  private[generation] var borderFinder: BorderFinder = _
  private[generation] var tantrix: Tantrix = _

  test("FindBorderForFirstTileOfThree") {
    tantrix = new TantrixBoard(THREE_TILES).tantrix
    borderFinder = new BorderFinder(tantrix, 10, PathColor.YELLOW)
    verifyBorderLocations(IntLocation(22, 20), IntLocation(22, 21))
  }

  test("FindBorderForTwoOfThreeTilesA") {
    tantrix = place2of3Tiles_OneThenTwo.tantrix
    borderFinder = new BorderFinder(tantrix, 10, PathColor.YELLOW)
    verifyBorderLocations(IntLocation(22, 20))
  }

  test("FindBorderForTwoOfThreeTilesB") {
    tantrix = place2of3Tiles_OneThenThree.tantrix
    borderFinder = new BorderFinder(tantrix, 10, PathColor.YELLOW)
    verifyBorderLocations(IntLocation(22, 20))
  }

  test("FindBorderForThreeSolvedTiles") {
    tantrix = place3SolvedTiles.tantrix
    borderFinder = new BorderFinder(tantrix, 10, PathColor.YELLOW)
    verifyBorderLocations()
  }

  test("FindBorderForTwoOfThreeTilesA_ConstrainedByBorder") {
    tantrix = place2of3Tiles_OneThenTwo.tantrix
    borderFinder = new BorderFinder(tantrix, 1, PathColor.YELLOW)
    verifyBorderLocations(IntLocation(22, 20))
  }

  test("FindBorderForTwoOfThreeTilesB_ConstrainedByBorder") {
    tantrix = place2of3Tiles_OneThenThree.tantrix
    borderFinder = new BorderFinder(tantrix, 1, PathColor.YELLOW)
    verifyBorderLocations(IntLocation(22, 20))
  }

  test("FindBorderFor4TilesNonLoop4Pieces") {
    tantrix = place4UnsolvedTiles.tantrix
    borderFinder = new BorderFinder(tantrix, 4, PathColor.RED)
    verifyBorderLocations(IntLocation(22, 20))
  }

  test("FindBorderFor4TilesNonLoopFewPieces") {
    tantrix = place4UnsolvedTiles.tantrix
    borderFinder = new BorderFinder(tantrix, 6, PathColor.RED)
    verifyBorderLocations(IntLocation(22, 20))
  }

  test("FindBorderFor4TilesNonLoopManyPieces") {
    tantrix = place4UnsolvedTiles.tantrix
    borderFinder = new BorderFinder(tantrix, 20, PathColor.RED)
    verifyBorderLocations(IntLocation(22, 20))
  }

  test("FindBorderFor4TileLoop") {
    tantrix = place4SolvedTiles.tantrix
    borderFinder = new BorderFinder(tantrix, 6, PathColor.RED)
    verifyBorderLocations()
  }

  test("FindBorderFor10TileLoopWithInnerSpace") {
    tantrix = place10LoopWithInnerSpace.tantrix
    borderFinder = new BorderFinder(tantrix, 12, PathColor.RED)
    verifyBorderLocations()
  }

  test("FindBorderFor9TilesAlmostLoop") {
    tantrix = place9AlmostLoop.tantrix
    borderFinder = new BorderFinder(tantrix, 16, PathColor.RED)
    verifyBorderLocations(IntLocation(21, 22))
  }

  private def verifyBorderLocations(locations: IntLocation*) {
    val positions = borderFinder.findBorderPositions
    assertEquals("Unexpected number of border locations.", locations.toSet, positions)
  }
}