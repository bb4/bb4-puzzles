/*
 * // Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
 */

package com.barrybecker4.puzzle.redpuzzle.solver

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.puzzle.common.AlgorithmEnum
import com.barrybecker4.puzzle.common.PuzzleController
import com.barrybecker4.puzzle.common.solver.AStarConcurrentPuzzleSolver
import com.barrybecker4.puzzle.common.solver.AStarPuzzleSolver
import com.barrybecker4.puzzle.common.solver.ConcurrentPuzzleSolver
import com.barrybecker4.puzzle.common.solver.PuzzleSolver
import com.barrybecker4.puzzle.common.solver.SequentialPuzzleSolver
import com.barrybecker4.puzzle.hiq.model.{PegBoard, PegMove}
import com.barrybecker4.puzzle.redpuzzle1.model.Piece
import com.barrybecker4.puzzle.redpuzzle1.model.PieceList


case object BRUTE_FORCE_ORIGINAL extends Algorithm
case object BRUTE_FORCE_SEQUENTIAL extends Algorithm
case object A_STAR_SEQUENTIAL extends Algorithm
case object A_STAR_CONCURRENT extends Algorithm
case object CONCURRENT_BREADTH extends Algorithm
case object CONCURRENT_DEPTH extends Algorithm
case object CONCURRENT_OPTIMUM extends Algorithm

/**
  * Enum for type of solver to employ when solving the puzzle.
  *
  * @author Barry Becker
  */
sealed trait Algorithm extends AlgorithmEnum[PieceList, Piece] {

  private val label = AppContext.getLabel(this.toString)

  def getLabel: String = label


  /**
    * Create an instance of the algorithm given the controller and a refreshable.
    */
  def createSolver(controller: PuzzleController[PieceList, Piece]): PuzzleSolver[Piece] = {
    this match {
      case BRUTE_FORCE_ORIGINAL => new BruteForceSolver(controller)
      case BRUTE_FORCE_SEQUENTIAL => new SequentialPuzzleSolver[PieceList, Piece](controller)
      case A_STAR_SEQUENTIAL => new AStarPuzzleSolver[PieceList, Piece](controller)
      case A_STAR_CONCURRENT => new AStarConcurrentPuzzleSolver[PieceList, Piece](controller)
      case CONCURRENT_DEPTH => new ConcurrentPuzzleSolver[PieceList, Piece](controller, 0.1f)
      case CONCURRENT_BREADTH => new ConcurrentPuzzleSolver[PieceList, Piece](controller, 0.25f)
      case GENETIC_SEARCH => new GeneticSearchSolver(controller, false)
      case CONCURRENT_GENETIC_SEARCH => new GeneticSearchSolver(controller, true)
      case _ => throw new IllegalArgumentException("Unexpected enum value: " + this)
    }
  }

  override def ordinal(): Int = Algorithm.VALUES.indexOf(this)
}

object Algorithm {
  val VALUES: Array[AlgorithmEnum[PieceList, Piece]] = Array(
    BRUTE_FORCE_ORIGINAL, BRUTE_FORCE_SEQUENTIAL, A_STAR_SEQUENTIAL, A_STAR_CONCURRENT,
    CONCURRENT_BREADTH, CONCURRENT_DEPTH, CONCURRENT_OPTIMUM
  )
}