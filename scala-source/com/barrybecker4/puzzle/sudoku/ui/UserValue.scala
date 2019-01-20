// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.ui

/**
  * A user entered value and it cell location.
  * @author Barry Becker
  */
class UserValue(var value: Int) {

  var isValid = false
  var isValidated = false

  def getValue: Int = value

  def setValid(valid: Boolean) {
    isValid = valid
    isValidated = true
  }
}
