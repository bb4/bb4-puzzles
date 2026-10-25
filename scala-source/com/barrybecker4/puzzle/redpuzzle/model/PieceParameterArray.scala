// Copyright by Barry G. Becker, 2017-2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.model

import com.barrybecker4.math.MathUtil
import com.barrybecker4.optimization.parameter.{Move, PermutedParameterArray, Swap, SwapSequence}
import com.barrybecker4.optimization.parameter.types.{IntegerParameter, Parameter}
import PieceParameterArray._
import scala.util.Random


object PieceParameterArray {

  /** Number of random puzzles in a generation population */
  private val SAMPLE_POPULATION_SIZE = 400

  /** One parameter per piece, valued by the piece number. The numbers are unique, which is what the library's
    * permutation operators (order crossover, distance) need. Orientation is deliberately not part of the value.
    */
  private def parametersFor(pieces: PieceList): IndexedSeq[Parameter] =
    pieces.pieces.map(p => IntegerParameter(p.piece.pieceNumber, 1, 9, "piece")).toIndexedSeq

  /** Pick 2 pieces to exchange, even if it means the fitness gets worse.
    * Skew away from selecting pieces that have fits.
    * The probability of selecting pieces that already have fits is sharply reduced.
    * The denominator is 1 + the number of fits that the piece has.
    */
  private def pickSwap(pieces: PieceList, rnd: Random): Swap = {
    val swapProbabilities: IndexedSeq[Double] = findSwapProbabilities(pieces)
    val totalProb = swapProbabilities.sum
    val tot = pieces.numTotal
    val p1: Int = getPieceFromProb(totalProb * rnd.nextDouble(), swapProbabilities, tot)
    var p2: Int = 0

    while (p2 == p1) {
      p2 = getPieceFromProb(totalProb * rnd.nextDouble(), swapProbabilities, tot)
    }
    Swap(p1, p2)
  }

  /** @param p some value between 0 and the totalProbability (i.e. 100%).
    * @return the index of the piece that was selected given the probability.
    */
  private def getPieceFromProb(p: Double, probabilities: IndexedSeq[Double], numTotal: Int): Int = {
    var total: Double = 0
    var i: Int = 0
    while (total < p && i < numTotal) {
      total += probabilities(i)
      i += 1
    }
    i - 1
  }

  /** @param pieces piece list to find probabilities for.
    * @return probability used to determine if we do a piece swap.
    *         Pieces that already fit have a lower probability of being swapped.
    */
  private def findSwapProbabilities(pieces: PieceList): IndexedSeq[Double] =
    for (i <- 0 until pieces.numTotal) yield 1.0 / (1.0 + pieces.getNumFits(i))

  /** After swaps, rotate each piece to the rotation that maximizes local nub fits. */
  private[model] def rotateEachPieceForBestLocalFits(pieceList: PieceList): PieceList = {
    var pl = pieceList
    for (k <- 0 until pl.size)
      pl = rotateSinglePieceForBestFit(pl, k)
    pl
  }

  private def rotateSinglePieceForBestFit(pieceList: PieceList, k: Int): PieceList = {
    var bestNumFits = pieceList.getNumFits(k)
    var bestRot = 0
    for (i <- 1 to 3) {
      val numFits = pieceList.rotate(k, i).getNumFits(k)
      if (numFits > bestNumFits) {
        bestNumFits = numFits
        bestRot = i
      }
    }
    pieceList.rotate(k, bestRot)
  }
}

/**
  * The parameter array to use when searching (using optimization) to find a red puzzle solution.
  * It has some unique properties.
  * For example, when finding a random neighbor, we consider rotations of
  * non-fitting pieces rather than just offsetting the number by some random amount.
  */
class PieceParameterArray(var pieces: PieceList, rnd: Random = MathUtil.RANDOM) extends PermutedParameterArray(PieceParameterArray.parametersFor(pieces), rnd) {

  override def getSamplePopulationSize: Int = SAMPLE_POPULATION_SIZE

