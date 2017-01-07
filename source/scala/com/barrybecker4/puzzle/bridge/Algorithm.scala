// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.puzzle.bridge.model.Bridge
import com.barrybecker4.puzzle.bridge.model.BridgeMove
import com.barrybecker4.puzzle.common.AlgorithmEnum
import com.barrybecker4.puzzle.common.PuzzleController
import com.barrybecker4.puzzle.common.solver.AStarConcurrentPuzzleSolver
import com.barrybecker4.puzzle.common.solver.AStarPuzzleSolver
import com.barrybecker4.puzzle.common.solver.ConcurrentPuzzleSolver
import com.barrybecker4.puzzle.common.solver.PuzzleSolver
import com.barrybecker4.puzzle.common.solver.SequentialPuzzleSolver


object Algorithm {
  val VALUES: Array[AlgorithmEnum[Bridge, BridgeMove]] = Array(
    A_STAR_SEQUENTIAL, A_STAR_CONCURRENT, SIMPLE_SEQUENTIAL,
    CONCURRENT_BREADTH, CONCURRENT_DEPTH, CONCURRENT_OPTIMUM
  )
}
/**
  * Type of solver to use.
  *
  * @author Barry Becker
  */
sealed trait Algorithm extends AlgorithmEnum[Bridge, BridgeMove] {

  private val label = AppContext.getLabel(this.toString)
  def getLabel: String = label

  /**
    * Create an instance of the algorithm given the controller and a refreshable.
    */
  def createSolver(controller: PuzzleController[Bridge, BridgeMove]): PuzzleSolver[BridgeMove] = {
    this match {
      case A_STAR_SEQUENTIAL =>
        new AStarPuzzleSolver[Bridge, BridgeMove](controller)
      case A_STAR_CONCURRENT =>
        new AStarConcurrentPuzzleSolver[Bridge, BridgeMove](controller)
      case SIMPLE_SEQUENTIAL =>
        // this will find a solution, but not necessary the shortest path
        new SequentialPuzzleSolver[Bridge, BridgeMove](controller)
      case CONCURRENT_BREADTH =>
        // this will find the shortest path to a solution if one exists, but takes longer
        new ConcurrentPuzzleSolver[Bridge, BridgeMove](controller, 1.0f)
      case CONCURRENT_DEPTH =>
        new ConcurrentPuzzleSolver[Bridge, BridgeMove](controller, 0.1f)
      case CONCURRENT_OPTIMUM =>
        new ConcurrentPuzzleSolver[Bridge, BridgeMove](controller, 0.3f)
      case _ => throw new IllegalArgumentException("Unexpected enuma value: " + this)
    }
  }

  override def ordinal(): Int = Algorithm.VALUES.indexOf(this)
}

case object A_STAR_SEQUENTIAL extends Algorithm
case object A_STAR_CONCURRENT extends Algorithm
case object SIMPLE_SEQUENTIAL extends Algorithm
case object CONCURRENT_BREADTH extends Algorithm
case object CONCURRENT_DEPTH extends Algorithm
case object CONCURRENT_OPTIMUM extends Algorithm
