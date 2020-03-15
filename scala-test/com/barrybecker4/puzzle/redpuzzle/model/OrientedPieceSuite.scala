package com.barrybecker4.puzzle.redpuzzle.model

import org.scalatest.funsuite.AnyFunSuite
import OrientedPieceSuite.PIECE


object OrientedPieceSuite {
  val PIECE = Piece(Nub.OUTY_SPADE, Nub.OUTY_DIAMOND, Nub.INNY_HEART, Nub.INNY_DIAMOND, 1)
}

class OrientedPieceSuite extends AnyFunSuite {

  test("Construction") {
    val op = OrientedPiece(PIECE, Direction.TOP)
    assertResult(
      "Piece 1 (orientation=TOP): TOP:outy Suit(S);RIGHT:outy Suit(D);BOTTOM:inny Suit(H);LEFT:inny Suit(D);"
    ) {op.toString}
  }

  test("Rotate clokcwise by 90 degrees") {
    val op = OrientedPiece(PIECE, Direction.TOP)
    assertResult(
      "Piece 1 (orientation=RIGHT): TOP:outy Suit(D);RIGHT:inny Suit(H);BOTTOM:inny Suit(D);LEFT:outy Suit(S);"
    ) {op.rotate().toString}
  }

  test("Rotate clokcwise by 180 degrees") {
    val op = OrientedPiece(PIECE, Direction.TOP)
    assertResult(
      "Piece 1 (orientation=BOTTOM): TOP:inny Suit(H);RIGHT:inny Suit(D);BOTTOM:outy Suit(S);LEFT:outy Suit(D);"
    ) {op.rotate(2).toString}
  }
}
