// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.puzzle.common.{AlgorithmEnum, PuzzleController}
import com.barrybecker4.puzzle.common.solver.{AStarConcurrentPuzzleSolver, AStarPuzzleSolver, ConcurrentPuzzleSolver, IDAStarPuzzleSolver, PuzzleSolver, SequentialPuzzleSolver}
import com.barrybecker4.puzzle.rubixcube.model.{Cube, CubeMove}


/**
  * Type of solver to use.
  *
  * @author Barry Becker
  */
enum Algorithm extends AlgorithmEnum[Cube, CubeMove] {

  val label: String = AppContext.getLabel(this.toString)
  def getLabel: String = label

  /**
    * Create an instance of the algorithm given the controller and a refreshable.
    */
  def createSolver(controller: PuzzleController[Cube, CubeMove]): PuzzleSolver[CubeMove] = this match {
    case A_STAR_SEQUENTIAL => new AStarPuzzleSolver[Cube, CubeMove](controller)
    case A_STAR_CONCURRENT => new AStarConcurrentPuzzleSolver[Cube, CubeMove](controller)
    case IDA_STAR => new IDAStarPuzzleSolver[Cube, CubeMove](controller)
    // this will find a solution, but not necessary the shortest path
    case SIMPLE_SEQUENTIAL => new SequentialPuzzleSolver[Cube, CubeMove](controller)
    // this will find the shortest path to a solution if one exists, but takes longer
    case CONCURRENT_BREADTH => new ConcurrentPuzzleSolver[Cube, CubeMove](controller, 1.0f)
    case CONCURRENT_DEPTH => new ConcurrentPuzzleSolver[Cube, CubeMove](controller, 0.12f)
    case CONCURRENT_OPTIMUM => new ConcurrentPuzzleSolver[Cube, CubeMove](controller, 0.3f)
  }

  case SIMPLE_SEQUENTIAL extends Algorithm
  case A_STAR_SEQUENTIAL extends Algorithm
  case A_STAR_CONCURRENT extends Algorithm
  case IDA_STAR extends Algorithm
  case CONCURRENT_BREADTH extends Algorithm
  case CONCURRENT_DEPTH extends Algorithm
  case CONCURRENT_OPTIMUM extends Algorithm
}
