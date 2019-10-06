// Copyright by Barry G. Becker, 2000-2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.twopails

import com.barrybecker4.search.Refreshable
import com.barrybecker4.puzzle.common.ui.AbstractPuzzleController
import com.barrybecker4.puzzle.twopails.model.MoveGenerator
import com.barrybecker4.puzzle.twopails.model.PailParams
import com.barrybecker4.puzzle.twopails.model.Pails
import com.barrybecker4.puzzle.twopails.model.PourOperation


/**
  * Two pails puzzle Controller.
  * See http://www.cut-the-knot.org/ctk/CartWater.shtml#solve
  * http://demonstrations.wolfram.com/WaterPouringProblem/
  * Inspired by Peter Norvig's "Design of Computer Programs" class on Udacity.
  *
  * @author Barry Becker
  */
object TwoPailsPuzzleController {
  private val DEFAULT_PARAMS = new PailParams(9, 4, 6)
}

class TwoPailsPuzzleController(ui: Refreshable[Pails, PourOperation])
  extends AbstractPuzzleController[Pails, PourOperation](ui) {

  private var initialPosition = new Pails(TwoPailsPuzzleController.DEFAULT_PARAMS)
  // set default
  algorithm = A_STAR_SEQUENTIAL


  /**
    * Puzzle parameters determine the size of the containers and target measure.
    *
    * @param params new parameters to use when solving
    */
  def setParams(params: PailParams): Unit = {
    initialPosition = new Pails(params)
    if (ui != null) ui.refresh(initialPosition, 0)
  }

  override def initialState: Pails = initialPosition

  override def isGoal(position: Pails): Boolean = position.isSolved

  override def legalTransitions(position: Pails): Seq[PourOperation] =
    new MoveGenerator(position).generateMoves

  override def transition(position: Pails, move: PourOperation): Pails =
    position.applyMove(move, undo = false)
}

