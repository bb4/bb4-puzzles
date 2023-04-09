// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.ui

import com.barrybecker4.puzzle.common.AlgorithmEnum
import com.barrybecker4.puzzle.common.ui.{NavigationPanel, _}
import com.barrybecker4.puzzle.rubixcube.{Algorithm, RubixCubeController}
import com.barrybecker4.puzzle.rubixcube.model.{Cube, CubeMove}
import com.barrybecker4.search.Refreshable
import com.barrybecker4.ui.util.GUIUtil

import javax.swing._


/**
  * Rubix Cube - https://en.wikipedia.org/wiki/Rubik%27s_Cube.
  * This program solves the well-known Rubix Cube puzzle.
  * For advanced solving approaches see
  * https://medium.com/@benjamin.botto/implementing-an-optimal-rubiks-cube-solver-using-korf-s-algorithm-bf750b332cf9
  */
object RubixCubePuzzle extends App {
  /** Use this to run as an application instead of an applet.  */
  val applet = new RubixCubePuzzle(args)
  // this will call applet.init() and start() methods instead of the browser
  GUIUtil.showApplet(applet)
}

case class RubixCubePuzzle(myargs: Array[String])
  extends PuzzleApplet[Cube, CubeMove](myargs) with DoneListener {

  private var navPanel: NavigationPanel = _

  /** Construct the application */
  def this() = { this(Array[String]()) }

  protected def createViewer = new CubeViewer(this)

  protected def createController(
      viewer: Refreshable[Cube, CubeMove]): RubixCubeController = new RubixCubeController(viewer)

  protected def getAlgorithmValues: Array[AlgorithmEnum[Cube, CubeMove]] =
    Algorithm.values.asInstanceOf[Array[AlgorithmEnum[Cube, CubeMove]]]

  override protected def createTopControls: RubixCubeTopControls =
    RubixCubeTopControls(controller.asInstanceOf[RubixCubeController], getAlgorithmValues)

  override protected def createBottomControls: JPanel = {
    navPanel = new NavigationPanel()
    navPanel
  }

  def done(): Unit = {
    navPanel.setPathNavigator(viewer.asInstanceOf[PathNavigator])
  }
}

