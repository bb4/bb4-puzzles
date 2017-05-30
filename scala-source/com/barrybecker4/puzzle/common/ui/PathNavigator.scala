// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.common.ui

import com.barrybecker4.puzzle.common.model.Move


/**
  * Allows navigating forward and backward in the solution path.
  *
  * @author Barry Becker
  */
trait PathNavigator {

  /** @return the path to navigate on. */
  def getPath: List[_ <: Move]

  /**
    * Switch from the current move in the sequence forwards or backwards stepSize.
    *
    * @param currentStep current position in path
    * @param undo        whether to make the move or undo it. For some puzzles applying the same move a second time undoes it.
    */
  def makeMove(currentStep: Int, undo: Boolean): Unit
}
