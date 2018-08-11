// Copyright by Barry G. Becker, 2017-2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.model

import com.barrybecker4.common.math.MathUtil
import com.barrybecker4.optimization.parameter.{ParameterArray, PermutedParameterArray}

import scala.util.Random


object PieceParameterArray {

  /** Number of random puzzles in a generation population */
  private val SAMPLE_POPULATION_SIZE = 500


  /** Exchange 2 pieces, even if it means the fitness gets worse.
    * Skew away from selecting pieces that have fits.
    * The probability of selecting pieces that already have fits is sharply reduced.
    * The denominator is 1 + the number of fits that the piece has.
    */
  private def doPieceSwap(pieces: PieceList, rnd: Random): PieceList = {
    val swapProbabilities: IndexedSeq[Double] = findSwapProbabilities(pieces)
    var totalProb: Double = 0

    for (i <- 0 until pieces.numTotal) totalProb += swapProbabilities(i)

    val tot = pieces.numTotal
    val p1: Int = getPieceFromProb(totalProb * rnd.nextDouble, swapProbabilities, tot)
    var p2: Int = 0

    do {
      p2 = getPieceFromProb(totalProb * rnd.nextDouble, swapProbabilities, tot)
    } while (p2 == p1)

    pieces.doSwap(p1, p2)
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
    for (i <- 0 until pieces.numTotal) yield 2.0 / (2.0 + pieces.getNumFits(i))
}

/**
  * The parameter array to use when searching (using optimization) to find a red puzzle solution.
  * It has some unique properties.
  * For example, when finding a random neighbor, we consider rotations of
  * non-fitting pieces rather than just offsetting the number by some random amount.
  * @author Barry Becker
  */
class PieceParameterArray(var pieces: PieceList, rnd: Random = MathUtil.RANDOM) extends PermutedParameterArray(rnd) {


  override def getSamplePopulationSize: Int = PieceParameterArray.SAMPLE_POPULATION_SIZE

  /** We want to find a potential solution close to the one that we have,
    * with minimal disturbance of the pieces that are already fit.
    * @param radius proportional to the number of pieces that you want to vary.
    * @return the random nbr (potential solution).
    */
  override def getRandomNeighbor(radius: Double): PieceParameterArray = {

    var pieceList: PieceList = new PieceList(pieces)
    val numSwaps: Int = Math.max(1.0, 3.0 * radius ).toInt
    println(s"numSwaps = $numSwaps rad= $radius   orig piecelist:")
    //println(pieceList.toString)

    for (i <- 0 until numSwaps)
      pieceList = doPieceSwap(pieceList, rnd)

    assert(pieceList.size == pieceList.numTotal)

    // Make a pass over all the pieces. If rotating a piece leads to more fits, then do it.
    for (k <- 0 until pieceList.size) {
      var numFits: Int = pieceList.getNumFits(k)
      var bestNumFits: Int = numFits
      var bestRot: Int = 0
      for (i <- 1 to 3) {
        val plist = pieceList.rotate(k, i)
        numFits = plist.getNumFits(k)
        if (numFits > bestNumFits) {
          bestNumFits = numFits
          bestRot = i
        }
      }
      // rotate the piece to position of best fit.
      pieceList = pieceList.rotate(k, bestRot)
    }
    new PieceParameterArray(pieceList, rnd)
  }

  /** @return get a completely random solution in the parameter space.*/
  override def getRandomSample: PieceParameterArray = {
    val pl: PieceList = new PieceList(pieces)
    val shuffledPieces: PieceList = pl.shuffle(rnd)
    new PieceParameterArray(shuffledPieces, rnd)
  }

  override def setPermutation(indices: List[Int]): PieceParameterArray = {
    var newParams: PieceList = PieceList(List[OrientedPiece](), pieces.numTotal) //pieces
    indices.foreach(p => newParams = newParams.add(pieces.get(p)))
    new PieceParameterArray(newParams, rnd)
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
        (that canEqual this) &&
        pieces == that.pieces
    case _ => false
  }

  override def hashCode: Int = if (pieces != null) pieces.hashCode else 0
}