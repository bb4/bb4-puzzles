// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku

import com.barrybecker4.puzzle.sudoku.model.board.Board
import org.junit.Assert.{assertEquals, assertNotNull}
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
    assertEquals("Unexpected generated board", expBoard, board)
  }

  test("GenerateInitialSolution3") {
    val board = generateInitialSolution(3, rand)
    val expBoard = new Board(Array[Array[Int]](
      Array(9, 3, 8, 7, 6, 4, 5, 2, 1),
      Array(5, 7, 2, 3, 9, 1, 6, 4, 8),
      Array(1, 6, 4, 2, 5, 8, 7, 3, 9),
      Array(7, 8, 9, 5, 3, 6, 4, 1, 2),
      Array(3, 5, 6, 4, 1, 2, 8, 9, 7),
      Array(4, 2, 1, 9, 8, 7, 3, 5, 6),
      Array(2, 4, 3, 8, 7, 9, 1, 6, 5),
      Array(8, 1, 5, 6, 2, 3, 9, 7, 4),
      Array(6, 9, 7, 1, 4, 5, 2, 8, 3)))
    assertEquals("Unexpected generated board", expBoard, board)
  }

  /** works only half the time!    */
  test("GenerateInitialSolution4Many") {
    for (i <- 0 until 10) {
      val rand = new Random()
      rand.setSeed(i)
      val board = generateInitialSolution(4, rand)
      System.out.println(board)
      assertNotNull("Could not create a consistent board", board)
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
      Array(0, 2, 3, 0),
      Array(0, 0, 0, 0),
      Array(0, 3, 4, 0),
      Array(0, 4, 0, 0)))
    assertEquals("Unexpected generated board", expBoard, board)
  }

  private def generateInitialSolution(baseSize: Int, rand: Random): Board = {
    generator = new SudokuGenerator(baseSize, null, rand)
    val start = System.currentTimeMillis
    val b = new Board(baseSize)
    val solved = generator.generateSolution(b)
    System.out.println("SOLVED = " + solved + "  Time to generate solution for size=" + baseSize + " was " + (System.currentTimeMillis - start))
    //assertTrue("The board was not solved!", solved);
    if (!solved) null else b
  }

  private def generatePuzzle(baseSize: Int, rand: Random) = {
    generator = new SudokuGenerator(baseSize, null, rand)
    val start = System.currentTimeMillis
    val b = generator.generatePuzzleBoard
    System.out.println(" Time to generate size=" + baseSize + " was " + (System.currentTimeMillis - start))
    b
  }
}


