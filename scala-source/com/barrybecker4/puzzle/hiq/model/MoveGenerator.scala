// Copyright by Barry G. Becker, 2012 - 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.hiq.model

import com.barrybecker4.common.geometry.Location


/**
  * HiQ Puzzle move generator. Generates valid next moves.
  * @author Barry Becker
  */
class MoveGenerator(var board: PegBoard) {

  /** @return List of all valid jumps for the current board state */
  def generateMoves: List[PegMove] = {
    var moves = List[PegMove]()
    val emptyLocations = board.getLocations(false)
    if (emptyLocations.isEmpty)
      moves +:= board.getFirstMove
    else
      for (pos <- emptyLocations) moves ++= findMovesForLocation(pos, undo = false)

    moves
  }

  /**
    * @param location Location empty or peg location based on undo
    * @param undo     boolean find undo (peg) or redo (empty location) moves.
    * @return List of possible peg moves
    */
  private def findMovesForLocation(location: Location, undo: Boolean) = {
    var moves = List[PegMove]()
    val r = location.row.toByte
    val c = location.col.toByte
    // 4 cases to consider: NEWS
    moves = checkMoveForDirection(r, c, 0, -2, undo, moves)
    moves = checkMoveForDirection(r, c, 0, 2, undo, moves)
    moves = checkMoveForDirection(r, c, -2, 0, undo, moves)
    moves = checkMoveForDirection(r, c, 2, 0, undo, moves)
    moves
  }

  private def checkMoveForDirection(r: Byte, c: Byte, rowOffset: Int, colOffset: Int,
                                    undo: Boolean, moves: List[PegMove]): List[PegMove] = {
    val fromRow = (r + rowOffset).toByte
    val fromCol = (c + colOffset).toByte

    if (PegBoard.isValidPosition(fromRow, fromCol) && board.getPosition(fromRow, fromCol) != undo &&
      board.getPosition((r + rowOffset / 2).toByte, (c + colOffset / 2).toByte) != undo)
      new PegMove(fromRow, fromCol, r, c) +: moves
    else moves
  }
}
