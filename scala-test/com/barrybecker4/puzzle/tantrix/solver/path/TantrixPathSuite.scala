// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path

import scala.compiletime.uninitialized
import com.barrybecker4.common.geometry.ByteLocation
import com.barrybecker4.puzzle.tantrix.PathTstUtil._
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil._
import com.barrybecker4.puzzle.tantrix.model.HexTiles._
import com.barrybecker4.puzzle.tantrix.model.Rotation._
import com.barrybecker4.puzzle.tantrix.model.{PathColor, TilePlacement}
import org.scalatest.funsuite.AnyFunSuite
import scala.util.Random

/**
  */
class TantrixPathSuite extends AnyFunSuite {
  /** instance under test */
  private var path: TantrixPath = uninitialized

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

  test("HasOrderedPrimaryPath empty or one tile is trivially true") {
    assert(TantrixPath.hasOrderedPrimaryPath(Nil, PathColor.YELLOW))
    assert(TantrixPath.hasOrderedPrimaryPath(List(TilePlacement(TILE1, UPPER, ANGLE_0)), PathColor.YELLOW))
  }

  test("subPath forward slice matches reverse subPath") {
    val tiles = List(
      TilePlacement(TILE2, LOWER_RIGHT, ANGLE_60),
      TilePlacement(TILE1, UPPER, ANGLE_0),
      TilePlacement(TILE3, LOWER_LEFT, ANGLE_120))
    val path = new TantrixPath(tiles, PathColor.YELLOW, 3)
    assert(path.subPath(0, 1).tiles == tiles.take(2))
    assert(path.subPath(2, 0).tiles == tiles.reverse)
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
      TilePlacement(TILES.getTile(2), new ByteLocation(22, 21), ANGLE_60))

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
      TilePlacement(TILES.getTile(2), new ByteLocation(22, 21), ANGLE_60))

    val expectedPath = new TantrixPath(tiles, PathColor.YELLOW, board.numTiles)
    assertResult(expectedPath) { nbr }
  }

  test("random samples are different paths through the same tiles") {
    val original = LOOP_PATH7
    val samples = (0 until 50).map(_ => original.getRandomSample.asInstanceOf[TantrixPath])
    assert(samples.toSet.size > 1, "all the random samples were the same path")
    for (sample <- samples) {
      assertResult(original.tiles.map(_.tile).toSet) { sample.tiles.map(_.tile).toSet }
      assertResult(original.size) { sample.size }
      assertResult(original.primaryPathColor) { sample.primaryPathColor }
    }
  }

  /** The tile 10 leads to tile 2, but tile 2 leads to other places. Tile 2 is next to the end of the path, but not connected. */
  test("a tile that leads to the one before it, but is not led to by it, is not a primary path") {
    val tile2 = TilePlacement(TILES.getTile(2), loc(26, 21), ANGLE_0)
    val tile10 = TilePlacement(TILES.getTile(10), loc(27, 21), ANGLE_300)
    assert(tile10.hasPathTo(PathColor.YELLOW, tile2.location))
    assert(!tile2.hasPathTo(PathColor.YELLOW, tile10.location))
    assert(!TantrixPath.hasOrderedPrimaryPath(Seq(tile2, tile10), PathColor.YELLOW))
    assert(!TantrixPath.hasOrderedPrimaryPath(Seq(tile10, tile2), PathColor.YELLOW))
    intercept[IllegalStateException] {
      new TantrixPath(Seq(tile2, tile10), PathColor.YELLOW, 2)
    }
  }

  test("when the tiles are put in another order by a crossover, the result is a primary path with the same tiles") {
    import com.barrybecker4.optimization.parameter.types.IntegerParameter
    val board = place3UnsolvedTiles
    val original = new TantrixPath(board.tantrix, board.primaryColor, board.numTiles, new Random(0))
    for (order <- Seq(0, 1, 2).permutations) {
      val params = order.map(id => IntegerParameter(id, 0, 2, "tile")).toIndexedSeq
      val rebuilt = original.rebuildAfterOrderCrossover(params, new Random(1)).asInstanceOf[TantrixPath]
      assert(TantrixPath.hasOrderedPrimaryPath(rebuilt.tiles, original.primaryPathColor), s"order $order")
      assertResult(original.tiles.toSet)(rebuilt.tiles.toSet)
    }
  }
}
