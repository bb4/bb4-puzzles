// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.model

import com.barrybecker4.puzzle.testsupport.strip
import org.scalatest.{BeforeAndAfter, FunSuite}
import PieceListSuite.{ALL_OUTTY_CLUB_PIECE, RANDOM_PIECE}

import scala.util.Random


object PieceListSuite {
  val ALL_OUTTY_CLUB_PIECE = Piece(Nub.OUTY_CLUB, Nub.OUTY_CLUB, Nub.OUTY_CLUB, Nub.OUTY_CLUB, 5)
  val RANDOM_PIECE = Piece(Nub.INNY_CLUB, Nub.OUTY_DIAMOND, Nub.INNY_HEART, Nub.OUTY_CLUB, 5)
}

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

  test("shuffle") {
    pieceList = new PieceList(PieceLists.INITIAL_PIECES_4)

    val newpl = pieceList.shuffle(new Random(5))

    assertResult(strip("""PieceList: (4 pieces)
       |Piece 4 (orientation=BOTTOM): TOP:inny Suit(S);RIGHT:inny Suit(H);BOTTOM:outy Suit(C);LEFT:outy Suit(H);
       |Piece 1 (orientation=BOTTOM): TOP:inny Suit(H);RIGHT:inny Suit(D);BOTTOM:outy Suit(S);LEFT:outy Suit(D);
       |Piece 3 (orientation=TOP): TOP:outy Suit(H);RIGHT:outy Suit(S);BOTTOM:inny Suit(S);LEFT:inny Suit(C);
       |Piece 2 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(D);LEFT:inny Suit(C);
       |""")) {newpl.toString}
  }

  test("add all outty at pos 1") {
    pieceList = new PieceList(PieceLists.INITIAL_PIECES_4)
    val newPl = pieceList.add(1, OrientedPiece(ALL_OUTTY_CLUB_PIECE, Direction.LEFT))

    assertResult(strip("""PieceList: (5 pieces)
       |Piece 1 (orientation=TOP): TOP:outy Suit(S);RIGHT:outy Suit(D);BOTTOM:inny Suit(H);LEFT:inny Suit(D);
       |Piece 5 (orientation=LEFT): TOP:outy Suit(C);RIGHT:outy Suit(C);BOTTOM:outy Suit(C);LEFT:outy Suit(C);
       |Piece 2 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(D);LEFT:inny Suit(C);
       |Piece 3 (orientation=TOP): TOP:outy Suit(H);RIGHT:outy Suit(S);BOTTOM:inny Suit(S);LEFT:inny Suit(C);
       |Piece 4 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(S);LEFT:inny Suit(H);
       |""")) {newPl.toString}
  }

  test("add random peice at pos 2") {
    pieceList = new PieceList(PieceLists.INITIAL_PIECES_4)
    val newPl = pieceList.add(2, OrientedPiece(RANDOM_PIECE, Direction.TOP))

    assertResult(strip("""PieceList: (5 pieces)
       |Piece 1 (orientation=TOP): TOP:outy Suit(S);RIGHT:outy Suit(D);BOTTOM:inny Suit(H);LEFT:inny Suit(D);
       |Piece 2 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(D);LEFT:inny Suit(C);
       |Piece 5 (orientation=TOP): TOP:inny Suit(C);RIGHT:outy Suit(D);BOTTOM:inny Suit(H);LEFT:outy Suit(C);
       |Piece 3 (orientation=TOP): TOP:outy Suit(H);RIGHT:outy Suit(S);BOTTOM:inny Suit(S);LEFT:inny Suit(C);
       |Piece 4 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(S);LEFT:inny Suit(H);
       |""")) {newPl.toString}
  }

  test("remove piece after add") {
    pieceList = new PieceList(PieceLists.INITIAL_PIECES_4)
    val newPl = pieceList.add(1, OrientedPiece(ALL_OUTTY_CLUB_PIECE, Direction.LEFT))
    assertResult(pieceList) {
      newPl.remove(ALL_OUTTY_CLUB_PIECE)
    }
  }

  test("remove piece by index after add") {
    pieceList = new PieceList(PieceLists.INITIAL_PIECES_4)
    val newPl = pieceList.add(1, OrientedPiece(ALL_OUTTY_CLUB_PIECE, Direction.LEFT))
    assertResult(pieceList) {newPl.remove(1)}
  }

  test("remove last") {
    pieceList = new PieceList(PieceLists.INITIAL_PIECES_4)
    val newPl = pieceList.removeLast()

    assertResult(strip("""PieceList: (3 pieces)
                         |Piece 1 (orientation=TOP): TOP:outy Suit(S);RIGHT:outy Suit(D);BOTTOM:inny Suit(H);LEFT:inny Suit(D);
                         |Piece 2 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(D);LEFT:inny Suit(C);
                         |Piece 3 (orientation=TOP): TOP:outy Suit(H);RIGHT:outy Suit(S);BOTTOM:inny Suit(S);LEFT:inny Suit(C);
                         |""")) {newPl.toString}
  }

  test("remove last twice") {
    pieceList = new PieceList(PieceLists.INITIAL_PIECES_4)
    var newPl = pieceList.removeLast()
    newPl = newPl.removeLast()

    assertResult(strip("""PieceList: (2 pieces)
                         |Piece 1 (orientation=TOP): TOP:outy Suit(S);RIGHT:outy Suit(D);BOTTOM:inny Suit(H);LEFT:inny Suit(D);
                         |Piece 2 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(D);LEFT:inny Suit(C);
                         |""")) {newPl.toString}
  }
}
