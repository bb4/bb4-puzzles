// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path.permuting

import com.barrybecker4.puzzle.tantrix.PathTstUtil._
import com.barrybecker4.puzzle.tantrix.model.RotationEnum._
import com.barrybecker4.puzzle.tantrix.model.{PathColor, TilePlacement}
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath
import org.junit.Assert.assertEquals
import org.junit.Test

import scala.collection.mutable.ListBuffer

/**
  * @author Barry Becker
  */
class PathTilePermuterSuite {
  /** instance under test */
  private var permuter: PathTilePermuter = _
  

  @Test def testPermute3TileLoopFirstTwoPermuted() {
    val path = LOOP_PATH3
    permuter = new PathTilePermuter(path)
    val permutedPath = permuter.permute(ListBuffer(0, 1), ListBuffer(1, 0))
    val expPath = createPath(
      TilePlacement(TILE1, LOWER_RIGHT, ANGLE_240), 
      TilePlacement(TILE2, UPPER, ANGLE_180), 
      TilePlacement(TILE3, LOWER_LEFT, ANGLE_120)
    )
    assertEquals("Unexpected permuted path.", expPath, permutedPath)
  }

  @Test def testPermute3TileLoopSecond2Permuted() {
    val path = LOOP_PATH3
    permuter = new PathTilePermuter(path)
    val permutedPath = permuter.permute(ListBuffer(1, 2), ListBuffer(2, 1))
    val expPath = createPath(
      TilePlacement(TILE2, LOWER_RIGHT, ANGLE_60),
      TilePlacement(TILE3, UPPER, ANGLE_0),
      TilePlacement(TILE1, LOWER_LEFT, ANGLE_120)
    )
    assertEquals("Unexpected permuted path.", expPath, permutedPath)
  }

  @Test def testPermute3TileLoopAll3PermutedA() {
    val path = LOOP_PATH3
    permuter = new PathTilePermuter(path)
    val permutedPath = permuter.permute(ListBuffer(0, 1, 2), ListBuffer(2, 1, 0))
    val expPath = createPath(
      TilePlacement(TILE3, LOWER_RIGHT, ANGLE_240),
      TilePlacement(TILE1, UPPER, ANGLE_0),
      TilePlacement(TILE2, LOWER_LEFT, ANGLE_300)
    )
    assertEquals("Unexpected permuted path.", expPath, permutedPath)
  }

  @Test def testPermute3TileLoopAll3PermutedB() {
    val path = LOOP_PATH3
    permuter = new PathTilePermuter(path)
    val permutedPath = permuter.permute(ListBuffer(0, 1, 2), ListBuffer(1, 2, 0))
    val expPath = createPath(
      TilePlacement(TILE1, LOWER_RIGHT, ANGLE_240),
      TilePlacement(TILE3, UPPER, ANGLE_0),
      TilePlacement(TILE2, LOWER_LEFT, ANGLE_300)
    )
    assertEquals("Unexpected permuted path.", expPath, permutedPath)
  }

  @Test def testPermute3TileNonLoopPath1() {
    val path = NON_LOOP_PATH3
    permuter = new PathTilePermuter(path)
    val permutedPath = permuter.permute(ListBuffer(0, 1), ListBuffer(1, 0))
    val expPath = createPath(TilePlacement(TILE1, LOWER_RIGHT, ANGLE_240), TilePlacement(TILE2, UPPER, ANGLE_180), TilePlacement(TILE3, LOWER_LEFT, ANGLE_120))
    assertEquals("Unexpected permuted path.", expPath, permutedPath)
  }

  @Test def testSwapIn3TileNonLoopPath2() {
    val path = NON_LOOP_PATH3
    permuter = new PathTilePermuter(path)
    val permutedPath = permuter.permute(ListBuffer(1, 2), ListBuffer(2, 1))
    val expPath = createPath(
      TilePlacement(TILE2, LOWER_RIGHT, ANGLE_0),
      TilePlacement(TILE3, UPPER, ANGLE_0),
      TilePlacement(TILE1, LOWER_LEFT, ANGLE_180)
    )
    assertEquals("Unexpected permuted path.", expPath, permutedPath)
  }

  @Test def testSwapIn4TileLoopPathWideArc() {
    val path = LOOP_PATH4
    permuter = new PathTilePermuter(path)
    permuter.permute(ListBuffer(0, 2), ListBuffer(2, 0))
  }

  @Test def testSwapIn4TileNonLoopPathWideArc() {
    val path = NON_LOOP_PATH4
    permuter = new PathTilePermuter(path)
    permuter.permute(ListBuffer(0, 2), ListBuffer(2, 0))
  }

  @Test def testSwapIn4TileNonLoopPathTightArc() {
    val path = NON_LOOP_PATH4
    permuter = new PathTilePermuter(path)
    val permutedPath = permuter.permute(ListBuffer(1, 3), ListBuffer(3, 1))
    val expPath = new TantrixPath(Seq(
      TilePlacement(TILE1, LOWER_LEFT, ANGLE_120),
      TilePlacement(TILE3, UPPER_LEFT, ANGLE_180),
      TilePlacement(TILE4, UPPER, ANGLE_60),
      TilePlacement(TILE2, LOWER_RIGHT, ANGLE_180)
    ), PathColor.RED)
    assertEquals("Unexpected permuted path.", expPath, permutedPath)
  }

  /** Exception if the tiles cannot be swapped.
    * Should not be able to swap a tile with itself.
    */
  @Test(expected = classOf[AssertionError]) def testSwapDuplicateIdices() {
    permuter = new PathTilePermuter(NON_LOOP_PATH3)
    permuter.permute(ListBuffer(0, 0), ListBuffer(0, 0))
  }

  /** Exception if the tiles cannot be swapped.
    * The permutation indices need to match.
    */
  @Test(expected = classOf[AssertionError]) def testSwapInvalidIndices() {
    permuter = new PathTilePermuter(NON_LOOP_PATH3)
    permuter.permute(ListBuffer(0, 2), ListBuffer(0, 1))
  }
}
