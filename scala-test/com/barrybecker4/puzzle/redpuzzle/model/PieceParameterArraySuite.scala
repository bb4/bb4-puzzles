package com.barrybecker4.puzzle.redpuzzle.model

import org.scalatest.{BeforeAndAfter, FunSuite}
import PieceParameterArraySuite._
import com.barrybecker4.common.testsupport.strip
import com.barrybecker4.puzzle.redpuzzle.solver.FitnessFinder

import scala.util.Random


object PieceParameterArraySuite {
  val PIECE_LIST = new PieceList(PieceLists.INITIAL_PIECES_4)
}

/**
  * @author Barry Becker
  */
class PieceParameterArraySuite extends FunSuite with BeforeAndAfter {

  private var fourPieces: PieceParameterArray = _
  private var ninePieces: PieceParameterArray = _
  private var rnd: Random = _

  /** instance under test */
  private var params: PieceParameterArray = _
  private val fitnessFinder = new FitnessFinder()

  before {
    rnd = new Random(1)
    fourPieces = new PieceParameterArray(PieceLists.getInitialPuzzlePieces(4, rnd), rnd)
    ninePieces = new PieceParameterArray(PieceLists.getInitialPuzzlePieces(9, rnd), rnd)
  }

  test("random neighbor when rad = 1.0 seed = 1") {
    params = new PieceParameterArray(PIECE_LIST, new Random(1))

    val params2 = params.getRandomNeighbor(1.0)

    assertResult(strip("""PieceList: (4 pieces)
       |Piece 4 (orientation=RIGHT): TOP:outy Suit(H);RIGHT:inny Suit(S);BOTTOM:inny Suit(H);LEFT:outy Suit(C);
       |Piece 2 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(D);LEFT:inny Suit(C);
       |Piece 3 (orientation=TOP): TOP:outy Suit(H);RIGHT:outy Suit(S);BOTTOM:inny Suit(S);LEFT:inny Suit(C);
       |Piece 1 (orientation=RIGHT): TOP:outy Suit(D);RIGHT:inny Suit(H);BOTTOM:inny Suit(D);LEFT:outy Suit(S);
       |""")) {params2.pieces.toString}
  }

  test("random neighbor when rad = 1.0 seed = 4") {
    params = new PieceParameterArray(PIECE_LIST, new Random(4))

    val params2 = params.getRandomNeighbor(1.0)

    assertResult(strip("""PieceList: (4 pieces)
       |Piece 1 (orientation=TOP): TOP:outy Suit(S);RIGHT:outy Suit(D);BOTTOM:inny Suit(H);LEFT:inny Suit(D);
       |Piece 2 (orientation=LEFT): TOP:inny Suit(C);RIGHT:outy Suit(C);BOTTOM:outy Suit(H);LEFT:inny Suit(D);
       |Piece 4 (orientation=RIGHT): TOP:outy Suit(H);RIGHT:inny Suit(S);BOTTOM:inny Suit(H);LEFT:outy Suit(C);
       |Piece 3 (orientation=BOTTOM): TOP:inny Suit(S);RIGHT:inny Suit(C);BOTTOM:outy Suit(H);LEFT:outy Suit(S);
       |""")) {params2.pieces.toString}
  }

