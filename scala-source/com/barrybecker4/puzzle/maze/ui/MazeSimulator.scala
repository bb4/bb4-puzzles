// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.maze.ui

import com.barrybecker4.ui.application.ApplicationApplet
import com.barrybecker4.ui.util.GUIUtil
import javax.swing._
import java.awt._


/**
  * A maze generator and solver application.
  *
  * @author Barry Becker
  */
object MazeSimulator {
  def main(args: Array[String]) {
    val simulator = new MazeSimulator
    GUIUtil.showApplet(simulator)
  }
}

class MazeSimulator() extends ApplicationApplet {

  /**
    * Create and initialize the puzzle.
    * (init required for applet)
    */
  def createMainPanel: JPanel = {
    val mazePanel = new MazePanel
    val controller = new MazeController(mazePanel)
    val topControls = new TopControlPanel(controller)
    val panel = new JPanel(new BorderLayout)
    panel.add(topControls, BorderLayout.NORTH)
    panel.add(mazePanel, BorderLayout.CENTER)
    val compListener = new ResizeAdapter(mazePanel, topControls)
    panel.addComponentListener(compListener)
    panel
  }

  override def getName = "Maze Generator"
}