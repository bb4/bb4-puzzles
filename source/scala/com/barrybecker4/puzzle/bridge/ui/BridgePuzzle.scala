// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge.ui

import com.barrybecker4.puzzle.bridge.{Algorithm, BridgePuzzleController, BridgePuzzleController1}
import com.barrybecker4.puzzle.bridge.model.Bridge
import com.barrybecker4.puzzle.bridge.model.BridgeMove
import com.barrybecker4.puzzle.common.AlgorithmEnum
import com.barrybecker4.puzzle.common.PuzzleController
import com.barrybecker4.common.search.Refreshable
import com.barrybecker4.puzzle.common.ui.{DoneListener, NavigationPanel, PathNavigator, PuzzleApplet}
import com.barrybecker4.ui.util.GUIUtil
import javax.swing.JPanel

/**
  * This program solves a very difficult classic solitaire puzzle
  * where you select pairs of people to move across a bridge at night to get them all to the other side.
  */
object BridgePuzzle {

  /**
    * Use this to run as an application instead of an applet.
    */
  def main(args: Array[String]) {
    val applet = new BridgePuzzle(args)
    // this will call applet.init() and start() methods instead of the browser
    GUIUtil.showApplet(applet)
  }
}

final class BridgePuzzle(args: Array[String])
  extends PuzzleApplet[Bridge, BridgeMove](args: Array[String]) with DoneListener {

  private var navPanel = new NavigationPanel()

  /** Default constructor */
  def this() {
    this(Array[String]())
  }

  protected def createViewer = new BridgeViewer(this)

  protected def createController(viewer: Refreshable[Bridge, BridgeMove]): PuzzleController[Bridge, BridgeMove] = {
    new BridgePuzzleController(viewer)
  }

  protected def getAlgorithmValues: Array[AlgorithmEnum[Bridge, BridgeMove]]/*Algorithm.ValueSet*/ =
    Algorithm.values.toArray.map(x => x.asInstanceOf[AlgorithmEnum[Bridge, BridgeMove]])

  override protected def createTopControls = new BridgeTopControls(controller_, getAlgorithmValues)

  override protected def createBottomControls: JPanel = {
    navPanel
  }

  def done() {
    navPanel.setPathNavigator(viewer_.asInstanceOf[PathNavigator])
  }
}

