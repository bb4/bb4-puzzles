// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path

import com.barrybecker4.puzzle.tantrix.solver.path.PathSelector.RAND

import scala.collection.mutable.ListBuffer
import scala.util.Random

object PathSelector {
  val RAND = new Random()
}

/**
  * Select a path from a set of paths.
  * When testing, pass in mock evaluator.
  *
  * @author Barry Becker
  */
class PathSelector private[path](evaluator: PathEvaluator, rnd: Random = RAND) {

  def this() { this(new PathEvaluator) }

  /**
    * Skew toward selecting the best, but don't always select the best because then we
    * might always return the same random neighbor.
    *
    * @param paths list of paths to evaluate.
    * @return a random path with a likely good score. In other words, the path which is close to a valid solution.
    */
  private[path] def selectPath(paths: ListBuffer[TantrixPath]): TantrixPath = {
    var totalScore: Double = 0
    val scores: ListBuffer[Double] = ListBuffer() //paths.map(p => evaluator.evaluateFitness(p))

    for (path <- paths) {
      val score = evaluator.evaluateFitness(path)
      if (score >= PathEvaluator.SOLVED_THRESH) return path
      totalScore += score
      scores.append(score)
    }

    scores.append(10000.0)
    val r = rnd.nextDouble * totalScore
    var total: Double = 0
    var ct: Int = 0
    do {
      total += scores(ct)
      ct += 1
    } while (r > total)
    paths(ct - 1)
  }
}