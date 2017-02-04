// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model.board

import com.barrybecker4.common.geometry.Location
import com.barrybecker4.puzzle.sudoku.model.ValueConverter

/**
  * The Board describes the physical layout of the puzzle.
  * The number of Cells in the board is n^2 * n^2, but there are n * n big cells.
  *
  * @author Barry Becker
  */
object Board {
  val MAX_SIZE = 9
}

class Board(var n: Int) {
  assert (n > 1 && n < Board.MAX_SIZE)
  private val nn = n * n

  /** all the values in the big cells or rows/cols 1...nn_ */
  private var valuesList = new ValuesList(nn) // nn
  private var cells: Array[Array[Cell]] = _

  // row and col cells for every row and col.
  protected var rowCells: CellArrays = _
  protected var colCells: CellArrays = _

  /** the internal data structures representing the game board. */
  protected var bigCells: BigCellArray = _
  private var numIterations: Int = 0
  reset()

  /** Copy constructor */
  def this(b: Board) {
    this(b.getBaseSize)
    var i: Int = 0
    for (i <- 0 until nn; j <- 0 until nn)
      getCell(i, j).setOriginalValue(b.getCell(i, j).getValue)
  }

  def this (initialData: Array[Array[Int]] ) {
    this(Math.sqrt(initialData.length).toInt)
    assert(initialData.length == nn && initialData(0).length == nn)

    for {
      i <- 0 until nn
      j <- 0 until nn
    } getCell(i, j).setOriginalValue(initialData(i)(j))
  }

  /** return to original state before attempting solution. Non original values become 0. */
  def reset() {
    cells = Array.fill[Cell](nn, nn)(new Cell(0)) //Array.ofDim[Cell](nn, nn)
    bigCells = new BigCellArray(this)
    rowCells = CellArrays.createRowCellArrays (this)
    colCells = CellArrays.createColCellArrays (this)
    numIterations = 0
  }

  def getRowCells: CellArrays = rowCells
  def getColCells: CellArrays = colCells
  def getBigCells: BigCellArray = bigCells

  /** @return retrieve the base size of the board (sqrt(edge magnitude)) */
  def getBaseSize: Int = n


  /** @return retrieve the edge size of the board. */
  def getEdgeLength: Int = nn
  def getNumCells: Int = nn * nn

  /** @return the bigCell at the specified location.*/
  def getBigCell (row: Int, col: Int): BigCell = bigCells.getBigCell(row, col)

  /**
    * @param row 0 - nn-1
    * @param col 0 - nn-1
    * @return the cell in the bigCellArray at the specified location.
    */
  def getCell(row: Int, col: Int): Cell = cells(row)(col)

  def getCell(location: Location): Cell = cells(location.getRow)(location.getCol)

  /**
    * @param position a number between 0 and nn ** 2
    * @return the cell at the specified position.
    */
  def getCell (position: Int): Cell = getCell (position / nn, position % nn)

  /** @return true if the board has been successfully solved. */
  def solved: Boolean = isFilledIn && hasNoCandidates

  /**
    * @return true if all the cells have been filled in with a value (even if not a valid solution).
    */
  private def isFilledIn: Boolean = {
    for (row <- 0 until nn; col <- 0 until nn) {
        val c: Cell = getCell(row, col)
        if (c.getValue <= 0) return false
    }
    true
  }

  private def hasNoCandidates: Boolean = {
    for (row <- 0 until nn; col <- 0 until nn if getCell(row, col).getCandidates.isDefined) return false
    true
  }

  /** @return the complete set of allowable values (1,... nn) */
  def getValuesList: ValuesList = valuesList

  def getNumIterations: Int = numIterations

  def incrementNumIterations() {
    numIterations += 1
  }

  override def equals(other: Any): Boolean = other match {
    case that: Board =>
      (that canEqual this) &&
        nn == that.nn &&
        (cells.deep == that.cells.deep) &&
        n == that.n
    case _ => false
  }

  override def hashCode: Int = {
    var result: Int = n
    result = 31 * result + nn
    result = 31 * result + (if (rowCells != null) rowCells.hashCode else 0)
    result
  }

  override def toString: String = {
    val builder: StringBuilder = new StringBuilder ("\n")
    for (row <- 0 until nn) {
      for (col <- 0 until nn) {
        builder.append (ValueConverter.getSymbol (getCell (row, col).getValue) )
        builder.append (" ")
      }
      builder.append ("\n")
    }
    builder.append ("rowCells=\n").append (rowCells)
    builder.append ("bigCells =\n").append (getBigCells)
    builder.toString
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[Board]
}
