// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.solver.reduction

import com.barrybecker4.puzzle.slidingpuzzle.solver.fastsearch.GridGeometry

import scala.collection.mutable

/**
  * Finds the fewest slides that move a few chosen tiles to where they belong without touching any cell in `fixedCells`.
  * Only the blank and the chosen tiles matter, since all other tiles are interchangeable for this purpose, so there
  * are very few positions to search (at most cells to the power of the number of chosen tiles + 1), and a
  * plain breadth first search over them is instant.
  *
  * A position is the cell of the blank followed by the cell of each chosen tile, packed into one int.
  * @param geometry the shape of the puzzle
  * @param fixedCells true for each cell that must not be disturbed
  * @author Barry Becker
  */
final class StepSearch(geometry: GridGeometry, fixedCells: Array[Boolean]) {

  private val numCells = geometry.numCells

  /** @return the cell slid into the blank at each step, or none if the tiles cannot be brought home. */
  def shortestSlides(blank: Int, tileCells: Array[Int], goalCells: Array[Int]): Option[Array[Int]] = {
    val start = pack(blank, tileCells)
    val cameFrom = mutable.HashMap[Int, Int](start -> start)
    val queue = mutable.Queue(start)
    var goal = if (isGoal(start, goalCells)) Some(start) else None
    while (goal.isEmpty && queue.nonEmpty) goal = expand(queue.dequeue(), cameFrom, queue, goalCells)
    goal.map(slidesTo(_, start, cameFrom, goalCells.length))
  }

  /** Adds the positions one slide away that are new. @return the goal position if one of them is. */
  private def expand(position: Int, cameFrom: mutable.HashMap[Int, Int], queue: mutable.Queue[Int],
                     goalCells: Array[Int]): Option[Int] = {
    val cells = unpack(position, goalCells.length)
    var goal: Option[Int] = None
    for (next <- geometry.neighbors(cells(0)) if !fixedCells(next) && !cameFrom.contains(slide(cells, next))) {
      val packed = slide(cells, next)
      cameFrom(packed) = position
      queue.enqueue(packed)
      if (goal.isEmpty && isGoal(packed, goalCells)) goal = Some(packed)
    }
    goal
  }

  /** @return the packed position after the tile in `next` slides into the blank. */
  private def slide(cells: Array[Int], next: Int): Int = {
    val moved = cells.map(cell => if (cell == next) cells(0) else cell)
    moved(0) = next
    pack(next, moved.drop(1))
  }

  /** The blank's cell after each slide is the cell whose tile was slid. */
  private def slidesTo(goal: Int, start: Int, cameFrom: mutable.HashMap[Int, Int], numTiles: Int): Array[Int] = {
    val blanks = Iterator.iterate(goal)(cameFrom).takeWhile(_ != start).map(unpack(_, numTiles)(0))
    blanks.toArray.reverse
  }

  private def isGoal(position: Int, goalCells: Array[Int]): Boolean =
    unpack(position, goalCells.length).drop(1).sameElements(goalCells)

  private def pack(blank: Int, tileCells: Array[Int]): Int = tileCells.foldLeft(blank)(_ * numCells + _)

  /** @return the blank cell followed by `numTiles` tile cells. */
  private def unpack(position: Int, numTiles: Int): Array[Int] = {
    val cells = new Array[Int](numTiles + 1)
    var rest = position
    for (i <- numTiles to 0 by -1) { cells(i) = rest % numCells; rest /= numCells }
    cells
  }
}
