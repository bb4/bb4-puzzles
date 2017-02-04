// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge.model

import org.junit.Assert.assertEquals
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.collection.Seq

/**
  * @author Barry Becker
  */
class MoveGeneratorSuite extends FunSuite with BeforeAndAfter {

  test("GenerateMovesForStandardProblemInitialStateMoveRight") {
    val initialState = Bridge(List(1, 2, 5, 8), List[Int](), lightCrossed = false)
    val expectedMoves = Seq(
      BridgeMove(List(8), true), BridgeMove(List(5, 8), true), BridgeMove(List(5), true), BridgeMove(List(2, 8), true),
      BridgeMove(List(2, 5), true), BridgeMove(List(2), true), BridgeMove(List(1, 8), true),
      BridgeMove(List(1, 5), true), BridgeMove(List(1, 2), direction = true),
      BridgeMove(List(1), direction = true)
    )
    verifyGeneratedMoves(initialState, expectedMoves)
  }

  test("GenerateMovesForStandardProblemInitialStateMoveLeft") {
    val initialState = Bridge(List(2, 5, 8), List[Int](1), lightCrossed = true)
    val expectedMoves = Seq(BridgeMove(List(1), direction = false))
    verifyGeneratedMoves(initialState, expectedMoves)
  }

  test("GenerateMovesForStandardProblemMiddleStateMoveRight") {
    val initialState = Bridge(List(1, 8), List[Int](2, 5, 3), lightCrossed = false)
    val expectedMoves = Seq(
      BridgeMove(List(8), true), BridgeMove(List(1, 8), direction = true), BridgeMove(List(1), direction = true)
    )
    verifyGeneratedMoves(initialState, expectedMoves)
  }

  test("GenerateMovesForStandardProblemMiddleStateMoveLeft") {
    val initialState = Bridge(List(1, 8), List[Int](2, 5, 3), lightCrossed = true)
    val expectedMoves = Seq(
      BridgeMove(List(3), false), BridgeMove(List(5, 3), false),BridgeMove(List(5), false),
      BridgeMove(List(2, 3), false), BridgeMove(List(2, 5), direction = false), BridgeMove(List(2), direction = false)
    )
    verifyGeneratedMoves(initialState, expectedMoves)
  }

  private def verifyGeneratedMoves(initialState: Bridge, expectedMoves: Seq[BridgeMove]) {
    val generator = new MoveGenerator(initialState)
    val possibleMoves = generator.generateMoves
    assertEquals("Unexpected list of candidate moves", expectedMoves, possibleMoves)
  }
}
