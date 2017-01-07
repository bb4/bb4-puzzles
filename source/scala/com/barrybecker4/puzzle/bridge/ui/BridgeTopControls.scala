// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge.ui

import com.barrybecker4.puzzle.bridge.model.Bridge
import com.barrybecker4.puzzle.bridge.model.BridgeMove
import com.barrybecker4.puzzle.common.AlgorithmEnum
import com.barrybecker4.puzzle.common.PuzzleController
import javax.swing.Box
import javax.swing.JPanel
import java.awt.event.ItemEvent
import java.awt.event.ItemListener

import com.barrybecker4.puzzle.bridge.BridgePuzzleController1
import com.barrybecker4.puzzle.common.ui.TopControlPanel

/**
  * Buttons at the top for generating and solving the puzzle using different strategies, and initial configurations.
  * The solve and generate button at the top.
  * @author Barry Becker
  */
final class BridgeTopControls(val controller: PuzzleController[Bridge, BridgeMove],
                              val algorithmValues: Array[AlgorithmEnum[Bridge, BridgeMove]])
  extends TopControlPanel[Bridge, BridgeMove](controller, algorithmValues) with ItemListener {
  private var configurationSelector: InitialConfigurationSelector1 = _

  override protected def addFirstRowControls(panel: JPanel) {
    super.addFirstRowControls(panel)
    configurationSelector = new InitialConfigurationSelector1()
    configurationSelector.addItemListener(this)
    panel.add(configurationSelector)
    panel.add(Box.createHorizontalGlue)
  }

  /**
    * size choice selected.
    *
    * @param e item event.
    */
  override def itemStateChanged(e: ItemEvent) {
    super.itemStateChanged(e)
    if (e.getSource eq configurationSelector)
      controller_.asInstanceOf[BridgePuzzleController1].setConfiguration(configurationSelector.getSelectedConfiguration)
  }
}
