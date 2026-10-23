// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.solver.reduction

import com.barrybecker4.puzzle.slidingpuzzle.solver.fastsearch.GridGeometry

import scala.collection.mutable.ArrayBuffer

/**
  * Solves any solvable sliding puzzle in a few milliseconds, the way a person would, one tile (or pair) at a time
  * (see [[SolvingOrder]]). The solution is not the shortest. For the 5 by 5 puzzle it is typically around 150 slides,
  * where a shortest one would be about 40 to 60. What it offers is that it always finishes, and quickly, when the
  * search for a shortest solution would take minutes or longer.
  * @param geometry the shape of the puzzle
  * @author Barry Becker
  */
final class ReductionSolver(geometry: GridGeometry) {

  /** @return the cell of the tile that is slid into the blank at each step. */
  def solve(startTiles: Seq[Int]): Array[Int] = {
    val tiles = startTiles.toArray
    val fixedCells = new Array[Boolean](geometry.numCells)
    val slides = ArrayBuffer[Int]()
    for (step <- SolvingOrder.steps(geometry)) {
      slides ++= completeStep(step, tiles, fixedCells)
      step.foreach(tile => fixedCells(geometry.goalCell(tile)) = true)
    }
    slides.toArray
  }

  /** Slides the tiles of the step home. @return the cell slid into the blank at each slide. */
  private def completeStep(step: Seq[Int], tiles: Array[Int], fixedCells: Array[Boolean]): Array[Int] = {
    val tileCells = step.map(tiles.indexOf(_)).toArray
    val goalCells = step.map(geometry.goalCell).toArray
    val search = new StepSearch(geometry, fixedCells)
    val slides = search.shortestSlides(tiles.indexOf(0), tileCells, goalCells)
      .getOrElse(throw new IllegalStateException(s"Cannot place tiles $step. Is the puzzle solvable?"))
    slides.foreach(slideIntoBlank(tiles, _))
    slides
  }

  private def slideIntoBlank(tiles: Array[Int], cell: Int): Unit = {
    tiles(tiles.indexOf(0)) = tiles(cell)
    tiles(cell) = 0
  }
}
