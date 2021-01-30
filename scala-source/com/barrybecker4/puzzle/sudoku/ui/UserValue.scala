// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.ui

/**
  * A user entered value and it cell location. Immutable
  * @author Barry Becker
  */
case class UserValue(value: Int, isValid: Boolean = false, isValidated: Boolean = false) {

  def setValue(v: Int): UserValue = UserValue(v, isValid, isValidated)

  def setValid(valid: Boolean): UserValue = UserValue(value, valid, isValidated = true)

}
