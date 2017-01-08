// Copyright by Barry G. Becker, 2000-2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.hiq.model

import com.barrybecker4.common.geometry.ByteLocation
import com.barrybecker4.puzzle.common.model.Move

/**
  * Definition for a peg jumping another peg.
  * Create a move object representing a transition on the board.
  * A naive implementation might use 4 four byte integers to store the from and to values.
  * This would use 16 bytes of memory per move.
  * If we do this, we will quickly run out of memory because fo the vast numbers of moves that must be stored.
  * I will use just 1 byte to store the move information.
  * All we need to know is the from position (which can be stored in 6 bits) and the to direction (which can be stored in 2 bits)
  * I know that a jump is always 2 spaces.
  * Immutable.
  * @author Barry Becker
  */
final case class PegMove(fromPosition: ByteLocation, toPosition: ByteLocation) extends Move {

  def this(fromRow: Byte, fromCol: Byte, destinationRow: Byte, destinationCol: Byte) {
    this(new ByteLocation(fromRow, fromCol), new ByteLocation(destinationRow, destinationCol))
  }

  /** @return a deep copy. */
  def copy = PegMove(fromPosition, toPosition)

  def getFromRow: Byte = fromPosition.getRow.toByte
  def getFromCol: Byte = fromPosition.getCol.toByte
  def getToRow: Byte = toPosition.getRow.toByte
  def getToCol: Byte = toPosition.getCol.toByte

  override def toString: String = {
    val s = new StringBuilder
    s.append("from ").append(fromPosition).append(" to ")
    s.append(toPosition)
    s.toString
  }
}

