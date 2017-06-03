package com.barrybecker4.puzzle.common.solver

import com.barrybecker4.puzzle.common.PuzzleController
import com.barrybecker4.puzzle.common.model.PuzzleNode
import java.util.concurrent.atomic.AtomicInteger


/**
  * Solver that recognizes when no solution exists and stops running if that happens.
  *
  * @param puzzle             the puzzle to solve
  * @param depthBreadthFactor the ratio of depth first to breadth first searching to use.
  *                           May have significant performance impact. If 1, then all BFS, if 0 then all DFS.
  * @author Brian Goetz
  * @author Tim Peierls
  */
class ConcurrentPuzzleSolver[P, M](puzzle: PuzzleController[P, M], val depthBreadthFactor: Float)
  extends BaseConcurrentPuzzleSolver[P, M](puzzle) {

  setDepthBreadthFactor(depthBreadthFactor)
  final private val taskCount = new AtomicInteger(0)
  taskCount.set(0)

  override protected def newTask(p: P, m: Option[M], n: Option[PuzzleNode[P, M]]) =
    new CountingSolverTask(p, m, n)

  /**
    * Inner class to identify when all tasks have been run without finding a solution.
    */
  class CountingSolverTask(pos: P, move: Option[M], prev: Option[PuzzleNode[P, M]])
    extends SolverTask(pos, move, prev) { // BaseConcurrentPuzzleSolver[P, M]#SolverTask

    taskCount.incrementAndGet

    override def run(): Unit = {
      try
        super.run()
      finally if (taskCount.decrementAndGet == 0) { // then there was no solution found
        solution.setValue(null)
      }
    }
  }

}
