// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model.board

/**
  * An array of cells for a row or column in the puzzle.
  *
  * @author Barry Becker
  */
object CellArray {

  def createRowCellArrays(board: Board): Seq[CellArray] = createCellArrays(board, CellArray.createRowCellArray)
  def createColCellArrays(board: Board): Seq[CellArray] = createCellArrays(board, CellArray.createColCellArray)

  private def createCellArrays(board: Board, cellArrayCreator: (Int, Board) => CellArray) =
    for (i <- 0 until board.edgeLength) yield cellArrayCreator(i, board)

  private def createRowCellArray(row: Int, board: Board) =
    createCellArray(board, row, (row, i, cells) => {
      val cell = board.getCell(row, i)
      cell.rowCells = cells
      cell
    })

  private def createColCellArray(col: Int, board: Board) =
    createCellArray(board, col, (col, i, cells) => {
      val cell = board.getCell(i, col)
      cell.colCells = cells
      cell
    })

  private def createCellArray(board: Board, rowOrCol: Int, extractor: (Int, Int, CellArray) => Cell) = {
    val cells = new CellArray(board.edgeLength)
    for (i <- 0 until board.edgeLength) {
      val cell = extractor(rowOrCol, i, cells)
      cells.cells(i) = cell
    }
    cells
  }
}

/**
  * @param size this size of the row (small grid dim squared).
  */
class CellArray private(val size: Int) extends CellSet {

  /** candidate sets for a row or col.   */
  private val cells: Array[Cell] = new Array[Cell](size)

  def getCell(i: Int): Cell = cells(i)

  def removeCandidate(unique: Int) {
    //cells.foreach(_.removeCandidate(unique))
  }

  def numCells: Int = cells.length

  override def toString: String = "CellArray cells:" + cells.mkString(", ")
}
