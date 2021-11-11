// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.solver

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.puzzle.common.{AlgorithmEnum, PuzzleController}
import com.barrybecker4.puzzle.common.solver._
import com.barrybecker4.puzzle.redpuzzle.model.{OrientedPiece, PieceList}


case object BRUTE_FORCE_ORIGINAL extends Algorithm
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
  * @author Barry Becker
  */
sealed trait Algorithm extends AlgorithmEnum[PieceList, OrientedPiece] {

  private val label = AppContext.getLabel(this.toString)
  def getLabel: String = label

  /** Create an instance of the algorithm given the controller and a refreshable. */
  def createSolver(controller: PuzzleController[PieceList, OrientedPiece]): PuzzleSolver[OrientedPiece] = {
    this match {
      case BRUTE_FORCE_ORIGINAL => new BruteForceSolver(controller)
      case BRUTE_FORCE_SEQUENTIAL => new SequentialPuzzleSolver[PieceList, OrientedPiece](controller)
      case A_STAR_SEQUENTIAL => new AStarPuzzleSolver[PieceList, OrientedPiece](controller)
      case A_STAR_CONCURRENT => new AStarConcurrentPuzzleSolver[PieceList, OrientedPiece](controller)
      case IDA_STAR => new IDAStarPuzzleSolver[PieceList, OrientedPiece](controller)
      case CONCURRENT_DEPTH => new ConcurrentPuzzleSolver[PieceList, OrientedPiece](controller, 0.1f)
      case CONCURRENT_BREADTH => new ConcurrentPuzzleSolver[PieceList, OrientedPiece](controller, 0.3f)
      case CONCURRENT_OPTIMUM => new ConcurrentPuzzleSolver[PieceList, OrientedPiece](controller, 0.2f)
      case GENETIC_SEARCH => new GeneticSearchSolver(controller, false)
      case CONCURRENT_GENETIC_SEARCH => new GeneticSearchSolver(controller, true)
      case null => throw new IllegalArgumentException("Unexpected enum value: " + this)
    }
  }

  override def ordinal: Int = Algorithm.VALUES.indexOf(this)
}

object Algorithm {
  val VALUES: Array[AlgorithmEnum[PieceList, OrientedPiece]] = Array(
    BRUTE_FORCE_ORIGINAL, BRUTE_FORCE_SEQUENTIAL, A_STAR_SEQUENTIAL, A_STAR_CONCURRENT, IDA_STAR,
    CONCURRENT_BREADTH, CONCURRENT_DEPTH, CONCURRENT_OPTIMUM, GENETIC_SEARCH, CONCURRENT_GENETIC_SEARCH
  )
}