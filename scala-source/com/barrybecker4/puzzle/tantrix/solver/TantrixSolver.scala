// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver

import com.barrybecker4.puzzle.common.solver.PuzzleSolver
import com.barrybecker4.puzzle.tantrix.model.{TantrixBoard, TilePlacement}

import scala.collection.Seq

/**
  * Abstract base class for tantrix puzzle solving strategies (see strategy pattern).
  * Subclasses do the hard work of actually solving the puzzle.
  * This is the controller in the model-view-controller pattern.
  *
  * @author Barry Becker
  */
abstract class TantrixSolver private[solver](var board: TantrixBoard) extends PuzzleSolver[TilePlacement] {

  protected var solution: TantrixBoard = _

  /**
    * Derived classes must provide the implementation for this abstract method.
    *
    * @return true if a solution is found.
    */
  def solve: Option[Seq[TilePlacement]]

  /**
    * @return the list of successfully placed pieces so far.
    */
  def getSolution: TantrixBoard = solution
}
