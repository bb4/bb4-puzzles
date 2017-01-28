// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model.update

import com.barrybecker4.puzzle.sudoku.model.update.updaters.BigCellScoutUpdater
import com.barrybecker4.puzzle.sudoku.model.update.updaters.LoneRangerUpdater
import com.barrybecker4.puzzle.sudoku.model.update.updaters.NakedSubsetUpdater
import com.barrybecker4.puzzle.sudoku.model.update.updaters.StandardCRBUpdater

/**
  * Default board updater applies all the standard updaters.
  *
  * @author Barry Becker
  */
object DefaultBoardUpdater {
  private var UPDATERS = List[Class[_ <: AbstractUpdater]](
      classOf[StandardCRBUpdater],
      classOf[LoneRangerUpdater],
      classOf[BigCellScoutUpdater],
      classOf[NakedSubsetUpdater])
}

class DefaultBoardUpdater() extends ReflectiveBoardUpdater(DefaultBoardUpdater.UPDATERS)



