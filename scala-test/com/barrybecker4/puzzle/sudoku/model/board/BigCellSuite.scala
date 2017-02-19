/*
 * // Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
 */

package com.barrybecker4.puzzle.sudoku.model.board

import com.barrybecker4.puzzle.sudoku.data.TestData
import org.scalatest.{BeforeAndAfter, FunSuite}

/**
  * Created by barry on 2/18/2017.
  */
class BigCellSuite extends FunSuite with BeforeAndAfter {

  private val board: Board = new Board(TestData.SIMPLE_4)

  test("Serialize BigCell, top left corner") {
    val bcell = new BigCell(board, 0, 0)

    assertResult("1,2,3"){ bcell.candidates.toString }
    assertResult("Cell value: 0, Cell value: 4\nCell value: 0, Cell value: 0"){ bcell.toString }
  }

  test("Serialize BigCell, bottom left corner") {
    val bcell = new BigCell(board, 2, 0)

    assertResult("Cell value: 4, Cell value: 3\nCell value: 0, Cell value: 0"){ bcell.toString }
  }

  test("numCells") {
    val bcell = new BigCell(board, 2, 0)
    assertResult(4) { bcell.numCells }
  }

  test("BigCell, findUniqueCol, top left") {
    val bcell = new BigCell(board, 2, 0)
    assertResult(-1) { bcell.findUniqueColFor(3) }
    assertResult(-1) { bcell.findUniqueColFor(1) }
    assertResult(-1) { bcell.findUniqueColFor(2) }
    assertResult(-1) { bcell.findUniqueColFor(4) }
  }

  test("BigCell, findUniqueRow, top right") {
    val bcell = new BigCell(board, 0, 2)

    assertResult(-1) { bcell.findUniqueRowFor(3) }
    assertResult(-1) { bcell.findUniqueRowFor(1) }
    assertResult(-1) { bcell.findUniqueRowFor(2) }
    assertResult(1) { bcell.findUniqueRowFor(4) }
  }
}