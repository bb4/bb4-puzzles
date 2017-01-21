// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.model

import com.barrybecker4.common.math.MathUtil
import com.barrybecker4.optimization.parameter.ParameterArray
import com.barrybecker4.optimization.parameter.PermutedParameterArray

/**
  * The parameter array to use when searching (using optimization) to find a red puzzle solution.
  * It has some unique properties.
  * For example, when finding a random neighbor, we consider rotations of
  * non-fitting pieces rather than just offsetting the number by some random amount.
  *
  * @author Barry Becker
  */
object PieceParameterArray {
  private val SAMPLE_POPULATION_SIZE = 400

  /**
    * @param pieces piece list to find probabilities for.
    * @return probability used to determine if we do a piece swap.
    *         Pieces that already fit have a low probability of being swapped.
    */
  private def findSwapProbabilities(pieces: PieceList): IndexedSeq[Double] =
    for (i <- 0 until pieces.numTotal) yield 1.0 / (1.0 + pieces.getNumFits(i))
}

class PieceParameterArray(var pieces: PieceList) extends PermutedParameterArray {

  override def copy: PieceParameterArray = {
    val copy: PieceParameterArray = new PieceParameterArray(pieces)
    copy.setFitness(this.getFitness)
    copy
  }

  override def getSamplePopulationSize: Int = PieceParameterArray.SAMPLE_POPULATION_SIZE

  /**
    * We want to find a potential solution close to the one that we have,
    * with minimal disturbance of the pieces that are already fit.
    *
    * @param radius proportional to the number of pieces that you want to vary.
    * @return the random nbr (potential solution).
    */
  override def getRandomNeighbor(radius: Double): PermutedParameterArray = {
    val pieceList: PieceList = new PieceList(pieces)
    val numSwaps: Int = 1 //Math.max(1, (int) (rad * 2.0));

    for (i <- 0 until numSwaps) doPieceSwap(pieceList)

    assert (pieceList.size == pieceList.numTotal)

    // Make a pass over all the pieces. If rotating a piece leads to more fits, then do it.
    for (k <- 0 until pieceList.size) {
      var numFits: Int = pieceList.getNumFits(k)
      var bestNumFits: Int = numFits
      var bestRot: Int = 1
      for (i <- 0 until 3) {
        pieceList.rotate (k, 1) // fix
        numFits = pieceList.getNumFits(k)
        if (numFits > bestNumFits) {
          bestNumFits = numFits
          bestRot = 2 + i
        }
      }
      // rotate the piece to position of best fit.
      pieceList.rotate(k, bestRot)
    }
    new PieceParameterArray(pieceList)
  }

  /**
    * Exchange 2 pieces, even if it means the fitness gets worse.
    *
    * Skew away from selecting pieces that have fits.
    * The probability of selecting pieces that already have fits is sharply reduced.
    * The denominator is 1 + the number of fits that the piece has.
    */
  def doPieceSwap(pieces: PieceList): PieceList = {
    val swapProbabilities: IndexedSeq[Double] = PieceParameterArray.findSwapProbabilities(pieces)
    var totalProb: Double = 0

    for (i <- 0 until pieces.numTotal) {
      totalProb += swapProbabilities(i)
    }

    val p1: Int = getPieceFromProb(totalProb * MathUtil.RANDOM.nextDouble, swapProbabilities)
    var p2: Int = 0

    do {
      p2 = getPieceFromProb(totalProb * MathUtil.RANDOM.nextDouble, swapProbabilities)
    } while (p2 == p1)

    pieces.doSwap(p1, p2)
    pieces
  }

  /**
    * @param p some value between 0 and the totalProbability (i.e. 100%).
    * @return the index of the piece that was selected given the probability.
    */
  def getPieceFromProb(p: Double, probabilities: IndexedSeq[Double]): Int = {
    var total: Double = 0
    var i: Int = 0
    while (total < p && i < pieces.numTotal) {
      total += probabilities(i)
      i += 1
    }
    i - 1
  }

  /**
    * @return get a completely random solution in the parameter space.
    */
  override def getRandomSample: ParameterArray = {
    val pl: PieceList = new PieceList(pieces)
    val shuffledPieces: PieceList = pl.shuffle
    new PieceParameterArray (shuffledPieces)
  }

  override def setPermutation(indices: java.util.List[Integer]): Unit = {
    var newParams: PieceList = pieces

    val it: java.util.Iterator[Integer] = indices.iterator()
    while (it.hasNext) {
      newParams.add(pieces.get(it.next()))
    }
    pieces = newParams
  }

  /** @return the piece list corresponding to the encoded parameter array */
  def getPieceList: PieceList = pieces

  /** @return the number of parameters in the array.*/
  override def size: Int = pieces.size

  override def toString: String = pieces.toString

  /** @return the parameters in a string of Comma Separated Values. */
  override def toCSVString: String = toString
}
