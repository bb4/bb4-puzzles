// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.solver

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.common.testsupport.i18n.StubMessageContext
import com.barrybecker4.puzzle.slidingpuzzle.SlidingPuzzleController
import com.barrybecker4.puzzle.slidingpuzzle.model.{SlideMove, SliderBoard, Solvability, Tiles}
import com.barrybecker4.search.Refreshable

import scala.util.Random

/** Helpers for making boards and controllers in the tests of the sliding puzzle solvers. */
object SliderTestSupport {

  AppContext.injectMessageContext(new StubMessageContext)

  private class QuietRefreshable extends Refreshable[SliderBoard, SlideMove] {
    override def refresh(state: SliderBoard, numTries: Long): Unit = {}
    override def animateTransition(transition: SlideMove): SliderBoard = null
    override def finalRefresh(path: Option[Seq[SlideMove]], state: Option[SliderBoard],
                              numTries: Long, elapsedMillis: Long): Unit = {}
  }

  /** Remembers each board it is asked to show while the solver works, and the final result. */
  class RecordingRefreshable extends Refreshable[SliderBoard, SlideMove] {
    val shown = scala.collection.mutable.ArrayBuffer[SliderBoard]()
    var finalCount = 0
    override def refresh(state: SliderBoard, numTries: Long): Unit = shown += state
    override def animateTransition(transition: SlideMove): SliderBoard = null
    override def finalRefresh(path: Option[Seq[SlideMove]], state: Option[SliderBoard],
                              numTries: Long, elapsedMillis: Long): Unit = finalCount += 1
  }

  def boardOf(tiles: Seq[Int]): SliderBoard = SliderBoard(Tiles(tiles.map(_.toByte).toIndexedSeq))

  /** @return a controller whose puzzle starts from the given board */
  def controllerFor(board: SliderBoard, ui: Refreshable[SliderBoard, SlideMove] = new QuietRefreshable): SlidingPuzzleController =
    new SlidingPuzzleController(ui) {
      override def initialState: SliderBoard = board
    }

  /** @return tiles in a random order that can be solved */
  def randomSolvableTiles(size: Int, rnd: Random): Seq[Int] = {
    Iterator.continually(rnd.shuffle((0 until size * size).toList)).find(Solvability.isSolvable(size, _)).get
  }

  def isSolvedBy(board: SliderBoard, moves: Seq[SlideMove]): Boolean = moves.foldLeft(board)(_.doMove(_)).isSolved
}
