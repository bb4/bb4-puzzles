// Copyright by Barry G. Becker, 2000-2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.hiq.model

import PegBoard.NUM_PEG_HOLES

/**
  * Compressed immutable internal bit representation of the pegs on the board
  * @param bits bit representation
  * @param finalBit the last bit not in the integer
  * @param nextToFinalBit bit before the last bit
  * @author Barry Becker
  */
case class PegBits(bits: Int = 0, finalBit: Boolean = false, nextToFinalBit: Boolean = false) {

  /** @return Map the coordinate location into our memory conserving hash.*/
  def getIndexForPosition(row: Int, col: Int): Int = {
    val pos = row * 10 + col
    pos match {
      case p if p > 19 && p < 47 => p % 10 + (p / 10 - 1) * 7 - 1
      case 2 => 0
      case 3 => 1
      case 4 => 2
      case 12 => 3
      case 13 => 4
      case 14 => 5
      case 52 => 27
      case 53 => 28
      case 54 => 29
      case 62 => 30
      case 63 => 31
      case 64 => 32
      case _ => throw new IllegalArgumentException("invalid position: row=" + row + " col=" + col)
    }
  }

  /** @return new pegBits with new value of position in internal compressed data structure. */
  def set(i: Int, value: Boolean): PegBits = {
    var newFinalBit = finalBit
    var newNextToFinalBit = nextToFinalBit
    var newBits = bits
    if (i == NUM_PEG_HOLES - 1) newFinalBit = value
    else if (i == NUM_PEG_HOLES - 2) newNextToFinalBit = value
    else {
      val place = 1 << i
      newBits -= (if (get(i)) place else 0)
      newBits += (if (value) place else 0)
    }
    PegBits(newBits, newFinalBit, newNextToFinalBit)
  }

  /** @return extract the value of the ith bit. */
  def get(i: Int): Boolean = {
    if (i == NUM_PEG_HOLES - 1) return finalBit
    if (i == NUM_PEG_HOLES - 2) return nextToFinalBit
    val place = 1 << i
    (bits & place) != 0
  }

  /** @return number of pegs left on the board. */
  def getNumPegsLeft: Int = {
    var nPegsLeft = 0
    for (i <- 0 until NUM_PEG_HOLES)
      if (get(i)) nPegsLeft += 1
    nPegsLeft
  }

  override def toString: String = {
    val buf = new StringBuilder(Integer.toBinaryString(bits))
    buf.append(if (nextToFinalBit) "1" else "0")
    buf.append(if (finalBit) "1" else "0")
    buf.toString
  }
}