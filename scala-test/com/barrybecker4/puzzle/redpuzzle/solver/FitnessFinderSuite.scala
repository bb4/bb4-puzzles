// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.solver

import com.barrybecker4.puzzle.redpuzzle.model.{Nub, Piece, PieceList, PieceLists}
import org.junit.Assert.assertEquals
import org.scalatest.{BeforeAndAfter, FunSuite}

/**
  * @author Barry Becker
  */
class FitnessFinderSuite extends FunSuite with BeforeAndAfter {
  /** instance under test */
  private val fitnessFinder = new FitnessFinder

  test("Fits") {
    val pieceList = new PieceList(PieceLists.getInitialPuzzlePieces)
    assertResult(23.0) {fitnessFinder.calculateFitness(pieceList)}
  }

  test("4PieceSomeFits") {
    val pieceList = new PieceList(Array[Piece](
      Piece(Nub.OUTY_SPADE, Nub.OUTY_DIAMOND, Nub.INNY_HEART, Nub.INNY_DIAMOND, 1),
      Piece(Nub.OUTY_CLUB, Nub.OUTY_HEART, Nub.INNY_DIAMOND, Nub.INNY_CLUB, 2),
      Piece(Nub.OUTY_HEART, Nub.OUTY_SPADE, Nub.INNY_SPADE, Nub.INNY_CLUB, 3),
      Piece(Nub.OUTY_CLUB, Nub.OUTY_HEART, Nub.INNY_SPADE, Nub.INNY_HEART, 4))
    )
    assertResult(23.0) {fitnessFinder.calculateFitness(pieceList)}
  }

  test("4PieceAllFits") {
    val pieceList = new PieceList(Array[Piece](
      Piece(Nub.OUTY_SPADE, Nub.OUTY_DIAMOND, Nub.INNY_HEART, Nub.INNY_DIAMOND, 1),
      Piece(Nub.OUTY_CLUB, Nub.OUTY_HEART, Nub.INNY_CLUB, Nub.INNY_DIAMOND, 2),
      Piece(Nub.OUTY_HEART, Nub.OUTY_SPADE, Nub.INNY_SPADE, Nub.INNY_CLUB, 3),
      Piece(Nub.OUTY_CLUB, Nub.OUTY_HEART, Nub.INNY_DIAMOND, Nub.INNY_SPADE, 4))
    )
    assertResult(17.0) {fitnessFinder.calculateFitness(pieceList)}
  }

  test("9PieceSomeFits") {
    val pieceList = PieceLists.getInitialPuzzlePieces
    assertResult(23.0) {fitnessFinder.calculateFitness(pieceList)}
  }

  /** should get exactly 1 3 fit bonus */
  test("9Piece3FitBonus") {
    val pieceList = new PieceList(Array[Piece](
      Piece(Nub.OUTY_SPADE, Nub.OUTY_CLUB, Nub.INNY_HEART, Nub.INNY_DIAMOND, 1),
      Piece(Nub.OUTY_CLUB, Nub.OUTY_HEART, Nub.INNY_DIAMOND, Nub.INNY_CLUB, 2),
      Piece(Nub.OUTY_HEART, Nub.OUTY_SPADE, Nub.INNY_SPADE, Nub.INNY_HEART, 3),
      Piece(Nub.OUTY_CLUB, Nub.OUTY_HEART, Nub.INNY_SPADE, Nub.INNY_HEART, 4),
      Piece(Nub.OUTY_DIAMOND, Nub.INNY_HEART, Nub.OUTY_SPADE, Nub.OUTY_DIAMOND, 5),
      Piece(Nub.OUTY_HEART, Nub.OUTY_DIAMOND, Nub.INNY_DIAMOND, Nub.INNY_HEART, 6),
      Piece(Nub.OUTY_HEART, Nub.OUTY_DIAMOND, Nub.INNY_CLUB, Nub.INNY_CLUB, 7),
      Piece(Nub.OUTY_DIAMOND, Nub.OUTY_CLUB, Nub.INNY_CLUB, Nub.INNY_DIAMOND, 8),
      Piece(Nub.OUTY_SPADE, Nub.OUTY_SPADE, Nub.INNY_HEART, Nub.INNY_CLUB, 9))
    )
    assertResult(14.9) {fitnessFinder.calculateFitness(pieceList)}
  }

  test("9PieceAllFit") {
    val pieceList = new PieceList(Array[Piece](
      Piece(Nub.OUTY_SPADE, Nub.OUTY_DIAMOND, Nub.INNY_HEART, Nub.INNY_DIAMOND, 1),
      Piece(Nub.INNY_CLUB, Nub.OUTY_CLUB, Nub.OUTY_HEART, Nub.INNY_DIAMOND, 2),
      Piece(Nub.INNY_CLUB, Nub.OUTY_HEART, Nub.OUTY_DIAMOND, Nub.INNY_CLUB, 3),
      Piece(Nub.OUTY_HEART, Nub.OUTY_SPADE, Nub.INNY_SPADE, Nub.INNY_CLUB, 4),
      Piece(Nub.INNY_HEART, Nub.OUTY_CLUB, Nub.OUTY_HEART, Nub.INNY_SPADE, 5),
      Piece(Nub.INNY_DIAMOND, Nub.OUTY_DIAMOND, Nub.OUTY_CLUB, Nub.INNY_CLUB, 6),
      Piece(Nub.OUTY_SPADE, Nub.OUTY_DIAMOND, Nub.INNY_SPADE, Nub.INNY_HEART, 7),
      Piece(Nub.INNY_HEART, Nub.OUTY_HEART, Nub.OUTY_DIAMOND, Nub.INNY_DIAMOND, 8),
      Piece(Nub.INNY_CLUB, Nub.OUTY_SPADE, Nub.OUTY_SPADE, Nub.INNY_HEART, 9))
    )
    assertResult(0.0) {fitnessFinder.calculateFitness(pieceList)}
  }
}
