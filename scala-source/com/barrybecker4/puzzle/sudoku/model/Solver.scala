// Copyright by Barry G. Becker, 2017 - 2019. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model

/** Solves a Sudoku Board configuration */
private case class Solver(board: Board, refresh: Option[Board => Unit] = None) {

  private var numIterations = 0

  /** @return the solved board, or None, if could not be solved */
  def solve(): Option[Board] = {
    val updatedBoard = board.updateFromInitialData()
    if (updatedBoard.isDefined) {
      searchForSolution(Some(board.valuesMap)) match {
        case Some(vals) =>
          return Some(board.setValuesMap(vals))
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
        // Chose the unfilled square, s, with the fewest possibilities
        val minSq: Location = (for (s <- board.comps.squares; if vals(s).size > 1)
          yield (vals(s).size, s)).min._2
        for (d <- vals(minSq)) {
          numIterations += 1
          //if (values.isDefined)
          //    refresh.foreach(f => f(board))
          val result = searchForSolution(board.assign(vals, minSq, d))
          if (result.nonEmpty) return result
        }
        None
    }
  }

}
