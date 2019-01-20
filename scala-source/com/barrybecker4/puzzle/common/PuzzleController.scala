// Copyright by Barry G. Becker, 2000-2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.common

import com.barrybecker4.search.SearchSpace


/**
  * PuzzleController constructor.
  * <p/>
  * Abstraction for puzzles like the 'sliding blocks puzzle'
  * The type parameters P and M correspond to a position (state) and a move (transition from one state to the next).
  *
  * @author Brian Goetz, and Tim Peierls
  */
trait PuzzleController[P, M] extends SearchSpace[P, M] {

  /** Get the algorithm to use.
    * @return algorithm to use when solving.
    */
  var algorithm: AlgorithmEnum[P, M]

  /** Begin the search to find a solution to the puzzle. */
  def startSolving(): Unit
}
