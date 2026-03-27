package com.barrybecker4.puzzle.hiq.model

import com.barrybecker4.common.geometry.ByteLocation
import org.scalatest.funsuite.AnyFunSuite

class PegBoardSuite extends AnyFunSuite {

  test("equals is false for non-PegBoard") {
    assert(!PegBoard.INITIAL_BOARD_POSITION.equals("not-a-board"))
  }

  test("INITIAL_BOARD_POSITION has 32 pegs and empty center") {
    val b = PegBoard.INITIAL_BOARD_POSITION
    assert(b.getNumPegsLeft == 32)
    assert(b.isEmpty(3, 3))
    assert(!b.isSolved)
  }

  test("getFirstMove jumps into center from row below center") {
    val m = PegBoard.INITIAL_BOARD_POSITION.getFirstMove
    assert(m.getFromRow == 3 && m.getFromCol == 1 && m.getToRow == 3 && m.getToCol == 3)
  }

  test("board with only center peg is solved") {
    val idx = PegBits().getIndexForPosition(3, 3)
    val solved = PegBoard(PegBits().set(idx, value = true))
    assert(solved.getNumPegsLeft == 1)
    assert(solved.getPosition(3, 3))
    assert(solved.isSolved)
  }

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
