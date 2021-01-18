// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT

package com.barrybecker4.puzzle.sudoku.model

import com.barrybecker4.math.MathUtil
import com.barrybecker4.puzzle.sudoku.data.TestData
import org.junit.Assert.{assertEquals, assertFalse, assertTrue}
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite


/**
  * @author Barry Becker
  */
class BoardSuite extends AnyFunSuite with BeforeAndAfter {

  /** instance under test */
  private var board: Board = _

  before {
    MathUtil.RANDOM.setSeed(1)
  }

  test("BoardConstruction") {
    val board = new Board(3)
    val expectedBoard = new Board(Array[Array[Int]](
      Array(0, 0, 0, 0, 0, 0, 0, 0, 0),
      Array(0, 0, 0, 0, 0, 0, 0, 0, 0),
      Array(0, 0, 0, 0, 0, 0, 0, 0, 0),
      Array(0, 0, 0, 0, 0, 0, 0, 0, 0),
      Array(0, 0, 0, 0, 0, 0, 0, 0, 0),
      Array(0, 0, 0, 0, 0, 0, 0, 0, 0),
      Array(0, 0, 0, 0, 0, 0, 0, 0, 0),
      Array(0, 0, 0, 0, 0, 0, 0, 0, 0),
      Array(0, 0, 0, 0, 0, 0, 0, 0, 0))
    )
    assertEquals("Unexpected board constructed", expectedBoard.toString, board.toString)
  }


  test("Easy to Solve") {
    board = new Board(TestData.SIMPLE_4)
    val solution = board.solve()
    assertTrue("Unexpectedly not solved", solution.isDefined && solution.get.isSolved)
  }

  test("Difficult to Solve") {
    board = new Board(TestData.DIFFICULT_9)
    val solution = board.solve()
    assertTrue("Unexpectedly not solved", solution.isDefined && solution.get.isSolved)
  }

  test("Norvig hard 9") {
    board = new Board(TestData.NORVIG_HARD_9)
    val solution = board.solve()
    assertTrue("Unexpectedly not solved", solution.isDefined && solution.get.isSolved)
  }

  // We cannot solve an invalid or inconsistent puzzle
  test("Impossible to Solve") {

    board = new Board(TestData.INCONSISTENT_9)
    val solution = board.solve()
    assertFalse("Unexpectedly solved impossible puzzle", solution.isDefined)
  }

  test("Invalid puzzle") {

    board = new Board(TestData.INVALID_9)
    val solution = board.solve()
    assertFalse("Unexpectedly solved invalid puzzle", solution.isDefined)
  }

  test("Completely Solved") {
    board = new Board(TestData.SIMPLE_4_SOLVED)
    val solution = board.solve()
    assertTrue("Unexpectedly not solved", solution.isDefined && solution.get.isSolved)
  }

  test("Large complex 16") {
    board = new Board(TestData.COMPLEX_16)
    val solution = board.solve()

    assertResult(16) {board.edgeLength}
    assertResult(256) {board.numCells}

    assertTrue("Unexpectedly not solved", solution.isDefined && solution.get.isSolved)
  }
}


