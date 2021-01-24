// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku

import com.barrybecker4.common.concurrency.ThreadUtil
import com.barrybecker4.puzzle.sudoku.SudokuGenerator.RANDOM
import com.barrybecker4.puzzle.sudoku.model.Board
import com.barrybecker4.puzzle.sudoku.ui.SudokuPanel

import scala.util.Random

object SudokuGenerator {
  val RANDOM: Random = new Random()
}

/**
  * Generate a Sudoku puzzle.
  * Initially created with grandma Becker July 8, 2006
  * In 2017, I revised the solver algorithm based on https://norvig.com/sudoku.html
  * Though that change made the solver better, it actually made the generator worse.
  * Now the generator can only generate very simple to solve problems.
  * To really understand why, see
  * https://www.quora.com/Are-all-sudoku-puzzles-solvable-by-logical-deductions-or-some-do-require-a-guess-at-some-stage
  * The Norvig solution will find a solution to any initial configuration - even one that is under specified.
  * The version that grandma and I created would find a solution by applying the set of rules that we had implemented.
  *
  * @param ppanel renders the puzzle. May be null if you do not want to see animation.
  * @author Barry Becker
  */
class SudokuGenerator (var ppanel: SudokuPanel = null, rand: Random = RANDOM) {

  var delay: Int = 0
  private var totalCt: Long = 0L

  /**
    * Find a complete, consistent solution.
    * @return generated random board
    */
  def generatePuzzleBoard(size: Int): Board = {
    totalCt = 0
    val board: Board = new Board(size)
    if (ppanel != null) ppanel.setBoard(board)
    val solution: Option[Board] = generateSolution(board)  // sometimes fails to generate solution...
    assert(solution.isDefined, "We were not able to generate a consistent board " + board +
      ". numCombinations examined: " + totalCt)
    if (ppanel != null) ppanel.repaint(solution.get)

    generateByRemoving(solution.get)
  }

  /** @return board representing a consistent solution if one could be found, else None  */
  def generateSolution(board: Board): Option[Board] = {
    val (consistent, solution) = generateSolution(board, 0)
    if (consistent) Some(solution) else None
  }

  /**
    * Recursive method to generate a completely solved, consistent sudoku board.
    * If at any point we find that we have an inconsistent/unsolvable board, then backtrack.
    * @param board the currently generated board (may be partial)
    * @return (true, solvedBoard), or (false, board) if could not find consistent solution.
    */
  private def generateSolution(board: Board, position: Int): (Boolean, Board) = {

    if (position == board.numCells) // base case of recursion
      return (true, board) // board completely solved now

    val loc = (position / board.edgeLength + 1, position % board.edgeLength + 1)
    val shuffledValues: Seq[Int] = rand.shuffle(board.getValues(loc))
    refresh(board)

    var updatedBoard = board

    for (value <- shuffledValues) {
      totalCt += 1
      updatedBoard = board.setOriginalValue(loc, value)
      refresh(updatedBoard)
      val updatedBoard2 = updatedBoard.updateFromInitialData()
      if (updatedBoard2.isDefined) {
        val (consistent, updatedBoard3) = generateSolution(updatedBoard2.get, position + 1)
        if (consistent) return (true, updatedBoard3)
        else {
          updatedBoard = updatedBoard.reset()
        }
      }
    }
    (false, updatedBoard.setOriginalValue(loc, 0)) // undo
  }

  private def refresh(board: Board): Unit = {
    if (ppanel == null) return
    if (delay < 0) {
      if (Math.random() < 0.05) ppanel.repaint()
    }
    else {
      ppanel.repaint(board)
      ThreadUtil.sleep(delay)
    }
  }

  /** Generate a sudoku puzzle that someone can solve. Do it by removing all the values you can and still
    * have a consistent board.
    * @param board the initially solved puzzle
    * @return same puzzle after removing values in as many cells as possible and still retain consistency.
    */
  private def generateByRemoving(board: Board): Board = {

    val positionList: Seq[(Int, Int)] = getRandomPositions(board.baseSize, rand)
    // we need a solver to verify that we can still deduce the original
    val len: Int = board.edgeLength
    val last: Int = len * len

    var b = board
    for (i <- 0 until last) {
      val newBoard = b.removeValueIfPossible(positionList(i), Some(refresh))
      if (newBoard.isDefined) {
        b = newBoard.get
      }
    }
    b
  }

  /** @param size the base size (fourth root of the number of cells).
    * @return the positions on the board in a random order in a list .
    */
  private def getRandomPositions(size: Int, rand: Random = RANDOM): Seq[(Int, Int)] = {
    val edgeLen = size * size
    val numPositions: Int = edgeLen * edgeLen
    val positionList: Seq[(Int, Int)] = (0 until numPositions).map(x => (x / edgeLen + 1, x % edgeLen + 1))
    rand.shuffle(positionList)
  }
}
