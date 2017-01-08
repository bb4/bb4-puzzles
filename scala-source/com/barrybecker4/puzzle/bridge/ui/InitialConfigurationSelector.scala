// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge.ui

import java.awt.Choice
import com.barrybecker4.puzzle.bridge.model._
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
    HARDER_PROBLEM.getLabel
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
  def getSelectedConfiguration: Array[Int] = {
    val selected = getSelectedIndex
    selected match {
      case 0 => STANDARD_PROBLEM.peopleSpeeds
      case 1 => ALTERNATIVE_PROBLEM.peopleSpeeds
      case 2 => DIFFICULT_PROBLEM.peopleSpeeds
      case 3 => HARDER_PROBLEM.peopleSpeeds
      case _ => throw new IllegalArgumentException("Unexpected selected index: " + selected)
    }
  }
}