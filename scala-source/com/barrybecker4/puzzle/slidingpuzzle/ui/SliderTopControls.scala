// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.ui

import com.barrybecker4.puzzle.common.AlgorithmEnum
import com.barrybecker4.puzzle.common.PuzzleController
import com.barrybecker4.puzzle.common.ui.TopControlPanel
import com.barrybecker4.puzzle.slidingpuzzle.SlidingPuzzleController
import com.barrybecker4.puzzle.slidingpuzzle.model.SlideMove
import com.barrybecker4.puzzle.slidingpuzzle.model.SliderBoard
import javax.swing.Box
import javax.swing.JPanel
import java.awt.event.ItemEvent
import java.awt.event.ItemListener

/**
  * Buttons at the top for generating and solving the puzzle using different strategies.
  * Contains the solve and generate button at the top.
  *
  * @author Barry Becker
  */
case class SliderTopControls private[ui](
        controller: PuzzleController[SliderBoard, SlideMove],
        algorithmValues: Array[AlgorithmEnum[SliderBoard, SlideMove]])
     extends TopControlPanel[SliderBoard, SlideMove](controller, algorithmValues) with ItemListener {

  private var sizeSelector: SizeSelector = _

  override protected def addFirstRowControls(panel: JPanel) {
    super.addFirstRowControls(panel)
    sizeSelector = new SizeSelector
    sizeSelector.addItemListener(this)
    panel.add(sizeSelector)
    panel.add(Box.createHorizontalGlue)
  }

  /**
    * size choice selected.
    *
    * @param e item event.
    */
  override def itemStateChanged(e: ItemEvent) {
    super.itemStateChanged(e)
    if (e.getSource eq sizeSelector)
      controller_.asInstanceOf[SlidingPuzzleController].setSize(sizeSelector.getSelectedSize)
  }
}
