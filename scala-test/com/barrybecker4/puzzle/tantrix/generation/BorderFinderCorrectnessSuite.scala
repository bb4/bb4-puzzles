// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.generation

import com.barrybecker4.common.geometry.{Box, Location}
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil.*
import com.barrybecker4.puzzle.tantrix.model.HexTile.NUM_SIDES
import com.barrybecker4.puzzle.tantrix.model.{HexUtil, PathColor, Tantrix, TantrixBoard}
import org.scalatest.funsuite.AnyFunSuite

/**
  * Invariants for [[BorderFinder]] (path-aware bbox vs half of target length).
  *
  * The implementation walks the primary path from [[Tantrix.lastTile]] with a working bbox that may grow
  * along the walk; each candidate empty hex must still satisfy the half-length cap for the bbox used
  * when it was discovered. A cheap global upper bound is: extend the full occupied bbox by that hex only.
  */
class BorderFinderCorrectnessSuite extends AnyFunSuite {

  private def primaryAdjacentEmpty(tantrix: Tantrix, primaryColor: PathColor): Set[Location] = {
    val acc = scala.collection.mutable.Set.empty[Location]
    for (pl <- tantrix.tiles; i <- 0 until NUM_SIDES)
      if (pl.getPathColor(i) == primaryColor) {
        val nl = HexUtil.getNeighborLocation(pl.location, i)
        if (tantrix(nl).isEmpty) acc += nl
      }
    acc.toSet
  }

  /**
    * "{{ @literal ≤ }} half" rule applied naïvely: only current placements plus one empty cell.
    * Any border returned by [[BorderFinder]] must lie in this set (algorithm is stricter or equal).
    */
  private def globalHalfBoxUpperBound(tantrix: Tantrix, numTiles: Int, primaryColor: PathColor): Set[Location] = {
    val maxHalf = (numTiles + 1) / 2
    val bbox = tantrix.getBoundingBox
    primaryAdjacentEmpty(tantrix, primaryColor).filter { loc =>
      Box(bbox, loc).getMaxDimension <= maxHalf
    }
  }

  private def assertBorderSound(tantrix: Tantrix, numTiles: Int, primary: PathColor): Unit = {
    val finder = new BorderFinder(tantrix, numTiles, primary)
    val got = finder.findBorderPositions
    val touchPrimary = primaryAdjacentEmpty(tantrix, primary)
    val globalOk = globalHalfBoxUpperBound(tantrix, numTiles, primary)
    got.foreach { loc =>
      assert(tantrix(loc).isEmpty, s"$loc must be empty (got $got)")
    }
    assert(
      got.forall(touchPrimary.contains),
      s"Border must be primary-color-adjacent empties. Extra: ${got.diff(touchPrimary)}"
    )
    assert(
      got.forall(globalOk.contains),
      s"Border must respect global half-box bound (occ ∪ {{loc}}). Extra: ${got.diff(globalOk)}"
    )
  }

  test("border is subset of primary-touching empties and global half-box bound") {
    val cases: Seq[(Tantrix, Int, PathColor)] = Seq(
      (new TantrixBoard(THREE_TILES).tantrix, 10, PathColor.YELLOW),
      (place2of3Tiles_OneThenTwo.tantrix, 10, PathColor.YELLOW),
      (place2of3Tiles_OneThenThree.tantrix, 10, PathColor.YELLOW),
      (place3SolvedTiles.tantrix, 10, PathColor.YELLOW),
      (place3of6UnsolvedTiles.tantrix, 6, PathColor.YELLOW),
      (place4UnsolvedTiles.tantrix, 4, PathColor.RED),
      (place4UnsolvedTiles.tantrix, 6, PathColor.RED),
      (place4UnsolvedTiles.tantrix, 20, PathColor.RED),
      (place4SolvedTiles.tantrix, 6, PathColor.RED),
      (place7SolvedTiles.tantrix, 8, PathColor.BLUE),
      (place10LoopWithInnerSpace.tantrix, 12, PathColor.RED),
      (place9AlmostLoop.tantrix, 16, PathColor.RED)
    )
    cases.foreach { case (t, n, c) => assertBorderSound(t, n, c) }
  }

  test("border grows monotonically with numTiles (relaxed half-length cap)") {
    val t = place4UnsolvedTiles.tantrix
    val c = PathColor.RED
    val borders = Seq(1, 2, 3, 4, 5, 7, 20).map { n =>
      new BorderFinder(t, n, c).findBorderPositions.toSet
    }
    borders.zip(borders.tail).foreach { case (small, large) =>
      assert(small.subsetOf(large), s"numTiles caps: ${small.diff(large)} would violate monotonicity")
    }
  }

  test("tight numTiles cap can eliminate all primary-adjacent empties that exceed half box") {
    val t = new TantrixBoard(THREE_TILES).tantrix
    val wide = new BorderFinder(t, 10, PathColor.YELLOW).findBorderPositions
    assert(wide.nonEmpty)
    val tight = new BorderFinder(t, 1, PathColor.YELLOW).findBorderPositions.toSet
    assert(tight.subsetOf(wide.toSet))
    assert(tight.forall(loc => Box(t.getBoundingBox, loc).getMaxDimension <= 1))
  }

  test("red wrong-loop seven tiles still yields coherent border set") {
    val t = new Tantrix(sevenTilesInAWrongRedLoop)
    assertBorderSound(t, 10, PathColor.RED)
  }
}
