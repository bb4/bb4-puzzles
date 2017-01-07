// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge.ui

import java.awt.Choice
import com.barrybecker4.puzzle.bridge.model.InitialConfiguration1._

/**
  * A combo box that allows the user to select the sort of people that need to cross
  *
  * @author Barry Becker
  */
object InitialConfigurationSelector {
  private val MENU_ITEMS = Array(
    STANDARD_PROBLEM.getLabel,
    ALTERNATIVE_PROBLEM.getLabel,
    DIFFICULT_PROBLEM.getLabel,
    SUPER_HARD.getLabel
  )
}

final class InitialConfigurationSelector extends Choice {

  for (item <- InitialConfigurationSelector.MENU_ITEMS) {
    add(item)
  }
  select(0)

  /**
    * @return the puzzle size for what was selected.
    */
  def getSelectedConfiguration: Array[Integer] = {
    val selected = getSelectedIndex
    selected match {
      case 0 => STANDARD_PROBLEM.getPeopleSpeeds
      case 1 => ALTERNATIVE_PROBLEM.getPeopleSpeeds
      case 2 => DIFFICULT_PROBLEM.getPeopleSpeeds
      case 3 => SUPER_HARD.getPeopleSpeeds
      case _ => throw new IllegalArgumentException("Unexpected selected index: " + selected)
    }
  }
}