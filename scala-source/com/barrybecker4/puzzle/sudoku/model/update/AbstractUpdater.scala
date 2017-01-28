// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model.update

import com.barrybecker4.puzzle.sudoku.model.board.Board

/**
  * Responsible for updating the board
  *
  * @author Barry Becker
  */
abstract class AbstractUpdater(var board: Board) extends IUpdater {

  def updateAndSet()
}