  test("random neighbor when rad = 1.0 seed = 3") {
    params = new PieceParameterArray(PIECE_LIST, new Random(3))

    val params2 = params.getRandomNeighbor(1.0)

    assertResult(strip("""PieceList: (4 pieces)
       |Piece 1 (orientation=TOP): TOP:outy Suit(S);RIGHT:outy Suit(D);BOTTOM:inny Suit(H);LEFT:inny Suit(D);
       |Piece 3 (orientation=RIGHT): TOP:outy Suit(S);RIGHT:inny Suit(S);BOTTOM:inny Suit(C);LEFT:outy Suit(H);
       |Piece 2 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(D);LEFT:inny Suit(C);
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
       |Piece 3 (orientation=RIGHT): TOP:outy Suit(S);RIGHT:inny Suit(S);BOTTOM:inny Suit(C);LEFT:outy Suit(H);
       |Piece 4 (orientation=RIGHT): TOP:outy Suit(H);RIGHT:inny Suit(S);BOTTOM:inny Suit(H);LEFT:outy Suit(C);
       |Piece 2 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(D);LEFT:inny Suit(C);
       |Piece 1 (orientation=BOTTOM): TOP:inny Suit(H);RIGHT:inny Suit(D);BOTTOM:outy Suit(S);LEFT:outy Suit(D);
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


  test("find random neighbor when rad = 0.2") {
    params = fourPieces
    val nbr = params.getRandomNeighbor(0.2)
    assertResult(strip("""PieceList: (4 pieces)
                         |Piece 3 (orientation=RIGHT): TOP:outy Suit(S);RIGHT:inny Suit(S);BOTTOM:inny Suit(C);LEFT:outy Suit(H);
                         |Piece 2 (orientation=LEFT): TOP:inny Suit(C);RIGHT:outy Suit(C);BOTTOM:outy Suit(H);LEFT:inny Suit(D);
                         |Piece 4 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(S);LEFT:inny Suit(H);
                         |Piece 1 (orientation=BOTTOM): TOP:inny Suit(H);RIGHT:inny Suit(D);BOTTOM:outy Suit(S);LEFT:outy Suit(D);
                         |""")) { nbr.toString }
    assertResult(21) {fitnessFinder.calculateFitness(nbr.pieces)}
  }

  test("find random neighbor when rad = 1.1") {
    params = fourPieces
    val nbr = params.getRandomNeighbor(1.1)
    assertResult(strip("""PieceList: (4 pieces)
     |Piece 4 (orientation=LEFT): TOP:inny Suit(H);RIGHT:outy Suit(C);BOTTOM:outy Suit(H);LEFT:inny Suit(S);
     |Piece 2 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(D);LEFT:inny Suit(C);
     |Piece 1 (orientation=BOTTOM): TOP:inny Suit(H);RIGHT:inny Suit(D);BOTTOM:outy Suit(S);LEFT:outy Suit(D);
     |Piece 3 (orientation=RIGHT): TOP:outy Suit(S);RIGHT:inny Suit(S);BOTTOM:inny Suit(C);LEFT:outy Suit(H);
     |""")) { nbr.toString }
    assertResult(21) {fitnessFinder.calculateFitness(nbr.pieces)} // was 19?
  }

  test("find random neighbor when rad = 0.2 for 9 piece puzzle") {
    params = ninePieces
    val nbr = params.getRandomNeighbor(0.2)
    assertResult(strip("""PieceList: (9 pieces)
                         |Piece 9 (orientation=RIGHT): TOP:outy Suit(S);RIGHT:inny Suit(H);BOTTOM:inny Suit(C);LEFT:outy Suit(S);
                         |Piece 7 (orientation=TOP): TOP:outy Suit(H);RIGHT:outy Suit(D);BOTTOM:inny Suit(C);LEFT:inny Suit(C);
                         |Piece 2 (orientation=LEFT): TOP:inny Suit(C);RIGHT:outy Suit(C);BOTTOM:outy Suit(H);LEFT:inny Suit(D);
                         |Piece 4 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(S);LEFT:inny Suit(H);
                         |Piece 5 (orientation=BOTTOM): TOP:outy Suit(S);RIGHT:outy Suit(D);BOTTOM:inny Suit(S);LEFT:inny Suit(H);
                         |Piece 6 (orientation=LEFT): TOP:inny Suit(H);RIGHT:outy Suit(H);BOTTOM:outy Suit(D);LEFT:inny Suit(D);
                         |Piece 1 (orientation=TOP): TOP:outy Suit(S);RIGHT:outy Suit(D);BOTTOM:inny Suit(H);LEFT:inny Suit(D);
                         |Piece 8 (orientation=TOP): TOP:outy Suit(D);RIGHT:outy Suit(C);BOTTOM:inny Suit(C);LEFT:inny Suit(D);
                         |Piece 3 (orientation=TOP): TOP:outy Suit(H);RIGHT:outy Suit(S);BOTTOM:inny Suit(S);LEFT:inny Suit(C);
                         |""")) { nbr.toString }
    assertResult(8.899999999999999) {fitnessFinder.calculateFitness(nbr.pieces)}
  }

  test("Find1GlobalSamples") {
    params = ninePieces
    assertResult(strip("""PieceList: (9 pieces)
                         |Piece 6 (orientation=RIGHT): TOP:outy Suit(D);RIGHT:inny Suit(D);BOTTOM:inny Suit(H);LEFT:outy Suit(H);
                         |Piece 8 (orientation=RIGHT): TOP:outy Suit(C);RIGHT:inny Suit(C);BOTTOM:inny Suit(D);LEFT:outy Suit(D);
                         |Piece 5 (orientation=BOTTOM): TOP:outy Suit(S);RIGHT:outy Suit(D);BOTTOM:inny Suit(S);LEFT:inny Suit(H);
                         |Piece 3 (orientation=TOP): TOP:outy Suit(H);RIGHT:outy Suit(S);BOTTOM:inny Suit(S);LEFT:inny Suit(C);
                         |Piece 9 (orientation=LEFT): TOP:inny Suit(C);RIGHT:outy Suit(S);BOTTOM:outy Suit(S);LEFT:inny Suit(H);
                         |Piece 1 (orientation=RIGHT): TOP:outy Suit(D);RIGHT:inny Suit(H);BOTTOM:inny Suit(D);LEFT:outy Suit(S);
                         |Piece 4 (orientation=LEFT): TOP:inny Suit(H);RIGHT:outy Suit(C);BOTTOM:outy Suit(H);LEFT:inny Suit(S);
                         |Piece 7 (orientation=RIGHT): TOP:outy Suit(D);RIGHT:inny Suit(C);BOTTOM:inny Suit(C);LEFT:outy Suit(H);
                         |Piece 2 (orientation=LEFT): TOP:inny Suit(C);RIGHT:outy Suit(C);BOTTOM:outy Suit(H);LEFT:inny Suit(D);
                         |""")) { params.findGlobalSamples(1).mkString(", ") }
  }

  test("Find3GlobalSamples") {
    params = ninePieces
    assertResult(strip("""PieceList: (9 pieces)
                         |Piece 6 (orientation=RIGHT): TOP:outy Suit(D);RIGHT:inny Suit(D);BOTTOM:inny Suit(H);LEFT:outy Suit(H);
                         |Piece 8 (orientation=RIGHT): TOP:outy Suit(C);RIGHT:inny Suit(C);BOTTOM:inny Suit(D);LEFT:outy Suit(D);
                         |Piece 5 (orientation=BOTTOM): TOP:outy Suit(S);RIGHT:outy Suit(D);BOTTOM:inny Suit(S);LEFT:inny Suit(H);
                         |Piece 3 (orientation=TOP): TOP:outy Suit(H);RIGHT:outy Suit(S);BOTTOM:inny Suit(S);LEFT:inny Suit(C);
                         |Piece 9 (orientation=LEFT): TOP:inny Suit(C);RIGHT:outy Suit(S);BOTTOM:outy Suit(S);LEFT:inny Suit(H);
                         |Piece 1 (orientation=RIGHT): TOP:outy Suit(D);RIGHT:inny Suit(H);BOTTOM:inny Suit(D);LEFT:outy Suit(S);
                         |Piece 4 (orientation=LEFT): TOP:inny Suit(H);RIGHT:outy Suit(C);BOTTOM:outy Suit(H);LEFT:inny Suit(S);
                         |Piece 7 (orientation=RIGHT): TOP:outy Suit(D);RIGHT:inny Suit(C);BOTTOM:inny Suit(C);LEFT:outy Suit(H);
                         |Piece 2 (orientation=LEFT): TOP:inny Suit(C);RIGHT:outy Suit(C);BOTTOM:outy Suit(H);LEFT:inny Suit(D);
                         |
                         |PieceList: (9 pieces)
                         |Piece 9 (orientation=LEFT): TOP:inny Suit(C);RIGHT:outy Suit(S);BOTTOM:outy Suit(S);LEFT:inny Suit(H);
                         |Piece 2 (orientation=BOTTOM): TOP:inny Suit(D);RIGHT:inny Suit(C);BOTTOM:outy Suit(C);LEFT:outy Suit(H);
                         |Piece 6 (orientation=RIGHT): TOP:outy Suit(D);RIGHT:inny Suit(D);BOTTOM:inny Suit(H);LEFT:outy Suit(H);
                         |Piece 8 (orientation=RIGHT): TOP:outy Suit(C);RIGHT:inny Suit(C);BOTTOM:inny Suit(D);LEFT:outy Suit(D);
                         |Piece 3 (orientation=TOP): TOP:outy Suit(H);RIGHT:outy Suit(S);BOTTOM:inny Suit(S);LEFT:inny Suit(C);
                         |Piece 1 (orientation=BOTTOM): TOP:inny Suit(H);RIGHT:inny Suit(D);BOTTOM:outy Suit(S);LEFT:outy Suit(D);
                         |Piece 4 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(S);LEFT:inny Suit(H);
                         |Piece 5 (orientation=TOP): TOP:inny Suit(S);RIGHT:inny Suit(H);BOTTOM:outy Suit(S);LEFT:outy Suit(D);
                         |Piece 7 (orientation=LEFT): TOP:inny Suit(C);RIGHT:outy Suit(H);BOTTOM:outy Suit(D);LEFT:inny Suit(C);
                         |
                         |PieceList: (9 pieces)
                         |Piece 5 (orientation=BOTTOM): TOP:outy Suit(S);RIGHT:outy Suit(D);BOTTOM:inny Suit(S);LEFT:inny Suit(H);
                         |Piece 1 (orientation=BOTTOM): TOP:inny Suit(H);RIGHT:inny Suit(D);BOTTOM:outy Suit(S);LEFT:outy Suit(D);
                         |Piece 2 (orientation=LEFT): TOP:inny Suit(C);RIGHT:outy Suit(C);BOTTOM:outy Suit(H);LEFT:inny Suit(D);
                         |Piece 4 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(S);LEFT:inny Suit(H);
                         |Piece 6 (orientation=LEFT): TOP:inny Suit(H);RIGHT:outy Suit(H);BOTTOM:outy Suit(D);LEFT:inny Suit(D);
                         |Piece 8 (orientation=RIGHT): TOP:outy Suit(C);RIGHT:inny Suit(C);BOTTOM:inny Suit(D);LEFT:outy Suit(D);
                         |Piece 9 (orientation=BOTTOM): TOP:inny Suit(H);RIGHT:inny Suit(C);BOTTOM:outy Suit(S);LEFT:outy Suit(S);
                         |Piece 3 (orientation=BOTTOM): TOP:inny Suit(S);RIGHT:inny Suit(C);BOTTOM:outy Suit(H);LEFT:outy Suit(S);
                         |Piece 7 (orientation=RIGHT): TOP:outy Suit(D);RIGHT:inny Suit(C);BOTTOM:inny Suit(C);LEFT:outy Suit(H);
                         |""")) { params.findGlobalSamples(3).mkString("\n") }
  }
}