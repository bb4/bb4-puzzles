// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.model

import scala.util.Random


/**
  * The pieces that are in the red puzzle. This class is only mostly immutable.
  * There is a cost for immutability, and I did not do it for methods that are not needed for concurrency safety.
  *
  * @author Barry Becker
  */
object PieceList {
  /** the real game has 9 pieces, but I might experiment with 4 or 16 for testing. */
  val DEFAULT_NUM_PIECES = 9

  /** use the same seed for repeatable results. */
  private val RANDOM = new Random(5)
}

/** A list of puzzle pieces (initially empty). */
class PieceList(val numPieces: Int = PieceList.DEFAULT_NUM_PIECES) {

  require(numPieces != 0, "Num pieces should not be 0")
  require(numPieces == 4 || numPieces == 9 || numPieces == 16)
  private val edgeLen = Math.sqrt(numPieces).toInt
  var pieces: List[Piece] = List[Piece]()

  /**
    * a list of puzzle pieces with pieces to use specified.
    *
    * @param pieces array of pieces to use.
    */
  def this(pieces: Array[Piece]) {
    this(pieces.length)
    this.pieces = pieces.toList
  }

  /** private copy constructor */
  def this(pieces: List[Piece], numTotal: Int) {
    this(numTotal)
    this.pieces = pieces
  }

  /** Copy constructor */
  def this(pieces: PieceList) { this(pieces.pieces, pieces.getTotalNum) }

  /**
    * @param i the index of the piece to get.
    * @return the i'th piece.
    */
  def get(i: Int): Piece = {
    assert(i < numPieces, "there are only " + numPieces + " pieces, but you tried to get the " + (i + 1) + "th")
    pieces(i)
  }

  /** @return the last piece added. */
  def getLast: Piece = pieces.last

  /** @return The total number of pieces in the puzzle.*/
  def getTotalNum: Int = numPieces

  /** @return the number of pieces in the list.*/
  def size: Int = pieces.size

  /** Does this need to be made immutable? Swap 2 pieces. */
  def doSwap(p1Pos: Int, p2Pos: Int) {
    assert(p1Pos <= numPieces && p2Pos < numPieces,
      "The position indices must be less than " + numPieces + ".  You had " + p1Pos + ",  " + p2Pos)
    pieces = pieces.updated(0, pieces(p1Pos)).updated(2, pieces(p2Pos))
  }

  /** @param p piece to add to the end of the list. */
  def add(p: Piece): PieceList = add(pieces.size, p)

  /** @param i the position to add the piece.
    * @param p piece to add to at the specified position in the list.  */
  def add(i: Int, p: Piece): PieceList = {
    val newPieceList = new PieceList(this)

    val (front, back) = newPieceList.pieces.splitAt(i)
    newPieceList.pieces = front ++ List(p) ++ back
    assert(newPieceList.pieces.size <= numPieces, "there can only be at most " + numPieces + " pieces.")
    newPieceList
  }

  /**
    * @param p the piece to remove.
    * @return true if the list contained this element.
    */
  def remove(p: Piece): PieceList = {
    val newPieceList = new PieceList(this)
    newPieceList.pieces = newPieceList.pieces.filter(x => x != p)
    newPieceList
  }

  /**
    * @return piece list after removing the last element.
    */
  def removeLast(): PieceList = remove(pieces.size - 1)

  /**
    * @return the list with the ith element removed.
    */
  def remove(index: Int): PieceList = {
    val newPieceList = new PieceList(this)
    newPieceList.pieces = newPieceList.pieces.take(index) ++ newPieceList.pieces.drop(index + 1)
    newPieceList
  }

  /** ? Does this need to be made immutable? */
  def rotate(k: Int, numRotations: Int) {
    val p = pieces(k)
    val rp = p.rotate(numRotations)
    pieces = pieces.updated(k, rp)
  }

  /** @return a new shuffled PieceList object based on the old. All pieces moved and rotated */
  def shuffle: PieceList = {
    //for (p <- pieces) p.rotate(PieceList.RANDOM.nextInt(4))
    //pieces = PieceList.RANDOM.shuffle(pieces)
    var plist = List[Piece]()
    for (p <- pieces.reverse) {
      val newp = p.rotate(PieceList.RANDOM.nextInt(4))
      plist +:= newp
    }
    plist = PieceList.RANDOM.shuffle(plist)
    new PieceList(plist, numPieces)
  }

  /**
    * Try the piece.
    *
    * @param piece the piece to try to fit into our current solution.
    * @return true if it fits.
    */
  def fits(piece: Piece): Boolean = {
    // it needs to match the piece to the left and above (if present)
    var fits = true
    val numSolved = size
    val row = numSolved / edgeLen
    val col = numSolved % edgeLen
    if (col > 0) {   // if other than a left edge piece, then we need to match to the left side nub.
      val leftPiece = getLast
      if (!leftPiece.getRightNub.fitsWith(piece.getLeftNub)) fits = false
    }
    if (row > 0) {   // then we need to match with the top one
      val topPiece = get(numSolved - edgeLen)
      if (!topPiece.getBottomNub.fitsWith(piece.getTopNub)) fits = false
    }
    fits
  }

  /** @return the number of matches for the nubs on this piece  */
  def getNumFits(i: Int): Int = {
    assert(i < numPieces)
    // it needs to match the piece to the left and above (if present)
    var numFits = 0
    val dim = edgeLen
    val row = i / dim
    val col = i % dim
    val piece = get(i)
    if (col > 0) {
      // if other than a left edge piece, then we need to match to the left side nub.
      val leftPiece = get(i - 1)
      if (leftPiece.getRightNub.fitsWith(piece.getLeftNub)) numFits += 1
    }
    if (row > 0) {
      // then we need to match with the top one
      val topPiece = get(i - dim)
      if (topPiece.getBottomNub.fitsWith(piece.getTopNub)) numFits += 1
    }
    if (col < (dim - 1)) {
      // if other than a right edge piece, then we need to match to the right side nub.
      val rightPiece = get(i + 1)
      if (rightPiece.getLeftNub.fitsWith(piece.getRightNub)) numFits += 1
    }
    if (row < (dim - 1)) {
      // then we need to match with the bottom one
      val bottomPiece = get(i + dim)
      if (bottomPiece.getTopNub.fitsWith(piece.getBottomNub)) numFits += 1
    }
    numFits
  }

  /** @return true if we contain p */
  def contains(p: Piece): Boolean = pieces.contains(p)

  override def toString: String = {
    val buf = new StringBuilder("PieceList: (" + size + " pieces)\n")
    for (p <- pieces) buf.append(p.toString).append('\n')
    buf.toString
  }
}
