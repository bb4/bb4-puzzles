// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.model

import com.barrybecker4.common.testsupport.strip
import org.scalatest.funsuite.AnyFunSuite
import PieceListSuite._
import org.scalatest.BeforeAndAfter
import scala.util.Random


object PieceListSuite {
  val ALL_OUTTY_CLUB_PIECE: Piece = Piece(Nub.OUTY_CLUB, Nub.OUTY_CLUB, Nub.OUTY_CLUB, Nub.OUTY_CLUB, 5)
  val RANDOM_PIECE: Piece = Piece(Nub.INNY_CLUB, Nub.OUTY_DIAMOND, Nub.INNY_HEART, Nub.OUTY_CLUB, 5)
  val INCOMPLETE_PUZZLE = new PieceList(PieceLists.INITIAL_PIECES_4.take(2).map(new OrientedPiece(_)).toList, 4)
}

/**
  * @author Barry Becker
  */
class PieceListSuite extends AnyFunSuite with BeforeAndAfter {

  test("ConstructionOfEmptyList") {
    val pieceList = new PieceList
    assertResult(0) {pieceList.size}
    assertResult("PieceList: (0 pieces)\n") {pieceList.toString}
  }

  test("Construction") {
    val pieceList = new PieceList(PieceLists.INITIAL_PIECES_4)
    assertResult(4) { pieceList.size }
    assertResult(4) { pieceList.size }
    assert(pieceList.contains(PieceLists.INITIAL_PIECES_4(1)))
    assertResult(1) {pieceList.getNumFits(0)}
    assertResult(0) { pieceList.getNumFits(1) }
    assertResult(1) { pieceList.getNumFits(2) }
    assertResult(0) { pieceList.getNumFits(3) }
  }

  test("getLast") {
    val pieceList = new PieceList(PieceLists.INITIAL_PIECES_4)
    assertResult(
      "Piece 4 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(S);LEFT:inny Suit(H);"
    ) {pieceList.getLast.toString }
  }

  test("Fits after rotate") {
    val pieceList = new PieceList(PieceLists.INITIAL_PIECES_4)
    assertResult(0) { pieceList.getNumFits(1) }
    // after rotating there should be a fit
    val newPieceList = pieceList.rotate(1, 1)
    assertResult(1) {newPieceList.getNumFits(1)}
  }

  test("Another fit check after rotation") {
    val pieceList = new PieceList(PieceLists.INITIAL_PIECES_4)
    assertResult(1) {pieceList.getNumFits(2)}
    assertResult(0) {pieceList.getNumFits(1)}
    // after rotating there should be a fit
    val newPieceList = pieceList.rotate(2, 1)
    assertResult(0) {newPieceList.getNumFits(2)}
    assertResult(0) {newPieceList.getNumFits(1)}
  }

  test("doSwap") {
    val pieceList = new PieceList(PieceLists.INITIAL_PIECES_4)
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
    val pieceList = new PieceList(PieceLists.INITIAL_PIECES_4)
    assertResult(strip("""PieceList: (4 pieces)
       |Piece 4 (orientation=BOTTOM): TOP:inny Suit(S);RIGHT:inny Suit(H);BOTTOM:outy Suit(C);LEFT:outy Suit(H);
       |Piece 1 (orientation=BOTTOM): TOP:inny Suit(H);RIGHT:inny Suit(D);BOTTOM:outy Suit(S);LEFT:outy Suit(D);
       |Piece 3 (orientation=TOP): TOP:outy Suit(H);RIGHT:outy Suit(S);BOTTOM:inny Suit(S);LEFT:inny Suit(C);
       |Piece 2 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(D);LEFT:inny Suit(C);
       |""")) {pieceList.shuffle(new Random(5)).toString}
  }

  test("add all outty at pos 1") {
    val pieceList = INCOMPLETE_PUZZLE
    val newPl = pieceList.add(1, OrientedPiece(ALL_OUTTY_CLUB_PIECE, Direction.LEFT))

    assertResult(strip("""PieceList: (3 pieces)
       |Piece 1 (orientation=TOP): TOP:outy Suit(S);RIGHT:outy Suit(D);BOTTOM:inny Suit(H);LEFT:inny Suit(D);
       |Piece 5 (orientation=LEFT): TOP:outy Suit(C);RIGHT:outy Suit(C);BOTTOM:outy Suit(C);LEFT:outy Suit(C);
       |Piece 2 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(D);LEFT:inny Suit(C);
       |""")) {newPl.toString}
  }

