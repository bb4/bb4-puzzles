package com.barrybecker4.puzzle.sudoku.generation

import com.barrybecker4.common.concurrency.ThreadUtil
import com.barrybecker4.puzzle.sudoku.model.Board
import com.barrybecker4.puzzle.sudoku.ui.SudokuPanel
import SudokuGenerator.RANDOM
import scala.util.Random

/**
  * Generate a difficult Sudoku puzzle rougly based on the algorithm described at
  * https://dlbeer.co.nz/articles/sudoku.html
  * 1) Start by storing the solution grid as the best puzzle, with a score of 0.
  * 2) Randomly remove a value.
  * 3) If the new grid is not uniquely solvable, add a value randomly.
  *    If it is solvable and has a difficulty score lower or equal to current, remove a value randomly.
  *    If solvable and better score than current best, advance to step 4.
  * 4) store it as the new best puzzle,
  *    go to step 2 and repeat until get diminishing returns or threshold difficulty reached.
  * @param ppanel renders the puzzle. May be null if you do not want to see animation.
  * @author Barry Becker
  */
case class DifficultSudokuGenerator(ppanel: SudokuPanel = null, rand: Random = RANDOM) extends SudokuGenerator {

  private var totalCt: Long = 0L

  /**
    * Find a complete, consistent solution.
    *
    * @return generated random board
    */
  def generatePuzzleBoard(size: Int): Board = {
    totalCt = 0
    val board: Board = new Board(size)
    if (ppanel != null) ppanel.setBoard(board)
    val solution: Option[Board] = generateSolution(board) // sometimes fails to generate solution...
    assert(solution.isDefined, "We were not able to generate a consistent board " + board +
      ". numCombinations examined: " + totalCt)
    if (ppanel != null) ppanel.repaint(solution.get)

    val puzzleBoard = generateByRemoving(solution.get)
    puzzleBoard.reset().updateFromInitialData().get
  }

  /** @return board representing a consistent solution if one could be found, else None */
  def generateSolution(board: Board): Option[Board] = {
    val (consistent, solution) = generateSolution(board, 0)
    if (consistent) Some(solution) else None
  }

  /**
    * Recursive method to generate a completely solved, consistent sudoku board.
    * If at any point we find that we have an inconsistent/unsolvable board, then backtrack.
    *
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
    *
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
