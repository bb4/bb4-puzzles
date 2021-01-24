// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku

import com.barrybecker4.common.concurrency.ThreadUtil
import com.barrybecker4.puzzle.sudoku.model.Board
import com.barrybecker4.puzzle.sudoku.ui.SudokuPanel

/**
  * This does the hard work of actually solving the puzzle.
  * @param puzzlePanel the viewer (may be null if no UI)
  * @author Barry Becker
  */
class SudokuSolver(puzzlePanel: SudokuPanel = null) {

  var delay: Int = 0

  /** Solves the puzzle.
    * This implements the main algorithm for solving the Sudoku puzzle.
    * @param board puzzle to solve
    * @return number of iterations or None if not solved.
    */
  def solvePuzzle(board: Board): Option[Board] = board.solve(Some(refresh))

  private def refresh(board: Board): Unit = {
    if (puzzlePanel != null && delay >= 0) {
      puzzlePanel.repaint(board)
      ThreadUtil.sleep(10 + delay) // give it a chance to repaint.
    }
  }
}
