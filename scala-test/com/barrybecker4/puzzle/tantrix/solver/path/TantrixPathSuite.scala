// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path

import com.barrybecker4.common.geometry.ByteLocation
import com.barrybecker4.puzzle.tantrix.PathTstUtil._
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil._
import com.barrybecker4.puzzle.tantrix.model.HexTiles._
import com.barrybecker4.puzzle.tantrix.model.Rotation._
import com.barrybecker4.puzzle.tantrix.model.{PathColor, TilePlacement}
import org.scalatest.funsuite.AnyFunSuite
import scala.util.Random

/**
  * @author Barry Becker
  */
class TantrixPathSuite extends AnyFunSuite {
  /** instance under test */
  private var path: TantrixPath = _

  test("path endpoint distance") {
    var result = ""
    for (testCase <- PathVerificationCase.cases) {
      val endPointDistance = testCase.path.getEndPointDistance
      val equality = if (endPointDistance == testCase.endPointDistance) "matched" else s"!= $endPointDistance"
      result += s"${testCase.name} endPointDistance = exp: ${testCase.endPointDistance} $equality \n"
    }
    assert(!result.contains("!="))
  }

  test("isLoop") {
    var result = ""
    for (testCase <- PathVerificationCase.cases) {
      val isLoop = testCase.path.isLoop
      val equality = if (isLoop == testCase.isLoop) "matched" else s"!= $isLoop"
      result += s"${testCase.name} isLoop = exp: ${testCase.isLoop} $equality \n"
    }
    assert(!result.contains("!="))
  }

  test("2TilePathConstruction") {
    val pivotTile = TILES.getTile(1)
    val first = TilePlacement(TILES.getTile(2), loc(2, 1), ANGLE_0)
    val second = TilePlacement(TILES.getTile(3), loc(1, 2), ANGLE_0)
    val tileList: List[TilePlacement] = List(first, second)
    path = new TantrixPath(tileList, pivotTile.primaryColor, 2)
    assertResult(tileList) { path.tiles }
  }

  /**
    * We should get an error if the tiles are not in path order, even if they
    * do form a path.
    */
  test("5TilePathConstructionWhenPathTilesUnordered") {
    val first = TilePlacement(TILES.getTile(4), new ByteLocation(21, 22), ANGLE_0)
    val second = TilePlacement(TILES.getTile(1), new ByteLocation(22, 21), ANGLE_300)
    val third = TilePlacement(TILES.getTile(2), new ByteLocation(23, 22), ANGLE_180)
    val fourth = TilePlacement(TILES.getTile(3), new ByteLocation(20, 21), ANGLE_120)
    val fifth = TilePlacement(TILES.getTile(5), new ByteLocation(21, 21), ANGLE_240)
    val tileList = List(first, second, third, fourth, fifth)
    val caught = intercept[IllegalStateException] {
      new TantrixPath(tileList, PathColor.RED, 5)
    }
    assert(caught.getMessage.contains("The following 5 tiles must form a primary path"))
  }

  /** we expect an exception because the tiles passed to the constructor do not form a primary path */
  test("NonLoopPathConstruction") {
    val board = place3UnsolvedTiles
    path = new TantrixPath(board.tantrix, board.primaryColor, board.numTiles, new Random(0))
    assertResult(3) { path.size }
  }

  /** we expect an exception because the tiles passed to the constructor do not form a primary path */
  test("InvalidPathConstruction") {
    val board = place3NonPathTiles
    val caught = intercept[IllegalStateException] {
      new TantrixPath(board.tantrix, board.primaryColor, board.numTiles, new Random(0))
    }
  }

  test("IsLoop") {
    val board = place3SolvedTiles
    val path = new TantrixPath(board.tantrix, board.primaryColor, board.numTiles, new Random(0))
    assert(path.isLoop)
  }

  test("IsNotLoop") {
    val board = place3UnsolvedTiles
    val path = new TantrixPath(board.tantrix, board.primaryColor, board.numTiles, new Random(0))
    assert(!path.isLoop)
  }

  test("HasOrderedPrimaryPathYellowOfLength3") {
    val tiles = List(
      TilePlacement(TILE2, LOWER_RIGHT, ANGLE_60),
      TilePlacement(TILE1, UPPER, ANGLE_0),
      TilePlacement(TILE3, LOWER_LEFT, ANGLE_120))
    assert(TantrixPath.hasOrderedPrimaryPath(tiles, PathColor.YELLOW))
  }

