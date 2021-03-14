// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge.ui

import java.awt.Graphics

import com.barrybecker4.common.format.FormatUtil
import com.barrybecker4.puzzle.bridge.model.{Bridge, BridgeMove}
import com.barrybecker4.puzzle.common.ui.{DoneListener, PathNavigator, PuzzleViewer}


/**
  * UI for drawing the current best solution to the puzzle.
  * @param doneListener called when the puzzle has been solved.
  * @author Barry Becker
  */
final class BridgeViewer private[ui](var doneListener: DoneListener)
  extends PuzzleViewer[Bridge, BridgeMove] with PathNavigator {

  private val renderer = new BridgeRenderer()
  private var thePath = List[BridgeMove]()
  private var lastMove: Option[BridgeMove] = None

  def getPath: List[BridgeMove] = thePath

  override def finalRefresh(path: Option[Seq[BridgeMove]], board: Option[Bridge],
                            numTries: Long, millis: Long): Unit = {
    super.finalRefresh(path, board, numTries, millis)
    if (board.isDefined) showPath(path.get, board.get)
  }

  override def refresh(board: Bridge, numTries: Long): Unit = {
    super.refresh(board, numTries)
    if (numTries == 0) lastMove = None
  }

  override def animateTransition(state: Bridge, transition: BridgeMove): Bridge = {
    val newState = state.applyMove(transition, reverse = false)
    simpleRefresh(newState)
    newState
  }

  def makeMove(currentStep: Int, undo: Boolean): Unit = {
    val m = getPath(currentStep)
    board = board.applyMove(m, undo)
    lastMove = Some(BridgeMove(m.people, if (undo) !m.direction else m.direction))
    repaint()
  }

  override protected def createFinalStatusMessage(numTries: Long, millis: Long,
                                                  path: Option[Seq[BridgeMove]]): String = {
    val time = millis.toFloat / 1000.0f
    var msg = "Did not find solution."
    if (path.isDefined) msg = "Found solution with total time = " + findCost(path.get.toList) + " in " +
      FormatUtil.formatNumber(time) + " seconds. " + createStatusMessage(numTries)
    msg
  }

  private def findCost(path: List[BridgeMove]) = path.map(_.cost).sum

  /** This renders the current state of the puzzle to the screen. */
  override protected def paintComponent(g: Graphics): Unit = {
    super.paintComponent(g)
    if (board != null) renderer.render(g, board, lastMove, getWidth, getHeight)
  }

  def showPath(path: Seq[BridgeMove], theBoard: Bridge): Unit = {
    thePath = path.toList
    board = theBoard
    if (doneListener != null) doneListener.done()
  }
}

