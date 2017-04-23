// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path

import com.barrybecker4.common.geometry.ByteLocation
import com.barrybecker4.puzzle.tantrix.PathTstUtil._
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil._
import com.barrybecker4.puzzle.tantrix.model.HexTiles.TILES
import com.barrybecker4.puzzle.tantrix.model.RotationEnum._
import com.barrybecker4.puzzle.tantrix.model.{PathColor, TilePlacement}
import com.barrybecker4.puzzle.tantrix.solver.path.permuting.PathPivotPermuter
import org.scalatest.FunSuite

/**
  * @author Barry Becker
  */
class PathPivotPermuterSuite extends FunSuite {

  /** instance under test */
  private var permuter: PathPivotPermuter = _

  test("Permute3TilePath") {
    val board = place3UnsolvedTiles
    permuter = new PathPivotPermuter(new TantrixPath(board.tantrix, board.primaryColor))
    val permutedPathList = permuter.findPermutedPaths(1, 1)
    assertResult(7) { permutedPathList.size }

    // for each of the 7 permuted paths, we expect that tile 2 will be the middle/pivot tile.
    val lowerLeft = new ByteLocation(22, 20)
    val lowerRight = new ByteLocation(22, 21)
    val pivot = TilePlacement(TILES.getTile(1), new ByteLocation(21, 21), ANGLE_0)
    val tile2 = TILES.getTile(2)
    val tile3 = TILES.getTile(3)
    val expPathList = Array(
      createPath(TilePlacement(tile2, lowerLeft, ANGLE_0), pivot, TilePlacement(tile3, lowerRight, ANGLE_240)),
      createPath(TilePlacement(tile2, lowerLeft, ANGLE_300), pivot, TilePlacement(tile3, lowerRight, ANGLE_180)),
      createPath(TilePlacement(tile2, lowerLeft, ANGLE_300), pivot, TilePlacement(tile3, lowerRight, ANGLE_240)), // complete loop!
      createPath(TilePlacement(tile3, lowerLeft, ANGLE_120), pivot, TilePlacement(tile2, lowerRight, ANGLE_60)),
      createPath(TilePlacement(tile3, lowerLeft, ANGLE_120), pivot, TilePlacement(tile2, lowerRight, ANGLE_0)),
      createPath(TilePlacement(tile3, lowerLeft, ANGLE_180), pivot, TilePlacement(tile2, lowerRight, ANGLE_60)), // complete loop!
      createPath(TilePlacement(tile3, lowerLeft, ANGLE_180), pivot, TilePlacement(tile2, lowerRight, ANGLE_0)))
    assertResult(expPathList) { permutedPathList }
  }

  test("Permute9AlmostLoop") {
    val board = place9AlmostLoop
    permuter = new PathPivotPermuter(new TantrixPath(board.tantrix, PathColor.RED))
    val permutedPathList = permuter.findPermutedPaths(1, 1)
    assertResult(7) { permutedPathList.size }
  }
}
