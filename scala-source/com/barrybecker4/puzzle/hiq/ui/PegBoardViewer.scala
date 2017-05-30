// Copyright by Barry G. Becker, 2000-2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.hiq.ui

import com.barrybecker4.puzzle.common.ui.DoneListener
import com.barrybecker4.puzzle.common.ui.PathNavigator
import com.barrybecker4.puzzle.common.ui.PuzzleViewer
import com.barrybecker4.puzzle.hiq.model.{PegBoard, PegMove}
import java.awt.Graphics
import java.util

import collection.JavaConverters._

/**
  * UI for drawing the current best solution to the puzzle.
  *
  * @author Barry Becker
  */
final class PegBoardViewer(val theBoard: PegBoard, var doneListener: DoneListener)
  extends PuzzleViewer[PegBoard, PegMove] with PathNavigator {

  board = theBoard
  private val renderer: PegBoardRenderer = new PegBoardRenderer()
  private var path: List[PegMove] = _

  def getPath: List[PegMove] = path

  override def refresh(board: PegBoard, numTries: Long) {
    if (numTries % 4000 == 0) {
      status = createStatusMessage(numTries)
      simpleRefresh(board, numTries)
    }
  }

  override def finalRefresh(path: util.List[PegMove], board: PegBoard, numTries: Long, millis: Long) {
    super.finalRefresh(path, board, numTries, millis)
    if (board != null) {
      makeSound()
      showPath(path.asScala.toList, board)
    }
  }

  def makeMove(currentStep: Int, undo: Boolean) {
    board = board.doMove(getPath(currentStep), undo)
    repaint()
  }

  /**
    * This renders the current state of the puzzle to the screen.
    */
  override protected def paintComponent(g: Graphics) {
    super.paintComponent(g)
    renderer.render(g, board, getWidth, getHeight)
  }

  def showPath(thePath: List[PegMove], theBoard: PegBoard) {
    path = thePath
    board = theBoard
    if (doneListener != null) doneListener.done()
  }
}

