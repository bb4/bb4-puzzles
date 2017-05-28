// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.twopails.model

import org.junit.Test
import java.util

import com.barrybecker4.puzzle.twopails.model.PourOperation.Action._
import com.barrybecker4.puzzle.twopails.model.PourOperation.Container._
import org.junit.Assert.assertEquals
import org.scalatest.FunSuite

/**
  * @author Barry Becker
  */
class MoveGeneratorSuite extends FunSuite {

  test("GenerateMovesWhenBothEmpty") {
    val pails = new Pails(new PailParams(9, 4, 6))
    val expectedOps = List(new PourOperation(FILL, FIRST), new PourOperation(FILL, SECOND))
    verifyGeneratedMoves(pails, expectedOps)
  }

  test("GenerateMovesWhenBothFull") {
    var pails = new Pails(new PailParams(9, 4, 6))
    pails = pails.doMove(new PourOperation(FILL, FIRST), undo = false)
    pails = pails.doMove(new PourOperation(FILL, SECOND), undo = false)
    val expectedOps = List(new PourOperation(EMPTY, FIRST), new PourOperation(EMPTY, SECOND))
    verifyGeneratedMoves(pails, expectedOps)
  }

  test("GenerateMovesWhenAll0") {
    val pails = new Pails(new PailParams(0, 0, 0))
    val expectedOps = List()
    verifyGeneratedMoves(pails, expectedOps)
  }

  test("GenerateMovesWhenAll1") {
    val pails = new Pails(new PailParams(1, 1, 1))
    val expectedOps = List(new PourOperation(FILL, FIRST), new PourOperation(FILL, SECOND))
    verifyGeneratedMoves(pails, expectedOps)
  }

  test("GenerateMovesFirstFull") {
    var pails = new Pails(new PailParams(7, 4, 3))
    pails = pails.doMove(new PourOperation(FILL, FIRST), undo = false)
    val expectedOps = List(
      new PourOperation(FILL, SECOND),
      new PourOperation(EMPTY, FIRST),
      new PourOperation(TRANSFER, FIRST)
    )
    verifyGeneratedMoves(pails, expectedOps)
  }

  test("GenerateMovesSmallerFull") {
    var pails = new Pails(new PailParams(7, 4, 3))
    pails = pails.doMove(new PourOperation(FILL, SECOND), undo = false)
    val expectedOps = List(
      new PourOperation(FILL, FIRST),
      new PourOperation(EMPTY, SECOND),
      new PourOperation(TRANSFER, SECOND)
    )
    verifyGeneratedMoves(pails, expectedOps)
  }

  test("GenerateMovesOnePartiallyFull") {
    var pails = new Pails(new PailParams(11, 4, 8))
    pails = pails.doMove(new PourOperation(FILL, SECOND), undo = false)
    pails = pails.doMove(new PourOperation(TRANSFER, SECOND), undo = false)
    pails = pails.doMove(new PourOperation(FILL, SECOND), undo = false)
    //First now has 4 of 11, second has 4 (full)
    val expectedOps = List(
      new PourOperation(FILL, FIRST),
      new PourOperation(EMPTY, FIRST),
      new PourOperation(EMPTY, SECOND),
      new PourOperation(TRANSFER, SECOND)
    )
    verifyGeneratedMoves(pails, expectedOps)
  }

  test("GenerateMovesOnePartiallyFullButCannotTransferWithoutOverFlowing") {
    var pails = new Pails(new PailParams(7, 4, 6))
    pails = pails.doMove(new PourOperation(FILL, SECOND), undo = false)
    pails = pails.doMove(new PourOperation(TRANSFER, SECOND), undo = false)
    pails = pails.doMove(new PourOperation(FILL, SECOND), undo = false)
    //First now has 4 of 7, second has 4 (full)
    val expectedOps = List(
      new PourOperation(FILL, FIRST),
      new PourOperation(EMPTY, FIRST),
      new PourOperation(EMPTY, SECOND),
      new PourOperation(TRANSFER, SECOND) // its allowed to overflow
    )
    verifyGeneratedMoves(pails, expectedOps)
  }

  private def verifyGeneratedMoves(initialState: Pails, expectedOps: List[PourOperation]) = {
    val generator = new MoveGenerator(initialState)
    val possibleOps = generator.generateMoves
    assertResult(expectedOps) { possibleOps }
  }
}
