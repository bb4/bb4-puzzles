// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.model


/**
  * One of the 9 board pieces in the Red Puzzle. Immutable.
  * @author Barry Becker
  */
case class Piece(topNub: Nub, rightNub: Nub, bottomNub: Nub, leftNub: Nub, pieceNumber: Int) {

  require(pieceNumber >= 1 && pieceNumber <= 9, "The piece number is not valid : " + pieceNumber)

  def nub(i: Int): Nub = {
    i match {
      case 0 => topNub
      case 1 => rightNub
      case 2 => bottomNub
      case 3 => leftNub
      case _ => throw new IllegalArgumentException("Invalid nub index of " + i)
    }
  }

  override def toString: String = s"Piece $pieceNumber: $topNub,  $rightNub $bottomNub, $leftNub"
}