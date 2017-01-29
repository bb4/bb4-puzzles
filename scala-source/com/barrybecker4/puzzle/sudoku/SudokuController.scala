// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku

import java.awt.Cursor

import com.barrybecker4.common.concurrency.Worker
import com.barrybecker4.puzzle.sudoku.ui.SudokuPanel

/**
  * Controller part of the MVC pattern.
  *
  * @author Barry Becker
  */
final class SudokuController(var puzzlePanel: SudokuPanel) {

  def setShowCandidates (show: Boolean) {
    puzzlePanel.setShowCandidates (show)
  }

  def generatePuzzle (delay: Int, size: Int) {

    val worker: Worker = new Worker() {

      def construct: AnyRef = {
        puzzlePanel.setCursor (Cursor.getPredefinedCursor (Cursor.WAIT_CURSOR) )
        val generator: SudokuGenerator = new SudokuGenerator(size, puzzlePanel)
        generator.delay = delay
        puzzlePanel.generateNewPuzzle(generator)
        None
      }

      override def finished () {
        puzzlePanel.repaint ()
        puzzlePanel.setCursor (Cursor.getDefaultCursor)
      }
    }
    worker.start ()
  }

  def solvePuzzle (delay: Int) {
    val worker: Worker = new Worker() {
      def construct: AnyRef = {
        val solver: SudokuSolver = new SudokuSolver
        solver.delay = delay
        puzzlePanel.startSolving(solver)
        None
      }

      override def finished () {
        puzzlePanel.repaint ()
      }
    }
    worker.start ()
  }

  def validatePuzzle () {
    puzzlePanel.validatePuzzle ()
  }
}
