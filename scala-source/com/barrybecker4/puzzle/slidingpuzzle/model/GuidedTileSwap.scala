// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.model

import com.barrybecker4.optimization.parameter.Swap

import scala.util.Random

/**
  * Finds a swap that is likely to put a misplaced tile closer to its goal cell. Pick a conflicted cell at random,
  * prefer putting the tile that belongs there into that cell, and otherwise swap with another conflicted cell.
  */
object GuidedTileSwap {

  /** @return a swap that repairs a misplaced cell, or None if the board is already solved */
  def find(board: SliderBoard, rnd: Random): Option[Swap] = {
    val conflicted = conflictedIndices(board)
    if (conflicted.isEmpty) None
    else {
      val anchor = conflicted(rnd.nextInt(conflicted.length))
      val goalTile = goalValueAt(anchor, board.size)
      val tiles = board.tiles.tiles
      val homeOfGoal = tiles.indexOf(goalTile)
      if (homeOfGoal >= 0 && homeOfGoal != anchor) Some(Swap(anchor, homeOfGoal))
      else {
        val others = conflicted.filter(_ != anchor)
        if (others.isEmpty) None else Some(Swap(anchor, others(rnd.nextInt(others.length))))
      }
    }
  }

  /** Cell indices whose tile is not the goal value for that cell. */
  def conflictedIndices(board: SliderBoard): Seq[Int] = {
    val tiles = board.tiles.tiles
    tiles.indices.filter(i => tiles(i) != goalValueAt(i, board.size))
  }

  /** Goal layout matches [[Tiles]]: 1..n²−1 then blank (0) in the last cell. */
  def goalValueAt(index: Int, edge: Int): Byte = {
    val numTiles = edge * edge
    ((index + 1) % numTiles).toByte
  }
}
