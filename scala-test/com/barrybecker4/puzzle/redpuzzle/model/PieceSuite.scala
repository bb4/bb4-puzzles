package com.barrybecker4.puzzle.redpuzzle.model

import org.scalatest.funsuite.AnyFunSuite

class PieceSuite extends AnyFunSuite {

  test("reject invalid piece number") {
    intercept[IllegalArgumentException] {
      Piece(Nub.OUTY_SPADE, Nub.OUTY_SPADE, Nub.INNY_HEART, Nub.INNY_CLUB, 10)
    }
  }
}
