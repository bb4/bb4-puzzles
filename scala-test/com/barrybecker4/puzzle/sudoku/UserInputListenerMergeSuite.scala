// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku

import com.barrybecker4.common.geometry.ByteLocation
import com.barrybecker4.puzzle.sudoku.data.TestData
import com.barrybecker4.puzzle.sudoku.model.Board
import com.barrybecker4.puzzle.sudoku.ui.{UserInputListener, UserValue}
import org.junit.Assert.assertEquals
import org.scalatest.funsuite.AnyFunSuite

class UserInputListenerMergeSuite extends AnyFunSuite {

  test("mergeValidatedOriginals skips unvalidated entries") {
    val board = new Board(TestData.SIMPLE_4).updateFromInitialData().get
    val loc = ByteLocation(0, 0)
    val solved = new Board(TestData.SIMPLE_4).solve().get
    val correctVal = solved.getValue((1, 1))
    val merged = UserInputListener.mergeValidatedOriginals(board, Map(loc -> UserValue(correctVal)))
    assertEquals(board, merged)
  }

  test("mergeValidatedOriginals skips validated wrong entries") {
    val board = new Board(TestData.SIMPLE_4).updateFromInitialData().get
    val loc = ByteLocation(0, 0)
    val merged = UserInputListener.mergeValidatedOriginals(board, Map(loc -> UserValue(9).setValid(false)))
    assertEquals(board, merged)
  }

  test("mergeValidatedOriginals promotes validated correct entries") {
    val board = new Board(TestData.SIMPLE_4).updateFromInitialData().get
    val loc = ByteLocation(0, 0)
    val solved = new Board(TestData.SIMPLE_4).solve().get
    val correctVal = solved.getValue((1, 1))
    val merged =
      UserInputListener.mergeValidatedOriginals(board, Map(loc -> UserValue(correctVal).setValid(true)))
    assertEquals(correctVal, merged.getCell((1, 1)).originalValue)
  }
}
