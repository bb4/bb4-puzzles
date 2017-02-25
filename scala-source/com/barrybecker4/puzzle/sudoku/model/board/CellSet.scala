// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model.board

/**
  * An array of cells in a row, column, or bigCell in the puzzle.
  *
  * @author Barry Becker
  */
trait CellSet {

  def getCell(i: Int): Cell

  def numCells: Int
}
