// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.ui.selectors

import com.barrybecker4.puzzle.rubixcube.ui.selectors.SizeSelector.DEFAULT_SIZE

import java.awt.Choice

/**
  * A combo box that allows the user to select the size of the puzzle
  */
object SizeSelector {
  val DEFAULT_SIZE: Int = 4

  private val MENU_ITEMS = Array(
    "edge size = 2",
    "edge size = 3 (standard)",
    "edge size = 4",
    "edge size = 5 (large)"
  )
}

final class SizeSelector() extends Choice {

  for (item <- SizeSelector.MENU_ITEMS) { add(item) }
  select(DEFAULT_SIZE - 2)

  /** @return the puzzle size for what was selected.*/
  def getSelectedSize: Int = this.getSelectedIndex + 2
}