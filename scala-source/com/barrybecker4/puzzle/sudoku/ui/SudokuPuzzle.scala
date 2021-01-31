// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.ui

import java.awt.BorderLayout
import javax.swing.{JApplet, JPanel, SwingUtilities}
import com.barrybecker4.puzzle.sudoku.{Data, SudokuController}
import com.barrybecker4.ui.util.GUIUtil

/**
  * Sudoku Puzzle UI.
  * This program can generate and solve Sudoku puzzles.
  * @author Barry Becker
  */
object SudokuPuzzle extends App {
  val applet = new SudokuPuzzle
  // this will call applet.init() and start() methods instead of the browser
  GUIUtil.showApplet(applet)
}

/** Construct the application and set the look and feel. */
final class SudokuPuzzle() extends JApplet {

  GUIUtil.setCustomLookAndFeel()

  /**
    * Create and initialize the puzzle.
    * (init required for applet)
    */
  override def init(): Unit = {
    val puzzlePanel = new SudokuPanel(Data.DLBEER_551_9)
    val controller = new SudokuController(puzzlePanel)
    val topControls = new TopControlPanel(controller)
    val panel = new JPanel(new BorderLayout)
    panel.add(topControls, BorderLayout.NORTH)
    panel.add(puzzlePanel, BorderLayout.CENTER)
    getContentPane.add(panel)
  }

  /** Called by the browser after init(), if running as an applet. */
  override def start(): Unit = {
    SwingUtilities.invokeLater(() => getContentPane.repaint())
  }

  override def getName = "Sudoku Puzzle Solver"
}
