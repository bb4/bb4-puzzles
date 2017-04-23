// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model

import com.barrybecker4.common.geometry.ByteLocation
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil._
import com.barrybecker4.puzzle.tantrix.model.HexTiles.TILES
import com.barrybecker4.puzzle.tantrix.model.RotationEnum._
import org.scalatest.FunSuite

/**
  * @author Barry Becker
  */
class TantrixBoardSuite extends FunSuite {
  /** instance under test */
  private[model] var board: TantrixBoard = _


  test("BoardConstruction") {
    board = place3UnsolvedTiles
    val expLastPlaced = TilePlacement(TILES.getTile(3), new ByteLocation(22, 21), ANGLE_180)
    assertResult(expLastPlaced) { board.getLastTile }
    assertResult(2) {board.getEdgeLength }
    assertResult(TILES.getTile(1).primaryColor) { board.primaryColor }
    assertResult(0) { board.unplacedTiles.size }
  }

  test("3TilesIsNotSolved") {
    board = place3UnsolvedTiles
    //System.out.println(board)
    assert(!board.isSolved)
  }

  test("3TilesIsSolved") {
    board = place3SolvedTiles
    //System.out.println(board)
    assert(board.isSolved)
  }

  test("4TilesIsNotSolved") {
    board = place4UnsolvedTiles
    assert(!board.isSolved)
  }

  test("4TilesIsSolved") {
    board = place4SolvedTiles
    assert(board.isSolved)
  }

  test("10TilesWithSpaceIsNotSolved") {
    board = place10LoopWithInnerSpace
    assert(!board.isSolved)
  }
}