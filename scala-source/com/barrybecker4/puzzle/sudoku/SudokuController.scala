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

  private var solver: Option[SudokuSolver] = None
  private var generator: Option[SudokuGenerator] = None

  def setShowCandidates(show: Boolean): Unit = puzzlePanel.setShowCandidates(show)
  def validatePuzzle(): Unit = puzzlePanel.validatePuzzle()

  def generatePuzzle(delay: Int, size: Int): Unit = {

    val worker: Worker = new Worker() {

      def construct: AnyRef = {
        puzzlePanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR))
        val gen = new SudokuGenerator(Some(puzzlePanel))
        gen.delay = delay
        generator = Some(gen)
        puzzlePanel.generateNewPuzzle(gen, size)
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
        val sol = new SudokuSolver(Some(puzzlePanel))
        sol.delay = delay
        solver = Some(sol)
        puzzlePanel.startSolving(sol)
        None
      }

      override def finished(): Unit = {
        puzzlePanel.repaint()
      }
    }
    worker.start()
  }

  def setDelay(delay: Int): Unit = {
    solver.foreach(_.delay = delay)
    generator.foreach(_.delay = delay)
  }
}
