// Copyright by Barry G. Becker, 2013-2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.common.ui

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.ui.components.GradientButton
import com.barrybecker4.ui.util.GUIUtil
import javax.swing.{JButton, JPanel}
import java.awt.BorderLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener


/**
  * Shows two buttons to control stepping forward and backward through the puzzle solution.
  */
object NavigationPanel {
  private val IMAGE_ROOT = "com/barrybecker4/puzzle/common/ui/images/" // NON-NLS

}

final class NavigationPanel() extends JPanel(new BorderLayout) with ActionListener {
  add(createBackPanel, BorderLayout.WEST)
  add(createForwardPanel, BorderLayout.EAST)
  private var backButton: JButton = _
  private var forwardButton: JButton = _
  private var startButton: JButton = _
  private var endButton: JButton = _
  private var currentStep: Int = 0
  private var navigator: PathNavigator = _

  private def createForwardPanel = {
    forwardButton = createButton("FORWARD", "forward_arrow.png")
    endButton = createButton("END", "end_arrow.png")
    val forwardPanel = new JPanel
    forwardPanel.add(forwardButton)
    forwardPanel.add(endButton)
    forwardPanel
  }

  private def createBackPanel = {
    backButton = createButton("BACKWARD", "backward_arrow.png")
    startButton = createButton("START", "start_arrow.png")
    val backPanel = new JPanel
    backPanel.add(startButton)
    backPanel.add(backButton)
    backPanel
  }

  private def createButton(msgKey: String, iconName: String) = {
    val button = new GradientButton(AppContext.getLabel(msgKey), GUIUtil.getIcon(NavigationPanel.IMAGE_ROOT + iconName))
    button.addActionListener(this)
    button.setEnabled(false)
    button
  }

  def setPathNavigator(navigator: PathNavigator): Unit = {
    this.navigator = navigator
    currentStep = navigator.getPath.size - 1
    updateButtonStates()
  }

  override def actionPerformed(e: ActionEvent): Unit = {
    if (e.getSource eq backButton) moveInPath(-1)
    else if (e.getSource eq forwardButton) moveInPath(1)
    else if (e.getSource eq startButton) moveInPath(-currentStep - 1)
    else if (e.getSource eq endButton) {
      val stepsUntilEnd = navigator.getPath.size - currentStep - 1
      moveInPath(stepsUntilEnd)
    }
    updateButtonStates()
  }

  private def updateButtonStates(): Unit = {
    val isAtStart = currentStep == -1
    val isAtEnd = currentStep == navigator.getPath.size - 1
    backButton.setEnabled(!isAtStart)
    startButton.setEnabled(!isAtStart)
    forwardButton.setEnabled(!isAtEnd)
    endButton.setEnabled(!isAtEnd)
  }

  /**
    * Switch from the current move in the sequence forwards or backwards stepSize.
    *
    * @param stepSize num steps to move.
    */
  def moveInPath(stepSize: Int): Unit = {
    if (stepSize == 0) return
    moveInPath(currentStep, stepSize)
    currentStep += stepSize
  }

  def moveInPath(currentPosition: Int, stepSize: Int): Unit = {
    var currentStep = currentPosition
    val moveForward = stepSize > 0
    val inc = if (moveForward) 1
    else -1
    var toStep = currentStep + stepSize
    if (moveForward) {
      currentStep += 1
      toStep += 1
    }
    do {
      println("makeMove " + currentStep + "+ fwd=" + moveForward)
      navigator.makeMove(currentStep, !moveForward)
      currentStep += inc
    } while (currentStep != toStep)
  }
}

