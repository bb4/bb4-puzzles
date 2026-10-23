// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.model

import scala.util.Random

/**
  * Mixes up the tiles of a sliding puzzle so that the result can always be solved.
  * @param rand the source of randomness
  */
class TileShuffler(rand: Random) {

  /** @return the tiles mixed up in the given way */
  def shuffle(tiles: Tiles, style: ShuffleStyle): Tiles = style match {
    case ShuffleStyle.RANDOM_WALK => byRandomWalk(tiles)
    case ShuffleStyle.UNIFORM_RANDOM => uniformly(tiles)
  }

  /**
    * A uniformly random arrangement of the tiles that can be solved. Half of all arrangements cannot be solved
    * (see [[Solvability]]), so if a random one is one of those, two tiles are swapped, which makes it a solvable one.
    * Every solvable arrangement is equally likely.
    */
  def uniformly(tiles: Tiles): Tiles = {
    val shuffled = rand.shuffle(tiles.tiles.map(_.toInt).toList)
    Tiles(makeSolvable(shuffled, tiles.size).map(_.toByte).toIndexedSeq)
  }

  /**
    * Slides tiles at random until the blank position has been in every cell. Every position it makes can be solved
    * because it was made by sliding tiles. See http://en.wikipedia.org/wiki/15_puzzle#CITEREFJohnsonStory1879
    */
  def byRandomWalk(tiles: Tiles): Tiles = {
    val cells = tiles.tiles.toArray
    val visited = Array.fill(cells.length)(false)
    var blank = cells.indexOf(0.toByte)
    visited(blank) = true
    while (visited.contains(false)) {
      val next = randomNeighbor(blank, tiles.size)
      swap(cells, blank, next)
      blank = next
      visited(blank) = true
    }
    Tiles(cells.toIndexedSeq)
  }

  /** @return the cell next to the given one, chosen so that each cell that is next to it is equally likely. */
  private def randomNeighbor(cell: Int, size: Int): Int = {
    val neighbors = neighborsOf(cell, size)
    neighbors(rand.nextInt(neighbors.length))
  }

  /** @return the cells above, below, left and right of the cell that are on the board */
  private def neighborsOf(cell: Int, size: Int): Seq[Int] = {
    val row = cell / size
    val col = cell % size
    Seq((row - 1, col), (row + 1, col), (row, col - 1), (row, col + 1))
      .filter((r, c) => r >= 0 && r < size && c >= 0 && c < size)
      .map((r, c) => r * size + c)
  }

  private def swap(cells: Array[Byte], a: Int, b: Int): Unit = {
    val value = cells(a)
    cells(a) = cells(b)
    cells(b) = value
  }

  /** @return the arrangement, with the first two tiles (not counting the blank) swapped if it could not be solved. */
  private def makeSolvable(arrangement: List[Int], size: Int): List[Int] =
    if (Solvability.isSolvable(size, arrangement)) arrangement
    else swapFirstTwoTiles(arrangement)

  private def swapFirstTwoTiles(values: List[Int]): List[Int] = {
    val Seq(first, second) = values.indices.filter(values(_) != 0).take(2)
    values.updated(first, values(second)).updated(second, values(first))
  }
}
