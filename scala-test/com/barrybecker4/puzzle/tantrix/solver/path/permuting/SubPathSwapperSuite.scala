// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path.permuting


import com.barrybecker4.common.geometry.ByteLocation
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil._
import com.barrybecker4.puzzle.tantrix.model.HexTiles.TILES
import com.barrybecker4.puzzle.tantrix.model.PathColor.PathColor
import com.barrybecker4.puzzle.tantrix.model.RotationEnum._
import com.barrybecker4.puzzle.tantrix.model.TilePlacement
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath
import org.scalatest.FunSuite

/**
  * @author Barry Becker
  */
class SubPathSwapperSuite extends FunSuite with SubPathMutatorBase {
  def createMutator(primaryColor: PathColor) = new SubPathSwapper(primaryColor)

  protected def verifyMutated1TilePath(resultPath: TantrixPath) {
    assertResult(1) { resultPath.size }
    val first = TilePlacement(TILES.getTile(2), loc(2, 0), ANGLE_300)
    val expList = Seq(first)
    assertResult(expList) { resultPath.tiles }
  }

  protected def verifyMutated2TilePath(resultPath: TantrixPath) {
    assertResult(2) { resultPath.size }
    val first = TilePlacement(TILES.getTile(2), loc(2, 0), ANGLE_300)
    val second = TilePlacement(TILES.getTile(3), loc(2, 1), ANGLE_300)
    val expList = Seq(first, second)
    assertResult(expList) { resultPath.tiles }
  }

  protected def verifyMutated3TilePath(resultPath: TantrixPath) {
    assertResult(3) { resultPath.size }
    val first = TilePlacement(TILES.getTile(1), new ByteLocation(19, 20), ANGLE_240)
    val second = TilePlacement(TILES.getTile(5), new ByteLocation(19, 21), ANGLE_0)
    val third = TilePlacement(TILES.getTile(2), new ByteLocation(19, 22), ANGLE_240)
    val expList = Seq(first, second, third)
    assertResult(expList) {resultPath.tiles }
  }

  protected def verifyMutated3aTilePath(resultPath: TantrixPath) {
    assertResult(3) { resultPath.size }
    val first = TilePlacement(TILES.getTile(2), new ByteLocation(23, 22), ANGLE_240)
    val second = TilePlacement(TILES.getTile(1), new ByteLocation(23, 21), ANGLE_0)
    val third = TilePlacement(TILES.getTile(4), new ByteLocation(22, 20), ANGLE_60)
    val expList = Seq(first, second, third)
    assertResult(expList) { resultPath.tiles }
  }
}
