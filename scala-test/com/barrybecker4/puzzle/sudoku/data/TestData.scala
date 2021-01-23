// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.data

import com.barrybecker4.puzzle.sudoku.Data

/**
  * Some sample sudoku test puzzle data.
  *
  * @author Barry Becker
  */
object TestData {
  /** simple test of a 4*4 puzzle */
  val SIMPLE_4 = Array(
    Array(0, 4, 0, 0),
    Array(0, 0, 2, 0),
    Array(4, 3, 0, 0),
    Array(0, 0, 0, 0)
  )

  /** simple test of a 4*4 puzzle solved */
  val SIMPLE_4_SOLVED = Array(
    Array(2, 4, 3, 1),
    Array(3, 1, 2, 4),
    Array(4, 3, 1, 2),
    Array(1, 2, 4, 3)
  )

  /** simple test of a 9*9 puzzle */
  val SIMPLE_9 = Array(
    Array(0, 0, 9, 0, 0, 0, 0, 0, 7),
    Array(2, 8, 6, 4, 7, 3, 0, 0, 0),
    Array(0, 0, 0, 5, 9, 0, 0, 0, 0),
    Array(0, 2, 1, 0, 8, 0, 0, 5, 6),
    Array(4, 0, 0, 0, 0, 0, 0, 0, 1),
    Array(8, 9, 0, 0, 6, 0, 3, 4, 0),
    Array(0, 0, 0, 0, 5, 2, 0, 0, 0),
    Array(0, 0, 0, 3, 1, 6, 7, 8, 4),
    Array(1, 0, 0, 0, 0, 0, 6, 0, 0)
  )

  /** Difficult to solve 9*9 puzzle */
  val DIFFICULT_9 = Data.DIFFICULT_9

  /** simple test of a 9*9 puzzle
    * (inconsistent. use only for testing)
    */
  val INCONSISTENT_9 = Array(
    Array(0, 0, 3, 7, 0, 0, 0, 2, 0),
    Array(0, 8, 0, 9, 0, 0, 4, 0, 1),
    Array(0, 9, 0, 0, 2, 1, 0, 6, 3),
    Array(0, 5, 2, 0, 7, 0, 0, 0, 9),
    Array(0, 0, 6, 1, 0, 9, 7, 0, 0),
    Array(8, 0, 0, 0, 6, 0, 3, 1, 0),
    Array(5, 3, 0, 0, 4, 0, 0, 8, 2),
    Array(9, 0, 7, 0, 0, 3, 0, 5, 0),
    Array(4, 0, 0, 7, 0, 5, 1, 0, 0)
  )

  val INVALID_9 = Array(
    Array(0, 0, 3, 7, 3, 3, 0, 2, 0),
    Array(0, 8, 0, 9, 3, 3, 4, 0, 1),
    Array(0, 9, 0, 0, 2, 1, 0, 6, 3),
    Array(0, 5, 2, 0, 7, 0, 0, 0, 9),
    Array(0, 0, 6, 1, 0, 9, 7, 0, 0),
    Array(8, 0, 0, 0, 6, 0, 3, 1, 0),
    Array(5, 3, 0, 0, 4, 0, 0, 8, 2),
    Array(9, 0, 7, 0, 0, 3, 0, 5, 0),
    Array(4, 0, 0, 7, 0, 5, 1, 0, 0)
  )

  /** Under-constrained
    */
  val UNDER_CONSTRAINED_IMPOSSIBLE_9 = Array(
    Array(0, 0, 0, 0, 0, 5, 0, 0, 0),
    Array(0, 0, 0, 6, 0, 1, 0, 4, 3),
    Array(0, 0, 0, 0, 0, 0, 0, 0, 0),
    Array(0, 0, 0, 5, 0, 0, 0, 0, 0),
    Array(0, 0, 0, 0, 0, 0, 0, 0, 0),
    Array(3, 0, 0, 0, 0, 0, 0, 0, 5),
    Array(5, 3, 0, 0, 0, 0, 0, 6, 0),
    Array(0, 0, 0, 0, 0, 0, 0, 0, 0),
    Array(0, 0, 0, 0, 0, 0, 0, 0, 0))

  /** Underconstrained
    */
  val OVER_CONSTRAINED_IMPOSSIBLE_9 = Array(
    Array(0, 0, 0, 4, 0, 5, 0, 0, 0),
    Array(0, 0, 0, 6, 0, 1, 0, 4, 3),
    Array(0, 4, 0, 0, 0, 0, 0, 0, 0),
    Array(0, 0, 0, 5, 0, 0, 4, 0, 0),
    Array(0, 0, 0, 0, 0, 0, 0, 0, 0),
    Array(3, 0, 0, 0, 0, 0, 0, 0, 5),
    Array(5, 3, 0, 0, 0, 0, 0, 6, 4),
    Array(4, 0, 0, 0, 0, 0, 0, 0, 0),
    Array(0, 0, 4, 0, 0, 0, 0, 0, 0))

  /** From http://norvig.com/sudoku.html
    */
  val NORVIG_IMPOSSIBLE_9 = Data.NORVIG_IMPOSSIBLE_9

  /**
    * hardest of a million samples
    * from https://norvig.com/sudoku.html
    */
  val NORVIG_HARD_9 = Data.NORVIG_HARD_9

  /** Complex 16x16 puzzle from grandma */
  val COMPLEX_16: Array[Array[Int]] = Data.SAMPLE_16
}
