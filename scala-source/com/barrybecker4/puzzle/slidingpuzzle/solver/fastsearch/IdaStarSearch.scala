// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.solver.fastsearch

object IdaStarSearch {
  /** The longest solution that can be recorded. The longest optimal solution of the 5 by 5 puzzle is far shorter. */
  private val MAX_SOLUTION_LENGTH = 1024
  /** How many positions are looked at between reports of progress. Must be a power of 2 (it is used as a mask). */
  val PROGRESS_INTERVAL: Int = 1 << 12
  /** A weight of 100 percent is the plain estimate. Weights above it make the search greedier. */
  val OPTIMAL_WEIGHT_PERCENT = 100
  private val NOT_APPLICABLE = -1
  private val PERCENT = 100
  private val UNBOUNDED = Int.MaxValue
}

/**
  * Iterative deepening A*. It looks for a solution whose weighted cost is at most `bound` by depth first search, giving up on any
  * path whose slides so far plus the estimate of the slides left is more than the bound. If there is none, the
  * bound is raised to the smallest estimate that was cut off, and it starts over. The first solution found is
  * therefore a shortest one.
  *
  * With a weight above 100 percent the estimate counts for more than the slides so far, so the search goes deeper sooner
  * and finishes much faster, at the price of a solution that may be longer than the shortest by up to that weight.
  *
  * It only remembers the path it is on, so it needs no more memory than the board, however long it runs. It never
  * slides a tile straight back to where it just came from.
  * @param state the position to solve. It is changed in place and ends up solved if a solution is found.
  * @param geometry the shape of the puzzle
  * @param onProgress told how many positions were looked at, every PROGRESS_INTERVAL positions. May throw to stop.
  * @param weightPercent how much the estimate counts for, compared to the slides so far, in percent
  * @param maxPositions the search gives up after looking at this many positions
  */
final class IdaStarSearch(state: SliderState, geometry: GridGeometry, onProgress: Long => Unit,
                         weightPercent: Int = IdaStarSearch.OPTIMAL_WEIGHT_PERCENT,
                         maxPositions: Long = Long.MaxValue) {
  import IdaStarSearch.*

  private val slidCells = new Array[Int](MAX_SOLUTION_LENGTH)
  private var solutionLength = 0
  private var found = false
  private var numVisited = 0L

  def positionsVisited: Long = numVisited

  /** @return the cell of the tile that is slid into the blank at each step of a shortest solution, unless the search
    *         gave up because it would have to look at more than `maxPositions` positions.
    */
  def solve(): Option[Array[Int]] = {
    var bound = weightedEstimate(0)
    while (!isFinished) bound = searchWithin(bound)
    if (found) Some(slidCells.take(solutionLength)) else None
  }

  private def isFinished: Boolean = found || numVisited > maxPositions

  /** Searches for a solution whose weighted cost is at most `bound`. @return the next bound to try if there was none. */
  private def searchWithin(bound: Int): Int = search(0, bound, NOT_APPLICABLE)

  private def search(depth: Int, bound: Int, previousBlank: Int): Int = {
    val estimated = weightedEstimate(depth)
    if (estimated > bound) estimated
    else if (state.estimate == 0) markSolved(depth)
    else tryEachSlide(depth, bound, previousBlank)
  }

  /** The cost of a path with the given number of slides so far, plus the weighted estimate of the rest. */
  private def weightedEstimate(depth: Int): Int = depth * PERCENT + state.estimate * weightPercent

  private def markSolved(depth: Int): Int = {
    found = true
    solutionLength = depth
    UNBOUNDED
  }

  /** @return the smallest estimate of a total path length that went over the bound. */
  private def tryEachSlide(depth: Int, bound: Int, previousBlank: Int): Int = {
    val candidates = geometry.neighbors(state.blankCell)
    var smallestOver = UNBOUNDED
    var i = 0
    while (i < candidates.length && !isFinished) {
      if (candidates(i) != previousBlank)
        smallestOver = Math.min(smallestOver, slideAndSearch(depth, bound, candidates(i)))
      i += 1
    }
    smallestOver
  }

  /** Slides the tile in `cell` into the blank, searches on from there, and slides it back if there was no solution. */
  private def slideAndSearch(depth: Int, bound: Int, cell: Int): Int = {
    val blankBefore = state.blankCell
    countVisit()
    slidCells(depth) = cell
    state.slideIntoBlank(cell)
    val result = search(depth + 1, bound, blankBefore)
    if (!found) state.slideIntoBlank(blankBefore)
    result
  }

  private def countVisit(): Unit = {
    numVisited += 1
    if ((numVisited & (PROGRESS_INTERVAL - 1)) == 0L) onProgress(numVisited)
  }
}
