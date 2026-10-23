// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.solver.fastsearch

import com.barrybecker4.puzzle.slidingpuzzle.solver.SliderTestSupport.randomSolvableTiles
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

class IdaStarSearchSuite extends AnyFunSuite {

  private val geometry = new GridGeometry(3)

  private def search(tiles: Seq[Int], weight: Int = 100, max: Long = Long.MaxValue, progress: Long => Unit = _ => ()) =
    new IdaStarSearch(new SliderState(geometry, tiles), geometry, progress, weight, max)

  /** What the board looks like after the tile in each given cell is slid into the blank, one after another. */
  private def replay(tiles: Seq[Int], cells: Seq[Int]): Seq[Int] = {
    val state = new SliderState(geometry, tiles)
    cells.foreach(state.slideIntoBlank)
    (0 until geometry.numCells).map(state.tileAt)
  }

  test("a solved board needs no slides") {
    assertResult(0)(search((1 to 8) :+ 0).solve().get.length)
  }

  test("finds the one slide that solves a board") {
    assertResult(Seq(8))(search(Seq(1, 2, 3, 4, 5, 6, 7, 0, 8)).solve().get.toSeq)
  }

  test("the slides it finds solve the board") {
    val tiles = randomSolvableTiles(3, new Random(5))
    assertResult((1 to 8) :+ 0)(replay(tiles, search(tiles).solve().get.toSeq))
  }

  test("finds a shortest solution of the hardest 3 by 3 boards, which take 31 slides") {
    assertResult(31)(search(Seq(8, 6, 7, 2, 5, 4, 3, 0, 1)).solve().get.length)
  }

  test("a larger weight finds a solution that is no longer than the weight times a shortest one") {
    val tiles = Seq(8, 6, 7, 2, 5, 4, 3, 0, 1)
    val length = search(tiles, weight = 200).solve().get.length
    assert(length >= 31 && length <= 62)
  }

  test("gives up after looking at the most positions it is allowed to") {
    val searcher = search(Seq(8, 6, 7, 2, 5, 4, 3, 0, 1), max = 1000)
    assert(searcher.solve().isEmpty)
    assert(searcher.positionsVisited > 1000 && searcher.positionsVisited < 1100)
  }

  test("reports progress every PROGRESS_INTERVAL positions") {
    var reports = 0
    val searcher = search(Seq(8, 6, 7, 2, 5, 4, 3, 0, 1), progress = _ => reports += 1)
    searcher.solve()
    assertResult((searcher.positionsVisited / IdaStarSearch.PROGRESS_INTERVAL).toInt)(reports)
  }

  test("stops when the progress report throws") {
    val geometry4 = new GridGeometry(4)
    val boards = (1 to 60).map(seed => randomSolvableTiles(4, new Random(seed)))
    val tiles = boards.find { b =>
      val probe = new IdaStarSearch(new SliderState(geometry4, b), geometry4, _ => (), 100, 1_000_000L)
      probe.solve()
      probe.positionsVisited > IdaStarSearch.PROGRESS_INTERVAL
    }.getOrElse(fail("expected a 4 by 4 board that takes more than one progress interval"))
    val searcher = new IdaStarSearch(new SliderState(geometry4, tiles), geometry4,
      _ => throw new InterruptedException("cancelled"))
    intercept[InterruptedException](searcher.solve())
    assertResult(IdaStarSearch.PROGRESS_INTERVAL.toLong)(searcher.positionsVisited)
  }
}
