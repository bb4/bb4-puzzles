/*
 * // Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
 */
package com.barrybecker4.puzzle.sudoku.model.update

import com.barrybecker4.puzzle.sudoku.model.board.Board
import com.barrybecker4.puzzle.sudoku.model.update.updaters.{BigCellScoutUpdater, LoneRangerUpdater, NakedSubsetUpdater, StandardCRBUpdater}

/**
  * Default board updater applies all the standard updaters.
  * Must use this version from an applet (or webstart) to avoid Security exception :(
  * We can use reflection in applet/webstart only if we sign the applet/jars.
  *
  * @author Barry Becker
  */
class NonReflectiveBoardUpdater() extends IBoardUpdater {
  /**
    * Update candidate lists for all cells then set the unique values that are determined.
    * First create the updaters using reflection, then apply them.
    */
  def updateAndSet(board: Board) {
    val updaters = createUpdaters(board)
    for (updater <- updaters) {
      updater.updateAndSet()
    }
  }

  private def createUpdaters(board: Board) = List[IUpdater](
    new StandardCRBUpdater(board),
    new LoneRangerUpdater(board),
    new BigCellScoutUpdater(board),
    new NakedSubsetUpdater(board)
  )
}
