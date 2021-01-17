// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.ui

import java.awt.Choice

/**
  * A combo box that allows the user to select the speed at which the puzzle is generated or solved.
  * @author Barry Becker
  */
object SpeedSelector {
  private val DEFAULT_SELECTION = 1
  private val SPEED_CHOICES = Array(
    "Fastest (minimal animation)",
    "Fastest (with animation)",
    "Medium speed",
    "Slow speed",
    "Extremely slow"
  )
}

final class SpeedSelector private[ui]() extends Choice {
  for (item <- SpeedSelector.SPEED_CHOICES) {
    add(item)
  }
  select(SpeedSelector.DEFAULT_SELECTION)

  /** @return the delay for selected speed.*/
  def getSelectedDelay: Int = {
    this.getSelectedIndex match {
      case 0 => -1
      case 1 => 0
      case 2 => 10
      case 3 => 50
      case 4 => 400
      case idx: Int =>  throw new IllegalArgumentException("unexpected index: " + idx)
    }
  }
}
