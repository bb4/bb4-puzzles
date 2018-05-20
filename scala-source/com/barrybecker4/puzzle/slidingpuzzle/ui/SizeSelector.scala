// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.ui

import java.awt.Choice

/**
  * A combo box that allows the user to select the size of the puzzle
  * @author Barry Becker
  */
object SizeSelector {
  private val MENU_ITEMS = Array(
    "3 Tiles",
    "8 Tiles",
    "15 Tiles",
    "24 Tiles (long time to solve)"
  )
}

final class SizeSelector() extends Choice {

  for (item <- SizeSelector.MENU_ITEMS) { add(item) }
  select(1)

  /** @return the puzzle size for what was selected.*/
  def getSelectedSize: Int = this.getSelectedIndex + 2
}