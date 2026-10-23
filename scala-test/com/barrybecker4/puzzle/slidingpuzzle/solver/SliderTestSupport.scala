// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.solver

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.common.testsupport.i18n.StubMessageContext
import com.barrybecker4.puzzle.slidingpuzzle.SlidingPuzzleController
import com.barrybecker4.puzzle.slidingpuzzle.model.{SlideMove, SliderBoard, Tiles}
import com.barrybecker4.puzzle.slidingpuzzle.solver.fastsearch.{GridGeometry, Solvability}
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

  def boardOf(tiles: Seq[Int]): SliderBoard = SliderBoard(Tiles(tiles.map(_.toByte).toIndexedSeq))

  /** @return a controller whose puzzle starts from the given board */
  def controllerFor(board: SliderBoard): SlidingPuzzleController =
    new SlidingPuzzleController(new QuietRefreshable) {
      override def initialState: SliderBoard = board
    }

  /** @return tiles in a random order that can be solved */
  def randomSolvableTiles(size: Int, rnd: Random): Seq[Int] = {
    val geometry = new GridGeometry(size)
    Iterator.continually(rnd.shuffle((0 until size * size).toList)).find(Solvability.isSolvable(geometry, _)).get
  }

  def isSolvedBy(board: SliderBoard, moves: Seq[SlideMove]): Boolean = moves.foldLeft(board)(_.doMove(_)).isSolved
}
