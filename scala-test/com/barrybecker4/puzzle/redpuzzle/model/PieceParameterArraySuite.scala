package com.barrybecker4.puzzle.redpuzzle.model

import com.barrybecker4.puzzle.testsupport.strip
import org.scalatest.FunSuite
import PieceParameterArraySuite._
import scala.util.Random


object PieceParameterArraySuite {
  val PIECE_LIST = new PieceList(PieceLists.INITIAL_PIECES_4)
}

/**
  * @author Barry Becker
  */
class PieceParameterArraySuite extends FunSuite {

  /** instance under test */
  private var params: PieceParameterArray = _

  test("random neighbor when rad = 1.0 seed = 1") {
    params = new PieceParameterArray(PIECE_LIST, new Random(1))

    val params2 = params.getRandomNeighbor(1.0)

    assertResult(strip("""PieceList: (4 pieces)
       |Piece 1 (orientation=TOP): TOP:outy Suit(S);RIGHT:outy Suit(D);BOTTOM:inny Suit(H);LEFT:inny Suit(D);
       |Piece 2 (orientation=RIGHT): TOP:outy Suit(H);RIGHT:inny Suit(D);BOTTOM:inny Suit(C);LEFT:outy Suit(C);
       |Piece 3 (orientation=TOP): TOP:outy Suit(H);RIGHT:outy Suit(S);BOTTOM:inny Suit(S);LEFT:inny Suit(C);
       |Piece 4 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(S);LEFT:inny Suit(H);
       |""")) {params2.pieces.toString}
  }

  test("random neighbor when rad = 1.0 seed = 4") {
    params = new PieceParameterArray(PIECE_LIST, new Random(4))

    val params2 = params.getRandomNeighbor(1.0)

    assertResult(strip("""PieceList: (4 pieces)
       |Piece 4 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(S);LEFT:inny Suit(H);
       |Piece 2 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(D);LEFT:inny Suit(C);
       |Piece 1 (orientation=TOP): TOP:outy Suit(S);RIGHT:outy Suit(D);BOTTOM:inny Suit(H);LEFT:inny Suit(D);
       |Piece 3 (orientation=TOP): TOP:outy Suit(H);RIGHT:outy Suit(S);BOTTOM:inny Suit(S);LEFT:inny Suit(C);
       |""")) {params2.pieces.toString}
  }

  test("random neighbor when rad = 1.0 seed = 3") {
    params = new PieceParameterArray(PIECE_LIST, new Random(3))

    val params2 = params.getRandomNeighbor(1.0)

    assertResult(strip("""PieceList: (4 pieces)
       |Piece 1 (orientation=TOP): TOP:outy Suit(S);RIGHT:outy Suit(D);BOTTOM:inny Suit(H);LEFT:inny Suit(D);
       |Piece 2 (orientation=RIGHT): TOP:outy Suit(H);RIGHT:inny Suit(D);BOTTOM:inny Suit(C);LEFT:outy Suit(C);
       |Piece 3 (orientation=TOP): TOP:outy Suit(H);RIGHT:outy Suit(S);BOTTOM:inny Suit(S);LEFT:inny Suit(C);
       |Piece 4 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(S);LEFT:inny Suit(H);
       |""")) {params2.pieces.toString}
  }

  test("random neighbor when rad = 0.5") {
    params = new PieceParameterArray(PIECE_LIST, new Random(1))
    val params2 = params.getRandomNeighbor(0.5)

    assertResult(strip("""PieceList: (4 pieces)
       |Piece 1 (orientation=TOP): TOP:outy Suit(S);RIGHT:outy Suit(D);BOTTOM:inny Suit(H);LEFT:inny Suit(D);
       |Piece 4 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(S);LEFT:inny Suit(H);
       |Piece 3 (orientation=TOP): TOP:outy Suit(H);RIGHT:outy Suit(S);BOTTOM:inny Suit(S);LEFT:inny Suit(C);
       |Piece 2 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(D);LEFT:inny Suit(C);
       |""")) {params2.pieces.toString}
  }

  test("random sample") {
    params = new PieceParameterArray(PIECE_LIST, new Random(1))
    val params2 = params.getRandomSample

    assertResult(strip("""PieceList: (4 pieces)
       |Piece 4 (orientation=BOTTOM): TOP:inny Suit(S);RIGHT:inny Suit(H);BOTTOM:outy Suit(C);LEFT:outy Suit(H);
       |Piece 1 (orientation=BOTTOM): TOP:inny Suit(H);RIGHT:inny Suit(D);BOTTOM:outy Suit(S);LEFT:outy Suit(D);
       |Piece 3 (orientation=TOP): TOP:outy Suit(H);RIGHT:outy Suit(S);BOTTOM:inny Suit(S);LEFT:inny Suit(C);
       |Piece 2 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(D);LEFT:inny Suit(C);
       |""")) {params2.pieces.toString}
  }

  test("setPermutation") {
    params = new PieceParameterArray(PIECE_LIST, new Random(1))
    val permutedParams = params.setPermutation(List(3, 2, 1, 0))

    assertResult(strip("""PieceList: (4 pieces)
       |Piece 4 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(S);LEFT:inny Suit(H);
       |Piece 3 (orientation=TOP): TOP:outy Suit(H);RIGHT:outy Suit(S);BOTTOM:inny Suit(S);LEFT:inny Suit(C);
       |Piece 2 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(D);LEFT:inny Suit(C);
       |Piece 1 (orientation=TOP): TOP:outy Suit(S);RIGHT:outy Suit(D);BOTTOM:inny Suit(H);LEFT:inny Suit(D);
       |""")) {permutedParams.pieces.toString}
  }
}