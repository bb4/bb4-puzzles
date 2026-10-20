// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path.permuting

import com.barrybecker4.puzzle.tantrix.PathTstUtil.*
import com.barrybecker4.puzzle.tantrix.solver.path.{PathVerificationCase, TantrixPath}
import org.scalatest.funsuite.AnyFunSuite

/**
  * Whatever pivots are chosen, the permuted paths must contain exactly the same tiles as the original path.
  * They may be arranged differently, but no tiles may be lost, duplicated, or stacked on top of each other.
  */
class PathPivotPermuterInvariantsSuite extends AnyFunSuite {

  /** The permuter needs each tile's primary path to lead to the next tile and the next tile's back again.
    * TantrixPath only requires the connection in one direction, and some test paths (like NON_LOOP_PATH5)
    * are connected that way only, so they cannot be permuted.
    */
  private def isConnectedBothWays(path: TantrixPath): Boolean =
    path.tiles.sliding(2).forall {
      case Seq(a, b) => a.hasPathTo(path.primaryPathColor, b.location) && b.hasPathTo(path.primaryPathColor, a.location)
      case _ => true
    }

  private val paths: Seq[TantrixPath] =
    PathVerificationCase.cases.map(_.path).filter(path => path.size >= 3 && isConnectedBothWays(path))

  private def tileSet(path: TantrixPath) = path.tiles.map(_.tile).toSet

  test("most of the test paths can be used for the invariants") {
    assert(paths.size >= PathVerificationCase.cases.size - 2, s"only ${paths.size} paths are connected both ways")
  }

  test("permuted paths have the same tiles as the original, at distinct locations, and there are at most 7") {
    for (path <- paths; lower <- 1 to path.size - 2; upper <- lower to path.size - 2) {
      val permuter = new PathPivotPermuter(path)
      val permuted = permuter.findPermutedPaths(lower, upper)
      val clue = s"pivots ($lower, $upper) of path with ${path.size} tiles"
      assert(permuted.size <= 7, clue)
      for (p <- permuted) {
        assertResult(path.size, clue) { p.size }
        assertResult(tileSet(path), clue) { tileSet(p) }
        assertResult(path.size, clue) { p.tiles.map(_.location).toSet.size }
        assertResult(path.desiredLength, clue) { p.desiredLength }
      }
    }
  }

  test("the order of the pivot indices does not matter") {
    val path = LOOP_PATH5
    val forward = new PathPivotPermuter(path).findPermutedPaths(1, 3).map(_.tiles)
    val backward = new PathPivotPermuter(path).findPermutedPaths(3, 1).map(_.tiles)
    assertResult(forward) { backward }
  }
}
