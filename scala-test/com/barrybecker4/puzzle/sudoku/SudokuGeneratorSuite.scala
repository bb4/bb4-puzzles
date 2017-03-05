// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku

import com.barrybecker4.puzzle.sudoku.model.Board
import org.junit.Assert.{assertEquals, assertNotNull, assertTrue}
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.util.Random

/**
  * @author Barry Becker
  */
class SudokuGeneratorSuite extends FunSuite with BeforeAndAfter {
  /** instance under test. */
  private var generator: SudokuGenerator = _
  private var rand: Random = _

  before {
    rand = Random
    rand.setSeed(1)
  }

  test("GenerateInitialSolution2") {
    val board = generateInitialSolution(2, rand)
    val expBoard = new Board(Array[Array[Int]](
      Array(4, 2, 3, 1),
      Array(3, 1, 2, 4),
      Array(1, 3, 4, 2),
      Array(2, 4, 1, 3)))
//    val expBoard = new Board(Array[Array[Int]](
//      Array(4, 1, 3, 2),
//      Array(3, 2, 1, 4),
//      Array(1, 4, 2, 3),
//      Array(2, 3, 4, 1)))
    assertEquals("Unexpected generated board", expBoard.toString, board.get.toString)
  }

  test("GenerateInitialSolution3") {
    val board = generateInitialSolution(3, rand)
    val expBoard = new Board(Array[Array[Int]](
      Array(7, 6, 1, 9, 3, 5, 4, 2, 8),
      Array(4, 9, 2, 6, 7, 8, 3, 5, 1),
      Array(5, 8, 3, 1, 4, 2, 7, 9, 6),
      Array(3, 2, 8, 7, 5, 6, 9, 1, 4),
      Array(9, 1, 5, 2, 8, 4, 6, 3, 7),
      Array(6, 4, 7, 3, 9, 1, 2, 8, 5),
      Array(8, 7, 9, 5, 6, 3, 1, 4, 2),
      Array(2, 5, 6, 4, 1, 9, 8, 7, 3),
      Array(1, 3, 4, 8, 2, 7, 5, 6, 9)))

    //    val expBoard = new Board(Array[Array[Int]](
//      Array(7, 2, 4, 5, 1, 3, 6, 9, 8),
//      Array(8, 9, 1, 6, 2, 7, 3, 5, 4),
//      Array(5, 3, 6, 8, 9, 4, 7, 2, 1),
//      Array(6, 8, 7, 1, 5, 9, 4, 3, 2),
//      Array(1, 5, 2, 3, 4, 8, 9, 7, 6),
//      Array(3, 4, 9, 7, 6, 2, 8, 1, 5),
//      Array(9, 1, 3, 4, 8, 5, 2, 6, 7),
//      Array(2, 6, 8, 9, 7, 1, 5, 4, 3),
//      Array(4, 7, 5, 2, 3, 6, 1, 8, 9)))
    assertEquals("Unexpected generated board", expBoard.toString, board.get.toString)
  }

  /** Generate solutions for a bunch of random puzzles  */
  test("GenerateInitialSolution4Many") {
    for (i <- 0 to 6) {
      val rand = new Random()
      rand.setSeed(i)
      val board = generateInitialSolution(4, rand)
      assertTrue("Could not create a consistent board", board.isDefined)
    }
  }

  test("GenerateInitialSolution4") {
    rand.setSeed(0)
    val board = generateInitialSolution(4, rand)
    assertNotNull("Could not create a consistent board", board)
  }

  test("GeneratePuzzle2") {
    val board = generatePuzzle(2, rand)
    val expBoard = new Board(Array[Array[Int]](
      Array(4, 0, 0, 1),
      Array(3, 0,  0, 0),
      Array(0, 0, 0, 2),
      Array(0, 0, 0, 0)))

    assertEquals("Unexpected generated board", expBoard.toString, board.toString)
  }

  private def generateInitialSolution(baseSize: Int, rand: Random): Option[Board] = {
    generator = new SudokuGenerator(rand = rand)
    val b = new Board(baseSize)
    val solved = generator.generateSolution(b)
    //assertTrue("The board was not solved!", solved);
    if (!solved) None else Some(b)
  }

  private def generatePuzzle(baseSize: Int, rand: Random): Board = {
    generator = new SudokuGenerator(null, rand)
    generator.generatePuzzleBoard(new Board(baseSize))
  }
}


