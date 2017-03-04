/*
 * // Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
 */
package com.barrybecker4.puzzle.sudoku.model.board

import com.barrybecker4.common.math.MathUtil
import com.barrybecker4.puzzle.sudoku.data.TestData
import org.junit.Assert.{assertEquals, assertFalse, assertTrue}
import org.scalatest.{BeforeAndAfter, FunSuite}


/**
  * @author Barry Becker
  */
class BoardSuite extends FunSuite with BeforeAndAfter {

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
    board.solve()
    assertTrue("Unexpectedly not solved", board.isSolved)
  }

  test("Difficult to Solve") {
    board = new Board(TestData.DIFFICULT_9)
    board.solve()
    assertTrue("Unexpectedly not solved", board.isSolved)
  }

  // We expect an exception if the board is inconsistent
  test("Impossible to Solve") {

    board = new Board(TestData.INCONSISTENT_9)
    board.solve()
    assertFalse("Unexpectedly soved impossible puzzle", board.isSolved)
  }

  test("Completely Solved") {
    board = new Board(TestData.SIMPLE_4_SOLVED)
    board.solve()
    assertTrue("Unexpectedly not solved", board.isSolved)
  }

}


