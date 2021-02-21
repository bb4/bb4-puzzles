// Copyright by Barry G. Becker, 2000-2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.common.ui

import java.awt.Graphics


/**
  * Takes the current state of the puzzle and renders it for the PuzzleViewer.
  * Having the renderer separate from the viewer helps to separate out the rendering logic
  * from other features of the PuzzleViewer.
  *
  * @author Barry Becker
  */
trait PuzzleRenderer[P] {

  /** This renders the current state of the puzzle, P, to the screen. */
  def render(g: Graphics, board: P, width: Int, height: Int): Unit
}


