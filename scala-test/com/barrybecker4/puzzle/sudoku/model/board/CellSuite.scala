/*
 * // Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
 */
package com.barrybecker4.puzzle.sudoku.model.board

import com.barrybecker4.puzzle.sudoku.data.TestData
import org.junit.Assert.{assertEquals, assertTrue}
import org.scalatest.{BeforeAndAfter, FunSuite}

/**
  * @author Barry Becker
  */
class CellSuite extends FunSuite with BeforeAndAfter {

  /** instance under test */
  private var cell: Cell = _
  private var board: Board = _

  test("FindCellCandidatesForFirstCell") {
    board = new Board(TestData.SIMPLE_4)
    cell = board.getCell(0, 0)
    val expCands = new Candidates(1, 2, 3) // everything but 4.
    assertEquals("Did find correct candidates", expCands, cell.getCandidates)
  }

  test("FindCellCandidatesForMiddleCell") {
    board = new Board(TestData.SIMPLE_4)
    cell = board.getCell(1, 1)
    val expCands = new Candidates(1)
    assertEquals("Did find correct candidates", expCands, cell.getCandidates)
  }

  /** Set an appropriate legal value */
  test("SetValueValid") {
    board = new Board(TestData.SIMPLE_4)
    cell = board.getCell(1, 1)
    assertEquals("Unexpected before candidates", new Candidates(1), cell.getCandidates)
    System.out.println("before" + board)
    cell.setValue(1)
    System.out.println("after" + board)
    // the candidate lists should be reduced.
    assertEquals("Unexpected value ", 1, cell.getValue)
    assertTrue(cell.getCandidates.isEmpty)
    assertEquals("Unexpected row 1 cands", new Candidates(3, 4), board.getRowCells.get(1).candidates)
    assertEquals("Unexpected col 1 cands", new Candidates(3, 4), board.getRowCells.get(1).candidates)
    assertEquals("Unexpected bigCell 0,0 cands", new Candidates(2, 3), board.getBigCell(0, 0).candidates)
  }

  /** Calling clear on a cell should undo a set. */
  test("ClearReversesSet") {
    val origBoard = new Board(TestData.SIMPLE_4)
    board = new Board(origBoard)
    cell = board.getCell(1, 1)
    cell.setValue(1)
    cell.clearValue()
    //assertResult(origBoard)("Unexpectedly not the same ", board)
    assertEquals("Unexpectedly not the same ", origBoard, board)
  }
}


