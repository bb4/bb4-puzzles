package com.barrybecker4.puzzle.hiq.model

import org.scalatest.funsuite.AnyFunSuite

import scala.collection.mutable

class PegBoardSymmetrySuite extends AnyFunSuite {

  test("containedIn detects equivalent board under a non-identity symmetry") {
    val initial = PegBoard.INITIAL_BOARD_POSITION
    val board = initial.doMove(initial.getFirstMove)
    val rotated = board.symmetry(2)
    assert(board != rotated)
    val seen = mutable.Set[PegBoard](rotated)
    assert(board.containedIn(seen))
  }

  test("symmetry index 0 is identity") {
    val board = PegBoard.INITIAL_BOARD_POSITION
    assert(board.symmetry(0) eq board)
  }
}
