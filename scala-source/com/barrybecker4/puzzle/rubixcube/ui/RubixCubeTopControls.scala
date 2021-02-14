// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.ui

import com.barrybecker4.puzzle.common.AlgorithmEnum
import com.barrybecker4.puzzle.common.ui.TopControlPanel
import com.barrybecker4.puzzle.rubixcube.RubixCubeController
import com.barrybecker4.puzzle.rubixcube.model.{Cube, CubeMove}
import com.barrybecker4.puzzle.rubixcube.ui.selectors.{DirectionSelector, LayerSelector, OrientationSelector, SizeSelector}

import java.awt.FlowLayout
import java.awt.event.{ActionEvent, ActionListener, ItemEvent, ItemListener}
import javax.swing.{Box, JButton, JPanel}

/**
  * Buttons at the top for generating and solving the Rubix cube using different strategies.
  * Contains the solve and shuffle buttons at the top.
  * @author Barry Becker
  */
case class RubixCubeTopControls private[ui](
        override val controller: RubixCubeController,
        override val algorithmValues: Array[AlgorithmEnum[Cube, CubeMove]])
     extends TopControlPanel[Cube, CubeMove](controller, algorithmValues) with ItemListener {

  private var sizeSelector: SizeSelector = _
  private var rotatorPanel: JPanel = _

  private var rotateButton: JButton = _
  private var orientationSelector: OrientationSelector = _
  private var layerSelector: LayerSelector = _
  private var directionSelector: DirectionSelector = _

  override protected def addFirstRowControls(panel: JPanel): Unit = {
    super.addFirstRowControls(panel)
    sizeSelector = new SizeSelector
    sizeSelector.addItemListener(this)
    panel.add(sizeSelector)

    val rotator: JPanel = createRotatorControls()
    panel.add(rotator)
  }

  private def createRotatorControls(): JPanel = {
    rotatorPanel = new JPanel(new FlowLayout())
    rotateButton = new JButton("Rotate")
    rotateButton.addActionListener(this)

    orientationSelector = new OrientationSelector()
    layerSelector = new LayerSelector(sizeSelector.getSelectedSize)
    directionSelector = new DirectionSelector()

    rotatorPanel.add(rotateButton)
    rotatorPanel.add(orientationSelector)
    rotatorPanel.add(layerSelector)
    rotatorPanel.add(directionSelector)
    rotatorPanel
  }

  /** size choice selected.
    * @param e item event.
    */
  override def itemStateChanged(e: ItemEvent): Unit = {
    super.itemStateChanged(e)
    if (e.getSource eq sizeSelector) {
      controller.asInstanceOf[RubixCubeController].setSize(sizeSelector.getSelectedSize)
      rotatorPanel.remove(directionSelector)
      rotatorPanel.remove(layerSelector)
      layerSelector = new LayerSelector(sizeSelector.getSelectedSize)
      rotatorPanel.add(layerSelector)
      rotatorPanel.add(directionSelector)
    }
  }

  override def actionPerformed (e: ActionEvent): Unit = {
    super.actionPerformed(e)

    val src: Any = e.getSource
    if (src == rotateButton) {
      val move = CubeMove(
        orientationSelector.getSelectedOrientation,
        layerSelector.getSelectedLayer,
        directionSelector.getSelectedDirection
      )
      controller.doMove(move)
    }
  }

}
