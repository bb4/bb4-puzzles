// Copyright by Barry G. Becker, 2021 Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.ui

import com.barrybecker4.puzzle.common.ui.{DoneListener, PathNavigator, PuzzleViewer}
import com.barrybecker4.puzzle.rubixcube.model.{Cube, CubeMove}
import com.barrybecker4.puzzle.rubixcube.ui.render.CubeCanvasContainer
import com.barrybecker4.puzzle.rubixcube.ui.util.CubeMoveTransition
import com.jme3.math.ColorRGBA

import java.awt.{BorderLayout, Canvas, Graphics}
import CubeViewer.BACKGROUND
import javax.swing.SwingUtilities


object CubeViewer {
  private val BACKGROUND: ColorRGBA = new ColorRGBA(0.9f, 0.9f, 1.0f, 1.0f)
}

/**
  * UI for drawing the current best solution to the puzzle.
  * @param doneListener called when the puzzle has been solved.
  */
final class CubeViewer(var doneListener: DoneListener)
      extends PuzzleViewer[Cube, CubeMove] with PathNavigator {

  private var canvasContainer: CubeCanvasContainer = _
  private var path: List[CubeMove] = _
  private var transition: Option[CubeMoveTransition] = None

  def getPath: List[CubeMove] = path
  private val self = this

  // Need to add after initialization, or it may show in slightly wrong position
  SwingUtilities.invokeLater(new Runnable() {
    override def run(): Unit = {
      canvasContainer = new CubeCanvasContainer()
      self.add(canvasContainer.canvas, BorderLayout.CENTER)
    }
  })


  override def refresh(theCube: Cube, numTries: Long): Unit = {
    board = theCube
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
    val move: CubeMove = getPath(currentStep)
    animateMove(move, undo)
    board = board.doMove(move)
    repaint()
  }

  def animateMove(move: CubeMove, undo: Boolean): Unit = {
  }

  /** This renders the current state of the puzzle to the screen. */
  override protected def paintComponent(g: Graphics): Unit = {

    if (board != null && canvasContainer != null) {
      canvasContainer.render(board, getWidth, getHeight)
    }
    /*
    super.paintComponent(g)
    if (board != null) renderer.render(g, board, getWidth, getHeight, transition)
     */
  }

  private def showPath(thePath: List[CubeMove], theBoard: Cube): Unit = {
    path = thePath
    board = theBoard
    if (doneListener != null) doneListener.done()
  }
}
