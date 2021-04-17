// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.puzzle.common.solver._
import com.barrybecker4.puzzle.common.{AlgorithmEnum, PuzzleController}
import com.barrybecker4.puzzle.tantrix.model.{TantrixBoard, TilePlacement}

case object SIMPLE_SEQUENTIAL extends Algorithm
case object BRUTE_FORCE_SEQUENTIAL extends Algorithm
case object A_STAR_SEQUENTIAL extends Algorithm
case object A_STAR_CONCURRENT extends Algorithm
case object IDA_STAR extends Algorithm
case object CONCURRENT_BREADTH extends Algorithm
case object CONCURRENT_DEPTH extends Algorithm
case object CONCURRENT_OPTIMUM extends Algorithm
case object GENETIC_SEARCH extends Algorithm
case object CONCURRENT_GENETIC_SEARCH extends Algorithm

/**
  * Enum for type of solver to employ when solving the puzzle.
  *
  * @author Barry Becker
  */
sealed trait Algorithm extends AlgorithmEnum[TantrixBoard, TilePlacement] {

  private val label = AppContext.getLabel(this.toString)
  def getLabel: String = label


  /**
    * Create an instance of the algorithm given the controller and a refreshable.
    */
  def createSolver(controller: PuzzleController[TantrixBoard, TilePlacement]): PuzzleSolver[TilePlacement] = {
    this match {
      case SIMPLE_SEQUENTIAL => new SequentialPuzzleSolver[TantrixBoard, TilePlacement](controller)
      case BRUTE_FORCE_SEQUENTIAL => new AStarPuzzleSolver[TantrixBoard, TilePlacement](controller)
      case A_STAR_SEQUENTIAL => new AStarPuzzleSolver[TantrixBoard, TilePlacement](controller)
      case A_STAR_CONCURRENT => new AStarConcurrentPuzzleSolver[TantrixBoard, TilePlacement](controller)
      case IDA_STAR => new IDAStarPuzzleSolver[TantrixBoard, TilePlacement](controller)
      case CONCURRENT_BREADTH => new ConcurrentPuzzleSolver[TantrixBoard, TilePlacement](controller, 0.4f)
      case CONCURRENT_DEPTH => new ConcurrentPuzzleSolver[TantrixBoard, TilePlacement](controller, 0.12f)
      case CONCURRENT_OPTIMUM => new ConcurrentPuzzleSolver[TantrixBoard, TilePlacement](controller, 0.2f)
      case GENETIC_SEARCH => new GeneticSearchSolver(controller, false)
      case CONCURRENT_GENETIC_SEARCH => new GeneticSearchSolver(controller, true)
    }
  }

  override def ordinal: Int = Algorithm.VALUES.indexOf(this)
}

object Algorithm {
  val VALUES: Array[AlgorithmEnum[TantrixBoard, TilePlacement]] = Array(
    SIMPLE_SEQUENTIAL, BRUTE_FORCE_SEQUENTIAL, A_STAR_SEQUENTIAL, A_STAR_CONCURRENT, IDA_STAR,
    CONCURRENT_BREADTH, CONCURRENT_DEPTH, CONCURRENT_OPTIMUM, GENETIC_SEARCH, CONCURRENT_GENETIC_SEARCH
  )
}