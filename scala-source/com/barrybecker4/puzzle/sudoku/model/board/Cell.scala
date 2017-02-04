// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model.board

import com.barrybecker4.puzzle.sudoku.model.board.Candidates.NO_CANDIDATES

/**
  * @author Barry Becker
  */
class Cell(value: Int) {
  
  setOriginalValue(value)
  /** must be a number between 1 and nn  */
  private var currentValue: Int = 0

  /** true if part of the original specification.  */
  var isOriginal: Boolean = false

  /** the BigCell to which I belong   */
  private var parentBigCell: BigCell = _
  var rowCells: CellSet = _
  var colCells: CellSet = _
  private var cachedCandidates: Candidates = _

  def setParent(parent: BigCell) {
    parentBigCell = parent
  }

  def getValue: Int = currentValue

  def isParent(bigCell: BigCell): Boolean = bigCell eq parentBigCell

  /**
    * Once the puzzle is started, you can only assign positive values to values of cells.
    *
    * @param value the value to set permanently in the cell (at least until cleared).
    */
  def setValue(value: Int) {
    assert(value > 0)
    currentValue = value
    isOriginal = false
    parentBigCell.removeCandidate(currentValue)
    rowCells.removeCandidate(currentValue)
    colCells.removeCandidate(currentValue)
    clearCache()
  }

  /**
    * Set the value back to unset and add the old value to the list of candidates
    * The value should only be added back to row/col/bigCell candidates if the value is not already set
    * for respective row/col/bigCell.
    * Clear value should be the inverse of setValue.
    */
  def clearValue() {
    if (currentValue == 0) return
    val value = currentValue
    currentValue = 0
    isOriginal = false
    rowCells.addCandidate(value)
    colCells.addCandidate(value)
    parentBigCell.addCandidate(value)
    clearCache()
  }

  /**
    * Once the puzzle is started, you can only assign positive values to values of cells.
    *
    * @param value original value
    */
  def setOriginalValue(value: Int) {
    assert(value >= 0)
    currentValue = value
    // if set to 0 initially, then it is a value that needs to be filled in.
    isOriginal = value > 0
    if (isOriginal) {
      parentBigCell.removeCandidate(value)
      rowCells.removeCandidate(value)
      colCells.removeCandidate(value)
    } else clearCache()
  }

  def remove(value: Int): Unit =
    getCandidates.remove(value)

  def clearCache() { cachedCandidates = null }

  /**
    * Intersect the parent big cell candidates with the row and column candidates.
    * [If after doing the intersection, we have only one value, then set it on the cell. ]
    */
  def getCandidates: Candidates = {
    if (currentValue > 0)
      cachedCandidates = NO_CANDIDATES
    else { //if (cachedCandidates == null) {
      cachedCandidates = parentBigCell.candidates.intersect(rowCells.candidates).intersect(colCells.candidates)
    }
    cachedCandidates
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[Cell]

  override def equals(other: Any): Boolean = other match {
    case that: Cell => (that canEqual this) && currentValue == that.currentValue
    case _ => false
  }

  override def hashCode: Int = value

  override def toString: String = "Cell value=" + getValue
}
