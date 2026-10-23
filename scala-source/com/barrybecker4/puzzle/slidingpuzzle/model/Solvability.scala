// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.model

/**
  * Tells whether a sliding puzzle position can be slid into the solved one at all. Exactly half of the ways to arrange
  * the tiles cannot, and a search would never finish on one of those, so they must be ruled out first.
  *
  * Sliding a tile sideways does not change the order of the tiles read row by row. Sliding one up or down moves it past
  * `size - 1` others. So: for an odd size the number of out of order pairs (inversions) must be even, and for an even
  * size the inversions plus the row of the blank (counting from the bottom) must be odd, when the solved board has the
  * blank in the last cell.
  * @author Barry Becker
  */
object Solvability {

  /** @param size the number of cells along an edge
    * @param tiles the tile in each cell, row by row, with 0 for the blank
    */
  def isSolvable(size: Int, tiles: Seq[Int]): Boolean = {
    val inversions = countInversions(tiles.filter(_ != 0))
    if (size % 2 == 1) inversions % 2 == 0
    else (inversions + rowFromBottom(size, tiles)) % 2 == 1
  }

  /** @return 1 when the blank is in the last row */
  private def rowFromBottom(size: Int, tiles: Seq[Int]): Int = size - tiles.indexOf(0) / size

  /** @return the number of pairs of tiles where the one that comes first is the larger. */
  private def countInversions(tiles: Seq[Int]): Int =
    (for (i <- tiles.indices; j <- i + 1 until tiles.size if tiles(i) > tiles(j)) yield 1).sum
}
