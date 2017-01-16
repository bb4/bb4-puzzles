// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.model

import com.barrybecker4.puzzle.redpuzzle.model.Piece.Direction
import com.barrybecker4.puzzle.redpuzzle.model.Piece.Direction.Direction

/**
  * One of the 9 board pieces in the Red Puzzle.
  * Orientation indicates which way the piece is oriented/rotated.
  * Rotation returns a copy.
  *
  * @author Barry Becker
  */
object Piece {
  /** there are exactly 4 sides to every piece. There are no edge or corner pieces in this puzzle. */
  private val NUM_SIDES = 4

  object Direction extends Enumeration {
    type Direction = Value
    val TOP, RIGHT, BOTTOM, LEFT = Value
    val DIRECTIONS: Seq[Direction] = values.toIndexedSeq
  }
}

case class Piece(topNub: Nub, rightNub: Nub, bottomNub: Nub, leftNub: Nub,
                 pieceNumber: Int, orientation: Direction) {

  require(pieceNumber >= 1 && pieceNumber <= 9, "the piece number is not valid : " + pieceNumber)

  // top, right, bottom, left
  private var nubs: Array[Nub] = Array(topNub, rightNub, bottomNub, leftNub)

  /** Assumes default orientation */
  def this (topNub: Nub, rightNub: Nub, bottomNub: Nub, leftNub: Nub, pieceNumber: Int) {
    this (topNub, rightNub, bottomNub, leftNub, pieceNumber, Piece.Direction.TOP)
  }

  /** Copy constructor. */
  def this (piece: Piece) {
    this (piece.nubs(0), piece.nubs(1), piece.nubs(2), piece.nubs(3), piece.pieceNumber, piece.orientation)
  }

  def getTopNub: Nub = getNub(Piece.Direction.TOP)
  def getRightNub: Nub = getNub(Piece.Direction.RIGHT)
  def getBottomNub: Nub = getNub(Piece.Direction.BOTTOM)
  def getLeftNub: Nub = getNub(Piece.Direction.LEFT)

  /**
    * @param dir nub orientation direction.
    * @return the suit of the nub fot the specified direction.
    */
  private def getNub (dir: Direction): Nub = nubs(getDirectionIndex(dir) )

  /** This rotates the piece 90 degrees clockwise. */
  def rotate: Piece = rotate(1)

  /** This rotates the piece the specified number of 90 degree increments. */
  def rotate (num: Int): Piece = {
    val newOrientation: Direction = Direction.DIRECTIONS((orientation.id + num) % Direction.DIRECTIONS.size)
    Piece(nubs(0), nubs(1), nubs(2), nubs(3), pieceNumber, newOrientation)
  }

  /** Sum of (orientation index + requested direction ) modulo the number of Directions (4). */
  private def getDirectionIndex (dir: Direction): Int = (orientation.id + dir.id) % Piece.Direction.values.size

  /** @return a nice readable string representation for debugging. */
  override def toString: String = {
    val buf: StringBuilder = new StringBuilder ("Piece " + pieceNumber + " (orientation=" + orientation + "): ")
    for (d <- Piece.Direction.values) {
      val n: Nub = getNub(d)
      buf.append (d.toString).append (':').append(n.toString).append (";  ")
    }
    buf.toString
  }

  def toRawString: String = "Piece " + pieceNumber + ":" + nubs.mkString(" ")
}
