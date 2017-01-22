// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle

import com.barrybecker4.common.search.Refreshable
import com.barrybecker4.puzzle.common.ui.AbstractPuzzleController
import com.barrybecker4.puzzle.slidingpuzzle.model.MoveGenerator
import com.barrybecker4.puzzle.slidingpuzzle.model.SlideMove
import com.barrybecker4.puzzle.slidingpuzzle.model.SliderBoard
import scala.collection.Seq

/**
  * Sliding Puzzle Controller.
  * See puzzle.common for puzzle framework classes.
  * See http://kociemba.org/fifteen/fifteensolver.html
  * Inspired by Peter Norvig's "Design of Computer Programs" class on Udacity.
  *
  * @author Barry Becker
  */
object SlidingPuzzleController {
  private val DEFAULT_SIZE = 3
  private val generator = new MoveGenerator
}

class SlidingPuzzleController(val ui: Refreshable[SliderBoard, SlideMove])
  extends AbstractPuzzleController[SliderBoard, SlideMove](ui) {

  private var initialPosition = new SliderBoard(SlidingPuzzleController.DEFAULT_SIZE, true)
  algorithm_ = A_STAR_SEQUENTIAL

  /** @param size the edge length of the puzzle to be solved */
  def setSize(size: Int) {
    initialPosition = new SliderBoard(size.toByte, true)
    if (ui_ != null) ui_.refresh(initialPosition, 0)
  }

  def initialState: SliderBoard = initialPosition

  def isGoal(position: SliderBoard): Boolean = position.isSolved

  def legalTransitions(position: SliderBoard): Seq[SlideMove] = SlidingPuzzleController.generator.generateMoves(position)

  def transition(position: SliderBoard, move: SlideMove): SliderBoard = position.doMove(move)

  /**
    * There are several commonly used "admissible" heuristics for determining this distance.
    * Here they are in order of easy/low quality to hard/high quality. Option 2 (manhattan) is implemented here.
    * 1) Number of tiles not in final position. Easies, but not that great.
    * 2) Manhattan distance: for each piece, sum the manhattan distance to its goal position. Easy, but not the best
    * 3) Walking distance (http://www.ic-net.or.jp/home/takaken/e/15pz/wd.gif). Hard, but better.
    * 4) disjoint pattern databases. See http://heuristicswiki.wikispaces.com/pattern+database
    *
    * @return estimate of the cost to reach the goal of all 9 pieces successfully placed
    */
  override def distanceFromGoal(position: SliderBoard): Int = position.distanceToGoal
}
