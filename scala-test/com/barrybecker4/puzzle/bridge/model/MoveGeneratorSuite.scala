// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge.model

import org.scalatest.{BeforeAndAfter, FunSuite}
import org.junit.Test
import scala.collection.Seq
import org.junit.Assert.assertEquals

/**
  * @author Barry Becker
  */
class MoveGeneratorSuite extends FunSuite with BeforeAndAfter {

  @Test def testGenerateMovesForStandardProblemInitialState() {
    val initialState = Bridge(List(1, 2, 5, 8), List[Int](), lightCrossed = false)
    val expectedMoves = Seq(
      BridgeMove(List(1), true), BridgeMove(List(1, 2), true),
      BridgeMove(List(1, 5), true), BridgeMove(List(1, 8), true),
      BridgeMove(List(2), true), BridgeMove(List(2, 5), true),
      BridgeMove(List(2, 8), true), BridgeMove(List(5), true),
      BridgeMove(List(5, 8), true), BridgeMove(List(8), true)
    )
    verifyGeneratedMoves(initialState, expectedMoves)
  }

  private def verifyGeneratedMoves(initialState: Bridge, expectedMoves: Seq[BridgeMove]) {
    val generator = new MoveGenerator(initialState)
    val possibleMoves = generator.generateMoves
    assertEquals("Unexpected list of candidate moves", expectedMoves, possibleMoves)
  }
}
