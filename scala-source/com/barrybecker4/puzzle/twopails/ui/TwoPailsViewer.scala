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


/**
  * UI for drawing the current state of the puzzle.
  * @param doneListener called when the puzzle has been solved.
  * @author Barry Becker
  */
final class TwoPailsViewer(var doneListener: DoneListener)
  extends PuzzleViewer[Pails, PourOperation] with PathNavigator {

  private val renderer: TwoPailsRenderer = new TwoPailsRenderer
  private var path: List[PourOperation] = _

  override def getPath: List[PourOperation] = path

  override def refresh(pails: Pails, numTries: Long): Unit = {
    board = pails
    makeSound()
    status = createStatusMessage(numTries)
    simpleRefresh(pails, numTries)
  }

  override def animateTransition(transition: PourOperation): Pails = {
    val newState = board.doMove(transition, undo = false)
    simpleRefresh(newState)
    newState
  }

  override def finalRefresh(path: Option[Seq[PourOperation]], pails: Option[Pails],
                            numTries: Long, millis: Long): Unit = {
    super.finalRefresh(path, pails, numTries, millis)
    if (path == null)
      JOptionPane.showMessageDialog(this,
        AppContext.getLabel("NO_SOLUTION_FOUND"), AppContext.getLabel("NO_SOLUTION"),
        JOptionPane.WARNING_MESSAGE)
    else showPath(path.get.toList, pails.get)
  }

  override def makeMove(currentStep: Int, undo: Boolean): Unit = {
    board = board.doMove(getPath(currentStep), undo)
    repaint()
  }

  /**
    * This renders the current state of the puzzle to the screen.
    */
  override protected def paintComponent(g: Graphics): Unit = {
    super.paintComponent(g)
    if (board != null) renderer.render(g, board, getWidth, getHeight)
  }

  def showPath(thePath: List[PourOperation], pails: Pails): Unit = {
    path = thePath
    board = pails
    if (doneListener != null) doneListener.done()
  }
}

