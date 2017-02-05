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
  /** Maximum size of one of the big cells. The whole puzzle should be no more than MAX_SIZE raised to the 4 cells. */
  val MAX_SIZE = 8
}

/**
  * @param baseSize the base size of the board (sqrt(edge magnitude))
  */
class Board(val baseSize: Int) {
  assert (baseSize > 1 && baseSize <= Board.MAX_SIZE)
  /** the edge length of the full board */
  val edgeLength: Int = baseSize * baseSize
  val numCells: Int = edgeLength * edgeLength

  /** all the values in the big cells or rows/cols 1...nn_ */
  val valuesList = new ValuesList(edgeLength) // nn
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
    this(b.baseSize)
    var i: Int = 0
    for (i <- 0 until edgeLength; j <- 0 until edgeLength)
      getCell(i, j).setOriginalValue(b.getCell(i, j).getValue)
  }

  def this (initialData: Array[Array[Int]] ) {
    this(Math.sqrt(initialData.length).toInt)
    assert(initialData.length == edgeLength && initialData(0).length == edgeLength)

    for {
      i <- 0 until edgeLength
      j <- 0 until edgeLength
    } getCell(i, j).setOriginalValue(initialData(i)(j))
  }

  /** return to original state before attempting solution. Non original values become 0. */
  def reset() {
    cells = Array.fill[Cell](edgeLength, edgeLength)(new Cell(0))
    bigCells = new BigCellArray(this)
    rowCells = CellArrays.createRowCellArrays(this)
    colCells = CellArrays.createColCellArrays(this)
    numIterations = 0
  }

  def getRowCells: CellArrays = rowCells
  def getColCells: CellArrays = colCells
  def getBigCells: BigCellArray = bigCells

  /** @return the bigCell at the specified location.*/
  def getBigCell(row: Int, col: Int): BigCell = bigCells.getBigCell(row, col)

  /** @return the cell in the bigCellArray at the specified location. */
  def getCell(row: Int, col: Int): Cell = cells(row)(col)

  def getCell(location: Location): Cell = cells(location.getRow)(location.getCol)

  /**
    * @param position a number between 0 and nn ** 2
    * @return the cell at the specified position.
    */
  def getCell(position: Int): Cell = getCell (position / edgeLength, position % edgeLength)

  /** @return true if the board has been successfully solved. */
  def solved: Boolean = isFilledIn && hasNoCandidates

  /**
    * @return true if all the cells have been filled in with a value (even if not a valid solution).
    */
  private def isFilledIn: Boolean = {
    for (row <- 0 until edgeLength; col <- 0 until edgeLength) {
        val c: Cell = getCell(row, col)
        if (c.getValue <= 0) return false
    }
    true
  }

  private def hasNoCandidates: Boolean = {
    for (row <- 0 until edgeLength; col <- 0 until edgeLength if !getCell(row, col).getCandidates.isEmpty) return false
    true
  }

  def getNumIterations: Int = numIterations

  def incrementNumIterations() {
    numIterations += 1
  }

  override def equals(other: Any): Boolean = other match {
    case that: Board =>
      (that canEqual this) &&
        edgeLength == that.edgeLength &&
        (cells.deep == that.cells.deep) &&
        baseSize == that.baseSize
    case _ => false
  }

  override def hashCode: Int = {
    var result: Int = baseSize
    result = 31 * result + edgeLength
    result = 31 * result + (if (rowCells != null) rowCells.hashCode else 0)
    result
  }

  override def toString: String = {
    val builder: StringBuilder = new StringBuilder ("\n")
    for (row <- 0 until edgeLength) {
      for (col <- 0 until edgeLength) {
        builder.append(ValueConverter.getSymbol (getCell (row, col).getValue) )
        builder.append(" ")
      }
      builder.append("\n")
    }
    builder.append("rowCells=\n").append(getRowCells)
    builder.append("colCells=\n").append(getColCells)
    builder.append("bigCells =\n").append(getBigCells)
    builder.toString
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[Board]
}
