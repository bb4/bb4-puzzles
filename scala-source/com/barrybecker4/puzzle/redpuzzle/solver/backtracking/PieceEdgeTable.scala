// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.solver.backtracking

import com.barrybecker4.puzzle.redpuzzle.model.{Direction, OrientedPiece}

object PieceEdgeTable {
  /** A piece can be turned to face four ways. */
  val ROTATIONS = 4
  /** A piece has four sides. The sides are numbered like `Direction.ordinal`: top, right, bottom, left. */
  val SIDES = 4
  val TOP: Int = Direction.TOP.ordinal
  val RIGHT: Int = Direction.RIGHT.ordinal
  val BOTTOM: Int = Direction.BOTTOM.ordinal
  val LEFT: Int = Direction.LEFT.ordinal
}

/**
  * Every piece in each of its four rotations (a "slot"), with the nub code of every side looked up in an array.
  * Slot `piece * 4 + rotation` is piece number `piece` (its index in the list given) turned `rotation` quarter turns
  * clockwise from the way the list had it.
  * @param pieces the pieces of the puzzle
  * @author Barry Becker
  */
final class PieceEdgeTable(pieces: Seq[OrientedPiece]) {
  import PieceEdgeTable.*

  val numPieces: Int = pieces.size

  private val orientedSlots: Array[OrientedPiece] =
    Array.tabulate(numPieces * ROTATIONS)(slot => pieces(pieceOf(slot)).rotate(slot % ROTATIONS))

  private val edgeCodes: Array[Int] =
    Array.tabulate(orientedSlots.length * SIDES)(i => NubCodes.encode(nubOnSide(orientedSlots(i / SIDES), i % SIDES)))

  def slot(piece: Int, rotation: Int): Int = piece * ROTATIONS + rotation

  def pieceOf(slot: Int): Int = slot / ROTATIONS

  /** @return the nub code on the given side (TOP, RIGHT, BOTTOM or LEFT) of the slot. */
  def edge(slot: Int, side: Int): Int = edgeCodes(slot * SIDES + side)

  def orientedPiece(slot: Int): OrientedPiece = orientedSlots(slot)

  private def nubOnSide(piece: OrientedPiece, side: Int) = side match {
    case TOP => piece.getTopNub
    case RIGHT => piece.getRightNub
    case BOTTOM => piece.getBottomNub
    case _ => piece.getLeftNub
  }
}
