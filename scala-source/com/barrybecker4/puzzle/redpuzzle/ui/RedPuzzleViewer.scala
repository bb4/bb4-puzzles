// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.ui

import com.barrybecker4.common.concurrency.ThreadUtil
import com.barrybecker4.puzzle.common.ui.PuzzleViewer
import com.barrybecker4.puzzle.redpuzzle.model.{OrientedPiece, Piece, PieceList}
import java.awt.Dimension
import java.awt.Graphics
import java.util

/**
  * Draws the current best solution to the puzzle in a panel.
  * The view in the model-view-controller pattern.
  * @author Barry Becker
  */
object RedPuzzleViewer {
  private[ui] val MAX_ANIM_SPEED = 100
  private[ui] val INITIAL_ANIM_SPEED = 20
}

final class RedPuzzleViewer private[ui]()
  extends PuzzleViewer[PieceList, OrientedPiece] {

  val baseDim: Int = 5 * RedPuzzleRenderer.PIECE_SIZE
  setPreferredSize(new Dimension(baseDim + 200, baseDim + 100))
  private var animationSpeed: Int = RedPuzzleViewer.INITIAL_ANIM_SPEED
  private val renderer: RedPuzzleRenderer = new RedPuzzleRenderer

  /*** @param speed higher the faster up to MAX_ANIM_SPEED.
    */
  private[ui] def setAnimationSpeed(speed: Int) {
    assert(speed > 0 && speed <= RedPuzzleViewer.MAX_ANIM_SPEED)
    animationSpeed = speed
  }

  override def refresh(pieces: PieceList, numTries: Long) {
    status = createStatusMessage(numTries)
    simpleRefresh(pieces, numTries)
    if (animationSpeed < RedPuzzleViewer.MAX_ANIM_SPEED) {
      if (numTries % 5 == 0) makeSound()
      // give it a chance to repaint.
      ThreadUtil.sleep(8 * RedPuzzleViewer.MAX_ANIM_SPEED / animationSpeed)
    }
  }

  override def finalRefresh(path: Option[Seq[OrientedPiece]], pieces: Option[PieceList], numTries: Long, millis: Long) {
    super.finalRefresh(path, pieces, numTries, millis)
    if (animationSpeed < RedPuzzleViewer.MAX_ANIM_SPEED - 1) ThreadUtil.sleep(10 * RedPuzzleViewer.MAX_ANIM_SPEED / animationSpeed)
    else ThreadUtil.sleep(20)
  }

  /** make a little click noise when the piece fits into place. */
  override def makeSound() {
    musicMaker.playNote(60, 20, 940)
  }

  /** This renders the current state of the PuzzlePanel to the screen.
    * This method is part of the component interface.
    */
  override protected def paintComponent(g: Graphics) {
    super.paintComponent(g)
    renderer.render(g, board, this.getWidth, this.getHeight)
  }
}
