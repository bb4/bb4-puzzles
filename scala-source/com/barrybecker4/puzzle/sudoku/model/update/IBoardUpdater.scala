// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model.update

import com.barrybecker4.puzzle.sudoku.model.board.Board

/**
  * A strategy for updating the sudoku board while solving it.
  * Update candidate lists for all cells then set the unique values that are determined.
  * Applies all updaters.
  *
  * @author Barry Becker
  */
trait IBoardUpdater {
  def updateAndSet(board: Board)
}
