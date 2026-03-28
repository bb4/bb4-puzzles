// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT

package com.barrybecker4.puzzle.sudoku.model

import scala.compiletime.uninitialized
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
  private var board: Board = uninitialized

  before {
    MathUtil.RANDOM.setSeed(1)
  }

  test("updateFromInitialData removes assigned value from peer candidates") {
    val empty = new Board(2)
    val withOne = empty.setOriginalValue((1, 1), 1).updateFromInitialData().get
    assertFalse(withOne.getValues((1, 2)).contains(1))
    assertFalse(withOne.getValues((2, 1)).contains(1))
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

  test("Remove if possible on 4x4") {
    board = new Board(TestData.SIMPLE_4)
    assertTrue(
      "Removing this clue should not leave a puzzle fully determined by propagation alone",
      board.removeValueIfPossible((1, 2)).isEmpty
    )
  }

  test("Difficult to Solve") {
    board = new Board(TestData.DIFFICULT_9)
    val solution = board.solve()
    assertTrue("Unexpectedly not solved", solution.isDefined && solution.get.isSolved)
  }

  test("Remove if possible on 9x9") {
    board = new Board(TestData.DIFFICULT_9)
    assertTrue(
      "Removing this clue should not leave a puzzle fully determined by propagation alone",
      board.removeValueIfPossible((1, 3)).isEmpty
    )
  }

  test("Remove redundant clue yields Some without refresh callback") {
    val solved = new Board(TestData.SIMPLE_4_SOLVED).updateFromInitialData().get
    assertTrue("Sanity check: filled valid grid should be propagation-solved", solved.isSolved)
    val locations = for (r <- solved.comps.digits; c <- solved.comps.digits) yield (r, c)
    val removable = locations.find(loc => solved.removeValueIfPossible(loc).isDefined)
    assertTrue(
      "Expected at least one clue removable while keeping full propagation to singletons",
      removable.isDefined
    )
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



