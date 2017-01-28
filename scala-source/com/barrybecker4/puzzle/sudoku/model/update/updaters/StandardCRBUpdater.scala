// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model.update.updaters

import com.barrybecker4.puzzle.sudoku.model.board.Board
import com.barrybecker4.puzzle.sudoku.model.update.AbstractUpdater

/**
  * CRB stands for Column, Row, Big Cell.
  * We scan each for unique values. When we find one we set it permanently in the cell.
  *
  * @author Barry Becker
  */
class StandardCRBUpdater(val b: Board) extends AbstractUpdater(b) {

  /** update candidate lists for all cells then set the unique values that are determined. */
  def updateAndSet() {
    updateCellCandidates()
    checkAndSetUniqueValues()
  }

  private def updateCellCandidates() {
    val values = board.getValuesList
    board.getRowCells.updateAll(values)
    board.getColCells.updateAll(values)
    board.getBigCells.update(values)
  }

  /** Takes the intersection of the three sets: row, col, bigCell candidates. */
  private def checkAndSetUniqueValues() {
    for (row <- 0 until board.getEdgeLength) {
      for (col <- 0 until board.getEdgeLength) {
        val cell = board.getCell(row, col)
        val cands = cell.getCandidates
        if (cands != null && cands.size == 1) {
          val unique = cands.getFirst
          cell.setValue(unique)
        }
      }
    }
  }
}