  /** We want to find a potential solution close to the one that we have,
    * with minimal disturbance of the pieces that are already fit.
    * @param radius proportional to the number of pieces that you want to vary.
    * @return the random nbr (potential solution).
    */
  override def getRandomNeighbor(radius: Double): PieceParameterArray = applyMove(proposeMove(radius))

  /** The neighbor is described by its swaps, so that a strategy can ask for the fitness after it without building it. */
  override def supportsMoves: Boolean = true

  /** The swaps that [[getRandomNeighbor]] makes. Each is picked in the list as the earlier ones left it. */
  override def proposeMove(radius: Double): Move = {
    val numSwaps: Int = Math.max(1.0, 3.0 * radius).toInt
    val (_, swaps) = (0 until numSwaps).foldLeft((pieces, Vector.empty[Swap])) { case ((list, done), _) =>
      val swap = pickSwap(list, rnd)
      (list.doSwap(swap.first, swap.second), done :+ swap)
    }
    SwapSequence(swaps)
  }

  /** Swap the pieces, then rotate each to the rotation that maximizes local nub fits. */
  override def applyMove(move: Move): PieceParameterArray = move match {
    case SwapSequence(swaps) => applySwaps(swaps)
    case swap: Swap => applySwaps(Vector(swap))
    case other => throw new UnsupportedOperationException("A piece list does not understand the move " + other)
  }

  private def applySwaps(swaps: Seq[Swap]): PieceParameterArray = {
    val swapped = swaps.foldLeft(pieces)((list, swap) => list.doSwap(swap.first, swap.second))
    assert(swapped.size == swapped.numTotal)
    new PieceParameterArray(rotateEachPieceForBestLocalFits(swapped), rnd)
  }

  /** A swap that puts a piece where it would fit its neighbor, in place of one that does not; None if there is none. */
  def proposeGuidedMove(guideRnd: Random): Option[Move] = GuidedSwap.find(pieces, guideRnd).map(swap => SwapSequence(Vector(swap)))

  /** @return get a completely random solution in the parameter space.*/
  override def getRandomSample: PieceParameterArray = {
    val shuffledPieces: PieceList = pieces.shuffle(rnd)
    new PieceParameterArray(shuffledPieces, rnd)
  }

  override def setPermutation(indices: List[Int]): PieceParameterArray = {
    val newPieces = indices.map(i => pieces.get(i))
    new PieceParameterArray(PieceList(newPieces, pieces.numTotal), rnd)
  }

  /** Keep the piece list (not just the parameters) when multistart searches re-seed the random generator. */
  override def forkWithRnd(newRnd: Random): PieceParameterArray = new PieceParameterArray(pieces, newRnd)

  /**
    * Order crossover only sees piece numbers, so rebuild the pieces from them.
    * Each piece keeps the orientation it has in this parent, and then every piece is rotated to the orientation
    * that gives it the most matching nubs, as in [[getRandomNeighbor]].
    */
  override def rebuildAfterOrderCrossover(childParams: IndexedSeq[Parameter], rnd: Random): PermutedParameterArray = {
    val byNumber = pieces.pieces.map(p => p.piece.pieceNumber -> p).toMap
    val childPieces = childParams.map(param => byNumber(param.getValue.round.toInt)).toList
    new PieceParameterArray(rotateEachPieceForBestLocalFits(PieceList(childPieces, pieces.numTotal)), rnd)
  }

  /** @return the piece list corresponding to the encoded parameter array */
  def getPieceList: PieceList = pieces

  /** @return the number of parameters in the array.*/
  override def size: Int = pieces.size

  override def toString: String = pieces.toString

  /** @return the parameters in a string of Comma Separated Values. */
  override def toCSVString: String = toString

  override def canEqual(other: Any): Boolean = other.isInstanceOf[PieceParameterArray]

  override def equals(other: Any): Boolean = other match {
    case that: PieceParameterArray =>
      super.equals(that) &&
        that.canEqual(this) &&
        pieces == that.pieces
    case _ => false
  }

  override def hashCode: Int = if (pieces != null) pieces.hashCode else 0
}
