// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku

/**
  * Some sample Sudoku test puzzle data.
  * Perhaps make these into separate date files and move to test.
  * @author Barry Becker
  */
object Data {

  /** simple test of a 4*4 puzzle */
  val SIMPLE_4 = Array(
    Array(0, 4, 0, 0),
    Array(0, 0, 2, 0),
    Array(4, 3, 0, 0),
    Array(0, 0, 0, 0))

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
    Array(1, 0, 0, 0, 0, 0, 6, 0, 0))

  /**
    * PeterNorvig's hardest from https://norvig.com/sudoku.html
    */
  val NORVIG_HARD_9 = Array(
    Array(0, 0, 0, 0, 0, 6, 0, 0, 0),
    Array(0, 5, 9, 0, 0, 0, 0, 0, 8),
    Array(2, 0, 0, 0, 0, 8, 0, 0, 0),
    Array(0, 4, 5, 0, 0, 0, 0, 0, 0),
    Array(0, 0, 3, 0, 0, 0, 0, 0, 0),
    Array(0, 0, 6, 0, 0, 3, 0, 5, 4),
    Array(0, 0, 0, 3, 2, 5, 0, 0, 6),
    Array(0, 0, 0, 0, 0, 0, 0, 0, 0),
    Array(0, 0, 0, 0, 0, 0, 0, 0, 0)
  )

  val DIFFICULT_9 = Array(
    Array(0, 0, 5, 3, 0, 0, 0, 0, 0),
    Array(8, 0, 0, 0, 0, 0, 0, 2, 0),
    Array(0, 7, 0, 0, 1, 0, 5, 0, 0),
    Array(4, 0, 0, 0, 0, 5, 3, 0, 0),
    Array(0, 1, 0, 0, 7, 0, 0, 0, 6),
    Array(0, 0, 3, 2, 0, 0, 0, 8, 0),
    Array(0, 6, 0, 5, 0, 0, 0, 0, 9),
    Array(0, 0, 4, 0, 0, 0, 0, 3, 0),
    Array(0, 0, 0, 0, 0, 9, 7, 0, 0)
  )


  /** From http://norvig.com/sudoku.html
    */
  val NORVIG_IMPOSSIBLE_9 = Array(
    Array(0, 0, 0, 0, 0, 5, 0, 8, 0),
    Array(0, 0, 0, 6, 0, 1, 0, 4, 3),
    Array(0, 0, 0, 0, 0, 0, 0, 0, 0),
    Array(0, 1, 0, 5, 0, 0, 0, 0, 0),
    Array(0, 0, 0, 1, 0, 6, 0, 0, 0),
    Array(3, 0, 0, 0, 0, 0, 0, 0, 5),
    Array(5, 3, 0, 0, 0, 0, 0, 6, 1),
    Array(0, 0, 0, 0, 0, 0, 0, 0, 4),
    Array(0, 0, 0, 0, 0, 0, 0, 0, 0))


  val SAMPLE_16 = Array(
    Array(0, 13, 16, 14, 10, 4, 0, 0, 0, 0, 0, 0, 0, 15, 2, 7),
    Array(0, 0, 4, 11, 0, 0, 0, 0, 8, 0, 0, 2, 0, 0, 0, 3),
    Array(0, 0, 9, 0, 16, 0, 3, 0, 0, 0, 0, 0, 14, 0, 0, 6),
    Array(6, 0, 0, 0, 0, 0, 0, 11, 0, 3, 13, 0, 12, 0, 0, 5),
    Array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0, 8, 0, 0, 0),
    Array(0, 5, 0, 10, 0, 7, 8, 6, 0, 0, 0, 14, 0, 0, 16, 0),
    Array(0, 7, 0, 0, 0, 10, 16, 12, 13, 0, 2, 0, 1, 5, 0, 14),
    Array(0, 4, 0, 12, 5, 0, 11, 9, 0, 0, 0, 6, 10, 0, 3, 0),
    Array(0, 12, 0, 0, 0, 0, 0, 4, 0, 0, 0, 8, 5, 0, 15, 10),
    Array(0, 0, 11, 13, 7, 9, 5, 10, 0, 0, 0, 4, 16, 0, 0, 0),
    Array(14, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 12),
    Array(0, 0, 0, 16, 0, 0, 2, 0, 0, 0, 1, 0, 0, 6, 14, 0),
    Array(9, 0, 13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0),
    Array(0, 0, 7, 4, 2, 13, 6, 16, 0, 0, 0, 0, 0, 0, 0, 11),
    Array(0, 3, 0, 0, 0, 14, 0, 0, 0, 5, 0, 0, 6, 0, 0, 4),
    Array(2, 0, 0, 6, 11, 0, 0, 0, 12, 0, 0, 0, 9, 0, 7, 16))
}
