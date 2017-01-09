// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge

import com.barrybecker4.common.search.Refreshable
import com.barrybecker4.puzzle.bridge.model._
import com.barrybecker4.puzzle.common.ui.AbstractPuzzleController
import scala.collection.Seq

/**
  * Bridge crossing Puzzle Controller.
  * See puzzle.common for puzzle framework classes.
  * See http://en.wikipedia.org/wiki/Bridge_and_torch_problem#cite_note-eatcs-4
  * See http://page.mi.fu-berlin.de/rote/Papers/pdf/Crossing+the+bridge+at+night.pdf
  * for analysis of general problem.
  * Inspired by Peter Norvig's "Design of Computer Programs" class on Udacity.
  *
  * @author Barry Becker
  */
object BridgePuzzleController {
  /** this is the standard bridge crossing problem with 4 people */
  private val DEFAULT_PEOPLE = InitialConfiguration.STANDARD_PROBLEM.peopleSpeeds
}

/**
  * @param ui shows the current state on the screen.
  */
class BridgePuzzleController(val ui: Refreshable[Bridge, BridgeMove])
  extends AbstractPuzzleController[Bridge, BridgeMove](ui) {

  private var initialPosition: Bridge = new Bridge(BridgePuzzleController.DEFAULT_PEOPLE)
  algorithm_ = A_STAR_SEQUENTIAL

  if (ui_ != null) ui_.refresh(initialPosition, 0)


  def setConfiguration(config: Array[Int]) {
    initialPosition = new Bridge(config)
    if (ui_ != null) ui_.refresh(initialPosition, 0)
  }

  def initialState: Bridge = initialPosition

  def isGoal(position: Bridge): Boolean = position.isSolved

  def legalTransitions(position: Bridge): Seq[BridgeMove] = new MoveGenerator(position).generateMoves

  def transition(position: Bridge, move: BridgeMove): Bridge = position.applyMove(move, reverse = false)

  /**
    * @return estimate of the cost to reach the goal of all 9 pieces successfully placed
    */
  override def distanceFromGoal(position: Bridge): Int = position.distanceFromGoal

  override def getCost(move: BridgeMove): Int = move.cost
}
