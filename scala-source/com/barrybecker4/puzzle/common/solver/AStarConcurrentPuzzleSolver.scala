package com.barrybecker4.puzzle.common.solver

import com.barrybecker4.search.SearchSpace


/**
  * @author Barry Becker
  */
class AStarConcurrentPuzzleSolver[P, M](searchSpace: SearchSpace[P, M])
  extends AStarPuzzleSolver[P, M](searchSpace) {
}
