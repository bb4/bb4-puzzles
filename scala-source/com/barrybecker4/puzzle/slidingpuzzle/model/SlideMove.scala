// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.model

import com.barrybecker4.common.geometry.Location
import com.barrybecker4.puzzle.common.model.Move

/**
  * Definition for tile that slides around on the Slider board. Immutable.
  * @author Barry Becker
  */
case class SlideMove(fromPosition: Location, toPosition: Location) extends Move {

//  def getFromRow: Byte = fromPosition.getRow.toByte
//  def getFromCol: Byte = fromPosition.getCol.toByte
//  def getToRow: Byte = toPosition.getRow.toByte
//  def getToCol: Byte = toPosition.getCol.toByte

  override def toString: String = "from " + fromPosition + " to " + toPosition
}

