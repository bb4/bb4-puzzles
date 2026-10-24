// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.model

import com.barrybecker4.optimization.parameter.{Swap, SwapSequence}
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

class PieceParameterArrayMovesSuite extends AnyFunSuite {

  private def puzzle(numPieces: Int, seed: Long) =
    new PieceParameterArray(PieceLists.getInitialPuzzlePieces(numPieces, new Random(seed)), new Random(seed))

  test("pieces support moves") {
    assert(puzzle(9, 1).supportsMoves)
  }

  for (numPieces <- Seq(4, 9); radius <- Seq(0.1, 1.0, 3.0))
    test(s"applying the proposed move gives the random neighbor ($numPieces pieces, radius $radius)") {
      (1 to 20).foreach { seed =>
        val viaMove = puzzle(numPieces, seed).applyMove(puzzle(numPieces, seed).proposeMove(radius))
        assertResult(puzzle(numPieces, seed).getRandomNeighbor(radius).pieces)(viaMove.pieces)
      }
    }

  test("a move is made of swaps, as many as the radius calls for") {
    val SwapSequence(swaps) = puzzle(9, 1).proposeMove(2.0): @unchecked
    assertResult(6)(swaps.size)
    assert(swaps.forall(s => s.first != s.second))
  }

  test("a move that is not made of swaps is refused") {
    intercept[UnsupportedOperationException](puzzle(9, 1).applyMove(new com.barrybecker4.optimization.parameter.Move {}))
  }

  test("the guided move is a single swap, and applying it keeps every piece exactly once") {
    val start = puzzle(9, 4)
    val guided = (1 to 30).flatMap(seed => puzzle(9, 4).proposeGuidedMove(new Random(seed)))
    assert(guided.nonEmpty)
    guided.foreach { move =>
      val SwapSequence(Vector(Swap(_, _))) = move: @unchecked
      assertResult((1 to 9).toSet)(start.applyMove(move).pieces.pieces.map(_.piece.pieceNumber).toSet)
    }
  }
}
