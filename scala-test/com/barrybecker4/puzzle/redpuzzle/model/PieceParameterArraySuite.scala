package com.barrybecker4.puzzle.redpuzzle.model

import org.scalatest.{BeforeAndAfter, FunSuite}
import com.barrybecker4.common.testsupport.strip

import scala.util.Random
import PieceParameterArraySuite._
import com.barrybecker4.puzzle.redpuzzle.solver.FitnessFinder

object PieceParameterArraySuite {
  val RANDOM = new Random(1)
  val NINE_PIECES = new PieceParameterArray(PieceLists.getInitialPuzzlePieces(9, RANDOM))
  val FOUR_PIECES = new PieceParameterArray(PieceLists.getInitialPuzzlePieces(4, RANDOM))
}

class PieceParameterArraySuite extends FunSuite with BeforeAndAfter {

  private var params: PieceParameterArray = _
  private var rnd: Random = _
  before {
    rnd = new Random(1)
  }

  test("serialization of 9 piece param array") {
    params = NINE_PIECES
    assertResult(strip("""PieceList: (9 pieces)
       |Piece 8 (orientation=BOTTOM): TOP:inny Suit(C);RIGHT:inny Suit(D);BOTTOM:outy Suit(D);LEFT:outy Suit(C);
       |Piece 3 (orientation=RIGHT): TOP:outy Suit(S);RIGHT:inny Suit(S);BOTTOM:inny Suit(C);LEFT:outy Suit(H);
       |Piece 7 (orientation=RIGHT): TOP:outy Suit(D);RIGHT:inny Suit(C);BOTTOM:inny Suit(C);LEFT:outy Suit(H);
       |Piece 5 (orientation=TOP): TOP:inny Suit(S);RIGHT:inny Suit(H);BOTTOM:outy Suit(S);LEFT:outy Suit(D);
       |Piece 4 (orientation=RIGHT): TOP:outy Suit(H);RIGHT:inny Suit(S);BOTTOM:inny Suit(H);LEFT:outy Suit(C);
       |Piece 9 (orientation=LEFT): TOP:inny Suit(C);RIGHT:outy Suit(S);BOTTOM:outy Suit(S);LEFT:inny Suit(H);
       |Piece 6 (orientation=TOP): TOP:outy Suit(H);RIGHT:outy Suit(D);BOTTOM:inny Suit(D);LEFT:inny Suit(H);
       |Piece 1 (orientation=BOTTOM): TOP:inny Suit(H);RIGHT:inny Suit(D);BOTTOM:outy Suit(S);LEFT:outy Suit(D);
       |Piece 2 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(D);LEFT:inny Suit(C);
       |""")) { params.toString }
    assertResult(9) { params.size }
  }

  test("serialization of 4 piece param array") {
    params = FOUR_PIECES
    assertResult(strip("""PieceList: (4 pieces)
       |Piece 3 (orientation=RIGHT): TOP:outy Suit(S);RIGHT:inny Suit(S);BOTTOM:inny Suit(C);LEFT:outy Suit(H);
       |Piece 1 (orientation=LEFT): TOP:inny Suit(D);RIGHT:outy Suit(S);BOTTOM:outy Suit(D);LEFT:inny Suit(H);
       |Piece 2 (orientation=LEFT): TOP:inny Suit(C);RIGHT:outy Suit(C);BOTTOM:outy Suit(H);LEFT:inny Suit(D);
       |Piece 4 (orientation=RIGHT): TOP:outy Suit(H);RIGHT:inny Suit(S);BOTTOM:inny Suit(H);LEFT:outy Suit(C);
       |""")) { params.toString }
  }

  test("Fitness of 4 random pieces") {
    val fitnessFinder = new FitnessFinder()
    FOUR_PIECES.setFitness(fitnessFinder.calculateFitness(FOUR_PIECES.pieces))
    assertResult(25) {FOUR_PIECES.getFitness}
  }

  test("Fitness of 9 random pieces") {
    val fitnessFinder = new FitnessFinder()
    NINE_PIECES.setFitness(fitnessFinder.calculateFitness(NINE_PIECES.pieces))
    assertResult(25) {NINE_PIECES.getFitness}
  }

  test("Equality of 2 equal PieceParameterArrays") {
    assert(NINE_PIECES == NINE_PIECES.copy)
  }

  test("Equality of 2 uneqaul PieceParameterArrays") {
    assert(NINE_PIECES != new PieceParameterArray(NINE_PIECES.pieces.shuffle()))
  }

  test("Equality of 2 very uneqaul PieceParameterArrays") {
    assert(NINE_PIECES != FOUR_PIECES)
  }

