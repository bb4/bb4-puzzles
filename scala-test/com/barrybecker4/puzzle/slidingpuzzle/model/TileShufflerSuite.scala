// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.model

import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

class TileShufflerSuite extends AnyFunSuite {

  private def shuffled(size: Int, seed: Int, style: ShuffleStyle): Seq[Int] =
    new SliderBoard(size).shuffle(new Random(seed), style).tiles.tiles.map(_.toInt)

  private def averageManhattan(size: Int, style: ShuffleStyle): Double =
    (1 to 100).map(seed => new SliderBoard(size).shuffle(new Random(seed), style).distanceToGoal).sum / 100.0

  for (style <- ShuffleStyle.values) {
    test(s"$style has the same tiles as the solved board") {
      for (size <- 2 to 5; seed <- 1 to 10)
        assertResult((0 until size * size).toList)(shuffled(size, seed, style).sorted.toList)
    }

    test(s"$style can always be solved") {
      for (size <- 2 to 6; seed <- 1 to 50)
        assert(Solvability.isSolvable(size, shuffled(size, seed, style)), s"size $size seed $seed")
    }

    test(s"$style gives the same shuffle for the same random seed") {
      assertResult(shuffled(4, 3, style))(shuffled(4, 3, style))
    }

    test(s"$style is rarely already solved") {
      assert((1 to 200).count(seed => new SliderBoard(4).shuffle(new Random(seed), style).isSolved) <= 1)
    }
  }

  test("a random walk slides tiles, so it only moves tiles to cells next to the blank") {
    val shuffled = new TileShuffler(new Random(5)).byRandomWalk(new Tiles(3.toByte))
    assert(shuffled != new Tiles(3.toByte) && Solvability.isSolvable(3, shuffled.tiles.map(_.toInt)))
  }

  test("shuffle with a style uses the shuffle of that style") {
    val tiles = new Tiles(4.toByte)
    assertResult(new TileShuffler(new Random(8)).byRandomWalk(tiles))(
      new TileShuffler(new Random(8)).shuffle(tiles, ShuffleStyle.RANDOM_WALK))
    assertResult(new TileShuffler(new Random(8)).uniformly(tiles))(
      new TileShuffler(new Random(8)).shuffle(tiles, ShuffleStyle.UNIFORM_RANDOM))
  }

  test("every solvable arrangement of the 2 by 2 board is about equally likely when shuffled uniformly") {
    val rnd = new Random(1)
    val counts = Seq.fill(12000)(new TileShuffler(rnd).uniformly(new Tiles(2.toByte)).tiles.toList).groupBy(identity).view.mapValues(_.size)
    assertResult(12)(counts.size)
    counts.values.foreach(count => assert(count > 800 && count < 1200, s"count $count"))
  }

  test("the blank is as likely to end up in any cell when shuffled uniformly") {
    val rnd = new Random(2)
    val blankCells = Seq.fill(8000)(new TileShuffler(rnd).uniformly(new Tiles(3.toByte)).tiles.indexOf(0.toByte)).groupBy(identity)
    assertResult(9)(blankCells.size)
    blankCells.values.foreach(cells => assert(cells.size > 700 && cells.size < 1080, s"count ${cells.size}"))
  }

  test("a random walk leaves the 15 puzzle much closer to solved than a uniform shuffle does") {
    val walk = averageManhattan(4, ShuffleStyle.RANDOM_WALK)
    val uniform = averageManhattan(4, ShuffleStyle.UNIFORM_RANDOM)
    assert(walk < uniform / 1.5, s"walk $walk uniform $uniform")
  }
}
