// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.ui.selectors

import com.barrybecker4.puzzle.rubixcube.model.Orientation

import java.awt.Choice


final class OrientationSelector() extends Choice {

  for (orientation <- Orientation.PRIMARY_ORIENTATIONS) { add(orientation.toString) }
  select(0)

  def getSelectedOrientation: Orientation = Orientation.PRIMARY_ORIENTATIONS(this.getSelectedIndex)
}