// Copyright by Barry G. Becker, 2017 - 2025. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path

import com.barrybecker4.common.geometry.ByteLocation
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil.*
import com.barrybecker4.puzzle.tantrix.model.{HexTiles, Rotation, TantrixBoard, TilePlacement}
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

class PathPermutationGeneratorSuite extends AnyFunSuite {

  test("getRandomNeighbor returns a valid primary path of same length") {
    val board = place3UnsolvedTiles
    val path = TantrixPath(board.tantrix, board.primaryColor, board.numTiles, Random(42))
    val gen = PathPermutationGenerator(path, Random(42))
    val nbr = gen.getRandomNeighbor(0.35).asInstanceOf[TantrixPath]
    assert(nbr.size == path.size)
    assert(TantrixPath.hasOrderedPrimaryPath(nbr.tiles, nbr.primaryPathColor))
    assert(nbr.desiredLength == path.desiredLength)
  }

  test("getRandomNeighbor with large radius still yields valid path") {
    val board = place3UnsolvedTiles
    val path = TantrixPath(board.tantrix, board.primaryColor, board.numTiles, Random(11))
    val gen = PathPermutationGenerator(path, Random(11))
    val nbr = gen.getRandomNeighbor(0.55).asInstanceOf[TantrixPath]
    assert(nbr.primaryPathColor == board.primaryColor)
    assert(TantrixPath.hasOrderedPrimaryPath(nbr.tiles, board.primaryColor))
  }

  test("candidate identity is the tiles, so a copy is the same candidate and a different path is not") {
    val board = place3UnsolvedTiles
    val path = TantrixPath(board.tantrix, board.primaryColor, board.numTiles, Random(42))
    val copy = TantrixPath(path.tiles, path.primaryPathColor, path.desiredLength, Random(7))
    assert(path.candidateIdentity == copy.candidateIdentity)
    val others = (1 to 20).map(seed => PathPermutationGenerator(path, Random(seed)).getRandomNeighbor(0.55).asInstanceOf[TantrixPath])
    others.foreach { nbr =>
      assert((nbr.candidateIdentity == path.candidateIdentity) == (nbr.tiles == path.tiles))
    }
  }

  /** The 12 tiles almost form a ring, so the changes that the small radius makes lay tiles on top of each other. */
  test("when every change would overlap, the neighbor is the path itself instead of an error") {
    val tiles = new HexTiles().createOrderedList(12)
    val color = new TantrixBoard(tiles).primaryColor
    // (tile number, row, column, rotation)
    val placements = Seq((1, 25, 27, 0), (4, 26, 26, 2), (7, 26, 25, 5), (9, 25, 25, 4), (10, 25, 24, 5), (12, 24, 24, 0),
      (8, 24, 25, 1), (6, 23, 26, 4), (2, 22, 26, 3), (11, 23, 27, 5), (5, 24, 26, 2), (3, 24, 27, 4))
      .map { case (num, row, col, rot) => TilePlacement(tiles(num - 1), new ByteLocation(row, col), Rotation.values(rot)) }
    val path = new TantrixPath(placements, color, 12)
    for (seed <- 1 to 50) {
      val nbr = PathPermutationGenerator(path, Random(seed)).getRandomNeighbor(0.05).asInstanceOf[TantrixPath]
      assert(nbr.size == 12)
      assert(TantrixPath.hasOrderedPrimaryPath(nbr.tiles, color))
    }
  }
}
