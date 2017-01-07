// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge.ui

import com.barrybecker4.common.format.FormatUtil
import com.barrybecker4.puzzle.bridge.model.Bridge
import com.barrybecker4.puzzle.bridge.model.BridgeMove
import com.barrybecker4.puzzle.common.ui.DoneListener
import com.barrybecker4.puzzle.common.ui.PathNavigator
import com.barrybecker4.puzzle.common.ui.PuzzleViewer
import java.awt.Graphics

/**
  * UI for drawing the current best solution to the puzzle.
  * @param doneListener called when the puzzle has been solved.
  * @author Barry Becker
  */
final class BridgeViewer private[ui](var doneListener: DoneListener)
  extends PuzzleViewer[Bridge, BridgeMove] with PathNavigator {

  private val renderer_ = new BridgeRenderer()
  private var path_ = List[BridgeMove]()

  def getPath: List[BridgeMove] = path_

  override def finalRefresh(path: List[BridgeMove], board: Bridge, numTries: Long, millis: Long) {
    super.finalRefresh(path, board, numTries, millis)
    if (board != null) showPath(path, board)
  }

  def makeMove(currentStep: Int, undo: Boolean) {
    board_ = board_.applyMove(getPath.get(currentStep), undo)
    repaint()
  }

  override protected def createFinalStatusMessage(numTries: Long, millis: Long, path: List[BridgeMove]): String = {
    val time = millis.toFloat / 1000.0f
    var msg = "Did not find solution."
    if (path != null) msg = "Found solution with total time = " + findCost(path) + " in " + FormatUtil.formatNumber(time) + " seconds. " + createStatusMessage(numTries)
    msg
  }

  private def findCost(path: List[BridgeMove]) = {
    var totalCost = 0
    import scala.collection.JavaConversions._
    for (m <- path) {
      totalCost += m.getCost
    }
    totalCost
  }

  /**
    * This renders the current state of the puzzle to the screen.
    */
  override protected def paintComponent(g: Graphics) {
    super.paintComponent(g)
    if (board_ != null) renderer_.render(g, board_, getWidth, getHeight)
  }

  def showPath(path: List[BridgeMove], board: Bridge) {
    path_ = path
    board_ = board
    if (doneListener != null) doneListener.done()
  }
}

