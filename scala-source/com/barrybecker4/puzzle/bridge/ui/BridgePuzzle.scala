// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge.ui

import com.barrybecker4.puzzle.bridge.{Algorithm, BridgePuzzleController}
import com.barrybecker4.puzzle.bridge.model.Bridge
import com.barrybecker4.puzzle.bridge.model.BridgeMove
import com.barrybecker4.puzzle.common.AlgorithmEnum
import com.barrybecker4.puzzle.common.PuzzleController
import com.barrybecker4.search.Refreshable
import com.barrybecker4.puzzle.common.ui.{DoneListener, NavigationPanel, PathNavigator, PuzzleApplet}
import com.barrybecker4.ui.util.GUIUtil
import javax.swing.JPanel

/**
  * This program solves a very difficult classic solitaire puzzle
  * where you select pairs of people to move across a bridge at night to get them all to the other side.
  */
object BridgePuzzle extends App {
  val applet = new BridgePuzzle(args)
  // this will call applet.init() and start() methods instead of the browser
  GUIUtil.showApplet(applet)
}

final class BridgePuzzle(args: Array[String])
  extends PuzzleApplet[Bridge, BridgeMove](args: Array[String]) with DoneListener {

  private val navPanel = new NavigationPanel()

  /** Default constructor */
  def this() = { this(Array[String]()) }

  protected def createViewer = new BridgeViewer(this)

  protected def createController(viewer: Refreshable[Bridge, BridgeMove]): PuzzleController[Bridge, BridgeMove] = {
    new BridgePuzzleController(viewer)
  }

  protected def getAlgorithmValues: Array[AlgorithmEnum[Bridge, BridgeMove]] =
    Algorithm.values.asInstanceOf[Array[AlgorithmEnum[Bridge, BridgeMove]]]

  override protected def createTopControls = new BridgeTopControls(controller, getAlgorithmValues)

  override protected def createBottomControls: JPanel = navPanel

  def done(): Unit = navPanel.setPathNavigator(viewer.asInstanceOf[PathNavigator])
}

