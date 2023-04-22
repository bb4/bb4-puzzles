// Copyright by Barry G. Becker, 2000-2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.common.solver


/**
  * PuzzleSolver strategy pattern interface.
  * M is the move type
  * @author Barry Becker
  */
trait PuzzleSolver[M] {

  /** Solve the puzzle and return a list of moves that lead to the solution.
    * @return list of moves (transitions) that can be made to arrive at a solution.
    *         None if no solution found.
    */
  @throws[InterruptedException]
  def solve: Option[Seq[M]]
}
