package com.barrybecker4.puzzle.sudoku.model

/**
  * Immutable Sudoku cell representation
  * @param originalValue value at initial start state
  * @param proposedValue someones guess at the final value
  */
class Cell(var originalValue: Int, var proposedValue: Int)