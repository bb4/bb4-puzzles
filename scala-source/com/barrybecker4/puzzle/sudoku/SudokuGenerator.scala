// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku

import com.barrybecker4.common.concurrency.ThreadUtil
import com.barrybecker4.puzzle.sudoku.SudokuGenerator.RANDOM
import com.barrybecker4.puzzle.sudoku.model.board.Board
import com.barrybecker4.puzzle.sudoku.ui.SudokuPanel

import scala.util.Random

object SudokuGenerator {
  val RANDOM: Random = new Random()
}

/**
  * Generate a Sudoku puzzle.
  * Initially created with grandma Becker July 8, 2006
  * In 2017, I revised the solver algorithm based on ...
  * Though that change made the solver better, it actually made the generator worse.
  * Now the generator can only generate very simple to solve problems.
  * To really understand why, see
  * https://www.quora.com/Are-all-sudoku-puzzles-solvable-by-logical-deductions-or-some-do-require-a-guess-at-some-stage
  * The Norvig solution will find a solution to any initial configuration - even one that is under specified.
  * The version that grandma and I created would find a solution by applying the set of rules that we had implemented.
  *
  * @param size 4, 9, or 16
  * @param ppanel   renders the puzzle. May be null if you do not want to see animation.
  *
  * @author Barry Becker
  */
class SudokuGenerator (size: Int, var ppanel: SudokuPanel = null, rand: Random = RANDOM) {

  var delay: Int = 0
  private var totalCt: Long = 0L

  /**
    * Use this Constructor if you do not need to show the board in a UI.
    *
    * @param baseSize 4, 9, or 16
    */
  def this(baseSize: Int) { this(baseSize, null) }

  /**
    * Find a complete, consistent solution.
    *
    * @return generated random board
    */
  def generatePuzzleBoard: Board = {
    val board: Board = new Board(size)
    if (ppanel != null) ppanel.setBoard(board)
    val success: Boolean = generateSolution(board)  // sometimes fails to generate solution...
    if (ppanel != null) ppanel.repaint()
    assert(success, "We were not able to generate a consistent board " + board +
      ". numCombinations examined: " + totalCt)
    // now start removing values until we cannot deduce the final solution from it.
    // for every position (in random order) if we can remove it, do so.
    generateByRemoving(board)
  }

  /** @return whether or not the specified board has a consistent solution. */
  def generateSolution(board: Board): Boolean = generateSolution(board, 0)

  /**
    * Recursive method to generate a completely solved, consistent sudoku board.
    * If at any point we find that we have an inconsistent/unsolvable board, then backtrack.
    *
    * @param board the currently generated board (may be partial)
    * @return whether or not the current board is consistent.
    */
  private def generateSolution(board: Board, position: Int): Boolean = {

    if (position == board.numCells) {  // base case of recursion
      return true // board completely solved now
    }
    val loc = (position / board.edgeLength + 1, position % board.edgeLength + 1)
    val shuffledValues: Seq[Int] = rand.shuffle(board.getValues(loc))
    refresh()

    for (value <- shuffledValues) {
      totalCt += 1
      try {
        board.setOriginalValue(loc, value)
        val newBoard = board.copy()
        newBoard.updateFromInitialData()
        return generateSolution(board, position + 1)
      } catch {
        case e: IllegalStateException => board.setOriginalValue(loc, 0)
      }
    }
    false // backtrack
  }

  private def refresh() {
    if (ppanel == null) return
    if (delay <= 0) {
      if (Math.random() < 0.1) ppanel.repaint()
    }
    else {
      ppanel.repaint()
      ThreadUtil.sleep(delay)
    }
  }

  /**
    * Generate a sudoku puzzle that someone can solve. Do it by removing all the values you can and still
    * have a consistent board.
    *
    * @param board the initially solved puzzle
    * @return same puzzle after removing values in as many cells as possible and still retain consistency.
    */
  private def generateByRemoving(board: Board): Board = {
    if (ppanel != null) {
      ppanel.setBoard(board)
    }
    val positionList: Seq[(Int, Int)] = getRandomPositions(size, rand)
    // we need a solver to verify that we can still deduce the original
    val solver: SudokuSolver = new SudokuSolver()
    solver.delay = delay
    val len: Int = size * size
    val last: Int = len * len

    var newBoard = board
    for (i <- 0 until last)
      newBoard = newBoard.removeValueIfPossible(positionList(i))

    newBoard
  }

  /**
    * @param size the base size (fourth root of the number of cells).
    * @return the positions on the board in a random order in a list .
    */
  private def getRandomPositions(size: Int, rand: Random = RANDOM): Seq[(Int, Int)] = {
    val edgeLen = size * size
    val numPositions: Int = edgeLen * edgeLen
    val positionList: Seq[(Int, Int)] = (0 until numPositions).map(x => (x / edgeLen + 1, x % edgeLen + 1))
    rand.shuffle(positionList)
  }
}
