package com.barrybecker4.puzzle.maze.ui

import com.barrybecker4.puzzle.maze.MazeController
import com.barrybecker4.ui.components.GradientButton
import com.barrybecker4.ui.components.NumberInput
import com.barrybecker4.ui.sliders.LabeledSlider
import javax.swing.BorderFactory
import javax.swing.JPanel
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

/**
  * A maze generator and solver
  *
  * @author Barry Becker
  */
object TopControlPanel {
  /** the passage thickness in pixels */
  private val PASSAGE_THICKNESS = 40
  private val INITIAL_ANIMATION_SPEED = 20
}

class TopControlPanel(var controller: MazeController) extends JPanel with ActionListener {
  this.setLayout(new GridBagLayout)
  val c = new GridBagConstraints
  c.fill = GridBagConstraints.HORIZONTAL
  this.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2))
  private var thicknessField =
    new NumberInput("Thickness", TopControlPanel.PASSAGE_THICKNESS, "The passage thickness", 2, 200, true)
  private var animSpeedSlider = new LabeledSlider("Speed ", TopControlPanel.INITIAL_ANIMATION_SPEED, 1, 100)
  animSpeedSlider.setResolution(99)
  animSpeedSlider.setShowAsInteger(true)
  animSpeedSlider.addChangeListener(controller)

  private var forwardProbField =
    new NumberInput("Forward", 0.34, "The probability of moving straight forward", 0, 1.0, false)
  private var leftProbField = new NumberInput("Left", 0.33, "The probability of moving left", 0, 1.0, false)
  private var rightProbField = new NumberInput("Right", 0.33, "The probability of moving right", 0, 1.0, false)
  add(thicknessField)
  add(forwardProbField)
  add(leftProbField)
  add(rightProbField)
  add(animSpeedSlider)

  private var regenerateButton = new GradientButton("Generate")
  regenerateButton.addActionListener(this)
  add(regenerateButton)

  private var solveButton = new GradientButton("Solve")
  solveButton.addActionListener(this)
  add(solveButton)

  controller.setRepaintListener(this)

  /**
    * called when a button is pressed.
    */
  def actionPerformed(e: ActionEvent) {
    val source = e.getSource
    if (source eq regenerateButton) regenerate()
    if (source eq solveButton) controller.solve(getAnimationSpeed)
  }

  def regenerate() {
    controller.regenerate(getThickness, getAnimationSpeed, getForwardProbability, getLeftProbability, getRightProbability)
  }

  private def getThickness = thicknessField.getIntValue

  private def getForwardProbability = forwardProbField.getValue

  private def getLeftProbability = leftProbField.getValue

  private def getRightProbability = rightProbField.getValue

  private def getAnimationSpeed = animSpeedSlider.getValue.toInt
}