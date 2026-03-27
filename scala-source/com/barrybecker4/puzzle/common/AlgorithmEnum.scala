// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.common

import com.barrybecker4.puzzle.common.solver.PuzzleSolver
import scala.reflect.Enum as ScalaEnum

/**
  * Common supertype for each puzzle’s `enum Algorithm` (Scala 3).
  * Implementations must be Scala `enum` types so `ordinal` and exhaustiveness come from `scala.reflect.Enum`.
  *
  * @author Barr Becker
  */
trait AlgorithmEnum[P, M] extends ScalaEnum:

  def getLabel: String

  def createSolver(controller: PuzzleController[P, M]): PuzzleSolver[M]

object AlgorithmEnum:

  /**
    * Each puzzle defines `enum Algorithm extends AlgorithmEnum[P, M]`; `Array` is invariant, so
    * `Algorithm.values` needs a single widened reference for UI that expects `Array[AlgorithmEnum[P, M]]`.
    */
  def widenArray[P, M, E <: AlgorithmEnum[P, M]](values: Array[E]): Array[AlgorithmEnum[P, M]] =
    values.asInstanceOf[Array[AlgorithmEnum[P, M]]]
