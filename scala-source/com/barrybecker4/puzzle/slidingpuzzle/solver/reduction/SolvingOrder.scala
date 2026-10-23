// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.solver.reduction

import com.barrybecker4.puzzle.slidingpuzzle.solver.fastsearch.GridGeometry

/**
  * The order in which people solve a sliding puzzle by hand, one small step at a time, never disturbing what is done.
  * Each step puts one tile, or a pair or trio that must go in together, where they belong:
  *  1. The rows from the top, except for the last two rows. Each tile of the row on its own, except that the last
  *     two tiles of the row go in together (placing the second to last alone would block the last).
  *  2. Then the columns from the left in the last two rows, two tiles at a time, for the same reason.
  *  3. That leaves a 2 by 2 corner. Its three tiles go in together.
  *
  * Each step is a list of the tile numbers that it places. A tile's number is one more than the cell it belongs in.
  * @author Barry Becker
  */
object SolvingOrder {

  def steps(geometry: GridGeometry): Seq[Seq[Int]] = {
    val last = geometry.size - 1
    val rowSteps = (0 until last - 1).flatMap(row => rowStep(geometry, row))
    val columnSteps = (0 until last - 1).map(col => Seq(tileAt(geometry, last - 1, col), tileAt(geometry, last, col)))
    rowSteps ++ columnSteps :+ cornerStep(geometry)
  }

  /** The tiles of a row on their own, then the last two together. */
  private def rowStep(geometry: GridGeometry, row: Int): Seq[Seq[Int]] = {
    val last = geometry.size - 1
    val singles = (0 until last - 1).map(col => Seq(tileAt(geometry, row, col)))
    singles :+ Seq(tileAt(geometry, row, last - 1), tileAt(geometry, row, last))
  }

  private def cornerStep(geometry: GridGeometry): Seq[Int] = {
    val last = geometry.size - 1
    Seq(tileAt(geometry, last - 1, last - 1), tileAt(geometry, last - 1, last), tileAt(geometry, last, last - 1))
  }

  private def tileAt(geometry: GridGeometry, row: Int, col: Int): Int = row * geometry.size + col + 1
}
