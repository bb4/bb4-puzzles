// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.solver.fastsearch

/**
  * Counts the "linear conflicts" in one row or column, which the Manhattan distance misses.
  *
  * Two tiles are in linear conflict when both are in the row they belong in, but in the wrong order, like 3 1 in the
  * top row. Each must step out of the row so the other can pass, and step back in again, which costs at least
  * 2 extra slides. The least number of tiles that have to step out is all of the tiles that belong in the line
  * except for the longest run of them that is already in the right order. Counting that gives a larger estimate that is
  * still never more than the true number of slides, so the search stays optimal.
  * @author Barry Becker
  */
final class LineConflicts(geometry: GridGeometry) {

  /** Each tile that has to step out of its line and back in costs this many extra slides. */
  private val EXTRA_SLIDES_PER_TILE = 2

  /** The cell positions (within the line) of the tiles that belong in it, in order, and the longest run ending at each. */
  private val goalPositions = new Array[Int](geometry.size)
  private val longestRun = new Array[Int](geometry.size)

  /** @return extra slides needed because of tiles in the wrong order in the given row */
  def rowExtra(tiles: Array[Int], row: Int): Int = extraSlides(tiles, row * geometry.size, 1, row, isRow = true)

  /** @return extra slides needed because of tiles in the wrong order in the given column */
  def colExtra(tiles: Array[Int], col: Int): Int = extraSlides(tiles, col, geometry.size, col, isRow = false)

  /** Walks the line from its first cell, stepping by `stride`, keeping the tiles that belong in the line. */
  private def extraSlides(tiles: Array[Int], firstCell: Int, stride: Int, line: Int, isRow: Boolean): Int = {
    var count = 0
    var i = 0
    while (i < geometry.size) {
      val tile = tiles(firstCell + i * stride)
      if (tile != 0 && lineOfGoal(tile, isRow) == line) {
        goalPositions(count) = positionInLineOfGoal(tile, isRow)
        count += 1
      }
      i += 1
    }
    EXTRA_SLIDES_PER_TILE * (count - longestIncreasingRun(count))
  }

  /** @return the row (or column) that the tile belongs in */
  private def lineOfGoal(tile: Int, isRow: Boolean): Int =
    if (isRow) geometry.row(geometry.goalCell(tile)) else geometry.col(geometry.goalCell(tile))

  /** @return where along the row (or column) the tile belongs */
  private def positionInLineOfGoal(tile: Int, isRow: Boolean): Int = lineOfGoal(tile, !isRow)

  /** @return the length of the longest increasing subsequence of the first `count` goal positions. */
  private def longestIncreasingRun(count: Int): Int = {
    var best = 0
    var i = 0
    while (i < count) {
      longestRun(i) = 1 + longestRunBefore(i)
      best = Math.max(best, longestRun(i))
      i += 1
    }
    best
  }

  private def longestRunBefore(i: Int): Int = {
    var best = 0
    var j = 0
    while (j < i) {
      if (goalPositions(j) < goalPositions(i) && longestRun(j) > best) best = longestRun(j)
      j += 1
    }
    best
  }
}