  test("GetSamplePopulation for 4 pieces"){
    params = FOUR_PIECES
    assertResult(400) { params.getSamplePopulationSize }
  }

  test("GetSamplePopulation for 9 peices") {
    params = NINE_PIECES
    assertResult(400) { params.getSamplePopulationSize }
  }

  test("find random neighbor when rad = 0.2") {
    params = FOUR_PIECES
    val nbr = params.getRandomNeighbor(0.2)
    assertResult(strip("""PieceList: (4 pieces)
             |Piece 3 (orientation=TOP): TOP:outy Suit(H);RIGHT:outy Suit(S);BOTTOM:inny Suit(S);LEFT:inny Suit(C);
             |Piece 4 (orientation=RIGHT): TOP:outy Suit(H);RIGHT:inny Suit(S);BOTTOM:inny Suit(H);LEFT:outy Suit(C);
             |Piece 2 (orientation=BOTTOM): TOP:inny Suit(D);RIGHT:inny Suit(C);BOTTOM:outy Suit(C);LEFT:outy Suit(H);
             |Piece 1 (orientation=TOP): TOP:outy Suit(S);RIGHT:outy Suit(D);BOTTOM:inny Suit(H);LEFT:inny Suit(D);
             |""")) { nbr.toString }
  }

  test("find random neighbor when rad = 1.1") {
    params = FOUR_PIECES
    val nbr = params.getRandomNeighbor(1.1)
    assertResult(strip("""PieceList: (4 pieces)
             |Piece 4 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(S);LEFT:inny Suit(H);
             |Piece 3 (orientation=BOTTOM): TOP:inny Suit(S);RIGHT:inny Suit(C);BOTTOM:outy Suit(H);LEFT:outy Suit(S);
             |Piece 2 (orientation=BOTTOM): TOP:inny Suit(D);RIGHT:inny Suit(C);BOTTOM:outy Suit(C);LEFT:outy Suit(H);
             |Piece 1 (orientation=TOP): TOP:outy Suit(S);RIGHT:outy Suit(D);BOTTOM:inny Suit(H);LEFT:inny Suit(D);
             |""")) { nbr.toString }
  }

  test("Find1GlobalSamples") {
    params = NINE_PIECES
    assertResult(strip("""PieceList: (9 pieces)
             |Piece 4 (orientation=RIGHT): TOP:outy Suit(H);RIGHT:inny Suit(S);BOTTOM:inny Suit(H);LEFT:outy Suit(C);
             |Piece 3 (orientation=TOP): TOP:outy Suit(H);RIGHT:outy Suit(S);BOTTOM:inny Suit(S);LEFT:inny Suit(C);
             |Piece 5 (orientation=BOTTOM): TOP:outy Suit(S);RIGHT:outy Suit(D);BOTTOM:inny Suit(S);LEFT:inny Suit(H);
             |Piece 2 (orientation=BOTTOM): TOP:inny Suit(D);RIGHT:inny Suit(C);BOTTOM:outy Suit(C);LEFT:outy Suit(H);
             |Piece 9 (orientation=LEFT): TOP:inny Suit(C);RIGHT:outy Suit(S);BOTTOM:outy Suit(S);LEFT:inny Suit(H);
             |Piece 7 (orientation=LEFT): TOP:inny Suit(C);RIGHT:outy Suit(H);BOTTOM:outy Suit(D);LEFT:inny Suit(C);
             |Piece 6 (orientation=LEFT): TOP:inny Suit(H);RIGHT:outy Suit(H);BOTTOM:outy Suit(D);LEFT:inny Suit(D);
             |Piece 1 (orientation=LEFT): TOP:inny Suit(D);RIGHT:outy Suit(S);BOTTOM:outy Suit(D);LEFT:inny Suit(H);
             |Piece 8 (orientation=TOP): TOP:outy Suit(D);RIGHT:outy Suit(C);BOTTOM:inny Suit(C);LEFT:inny Suit(D);
     |""")) { params.findGlobalSamples(1).mkString(", ") }
  }

