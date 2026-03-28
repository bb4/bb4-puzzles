// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.generation

import com.barrybecker4.puzzle.tantrix.TantrixTstUtil._
import com.barrybecker4.puzzle.tantrix.model.HexTiles.TILES
import com.barrybecker4.puzzle.tantrix.model.TantrixBoard
import org.scalatest.funsuite.AnyFunSuite

/**
  * Every generated move must physically fit the board; violations here usually mean
  * border or move-generation bugs.
  */
class MoveGeneratorFitsSuite extends AnyFunSuite {

  private def assertAllMovesFit(board: TantrixBoard): Unit = {
    val gen = new MoveGenerator(board)
    val moves = gen.generateMoves
    assert(moves.nonEmpty || board.unplacedTiles.isEmpty,
      "Expected moves while tiles remain unplaced")
    moves.foreach { m =>
      assert(board.fits(m), s"MoveGenerator produced non-fitting move $m on board:\n$board")
    }
  }

  test("allMovesFit_twoOfThreeVariants") {
    assertAllMovesFit(place2of3Tiles_OneThenTwo)
    assertAllMovesFit(place2of3Tiles_OneThenThree)
  }

  test("allMovesFit_partialSixTileLayouts") {
    assertAllMovesFit(place3of6UnsolvedTiles)
    assertAllMovesFit(place3of6SolvedTiles)
  }

  test("allMovesFit_firstTileOnly") {
    assertAllMovesFit(new TantrixBoard(TILES.createOrderedList(4)))
  }

  test("allMovesFit_fourteenTileAlmostLoop") {
    assertAllMovesFit(place9AlmostLoop)
    assertAllMovesFit(placeJumbled9)
  }
}
