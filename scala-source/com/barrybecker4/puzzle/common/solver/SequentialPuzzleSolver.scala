package com.barrybecker4.puzzle.common.solver

import com.barrybecker4.puzzle.common.PuzzleController
import com.barrybecker4.puzzle.common.model.PuzzleNode
import scala.collection.mutable


/**
  * Naive Sequential puzzle solver.
  * Performs a depth first search on the state space.
  * It will find a solution if there is one, but it may not be the best solution or the shortest path to it.
  * See A* for a better way to search that involves priority sorting of current paths.
  * Uses an explicit stack so deep searches (e.g. 15-puzzle) do not overflow the JVM call stack.
  * P represents a puzzle state
  * M represents a move transition
  *
  * @param puzzle the puzzle to solve
  * @author Brian Goetz
  * @author Tim Peierls  (Java Concurrency in Practice)
  */
class SequentialPuzzleSolver[P, M](val puzzle: PuzzleController[P, M]) extends PuzzleSolver[M] {
  /** The set of visited nodes. Do not re-search them. */
  final private val seen = new mutable.HashSet[P]
  private var numTries = 0

  override def solve: Option[Seq[M]] = {
    val pos = puzzle.initialState
    val startTime = System.currentTimeMillis
    val solutionState: Option[PuzzleNode[P, M]] = search(new PuzzleNode[P, M](pos))
    var pathToSolution: Option[Seq[M]] = Option.empty
    var solution: Option[P] = Option.empty
    if (solutionState.isDefined) {
      pathToSolution = Option.apply(solutionState.get.asMoveList)
      solution = Option.apply(solutionState.get.getPosition)
    }
    val elapsedTime = System.currentTimeMillis - startTime
    puzzle.finalRefresh(pathToSolution, solution, numTries, elapsedTime)
    pathToSolution
  }

  /**
    * Iterative depth-first search for a solution to the puzzle.
    * Same exploration order as the former recursive version: first legal transition first.
    *
    * @param start the initial state of the puzzle.
    * @return list of moves leading to a solution. None if no solution.
    */
  private def search(start: PuzzleNode[P, M]): Option[PuzzleNode[P, M]] = {
    val stack = mutable.Stack[PuzzleNode[P, M]](start)
    while (stack.nonEmpty) {
      val node = stack.pop()
      val currentState = node.getPosition
      if (!puzzle.alreadySeen(currentState, seen)) {
        if (puzzle.isGoal(currentState)) return Some(node)
        // Push in reverse so the first legal transition is explored first (DFS LIFO).
        for (move <- puzzle.legalTransitions(currentState).reverseIterator) {
          val position = puzzle.transition(currentState, move)
          puzzle.refresh(position, numTries)
          numTries += 1
          stack.push(new PuzzleNode[P, M](position, Some(move), Some(node)))
        }
      }
    }
    None
  }
}
