// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.ui

import com.barrybecker4.puzzle.common.ui.DoneListener
import com.barrybecker4.puzzle.common.ui.PathNavigator
import com.barrybecker4.puzzle.common.ui.PuzzleViewer
import com.barrybecker4.puzzle.slidingpuzzle.model.SlideMove
import com.barrybecker4.puzzle.slidingpuzzle.model.SliderBoard
import java.awt.Graphics
import java.util
import collection.JavaConverters._

/**
  * UI for drawing the current best solution to the puzzle.
  * @param doneListener called when the puzzle has been solved.
  * @author Barry Becker
  */
final class SliderViewer(var doneListener: DoneListener)
      extends PuzzleViewer[SliderBoard, SlideMove] with PathNavigator {

  private val renderer: SliderRenderer = new SliderRenderer
  private var path: List[SlideMove] = _

  def getPath: List[SlideMove] = path

  override def refresh(theBoard: SliderBoard, numTries: Long): Unit = {
    board = theBoard
    if (numTries % 500 == 0) {
      makeSound()
      status = createStatusMessage(numTries)
      simpleRefresh(board, numTries)
    }
  }

  override def finalRefresh(path: Option[Seq[SlideMove]], board: Option[SliderBoard],
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

  private def showPath(thePath: List[SlideMove], theBoard: SliderBoard): Unit = {
    path = thePath
    board = theBoard
    if (doneListener != null) doneListener.done()
  }
}
