// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.ui.selectors

import com.barrybecker4.puzzle.rubixcube.ui.selectors.SizeSelector.DEFAULT_SIZE

import javax.swing.JComboBox

/**
  * Combo box for puzzle edge size. Swing (not AWT Choice) so it paints correctly over a GL canvas on macOS.
  */
object SizeSelector {
  val DEFAULT_SIZE: Int = 3

  private val MENU_ITEMS = Array(
    "edge size = 2",
    "edge size = 3 (standard)",
    "edge size = 4",
    "edge size = 5 (large)"
  )
}

final class SizeSelector() extends JComboBox[String](SizeSelector.MENU_ITEMS) {
  setSelectedIndex(DEFAULT_SIZE - 2)
  setToolTipText("Cube edge size")

  /** @return the puzzle size for what was selected. */
  def getSelectedSize: Int = this.getSelectedIndex + 2
}
