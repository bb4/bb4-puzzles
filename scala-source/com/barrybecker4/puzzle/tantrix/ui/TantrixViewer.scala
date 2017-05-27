/*
 * // Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
 */
package com.barrybecker4.puzzle.tantrix.ui

import java.awt._

import com.barrybecker4.puzzle.common.ui.PuzzleViewer
import com.barrybecker4.puzzle.tantrix.model.{TantrixBoard, TilePlacement}
import com.barrybecker4.puzzle.tantrix.ui.rendering.TantrixBoardRenderer

/**
  * Draws the current best solution to the puzzle in a panel.
  * The view in the model-view-controller pattern.
  *
  * @author Barry Becker
  */
final class TantrixViewer() extends PuzzleViewer[TantrixBoard, TilePlacement] {

  private var renderer: TantrixBoardRenderer = new TantrixBoardRenderer

  def getBoard: TantrixBoard = board_

  /**
    * This renders the current state of the PuzzlePanel to the screen.
    * This method is part of the component interface.
    */
  override protected def paintComponent(g: Graphics) {
    super.paintComponent(g)
    renderer.render(g, board_, getWidth, getHeight)
  }

  override def refresh(board: TantrixBoard, numTries: Long) {
    status_ = createStatusMessage(numTries)
    simpleRefresh(board, numTries)
  }

  override def makeSound() {
    val note = Math.min(127, 20 + getBoard.unplacedTiles.size * 12)
    musicMaker_.playNote(note * 20, 20, 640)
  }
}
