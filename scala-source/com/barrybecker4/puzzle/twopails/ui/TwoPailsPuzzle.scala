// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.twopails.ui

import com.barrybecker4.puzzle.common.{AlgorithmEnum, PuzzleController}
import com.barrybecker4.search.Refreshable
import com.barrybecker4.puzzle.common.ui.DoneListener
import com.barrybecker4.puzzle.common.ui.NavigationPanel
import com.barrybecker4.puzzle.common.ui.PathNavigator
import com.barrybecker4.puzzle.common.ui.PuzzleApplet
import com.barrybecker4.puzzle.twopails.Algorithm
import com.barrybecker4.puzzle.twopails.TwoPailsPuzzleController
import com.barrybecker4.puzzle.twopails.model.Pails
import com.barrybecker4.puzzle.twopails.model.PourOperation
import com.barrybecker4.ui.util.GUIUtil
import javax.swing.JPanel


/**
  * This was a puzzle described by Peter Norvig in his excellent "Design of Computer Programs" class on Udacity.
  * You start with two containers of size M and N liters. You need to measure out X liters using them.
  * What are the steps to do it?
  * For example you have two containers that hold 9 and 4 liters respectively. How do you measure out 6 liters?
  * See http://www.cut-the-knot.org/ctk/CartWater.shtml#solve
  */
object TwoPailsPuzzle {
  /**
    * Use this to run as an application instead of an applet.
    */
  def main(args: Array[String]): Unit = {
    val applet = new TwoPailsPuzzle(args)
    // this will call applet.init() and start() methods instead of the browser
    GUIUtil.showApplet(applet)
  }
}

final class TwoPailsPuzzle(args: Array[String]) extends PuzzleApplet[Pails, PourOperation] with DoneListener {

  private var navPanel: NavigationPanel = _

  /** Construct the application */
  def this() = { this(Array[String]())}

  override protected def createViewer = new TwoPailsViewer(this)

  protected def createController(viewer: Refreshable[Pails, PourOperation] ): PuzzleController[Pails, PourOperation] = {
    new TwoPailsPuzzleController(viewer)
  }

  override protected def getAlgorithmValues: Array[AlgorithmEnum[Pails, PourOperation]] =
    Algorithm.values.asInstanceOf[Array[AlgorithmEnum[Pails, PourOperation]]];

  override protected def createTopControls = new TopControls(controller, getAlgorithmValues)

  override protected def createBottomControls: JPanel = {
    navPanel = new NavigationPanel()
    navPanel
  }

  override def done(): Unit = {
    navPanel.setPathNavigator(viewer.asInstanceOf[PathNavigator])
  }
}

