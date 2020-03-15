// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.hiq.model

import org.junit.Assert.assertEquals
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite

/**
  * @author Barry Becker
  */
class MoveGeneratorSuite extends AnyFunSuite with BeforeAndAfter {

  test("GenerateMovesForStandardProblemInitialState") {
    val initialState = PegBoard.INITIAL_BOARD_POSITION

    val expectedMoves = Seq(
      new PegMove(5,3, 3, 3), new PegMove(1, 3, 3, 3), new PegMove(3, 5, 3, 3), new PegMove(3, 1, 3,3)
    )

    verifyGeneratedMoves(initialState, expectedMoves)
  }

  private def verifyGeneratedMoves(initialState: PegBoard, expectedMoves: Seq[PegMove]): Unit = {
    val generator = new MoveGenerator(initialState)
    val possibleMoves = generator.generateMoves
    assertEquals("Unexpected list of candidate moves", expectedMoves, possibleMoves)
  }
}
