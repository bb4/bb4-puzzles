// Copyright by Barry G. Becker, 2000-2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.twopails

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.puzzle.common.AlgorithmEnum
import com.barrybecker4.puzzle.common.PuzzleController
import com.barrybecker4.puzzle.common.solver.{AStarConcurrentPuzzleSolver, AStarPuzzleSolver, ConcurrentPuzzleSolver, IDAStarPuzzleSolver, PuzzleSolver, SequentialPuzzleSolver}
import com.barrybecker4.puzzle.twopails.model.Pails
import com.barrybecker4.puzzle.twopails.model.PourOperation

case object SIMPLE_SEQUENTIAL extends Algorithm
case object A_STAR_SEQUENTIAL extends Algorithm
case object A_STAR_CONCURRENT extends Algorithm
case object IDA_STAR extends Algorithm
case object CONCURRENT_BREADTH extends Algorithm
case object CONCURRENT_DEPTH extends Algorithm
case object CONCURRENT_OPTIMUM extends Algorithm

/**
  * Type of search solver to use.
  *
  * @author Barry Becker
  */
sealed trait Algorithm extends AlgorithmEnum[Pails, PourOperation] {

  private val label = AppContext.getLabel(this.toString)
  def getLabel: String = label

  /**
    * Create an instance of the algorithm given the controller and a refreshable.
    */
  def createSolver(controller: PuzzleController[Pails, PourOperation]): PuzzleSolver[PourOperation] = {
    this match {
      case SIMPLE_SEQUENTIAL => new SequentialPuzzleSolver[Pails, PourOperation](controller)
      case A_STAR_SEQUENTIAL => new AStarPuzzleSolver[Pails, PourOperation](controller)
      case A_STAR_CONCURRENT => new AStarConcurrentPuzzleSolver[Pails, PourOperation](controller)
      case IDA_STAR => new IDAStarPuzzleSolver[Pails, PourOperation](controller)
      case CONCURRENT_BREADTH => new ConcurrentPuzzleSolver[Pails, PourOperation](controller, 0.4f)
      case CONCURRENT_DEPTH => new ConcurrentPuzzleSolver[Pails, PourOperation](controller, 0.12f)
      case CONCURRENT_OPTIMUM => new ConcurrentPuzzleSolver[Pails, PourOperation](controller, 0.2f)
      case null => throw new IllegalStateException("unknow solver type: " + this)
    }
  }

  override def ordinal: Int = Algorithm.VALUES.indexOf(this)
}

object Algorithm {
  val VALUES: Array[AlgorithmEnum[Pails, PourOperation]] = Array(
    SIMPLE_SEQUENTIAL, A_STAR_CONCURRENT, IDA_STAR,
    CONCURRENT_BREADTH, CONCURRENT_DEPTH, CONCURRENT_OPTIMUM
  )
}
