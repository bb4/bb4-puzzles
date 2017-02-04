// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model.update.updaters

import com.barrybecker4.puzzle.sudoku.model.board.Candidates


/**
  * An array of sets of integers representing the candidates for the cells in a row or column.
  *
  * @author Barry Becker
  */
class CandidatesArray(var candidates: Array[Candidates]) {

  def get(i: Int): Candidates = candidates(i)

  val size: Int = candidates.length

  override def toString: String = candidates.mkString("\n")
}
