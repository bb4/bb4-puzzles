// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.model

import com.barrybecker4.optimization.parameter.{Swap, SwapSequence}
import com.barrybecker4.puzzle.redpuzzle.solver.FitnessFinder
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

class GuidedSwapSuite extends AnyFunSuite {

  private val finder = new FitnessFinder
  private val pieces = new PieceList(PieceLists.INITIAL_PIECES_4)

  /** Every order and turn of the 4 pieces, so the solved layouts can be found without a solver. */
  private val everyLayout: Seq[PieceList] =
    pieces.pieces.permutations.flatMap { order =>
      (0 until 256).map { turns =>
        PieceList(order.zipWithIndex.map { case (p, i) => p.rotate((turns >> (2 * i)) & 3) }, 4)
      }
    }.toSeq

  /** The 4 neighboring pairs of a 2 by 2 puzzle all fit; each pair counts for both of its pieces. */
  private def allFit(list: PieceList): Boolean = (0 until 4).map(list.getNumFits).sum == 8

  private val solved = everyLayout.find(allFit)

  test("a puzzle with the pieces in place has no loose edge, so there is nothing to guide") {
    assert(solved.isDefined, "the 4 piece puzzle should have a solution")
    assertResult(None)(GuidedSwap.find(solved.get, new Random(1)))
  }

  private val unsolved = everyLayout.filter(!allFit(_)).take(200)

  test("a guided swap exchanges two different positions") {
    val swaps = unsolved.flatMap(GuidedSwap.find(_, new Random(2)))
    assert(swaps.nonEmpty, "some layouts should have a piece that fits somewhere else")
    assert(swaps.forall(s => s.first != s.second && s.first >= 0 && s.second >= 0 && s.first < 4 && s.second < 4))
  }

  test("the piece swapped in can be turned to fit the piece it is put beside") {
    val fitsAfter = unsolved.flatMap(list => GuidedSwap.find(list, new Random(3)).map(list -> _)).map { case (list, swap) =>
      val moved = list.doSwap(swap.first, swap.second)
      (0 until 4).exists(turns => moved.rotate(swap.first, turns).getNumFits(swap.first) > 0)
    }
    assert(fitsAfter.nonEmpty && fitsAfter.forall(identity))
  }

  test("the same random numbers give the same guided swap") {
    val list = unsolved.head
    assertResult(GuidedSwap.find(list, new Random(5)))(GuidedSwap.find(list, new Random(5)))
  }
}
