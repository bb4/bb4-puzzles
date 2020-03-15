/*
 * // Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
 */

package com.barrybecker4.puzzle.tantrix.model.analysis.fitting

import com.barrybecker4.puzzle.tantrix.TantrixTstUtil._
import com.barrybecker4.puzzle.tantrix.model._
import org.scalatest.funsuite.AnyFunSuite


/**
  * @author Barry Becker
  */
class TantrixTileFitterSuite extends AnyFunSuite  {
  
  private val TILES = new HexTiles
  /** instance under test */
  private var fitter: TantrixTileFitter = _
  private var tantrix: Tantrix = _

  /**
    * Here we ask if there are fits where three should be possible.
    * 1
    * (3)   2
    */
  test("FitOnTwoWhereOnePossible") {
    tantrix = place2of3Tiles_OneThenTwo.tantrix
    fitter = new TantrixTileFitter(tantrix, PathColor.YELLOW)
    val placements = fitter.getFittingPlacements(TILES.getTile(3), loc(2, 0))
    assertResult(1) { placements.size }
  }

  /**
    * Here we ask if there are fits where three should be possible.
    * 1
    * (4)   2
    */
  test("FitOnTwoWhereNonePossible") {
    tantrix = place2of3Tiles_OneThenTwo.tantrix
    fitter = new TantrixTileFitter(tantrix, PathColor.YELLOW)
    val placements = fitter.getFittingPlacements(TILES.getTile(4), loc(2, 0))
    assertResult(0) { placements.size }
  }

  /**
    * Here we ask if there are fits at a location where no primary path connections are possible.
    * 1   (3)
    * 2
    */
  test("FitOnTwoWhereNoPrimaryMatchPossible") {
    tantrix = place2of3Tiles_OneThenTwo.tantrix
    fitter = new TantrixTileFitter(tantrix, PathColor.YELLOW)
    val placements = fitter.getFittingPlacements(TILES.getTile(3), loc(1, 2))
    assertResult(0) { placements.size }
  }

  /**
    * Its not possible to have any primary path fits on a completed loop.
    * (4)    1
    * 3    2
    */
  test("FitOnThreeLoop") {
    tantrix = place3SolvedTiles.tantrix
    fitter = new TantrixTileFitter(tantrix, PathColor.YELLOW)
    val placements = fitter.getFittingPlacements(TILES.getTile(4), loc(1, 0))
    assertResult(0) { placements.size }
  }

  /**
    * In this case it will fit because we are only checking that one primary path matches,
    * but if we were being strict and using TileFitter it would not.
    * 1
    * (2)  3
    */
  test("PlacementDoesNotFit0") {
    tantrix = place2of3Tiles_OneThenThree.tantrix
    //System.out.println("tantrix=" + tantrix)
    val tile2 = TilePlacement(TILES.getTile(2), loc(2, 1), RotationEnum.ANGLE_0)
    fitter = new TantrixTileFitter(tantrix, PathColor.YELLOW)
    assert(fitter.isFit(tile2))
  }

  test("PlacementDoesNotFit60") {
    tantrix = place2of3Tiles_OneThenThree.tantrix
    val tile2 = TilePlacement(TILES.getTile(2), loc(2, 0), RotationEnum.ANGLE_60)
    fitter = new TantrixTileFitter(tantrix, PathColor.YELLOW)
    assert(!fitter.isFit(tile2))
  }

  test("PlacementFits") {
    tantrix = place2of3Tiles_OneThenThree.tantrix
    val tile2 = TilePlacement(TILES.getTile(2), loc(2, 0), RotationEnum.ANGLE_300)
    fitter = new TantrixTileFitter(tantrix, PathColor.YELLOW)
    assert(fitter.isFit(tile2))
  }

  test("Tile2PlacementFits") {
    tantrix = place1of3Tiles_startingWithTile2.tantrix
    val tile2 = TilePlacement(TILES.getTile(3), loc(0, 0), RotationEnum.ANGLE_60)
    fitter = new TantrixTileFitter(tantrix, PathColor.YELLOW)
    System.out.println(tantrix)
    assert(fitter.isFit(tile2))
  }
}
