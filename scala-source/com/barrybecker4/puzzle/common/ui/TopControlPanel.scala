// Copyright by Barry G. Becker, 2000-2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.common.ui

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.puzzle.common.AlgorithmEnum
import com.barrybecker4.puzzle.common.PuzzleController
import com.barrybecker4.ui.components.GradientButton
import javax.swing._
import java.awt._
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.ItemEvent
import java.awt.event.ItemListener


/**
  * Shows the main button controls in a panel above the puzzle.
  *
  * @author Barry Becker
  */
class TopControlPanel[P, M](
                 val controller: PuzzleController[P, M],
                 val algorithmValues: Array[AlgorithmEnum[P, M]])
  extends JPanel with ActionListener with ItemListener {

  val firstRowPanel: JPanel = new JPanel (createLayout)
  addFirstRowControls (firstRowPanel)
  firstRowPanel.add (new JPanel)
  val additionalControlsPanel: JPanel = new JPanel (createLayout)
  addAdditionalControls (additionalControlsPanel)
  val mainPanel: JPanel = new JPanel (new BorderLayout)
  mainPanel.add (firstRowPanel, BorderLayout.NORTH)
  if (additionalControlsPanel.getComponents.length > 0) {
    mainPanel.add (additionalControlsPanel, BorderLayout.CENTER)
  }
  add (mainPanel)
  private var solveButton: JButton = _
  private var algorithmChoice: JComboBox[String] = _

  private def createLayout: FlowLayout = {
    val layout: FlowLayout = new FlowLayout
    layout.setAlignment(FlowLayout.LEADING)
    layout
  }

  protected def addFirstRowControls (firstRowPanel: JPanel): Unit = {
    solveButton = new GradientButton (AppContext.getLabel ("SOLVE") )
    solveButton.addActionListener (this)
    firstRowPanel.add (solveButton)
    firstRowPanel.add (createAlgorithmDropdown)
  }
  protected def addAdditionalControls (panel: JPanel): Unit = {
    // override to add stuff
  }
  protected def getController: PuzzleController[P, M] = {
    controller
  }

  /**
    * The dropdown menu at the top for selecting an algorithm for solving the puzzle.
    * @return a dropdown/down component.
    */
  private def createAlgorithmDropdown: JComboBox[String] = {
    algorithmChoice = new JComboBox[String]
    algorithmChoice.addItemListener (this)
    for (a <- algorithmValues) {
      algorithmChoice.addItem (a.getLabel)
    }
    algorithmChoice.setSelectedIndex (controller.algorithm.ordinal)
    algorithmChoice
  }

  /**
    * algorithm selected.
    * @param e item event
    */
  override def itemStateChanged (e: ItemEvent): Unit = {
    val selected: Int = algorithmChoice.getSelectedIndex
    controller.algorithm = algorithmValues(selected)
  }

  /**
    * Solve button clicked.
    * Must execute long tasks in a separate thread,
    * otherwise you don't see the steps of the animation.
    */
  override def actionPerformed (e: ActionEvent): Unit = {
    val src: Any = e.getSource
    if (src == solveButton) {
      controller.startSolving ()
    }
  }
}

