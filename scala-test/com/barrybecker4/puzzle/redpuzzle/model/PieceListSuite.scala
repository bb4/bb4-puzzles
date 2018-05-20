// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.model

import com.barrybecker4.puzzle.testsupport.strip
import org.scalatest.{BeforeAndAfter, FunSuite}


/**
  * @author Barry Becker
  */
class PieceListSuite extends FunSuite with BeforeAndAfter {
  /** instance under test */
  private var pieceList: PieceList = _

  test("ConstructionOfEmptyList") {
    pieceList = new PieceList
    assertResult(0) {pieceList.size}
  }

  test("Construction") {
    pieceList = new PieceList(PieceLists.INITIAL_PIECES_4)
    assertResult(4) { pieceList.size }
    assertResult(true) { pieceList.contains(PieceLists.INITIAL_PIECES_4(1)) }
    assertResult(1) {pieceList.getNumFits(0)}
    assertResult(0) { pieceList.getNumFits(1) }
    assertResult(1) { pieceList.getNumFits(2) }
    assertResult(0) { pieceList.getNumFits(3) }
  }

  test("Fits after rotate") {
    pieceList = new PieceList(PieceLists.INITIAL_PIECES_4)
    assertResult(0) { pieceList.getNumFits(1) }
    // after rotating there should be a fit
    val newPieceList = pieceList.rotate(1, 1)
    assertResult(1) {newPieceList.getNumFits(1)}
  }

  test("Another fit check after rotatation") {
    pieceList = new PieceList(PieceLists.INITIAL_PIECES_4)
    assertResult(1) {pieceList.getNumFits(2)}
    // after rotating there should be a fit
    val newPieceList = pieceList.rotate(2, 1)
    assertResult(0) {newPieceList.getNumFits(2)}
    assertResult(0) {newPieceList.getNumFits(1)}
  }

  test("doSwap") {
    pieceList = new PieceList(PieceLists.INITIAL_PIECES_4)
    assertResult(strip("""PieceList: (4 pieces)
       |Piece 1 (orientation=TOP): TOP:outy Suit(S);RIGHT:outy Suit(D);BOTTOM:inny Suit(H);LEFT:inny Suit(D);
       |Piece 2 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(D);LEFT:inny Suit(C);
       |Piece 3 (orientation=TOP): TOP:outy Suit(H);RIGHT:outy Suit(S);BOTTOM:inny Suit(S);LEFT:inny Suit(C);
       |Piece 4 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(S);LEFT:inny Suit(H);
       |""")) {pieceList.toString}

    val newpl = pieceList.doSwap(1, 2)

    assertResult(strip("""PieceList: (4 pieces)
      |Piece 1 (orientation=TOP): TOP:outy Suit(S);RIGHT:outy Suit(D);BOTTOM:inny Suit(H);LEFT:inny Suit(D);
      |Piece 3 (orientation=TOP): TOP:outy Suit(H);RIGHT:outy Suit(S);BOTTOM:inny Suit(S);LEFT:inny Suit(C);
      |Piece 2 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(D);LEFT:inny Suit(C);
      |Piece 4 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(S);LEFT:inny Suit(H);
      |""")) {newpl.toString}
  }
}
