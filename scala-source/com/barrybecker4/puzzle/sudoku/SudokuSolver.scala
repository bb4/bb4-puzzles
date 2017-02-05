// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku

import java.awt.Container

import com.barrybecker4.common.concurrency.ThreadUtil
import com.barrybecker4.puzzle.sudoku.model.board.Board
import com.barrybecker4.puzzle.sudoku.model.update.{IBoardUpdater, NonReflectiveBoardUpdater}

/**
  * This does the hard work of actually solving the puzzle.
  * Controller in the model-view-controller pattern.
  *
  * @author Barry Becker
  */
class SudokuSolver() {

  var updater: IBoardUpdater = new NonReflectiveBoardUpdater()
  var delay: Int = 0

  /**
    * Solves the puzzle.
    * This implements the main algorithm for solving the Sudoku puzzle.
    *
    * @param board       puzzle to solve
    * @param puzzlePanel the viewer (may be null)
    * @return true if solved.
    */
  def solvePuzzle(board: Board, puzzlePanel: Container = null): Boolean = {
    var solved = false
    // not sure what this should be.
    val maxIterations = 2 * board.edgeLength
    //println("initial board = " + board)
    do {
      solved = doIteration(board)
      refreshWithDelay(puzzlePanel, 3)
      //println("iteration = " + board.getNumIterations)
    } while (!solved && board.getNumIterations < maxIterations)
    refresh(puzzlePanel)
    // if we get here and solved is not true, we did not find a solution.
    solved
  }

  def doIteration(board: Board): Boolean = {
    // find missing row and column numbers
    updater.updateAndSet(board)
    board.incrementNumIterations()
    board.solved
  }

  private def refreshWithDelay(puzzlePanel: Container, relativeDelay: Int) {
    if (delay <= 0) {
      if (Math.random() < 0.1) refresh(puzzlePanel)
    }
    else {
      refresh(puzzlePanel)
      ThreadUtil.sleep(relativeDelay * delay)
    }
  }

  private def refresh(puzzlePanel: Container) {
    if (puzzlePanel == null || delay < 0) return
    puzzlePanel.repaint()
    ThreadUtil.sleep(10 + delay) // give it a chance to repaint.
  }
}