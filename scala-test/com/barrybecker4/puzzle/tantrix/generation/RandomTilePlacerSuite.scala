// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.generation

import com.barrybecker4.common.geometry.IntLocation
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil._
import com.barrybecker4.puzzle.tantrix.generation.RandomTilePlacerSuite.RND
import com.barrybecker4.puzzle.tantrix.model.RotationEnum._
import com.barrybecker4.puzzle.tantrix.model.{PathColor, TantrixBoard, TilePlacement}
import org.junit.Assert.assertEquals
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.util.Random

object RandomTilePlacerSuite {
  val RND = new Random(0)
}

/**
  * @author Barry Becker
  */
class RandomTilePlacerSuite extends FunSuite with BeforeAndAfter {
  /** instance under test */
  private[generation] var placer : RandomTilePlacer = _
  private[generation] var tantrix: TantrixBoard = _


  test("FindRandomPlacementForFirstTileOfThreeYellowPath") {
    placer = new RandomTilePlacer(PathColor.YELLOW, RND)
    tantrix = new TantrixBoard(THREE_TILES)
    verifyPlacement(Some(TilePlacement(THREE_TILES(1), new IntLocation(22, 20), ANGLE_300)))
  }

  test("FindRandomPlacementForFirstTileOfThreeRedPath") {
    placer = new RandomTilePlacer(PathColor.RED, RND)
    tantrix = new TantrixBoard(THREE_TILES)
    verifyPlacement(Some(TilePlacement(THREE_TILES(2), new IntLocation(20, 20), ANGLE_240)))
  }

  test("FindRandomPlacementForTwoOfThree") {
    placer = new RandomTilePlacer(PathColor.YELLOW, RND)
    tantrix = place2of3Tiles_OneThenTwo
    verifyPlacement(Some(TilePlacement(THREE_TILES(2), new IntLocation(22, 20), ANGLE_120)))
  }

  /** no tile to place if already a loop */
  test("FindRandomPlacement3of6Unsolved") {
    placer = new RandomTilePlacer(PathColor.BLUE, RND)
    tantrix = place3of6UnsolvedTiles
    verifyPlacement(Some(TilePlacement(SIX_TILES(3), new IntLocation(19, 21), ANGLE_0)))
  }

  /** no tile to place if already a loop */
  test("FindRandomPlacement3Loop") {
    placer = new RandomTilePlacer(PathColor.YELLOW, RND)
    tantrix = place3SolvedTiles
    verifyPlacement(None)
  }

  /** no tile to place if already a loop */
  test("FindRandomPlacement3of6InLoop") {
    placer = new RandomTilePlacer(PathColor.YELLOW, RND)
    tantrix = place3of6SolvedTiles
    verifyPlacement(None)
  }

  test("FindRandomPlacementFor4UnsolvedTilesYellow") {
    placer = new RandomTilePlacer(PathColor.YELLOW, RND)
    tantrix = place4UnsolvedTiles
    verifyPlacement(None)
  }

  test("FindRandomPlacementFor4UnsolvedTilesRed") {
    placer = new RandomTilePlacer(PathColor.RED, RND)
    tantrix = place4UnsolvedTiles
    verifyPlacement(None)
  }

  test("FindRandomPlacementFor9AlmostLoopBlue") {
    placer = new RandomTilePlacer(PathColor.BLUE, RND)
    tantrix = place9AlmostLoop
    verifyPlacement(Some(TilePlacement(FOURTEEN_TILES(13), new IntLocation(21, 23), ANGLE_180)))
  }

  test("FindRandomPlacementFor9AlmostLoopRed") {
    placer = new RandomTilePlacer(PathColor.RED, RND)
    tantrix = place9AlmostLoop
    verifyPlacement(Some(TilePlacement(FOURTEEN_TILES(11), new IntLocation(21, 22), ANGLE_300)))
  }

  private def verifyPlacement(expPlacement: Option[TilePlacement]) {
    val placement = placer.generateRandomPlacement(tantrix)
    assertEquals("Unexpected placement.", expPlacement, placement)
  }
}