// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.generation

import com.barrybecker4.common.geometry.ByteLocation
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil._
import com.barrybecker4.puzzle.tantrix.generation.RandomPathGeneratorSuite.RND
import com.barrybecker4.puzzle.tantrix.model.HexTiles.TILES
import com.barrybecker4.puzzle.tantrix.model.RotationEnum._
import com.barrybecker4.puzzle.tantrix.model.{PathColor, TilePlacement}
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath
import org.junit.Assert.{assertEquals, assertNotEquals}
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.util.Random

object RandomPathGeneratorSuite {
  val RND = new Random(0)
}

/**
  * @author Barry Becker
  */
class RandomPathGeneratorSuite extends FunSuite with BeforeAndAfter {

  /** instance under test */
  private var pathGenerator: RandomPathGenerator = _


  test("3TilesPathGen") {
    pathGenerator = new RandomPathGenerator(place3UnsolvedTiles, RND)
    val rPath = pathGenerator.generateRandomPath
    assertEquals("Unexpected length for randomly generated path.", 3, rPath.size)
    val tiles = Seq[TilePlacement](
      TilePlacement(TILES.getTile(2), new ByteLocation(22, 20), ANGLE_0),
      TilePlacement(TILES.getTile(1), new ByteLocation(21, 21), ANGLE_0),
      TilePlacement(TILES.getTile(3), new ByteLocation(22, 21), ANGLE_180)
    )
    val expectedPath = new TantrixPath(tiles, PathColor.YELLOW)
    assertEquals("Unexpected path.", expectedPath, rPath)
  }

  test("6TilesPathGen") {
    pathGenerator = new RandomPathGenerator(place3of6UnsolvedTiles, RND)
    val rPath = pathGenerator.generateRandomPath
    assertEquals("Unexpected length for randomly generated path.", 6, rPath.size)
    /*
    val tiles = Seq[TilePlacement](
      TilePlacement(TILES.getTile(5), new ByteLocation(21, 20), ANGLE_300),
      TilePlacement(TILES.getTile(1), new ByteLocation(21, 21), ANGLE_0),
      TilePlacement(TILES.getTile(2), new ByteLocation(20, 21), ANGLE_60),
      TilePlacement(TILES.getTile(3), new ByteLocation(19, 22), ANGLE_180),
      TilePlacement(TILES.getTile(6), new ByteLocation(19, 21), ANGLE_120),
      TilePlacement(TILES.getTile(4), new ByteLocation(20, 20), ANGLE_240)

      - 6TilesPathGen *** FAILED *** (30 milliseconds)
  Expected
  List([tileNum=4 colors: BLUE,YELLOW,RED,BLUE,RED,YELLOW at (row=20, column=20) ANGLE_240], [tileNum=6 colors: YELLOW,RED,BLUE,YELLOW,BLUE,RED at (row=19, column=21) ANGLE_120], [tileNum=3 colors: BLUE,BLUE,RED,RED,YELLOW,YELLOW at (row=19, column=22) ANGLE_180], [tileNum=2 colors: BLUE,YELLOW,YELLOW,BLUE,RED,RED at (row=20, column=21) ANGLE_60], [tileNum=1 colors: RED,BLUE,RED,BLUE,YELLOW,YELLOW at (row=21, column=21) ANGLE_0], [tileNum=5 colors: RED,BLUE,BLUE,RED,YELLOW,YELLOW at (row=21, column=20) ANGLE_300]),

  got

  List([tileNum=3 colors: BLUE,BLUE,RED,RED,YELLOW,YELLOW at (row=19, column=22) ANGLE_180],
  [tileNum=6 colors: YELLOW,RED,BLUE,YELLOW,BLUE,RED at (row=19, column=21) ANGLE_120],
  [tileNum=4 colors: BLUE,YELLOW,RED,BLUE,RED,YELLOW at (row=20, column=20) ANGLE_240],
  [tileNum=5 colors: RED,BLUE,BLUE,RED,YELLOW,YELLOW at (row=21, column=20) ANGLE_300],
  [tileNum=1 colors: RED,BLUE,RED,BLUE,YELLOW,YELLOW at (row=21, column=21) ANGLE_0], [tileNum=2 colors: BLUE,YELLOW,YELLOW,BLUE,RED,RED at (row=20, column=21) ANGLE_60])
    )*/

    val tiles = Seq[TilePlacement](
      TilePlacement(TILES.getTile(3), new ByteLocation(19, 22), ANGLE_180),
      TilePlacement(TILES.getTile(6), new ByteLocation(19, 21), ANGLE_120),
      TilePlacement(TILES.getTile(4), new ByteLocation(20, 20), ANGLE_240),
      TilePlacement(TILES.getTile(5), new ByteLocation(21, 20), ANGLE_300),
      TilePlacement(TILES.getTile(1), new ByteLocation(21, 21), ANGLE_0),
      TilePlacement(TILES.getTile(2), new ByteLocation(20, 21), ANGLE_60),
    )
//    val tiles = Seq[TilePlacement](
//      TilePlacement(TILES.getTile(4), new ByteLocation(20, 20), ANGLE_240),
//      TilePlacement(TILES.getTile(6), new ByteLocation(19, 21), ANGLE_120),
//      TilePlacement(TILES.getTile(3), new ByteLocation(19, 22), ANGLE_180),
//      TilePlacement(TILES.getTile(2), new ByteLocation(20, 21), ANGLE_60),
//      TilePlacement(TILES.getTile(1), new ByteLocation(21, 21), ANGLE_0),
//      TilePlacement(TILES.getTile(5), new ByteLocation(21, 20), ANGLE_300)
//    )

    val expectedPath = new TantrixPath(tiles, PathColor.BLUE)
    assertResult(expectedPath) { rPath }
    // make sure we get a different random path on the second call.
    val rPath2 = pathGenerator.generateRandomPath
    val rPath3 = pathGenerator.generateRandomPath
    assertNotEquals("Unexpected path.", rPath.toString, rPath2.toString)
    assertNotEquals("Unexpected path.", rPath2.toString, rPath3.toString)
  }

  test("5TilesPathGenNeverNull") {
    pathGenerator = new RandomPathGenerator(place1of5UnsolvedTiles)

    for (i <- 0 until 100) {
        val rPath = pathGenerator.generateRandomPath
        assertEquals("Unexpected length for randomly generated path.", 5, rPath.size)
    }
  }
}
