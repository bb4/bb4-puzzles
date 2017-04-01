/*
 * // Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
 */

package com.barrybecker4.puzzle.tantrix.model.analysis.fitting

import com.barrybecker4.puzzle.tantrix.TantrixTstUtil
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil._
import com.barrybecker4.puzzle.tantrix.model.{PathColor, RotationEnum, Tantrix, TilePlacement}
import org.scalatest.FunSuite


/**
  * @author Barry Becker
  */
class PrimaryPathFitterSuite extends FunSuite  {

  /** instance under test */
  private var fitter: PrimaryPathFitter = _
  private var tantrix: Tantrix = _

  /**
    * Here we ask if there are fits where one should be possible.
    * 1
    * (3)   2
    */
  test("FitOnTwoWhereOnePossible") {
    tantrix = place2of3Tiles_OneThenTwo.tantrix
    fitter = new PrimaryPathFitter(tantrix, PathColor.YELLOW)
    val placements = fitter.getFittingPlacements(TILES.getTile(3), loc(2, 0))
    assertResult(1) { placements.size }
  }

  /**
    * Here we ask if there are fits where none should be possible.
    * 1
    * (4)   2
    */
  test("FitOnTwoWhereNonePossible") {
    tantrix = place2of3Tiles_OneThenTwo.tantrix
    fitter = new PrimaryPathFitter(tantrix, PathColor.YELLOW)
    val placements = fitter.getFittingPlacements(TILES.getTile(4), loc(2, 0))
    assertResult(0) { placements.size }
  }

  /**
    * Here we ask if there are fits at a location where no primary path connections are possible, but
    * we consider fits where no primary paths touch, hence 3 is expected.
    * 1   (3)
    * 2
    */
  test("FitOnTwoWhereNoPrimaryMatchPossible") {
    tantrix = place2of3Tiles_OneThenTwo.tantrix
    fitter = new PrimaryPathFitter(tantrix, PathColor.YELLOW)
    val placements = fitter.getFittingPlacements(TILES.getTile(3), loc(1, 2))
    println("tantrix=" + tantrix.tiles)
    println(" placements=" + placements)
    assertResult(3) { placements.size }
  }

  /**
    * Its not possible to have any primary path fits on a completed loop, but
    * we consider fits where no primary paths touch, hence 2 is expected
    * (4)    1
    * 3    2
    */
  test("FitOnThreeLoop") {
    tantrix = place3SolvedTiles.tantrix
    fitter = new PrimaryPathFitter(tantrix, PathColor.YELLOW)
    val placements = fitter.getFittingPlacements(TILES.getTile(4), loc(1, 0))
    assertResult(2) { placements.size }
  }

  /**
    * In this case it will fit because we are only checking that one primary path matches,
    * but if we were being strict and using TileFitter it would not.
    * 1
    * (2)  3
    */
  test("PlacementDoesNotFit0") {
    tantrix = place2of3Tiles_OneThenThree.tantrix
    println("tantrix=" + tantrix)
    val tile2 = TilePlacement(TantrixTstUtil.TILES.getTile(2), loc(2, 1), RotationEnum.ANGLE_0)
    fitter = new PrimaryPathFitter(tantrix, PathColor.YELLOW)
    assert(fitter.isFit(tile2))
  }

  test("PlacementDoesNotFit60") {
    tantrix = place2of3Tiles_OneThenThree.tantrix
    val tile2 = TilePlacement(TantrixTstUtil.TILES.getTile(2), loc(2, 0), RotationEnum.ANGLE_60)
    fitter = new PrimaryPathFitter(tantrix, PathColor.YELLOW)
    assert(!fitter.isFit(tile2))
  }

  test("PlacementFits") {
    tantrix = place2of3Tiles_OneThenThree.tantrix
    val tile2 = TilePlacement(TantrixTstUtil.TILES.getTile(2), loc(2, 0), RotationEnum.ANGLE_300)
    fitter = new PrimaryPathFitter(tantrix, PathColor.YELLOW)
    assert(fitter.isFit(tile2))
  }

  test("Tile2PlacementFits") {
    tantrix = place1of3Tiles_startingWithTile2.tantrix
    val tile2 = TilePlacement(TantrixTstUtil.TILES.getTile(3), loc(0, 0), RotationEnum.ANGLE_60)
    fitter = new PrimaryPathFitter(tantrix, PathColor.YELLOW)
    println(tantrix)
    assert(fitter.isFit(tile2))
  }

  /* The TILES form a path but not a loop */
  test("NumFitsFor3UnsolvedTiles") {
    tantrix = place3UnsolvedTiles.tantrix
    val fitter = new PrimaryPathFitter(tantrix, PathColor.YELLOW)
    assertResult(4) { fitter.numPrimaryFits }
  }

  /* The TILES do not even form a path */
  test("NumFitsFor3NonPathTiles") {
    tantrix = place3NonPathTiles.tantrix
    val fitter = new PrimaryPathFitter(tantrix, PathColor.YELLOW)
    assertResult(2) { fitter.numPrimaryFits }
  }
}
