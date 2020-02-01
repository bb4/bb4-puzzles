// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.model

import com.barrybecker4.common.geometry.Location
import com.barrybecker4.math.MathUtil
import scala.util.Random

/**
  * Immutable representation of a slider board.
  * @param tiles square grid of tiles. Size is the board edge. If size = 4, then there will be 16-1 = 15 tiles.
  * @author Barry Becker
  */
case class SliderBoard(tiles:Tiles) {

  private var hamming: Byte = -1
  private val manhattan = tiles.calculateManhattan

  /** @param size edge length of square board */
  def this(size: Int) {
    this(new Tiles(size.toByte))
  }

  /** Create a new Slider by applying a move to another Slider.
    * Applying the same move a second time will undo it because it just swaps tiles.
    */
  def this(board: SliderBoard, move: SlideMove) {
    this(board.tiles.applyMove(move))
  }

  def size: Int = tiles.size

  /**  shuffle the board tiles */
  def shuffle(rand: Random = MathUtil.RANDOM): SliderBoard = {
    SliderBoard(tiles.shuffle(getEmptyLocation, rand))
  }

  /** @return number of tiles not in the goal state (i.e. sequential order) */
  def getHamming: Byte = {
    if (hamming == -1) hamming = tiles.calculateHamming
    hamming
  }

  def distanceToGoal: Int = manhattan
  def getPosition(row: Byte, col: Byte): Byte = tiles.get(row, col)

  /** @return true if the coordinates refer to one of the tiles. */
  def isValidPosition(loc: Location): Boolean = tiles.isValidPosition(loc)

  /** @return true if all the tiles, when read across and down, are in increasing order.*/
  def isSolved: Boolean = getHamming == 0

  /** Creates a new board with the move applied. Does not violate immutability. */
  def doMove(move: SlideMove) = new SliderBoard(this, move)

  /** @return the position of the empty space (there is only one). */
  def getEmptyLocation: Location = tiles.getEmptyLocation

  override def toString: String =
    "Slider (ham:" + hamming+" manhattan:" + manhattan +"):\n" + tiles.toString
}
