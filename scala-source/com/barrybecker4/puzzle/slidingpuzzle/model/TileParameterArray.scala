// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.model

import com.barrybecker4.math.MathUtil
import com.barrybecker4.optimization.parameter.{Move, PermutedParameterArray, Swap, SwapSequence}
import com.barrybecker4.optimization.parameter.types.{IntegerParameter, Parameter}
import TileParameterArray._

import scala.util.Random

object TileParameterArray {

  /** Population size scales with the square of the board so genetic search has room to explore larger puzzles. */
  private def samplePopulationSize(numCells: Int): Int = Math.max(40, numCells * numCells)

  /** One parameter per cell, valued by the tile id. The numbers are unique for the library's permutation operators. */
  private def parametersFor(tiles: Tiles): IndexedSeq[Parameter] = {
    val maxId = tiles.tiles.length - 1
    tiles.tiles.map(t => IntegerParameter(t.toInt, 0, maxId, "tile")).toIndexedSeq
  }

  /** Prefer swapping cells that are not already correct; correct cells get a low probability. */
  private def findSwapProbabilities(board: SliderBoard): IndexedSeq[Double] = {
    val tiles = board.tiles.tiles
    tiles.indices.map { i =>
      if (tiles(i) == GuidedTileSwap.goalValueAt(i, board.size)) 0.1 else 1.0
    }
  }

  private def pickSwap(board: SliderBoard, rnd: Random): Swap = {
    val probabilities = findSwapProbabilities(board)
    val totalProb = probabilities.sum
    val tot = board.tiles.tiles.length
    val p1 = getIndexFromProb(totalProb * rnd.nextDouble(), probabilities, tot)
    var p2 = p1
    while (p2 == p1) {
      p2 = getIndexFromProb(totalProb * rnd.nextDouble(), probabilities, tot)
    }
    Swap(p1, p2)
  }

  private def getIndexFromProb(p: Double, probabilities: IndexedSeq[Double], numTotal: Int): Int = {
    var total: Double = 0
    var i: Int = 0
    while (total < p && i < numTotal) {
      total += probabilities(i)
      i += 1
    }
    i - 1
  }

  private def boardWithSwaps(board: SliderBoard, swaps: Seq[Swap]): SliderBoard = {
    val tiles = board.tiles.tiles.toArray
    swaps.foreach { swap =>
      val tmp = tiles(swap.first)
      tiles(swap.first) = tiles(swap.second)
      tiles(swap.second) = tmp
    }
    SliderBoard(Tiles(tiles.toIndexedSeq))
  }
}

/**
  * The parameter array used when searching (with bb4-optimization) for a sliding-puzzle layout that matches the goal.
  * Each parameter is a cell's tile id; neighbors are made by swapping cells.
  */
class TileParameterArray(val board: SliderBoard, rnd: Random = MathUtil.RANDOM)
  extends PermutedParameterArray(TileParameterArray.parametersFor(board.tiles), rnd) {

  override def getSamplePopulationSize: Int = samplePopulationSize(board.tiles.tiles.length)

  override def getRandomNeighbor(radius: Double): TileParameterArray = applyMove(proposeMove(radius))

  override def supportsMoves: Boolean = true

  override def supportsLocalMoves: Boolean = true

  override def proposeMove(radius: Double): Move = {
    val numSwaps: Int = Math.max(1.0, 3.0 * radius).toInt
    val (_, swaps) = (0 until numSwaps).foldLeft((board, Vector.empty[Swap])) { case ((current, done), _) =>
      val swap = pickSwap(current, rnd)
      (boardWithSwaps(current, Vector(swap)), done :+ swap)
    }
    SwapSequence(swaps)
  }

  override def applyMove(move: Move): TileParameterArray = move match {
    case SwapSequence(swaps) => new TileParameterArray(boardWithSwaps(board, swaps), rnd)
    case swap: Swap => new TileParameterArray(boardWithSwaps(board, Vector(swap)), rnd)
    case other => throw new UnsupportedOperationException("A sliding board does not understand the move " + other)
  }

  /** A swap that puts the correct tile into a misplaced cell, or None if already solved. */
  def proposeGuidedMove(guideRnd: Random): Option[Move] =
    GuidedTileSwap.find(board, guideRnd).map(swap => SwapSequence(Vector(swap)))

  /** Cells whose tile is not the goal value; used by min-conflicts and tabu search. */
  def conflictedPositions: Seq[Int] = GuidedTileSwap.conflictedIndices(board)

  override def getRandomSample: TileParameterArray = {
    val shuffled = rnd.shuffle(board.tiles.tiles.toList).toIndexedSeq
    new TileParameterArray(SliderBoard(Tiles(shuffled)), rnd)
  }

  override def setPermutation(indices: List[Int]): TileParameterArray = {
    val tiles = board.tiles.tiles
    val newTiles = indices.map(i => tiles(i)).toIndexedSeq
    new TileParameterArray(SliderBoard(Tiles(newTiles)), rnd)
  }

  override def forkWithRnd(newRnd: Random): TileParameterArray = new TileParameterArray(board, newRnd)

  /** Rebuild the board from the child's tile-id parameters after order crossover. */
  override def rebuildAfterOrderCrossover(childParams: IndexedSeq[Parameter], rnd: Random): PermutedParameterArray = {
    val newTiles = childParams.map(p => p.getValue.round.toByte).toIndexedSeq
    new TileParameterArray(SliderBoard(Tiles(newTiles)), rnd)
  }

  def getBoard: SliderBoard = board

  override def size: Int = board.tiles.tiles.length

  override def toString: String = board.toString

  override def toCSVString: String = board.tiles.tiles.mkString(",")

  override def canEqual(other: Any): Boolean = other.isInstanceOf[TileParameterArray]

  override def equals(other: Any): Boolean = other match {
    case that: TileParameterArray =>
      super.equals(that) && that.canEqual(this) && board == that.board
    case _ => false
  }

  override def hashCode: Int = if (board != null) board.hashCode else 0
}
