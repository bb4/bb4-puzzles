package com.barrybecker4.puzzle.common.solver

import com.barrybecker4.puzzle.common.PuzzleController
import com.barrybecker4.puzzle.common.model.PuzzleNode
import scala.collection.Seq
import scala.collection.mutable


/**
  * Naive Sequential puzzle solver.
  * Performs a depth first search on the state space.
  * It will find a solution if there is one, but it may not be the best solution or the shortest path to it.
  * See A* for a better way to search that involves priority sorting of current paths.
  * P represents a puzzle state
  * M represents a move transition
  *
  * @param puzzle the puzzle to solve
  * @author Brian Goetz
  * @author Tim Peierls  (Java Concurrency in Practice)
  * @author Barry Becker
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
    * Recursive Depth first search for a solution to the puzzle.
    *
    * @param node the current state of the puzzle.
    * @return list of moves leading to a solution. Null if no solution.
    */
  private def search(node: PuzzleNode[P, M]): Option[PuzzleNode[P, M]] = {
    val currentState = node.getPosition
    if (!puzzle.alreadySeen(currentState, seen)) {
      if (puzzle.isGoal(currentState))
        return Some(node)
      val moves = puzzle.legalTransitions(currentState)

      for (move <- moves) {
        val position = puzzle.transition(currentState, move)
        puzzle.refresh(position, numTries)
        val child = new PuzzleNode[P, M](position, Some(move), Some(node))
        numTries += 1
        val result = search(child) // recursive call
        if (result.isDefined)
          return result
      }
    }
    None
  }
}
