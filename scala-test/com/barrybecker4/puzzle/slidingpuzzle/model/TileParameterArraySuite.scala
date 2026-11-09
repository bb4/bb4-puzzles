// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.model

import com.barrybecker4.optimization.parameter.{ParameterCrossover, Swap, SwapSequence}
import com.barrybecker4.puzzle.slidingpuzzle.solver.SliderTestSupport.boardOf
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

class TileParameterArraySuite extends AnyFunSuite {

  private val scrambled3 = boardOf(Seq(8, 6, 7, 2, 5, 4, 3, 0, 1))
  private val solved3 = boardOf((1 to 8) :+ 0)

  test("there is one parameter per cell, valued by tile id") {
    val params = new TileParameterArray(scrambled3, new Random(1))
    assertResult(9)(params.size)
    assertResult(scrambled3.tiles.tiles.map(_.toInt).toSet)((0 until params.size).map(i => params.get(i).getValue.toInt).toSet)
  }

  test("a random neighbor keeps the same multiset of tiles") {
    val params = new TileParameterArray(scrambled3, new Random(2))
    val neighbor = params.getRandomNeighbor(1.0)
    assertResult(scrambled3.tiles.tiles.sorted)(neighbor.getBoard.tiles.tiles.sorted)
    assert(neighbor.getBoard != scrambled3)
  }

  test("applying a swap exchanges two cells") {
    val params = new TileParameterArray(scrambled3, new Random(1))
    val swapped = params.applyMove(Swap(0, 1))
    assertResult(scrambled3.tiles.tiles(1))(swapped.getBoard.tiles.tiles(0))
    assertResult(scrambled3.tiles.tiles(0))(swapped.getBoard.tiles.tiles(1))
  }

  test("conflicted positions are the cells that are not home") {
    val params = new TileParameterArray(scrambled3, new Random(1))
    assert(params.conflictedPositions.nonEmpty)
    assertResult(Seq.empty)(new TileParameterArray(solved3, new Random(1)).conflictedPositions)
  }

  test("a guided move swaps two different cells on a scrambled board") {
    val params = new TileParameterArray(scrambled3, new Random(3))
    val move = params.proposeGuidedMove(new Random(3))
    assert(move.isDefined)
    move.get match {
      case SwapSequence(swaps) =>
        assertResult(1)(swaps.length)
        assert(swaps.head.first != swaps.head.second)
      case other => fail(s"expected SwapSequence, got $other")
    }
  }

  test("a solved board has no guided move") {
    assertResult(None)(new TileParameterArray(solved3, new Random(1)).proposeGuidedMove(new Random(1)))
  }

  test("setPermutation reorders the cells") {
    val params = new TileParameterArray(scrambled3, new Random(1))
    val reversed = params.setPermutation((0 until 9).toList.reverse)
    assertResult(scrambled3.tiles.tiles.reverse)(reversed.getBoard.tiles.tiles)
  }

  test("order crossover gives a full permutation of the tiles") {
    val a = new TileParameterArray(scrambled3, new Random(1))
    val b = a.getRandomSample
    for (seed <- 1 to 30) {
      val child = ParameterCrossover.cross(a, b, new Random(seed)).asInstanceOf[TileParameterArray]
      assertResult(9)(child.size)
      assertResult((0 to 8).toList)(child.getBoard.tiles.tiles.map(_.toInt).sorted.toList)
    }
  }

  test("forking keeps the board") {
    val params = new TileParameterArray(scrambled3, new Random(1))
    val forked = params.forkWithRnd(new Random(7))
    assert(forked.isInstanceOf[TileParameterArray])
    assertResult(scrambled3)(forked.getBoard)
  }

  test("a random sample is a permutation of the same tiles") {
    val params = new TileParameterArray(scrambled3, new Random(5))
    val sample = params.getRandomSample
    assertResult(scrambled3.tiles.tiles.sorted)(sample.getBoard.tiles.tiles.sorted)
  }
}
