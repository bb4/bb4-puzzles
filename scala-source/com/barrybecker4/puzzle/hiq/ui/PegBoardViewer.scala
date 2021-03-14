// Copyright by Barry G. Becker, 2000-2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.hiq.ui

import com.barrybecker4.puzzle.common.ui.DoneListener
import com.barrybecker4.puzzle.common.ui.PathNavigator
import com.barrybecker4.puzzle.common.ui.PuzzleViewer
import com.barrybecker4.puzzle.hiq.model.{PegBoard, PegMove}
import java.awt.Graphics


/**
  * UI for drawing the current best solution to the puzzle.
  * @author Barry Becker
  */
final class PegBoardViewer(val theBoard: PegBoard, var doneListener: DoneListener)
  extends PuzzleViewer[PegBoard, PegMove] with PathNavigator {

  board = theBoard
  private val renderer: PegBoardRenderer = new PegBoardRenderer()
  private var path: List[PegMove] = _

  def getPath: List[PegMove] = path

  override def refresh(board: PegBoard, numTries: Long): Unit = {
    if (numTries % 4000 == 0) {
      status = createStatusMessage(numTries)
      simpleRefresh(board, numTries)
    }
  }

  override def animateTransition(transition: PegMove): PegBoard = {
    val newState = board.doMove(transition)
    simpleRefresh(newState)
    newState
  }

  override def finalRefresh(path: Option[Seq[PegMove]], board: Option[PegBoard],
                            numTries: Long, millis: Long): Unit = {
    super.finalRefresh(path, board, numTries, millis)
    if (board.isDefined) {
      makeSound()
      showPath(path.get.toList, board.get)
    }
  }

  def makeMove(currentStep: Int, undo: Boolean): Unit = {
    board = board.doMove(getPath(currentStep), undo)
    repaint()
  }

  /** This renders the current state of the puzzle to the screen. */
  override protected def paintComponent(g: Graphics): Unit = {
    super.paintComponent(g)
    renderer.render(g, board, getWidth, getHeight)
  }

  def showPath(thePath: List[PegMove], theBoard: PegBoard): Unit = {
    path = thePath
    board = theBoard
    if (doneListener != null) doneListener.done()
  }
}