  test("HasOrderedPrimaryPathRedOfLength7") {
    val tiles = sevenTilesInAWrongRedLoop
    //println("tiles = " + tiles.mkString("\n"))
    assert(TantrixPath.hasOrderedPrimaryPath(tiles, PathColor.RED))
  }

  test("HasOrderedPrimaryPathRedOfLength3") {
    val tiles = List(
      TilePlacement(TILE2, LOWER_RIGHT, ANGLE_60),
      TilePlacement(TILE1, UPPER, ANGLE_0),
      TilePlacement(TILE3, LOWER_LEFT, ANGLE_120))
    assert(!TantrixPath.hasOrderedPrimaryPath(tiles, PathColor.RED))
  }

  /**
    * Expected
    * List([tileNum=3 colors: BLUE,BLUE,RED,RED,YELLOW,YELLOW at (row=22, column=20) ANGLE_120], [tileNum=1 colors: RED,BLUE,RED,BLUE,YELLOW,YELLOW at (row=21, column=21) ANGLE_0], [tileNum=2 colors: BLUE,YELLOW,YELLOW,BLUE,RED,RED at (row=22, column=21) ANGLE_60]),
    *
    * ([tileNum=3 colors: BLUE,BLUE,RED,RED,YELLOW,YELLOW at (row=22, column=20) ANGLE_120], [tileNum=1 colors: RED,BLUE,RED,BLUE,YELLOW,YELLOW at (row=21, column=21) ANGLE_0], [tileNum=2 colors: BLUE,YELLOW,YELLOW,BLUE,RED,RED at (row=22, column=21) ANGLE_0])
    */
  test("FindRandomNeighbor rad = 0.5") {
    val board = place3UnsolvedTiles
    val path = new TantrixPath(board.tantrix, board.primaryColor, board.numTiles, new Random(0))
    val nbr = path.getRandomNeighbor(0.5).asInstanceOf[TantrixPath]
    //println("nbr = " + nbr.toString)

    val tiles = Seq(
      TilePlacement(TILES.getTile(3), new ByteLocation(22, 20), ANGLE_120),
      TilePlacement(TILES.getTile(1), new ByteLocation(21, 21), ANGLE_0),
      TilePlacement(TILES.getTile(2), new ByteLocation(22, 21), ANGLE_0)) // was _60

    val expectedPath = new TantrixPath(tiles, PathColor.YELLOW, board.numTiles)
    assertResult(expectedPath) { nbr }
  }

  /*
  - FindRandomNeighbor rad = 1.0 *** FAILED *** (7 milliseconds)
    Expected List

    ([tileNum=3 colors: BLUE,BLUE,RED,RED,YELLOW,YELLOW at (row=22, column=20) ANGLE_120],
    [tileNum=1 colors: RED,BLUE,RED,BLUE,YELLOW,YELLOW at (row=21, column=21) ANGLE_0],
    [tileNum=2 colors: BLUE,YELLOW,YELLOW,BLUE,RED,RED at (row=22, column=21) ANGLE_60]),


    ([tileNum=3 colors: BLUE,BLUE,RED,RED,YELLOW,YELLOW at (row=22, column=20) ANGLE_120],
    [tileNum=1 colors: RED,BLUE,RED,BLUE,YELLOW,YELLOW at (row=21, column=21) ANGLE_0],
    [tileNum=2 colors: BLUE,YELLOW,YELLOW,BLUE,RED,RED at (row=22, column=21) ANGLE_0]) (TantrixPathSuite.scala:128)
    org.scalatest.exceptions.TestFailedException:
   */
  test("FindRandomNeighbor rad = 1.0") {
    val board = place3UnsolvedTiles
    val path = new TantrixPath(board.tantrix, board.primaryColor, board.numTiles, new Random(0))
    val nbr = path.getRandomNeighbor(1.0).asInstanceOf[TantrixPath]
    //println("nbr = " + nbr.toString)

    val tiles = Seq(
      TilePlacement(TILES.getTile(3), new ByteLocation(22, 20), ANGLE_120),
      TilePlacement(TILES.getTile(1), new ByteLocation(21, 21), ANGLE_0),
      TilePlacement(TILES.getTile(2), new ByteLocation(22, 21), ANGLE_0)) // was _60

    val expectedPath = new TantrixPath(tiles, PathColor.YELLOW, board.numTiles)
    assertResult(expectedPath) { nbr }
  }
}
