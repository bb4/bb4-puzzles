// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.solver.reduction

import com.barrybecker4.puzzle.slidingpuzzle.solver.SliderTestSupport.randomSolvableTiles
import com.barrybecker4.puzzle.slidingpuzzle.solver.fastsearch.GridGeometry
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

class ReductionSolverSuite extends AnyFunSuite {

  private def solvedBy(tiles: Seq[Int], slides: Array[Int]): Seq[Int] = {
    val board = tiles.toArray
    slides.foreach { cell => board(board.indexOf(0)) = board(cell); board(cell) = 0 }
    board.toSeq
  }

  test("solves random solvable boards of every size from 2 to 6") {
    val rnd = new Random(2)
    for (size <- 2 to 6; _ <- 1 to 25) {
      val tiles = randomSolvableTiles(size, rnd)
      val slides = new ReductionSolver(new GridGeometry(size)).solve(tiles)
      assertResult((1 until size * size) :+ 0)(solvedBy(tiles, slides))
    }
  }

  test("a solved board needs no slides") {
    assertResult(0)(new ReductionSolver(new GridGeometry(4)).solve((1 to 15) :+ 0).length)
  }

  test("every slide is of a tile next to the blank") {
    val geometry = new GridGeometry(4)
    val board = randomSolvableTiles(4, new Random(9)).toArray
    for (cell <- new ReductionSolver(geometry).solve(board.toSeq)) {
      assert(geometry.neighbors(board.indexOf(0)).contains(cell))
      board(board.indexOf(0)) = board(cell)
      board(cell) = 0
    }
  }

  test("fails clearly on a board that cannot be solved") {
    intercept[IllegalStateException](new ReductionSolver(new GridGeometry(3)).solve(Seq(2, 1, 3, 4, 5, 6, 7, 8, 0)))
  }
}
