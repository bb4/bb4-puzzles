// Copyright by Barry G. Becker, 20017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.twopails.model

/**
  * Defines the two parameters for the two pail problem.
  * Immutable
  *
  * @author Barry Becker
  */
object PailParams {
  /** the maximum capacity of any pail */
  val MAX_CAPACITY = 99
}

case class PailParams(pail1Size: Int, pail2Size: Int, targetMeasureSize: Int) {

  def getBiggest: Int = Math.max(pail1Size, pail2Size)

  override def toString: String = "pail1=" + pail1Size + " pail2=" + pail2Size + " target=" + targetMeasureSize
}
