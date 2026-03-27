// Copyright by Barry G. Becker, 2021 Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.ui

import scala.compiletime.uninitialized
import com.barrybecker4.puzzle.common.ui.{DoneListener, PathNavigator, PuzzleViewer}
import com.barrybecker4.puzzle.rubixcube.model.{Cube, CubeMove}
import com.barrybecker4.puzzle.rubixcube.ui.render.CubeCanvasContainer

import java.awt.{BorderLayout, Dimension, Graphics}
import scala.collection.immutable.Queue


/**
  * UI for drawing the current best solution to the puzzle.
  * @param doneListener called when the puzzle has been solved.
  */
final class CubeViewer(var doneListener: DoneListener)
      extends PuzzleViewer[Cube, CubeMove] with PathNavigator {

  setLayout(new BorderLayout())
  // JME uses a heavyweight AWT Canvas; disable double-buffering so Swing does not fight the GL surface.
  setDoubleBuffered(false)

  private val canvasContainer: CubeCanvasContainer = new CubeCanvasContainer()
  private var animatingQueue: Queue[CubeMove] = Queue()
  private var isAnimating = false
  private var path: List[CubeMove] = uninitialized

  def getPath: List[CubeMove] = path
  private val self = this

  // RubixCubePuzzle wraps GUIUtil.showApplet in invokeLater so this constructor runs on the EDT.
  // Adding the canvas here (synchronously) ensures it is in the hierarchy before setVisible(true).
  // BorderLayout is required — FlowLayout gives AWT Canvas ~0 preferred size.
  add(canvasContainer.canvas, BorderLayout.CENTER)
  setMinimumSize(new Dimension(400, 400))

  override def refresh(theCube: Cube, numTries: Long): Unit = {
    if (board != null && theCube.size != board.size) {
      this.showPath(List(), theCube)
    }
    board = theCube

    if (numTries % 500 == 0) {
      makeSound()
      status = createStatusMessage(numTries)
      simpleRefresh(board, numTries)
    }
  }

  /** the request is ignored if we are already animating */
  override def animateTransition(transition: CubeMove): Cube = this.synchronized {

    if (isAnimating) {
      animatingQueue = animatingQueue.enqueue(transition)
      return board
    }

    doNextAnimationTransition(transition)
  }

  private def doNextAnimationTransition(transition: CubeMove): Cube = {
    isAnimating = true
    val newCubeState = board.doMove(transition)
    canvasContainer.rotateSlice(transition, () => {
      simpleRefresh(newCubeState)
      if (animatingQueue.nonEmpty) {
        val (nextTrans, q) = animatingQueue.dequeue
        animatingQueue = q
        doNextAnimationTransition(nextTrans)
      }
      else {
        isAnimating = false
      }
    })

    newCubeState
  }

  override def finalRefresh(path: Option[Seq[CubeMove]], board: Option[Cube],
                            numTries: Long, millis: Long): Unit = {
    super.finalRefresh(path, board, numTries, millis)
    if (board.isDefined) showPath(path.get.toList, board.get)
  }

  def makeMove(currentStep: Int, undo: Boolean): Unit = {
    val pathMove: CubeMove = getPath(currentStep)
    val move = if (undo) pathMove.reverse else pathMove
    animateTransition(move)
  }

  /** This renders the current state of the puzzle to the screen. */
  override protected def paintComponent(g: Graphics): Unit = {

    if (board != null && canvasContainer != null) {
      canvasContainer.render(board, getWidth, getHeight)
    }
  }

  private def showPath(thePath: List[CubeMove], theBoard: Cube): Unit = {
    path = thePath
    board = theBoard
    if (doneListener != null) doneListener.done()
  }
}

