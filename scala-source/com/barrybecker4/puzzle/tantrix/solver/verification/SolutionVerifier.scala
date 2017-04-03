/*
 * // Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
 */
package com.barrybecker4.puzzle.tantrix.solver.verification

import com.barrybecker4.puzzle.tantrix.model.TantrixBoard

/**
  * Used to determine whether or not a given tantrix state is a valid solution.
  *
  * @param board the tantrix state to test for solution.
  * @author Barry Becker
  */
class SolutionVerifier(var board: TantrixBoard) {

  /**
    * The puzzle is solved if there is a loop of the primary color
    * and all secondary colors match. Since a tile can only be placed in
    * a valid position, we only need to check if there is a complete loop.
    *
    * @return true if solved.
    */
  def isSolved: Boolean = {
    val loopDetector = new LoopDetector(board)
    val detector = new InnerSpaceDetector(board.tantrix)
    loopDetector.hasLoop && !detector.hasInnerSpaces
  }
}
