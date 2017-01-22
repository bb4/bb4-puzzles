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
  *
  * @param doneListener called when the puzzle has been solved.
  * @author Barry Becker
  */
final class SliderViewer(var doneListener: DoneListener)
      extends PuzzleViewer[SliderBoard, SlideMove] with PathNavigator {

  private val renderer: SliderRenderer = new SliderRenderer
  private var path: List[SlideMove] = _

  def getPath: util.List[SlideMove] = path.asJava

  override def refresh(board: SliderBoard, numTries: Long) {
    board_ = board
    if (numTries % 500 == 0) {
      makeSound()
      status_ = createStatusMessage(numTries)
      simpleRefresh(board, numTries)
    }
  }

  override def finalRefresh(path: util.List[SlideMove], board: SliderBoard, numTries: Long, millis: Long) {
    super.finalRefresh(path, board, numTries, millis)
    if (board != null) showPath(path.asScala.toList, board)
  }

  def makeMove(currentStep: Int, undo: Boolean) {
    board_ = board_.doMove(getPath.get(currentStep))
    repaint()
  }

  /**
    * This renders the current state of the puzzle to the screen.
    */
  override protected def paintComponent(g: Graphics) {
    super.paintComponent(g)
    if (board_ != null) renderer.render(g, board_, getWidth, getHeight)
  }

  private def showPath(thePath: List[SlideMove], board: SliderBoard) {
    path = thePath
    board_ = board
    if (doneListener != null) doneListener.done()
  }
}