  test("Find3GlobalSamples") {
    params = NINE_PIECES
    assertResult(strip("""PieceList: (9 pieces)
                         |Piece 5 (orientation=BOTTOM): TOP:outy Suit(S);RIGHT:outy Suit(D);BOTTOM:inny Suit(S);LEFT:inny Suit(H);
                         |Piece 8 (orientation=TOP): TOP:outy Suit(D);RIGHT:outy Suit(C);BOTTOM:inny Suit(C);LEFT:inny Suit(D);
                         |Piece 2 (orientation=BOTTOM): TOP:inny Suit(D);RIGHT:inny Suit(C);BOTTOM:outy Suit(C);LEFT:outy Suit(H);
                         |Piece 9 (orientation=TOP): TOP:outy Suit(S);RIGHT:outy Suit(S);BOTTOM:inny Suit(H);LEFT:inny Suit(C);
                         |Piece 1 (orientation=BOTTOM): TOP:inny Suit(H);RIGHT:inny Suit(D);BOTTOM:outy Suit(S);LEFT:outy Suit(D);
                         |Piece 7 (orientation=LEFT): TOP:inny Suit(C);RIGHT:outy Suit(H);BOTTOM:outy Suit(D);LEFT:inny Suit(C);
                         |Piece 6 (orientation=RIGHT): TOP:outy Suit(D);RIGHT:inny Suit(D);BOTTOM:inny Suit(H);LEFT:outy Suit(H);
                         |Piece 4 (orientation=RIGHT): TOP:outy Suit(H);RIGHT:inny Suit(S);BOTTOM:inny Suit(H);LEFT:outy Suit(C);
                         |Piece 3 (orientation=RIGHT): TOP:outy Suit(S);RIGHT:inny Suit(S);BOTTOM:inny Suit(C);LEFT:outy Suit(H);
                         |
                         |PieceList: (9 pieces)
                         |Piece 1 (orientation=TOP): TOP:outy Suit(S);RIGHT:outy Suit(D);BOTTOM:inny Suit(H);LEFT:inny Suit(D);
                         |Piece 4 (orientation=BOTTOM): TOP:inny Suit(S);RIGHT:inny Suit(H);BOTTOM:outy Suit(C);LEFT:outy Suit(H);
                         |Piece 8 (orientation=BOTTOM): TOP:inny Suit(C);RIGHT:inny Suit(D);BOTTOM:outy Suit(D);LEFT:outy Suit(C);
                         |Piece 9 (orientation=LEFT): TOP:inny Suit(C);RIGHT:outy Suit(S);BOTTOM:outy Suit(S);LEFT:inny Suit(H);
                         |Piece 7 (orientation=LEFT): TOP:inny Suit(C);RIGHT:outy Suit(H);BOTTOM:outy Suit(D);LEFT:inny Suit(C);
                         |Piece 5 (orientation=TOP): TOP:inny Suit(S);RIGHT:inny Suit(H);BOTTOM:outy Suit(S);LEFT:outy Suit(D);
                         |Piece 6 (orientation=RIGHT): TOP:outy Suit(D);RIGHT:inny Suit(D);BOTTOM:inny Suit(H);LEFT:outy Suit(H);
                         |Piece 2 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(D);LEFT:inny Suit(C);
                         |Piece 3 (orientation=RIGHT): TOP:outy Suit(S);RIGHT:inny Suit(S);BOTTOM:inny Suit(C);LEFT:outy Suit(H);
                         |
                         |PieceList: (9 pieces)
                         |Piece 6 (orientation=TOP): TOP:outy Suit(H);RIGHT:outy Suit(D);BOTTOM:inny Suit(D);LEFT:inny Suit(H);
                         |Piece 3 (orientation=TOP): TOP:outy Suit(H);RIGHT:outy Suit(S);BOTTOM:inny Suit(S);LEFT:inny Suit(C);
                         |Piece 5 (orientation=BOTTOM): TOP:outy Suit(S);RIGHT:outy Suit(D);BOTTOM:inny Suit(S);LEFT:inny Suit(H);
                         |Piece 1 (orientation=LEFT): TOP:inny Suit(D);RIGHT:outy Suit(S);BOTTOM:outy Suit(D);LEFT:inny Suit(H);
                         |Piece 2 (orientation=BOTTOM): TOP:inny Suit(D);RIGHT:inny Suit(C);BOTTOM:outy Suit(C);LEFT:outy Suit(H);
                         |Piece 7 (orientation=RIGHT): TOP:outy Suit(D);RIGHT:inny Suit(C);BOTTOM:inny Suit(C);LEFT:outy Suit(H);
                         |Piece 4 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(S);LEFT:inny Suit(H);
                         |Piece 9 (orientation=TOP): TOP:outy Suit(S);RIGHT:outy Suit(S);BOTTOM:inny Suit(H);LEFT:inny Suit(C);
                         |Piece 8 (orientation=LEFT): TOP:inny Suit(D);RIGHT:outy Suit(D);BOTTOM:outy Suit(C);LEFT:inny Suit(C);
                         |""")) { params.findGlobalSamples(3).mkString("\n") }
  }
/*
  test("Find2GlobalSamples") {
    params = createParamArray(Set(2, -1, 3, -4))
    val samples = getListFromIterator(params.findGlobalSamples(2))
    assertResult(2) { samples.length }
  }

  test("Find3GlobalSamples") {
    params = createParamArray(Set(2, -1, 3, -4))
    val samples = getListFromIterator(params.findGlobalSamples(3))
    assertResult(3) { samples.length }
  }

  test("Find4GlobalSamples") {
    params = createParamArray(Set(2, -1, 3))
    val samples = getListFromIterator(params.findGlobalSamples(4))
    assertResult(4) { samples.length }
    val expParams = Array(
      createParamArray(Set(-1, 3)),
      createParamArray(Set(3, 2)),
      createParamArray(Set(-1, 2)),
      createParamArray(Set(2, -1))
    )
    assertResult(expParams) { samples }
  }

  test("Find10GlobalSamples") {
    params = createParamArray(Set(2, -1, 3, -5, 3, -4, -2, -3, 5, -9, 6))
    val samples = getListFromIterator(params.findGlobalSamples(10))
    assertResult(10)  { samples.length }
  }

  test("Find97GlobalSamples") {
    params = createParamArray(Set(2, -1, 3, -4))
    val samples = getListFromIterator(params.findGlobalSamples(97))
    assertResult(15) { samples.length }
  }

  test("swap nodes (4 params). r = 1.2") {
    params = createParamArray(Set(2, -1, 3, -4))
    val nbr = params.getRandomNeighbor(1.2)
    assertResult(strip("""
                         |parameter[0] = p2 = 2.0 [0, 2.0]
                         |parameter[1] = p-1 = -1.00 [-1.00, 0]
                         |parameter[2] = p3 = 3.0 [0, 3.0]
                         |parameter[3] = p-4 = -4.0 [-4.0, 0]
                         |fitness = 0.0""")) { nbr.toString }
  }

  test("swap nodes (4 params). r =  0.3") {
    params = createParamArray(Set(2, -1, 3, -4))
    val nbr = params.getRandomNeighbor(0.3)
    assertResult(strip("""
                         |parameter[0] = p2 = 2.0 [0, 2.0]
                         |parameter[1] = p-1 = -1.00 [-1.00, 0]
                         |parameter[2] = p3 = 3.0 [0, 3.0]
                         |parameter[3] = p-4 = -4.0 [-4.0, 0]
                         |fitness = 0.0""")) { nbr.toString }
  }

  test("swap nodes (11 params). r = 1.2") {
    params = createParamArray(Set(2, -1, 3, -5, 3, -4, -2, -3, 5, -9, 6))
    val nbr = params.getRandomNeighbor(1.2)
    assertResult(strip("""
                         |parameter[0] = p3 = 3.0 [0, 3.0]
                         |parameter[1] = p-1 = -1.00 [-1.00, 0]
                         |parameter[2] = p2 = 2.0 [0, 2.0]
                         |parameter[3] = p-2 = -2.0 [-2.0, 0]
                         |parameter[4] = p6 = 6.0 [0, 6.0]
                         |parameter[5] = p-4 = -4.0 [-4.0, 0]
                         |parameter[6] = p-9 = -9.0 [-9.0, 0]
                         |parameter[7] = p-5 = -5.0 [-5.0, 0]
                         |parameter[8] = p5 = 5.0 [0, 5.0]
                         |parameter[9] = p-3 = -3.0 [-3.0, 0]
                         |fitness = 0.0""")) { nbr.toString }
  }

  test("swap nodes (11 params). r =  0.3") {
    params = createParamArray(Set(2, -1, 3, -5, 3, -4, -2, -3, 5, -9, 6))
    val nbr = params.getRandomNeighbor(0.3)
    assertResult(strip("""
                         |parameter[0] = p3 = 3.0 [0, 3.0]
                         |parameter[1] = p-1 = -1.00 [-1.00, 0]
                         |parameter[2] = p2 = 2.0 [0, 2.0]
                         |parameter[3] = p-2 = -2.0 [-2.0, 0]
                         |parameter[4] = p6 = 6.0 [0, 6.0]
                         |parameter[5] = p-4 = -4.0 [-4.0, 0]
                         |parameter[6] = p-9 = -9.0 [-9.0, 0]
                         |parameter[7] = p-5 = -5.0 [-5.0, 0]
                         |parameter[8] = p5 = 5.0 [0, 5.0]
                         |parameter[9] = p-3 = -3.0 [-3.0, 0]
                         |fitness = 0.0""")) { nbr.toString }
  }*/

  private def getListFromIterator(iter: Iterator[PieceParameterArray]): Array[PieceParameterArray] =
    iter.toArray
}