  test("remove a piece") {
    val pieceList = new PieceList(PieceLists.INITIAL_PIECES_4)

    assertResult(strip("""PieceList: (3 pieces)
        |Piece 1 (orientation=TOP): TOP:outy Suit(S);RIGHT:outy Suit(D);BOTTOM:inny Suit(H);LEFT:inny Suit(D);
        |Piece 2 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(D);LEFT:inny Suit(C);
        |Piece 4 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(S);LEFT:inny Suit(H);
        |""")) {pieceList.remove(2).toString}
  }

  test("add all outty at pos 1 after removing pos 1") {
    val pieceList = new PieceList(PieceLists.INITIAL_PIECES_4)

    var newPl = pieceList.remove(1)
    assertResult(3) {newPl.size}
    newPl = newPl.add(1, OrientedPiece(ALL_OUTTY_CLUB_PIECE, Direction.LEFT))

    assertResult(strip("""PieceList: (4 pieces)
       |Piece 1 (orientation=TOP): TOP:outy Suit(S);RIGHT:outy Suit(D);BOTTOM:inny Suit(H);LEFT:inny Suit(D);
       |Piece 5 (orientation=LEFT): TOP:outy Suit(C);RIGHT:outy Suit(C);BOTTOM:outy Suit(C);LEFT:outy Suit(C);
       |Piece 3 (orientation=TOP): TOP:outy Suit(H);RIGHT:outy Suit(S);BOTTOM:inny Suit(S);LEFT:inny Suit(C);
       |Piece 4 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(S);LEFT:inny Suit(H);
       |""")) {newPl.toString}
  }

  test("add random peice at pos 2") {
    val pieceList = INCOMPLETE_PUZZLE
    val newPl = pieceList.add(2, OrientedPiece(RANDOM_PIECE, Direction.TOP))

    assertResult(strip("""PieceList: (3 pieces)
       |Piece 1 (orientation=TOP): TOP:outy Suit(S);RIGHT:outy Suit(D);BOTTOM:inny Suit(H);LEFT:inny Suit(D);
       |Piece 2 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(D);LEFT:inny Suit(C);
       |Piece 5 (orientation=TOP): TOP:inny Suit(C);RIGHT:outy Suit(D);BOTTOM:inny Suit(H);LEFT:outy Suit(C);
       |""")) {newPl.toString}
  }

  test("remove piece after add") {
    val pieceList = INCOMPLETE_PUZZLE
    val newPl = pieceList.add(1, OrientedPiece(ALL_OUTTY_CLUB_PIECE, Direction.LEFT))
    assertResult(pieceList) {
      newPl.remove(ALL_OUTTY_CLUB_PIECE)
    }
  }

  test("remove piece by index after add") {
    val pieceList = INCOMPLETE_PUZZLE
    val newPl = pieceList.add(1, OrientedPiece(ALL_OUTTY_CLUB_PIECE, Direction.LEFT))
    assertResult(pieceList) {newPl.remove(1)}
  }

  test("remove last") {
    val pieceList = new PieceList(PieceLists.INITIAL_PIECES_4)
    val newPl = pieceList.removeLast()

    assertResult(strip("""PieceList: (3 pieces)
       |Piece 1 (orientation=TOP): TOP:outy Suit(S);RIGHT:outy Suit(D);BOTTOM:inny Suit(H);LEFT:inny Suit(D);
       |Piece 2 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(D);LEFT:inny Suit(C);
       |Piece 3 (orientation=TOP): TOP:outy Suit(H);RIGHT:outy Suit(S);BOTTOM:inny Suit(S);LEFT:inny Suit(C);
       |""")) {newPl.toString}
  }

  test("remove last twice") {
    val pieceList = new PieceList(PieceLists.INITIAL_PIECES_4)
    var newPl = pieceList.removeLast()
    newPl = newPl.removeLast()

    assertResult(strip("""PieceList: (2 pieces)
       |Piece 1 (orientation=TOP): TOP:outy Suit(S);RIGHT:outy Suit(D);BOTTOM:inny Suit(H);LEFT:inny Suit(D);
       |Piece 2 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(D);LEFT:inny Suit(C);
       |""")) {newPl.toString}
  }

  test("See if new piece fits in partially solved puzzle (when it does not)") {
    val pieceList = INCOMPLETE_PUZZLE
    assert(!pieceList.fits(new OrientedPiece(RANDOM_PIECE)))
  }

  // In order to fit into the partial puzzle, we just need an outty daimond on top.
  test("See if new piece fits in partially solved puzzle (when it does)") {
    val pieceList = INCOMPLETE_PUZZLE
    val newPiece = new OrientedPiece(Piece(Nub.OUTY_HEART, Nub.OUTY_DIAMOND, Nub.INNY_HEART, Nub.OUTY_CLUB, 2))
    assert(pieceList.fits(newPiece))
  }
}
