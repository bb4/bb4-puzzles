// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.ui.selectors

import java.awt.Choice


final class LayerSelector(size: Int) extends Choice {

  for (layer <- 1 to size) { add(layer.toString) }
  select(0)

  def getSelectedLayer: Int = this.getSelectedIndex + 1
}