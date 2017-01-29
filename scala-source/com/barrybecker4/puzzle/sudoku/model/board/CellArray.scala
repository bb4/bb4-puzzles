// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model.board

/**
  * An array of cells for a row or column in the puzzle.
  *
  * @author Barry Becker
  */
object CellArray {
  private[board] def createRowCellArray(row: Int, board: Board) = {
    val cells = new CellArray(board.getEdgeLength)
    cells.candidates.addAll(board.getValuesList)
    for (i <- 0 until board.getEdgeLength) {
        val cell = board.getCell(row, i)
        cell.rowCells = cells
        cells.cells(i) = cell
        if (cell.getValue > 0) cells.removeCandidate(cell.getValue)
    }
    cells
  }

  private[board] def createColCellArray(col: Int, board: Board) = {
    val cells = new CellArray(board.getEdgeLength)
    cells.candidates.addAll(board.getValuesList)
    for (i <- 0 until board.getEdgeLength) {
        val cell = board.getCell(i, col)
        cell.colCells = cells
        cells.cells(i) = cell
        if (cell.getValue > 0) cells.removeCandidate(cell.getValue)
    }
    cells
  }
}

/**
  * @param size this size of the row (small grid dim squared).
  */
class CellArray private(val size: Int) extends CellSet {

  /** the candidates for the cells in this row or column */
  val candidates: Candidates = new Candidates

  /** candidate sets for a row or col.   */
  private val cells: Array[Cell] = new Array[Cell](size)

  def getCell(i: Int): Cell = cells(i)

  //def getCandidates: Candidates = candidates

  def removeCandidate(unique: Int) {
    candidates.remove(unique)
    var i = 0
    for (i <- 0 until numCells) {
        cells(i).remove(unique)
    }
  }

  /**
    * We can only add the value if none of our cells already have it set.
    *
    * @param value value to add to cells candidate list and that of rows/cols/bigCell if possible.
    */
  def addCandidate(value: Int) {
    candidates.add(value)
    clearCaches()
  }

  def numCells: Int = cells.length

  /** Assume all of them, then remove the values that are represented. */
  def updateCandidates(values: ValuesList) {
    candidates.clear()
    candidates.addAll(values)
    for (i <- 0 until numCells) {
      val v = cells(i).getValue
      if (v > 0) candidates.remove(v)
    }
  }

  private def clearCaches() {
    for (i <- 0 until numCells) {
        cells(i).clearCache()
    }
  }

  override def toString: String = "CellArray cells:" + cells.mkString(", ") + "    cands=" + candidates + "\n"
}
