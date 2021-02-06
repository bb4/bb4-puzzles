// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku

import com.barrybecker4.common.testsupport.strip
import com.barrybecker4.puzzle.sudoku.data.TestData._
import com.barrybecker4.puzzle.sudoku.generation.SimpleSudokuGenerator
import com.barrybecker4.puzzle.sudoku.model.Board
import org.junit.Assert.{assertEquals, assertFalse, assertTrue}
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

/**
  * @author Barry Becker
  */
class SudokuSolverSuite extends AnyFunSuite with BeforeAndAfter {
  
  /** instance under test. */
  private var solver: SudokuSolver = _
  private var generator: SimpleSudokuGenerator = _
  private var rand: Random = _

  /**
    * common initialization for all go test cases.
    */
  before {
    rand = new Random()
    rand.setSeed(1)
  }

  test("CaseSimpleSample") {
    solver = new SudokuSolver()
    val solvedBoard = solver.solvePuzzle(new Board(SIMPLE_9))
    assertTrue("Did not solve SIMPLE_9 successfully", solvedBoard.isDefined)
  }

  /** negative test case */
  test("ImpossiblePuzzle") {
    solver = new SudokuSolver()
    val solvedBoard = solver.solvePuzzle(new Board(INCONSISTENT_9))
    assertFalse("Solved impossible INCONSISTENT_9 puzzle. Should not have.", solvedBoard.isDefined)
  }

  /** negative test case */
  test("InvalidPuzzle") {
    solver = new SudokuSolver()
    val solvedBoard = solver.solvePuzzle(new Board(INVALID_9))
    assertResult(None) { solvedBoard }
  }

  test("Difficult9Puzzle") {
    solver = new SudokuSolver()
    val solvedBoard = solver.solvePuzzle(new Board(DIFFICULT_9))
    assertTrue("Did not solve DIFFICULT_9 puzzle successfully.", solvedBoard.isDefined)
    assertEquals(
      strip("""
              |Array(1, 4, 5, 3, 2, 7, 6, 9, 8),
              |Array(8, 3, 9, 6, 5, 4, 1, 2, 7),
              |Array(6, 7, 2, 9, 1, 8, 5, 4, 3),
              |Array(4, 9, 6, 1, 8, 5, 3, 7, 2),
              |Array(2, 1, 8, 4, 7, 3, 9, 5, 6),
              |Array(7, 5, 3, 2, 9, 6, 4, 8, 1),
              |Array(3, 6, 7, 5, 4, 2, 8, 1, 9),
              |Array(9, 8, 4, 7, 6, 1, 2, 3, 5),
              |Array(5, 2, 1, 8, 3, 9, 7, 6, 4)"""), solvedBoard.get.setSolvedValues().toString)
  }

  test("HardNorvigPuzzle") {
    solver = new SudokuSolver()
    val solvedBoard = solver.solvePuzzle(new Board(NORVIG_HARD_9))
    assertTrue("Did not solve NORVIG_HARD_9 puzzle successfully.", solvedBoard.isDefined)
  }

  /** negative test case. Takes a very long time to determine that it is impossible (almost 4 minutes on i7-6700K) *
  test("ImpossibleNorvigPuzzle") {
    solver = new SudokuSolver()
    val solvedBoard = solver.solvePuzzle(new Board(NORVIG_IMPOSSIBLE_9))
    assertTrue(s"Solved impossible NORVIG_IMPOSSIBLE_9 puzzle. Should not have.",
      solvedBoard.isEmpty)
  }*/

  /** There are many solutions for under-constrained puzzle. */
  test("UnderConstrainedPuzzle") {
    solver = new SudokuSolver()
    val solvedBoard = solver.solvePuzzle(new Board(UNDER_CONSTRAINED_IMPOSSIBLE_9))
    assertFalse("Failed to solve UNDER_CONSTRAINED_IMPOSSIBLE_9 puzzle.", solvedBoard.isEmpty)
  }

  /** No solutions if over-constrained */
  test("ImpossibleOverConstrainedPuzzle") {
    solver = new SudokuSolver()
    val solvedBoard = solver.solvePuzzle(new Board(OVER_CONSTRAINED_IMPOSSIBLE_9))
    assertTrue(s"Solved OVER_CONSTRAINED_IMPOSSIBLE_9 puzzle. Should not have.",
      solvedBoard.isEmpty)
  }

  test("Solving16x16Puzzle") {
    solver = new SudokuSolver
    val solvedBoard = solver.solvePuzzle(new Board(COMPLEX_16))
    assertTrue("Unexpectedly could not solve 16x16 puzzle.", solvedBoard.isDefined)
  }

  test("GenerateAndSolveMany 9x9 puzzles") {
    for (r <- 0 until 40)  {
      rand = new Random()
      rand.setSeed(r)
      generateAndSolve(3, rand)
    }
  }

  /** The large tests takes a long time because of the exponential growth with the size of the puzzle.
    * They super exponential run time as N increases */

  test("GenerateAndSolve2") {
    generateAndSolve(2, rand)
  }
  test("GenerateAndSolve3") {
    generateAndSolve(3, rand)
  }
  test("GenerateAndSolve4") {
    generateAndSolve(4, rand);  // 256 cells    2,077 ms
    // generateAndSolve(5);  // 625 cells  687,600 ms    too slow
  }

  private def generateAndSolve(baseSize: Int, rand: Random): Unit = {
    val board = generatePuzzle(baseSize, rand)
    solve(board, rand)
  }

  private def generatePuzzle(baseSize: Int, rand: Random) = {
    generator = new SimpleSudokuGenerator(null, rand)
    generator.generatePuzzleBoard(baseSize)
  }

  private def solve(board: Board, rand: Random): Unit = {
    val solver = new SudokuSolver()
    val solvedBoard = solver.solvePuzzle(board)
    assertTrue("Unexpectedly not solved.", solvedBoard.isDefined)
  }
}
