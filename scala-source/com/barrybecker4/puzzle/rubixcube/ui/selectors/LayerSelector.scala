// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.ui.selectors

import java.awt.Choice


final class LayerSelector(initialSize: Int) extends Choice {

  private var numLayers: Int = initialSize

  for (layer <- 1 to numLayers) { add(layer.toString) }
  select(0)

  def getSelectedLayer: Int = this.getSelectedIndex + 1

  def setSize(newSize: Int): Unit = {
    this.removeAll()
    for (i <- 1 to newSize)
      add(i.toString)

    numLayers = newSize
  }
}