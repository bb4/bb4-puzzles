// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.ui

import java.awt.event.{ActionEvent, ActionListener, ItemEvent, ItemListener}
import javax.swing._

import com.barrybecker4.puzzle.sudoku.SudokuController
import com.barrybecker4.ui.components.GradientButton

/**
  * Buttons at the top for generating and solving the puzzle using different strategies.
  * The solve and generate button at the top.
  * @author Barry Becker
  */
object TopControlPanel {
  /** initial value of the show candidates checkbox. */
  private val DEFAULT_SHOW_CANDIDATES = false
}

final class TopControlPanel(var controller: SudokuController)
      extends JPanel with ActionListener with ItemListener {

  controller.setShowCandidates(TopControlPanel.DEFAULT_SHOW_CANDIDATES)
  setLayout(new BoxLayout(this, BoxLayout.Y_AXIS))

  /** click this button to generate a new puzzle */
  private val generateButton: JButton = new GradientButton("Generate")
  generateButton.addActionListener(this)

  /** click this button to validate user entries. */
  private val validateButton: JButton = new GradientButton("Validate")
  validateButton.addActionListener(this)

  /** click this button to solve the current puzzle. */
  private val solveButton: JButton = new GradientButton("Solve")
  solveButton.addActionListener(this)

  private val sizeSelector: SizeSelector = new SizeSelector
  sizeSelector.addItemListener(this)

  private var speedSelector: SpeedSelector = _
  private var showCandidatesCheckBox: JCheckBox = _

  add(createRowOneControls)
  add(createRowTwoControls)


  private def createRowOneControls: JPanel = {
    val rowOne: JPanel = new JPanel
    rowOne.setLayout(new BoxLayout(rowOne, BoxLayout.X_AXIS))

    rowOne.add(generateButton)
    rowOne.add(validateButton)
    rowOne.add(solveButton)
    rowOne.add(sizeSelector)
    rowOne.add(Box.createHorizontalGlue)
    rowOne.add(Box.createHorizontalGlue)
    rowOne.add(Box.createHorizontalGlue)
    rowOne
  }

  private def createRowTwoControls: JPanel = {
    val rowTwo: JPanel = new JPanel
    rowTwo.setLayout(new BoxLayout(rowTwo, BoxLayout.X_AXIS))
    showCandidatesCheckBox = new JCheckBox("Show Candidates", TopControlPanel.DEFAULT_SHOW_CANDIDATES)
    showCandidatesCheckBox.addActionListener(this)
    speedSelector = new SpeedSelector
    speedSelector.addItemListener(this)
    rowTwo.add(showCandidatesCheckBox)
    rowTwo.add(speedSelector)
    rowTwo.add(Box.createHorizontalGlue)
    rowTwo.add(Box.createHorizontalGlue)
    rowTwo.add(Box.createHorizontalGlue)
    rowTwo.add(Box.createHorizontalGlue)
    rowTwo
  }

  /** Must execute long tasks in a separate thread, otherwise you don't see the steps of the animation. */
  def actionPerformed(e: ActionEvent): Unit = {
    val src: Any = e.getSource
    if (src == generateButton) {
      generatePuzzle(speedSelector.getSelectedDelay)
    }
    else if (src == validateButton) {
      controller.validatePuzzle()
      // figure out which user values are right or wrong.
    }
    else if (src == solveButton) {
      solvePuzzle()
    }
    else if (src == showCandidatesCheckBox) {
      controller.setShowCandidates(showCandidatesCheckBox.isSelected)
    }
  }

  private def generatePuzzle(delay: Int): Unit = {
    controller.generatePuzzle(delay, sizeSelector.getSelectedSize)
    solveButton.setEnabled(true)
    validateButton.setEnabled(true)
  }

  private def solvePuzzle(): Unit = {
    controller.solvePuzzle(speedSelector.getSelectedDelay)
    solveButton.setEnabled(false)
    validateButton.setEnabled(false)
  }

  /**
    * size choice selected.
    *
    * @param e item event.
    */
  def itemStateChanged(e: ItemEvent): Unit = {
    if (e.getSource == sizeSelector) {
      generatePuzzle(speedSelector.getSelectedDelay)
    }
    if (e.getSource == speedSelector) {
      controller.setDelay(speedSelector.getSelectedDelay)
    }
  }
}
