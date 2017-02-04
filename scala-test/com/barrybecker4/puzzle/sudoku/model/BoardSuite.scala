// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model

import com.barrybecker4.common.math.MathUtil
import com.barrybecker4.puzzle.sudoku.data.TestData
import com.barrybecker4.puzzle.sudoku.model.board.{Board, Candidates, ValuesList}
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

  test("FindCellCandidatesForAll") {
    board = new Board(TestData.SIMPLE_4)
    val expCands = Array(
      Array(new Candidates(1, 2, 3), null, new Candidates(1, 3), new Candidates(1, 3)),
      Array(new Candidates(1, 3), new Candidates(1), null, new Candidates(1, 3, 4)),
      Array(null, null, new Candidates(1), new Candidates(1, 2)),
      Array(new Candidates(1, 2), new Candidates(1, 2), new Candidates(1, 3, 4), new Candidates(1, 2, 3, 4))
    )
    var valid = true
    for (i <- 0 until board.getEdgeLength) {
      for (j <- 0 until board.getEdgeLength) {
        val cands = board.getCell(i, j).getCandidates
        if (expCands(i)(j) ne cands) valid = false
      }
    }
    if (!valid) System.out.println("baord = " + board)
    for (i <- 0 until board.getEdgeLength) {
      for (j <- 0 until board.getEdgeLength) {
        val cands = board.getCell(i, j).getCandidates
        assertEquals("Did find correct candidates for cell row=" + i + " j=" + j, expCands(i)(j), cands)
      }
    }
  }

  test("FindShuffledCellCandidates2") {
    board = new Board(TestData.SIMPLE_4)
    val cands = ValuesList.getShuffledCandidates(board.getCell(0).getCandidates)
    checkCandidates(List(2, 3, 1), cands)
  }

  test("FindShuffledCellCandidates3") {
    board = new Board(TestData.SIMPLE_9)
    var cands = ValuesList.getShuffledCandidates(board.getCell(0).getCandidates)
    checkCandidates(List(3, 5), cands)
    cands = ValuesList.getShuffledCandidates(board.getCell(1).getCandidates)
    checkCandidates(List(4, 5, 3, 1), cands)
    cands = ValuesList.getShuffledCandidates(board.getCell(2).getCandidates)
    val expList = List()
    checkCandidates(expList, cands)
  }

  private def checkCandidates(expCands: List[Integer], actCands: ValuesList) {
    assertEquals("Did find correct candidates", expCands, actCands.elements)
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


