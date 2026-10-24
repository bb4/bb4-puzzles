// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.model

import com.barrybecker4.optimization.parameter.Swap

import scala.util.Random

/**
  * Finds a swap that is likely to repair a place where two neighboring pieces do not fit. Pick such a place at random,
  * keep one of the two pieces where it is, and look for a piece somewhere else that fits that one, turned whichever way
  * it needs to be. Swapping that piece into the other position is a better bet than a swap of two random positions.
  */
object GuidedSwap {

  /** An edge that does not fit: the piece at `anchor` stays and the piece at `target` is the one to replace. */
  private case class LooseEdge(anchor: Int, target: Int, targetSide: Direction)

  /** @return a swap that puts a piece that fits the anchor in place of the one that does not, or None if no edge is loose or nothing fits */
  def find(pieces: PieceList, rnd: Random): Option[Swap] = {
    val loose = looseEdges(pieces)
    if (loose.isEmpty) None else swapForEdge(pieces, loose(rnd.nextInt(loose.size)), rnd)
  }

  private def swapForEdge(pieces: PieceList, edge: LooseEdge, rnd: Random): Option[Swap] = {
    val candidates = (0 until pieces.numTotal).filter(z => z != edge.anchor && z != edge.target && fitsBeside(pieces, edge, z))
    if (candidates.isEmpty) None else Some(Swap(edge.target, candidates(rnd.nextInt(candidates.size))))
  }

  /** True if the piece at `position`, turned some way, fits against the anchor on the side where the target is. */
  private def fitsBeside(pieces: PieceList, edge: LooseEdge, position: Int): Boolean =
    (0 until Direction.values.length).exists(turns => fits(pieces.get(edge.anchor), pieces.get(position).rotate(turns), edge.targetSide))

  /** @param side where the candidate sits, as seen from the anchor */
  private def fits(anchor: OrientedPiece, candidate: OrientedPiece, side: Direction): Boolean = side match {
    case Direction.RIGHT => anchor.getRightNub.fitsWith(candidate.getLeftNub)
    case Direction.LEFT => candidate.getRightNub.fitsWith(anchor.getLeftNub)
    case Direction.BOTTOM => anchor.getBottomNub.fitsWith(candidate.getTopNub)
    case Direction.TOP => candidate.getBottomNub.fitsWith(anchor.getTopNub)
  }

  /** Each neighboring pair that does not fit counts twice, because either piece can be the one to replace. */
  private def looseEdges(pieces: PieceList): IndexedSeq[LooseEdge] =
    (0 until pieces.numTotal).flatMap(position => looseEdgesBelowOrRightOf(pieces, position))

  private def looseEdgesBelowOrRightOf(pieces: PieceList, position: Int): Seq[LooseEdge] = {
    val (row, col) = PieceList.rowColForIndex(position, pieces.numTotal)
    val right = Option.when(col < pieces.edgeLength - 1 && !pieces.fitsRight(position))(position + 1 -> Direction.RIGHT)
    val below = Option.when(row < pieces.edgeLength - 1 && !pieces.fitsBelow(position))(position + pieces.edgeLength -> Direction.BOTTOM)
    (right ++ below).toSeq.flatMap { case (other, side) =>
      Seq(LooseEdge(position, other, side), LooseEdge(other, position, opposite(side)))
    }
  }

  private def opposite(side: Direction): Direction = Direction.values((side.ordinal + 2) % Direction.values.length)
}
