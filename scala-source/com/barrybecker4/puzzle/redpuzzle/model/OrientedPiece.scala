// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.model

import com.barrybecker4.puzzle.redpuzzle.model.Direction.Direction


object Direction extends Enumeration {
  type Direction = Value
  val TOP, RIGHT, BOTTOM, LEFT = Value
  val DIRECTIONS: Seq[Direction] = values.toIndexedSeq
}

/**
  * A puzzle piece and its orientation. Immutable
  * @author Barry Becker
  */
case class OrientedPiece(piece: Piece, orientation: Direction) {

  def this(piece: Piece) = { this(piece, Direction.TOP) }

  /** This rotates the piece the specified number of 90 degree increments. By default roatates 90 degrees clockwise. */
  def rotate(num: Int = 1): OrientedPiece = {
    val newOrientation: Direction = Direction.DIRECTIONS((orientation.id + num) % Direction.maxId /*DIRECTIONS.size*/)
    OrientedPiece(piece, newOrientation)
  }

  def getTopNub: Nub = getNub(Direction.TOP)
  def getRightNub: Nub = getNub(Direction.RIGHT)
  def getBottomNub: Nub = getNub(Direction.BOTTOM)
  def getLeftNub: Nub = getNub(Direction.LEFT)

  /** @param dir nub orientation direction.
    * @return the suit of the nub fot the specified direction.
    */
  private def getNub(dir: Direction): Nub = piece.nub(getDirectionIndex(dir))

  /** @return sum of (orientation index + requested direction ) modulo the number of Directions (4). */
  private def getDirectionIndex(dir: Direction): Int = (orientation.id + dir.id) % Direction.maxId

  /** @return a nice readable string representation for debugging. */
  override def toString: String = {
    val buf: StringBuilder = new StringBuilder ("Piece " + piece.pieceNumber + " (orientation=" + orientation + "): ")
    for (d <- Direction.values) buf.append(d).append (':').append(getNub(d)).append (";")
    buf.toString
  }
}