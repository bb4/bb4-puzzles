// Copyright by Barry G. Becker, 2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.model

import com.barrybecker4.common.geometry.{ByteLocation, Location}

import scala.collection.immutable.{HashSet, IndexedSeq}
import scala.util.Random
import Tiles.*


object Tiles {
  /** The four directions that a tile can move */
  private val INDICES: List[Int] = List(0, 1, 2, 3)

  private def createGoalTiles(edgeLen: Byte): IndexedSeq[Byte] = {
    val numTiles = edgeLen * edgeLen
    (1 to numTiles).map(_ % numTiles).map(_.toByte)
  }
}

case class Tiles(tiles: IndexedSeq[Byte]) {

  val size: Int = Math.sqrt(tiles.length).toInt

  def this(edgeLen: Byte) = {this(createGoalTiles(edgeLen))}

  def get(loc:Location): Byte = get(loc.row, loc.col)
  def get(i: Int, j: Int): Byte = tiles(i * size + j)
  def applyMove(move: SlideMove): Tiles = swap(move.fromPosition, move.toPosition)

  def calculateHamming: Byte =
    tiles.zipWithIndex.count { case (value, idx) =>
      value != 0 && value != (idx + 1).toByte
    }.toByte

  def calculateManhattan: Int = {
    var totalDistance = 0
    for {
      i <- tiles.indices
      value = tiles(i)
      if value != 0
    } {
      val expCol = (value - 1) % size
      val expRow = (value - 1) / size
      val deltaRow = Math.abs(expRow - i / size)
      val deltaCol = Math.abs(expCol - i % size)
      totalDistance += deltaRow + deltaCol
    }
    totalDistance
  }

  /**
    * A uniformly random arrangement of the tiles that can be solved. Half of all arrangements cannot be solved
    * (see [[Solvability]]), so if a random one is one of those, two tiles are swapped, which makes it a solvable one.
    * Every solvable arrangement is equally likely.
    */
  def shuffleUniformly(rand: Random): Tiles = {
    val shuffled = rand.shuffle(tiles.toList)
    Tiles(makeSolvable(shuffled).map(_.toByte).toIndexedSeq)
  }

  /**
    * Moves tiles around until the blank position has been everywhere. Every position it makes can be solved because
    * it was made by sliding tiles. See http://en.wikipedia.org/wiki/15_puzzle#CITEREFJohnsonStory1879
    */
  def shuffleByRandomWalk(rand: Random): Tiles = {
    var visited = HashSet[Location]()
    var blankLocation = getEmptyLocation
    visited += blankLocation
    val newTiles: Array[Byte] = tiles.toArray

    while (visited.size < tiles.length) {
      val loc = randomNeighbor(blankLocation, rand)
      internalSwap(newTiles, blankLocation, loc)
      blankLocation = loc
      visited += blankLocation
    }
    Tiles(newTiles.toIndexedSeq)
  }

  /** A uniformly random orthogonally adjacent cell in bounds (neighbors of the blank exist while shuffling). */
  private def randomNeighbor(blankLocation: Location, rand: Random): Location =
    rand.shuffle(INDICES).iterator
      .map(i => blankLocation.incrementOnCopy(MoveGenerator.OFFSETS(i)))
      .find(isValidPosition)
      .get

  /** @return the arrangement, with the first two tiles (not counting the blank) swapped if it could not be solved. */
  private def makeSolvable(arrangement: List[Byte]): List[Int] = {
    val values = arrangement.map(_.toInt)
    if (Solvability.isSolvable(size, values)) values
    else swapFirstTwoTiles(values)
  }

  private def swapFirstTwoTiles(values: List[Int]): List[Int] = {
    val (first, second) = values.indices.filter(values(_) != 0).take(2) match { case Seq(a, b) => (a, b) }
    values.updated(first, values(second)).updated(second, values(first))
  }

  def isValidPosition(loc: Location): Boolean =
    loc.row >= 0 && loc.row < size && loc.col >= 0 && loc.col < size

  /** @return the position of the empty space (there is only one). */
  def getEmptyLocation: Location = {
    val i = tiles.indexWhere(_ == 0)
    if (i >= 0) ByteLocation((i / size).toByte, (i % size).toByte)
    else throw new IllegalStateException("There should have been a blank space in\n" + this)
  }

  override def toString: String = {
    val sb = new StringBuilder
    for (i <- 0 until size) {
      for (j <- 0 until size) {
        sb.append(get(i, j).toString).append(' ')
      }
      sb.append('\n')
    }
    sb.toString
  }

  private def swap(fromPosition: Location, toPosition: Location): Tiles = {
    val newTiles: Array[Byte] = tiles.toArray
    internalSwap(newTiles, fromPosition, toPosition)
    Tiles(newTiles.toIndexedSeq)
  }

  private def internalSwap(someTiles: Array[Byte], fromPosition: Location, toPosition: Location): Unit = {
    val fromIdx = fromPosition.row * size + fromPosition.col
    val toIdx = toPosition.row * size + toPosition.col
    val value = someTiles(fromIdx)
    someTiles(fromIdx) = someTiles(toIdx)
    someTiles(toIdx) = value
  }
}
