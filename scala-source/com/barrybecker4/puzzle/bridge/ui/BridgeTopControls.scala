// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge.ui

import scala.compiletime.uninitialized

import com.barrybecker4.puzzle.bridge.BridgePuzzleController
import com.barrybecker4.puzzle.bridge.model.BridgeMove
import com.barrybecker4.puzzle.bridge.model.Bridge
import com.barrybecker4.puzzle.bridge.model.InitialConfiguration.configurations
import com.barrybecker4.puzzle.common.AlgorithmEnum
import com.barrybecker4.puzzle.common.ui.TopControlPanel
import java.awt.event.ItemEvent
import java.awt.event.ItemListener
import javax.swing.{JComboBox, JPanel}

/**
  * Buttons at the top for generating and solving the puzzle using different strategies, and initial configurations.
  * The solve and generate button at the top.
  * @author Barry Becker
  */
final class BridgeTopControls(bridgeController: BridgePuzzleController,
                              algorithmValues: Array[AlgorithmEnum[Bridge, BridgeMove]])
  extends TopControlPanel[Bridge, BridgeMove](bridgeController, algorithmValues) with ItemListener {

  private var configurationSelector: JComboBox[String] = uninitialized

  override protected def addFirstRowControls(panel: JPanel): Unit = {
    super.addFirstRowControls(panel)
    configurationSelector = new JComboBox[String]()
    configurations.foreach(c => configurationSelector.addItem(c.label))
    configurationSelector.addItemListener(this)
    panel.add(configurationSelector)
    panel.add(javax.swing.Box.createHorizontalGlue)
  }

  /**
    * Configuration or algorithm selection changed.
    *
    * @param e item event.
    */
  override def itemStateChanged(e: ItemEvent): Unit = {
    super.itemStateChanged(e)
    if (e.getSource eq configurationSelector) && e.getStateChange == ItemEvent.SELECTED then
      val people = configurations(configurationSelector.getSelectedIndex).peopleSpeeds
      bridgeController.setConfiguration(people)
  }
}
