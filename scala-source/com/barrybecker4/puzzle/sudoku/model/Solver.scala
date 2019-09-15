// Copyright by Barry G. Becker, 2017 - 2019. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model

/** Solves a Sudoku Board configuration */
private case class Solver(board: Board, refresh: Option[() => Unit] = None) {

  private var numIterations = 0

  /** @return then number of iterations it took to solve, or None, if could not be solved */
  def solve(): Option[Int] = {
    if (board.updateFromInitialData()) {
      searchForSolution(Some(board.valuesMap)) match {
        case Some(vals) =>
          board.valuesMap = vals
          return Some(numIterations)
        case None => return None
      }
    }
    None
  }

  def getNumIterations: Int = numIterations

  private def searchForSolution(values: Option[ValueMap]): Option[ValueMap] = {
    values match {
      case None => None
      case Some(vals) =>
        if (vals.values.forall(_.size == 1)) return Some(vals)
        // Chose the unfilled square s with the fewest possibilities
        val minSq: (Int, Int) = (for (s <- board.comps.squares; if vals(s).size > 1)
          yield (vals(s).size, s)).min._2
        for (d <- vals(minSq)) {
          numIterations += 1
          board.doRefresh(refresh)
          val result = searchForSolution(board.assign(vals, minSq, d))
          if (result.nonEmpty) return result
        }
        None
    }
  }

}
