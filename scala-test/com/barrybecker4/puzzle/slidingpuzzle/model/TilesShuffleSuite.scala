// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.model

import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

class TilesShuffleSuite extends AnyFunSuite {

  private def shuffled(size: Int, seed: Int): Seq[Int] =
    new Tiles(size.toByte).shuffle(new Random(seed)).tiles.map(_.toInt)

  test("a shuffle has the same tiles as the solved board") {
    for (size <- 2 to 5; seed <- 1 to 10)
      assertResult((0 until size * size).toList)(shuffled(size, seed).sorted.toList)
  }

  test("a shuffle can always be solved") {
    for (size <- 2 to 6; seed <- 1 to 50)
      assert(Solvability.isSolvable(size, shuffled(size, seed)), s"size $size seed $seed")
  }

  test("the same random seed gives the same shuffle") {
    assertResult(shuffled(4, 3))(shuffled(4, 3))
  }

  test("every solvable arrangement of the 2 by 2 board is about equally likely") {
    val rnd = new Random(1)
    val counts = Seq.fill(12000)(new Tiles(2.toByte).shuffle(rnd).tiles.toList).groupBy(identity).view.mapValues(_.size)
    assertResult(12)(counts.size)
    counts.values.foreach(count => assert(count > 800 && count < 1200, s"count $count"))
  }

  test("the blank is as likely to end up in any cell") {
    val rnd = new Random(2)
    val blankCells = Seq.fill(8000)(new Tiles(3.toByte).shuffle(rnd).tiles.indexOf(0.toByte)).groupBy(identity)
    assertResult(9)(blankCells.size)
    blankCells.values.foreach(cells => assert(cells.size > 700 && cells.size < 1080, s"count ${cells.size}"))
  }

  test("a shuffled board is rarely already solved") {
    assert((1 to 200).count(seed => new SliderBoard(4).shuffle(new Random(seed)).isSolved) <= 1)
  }
}
