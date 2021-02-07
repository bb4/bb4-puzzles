// Copyright by Barry G. Becker, 20121 Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.ui

import com.barrybecker4.puzzle.common.ui.{DoneListener, PathNavigator, PuzzleViewer}
import com.barrybecker4.puzzle.rubixcube.model.{Cube, CubeMove}

import java.awt.Graphics


/**
  * UI for drawing the current best solution to the puzzle.
  * @param doneListener called when the puzzle has been solved.
  */
final class CubeViewer(var doneListener: DoneListener)
      extends PuzzleViewer[Cube, CubeMove] with PathNavigator {

  private val renderer: CubeRenderer = new CubeRenderer
  private var path: List[CubeMove] = _

  def getPath: List[CubeMove] = path

  override def refresh(theBoard: Cube, numTries: Long): Unit = {
    board = theBoard
    if (numTries % 500 == 0) {
      makeSound()
      status = createStatusMessage(numTries)
      simpleRefresh(board, numTries)
    }
  }

  override def finalRefresh(path: Option[Seq[CubeMove]], board: Option[Cube],
                            numTries: Long, millis: Long): Unit = {
    super.finalRefresh(path, board, numTries, millis)
    if (board.isDefined) showPath(path.get.toList, board.get)
  }

  def makeMove(currentStep: Int, undo: Boolean): Unit = {
    board = board.doMove(getPath(currentStep))
    repaint()
  }

  /** This renders the current state of the puzzle to the screen. */
  override protected def paintComponent(g: Graphics): Unit = {
    super.paintComponent(g)
    if (board != null) renderer.render(g, board, getWidth, getHeight)
  }

  private def showPath(thePath: List[CubeMove], theBoard: Cube): Unit = {
    path = thePath
    board = theBoard
    if (doneListener != null) doneListener.done()
  }
}
