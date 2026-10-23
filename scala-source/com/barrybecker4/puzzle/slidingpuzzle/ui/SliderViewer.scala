// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.ui

import scala.compiletime.uninitialized
import com.barrybecker4.puzzle.common.ui.DoneListener
import com.barrybecker4.puzzle.common.ui.PathNavigator
import com.barrybecker4.puzzle.common.ui.PuzzleViewer
import com.barrybecker4.puzzle.slidingpuzzle.model.SlideMove
import com.barrybecker4.puzzle.slidingpuzzle.model.SliderBoard
import java.awt.Graphics


/**
  * UI for drawing the current best solution to the puzzle.
  * @param doneListener called when the puzzle has been solved.
  */
object SliderViewer {
  /** The shortest time between repaints while solving. */
  private val MIN_REPAINT_MILLIS = 50L
}

final class SliderViewer(var doneListener: DoneListener)
      extends PuzzleViewer[SliderBoard, SlideMove] with PathNavigator {

  private val renderer: SliderRenderer = new SliderRenderer
  private var path: List[SlideMove] = uninitialized
  private var lastRepaintMillis = 0L

  def getPath: List[SlideMove] = path

  /**
    * Shows the board the solver is working on, but at most about 20 times a second, because fast solvers can look at
    * millions of positions in that time. (Counting tries is not a good way to decide when to repaint, since a solver
    * may report every few thousand tries, or only some of them.)
    */
  override def refresh(theBoard: SliderBoard, numTries: Long): Unit = {
    board = theBoard
    val now = System.currentTimeMillis
    if (now - lastRepaintMillis >= SliderViewer.MIN_REPAINT_MILLIS) {
      lastRepaintMillis = now
      makeSound()
      status = createStatusMessage(numTries)
      simpleRefresh(board, numTries)
    }
  }

  override def animateTransition(transition: SlideMove): SliderBoard = {
    val newState = board.doMove(transition)
    simpleRefresh(newState)
    newState
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

