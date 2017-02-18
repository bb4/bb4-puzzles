/*
 * // Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
 */
package com.barrybecker4.puzzle.sudoku.model.board

import com.barrybecker4.common.math.MathUtil
import com.barrybecker4.puzzle.sudoku.data.TestData
import com.barrybecker4.puzzle.sudoku.model.update.updaters.{LoneRangerUpdater, StandardCRBUpdater}
import com.barrybecker4.puzzle.sudoku.model.update.{IBoardUpdater, ReflectiveBoardUpdater}
import org.junit.Assert.assertEquals
import org.scalatest.{BeforeAndAfter, FunSuite}

/**
  * @author Barry Becker
  */
class BoardUpdaterSuite extends FunSuite with BeforeAndAfter {

  /** instance under test */
  private var updater: IBoardUpdater = _
  private var board: Board = _

  before {
    MathUtil.RANDOM.setSeed(1)
    board = new Board(TestData.SIMPLE_4)
  }

  test("UpdateAndSetStandardCRB") {
    updater = new ReflectiveBoardUpdater(classOf[StandardCRBUpdater])
    updater.updateAndSet(board)
    val expectedSetValues = Array(
      Array(0, 4, 0, 0),
      Array(0, 1, 2, 0),
      Array(4, 3, 1, 2),
      Array(0, 2, 0, 0))
    verifySetValues(expectedSetValues, board)
  }

  test("UpdateAndSetStandardCRBAndLoneRanger") {
    updater = new ReflectiveBoardUpdater(classOf[StandardCRBUpdater], classOf[LoneRangerUpdater])
    println("initial board = " + board)
    updater.updateAndSet(board)
    val expectedSetValues = Array(
      Array(2, 4, 0, 1),
      Array(3, 1, 2, 4),
      Array(4, 3, 1, 2),
      Array(1, 2, 4, 3))
    verifySetValues(expectedSetValues, board)
  }

  test("UpdateAndSetLoneRangerAndStandardCRB") {
    updater = new ReflectiveBoardUpdater(classOf[LoneRangerUpdater], classOf[StandardCRBUpdater])
    updater.updateAndSet(board)
    val expectedSetValues = Array(Array(2, 4, 0, 1), Array(3, 1, 2, 4), Array(4, 3, 1, 2), Array(1, 2, 4, 3))
    verifySetValues(expectedSetValues, board)
  }

  test("UpdateAndSetLoneRangerOnly") {
    updater = new ReflectiveBoardUpdater(classOf[LoneRangerUpdater])
    updater.updateAndSet(board)
    val expectedSetValues = Array(
      Array(2, 4, 0, 0),
      Array(3, 1, 2, 4),
      Array(4, 3, 0, 2),
      Array(1, 2, 4, 3))
    verifySetValues(expectedSetValues, board)
  }

  private def verifySetValues(expectedSetValues: Array[Array[Int]], board: Board) {
    System.out.println("board=" + board)
    for (i <- 0 until board.edgeLength) {
      for (j <- 0 until board.edgeLength) {
        assertEquals("Unexpected set value at row=" + i + " col=" + j,
          expectedSetValues(i)(j), board.getCell(i, j).getValue)
      }
    }
  }
}


