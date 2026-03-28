// Copyright by Barry G. Becker, 2017 - 2025. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path

import com.barrybecker4.puzzle.tantrix.TantrixTstUtil.*
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
}
