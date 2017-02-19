// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model.update.updaters

import com.barrybecker4.puzzle.sudoku.model.board.{BigCell, Board, Candidates, Cell}
import com.barrybecker4.puzzle.sudoku.model.update.AbstractUpdater


/**
  * Lone rangers are cells that have a candidate (among others) that is unique when
  * compared with the other candidates in other cells in that row, column, or bigCell.
  *
  * For example, consider these candidates for cells in a row (or column, or bigCell).
  * 23 278 13 28 238 23
  * Then the second cell has 7 as a lone ranger.
  *
  * @author Barry Becker
  */
class LoneRangerUpdater(val b: Board) extends AbstractUpdater(b) {

  def updateAndSet() { checkForLoneRangers() }

  /** Look for lone rangers in row, col, and bigCell */
  private def checkForLoneRangers() {
    val n = board.baseSize
    for (row <- 0 until board.edgeLength; col <- 0 until board.edgeLength) {
      val cell = board.getCell(row, col)
      val bigCell = board.getBigCell(row / n, col / n)
      val bigCellCands = getCandidatesArrayExcluding(bigCell, row % n, col % n)
      val rowCellCands = getCandidatesArrayForRowExcludingCol(row, col)
      val colCellCands = getCandidatesArrayForColExcludingRow(row, col)
      checkAndSetLoneRangers(bigCellCands, cell)
      checkAndSetLoneRangers(rowCellCands, cell)
      checkAndSetLoneRangers(colCellCands, cell)
    }
  }

  /** @return all the candidate lists for all the cells in the bigCell except the one specified. */
  private def getCandidatesArrayExcluding(bigCell: BigCell, row: Int, col: Int) = {
    var cands = List[Candidates]()
    val n = bigCell.getSize
    for (i <- 0 until n) {
      for (j <- 0 until n) {
        val c = bigCell.getCell(i, j).getCandidates
        if ((i != row || j != col) && !c.isEmpty)
          cands +:= c
      }
    }
    new CandidatesArray(cands.toArray)
  }

  private def getCandidatesArrayForRowExcludingCol(row: Int, col: Int) = {
    var cands = List[Candidates]()
    for (i <- 0 until board.edgeLength) {
      val c = board.getCell(row, i).getCandidates
      if ((i != col) && !c.isEmpty)
        cands +:= c
    }
    new CandidatesArray(cands.toArray)
  }

  private def getCandidatesArrayForColExcludingRow(row: Int, col: Int) = {
    var cands = List[Candidates]()
    for (i <- 0 until board.edgeLength) {
        val c = board.getCell(i, col).getCandidates
        if ((i != row) && !c.isEmpty)
          cands +:= c
    }
    new CandidatesArray(cands.toArray)
  }

  private def checkAndSetLoneRangers(candArray: CandidatesArray, cell: Cell) {
    if (cell.getCandidates.isEmpty) return
    val candsCopy = cell.getCandidates.copy
    var i = 0
    while (i < candArray.size && !candsCopy.isEmpty) {
      val c = candArray.get(i)
      i += 1
      if (c != null) candsCopy.removeAll(c)
    }
    if (candsCopy.size == 1) {
      val unique = candsCopy.getFirst
      //println("LoneRanger set " + unique)
      cell.setValue(unique)
    }
  }
}
