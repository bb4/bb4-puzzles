// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.ui.selectors

import com.barrybecker4.puzzle.rubixcube.model.Direction
import java.awt.Choice


final class DirectionSelector() extends Choice {

  for (direction <- Direction.values) { add(direction.toString) }
  select(0)

  def getSelectedDirection: Direction = Direction.values(this.getSelectedIndex)
}