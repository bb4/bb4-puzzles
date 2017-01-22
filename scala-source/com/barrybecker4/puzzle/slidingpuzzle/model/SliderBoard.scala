// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.model

import java.util
import java.util.Arrays

import com.barrybecker4.common.geometry.ByteLocation
import com.barrybecker4.common.geometry.Location

import scala.util.Random
import scala.collection.immutable.HashSet
import SliderBoard.createTiles


object SliderBoard {
  /** Represents the four different directions that a tile can move */
  val INDICES: List[Int] = List(0, 1, 2, 3)

  private def createTiles(size: Int): Array[Array[Byte]] = {
    val tiles = Array.ofDim[Byte](size, size)
    var ct = 1
    for (row <- 0 until size) {
      for (col <- 0 until size) {
        tiles(row)(col) = ct.toByte
        ct += 1
      }
    }
    tiles(size - 1)(size - 1) = 0
    tiles
  }
}

/**
  * Immutable representation of a slider board.
  *
  * @param tiles square grid of tiles. Size is the board edge. If size = 4, then there will be 16-1 = 15 tiles.
  * @param shuffle if true then the created slider will have the tiles shuffled,
  *                else they will be in the goal state.
  * @author Barry Becker
  */
case class SliderBoard(tiles:Array[Array[Byte]], shuffle: Boolean) {

  val size: Int = tiles.length
  if (shuffle) shuffleTiles()
  private var hamming: Byte = -1
  private var manhattan = calculateManhattan

  def this(tiles: Array[Array[Int]]) { this(tiles.map(_.map(_.toByte)), false)}

  /**
    * Constructor.
    * @param size esge length of square board
    * @param shuffle if true then the created slider will have the tiles shuffled,
    *                else they will be in the goal state.
    */
  def this(size: Int, shuffle: Boolean) {
    this(createTiles(size), shuffle)
  }

  def this(board: SliderBoard) {
    this(Array.ofDim[Byte](board.size, board.size), false)
    for (i <- 0 until size) System.arraycopy(board.tiles(i), 0, tiles(i), 0, size)
    this.hamming = board.hamming
    this.manhattan = board.manhattan
  }

  /**
    * Constructor. Create a new Slider by applying a move to another Slider.
    * Applying the same move a second time will undo it because it just swaps tiles.
    */
  def this(pos: SliderBoard, move: SlideMove) {
    this(pos)
    applyMove(move)
    this.hamming = -1
    this.manhattan = calculateManhattan
  }

  def getHamming: Byte = {
    if (hamming == -1) hamming = calculateHamming
    hamming
  }

  def getManhattan: Int = manhattan
  def distanceToGoal: Int = manhattan

  private def calculateHamming: Byte = {
    var expected = 0
    var hamCount = 0
    for (i <- 0 until size) {
        for (j <- 0 until size) {
            val value = tiles(i)(j)
            expected += 1
            if (value != 0 && value != expected) hamCount += 1
        }
    }
    hamCount.toByte
  }

  private def calculateManhattan = {
    var totalDistance = 0
    for (i <- 0 until size) {
        for (j <- 0 until size) {
            val value = tiles(i)(j)
            if (value != 0) {
              val expCol = (value - 1) % size
              val expRow = (value - 1) / size
              val deltaRow = Math.abs(expRow - i)
              val deltaCol = Math.abs(expCol - j)
              totalDistance += deltaRow + deltaCol
            }
        }
    }
    totalDistance
  }

  def getPosition(row: Byte, col: Byte): Byte = tiles(row)(col)

  /**
    * If the tiles are randomly placed, it is not guaranteed that there will be a solution.
    * See http://en.wikipedia.org/wiki/15_puzzle#CITEREFJohnsonStory1879
    * To shuffleUntilSorted, move tiles around until the blank position has been everywhere.
    */
  private def shuffleTiles() {
    var visited = HashSet[Location]()
    var blankLocation = getEmptyLocation
    visited += blankLocation
    val numTiles = size * size
    val rand = new Random()
    while (visited.size < numTiles) {
      val indices: List[Int] = rand.shuffle(SliderBoard.INDICES)
      var loc: Location = null
      var ct = 0
      do {
        loc = blankLocation.incrementOnCopy(MoveGenerator.OFFSETS(indices(ct)))
        ct += 1
      } while (!isValidPosition(loc))

      val move = SlideMove(blankLocation, loc)
      applyMove(move)
      blankLocation = loc
      visited += blankLocation
    }
  }

  private def applyMove(move: SlideMove) {
    val fromRow = move.getFromRow
    val fromCol = move.getFromCol
    val toRow = move.getToRow
    val toCol = move.getToCol
    val value = getPosition(fromRow, fromCol)
    setPosition(fromRow, fromCol, getPosition(toRow, toCol))
    setPosition(toRow, toCol, value)
  }

  /** Private so others can not modify our immutable state after construction. */
  private def setPosition(row: Byte, col: Byte, v: Byte) {
    tiles(row)(col) = v
  }

  /** @return true if the coordinates refer to one of the tiles. */
  def isValidPosition(loc: Location): Boolean =
    loc.getRow >= 0 && loc.getRow < size && loc.getCol >= 0 && loc.getCol < size

  /** @return true if all the tiles, when read across and down, are in increasing order.*/
  def isSolved: Boolean = getHamming == 0

  /** Creates a new board with the move applied. Does not violate immutability. */
  def doMove(move: SlideMove) = new SliderBoard(this, move)

  /** @return the position of the empty space (there is only one). */
  def getEmptyLocation: Location = {
    for (i <- 0 until size) {
        for (j <- 0 until size) {
            if (getPosition(i.toByte, j.toByte) == 0) return new ByteLocation(i, j)
        }
    }
    throw new IllegalStateException("There should have been a blank space in\n" + toString)
  }

  override def equals(o: Any): Boolean = {
    if (!o.isInstanceOf[SliderBoard]) return false
    val board = o.asInstanceOf[SliderBoard]
    if (size != board.size) return false
    if (this.getHamming != board.getHamming) return false
    tiles.deep == board.tiles.deep
  }

  override def hashCode: Int = util.Arrays.deepHashCode(tiles.toArray)

  override def toString: String =
    "Slider (ham:"+hamming+" manhattan:"+ manhattan +"):\n" + tiles.map(_.mkString("\t")).mkString("\n")
}
