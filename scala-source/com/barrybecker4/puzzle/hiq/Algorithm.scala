// Copyright by Barry G. Becker, 2013-2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.hiq

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.puzzle.common.AlgorithmEnum
import com.barrybecker4.puzzle.common.PuzzleController
import com.barrybecker4.puzzle.common.solver.{AStarConcurrentPuzzleSolver, AStarPuzzleSolver, ConcurrentPuzzleSolver, IDAStarPuzzleSolver, PuzzleSolver, SequentialPuzzleSolver}
import com.barrybecker4.puzzle.hiq.model.{PegBoard, PegMove}


/**
  * Type of HiQ solver to use.
  * @author Barry Becker
  */
enum Algorithm extends AlgorithmEnum[PegBoard, PegMove] {

  private val label = AppContext.getLabel(this.toString)

  def getLabel: String = label

  /** Create an instance of the algorithm given the controller and a refreshable. */
  def createSolver(controller: PuzzleController[PegBoard, PegMove]): PuzzleSolver[PegMove] = this match {
    case SIMPLE_SEQUENTIAL => new SequentialPuzzleSolver[PegBoard, PegMove](controller)
    case A_STAR_SEQUENTIAL => new AStarPuzzleSolver[PegBoard, PegMove](controller)
    case A_STAR_CONCURRENT => new AStarConcurrentPuzzleSolver[PegBoard, PegMove](controller)
    case IDA_STAR => new IDAStarPuzzleSolver[PegBoard, PegMove](controller)
    case CONCURRENT_BREADTH => new ConcurrentPuzzleSolver[PegBoard, PegMove](controller, 0.4f)
    case CONCURRENT_DEPTH => new ConcurrentPuzzleSolver[PegBoard, PegMove](controller, 0.05f)
    case CONCURRENT_OPTIMUM => new ConcurrentPuzzleSolver[PegBoard, PegMove](controller, 0.15f)
    case null => throw new IllegalArgumentException("Unexpected enum value: " + this)
  }

  case SIMPLE_SEQUENTIAL extends Algorithm
  case A_STAR_SEQUENTIAL extends Algorithm
  case A_STAR_CONCURRENT extends Algorithm
  case IDA_STAR extends Algorithm
  case CONCURRENT_BREADTH extends Algorithm
  case CONCURRENT_DEPTH extends Algorithm
  case CONCURRENT_OPTIMUM extends Algorithm
}
