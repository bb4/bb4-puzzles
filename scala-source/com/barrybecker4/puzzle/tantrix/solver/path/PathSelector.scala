// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path

import scala.collection.mutable.ListBuffer
import scala.util.Random
import scala.util.boundary
import scala.util.boundary.break


object PathSelector {
  val RAND = new Random()
}

/**
  * Select a path from a set of paths.
  * When testing, pass in mock evaluator.
  * @author Barry Becker
  */
class PathSelector private[path](evaluator: PathEvaluator, rnd: Random = RAND) {

  def this(rnd: Random) = { this(new PathEvaluator, rnd) }

  /** Skew toward selecting the best, but don't always select the best because then we
    * might always return the same random neighbor.
    * @param paths list of paths to evaluate.
    * @return a random path with a likely good score. In other words, the path which is close to a valid solution.
    */
  private[path] def selectPath(paths: ListBuffer[TantrixPath]): TantrixPath = boundary:
    require(paths.nonEmpty, "There must be at least one path to select from.")
    val scores = paths.map { path =>
      val score = PathEvaluator.FITNESS_RANGE - evaluator.evaluateFitness(path)
      if (score <= 0) break(path) // a path that cannot be given a positive weight is returned as is
      score
    }
    paths(rouletteIndex(scores, rnd.nextDouble() * scores.sum))

  /** Roulette wheel selection: the bigger the score, the more of the wheel the entry occupies.
    * @param scores positive weights
    * @param target a point on the wheel in the range [0, sum(scores)]
    * @return index of the first entry whose cumulative score reaches the target. A target of 0 selects the first.
    */
  private def rouletteIndex(scores: Seq[Double], target: Double): Int = {
    val index = scores.scanLeft(0.0)(_ + _).tail.indexWhere(target <= _)
    if (index >= 0) index else scores.size - 1
  }
}
