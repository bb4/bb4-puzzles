// Copyright by Barry G. Becker, 2013-2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.common.ui

/**
  * Listens for something to be done.
  * @author Barry Becker
  */
trait DoneListener {
  def done(): Unit
}
