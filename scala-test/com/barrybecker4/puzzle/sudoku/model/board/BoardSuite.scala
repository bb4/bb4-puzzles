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
    assertEquals("Unexpected board constructed", expectedBoard, board)
  }



  test("NotSolved") {
    board = new Board(TestData.SIMPLE_4)
    assertFalse("Unexpectedly solved", board.solved)
  }

  test("Solved") {
    board = new Board(TestData.SIMPLE_4_SOLVED)
    assertTrue("Unexpectedly not solved", board.solved)
  }

}


