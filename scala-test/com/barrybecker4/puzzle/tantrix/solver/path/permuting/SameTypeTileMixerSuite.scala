// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path.permuting

import com.barrybecker4.common.geometry.ByteLocation
import com.barrybecker4.puzzle.tantrix.PathTstUtil._
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil._
import com.barrybecker4.puzzle.tantrix.model.HexTiles.TILES
import com.barrybecker4.puzzle.tantrix.model.Rotation._
import com.barrybecker4.puzzle.tantrix.model._
import com.barrybecker4.puzzle.tantrix.solver.path.permuting.SameTypeTileMixerSuite.RND
import com.barrybecker4.puzzle.tantrix.solver.path.{PathType, TantrixPath}
import org.junit.Assert.assertEquals
import org.scalatest.funsuite.AnyFunSuite
import scala.collection.mutable.ListBuffer
import scala.util.Random

object SameTypeTileMixerSuite {
  val RND = new Random(1)
}

/**
  * @author Barry Becker
  */
class SameTypeTileMixerSuite extends AnyFunSuite {
  /** instance under test */
  private var mixer: SameTypeTileMixer = _


  test("Mix3TilesTIGHT") {
    val board = place3SolvedTiles
    mixer = new SameTypeTileMixer(PathType.TIGHT_CURVE, new TantrixPath(board.tantrix, board.primaryColor, RND), RND)
    val permutedPathList = mixer.findPermutedPaths
    assertResult(3) { permutedPathList.size }
    /* This is how it used to be. I think it is also correct,
               but then something changed - perhaps when upgrading to java 8.
            List<TantrixPath> expPathList = Arrays.asList(
                createPath(new TilePlacement(TILE3, LOWER_RIGHT, Rotation.ANGLE_240),
                           new TilePlacement(TILE2, UPPER, Rotation.ANGLE_180),
                           new TilePlacement(TILE1, LOWER_LEFT, Rotation.ANGLE_120)),
                createPath(new TilePlacement(TILE1, LOWER_RIGHT, Rotation.ANGLE_240),
                           new TilePlacement(TILE3, UPPER, Rotation.ANGLE_0),
                           new TilePlacement(TILE2, LOWER_LEFT, Rotation.ANGLE_300)),
                createPath(new TilePlacement(TILE1, LOWER_RIGHT, Rotation.ANGLE_240),
                           new TilePlacement(TILE2, UPPER, Rotation.ANGLE_180),
                           new TilePlacement(TILE3, LOWER_LEFT, Rotation.ANGLE_120))
            );*/
    val expPathList = ListBuffer(
      createPath(TilePlacement(TILE2, UPPER, ANGLE_180),
        TilePlacement(TILE3, LOWER_RIGHT, ANGLE_240), TilePlacement(TILE1, LOWER_LEFT, ANGLE_120)),
      createPath(TilePlacement(TILE3, UPPER, ANGLE_0),
        TilePlacement(TILE1, LOWER_RIGHT, ANGLE_240), TilePlacement(TILE2, LOWER_LEFT, ANGLE_300)),
      createPath(TilePlacement(TILE2, UPPER, ANGLE_180),
        TilePlacement(TILE1, LOWER_RIGHT, ANGLE_240), TilePlacement(TILE3, LOWER_LEFT, ANGLE_120))
    )
    /*
    val expPathList = ListBuffer(
      createPath(TilePlacement(TILE1, LOWER_LEFT, ANGLE_120), TilePlacement(TILE3, LOWER_RIGHT, ANGLE_240), TilePlacement(TILE2, UPPER, ANGLE_180)),
      createPath(TilePlacement(TILE2, LOWER_LEFT, ANGLE_300), TilePlacement(TILE1, LOWER_RIGHT, ANGLE_240), TilePlacement(TILE3, UPPER, ANGLE_0)),
      createPath(TilePlacement(TILE2, LOWER_LEFT, ANGLE_300), TilePlacement(TILE3, LOWER_RIGHT, ANGLE_240), TilePlacement(TILE1, UPPER, ANGLE_0)))*/
    assertResult( expPathList) { permutedPathList }
  }

