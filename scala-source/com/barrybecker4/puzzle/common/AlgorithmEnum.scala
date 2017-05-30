// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.common

import com.barrybecker4.puzzle.common.solver.PuzzleSolver


/**
  * Enum for type of solver to employ when solving the puzzle.
  * Solver for a given puzzle position P and state transition/move M.
  *
  * @author Barr Becker
  */
trait AlgorithmEnum[P, M] {
  def getLabel: String

  def ordinal: Int

  def createSolver(controller: PuzzleController[P, M]): PuzzleSolver[M]
}
