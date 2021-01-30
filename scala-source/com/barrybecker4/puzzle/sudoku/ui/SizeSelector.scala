// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.ui

import java.awt.Choice

/**
  * A combo box that allows the user to select the size of the puzzle
  * @author Barry Becker
  */
object SizeSelector {
  private val SIZE_MENU_ITEMS = Array(
    "4 cells on a side",
    "9 cells on a side",
    "16 cells on a side",
    "25 cells (prepare to wait)"
  )
}

final class SizeSelector() extends Choice {

  SizeSelector.SIZE_MENU_ITEMS.foreach(item => add(item))
  select(1)

  /** @return the puzzle size for what was selected.*/
  def getSelectedSize: Int = this.getSelectedIndex + 2
}
