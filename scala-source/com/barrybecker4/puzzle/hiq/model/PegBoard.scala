// Copyright by Barry G. Becker, 2000-2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.hiq.model

import com.barrybecker4.common.geometry.ByteLocation
import com.barrybecker4.common.geometry.Location
import scala.collection.mutable

/**
  * Immutable representation of a PegBoard.
  *
  * @author Barry Becker
  */
object PegBoard {

  /** this must be odd */
  val SIZE = 7
  private val NUM_PEG_HOLES = 33
  private val CENTER: Byte = 3
  private val CORNER_SIZE = 2

  /** The initial board position constant */
  val INITIAL_BOARD_POSITION = new PegBoard()

  /**
    * @return true if the coordinates refer to one of the 33 board positions that can hold a peg.
    */
  def isValidPosition(row: Int, col: Int): Boolean = {
    if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) return false
    row >= CORNER_SIZE && row < SIZE - CORNER_SIZE || col >= CORNER_SIZE && col < SIZE - CORNER_SIZE
  }

  var i: Int = 0
  while (i < SIZE) {
      var j: Int = 0
      while (j < SIZE) {
          if (PegBoard.isValidPosition(i, j))
            INITIAL_BOARD_POSITION.setPosition(i.toByte, j.toByte, value = true)
          j += 1
      }
      i += 1
  }
  INITIAL_BOARD_POSITION.setPosition(PegBoard.CENTER, PegBoard.CENTER, value = false)
}

/**
  * maintains the compressed peg position information for the board.
  */
class PegBoard(var bits: Int, var finalBit: Boolean, var nextToFinalBit: Boolean) {

  /**
    * Copy constructor.
    */
  def this(board: PegBoard) {this(board.bits, board.finalBit, board.nextToFinalBit) }

  def this() { this(0, false, false) }

  /**
    * Constructor
    * create a new BoardPosition by applying a move to another BoardPosition.
    */
  def this(pos: PegBoard, move: PegMove, undo: Boolean) {
    this(pos)
    val fromRow = move.getFromRow
    val fromCol = move.getFromCol
    val toRow = move.getToRow
    val toCol = move.getToCol
    setPosition(fromRow, fromCol, undo)
    // Remove or replace the piece that was jumped as appropriate
    setPosition(((fromRow + toRow) >> 1).toByte, ((fromCol + toCol) >> 1).toByte, undo)
    setPosition(toRow, toCol, !undo)
  }

  def getPosition(row: Byte, col: Byte): Boolean = get(getIndexForPosition(row, col))

  /**
    * Private so others can not modify our immutable state after construction.
    */
  private def setPosition(row: Byte, col: Byte, value: Boolean) {
    set(getIndexForPosition(row, col), value)
  }

  def isEmpty(row: Byte, col: Byte): Boolean = !getPosition(row, col)

  /**
    * Because of symmetry, there is really only one first move not 4.
    *
    * @return Move the first move.
    */
  def getFirstMove = new PegMove(PegBoard.CENTER, (PegBoard.CENTER - 2).toByte, PegBoard.CENTER, PegBoard.CENTER)

  def isSolved: Boolean = getNumPegsLeft == 1 && getPosition(PegBoard.CENTER, PegBoard.CENTER)

  /**
    * Creates a new board with the move applied.
    * Does not violate immutability.
    */
  def doMove(move: PegMove, undo: Boolean) = new PegBoard(this, move, undo)

  /**
    * @param pegged boolean if true get pegged locations, else empty locations
    * @return List of pegged or empty locations
    */
  def getLocations(pegged: Boolean): List[Location] = {
    var list = List[Location]()
    var i = 0
    while (i < PegBoard.SIZE) {
        var j = 0
        while (j < PegBoard.SIZE) {
            if (PegBoard.isValidPosition(i, j) && getPosition(i.toByte, j.toByte) == pegged)
              list :+= new ByteLocation(i, j)
            j += 1
        }
        i += 1
    }
    list
  }

