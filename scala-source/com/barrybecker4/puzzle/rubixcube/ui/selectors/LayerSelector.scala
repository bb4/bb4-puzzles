// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.ui.selectors

import javax.swing.JComboBox


final class LayerSelector(initialSize: Int) extends JComboBox[String]() {

  private var numLayers: Int = initialSize

  setLayers(initialSize)
  setToolTipText("Layer to rotate")

  def getSelectedLayer: Int = this.getSelectedIndex + 1

  def setSize(newSize: Int): Unit = setLayers(newSize)

  private def setLayers(newSize: Int): Unit = {
    removeAllItems()
    for (i <- 1 to newSize) addItem(i.toString)
    numLayers = newSize
    if (getItemCount > 0) setSelectedIndex(0)
  }
}
