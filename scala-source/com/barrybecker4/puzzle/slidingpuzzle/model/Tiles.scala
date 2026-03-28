// Copyright by Barry G. Becker, 2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.model

import com.barrybecker4.common.geometry.{ByteLocation, IntLocation, Location}

import scala.collection.immutable.{HashSet, IndexedSeq}
import scala.util.Random
import Tiles.*


object Tiles {
  /** Represents the four different directions that a tile can move */
  val INDICES: List[Int] = List(0, 1, 2, 3)

  def createGoalTiles(edgeLen: Byte): IndexedSeq[Byte] = {
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

  /** If the tiles are randomly placed, it is not guaranteed that there will be a solution.
    * See http://en.wikipedia.org/wiki/15_puzzle#CITEREFJohnsonStory1879
    * To shuffleUntilSorted, move tiles around until the blank position has been everywhere.
    */
  def shuffle(emptyLocation: Location, rand: Random): Tiles = {
    var visited = HashSet[Location]()
    var blankLocation = emptyLocation
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
  private def randomNeighbor(blankLocation: Location, rand: Random): Location = {
    val order = rand.shuffle(INDICES)
    var i = 0
    var loc: Location = new IntLocation(-1, -1)
    while (!isValidPosition(loc)) {
      loc = blankLocation.incrementOnCopy(MoveGenerator.OFFSETS(order(i)))
      i += 1
    }
    loc
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
