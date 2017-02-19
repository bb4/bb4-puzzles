/*
 * // Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
 */

package com.barrybecker4.puzzle.sudoku.model.updaters

import com.barrybecker4.puzzle.sudoku.model.board.Board
import com.barrybecker4.puzzle.sudoku.model.update.updaters.BigCellScoutUpdater
import org.scalatest.FunSuite

/**
  * Created by barry on 2/18/2017.
  */
class BigCellScoutUpdaterSuite extends FunSuite {


  test("Simple scout update") {

    val board: Board = new Board(Array(
      Array(0, 4, 0, 0),
      Array(1, 0, 2, 0),
      Array(4, 3, 1, 2),
      Array(2, 0, 0, 0)
    ))
    println(board.toString)

    val updater = new BigCellScoutUpdater(board)
    updater.updateAndSet()

    val expectedBoard = new Board(Array[Array[Int]](
      Array(0, 4, 0, 1),  // 1 added in last column
      Array(1, 0, 2, 0),
      Array(4, 3, 1, 2),
      Array(2, 0, 0, 3))  // 3 added in last column
    )

    assertResult(expectedBoard) { board }
  }

}
