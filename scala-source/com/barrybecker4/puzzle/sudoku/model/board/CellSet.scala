// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model.board

/**
  * An array of cells in a row, column, or bigCell in the puzzle.
  *
  * @author Barry Becker
  */
trait CellSet {

  def getCell(i: Int): Cell

  def candidates: Candidates

  def removeCandidate(unique: Int)

  def addCandidate(value: Int)

  def numCells: Int

  /**
    * Assume all of them, then remove the values that are represented.
    */
  def updateCandidates(values: ValuesList)
}