  /**
    * @return Map the coordinate location into our memory conserving hash.
    */
  private def getIndexForPosition(row: Int, col: Int): Int = {
    val p = row * 10 + col
    var index = -1
    if (p > 19 && p < 47) {
      // this crazy formula gives the index for the middle 3 rows in the board.
      return p % 10 + (p / 10 - 1) * 7 - 1
    }
    p match {
      case 2 => index = 0
      case 3 => index = 1
      case 4 => index = 2
      case 12 => index = 3
      case 13 => index = 4
      case 14 => index = 5
      case 52 => index = 27
      case 53 => index = 28
      case 54 => index = 29
      case 62 => index = 30
      case 63 => index = 31
      case 64 => index = 32
      case _ =>
        assert(assertion = false, "invalid position row=" + row + " col=" + col)
    }
    index
  }

  /**
    * Set value of position in internal compress data structure.
    */
  private def set(i: Int, value: Boolean) {
    if (i == PegBoard.NUM_PEG_HOLES - 1) finalBit = value
    else if (i == PegBoard.NUM_PEG_HOLES - 2) nextToFinalBit = value
    else {
      val place = 1 << i
      bits -= (if (get(i)) place else 0)
      bits += (if (value) place else 0)
    }
  }

  /**
    * @return extract the value of the ith bit.
    */
  private def get(i: Int): Boolean = {
    if (i == PegBoard.NUM_PEG_HOLES - 1) return finalBit
    if (i == PegBoard.NUM_PEG_HOLES - 2) return nextToFinalBit
    val place = 1 << i
    (bits & place) != 0
  }

  /**
    * @return number of pegs left on the board.
    */
  def getNumPegsLeft: Int = {
    var nPegsLeft = 0
    var i = 0
    while (i < PegBoard.NUM_PEG_HOLES) {
      {
        if (get(i)) nPegsLeft += 1
      }
      {
        i += 1; i - 1
      }
    }
    nPegsLeft
  }

  def containedIn(setOfBoards: mutable.Set[PegBoard]): Boolean = {
    var visited = false
    var i = 0
    while (!visited && i < PegBoardSymmetries.SYMMETRIES) {
        if (setOfBoards.contains(symmetry(i))) {
          visited = true
        }
        i += 1
    }
    visited
  }

  /**
    * Check all 8 symmetries
    * if rotateIndex = 0 then no rotation
    * if rotateIndex = 1 mirror image of this,
    * if rotateIndex = 2 then 90 degree rotation of this,
    * if rotateIndex = 3 then mirror image of 2, etc
    *
    * @return specified rotation of the board.
    */
  private def symmetry(symmIndex: Int) =
    if (symmIndex == 0) this else rotate(PegBoardSymmetries.getSymmetry(symmIndex))

  override def equals(b: Any): Boolean = {
    val board = b.asInstanceOf[PegBoard]
    bits == board.bits && finalBit == board.finalBit && nextToFinalBit == board.nextToFinalBit
  }

  /**
    * All but one bit accounted for in the hash.
    */
  override def hashCode: Int = if (nextToFinalBit) -bits else bits

  /**
    * Rotate the board according to symmetry.
    * Not all are rotational symmetries, but you get the idea....
    *
    * @return new board with specified rotation applied.
    */
  private def rotate(rotateIndices: Array[Byte]): PegBoard = {
    val rotatedBoard = new PegBoard()
    var i = 0
    while (i < PegBoard.NUM_PEG_HOLES) {
        rotatedBoard.set(i, get(rotateIndices(i)))
        i += 1
    }
    rotatedBoard
  }

  override def toString: String = {
    val buf = new StringBuilder(if (finalBit) "1" else "0")
    buf.append(if (nextToFinalBit) "1" else "0")
    buf.append(Integer.toBinaryString(bits))
    buf.toString
  }
}
