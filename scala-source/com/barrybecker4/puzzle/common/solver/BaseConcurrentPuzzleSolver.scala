package com.barrybecker4.puzzle.common.solver

import com.barrybecker4.math.MathUtil
import com.barrybecker4.puzzle.common.PuzzleController
import com.barrybecker4.puzzle.common.model.PuzzleNode
import scala.collection.mutable
import java.security.AccessControlException
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor


/**
  * Concurrent version of puzzle solver.
  * Does not recognize when there is no solution (use ConcurrentPuzzle Solver instead)
  * P is the Puzzle type
  * M is the move type
  *
  * @author Brian Goetz
  * @author Tim Peierls
  * @author Barry Becker
  */
object BaseConcurrentPuzzleSolver {
  private val THREAD_POOL_SIZE = 100
}

/**
  * @param puzzle the puzzle instance to solve.
  */
class BaseConcurrentPuzzleSolver[P, M](val puzzle: PuzzleController[P, M])
  extends PuzzleSolver[M] {

  final private val exec = initThreadPool

  /** Set of positions that have been visited */
  final private val seen = new mutable.HashSet[P]

  /** Number of nodes visited during search. Volatile to prevent corruption during concurrent updates */
  private var numTries = 0
  /** default is a mixture between depth (0) (sequential) and breadth (1.0) (concurrent) first search. */
  private var depthBreadthFactor = 0.4f

  exec match {
    case tpe: ThreadPoolExecutor =>
      tpe.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy)
    case _ => throw new IllegalArgumentException("Unexpected executor")
  }

  /** Prevents a value from being recieved until it is set. */
  final protected val solution = new ValueLatch[PuzzleNode[P, M]]


  /**
    * The amount that you want the search to use depth first or breadth first search.
    * If factor is 0, then all depth first traversal and no concurrent,
    * if 1, then all breadth first search and not sequential.
    * If the search is large, it is easier to run out of memory at the extremes.
    * Must be greater than 0 to have some amount of concurrency used.
    *
    * @param factor a number between 0 and 1. One being all breadth first search and not sequential.
    */
  private[solver] def setDepthBreadthFactor(factor: Float): Unit = {
    depthBreadthFactor = factor
  }

  /** initialize the thread pool with some initial fixed size */
  private def initThreadPool = Executors.newFixedThreadPool(BaseConcurrentPuzzleSolver.THREAD_POOL_SIZE)

  @throws[InterruptedException]
  override def solve: Option[Seq[M]] = try
    doSolve()
  finally try
    exec.shutdown()
  catch {
    case e: AccessControlException =>
      println("AccessControlException shutting down exec thread. " + "Probably because running in a secure sandbox.")
  }

  /**
    * Solve the puzzle concurrently
    *
    * @return list fo moves leading to the solution (assuming one was found).
    *         Null is returned if there was no solution.
    * @throws InterruptedException if interrupted during processing.
    */
  private def doSolve() = {
    val p: P = puzzle.initialState
    val startTime = System.currentTimeMillis
    val task: Runnable = newTask(p, None, None)
    exec.execute(task)
    // block until solution found
    val solutionPuzzleNode = solution.getValue
    // there has to be a better way to do this
    val path = if (solutionPuzzleNode == null) None //Option.apply(null)
    else Some(solutionPuzzleNode.asMoveList)
    val elapsedTime = System.currentTimeMillis - startTime
    val position: Option[P] = if (solutionPuzzleNode == null) None
                   else Some(solutionPuzzleNode.getPosition)
    System.out.println("solution = " + position)
    puzzle.finalRefresh(path, position, numTries, elapsedTime)
    path
  }

  protected def newTask(p: P, m: Option[M], n: Option[PuzzleNode[P, M]]): Runnable =
    new SolverTask(p, m, n) // BaseConcurrentPuzzleSolver[P, M]#SolverTask(p, m, n)

  /**
    * Runnable used to solve a puzzle.
    * The {depthBreadthFactor} determines whether to process the children
    * sequentially or concurrently based on depthBreadthFactor.
    */
  protected class SolverTask(pos: P, move: Option[M], prev: Option[PuzzleNode[P, M]])
    extends PuzzleNode[P, M](pos, move, prev) with Runnable {
    override def run(): Unit = {
      numTries += 1
      if (solution.isSet || puzzle.alreadySeen(getPosition, seen))
        return // already solved or seen this position, so skip
      puzzle.refresh(getPosition, numTries)
      if (puzzle.isGoal(getPosition)) solution.setValue(this)
      else {
        val transitions = puzzle.legalTransitions(getPosition)
        for (move <- transitions) {
          val task = newTask(puzzle.transition(getPosition, move), Some(move), Some(this))
          if (MathUtil.RANDOM.nextFloat > depthBreadthFactor) { // go deep
            task.run()
          }
          else { // go wide
            exec.execute(task)
          }
        }
      }
    }
  }

}

