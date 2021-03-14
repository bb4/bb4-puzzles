package com.barrybecker4.puzzle.common.solver

import com.barrybecker4.search.AStarSearch
import com.barrybecker4.search.queue.HeapPriorityQueue
import com.barrybecker4.search.space.SearchSpace


/**
  * @author Barry Becker
  */
class AStarPuzzleSolver[P, M](searchSpace: SearchSpace[P, M])
  extends AStarSearch[P, M](searchSpace, new HeapPriorityQueue[P, M](256, null))
  with PuzzleSolver[M] {
}
