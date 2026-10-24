// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path

import com.barrybecker4.optimization.parameter.Move
import com.barrybecker4.puzzle.tantrix.model.{HexTiles, TantrixBoard}
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

class GuidedPivotSuite extends AnyFunSuite {

  /** The random path generator is not seeded, so a path is made once per seed and copied, with a fresh generator. */
  private val firstPaths = scala.collection.mutable.Map.empty[Long, TantrixPath]

  private def randomPath(numTiles: Int, seed: Long): TantrixPath = {
    val first = firstPaths.getOrElseUpdate(seed,
      new TantrixPath(new TantrixBoard(new HexTiles().createOrderedList(numTiles)), new Random(seed)))
    new TantrixPath(first.tiles, first.primaryPathColor, first.desiredLength, new Random(seed))
  }

  private def tileNumbers(path: TantrixPath): Set[Int] = path.tiles.map(_.tile.tantrixNumber.toInt).toSet

  test("a path supports moves") {
    assert(randomPath(9, 1).supportsMoves)
  }

  for (radius <- Seq(0.05, 0.2, 0.6))
    test(s"applying the proposed move gives the random neighbor (radius $radius)") {
      (1 to 10).foreach { seed =>
        val viaMove = randomPath(9, seed).applyMove(randomPath(9, seed).proposeMove(radius))
        val direct = randomPath(9, seed).getRandomNeighbor(radius).asInstanceOf[TantrixPath]
        assertResult(direct.tiles)(viaMove.tiles)
      }
    }

  test("a move that is not a path is refused") {
    intercept[UnsupportedOperationException](randomPath(9, 1).applyMove(new Move {}))
  }

  test("a guided neighbor is a valid primary path through the same tiles") {
    val neighbors = (1 to 40).flatMap(seed => GuidedPivot.find(randomPath(9, seed), new Random(seed)).map(seed -> _))
    assert(neighbors.size > 10, "most random paths have a tile that does not fit and can be rearranged")
    neighbors.foreach { case (seed, nbr) =>
      val path = randomPath(9, seed)
      assertResult(path.size)(nbr.size)
      assertResult(tileNumbers(path))(tileNumbers(nbr))
      assert(TantrixPath.hasOrderedPrimaryPath(nbr.tiles, nbr.primaryPathColor))
    }
  }

  test("a guided neighbor differs from the path it came from") {
    val same = (1 to 40).flatMap(seed => GuidedPivot.find(randomPath(9, seed), new Random(seed)).filter(_.tiles == randomPath(9, seed).tiles))
    assert(same.isEmpty)
  }

  test("the same random numbers give the same guided neighbor") {
    val path = randomPath(9, 3)
    assertResult(GuidedPivot.find(path, new Random(5)).map(_.tiles))(GuidedPivot.find(path, new Random(5)).map(_.tiles))
  }

  test("the guided move is the same neighbor, as a move") {
    val path = randomPath(9, 3)
    val move = path.proposeGuidedMove(new Random(5))
    assertResult(GuidedPivot.find(path, new Random(5)).map(_.tiles))(move.map(m => path.applyMove(m).tiles))
  }
}
