// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.solver

import com.barrybecker4.puzzle.common.solver.AStarPuzzleSolver
import com.barrybecker4.puzzle.slidingpuzzle.model.{SlideMove, SliderBoard}
import com.barrybecker4.puzzle.slidingpuzzle.solver.FastSliderSolver.SearchStage
import com.barrybecker4.puzzle.slidingpuzzle.solver.SliderTestSupport.*
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

class FastSliderSolverSuite extends AnyFunSuite {

  private val hardest3 = Seq(8, 6, 7, 2, 5, 4, 3, 0, 1)

  private def shortest(board: SliderBoard): Seq[SlideMove] = new FastSliderSolver(controllerFor(board)).solve.get

  test("a solved board needs no moves") {
    assertResult(0)(shortest(boardOf((1 to 8) :+ 0)).length)
  }

  test("the moves solve random boards of sizes 2 to 5") {
    val rnd = new Random(4)
    for (size <- 2 to 5; _ <- 1 to 5) {
      val board = boardOf(randomSolvableTiles(size, rnd))
      assert(isSolvedBy(board, new FastSliderSolver(controllerFor(board), FastSliderSolver.SHORTEST_IF_QUICK, true).solve.get))
    }
  }

  test("finds the same length as the generic A* solver, which is shortest") {
    val rnd = new Random(6)
    for (_ <- 1 to 15) {
      val board = boardOf(randomSolvableTiles(3, rnd))
      val generic = new AStarPuzzleSolver[SliderBoard, SlideMove](controllerFor(board)).solve.get
      assertResult(generic.length)(shortest(board).length)
    }
  }

  test("finds a 31 move solution to the hardest 3 by 3 board") {
    assertResult(31)(shortest(boardOf(hardest3)).length)
  }

  test("returns none for a board that cannot be solved") {
    assert(new FastSliderSolver(controllerFor(boardOf(Seq(2, 1, 3, 4, 5, 6, 7, 8, 0)))).solve.isEmpty)
  }

  test("falls back to solving tile by tile when the stages give up") {
    val board = boardOf(hardest3)
    val giveUp = Seq(SearchStage(100, 10))
    assert(new FastSliderSolver(controllerFor(board), giveUp).solve.isEmpty)
    val moves = new FastSliderSolver(controllerFor(board), giveUp, reduceIfGivenUp = true).solve.get
    assert(isSolvedBy(board, moves) && moves.length > 31)
  }

  test("moves are of a tile next to the blank into the blank") {
    val board = boardOf(randomSolvableTiles(4, new Random(8)))
    shortest(board).foldLeft(board) { (b, move) =>
      assertResult(b.getEmptyLocation)(move.toPosition)
      b.doMove(move)
    }
  }
}
