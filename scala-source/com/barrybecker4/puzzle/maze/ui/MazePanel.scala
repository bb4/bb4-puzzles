// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.maze.ui

import com.barrybecker4.common.concurrency.ThreadUtil
import com.barrybecker4.common.geometry.Location
import com.barrybecker4.common.math.MathUtil
import com.barrybecker4.puzzle.maze.model.MazeModel
import javax.swing.JComponent
import java.awt.{Dimension, Graphics, Graphics2D}

/**
  * This panel is responsible for drawing the MazeModel using the MazeRenderer.
  *
  * @author Barry Becker
  */
class MazePanel() extends JComponent {

  val maze = new MazeModel(100, 100)
  private val renderer = new MazeRenderer()
  var animationSpeed: Int = 0
  private var cellSize = 0

  def setThickness(thickness: Int) {
    val dim: Dimension = this.getSize
    if (dim.width <= 0 || dim.height < 0) return
    cellSize = thickness
    renderer.setCellSize(cellSize)
    val width = dim.width / thickness
    val height = dim.height / thickness
    maze.setDimensions(width, height)
  }

  /** paint the whole window right now! */
  def paintAll() {
    val d: Dimension = this.getSize
    this.paintImmediately(0, 0, d.getWidth.toInt, d.getHeight.toInt)
  }

  /**
    * Paint just the region around a single cell for performance.
    *
    * @param point location of the cell to render.
    */
  def paintCell(point: Location) {
    val csized2 = (cellSize / 2) + 2
    val xpos = point.getX * cellSize
    val ypos = point.getY * cellSize
    if (animationSpeed <= 10) {
      // this paints just the cell immediately (sorta slow)
      this.paintImmediately(xpos - csized2, ypos - csized2, 2 * cellSize, 2 * cellSize)
      if (animationSpeed < 9) ThreadUtil.sleep(400 / animationSpeed - 40)
    }
    else {
      val rand = MathUtil.RANDOM.nextDouble
      if (rand < 1.0 / animationSpeed) this.repaint(xpos - csized2, ypos - csized2, 2 * cellSize, 2 * cellSize)
    }
  }

  /** Render the Environment on the screen. */
  override def paintComponent(g: Graphics) {
    super.paintComponent(g)
    renderer.render(g.asInstanceOf[Graphics2D], maze)
  }
}
