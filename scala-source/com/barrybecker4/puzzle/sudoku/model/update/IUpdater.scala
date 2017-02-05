/*
 * // Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
 */

package com.barrybecker4.puzzle.sudoku.model.update

/**
  * A strategy for updating the sudoku board while solving it.
  *
  * @author Barry Becker
  */
trait IUpdater {

  /**
    * Update candidate lists for all cells then set the unique values that are determined.
    * Next check for loan rangers.
    */
  def updateAndSet()
}
