// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.model

/**
  * The 2 ways that a level can be rotates
  */
object Direction extends Enumeration {
  type Direction = Value
  val CLOCKWISE, COUNTER_CLOCKWISE = Value

  val VALUES: Array[Direction] = Array(CLOCKWISE, COUNTER_CLOCKWISE)
}