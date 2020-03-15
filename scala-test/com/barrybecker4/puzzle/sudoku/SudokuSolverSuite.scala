// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku

import com.barrybecker4.puzzle.sudoku.data.TestData._
import com.barrybecker4.puzzle.sudoku.model.Board
import org.junit.Assert.{assertFalse, assertTrue}
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

/**
  * @author Barry Becker
  */
class SudokuSolverSuite extends AnyFunSuite with BeforeAndAfter {
  
  /** instance under test. */
  private var solver: SudokuSolver = _
  private var generator: SudokuGenerator = _
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
    val iterations = solver.solvePuzzle(new Board(SIMPLE_9))
    assertTrue("Did not solve SIMPLE_9 successfully", iterations.isDefined)
  }

  /** negative test case */
  test("ImpossiblePuzzle") {
    solver = new SudokuSolver()
    val iterations = solver.solvePuzzle(new Board(INCONSISTENT_9))
    assertFalse("Solved impossible INCONSISTENT_9 puzzle. Should not have.", iterations.isDefined)
  }

  /** negative test case */
  test("InvalidPuzzle") {
    solver = new SudokuSolver()
    val iterations = solver.solvePuzzle(new Board(INVALID_9))
    assertResult(None) { iterations }
  }

  test("HardNorvigPuzzle") {
    solver = new SudokuSolver()
    val iterations = solver.solvePuzzle(new Board(NORVIG_HARD_9))
    assertTrue("Did not solve NORVIG_HARD_9 puzzle successfully.", iterations.isDefined)
  }

  /** negative test case. Takes a very long time to determine that it is impossible *
  test("ImpossibleNorvigPuzzle") {
    solver = new SudokuSolver()
    val iterations = solver.solvePuzzle(new Board(NORVIG_IMPOSSIBLE_9))
    assertFalse("Solved impossible NORVIG_IMPOSSIBLE_9 puzzle. Should not have.", iterations.isEmpty)
  }*/

  test("Solving16x16Puzzle") {
    solver = new SudokuSolver
    val iterations = solver.solvePuzzle(new Board(COMPLEX_16))
    assertTrue("Unexpectedly could not solve 16x16 puzzle.", iterations.isDefined)
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
    generator = new SudokuGenerator(null, rand)
    val start = System.currentTimeMillis
    val b = generator.generatePuzzleBoard(new Board(baseSize))
    //System.out.println("Time to generate size=" + baseSize + " was " + (System.currentTimeMillis - start))
    b
  }

  private def solve(board: Board, rand: Random): Unit = {
    val solver = new SudokuSolver()
    val iterations = solver.solvePuzzle(board)
    assertTrue("Unexpectedly not solved.", iterations.isDefined)
  }
}