  test("Mix11TilesWIDE_CURVE") {
    val board = place10LoopWithInnerSpace
    mixer = new SameTypeTileMixer(PathType.WIDE_CURVE, new TantrixPath(board.tantrix, board.primaryColor, RND), RND)
    val permutedPathList = mixer.findPermutedPaths
    assertResult(11) { permutedPathList.size }
    /* this is large
    val expPathList = ListBuffer(
      createPath(TilePlacement(TILE2, UPPER, ANGLE_180), TilePlacement(TILE3, LOWER_RIGHT, ANGLE_240), TilePlacement(TILE1, LOWER_LEFT, ANGLE_120)),
      createPath(TilePlacement(TILE3, UPPER, ANGLE_0), TilePlacement(TILE1, LOWER_RIGHT, ANGLE_240), TilePlacement(TILE2, LOWER_LEFT, ANGLE_300)),
      createPath(TilePlacement(TILE2, UPPER, ANGLE_180), TilePlacement(TILE1, LOWER_RIGHT, ANGLE_240), TilePlacement(TILE3, LOWER_LEFT, ANGLE_120))
    )
    assertResult( expPathList) { permutedPathList }
    */
  }

  test("Mix3TilesWIDE") {
    val board = place3SolvedTiles
    mixer = new SameTypeTileMixer(PathType.WIDE_CURVE, new TantrixPath(board.tantrix, board.primaryColor, RND), RND)
    val permutedPathList = mixer.findPermutedPaths
    assertEquals("Unexpected number of permuted paths.", 0, permutedPathList.size)
  }

  test("Mix3TilesSTRAIGHT") {
    val board = place3SolvedTiles
    mixer = new SameTypeTileMixer(PathType.STRAIGHT, new TantrixPath(board.tantrix, board.primaryColor, RND), RND)
    val permutedPathList = mixer.findPermutedPaths
    assertEquals("Unexpected number of permuted paths.", 0, permutedPathList.size)
  }

  test("Mix5TilesTIGHT") {
    val origPath = createPathOf5Tiles
    mixer = new SameTypeTileMixer(PathType.TIGHT_CURVE, origPath, RND)
    val permutedPathList = mixer.findPermutedPaths
    assertEquals("Unexpected number of permuted paths.", 1, permutedPathList.size)
    val expPathList = Seq(new TantrixPath(Seq(
      TilePlacement(TILES.getTile(5), new ByteLocation(21, 22), ANGLE_120),
      TilePlacement(TILES.getTile(2), new ByteLocation(20, 21), ANGLE_0),
      TilePlacement(TILES.getTile(3), new ByteLocation(21, 21), ANGLE_300),
      TilePlacement(TILES.getTile(1), new ByteLocation(20, 20), ANGLE_300),
      TilePlacement(TILES.getTile(4), new ByteLocation(19, 21), ANGLE_120)), PathColor.RED))
    assertEquals("Unexpected permuted paths.", expPathList, permutedPathList)
  }

  test("Mix5TilesWIDE") {
    val origPath = createPathOf5Tiles
    mixer = new SameTypeTileMixer(PathType.WIDE_CURVE, origPath, RND)
    val permutedPathList = mixer.findPermutedPaths
    assertEquals("Unexpected number of permuted paths.", 1, permutedPathList.size)
    val expPathList = Seq(new TantrixPath(Seq(
      TilePlacement(TILES.getTile(5), new ByteLocation(21, 22), ANGLE_120),
      TilePlacement(TILES.getTile(3), new ByteLocation(20, 21), ANGLE_120),
      TilePlacement(TILES.getTile(2), new ByteLocation(21, 21), ANGLE_180),
      TilePlacement(TILES.getTile(4), new ByteLocation(20, 20), ANGLE_180),
      TilePlacement(TILES.getTile(1), new ByteLocation(19, 21), ANGLE_120)), PathColor.RED))
    assertEquals("Unexpected permuted paths.", expPathList, permutedPathList)
  }

  test("Mix5TilesSTRAIGHT") {
    val origPath = createPathOf5Tiles
    mixer = new SameTypeTileMixer(PathType.STRAIGHT, origPath, RND)
    val permutedPathList = mixer.findPermutedPaths
    assertEquals("Unexpected number of permuted paths.", 0, permutedPathList.size)
  }

  private def createPathOf5Tiles = {
    val tiles = Seq(
      TilePlacement(TILES.getTile(5), new ByteLocation(21, 22), ANGLE_120),
      TilePlacement(TILES.getTile(3), new ByteLocation(20, 21), ANGLE_120),
      TilePlacement(TILES.getTile(2), new ByteLocation(21, 21), ANGLE_180),
      TilePlacement(TILES.getTile(1), new ByteLocation(20, 20), ANGLE_300),
      TilePlacement(TILES.getTile(4), new ByteLocation(19, 21), ANGLE_120))
    new TantrixPath(tiles, PathColor.RED)
  }

  private def createPath(placement1: TilePlacement, placement2: TilePlacement, placement3: TilePlacement) =
    new TantrixPath(ListBuffer(placement1, placement2, placement3).toSeq, PathColor.YELLOW)
}
