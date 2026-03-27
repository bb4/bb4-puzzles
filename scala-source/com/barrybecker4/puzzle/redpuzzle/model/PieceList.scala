// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.model

import com.barrybecker4.math.MathUtil

import scala.util.Random


object PieceList {

  /** The standard game has 9 pieces; 4 pieces are supported for smaller puzzles. */
  val DEFAULT_NUM_PIECES = 9

  /** Row and column (0-based) for a linear index on a square grid of `pieceCount` pieces. */
  def rowColForIndex(index: Int, pieceCount: Int): (Int, Int) = {
    val edge = Math.sqrt(pieceCount).toInt
    (index / edge, index % edge)
  }
}

/**
  * A list of fit puzzle pieces (initially empty) for a numTotal sized puzzle.
  * @param pieces the currently fit pieces. May be be less than numTotal of them, but not more,
  * @param numTotal the total number of pieces in the puzzle.
  * @author Barry Becker
  */
case class PieceList(pieces: List[OrientedPiece], numTotal: Int) {

  require(numTotal == 4 || numTotal == 9, s"$numTotal is not a valid number of pieces")
  val edgeLength: Int = Math.sqrt(numTotal).toInt


  def this(pieces: Array[Piece]) = {
    this(pieces.map(p => OrientedPiece(p)).toList, pieces.length)
  }

  def this(numTotal: Int = PieceList.DEFAULT_NUM_PIECES) = {
    this(List[OrientedPiece](), numTotal)
  }

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

  def isEmpty: Boolean = pieces.isEmpty

  /** @return new piece list with 2 indicated pieces swapped. */
  def doSwap(p1Pos: Int, p2Pos: Int): PieceList = {
    require(
      p1Pos >= 0 && p1Pos < numTotal && p2Pos >= 0 && p2Pos < numTotal,
      s"Swap indices must be in [0, $numTotal); got $p1Pos, $p2Pos"
    )
    PieceList(pieces.updated(p2Pos, pieces(p1Pos)).updated(p1Pos, pieces(p2Pos)), numTotal)
  }

  /** @param p piece to add to the end of the list. */
  def add(p: OrientedPiece): PieceList = add(pieces.size, p)

  /** @param position the (1 based) position to add the piece.
    * @param p piece to add at the specified position in the list.
    */
  def add(position: Int, p: OrientedPiece): PieceList = {
    require(size < numTotal, "Cannot add another piece when already at max of " + numTotal)
    val (front, back) = pieces.splitAt(position)
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
  def shuffle(rnd: Random = MathUtil.RANDOM): PieceList = {
    val pieceList = for (p <- pieces) yield p.rotate(rnd.nextInt(4))
    PieceList(rnd.shuffle(pieceList), numTotal)
  }

  /** @param piece the piece to try to fit into our current solution (by checking the next available position).
    * @return true if the piece fits.
    */
  def fits(piece: OrientedPiece): Boolean = {
    val numSolved = size
    val col = numSolved % edgeLength
    val row = numSolved / edgeLength
    val leftOk = col == 0 || nubsFitHorizontally(getLast, piece)
    val topOk = row == 0 || nubsFitVertically(get(numSolved - edgeLength), piece)
    leftOk && topOk
  }

  /** @return the number of matches for the nubs on this piece (at most 4) */
  def getNumFits(pos: Int): Int = {
    assert(pos < numTotal)
    val row = pos / edgeLength
    val col = pos % edgeLength
    val piece = get(pos)
    var count = 0
    if col > 0 && nubsFitHorizontally(get(pos - 1), piece) then count += 1
    if row > 0 && nubsFitVertically(get(pos - edgeLength), piece) then count += 1
    if col < edgeLength - 1 && nubsFitHorizontally(piece, get(pos + 1)) then count += 1
    if row < edgeLength - 1 && nubsFitVertically(piece, get(pos + edgeLength)) then count += 1
    count
  }

  /** @return true if we contain p */
  def contains(p: Piece): Boolean = pieces.exists(_.piece == p)

  override def toString: String = {
    val buf = new StringBuilder("PieceList: (" + size + " pieces)\n")
    for (p <- pieces) buf.append(p.toString).append('\n')
    buf.toString
  }

  private def nubsFitHorizontally(left: OrientedPiece, right: OrientedPiece): Boolean =
    left.getRightNub.fitsWith(right.getLeftNub)

  private def nubsFitVertically(above: OrientedPiece, below: OrientedPiece): Boolean =
    above.getBottomNub.fitsWith(below.getTopNub)
}
