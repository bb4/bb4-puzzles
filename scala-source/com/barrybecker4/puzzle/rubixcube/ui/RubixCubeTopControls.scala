// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.ui

import com.barrybecker4.puzzle.common.AlgorithmEnum
import com.barrybecker4.puzzle.common.ui.TopControlPanel
import com.barrybecker4.puzzle.rubixcube.RubixCubeController
import com.barrybecker4.puzzle.rubixcube.model.{CubeMove, Cube}

import java.awt.event.{ItemEvent, ItemListener}
import javax.swing.{Box, JPanel}

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

  override protected def addFirstRowControls(panel: JPanel): Unit = {
    super.addFirstRowControls(panel)
    sizeSelector = new SizeSelector
    sizeSelector.addItemListener(this)
    panel.add(sizeSelector)
    panel.add(Box.createHorizontalGlue)
  }

  /** size choice selected.
    * @param e item event.
    */
  override def itemStateChanged(e: ItemEvent): Unit = {
    super.itemStateChanged(e)
    if (e.getSource eq sizeSelector)
      controller.asInstanceOf[RubixCubeController].setSize(sizeSelector.getSelectedSize)
  }
}
