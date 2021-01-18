package com.barrybecker4.puzzle.sudoku.model

/**
  * Immutable Sudoku cell representation
  * @param originalValue value at initial start state
  * @param proposedValue someones guess at the final value
  * @param candidateValues those values that could fit here without contradiction
  */
case class Cell(originalValue: Int, proposedValue: Int, candidateValues: Set[Int]) {

  def setValue(value: Int): Cell = {
    Cell(value, value, Set(value))
  }

  def setCandidateValues(newCandidates: Set[Int]): Cell = {
    Cell(originalValue, proposedValue, newCandidates)
  }
}