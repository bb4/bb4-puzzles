// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model.board

/**
  * An array of sets of integers representing the candidates for the cells in a big cell.
  *
  * @author Barry Becker
  */
class BigCellArray(val board: Board) {

  val size: Int = board.baseSize
  /** n by n grid of big cells.   */
  private val bigCells = Array.ofDim[BigCell](size, size)

  for (i <- 0 until size; j <- 0 until size)
    bigCells(i)(j) = new BigCell(board, size * i, size * j)

  def getBigCell(i: Int, j: Int): BigCell = {
    assert(i >= 0 && i < size && j >= 0 && j < size)
    bigCells(i)(j)
  }

  override def toString: String = {
    val a = for (row <- 0 until size; col <- 0 until size) yield s"v($row,$col)=${getBigCell(row, col).toString}"
    a.mkString("\n")
  }
}
