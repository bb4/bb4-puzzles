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
    assertResult("001") { board.toString }
  }

  test("bits=42 construction") {
    val board: PegBoard = new PegBoard(PegBits(42))
    assertResult("00101010") { board.toString }
  }

  test("bits=42 construction. finalBit") {
    val board: PegBoard = new PegBoard(PegBits(42, true, false))
    assertResult("10101010") { board.toString }
  }

  test("bits=42 construction: nextToFinal") {
    val board: PegBoard = new PegBoard(PegBits(42, false, true))
    assertResult("01101010") { board.toString }
  }

  test("bits=72 construction") {
    val board: PegBoard = new PegBoard(PegBits(72))
    assertResult("001001000") { board.toString }
  }

  test("apply a move") {
    var board: PegBoard = new PegBoard(PegBits(1))

    board = board.doMove(PegMove(ByteLocation(1, 2), ByteLocation(3, 2)))
    assertResult("001000000000000001") { board.toString }
  }
}
