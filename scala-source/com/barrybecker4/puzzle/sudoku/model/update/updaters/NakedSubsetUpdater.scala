// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model.update.updaters

import com.barrybecker4.puzzle.sudoku.model.board.{Board, Candidates, CellSet}
import com.barrybecker4.puzzle.sudoku.model.update.AbstractUpdater

import scala.collection.immutable.HashSet

/**
  * The idea with this updater is that we want to find a set of n cells that contain only a subset of n digits.
  * If we can do that, then all other cells in that row/col/bigCell can have that set of n digits removed as
  * candidates. This is a generalization of the twins and triplets solvers described in the Sudoku Programming book.
  *
  * Grandma wrote:
  * Would the twins be easier to think about if you look for n cells containing some subset of the same n digits?
  * Is that clear?  Here n would be 2, ..., N-2.  See two cells with BD in the 10th row of the
  * last grid you sent me. See also the lower left BigCell where 6 cells contain some subset of {1,5,8,0,D,E}.
  * Then this would mean that the two cells in that mini grid can't contain any of these 6 digits.
  *
  * @author Barry Becker
  */
class NakedSubsetUpdater(val b: Board) extends AbstractUpdater(b) {

  /**
    * We will only check for one naked subset in each row/col/bigCell since its rare to get even one,
    * and if there is a second we can always get it on the next iteration.
    *
    * for each cell in a row/col/miniGrid {
    * n = numDigits in cell;
    * if we can find n-1 other cells with only a subset of those n digits and none others {
    * remove those n digits from the candidate lists of all the other cells in that row/col/miniGrid
    * }
    * }
    */
  def updateAndSet() {
    checkNakedSubsetInRows()
    checkNakedSubsetInCols()
    checkNakedSubsetInBigCells()
  }

  private def checkNakedSubsetInRows() {
    for (i <- 0 until board.getEdgeLength) {
      val row = board.getRowCells.get(i)
      checkNakedSubset(row)
    }
  }

  private def checkNakedSubsetInCols() {
    for (i <- 0 until board.getEdgeLength) {
        val col = board.getColCells.get(i)
        checkNakedSubset(col)
    }
  }

  private def checkNakedSubsetInBigCells() {
    for (i <- 0 until board.getBaseSize) {
      for (j <- 0 until board.getBaseSize) {
         checkNakedSubset(board.getBigCell(i, j))
      }
    }
  }

  private def checkNakedSubset(cells: CellSet) {
    var foundSubset: Option[Candidates] = None
    var matches: HashSet[Int] = HashSet[Int]()
    for (i <- 0 until cells.numCells) {
      val cands = cells.getCell(i).getCandidates
      matches = HashSet[Int]()
      matches += i
      if (cands != null) {
        val n = cands.size
        for (j <- 0 until cells.numCells) {
          val cands2 = cells.getCell(j).getCandidates
          if (j != i && cands2 != null && cands.containsAll(cands2)) matches += j
        }
        if (matches.size == n) {
          foundSubset = Some(cands)
          //  break //todo: break is not supported
        }
      }
    }
    if (foundSubset.isDefined) {
      for (i <- 0 until cells.numCells) {
        val cell = cells.getCell(i)
        if (!matches.contains(i) && cell.getCandidates != null) cell.getCandidates.removeAll(foundSubset.get)
      }
    }
  }
}
