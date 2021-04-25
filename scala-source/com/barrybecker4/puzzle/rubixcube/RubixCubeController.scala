// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube

import com.barrybecker4.puzzle.common.ui.AbstractPuzzleController
import com.barrybecker4.puzzle.rubixcube.model.{Cube, CubeMove}
import com.barrybecker4.puzzle.rubixcube.model.MoveGenerator
import com.barrybecker4.puzzle.rubixcube.ui.selectors.SizeSelector
import com.barrybecker4.search.Refreshable


object RubixCubeController {
  private val DEFAULT_SIZE = SizeSelector.DEFAULT_SIZE
  private val generator = new MoveGenerator
}

class RubixCubeController(ui: Refreshable[Cube, CubeMove])
  extends AbstractPuzzleController[Cube, CubeMove](ui) {

  private var initialPosition = new Cube(RubixCubeController.DEFAULT_SIZE)
  algorithm = A_STAR_SEQUENTIAL

  /** @param size the edge length of the rubix cube to be solved */
  def setSize(size: Int): Unit = {
    initialPosition = new Cube(size.toByte)
    refresh()
  }

  def shuffle(): Unit = {
    initialPosition = initialPosition.shuffle()
    refresh()
  }

  def doMove(move: CubeMove): Unit = {
    if (ui != null)
      initialPosition = ui.animateTransition(move)
  }

  private def refresh(): Unit = {
    if (ui != null)
      ui.refresh(initialPosition, 0)
  }

  def initialState: Cube = initialPosition

  /** do this instead of using floats */
  override def getCost(move: CubeMove): Int = 10

  def isGoal(position: Cube): Boolean = position.isSolved

  def legalTransitions(position: Cube): Seq[CubeMove] =
    RubixCubeController.generator.generateMoves(position)

  def transition(position: Cube, move: CubeMove): Cube = position.doMove(move)

  /**
    * There are several possible "admissible" heuristics for determining this distance.
    * The simplest is probably jsut the number of colors not in final position.
    * @return estimate of the cost to reach the goal of all 9 pieces successfully placed
    */
  override def distanceFromGoal(position: Cube): Int = position.distanceToGoal
}
