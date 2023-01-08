// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path.permuting

import com.barrybecker4.common.geometry.ByteLocation
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil.loc
import com.barrybecker4.puzzle.tantrix.model.HexTiles.TILES
import com.barrybecker4.puzzle.tantrix.model.PathColor
import com.barrybecker4.puzzle.tantrix.model.Rotation._
import com.barrybecker4.puzzle.tantrix.model._
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath
import org.scalatest.funsuite.AnyFunSuite

/**
  * Base class for sub path mutator tests.
  * @author Barry Becker
  */
trait  SubPathMutatorBase extends AnyFunSuite {
  /** instance under test */
  private[permuting] var mutator: SubPathMutator = _

  /** creates the mutator to test */
  protected def createMutator(primaryColor: PathColor): SubPathMutator

  test("Mutating1TilePath") {
    val pivotTile = TILES.getTile(1)
    val pivotTilePlacement = TilePlacement(pivotTile, loc(1, 1), ANGLE_0)
    val firstTilePlacement = TilePlacement(TILES.getTile(2), loc(2, 1), ANGLE_0)
    var tileList = Seq[TilePlacement]()
    tileList :+= firstTilePlacement
    val path = new TantrixPath(tileList, PathColor.YELLOW, 1)
    mutator = createMutator(PathColor.YELLOW)
    val resultPath = mutator.mutate(pivotTilePlacement, path)
    verifyMutated1TilePath(resultPath)
  }

  test("Mutating2TilePath") {
    val pivotTile = TILES.getTile(1)
    val pivotTilePlacement = TilePlacement(pivotTile, loc(1, 1), ANGLE_0)
    val firstTilePlacement = TilePlacement(TILES.getTile(2), loc(2, 1), ANGLE_0)
    val secondTilePlacement = TilePlacement(TILES.getTile(3), loc(1, 2), ANGLE_0)
    val tileList = Seq(firstTilePlacement, secondTilePlacement)
    val path = new TantrixPath(tileList, PathColor.YELLOW, 2)
    mutator = createMutator(PathColor.YELLOW)
    val resultPath = mutator.mutate(pivotTilePlacement, path)
    verifyMutated2TilePath(resultPath)
  }

  test("Mutating3TilePath") {
    val pivotTile = TILES.getTile(3)
    val pivotTilePlacement = TilePlacement(pivotTile, new ByteLocation(20, 19), ANGLE_300)
    val firstTilePlacement = TilePlacement(TILES.getTile(1), new ByteLocation(19, 19), ANGLE_300)
    val secondTilePlacement = TilePlacement(TILES.getTile(5), new ByteLocation(18, 19), ANGLE_60)
    val thirdTilePlacement = TilePlacement(TILES.getTile(2), new ByteLocation(17, 20), ANGLE_300)
    val tileList = Seq(firstTilePlacement, secondTilePlacement, thirdTilePlacement)
    val path = new TantrixPath(tileList, PathColor.RED, 3)
    mutator = createMutator(PathColor.RED)
    val resultPath = mutator.mutate(pivotTilePlacement, path)
    verifyMutated3TilePath(resultPath)
  }

  test("Mutating3aTilePath") {
    val pivotTile = TILES.getTile(3)
    val pivotTilePlacement = TilePlacement(pivotTile, new ByteLocation(22, 21), ANGLE_120)
    val firstTilePlacement = TilePlacement(TILES.getTile(2), new ByteLocation(23, 21), ANGLE_180)
    val secondTilePlacement = TilePlacement(TILES.getTile(1), new ByteLocation(22, 20), ANGLE_300)
    val thirdTilePlacement = TilePlacement(TILES.getTile(4), new ByteLocation(21, 21), ANGLE_0)
    val tileList = Seq(firstTilePlacement, secondTilePlacement, thirdTilePlacement)
    val path = new TantrixPath(tileList, PathColor.RED, 3)
    mutator = createMutator(PathColor.RED)
    val resultPath = mutator.mutate(pivotTilePlacement, path)
    verifyMutated3aTilePath(resultPath)
  }

  test("Mutating3bTilePath") {
    val pivotTile = TILES.getTile(3)
    val pivotTilePlacement = TilePlacement(pivotTile, new ByteLocation(22, 21), ANGLE_120)
    val firstTilePlacement = TilePlacement(TILES.getTile(2), new ByteLocation(23, 21), ANGLE_180)
    val secondTilePlacement = TilePlacement(TILES.getTile(1), new ByteLocation(22, 20), ANGLE_300)
    val thirdTilePlacement = TilePlacement(TILES.getTile(4), new ByteLocation(21, 21), ANGLE_0)
    val tileList = Seq(firstTilePlacement, secondTilePlacement, thirdTilePlacement)
    val path = new TantrixPath(tileList, PathColor.RED, 4)
    mutator = createMutator(PathColor.RED)
    val resultPath = mutator.mutate(pivotTilePlacement, path)
    verifyMutated3aTilePath(resultPath)
  }

  protected def verifyMutated1TilePath(resultPath: TantrixPath): Unit
  protected def verifyMutated2TilePath(resultPath: TantrixPath): Unit
  protected def verifyMutated3TilePath(resultPath: TantrixPath): Unit
  protected def verifyMutated3aTilePath(resultPath: TantrixPath): Unit
}
