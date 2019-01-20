// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.ui

import com.barrybecker4.common.geometry.Location

/**
  * Called when the user enters a value.
  * @author Barry Becker
  */
trait RepaintListener {

  def valueEntered()
  def cellSelected(location: Location)
  def requestValidation()
}
