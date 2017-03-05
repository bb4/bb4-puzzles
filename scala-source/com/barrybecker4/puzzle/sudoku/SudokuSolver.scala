// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku

import java.awt.Container

import com.barrybecker4.common.concurrency.ThreadUtil
import com.barrybecker4.puzzle.sudoku.model.Board

/**
  * This does the hard work of actually solving the puzzle.
  * Controller in the model-view-controller pattern.
  *
  * @param puzzlePanel the viewer (may be null)
  * @author Barry Becker
  */
class SudokuSolver(puzzlePanel: Container = null) {

  var delay: Int = 0

  /**
    * Solves the puzzle.
    * This implements the main algorithm for solving the Sudoku puzzle.
    *
    * @param board       puzzle to solve
    * @return true if solved.
    */
  def solvePuzzle(board: Board): Boolean = {
    val solved = board.solve(Some(refresh _))
    if (solved) board.setSolvedValues()
    solved
  }

  private def refresh() {
    if (puzzlePanel != null && delay >= 0) {
      puzzlePanel.repaint()
      ThreadUtil.sleep(10 + delay) // give it a chance to repaint.
    }
  }
}