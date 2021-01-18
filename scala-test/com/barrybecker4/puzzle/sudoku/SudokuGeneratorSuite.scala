// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku

import com.barrybecker4.puzzle.sudoku.model.Board
import org.junit.Assert.{assertEquals, assertNotNull, assertTrue}
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

/**
  * @author Barry Becker
  */
class SudokuGeneratorSuite extends AnyFunSuite with BeforeAndAfter {

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

  /** Generate solutions for a bunch of random puzzles  */
  test("GenerateInitialSolution5Many") {
    for (i <- 0 to 10) {
      val rand = new Random()
      rand.setSeed(i)
      val board = generateInitialSolution(5, rand)
      //println(board)
      assertTrue("Could not create a consistent board", board.isDefined)
    }
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
    generator.generateSolution(b)
  }

  private def generatePuzzle(baseSize: Int, rand: Random): Board = {
    generator = new SudokuGenerator(null, rand)
    generator.generatePuzzleBoard(new Board(baseSize))
  }
}


