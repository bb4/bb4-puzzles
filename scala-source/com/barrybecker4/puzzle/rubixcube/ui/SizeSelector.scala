// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.ui

import java.awt.Choice

/**
  * A combo box that allows the user to select the size of the puzzle
  */
object SizeSelector {
  private val MENU_ITEMS = Array(
    "edge size = 2",
    "edge size = 3 (standard)",
    "edge size = 4",
    "edge size = 5 (large)"
  )
}

final class SizeSelector() extends Choice {

  for (item <- SizeSelector.MENU_ITEMS) { add(item) }
  select(1)

  /** @return the puzzle size for what was selected.*/
  def getSelectedSize: Int = this.getSelectedIndex + 2
}