// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.model


/**
  * One of the 9 board pieces in the Red Puzzle. Immutable.
  * @author Barry Becker
  */
case class Piece(topNub: Nub, rightNub: Nub, bottomNub: Nub, leftNub: Nub, pieceNumber: Int) {

  require(pieceNumber >= 1 && pieceNumber <= 9, s"The piece number is not valid: $pieceNumber")

  /** Nub on the given side in the piece's default (TOP-oriented) layout. */
  def nub(dir: Direction): Nub = dir match {
    case Direction.TOP => topNub
    case Direction.RIGHT => rightNub
    case Direction.BOTTOM => bottomNub
    case Direction.LEFT => leftNub
  }

  def nub(i: Int): Nub =
    if i >= 0 && i < Direction.values.length then nub(Direction.values(i))
    else throw new IllegalArgumentException("Invalid nub index of " + i)

  override def toString: String = s"Piece $pieceNumber: $topNub,  $rightNub $bottomNub, $leftNub"
}
