// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku

import java.awt.Container
import com.barrybecker4.common.concurrency.ThreadUtil
import com.barrybecker4.puzzle.sudoku.model.Board

/**
  * This does the hard work of actually solving the puzzle.
  * @param puzzlePanel the viewer (may be null if no UI)
  * @author Barry Becker
  */
class SudokuSolver(puzzlePanel: Container = null) {

  var delay: Int = 0

  /** Solves the puzzle.
    * This implements the main algorithm for solving the Sudoku puzzle.
    * @param board puzzle to solve
    * @return number of iterations or None if not solved.
    */
  def solvePuzzle(board: Board): Option[Int] = {
    val numIterations = board.solve(Some(refresh _))
    if (numIterations.isDefined) board.setSolvedValues()
    numIterations
  }

  private def refresh(): Unit = {
    if (puzzlePanel != null && delay >= 0) {
      puzzlePanel.repaint()
      ThreadUtil.sleep(10 + delay) // give it a chance to repaint.
    }
  }
}
