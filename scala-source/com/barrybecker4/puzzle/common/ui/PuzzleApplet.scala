// Copyright by Barry G. Becker, 2000-2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.common.ui

import com.barrybecker4.puzzle.common.AlgorithmEnum
import com.barrybecker4.puzzle.common.PuzzleController
import com.barrybecker4.search.Refreshable
import com.barrybecker4.ui.application.ApplicationApplet
import javax.swing.JPanel
import java.awt.BorderLayout
import java.util


/**
  * Base class for Puzzle applets.
  *
  * @author Barry Becker
  */
object PuzzleApplet {
  private val DEFAULT_PUZZLE = "com.barrybecker4.puzzle.maze.FractalExplorer"
}

abstract class PuzzleApplet[P, M](args: Array[String])  extends ApplicationApplet() {
  protected var controller:
  PuzzleController[P, M] = _
  protected var viewer: PuzzleViewer[P, M] = _

  /** Construct the application. */
  def this() { this(Array[String]())}

  /**
    * Create and initialize the puzzle
    * (init required for applet)
    */
  override protected def createMainPanel: JPanel = {
    val mainPanel = new JPanel(new BorderLayout)
    viewer = createViewer
    controller = createController(viewer)
    viewer.refresh(controller.initialState, 0)
    val topPanel = createTopControls
    mainPanel.add(topPanel, BorderLayout.NORTH)
    mainPanel.add(viewer, BorderLayout.CENTER)
    val bottomControls = createBottomControls
    if (bottomControls != null)
      mainPanel.add(bottomControls, BorderLayout.SOUTH)
    mainPanel
  }

  override protected def getResourceList: util.List[String] = {
    val resources = new util.ArrayList[String](super.getResourceList)
    resources.add("com.barrybecker4.puzzle.common.ui.message")
    resources
  }

  protected def createViewer: PuzzleViewer[P, M]

  protected def createController(viewer: Refreshable[P, M]): PuzzleController[P, M]

  protected def createTopControls: JPanel = new TopControlPanel[P, M](controller, getAlgorithmValues)

  protected def createBottomControls: JPanel

  protected def getAlgorithmValues: Array[AlgorithmEnum[P, M]]
}

