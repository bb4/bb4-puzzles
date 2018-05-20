// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.puzzle.common.AlgorithmEnum
import com.barrybecker4.puzzle.common.PuzzleController
import com.barrybecker4.puzzle.common.solver.AStarConcurrentPuzzleSolver
import com.barrybecker4.puzzle.common.solver.AStarPuzzleSolver
import com.barrybecker4.puzzle.common.solver.ConcurrentPuzzleSolver
import com.barrybecker4.puzzle.common.solver.PuzzleSolver
import com.barrybecker4.puzzle.common.solver.SequentialPuzzleSolver
import com.barrybecker4.puzzle.slidingpuzzle.model.SlideMove
import com.barrybecker4.puzzle.slidingpuzzle.model.SliderBoard


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
sealed trait Algorithm extends AlgorithmEnum[SliderBoard, SlideMove] {

  val label: String = AppContext.getLabel(this.toString)
  def getLabel: String = label

  /**
    * Create an instance of the algorithm given the controller and a refreshable.
    */
  def createSolver(controller: PuzzleController[SliderBoard, SlideMove]): PuzzleSolver[SlideMove] = {
    this match {
      case A_STAR_SEQUENTIAL => new AStarPuzzleSolver[SliderBoard, SlideMove](controller)
      case A_STAR_CONCURRENT => new AStarConcurrentPuzzleSolver[SliderBoard, SlideMove](controller)
      // this will find a solution, but not necessary the shortest path
      case SIMPLE_SEQUENTIAL => new SequentialPuzzleSolver[SliderBoard, SlideMove](controller)
      // this will find the shortest path to a solution if one exists, but takes longer
      case CONCURRENT_BREADTH => new ConcurrentPuzzleSolver[SliderBoard, SlideMove](controller, 1.0f)
      case CONCURRENT_DEPTH => new ConcurrentPuzzleSolver[SliderBoard, SlideMove](controller, 0.12f)
      case CONCURRENT_OPTIMUM => new ConcurrentPuzzleSolver[SliderBoard, SlideMove](controller, 0.3f)
    }
  }

  override def ordinal: Int = Algorithm.VALUES.indexOf(this)
}

object Algorithm {
  val VALUES: Array[AlgorithmEnum[SliderBoard, SlideMove]] = Array(
    SIMPLE_SEQUENTIAL, A_STAR_SEQUENTIAL, A_STAR_CONCURRENT, CONCURRENT_BREADTH, CONCURRENT_DEPTH, CONCURRENT_OPTIMUM
  )
}
