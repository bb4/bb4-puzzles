// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.twopails.ui

import com.barrybecker4.puzzle.common.AlgorithmEnum
import com.barrybecker4.puzzle.common.PuzzleController
import com.barrybecker4.puzzle.common.ui.TopControlPanel
import com.barrybecker4.puzzle.twopails.TwoPailsPuzzleController
import com.barrybecker4.puzzle.twopails.model.PailParams
import com.barrybecker4.puzzle.twopails.model.Pails
import com.barrybecker4.puzzle.twopails.model.PourOperation
import com.barrybecker4.ui.components.NumberInput
import javax.swing.JPanel
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import com.barrybecker4.puzzle.twopails.model.PailParams.MAX_CAPACITY


/**
  * Buttons at the top for generating and solving the puzzle using different strategies.
  *
  * @author Barry Becker
  */
final class TopControls (val controller: PuzzleController[Pails, PourOperation],
                         val algorithmValues: Array[AlgorithmEnum[Pails, PourOperation]])

/**
  * The solve and generate button at the top.
  */
  extends TopControlPanel[Pails, PourOperation](controller, algorithmValues) with KeyListener {
  // ui for entering the direction probabilities.
  private var firstPailSize: NumberInput = _
  private var secondPailSize: NumberInput = _
  private var targetMeasure: NumberInput = _

  override protected def addAdditionalControls(panel: JPanel): Unit = {
    super.addAdditionalControls(panel)
    firstPailSize =
      new NumberInput("First Pail Size", 9, "The fill capacity of the first container", 1, MAX_CAPACITY, true)
    secondPailSize =
      new NumberInput("Second Pail Size", 4, "The fill capacity of the second container", 1, MAX_CAPACITY, true)
    targetMeasure =
      new NumberInput("Target measure", 6, "The amount of liquid that is to be measured out exactly", 1,
        MAX_CAPACITY, true)
    firstPailSize.addKeyListener(this)
    secondPailSize.addKeyListener(this)
    targetMeasure.addKeyListener(this)
    panel.add(firstPailSize)
    panel.add(secondPailSize)
    panel.add(targetMeasure)
  }

  override def keyTyped(e: KeyEvent): Unit = {}
  override def keyPressed(e: KeyEvent): Unit = {}
  override def keyReleased(e: KeyEvent): Unit = {
    val params = new PailParams(firstPailSize.getIntValue, secondPailSize.getIntValue, targetMeasure.getIntValue)
    controller_.asInstanceOf[TwoPailsPuzzleController].setParams(params)
  }
}
