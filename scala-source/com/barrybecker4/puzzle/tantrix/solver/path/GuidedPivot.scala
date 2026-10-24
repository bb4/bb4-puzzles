// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path

import com.barrybecker4.puzzle.tantrix.model.analysis.fitting.TileFitter
import com.barrybecker4.puzzle.tantrix.solver.path.permuting.PathPivotPermuter

import scala.collection.mutable.ListBuffer
import scala.util.Random

/**
  * Finds a neighbor of a path by rearranging it where it does not fit. A random neighbor pivots the path at tiles
  * anywhere along it. This one picks a tile whose colors do not match its neighbors, and pivots only at that tile and
  * the ones on either side of it, so the change is made next to the problem. As for any neighbor, the better of the
  * rearrangements is the likelier one.
  */
object GuidedPivot {

  /** @return a rearranged path, or None if every tile fits or no rearrangement next to a loose tile is possible */
  def find(path: TantrixPath, rnd: Random): Option[TantrixPath] = {
    val loose = looseTileIndices(path)
    if (loose.isEmpty) None else rearrangeAround(path, loose(rnd.nextInt(loose.size)), rnd)
  }

  /** The indices of the tiles whose secondary colors do not match those of the tiles beside them. */
  private def looseTileIndices(path: TantrixPath): IndexedSeq[Int] = {
    val fitter = new TileFitter(path.tiles, path.primaryPathColor)
    path.tiles.indices.filterNot(i => fitter.isFit(path.tiles(i)))
  }

  /** The first and last tile of a path cannot be pivots, so a loose tile at an end is rearranged at its only neighbor. */
  private def rearrangeAround(path: TantrixPath, looseIndex: Int, rnd: Random): Option[TantrixPath] = {
    val pivots = (looseIndex - 1 to looseIndex + 1).filter(i => i >= 1 && i < path.size - 1)
    val permuter = PathPivotPermuter(path)
    val alternatives = pivots.flatMap(pivot => permuter.findPermutedPaths(pivot, pivot)).distinct
    Option.when(alternatives.nonEmpty)(new PathSelector(rnd).selectPath(ListBuffer.from(alternatives)))
  }
}
