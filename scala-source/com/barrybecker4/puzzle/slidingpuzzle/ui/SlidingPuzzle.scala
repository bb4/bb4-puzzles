// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.ui

import com.barrybecker4.search.Refreshable
import com.barrybecker4.puzzle.common.AlgorithmEnum
import com.barrybecker4.puzzle.common.PuzzleController
import com.barrybecker4.puzzle.common.ui._
import com.barrybecker4.puzzle.common.ui.NavigationPanel
import com.barrybecker4.puzzle.slidingpuzzle.Algorithm
import com.barrybecker4.puzzle.slidingpuzzle.SlidingPuzzleController
import com.barrybecker4.puzzle.slidingpuzzle.model.SlideMove
import com.barrybecker4.puzzle.slidingpuzzle.model.SliderBoard
import com.barrybecker4.ui.util.GUIUtil
import javax.swing._

/**
  * Sliding Puzzle - http://en.wikipedia.org/wiki/Sliding_puzzle.
  * This program solves a difficult classic solitaire puzzle where you slide tiles to make the correct sequence.
  */
object SlidingPuzzle extends App {
  /** Use this to run as an application instead of an applet.  */
  val applet = new SlidingPuzzle(args)
  // this will call applet.init() and start() methods instead of the browser
  GUIUtil.showApplet(applet)
}

case class SlidingPuzzle(myargs: Array[String])
  extends PuzzleApplet[SliderBoard, SlideMove](myargs) with DoneListener {

  private var navPanel: NavigationPanel = _

  /** Construct the application */
  def this() { this(Array[String]()) }

  protected def createViewer = new SliderViewer(this)

  protected def createController(
      viewer: Refreshable[SliderBoard, SlideMove]): PuzzleController[SliderBoard, SlideMove] = {
    new SlidingPuzzleController(viewer)
  }

  protected def getAlgorithmValues: Array[AlgorithmEnum[SliderBoard, SlideMove]] = Algorithm.VALUES

  override protected def createTopControls = SliderTopControls(controller, getAlgorithmValues)

  override protected def createBottomControls: JPanel = {
    navPanel = new NavigationPanel()
    navPanel
  }

  def done(): Unit = {
    navPanel.setPathNavigator(viewer.asInstanceOf[PathNavigator])
  }
}

