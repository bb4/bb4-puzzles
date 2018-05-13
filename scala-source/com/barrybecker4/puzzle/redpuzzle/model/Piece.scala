// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.model


/**
  * One of the 9 board pieces in the Red Puzzle. Immutable.
  * @author Barry Becker
  */
case class Piece(topNub: Nub, rightNub: Nub, bottomNub: Nub, leftNub: Nub, pieceNumber: Int) {

  require(pieceNumber >= 1 && pieceNumber <= 9, "The piece number is not valid : " + pieceNumber)
  val nubs: Array[Nub] = Array(topNub, rightNub, bottomNub, leftNub)

  override def toString: String = "Piece " + pieceNumber + ":" + nubs.mkString(" ")
}