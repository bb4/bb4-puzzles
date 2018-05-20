// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.model

import scala.util.Random


object PieceList {

  /** the real game has 9 pieces, but I might experiment with 4 or 16 for testing. */
  val DEFAULT_NUM_PIECES = 9

  /** use the same seed for repeatable results. */
  private val RANDOM = new Random(5)
}

/**
  * A list of fit puzzle pieces (initially empty) for a numPieces sized puzzle.
  * @param pieces the currently fit pieces. Maybe be less than numTotal of them, but not more,
  * @param numTotal the total number of pieces in the puzzle.
  * @author Barry Becker
  */
case class PieceList(pieces: List[OrientedPiece], numTotal: Int) {

  require(numTotal != 0, "Total num pieces should not be 0")
  require(numTotal == 4 || numTotal == 9 || numTotal == 16)
  private val edgeLen = Math.sqrt(numTotal).toInt


  def this(pieces: Array[Piece]) {
    this(pieces.map(new OrientedPiece(_)).toList, pieces.length)
  }

  def this(numTotal: Int = PieceList.DEFAULT_NUM_PIECES) {
    this(List[OrientedPiece](), numTotal)
  }

  /** Copy constructor */
  def this(pieces: PieceList) { this(pieces.pieces, pieces.numTotal) }

  /** @param i the index of the piece to get.
    * @return the i'th piece.
    */
  def get(i: Int): OrientedPiece = {
    assert(i < numTotal, "there are only " + numTotal + " pieces, but you tried to get the " + (i + 1) + "th")
    pieces(i)
  }

  /** @return the last piece added. */
  def getLast: OrientedPiece = pieces.last

  /** @return the number of pieces in the list.*/
  def size: Int = pieces.size

  /** @return new piecelist with 2 indicated pieces swapped. */
  def doSwap(p1Pos: Int, p2Pos: Int): PieceList = {
    assert(p1Pos <= numTotal && p2Pos < numTotal,
      "The position indices must be less than " + numTotal + ".  You had " + p1Pos + ",  " + p2Pos)
    PieceList(pieces.updated(p2Pos, pieces(p1Pos)).updated(p1Pos, pieces(p2Pos)), numTotal)
  }

  /** @param p piece to add to the end of the list. */
  def add(p: OrientedPiece): PieceList = add(pieces.size, p)

  /** @param i the position to add the piece.
    * @param p piece to add to at the specified position in the list.
    */
  def add(i: Int, p: OrientedPiece): PieceList = {
    val (front, back) = pieces.splitAt(i)
    PieceList(front ++ List(p) ++ back, numTotal)
  }

  def remove(p: Piece): PieceList = PieceList(pieces.filter(x => x.piece != p), numTotal)

  def removeLast(): PieceList = remove(pieces.size - 1)

  /** @return the list with the ith element removed. */
  def remove(index: Int): PieceList = PieceList(pieces.take(index) ++ pieces.drop(index + 1), numTotal)

  def rotate(k: Int, numRotations: Int): PieceList = {
    val rp = pieces(k).rotate(numRotations)
    PieceList(pieces.updated(k, rp), numTotal)
  }

  /** @return a new shuffled PieceList object based on the old. All pieces moved and rotated */
  def shuffle: PieceList = {
    val pieceList = for (p <- pieces) yield p.rotate(PieceList.RANDOM.nextInt(4))
    PieceList(PieceList.RANDOM.shuffle(pieceList), numTotal)
  }

  /** @param piece the piece to try to fit into our current solution.
    * @return true if the piece fits.
    */
  def fits(piece: OrientedPiece): Boolean = {
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
    assert(i < numTotal)
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
  def contains(p: Piece): Boolean = pieces.map(_.piece).contains(p)

  override def toString: String = {
    val buf = new StringBuilder("PieceList: (" + size + " pieces)\n")
    for (p <- pieces) buf.append(p.toString).append('\n')
    buf.toString
  }
}
