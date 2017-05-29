// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.twopails.ui

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.puzzle.common.ui.DoneListener
import com.barrybecker4.puzzle.common.ui.PathNavigator
import com.barrybecker4.puzzle.common.ui.PuzzleViewer
import com.barrybecker4.puzzle.twopails.model.Pails
import com.barrybecker4.puzzle.twopails.model.PourOperation
import javax.swing.JOptionPane
import java.awt.Graphics
import java.util


/**
  * UI for drawing the current state of the puzzle.
  * @param doneListener called when the puzzle has been solved.
  * @author Barry Becker
  */
final class TwoPailsViewer(var doneListener: DoneListener)
  extends PuzzleViewer[Pails, PourOperation] with PathNavigator {

  private val renderer: TwoPailsRenderer = new TwoPailsRenderer
  private var path: util.List[PourOperation] = _

  override def getPath: util.List[PourOperation] = path

  override def refresh(pails: Pails, numTries: Long): Unit = {
    board_ = pails
    makeSound()
    status_ = createStatusMessage(numTries)
    simpleRefresh(pails, numTries)
  }

  override def finalRefresh(path: util.List[PourOperation], pails: Pails, numTries: Long, millis: Long): Unit = {
    super.finalRefresh(path, pails, numTries, millis)
    if (path == null)
      JOptionPane.showMessageDialog(this,
        AppContext.getLabel("NO_SOLUTION_FOUND"), AppContext.getLabel("NO_SOLUTION"), JOptionPane.WARNING_MESSAGE)
    else showPath(path, pails)
  }

  override def makeMove(currentStep: Int, undo: Boolean): Unit = {
    board_ = board_.doMove(getPath.get(currentStep), undo)
    repaint()
  }

  /**
    * This renders the current state of the puzzle to the screen.
    */
  override protected def paintComponent(g: Graphics): Unit = {
    super.paintComponent(g)
    if (board_ != null) renderer.render(g, board_, getWidth, getHeight)
  }

  def showPath(thePath: util.List[PourOperation], pails: Pails): Unit = {
    path = thePath
    board_ = pails
    if (doneListener != null) doneListener.done()
  }
}

