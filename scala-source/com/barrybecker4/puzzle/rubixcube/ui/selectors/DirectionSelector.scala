// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.ui.selectors

import com.barrybecker4.puzzle.rubixcube.model.Direction

import javax.swing.JComboBox


final class DirectionSelector()
  extends JComboBox[String](Direction.values.map(_.toString)) {

  setSelectedIndex(0)
  setToolTipText("Rotation direction")

  def getSelectedDirection: Direction = Direction.values(this.getSelectedIndex)
}
