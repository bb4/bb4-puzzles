// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model.board

/**
  * All the arrays of cells for all the rows or columns in the puzzle.
  *
  * @author Barry Becker
  */
object CellArrays {
  def createRowCellArrays(board: Board): CellArrays = {
    val cellArrays = new CellArrays(board.getEdgeLength)
    for (i <- 0 until board.getEdgeLength) {
        cellArrays.cellArrays(i) = CellArray.createRowCellArray(i, board)
    }
    cellArrays
  }

  def createColCellArrays(board: Board): CellArrays = {
    val cellArrays = new CellArrays(board.getEdgeLength)
    for (i <- 0 until board.getEdgeLength) {
        cellArrays.cellArrays(i) = CellArray.createColCellArray(i, board)
    }
    cellArrays
  }
}

class CellArrays private(var size: Int) {

  /** candidate sets for a row or col.   */
  private var cellArrays: Array[CellArray] = new Array[CellArray](n)

  def get(i: Int): CellArray = cellArrays(i)

  def updateAll(values: ValuesList) {
    for (entry <- 0 until size) {
        cellArrays(entry).updateCandidates(values)
    }
  }

  override def toString: String = cellArrays.mkString(", ")
}
