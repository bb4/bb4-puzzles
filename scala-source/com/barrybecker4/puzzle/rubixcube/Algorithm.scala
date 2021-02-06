// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.puzzle.common.{AlgorithmEnum, PuzzleController}
import com.barrybecker4.puzzle.common.solver.{AStarConcurrentPuzzleSolver, AStarPuzzleSolver, ConcurrentPuzzleSolver, PuzzleSolver, SequentialPuzzleSolver}
import com.barrybecker4.puzzle.rubixcube.model.{CubeMove, Cube}

case object SIMPLE_SEQUENTIAL extends Algorithm
case object A_STAR_SEQUENTIAL extends Algorithm
case object A_STAR_CONCURRENT extends Algorithm
case object CONCURRENT_BREADTH extends Algorithm
case object CONCURRENT_DEPTH extends Algorithm
case object CONCURRENT_OPTIMUM extends Algorithm


/**
  * Type of solver to use.
  *
  * @author Barry Becker
  */
sealed trait Algorithm extends AlgorithmEnum[Cube, CubeMove] {

  val label: String = AppContext.getLabel(this.toString)
  def getLabel: String = label

  /**
    * Create an instance of the algorithm given the controller and a refreshable.
    */
  def createSolver(controller: PuzzleController[Cube, CubeMove]): PuzzleSolver[CubeMove] = {
    this match {
      case A_STAR_SEQUENTIAL => new AStarPuzzleSolver[Cube, CubeMove](controller)
      case A_STAR_CONCURRENT => new AStarConcurrentPuzzleSolver[Cube, CubeMove](controller)
      // this will find a solution, but not necessary the shortest path
      case SIMPLE_SEQUENTIAL => new SequentialPuzzleSolver[Cube, CubeMove](controller)
      // this will find the shortest path to a solution if one exists, but takes longer
      case CONCURRENT_BREADTH => new ConcurrentPuzzleSolver[Cube, CubeMove](controller, 1.0f)
      case CONCURRENT_DEPTH => new ConcurrentPuzzleSolver[Cube, CubeMove](controller, 0.12f)
      case CONCURRENT_OPTIMUM => new ConcurrentPuzzleSolver[Cube, CubeMove](controller, 0.3f)
    }
  }

  override def ordinal: Int = Algorithm.VALUES.indexOf(this)
}

object Algorithm {
  val VALUES: Array[AlgorithmEnum[Cube, CubeMove]] = Array(
    SIMPLE_SEQUENTIAL, A_STAR_SEQUENTIAL, A_STAR_CONCURRENT, CONCURRENT_BREADTH, CONCURRENT_DEPTH, CONCURRENT_OPTIMUM
  )
}
