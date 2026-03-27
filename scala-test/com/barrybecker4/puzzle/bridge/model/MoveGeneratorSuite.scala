// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge.model

import org.junit.Assert.assertEquals
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfter

/**
  * @author Barry Becker
  */
class MoveGeneratorSuite extends AnyFunSuite with BeforeAndAfter {

  test("GenerateMovesForStandardProblemInitialStateMoveRight") {
    val initialState = Bridge(List(1, 2, 5, 8), List[Int](), lightCrossed = false)
    val expectedMoves = Seq(
      BridgeMove(List(1), true), BridgeMove(List(2), true), BridgeMove(List(1, 2), true), BridgeMove(List(5), true),
      BridgeMove(List(2, 5), true), BridgeMove(List(1, 5), true), BridgeMove(List(8), true),
      BridgeMove(List(5, 8), true), BridgeMove(List(2, 8), direction = true),
      BridgeMove(List(1, 8), direction = true)
    )
    verifyGeneratedMoves(initialState, expectedMoves)
  }

  test("GenerateMovesForStandardProblemInitialStateMoveLeft") {
    val initialState = Bridge(List(2, 5, 8), List[Int](1), lightCrossed = true)
    val expectedMoves = Seq(BridgeMove(List(1), direction = false))
    verifyGeneratedMoves(initialState, expectedMoves)
  }

  /** Five-person variant (speeds 1,2,3,5,8): torch on left, {1,8} still to cross; {2,3,5} on the far bank. */
  test("GenerateMovesForFivePersonMiddleStateMoveRight") {
    val initialState = Bridge(List(1, 8), List(2, 5, 3), lightCrossed = false)
    val expectedMoves = Seq(
      BridgeMove(List(1), true), BridgeMove(List(8), direction = true), BridgeMove(List(1, 8), direction = true)
    )
    verifyGeneratedMoves(initialState, expectedMoves)
  }

  /** Same positions as above, torch on far bank — return trips use people {2,3,5}. */
  test("GenerateMovesForFivePersonMiddleStateMoveLeft") {
    val initialState = Bridge(List(1, 8), List(2, 5, 3), lightCrossed = true)
    val expectedMoves = Seq(
      BridgeMove(List(2), false), BridgeMove(List(3), false), BridgeMove(List(2, 3), false),
      BridgeMove(List(5), direction = false), BridgeMove(List(5, 3), direction = false),
      BridgeMove(List(2, 5), direction = false)
    )
    verifyGeneratedMoves(initialState, expectedMoves)
  }

  /** Every generated move only references people currently on the torch side. */
  test("GenerateMovesOnlyUsePeopleOnTorchSide") {
    val initialState = Bridge(List(1, 2, 5, 8), Nil, lightCrossed = false)
    val generator = new MoveGenerator(initialState)
    for (m <- generator.generateMoves)
      assert(m.people.forall(initialState.uncrossed.contains),
        s"move $m must use only uncrossed people ${initialState.uncrossed}")
  }

  private def verifyGeneratedMoves(initialState: Bridge, expectedMoves: Seq[BridgeMove]): Unit = {
    val generator = new MoveGenerator(initialState)
    val possibleMoves = generator.generateMoves
    assertEquals("Unexpected list of candidate moves", expectedMoves, possibleMoves)
  }
}
