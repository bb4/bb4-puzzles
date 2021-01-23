package com.barrybecker4.puzzle.sudoku.model

/**
  * Immutable Sudoku cell representation
  * @param originalValue value at initial start state. If non-0, it is guaranteed to be correct.
  * @param proposedValue someones guess at the final value
  */
case class Cell(originalValue: Int, proposedValue: Int) {

  def setValue(value: Int): Cell = Cell(value, value)

}