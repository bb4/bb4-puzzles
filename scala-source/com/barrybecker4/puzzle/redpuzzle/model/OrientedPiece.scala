// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.model


enum Direction:
  case TOP, RIGHT, BOTTOM, LEFT


/**
  * A puzzle piece and its orientation. Immutable
  * @author Barry Becker
  */
case class OrientedPiece(piece: Piece, orientation: Direction = Direction.TOP) {

  /** Rotates the piece the specified number of 90 degree increments. By default rotates 90 degrees clockwise. */
  def rotate(num: Int = 1): OrientedPiece = {
    val newOrientation = Direction.values((orientation.ordinal + num) % Direction.values.length)
    OrientedPiece(piece, newOrientation)
  }

  def getTopNub: Nub = getNub(Direction.TOP)
  def getRightNub: Nub = getNub(Direction.RIGHT)
  def getBottomNub: Nub = getNub(Direction.BOTTOM)
  def getLeftNub: Nub = getNub(Direction.LEFT)

  /** @param dir nub orientation direction in board space.
    * @return the nub for the specified direction.
    */
  private def getNub(dir: Direction): Nub = piece.nub(localDirectionFor(dir))

  /** Piece-local side that corresponds to the given board direction given current orientation. */
  private def localDirectionFor(worldDir: Direction): Direction =
    Direction.values((orientation.ordinal + worldDir.ordinal) % Direction.values.length)

  /** @return a nice readable string representation for debugging. */
  override def toString: String = {
    val buf = new StringBuilder(s"Piece ${piece.pieceNumber} (orientation=$orientation): ")
    for (d <- Direction.values) buf.append(d).append(':').append(getNub(d)).append(';')
    buf.toString
  }
}
