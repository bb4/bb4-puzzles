package com.barrybecker4.puzzle.hiq.model

import com.barrybecker4.common.geometry.ByteLocation
import org.scalatest.FunSuite

class PegBoardSuite extends FunSuite {

  test("default construction") {
    val board: PegBoard = new PegBoard()
    assertResult("000") { board.toString }
  }

  test("bits=1 construction") {
    val board: PegBoard = new PegBoard(PegBits(1))
    assertResult("100") { board.toString }
  }

  test("bits=42 construction") {
    val board: PegBoard = new PegBoard(PegBits(42))
    assertResult("10101000") { board.toString }
  }

  test("bits=42 construction. finalBit") {
    val board: PegBoard = new PegBoard(PegBits(42, true, false))
    assertResult("10101001") { board.toString }
  }

  test("bits=42 construction: nextToFinal") {
    val board: PegBoard = new PegBoard(PegBits(42, false, true))
    assertResult("10101010") { board.toString }
  }

  test("bits=72 construction") {
    val board: PegBoard = new PegBoard(PegBits(72))
    assertResult("100100000") { board.toString }
  }

  test("apply a move") {
    var board: PegBoard = new PegBoard(PegBits(1))

    board = board.doMove(PegMove(ByteLocation(1, 2), ByteLocation(3, 2)))
    assertResult("100000000000000100") { board.toString }
  }
}
