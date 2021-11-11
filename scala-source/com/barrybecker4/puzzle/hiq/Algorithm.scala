// Copyright by Barry G. Becker, 2013-2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.hiq

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.puzzle.common.AlgorithmEnum
import com.barrybecker4.puzzle.common.PuzzleController
import com.barrybecker4.puzzle.common.solver.{AStarConcurrentPuzzleSolver, AStarPuzzleSolver, ConcurrentPuzzleSolver, IDAStarPuzzleSolver, PuzzleSolver, SequentialPuzzleSolver}
import com.barrybecker4.puzzle.hiq.model.{PegBoard, PegMove}


case object SIMPLE_SEQUENTIAL extends Algorithm
case object A_STAR_SEQUENTIAL extends Algorithm
case object A_STAR_CONCURRENT extends Algorithm
case object IDA_STAR extends Algorithm
case object CONCURRENT_BREADTH extends Algorithm
case object CONCURRENT_DEPTH extends Algorithm
case object CONCURRENT_OPTIMUM extends Algorithm

/**
  * Type of HiQ solver to use.
  * @author Barry Becker
  */
sealed trait Algorithm extends AlgorithmEnum[PegBoard, PegMove] {

  private val label = AppContext.getLabel(this.toString)

  def getLabel: String = label

  /** Create an instance of the algorithm given the controller and a refreshable. */
  def createSolver(controller: PuzzleController[PegBoard, PegMove]): PuzzleSolver[PegMove] = {
    this match {
      case SIMPLE_SEQUENTIAL => new SequentialPuzzleSolver[PegBoard, PegMove](controller)
      case A_STAR_SEQUENTIAL => new AStarPuzzleSolver[PegBoard, PegMove](controller)
      case A_STAR_CONCURRENT => new AStarConcurrentPuzzleSolver[PegBoard, PegMove](controller)
      case IDA_STAR => new IDAStarPuzzleSolver[PegBoard, PegMove](controller)
      case CONCURRENT_BREADTH => new ConcurrentPuzzleSolver[PegBoard, PegMove](controller, 0.4f)
      case CONCURRENT_DEPTH => new ConcurrentPuzzleSolver[PegBoard, PegMove](controller, 0.05f)
      case CONCURRENT_OPTIMUM => new ConcurrentPuzzleSolver[PegBoard, PegMove](controller, 0.15f)
      case null => throw new IllegalArgumentException("Unexpected enum value: " + this)
    }
  }

  override def ordinal: Int = Algorithm.VALUES.indexOf(this)
}

object Algorithm {
  val VALUES: Array[AlgorithmEnum[PegBoard, PegMove]] = Array(
    SIMPLE_SEQUENTIAL, A_STAR_SEQUENTIAL, A_STAR_CONCURRENT, IDA_STAR, CONCURRENT_BREADTH, CONCURRENT_DEPTH, CONCURRENT_OPTIMUM
  )
}