// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku

import com.barrybecker4.common.concurrency.ThreadUtil
import com.barrybecker4.puzzle.sudoku.model.board.{Board, Cell, ValuesList}
import com.barrybecker4.puzzle.sudoku.ui.SudokuPanel


/**
  * Generate a Sudoku puzzle.
  * Initially created with grandma Becker July 8, 2006
  *
  * @param size 4, 9, or 16
  * @param ppanel   renders the puzzle. May be null if you do not want to see animation.
  *
  * @author Barry Becker
  */
class SudokuGenerator (var size: Int, var ppanel: SudokuPanel) {

  var delay: Int = 0
  private var totalCt: Long = 0L

  /**
    * Use this Constructor if you do not need to show the board in a UI.
    *
    * @param baseSize 4, 9, or 16
    */
  def this (baseSize: Int) {
    this (baseSize, null)
  }

  /**
    * find a complete consistent solution.
    *
    * @return generated random board
    */
  def generatePuzzleBoard: Board = {
    val board: Board = new Board(size)
    if (ppanel != null) {
      ppanel.setBoard(board)
    }
    val success: Boolean = generateSolution (board)
    if (ppanel != null) ppanel.repaint()
    assert (success, "We were not able to generate a consistent board " + board + "numCombinations examined = " + totalCt)
    // now start removing values until we cannot deduce the final solution from it.
    // for every position (in random order) if we can remove it, do so.
    generateByRemoving (board)
  }

  private def generateSolution (board: Board): Boolean = generateSolution (board, 0)

  /**
    * Recursive method to generate a completely solved, consistent sudoku board.
    * If at any point we find that we have an inconsistent/unsolvable board, then backtrack.
    *
    * @param board the currently generated board (may be partial)
    * @return whether or not the current board is consistent.
    */
  private def generateSolution (board: Board, position: Int): Boolean = {
    // base case of the recursion
    if (position == board.getNumCells) {
      // board completely solved now
      return true
    }
    val cell: Cell = board.getCell (position)
    val shuffledValues: ValuesList = ValuesList.getShuffledCandidates (cell.getCandidates)
    refresh ()

    for (value <- shuffledValues.elements) {
      cell.setValue (value)
      totalCt += 1
      if (generateSolution (board, position + 1) ) {
        return true
      }
      cell.clearValue ()
    }
    false
  }

  private def refresh() {
    if (ppanel == null) return
    if (delay >= 0) {
    ppanel.repaint ()
      ThreadUtil.sleep (delay)
    }
  }

  /**
    * Generate a sudoku puzzle that someone can solve.
    *
    * @param board the initially solved puzzle
    * @return same puzzle after removing values in as many cells as possible and still retain consistency.
    */
  private def generateByRemoving (board: Board): Board = {
    if (ppanel != null) {
      ppanel.setBoard(board)
    }
    val positionList: ValuesList = getRandomPositions(size)
    // we need a solver to verify that we can still deduce the original
    val solver: SudokuSolver = new SudokuSolver
    solver.delay = delay
    val len: Int = size * size
    val last: Int = len * len
    // the first len can be removed without worrying about having an unsolvable puzzle.
    for (i <- 0 until len) {
      val pos: Int = positionList.elements(i).asInstanceOf[Integer] - 1
      board.getCell (pos).clearValue ()
    }

    for (i <- len until last) {
      val pos: Int = positionList.elements(i).asInstanceOf[Integer] - 1
      tryRemovingValue(pos, board, solver)
    }
    board
  }

  /**
    * @param pos position to try removing.
    */
  private def tryRemovingValue (pos: Int, board: Board, solver: SudokuSolver) {
    val cell: Cell = board.getCell(pos)
    val value: Int = cell.getValue
    cell.clearValue ()
    if (ppanel != null && delay > 0) {
      ppanel.repaint ()
    }
    val copy: Board = new Board (board) // try to avoid this
    if (!solver.solvePuzzle(copy, ppanel) ) {
      // put it back since it cannot be solved without this positions value
      cell.setOriginalValue (value)
    }
  }

  /**
    * @param size the base size (fourth root of the number of cells).
    * @return the positions on the board in a random order in a list .
    */
  private def getRandomPositions(size: Int): ValuesList = {
    val numPositions: Int = size * size * size * size
    val positionList: ValuesList = new ValuesList(numPositions)
    positionList.shuffle() //Random.shuffle(positionList)
    positionList
  }
}
