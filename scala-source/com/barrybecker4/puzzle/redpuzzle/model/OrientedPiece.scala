// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.model

import com.barrybecker4.puzzle.redpuzzle.model.Direction.Direction


object Direction extends Enumeration {
  type Direction = Value
  val TOP, RIGHT, BOTTOM, LEFT = Value
  val DIRECTIONS: Seq[Direction] = values.toIndexedSeq
}

/**
  * A puzzle piece and its orientation.
  * @author Barry Becker
  */
case class OrientedPiece(piece: Piece, orientation: Direction) {

  def this(piece: Piece) { this(piece, Direction.TOP) }

  /** This rotates the piece 90 degrees clockwise. */
  def rotate: OrientedPiece = rotate(1)

  /** This rotates the piece the specified number of 90 degree increments. */
  def rotate(num: Int): OrientedPiece = {
    val newOrientation: Direction = Direction.DIRECTIONS((orientation.id + num) % Direction.DIRECTIONS.size)
    OrientedPiece(piece, newOrientation)
  }

  /** @return Sum of (orientation index + requested direction ) modulo the number of Directions (4). */
  private def getDirectionIndex (dir: Direction): Int = (orientation.id + dir.id) % Direction.values.size

  def getTopNub: Nub = getNub(Direction.TOP)
  def getRightNub: Nub = getNub(Direction.RIGHT)
  def getBottomNub: Nub = getNub(Direction.BOTTOM)
  def getLeftNub: Nub = getNub(Direction.LEFT)

  /** @param dir nub orientation direction.
    * @return the suit of the nub fot the specified direction.
    */
  private def getNub(dir: Direction): Nub = piece.nubs(getDirectionIndex(dir) )

  /** @return a nice readable string representation for debugging. */
  override def toString: String = {
    val buf: StringBuilder = new StringBuilder ("Piece " + piece.pieceNumber + " (orientation=" + orientation + "): ")
    for (d <- Direction.values) buf.append(d).append (':').append(getNub(d)).append (";")
    buf.toString
  }
}