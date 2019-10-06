// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku

import java.awt.Cursor

import com.barrybecker4.common.concurrency.Worker
import com.barrybecker4.puzzle.sudoku.ui.SudokuPanel

/**
  * Controller part of the MVC pattern.
  * @author Barry Becker
  */
final class SudokuController(var puzzlePanel: SudokuPanel) {

  def setShowCandidates(show: Boolean): Unit = puzzlePanel.setShowCandidates(show)
  def validatePuzzle(): Unit = puzzlePanel.validatePuzzle()

  def generatePuzzle(delay: Int, size: Int): Unit = {

    val worker: Worker = new Worker() {

      def construct: AnyRef = {
        puzzlePanel.setCursor (Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) )
        val generator: SudokuGenerator = new SudokuGenerator(puzzlePanel)
        generator.delay = delay
        puzzlePanel.generateNewPuzzle(generator, size)
        None
      }

      override def finished(): Unit = {
        puzzlePanel.repaint()
        puzzlePanel.setCursor(Cursor.getDefaultCursor)
      }
    }
    worker.start()
  }

  def solvePuzzle(delay: Int): Unit = {
    val worker: Worker = new Worker() {
      def construct: AnyRef = {
        val solver: SudokuSolver = new SudokuSolver(puzzlePanel)
        solver.delay = delay
        puzzlePanel.startSolving(solver)
        None
      }

      override def finished(): Unit = {
        puzzlePanel.repaint()
      }
    }
    worker.start()
  }
}
