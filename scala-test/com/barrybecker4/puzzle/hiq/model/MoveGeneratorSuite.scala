// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.hiq.model

import org.scalatest.{BeforeAndAfter, FunSuite}
import org.junit.Test

import scala.collection.Seq
import org.junit.Assert.assertEquals

/**
  * @author Barry Becker
  */
class MoveGeneratorSuite extends FunSuite with BeforeAndAfter {


  @Test def testGenerateMovesForStandardProblemInitialState() {
    val initialState = PegBoard.INITIAL_BOARD_POSITION

    val expectedMoves = Seq(
      new PegMove(5,3, 3, 3), new PegMove(1, 3, 3, 3), new PegMove(3, 5, 3, 3), new PegMove(3, 1, 3,3)
    )

    verifyGeneratedMoves(initialState, expectedMoves)
  }

  private def verifyGeneratedMoves(initialState: PegBoard, expectedMoves: Seq[PegMove]) {
    val generator = new MoveGenerator(initialState)
    val possibleMoves = generator.generateMoves
    assertEquals("Unexpected list of candidate moves", expectedMoves, possibleMoves)
  }
}
