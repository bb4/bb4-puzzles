// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.maze.ui

import java.awt.Dimension
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent

/**
  * Called whenever the maze window resizes. Avoids redrawing if the size did not change.
  * @author Barry Becker
  */
class ResizeAdapter private[ui](var mazePanel: MazePanel, var topControls: TopControlPanel) extends ComponentAdapter {
  private var oldSize: Dimension = _

  override def componentResized(ce: ComponentEvent): Unit = {
    // only resize if the dimensions have changed
    val newSize: Dimension = mazePanel.getSize
    val changedSize = oldSize == null || oldSize.getWidth != newSize.getWidth || oldSize.getHeight != newSize.getHeight
    if (changedSize) {
      oldSize = newSize
      if (newSize.getWidth > 0) topControls.regenerate()
    }
  }
}
