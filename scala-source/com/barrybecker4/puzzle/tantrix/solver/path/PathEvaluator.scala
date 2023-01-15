// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path

import com.barrybecker4.puzzle.tantrix.model.{HexTile, HexUtil, Tantrix}
import com.barrybecker4.puzzle.tantrix.solver.verification.{CompactnessCalculator, ConsistencyChecker, InnerSpaceDetector}
import PathEvaluator.*

/**
  * Evaluates the fitness of a tantrix path.
  * It gets the top score if it is a loop and all the path colors match.
  *
  * @author Barry Becker
  */
object PathEvaluator {
  /** When reached, the puzzle is solved. */
  val FITNESS_RANGE: Double = 8.0

  /** If more than this many tiles away from the required path length it doesn't matter */
  private val MAX_NUM_MISSING_TILES = 4

  /** How close are the endpoints of the primary path from forming a loop. */
  private val LOOP_PROXIMITY_WEIGHT = 0.3

  /** Weight to give if we actually have a primary path loop. */
  private val LOOP_WEIGHT = 0.3

  /** Weight to give matching paths (includes secondary paths) */
  private val PATH_MATCH_WEIGHT = 1.0

  /** We have a loop and all paths match */
  private val CONSISTENT_LOOP_BONUS = 0.6

  /** consistent loop and no inner spaces. */
  private val PERFECT_LOOP_BONUS = 2.0

  /** A measure of compactness. Avoids inner spaces. */
  private val COMPACTNESS = 0.2
}

case class PathEvaluator() {

  /** The main criteria for quality of the path is
    * 0) How close the path is to using all the tiles.
    * 1) How close the ends of the path are to each other. Perfection achieved when we have a closed loop.
    * 2) Better if more matching secondary path colors
    * 3) Fewer inner spaces and a bbox with less area.
    * Getting a closed loop with no internal spaces is more important than having a lot of matching paths,
    * since tiles can be swapped to get more matches after the loop is determined.
    * @return a measure of how good the path is. Smaller number indicates better fitness.
    */
  def evaluateFitness(path: TantrixPath): Double = {
    val numTiles = path.size
    val distance = path.getEndPointDistance
    val isLoop = distance == 0 && path.isLoop
    val checker = ConsistencyChecker(path.tiles, path.primaryPathColor)
    val numFits = checker.numFittingTiles
    val allFit = numFits == numTiles
    val consistentLoop = isLoop && allFit
    var perfectLoop = false
    val compactness = CompactnessCalculator().determineCompactness(path)
    if (consistentLoop) {
      val tantrix = new Tantrix(path.tiles)
      val innerDetector = InnerSpaceDetector(tantrix)
      perfectLoop = !innerDetector.hasInnerSpaces
    }
    assert(numFits <= numTiles)


    val fitness = if (perfectLoop && allFit) 0.0 else {
      val sum: Double = (MAX_NUM_MISSING_TILES - Math.min(path.desiredLength - numTiles, MAX_NUM_MISSING_TILES)) +
        LOOP_PROXIMITY_WEIGHT * distance / (0.1 + numTiles) +
        (if (isLoop) LOOP_WEIGHT else 0) +
        (numFits.toDouble / numTiles) * PATH_MATCH_WEIGHT +
        compactness * COMPACTNESS +
        (if (consistentLoop) CONSISTENT_LOOP_BONUS else 0) +
        (if (perfectLoop) PERFECT_LOOP_BONUS else 0)
      val score: Double = FITNESS_RANGE - sum
      assert(score >= 0, "Score not positive. " + score)
      if (score < 1.0)
        print("***SCORE < 2.0 : " + score)
      score
    }

    println("fitness = " + fitness)
    assert(fitness >= 0, "Fitness (" + fitness + ") must be >= 0")
    assert(fitness <= FITNESS_RANGE, "Fitness (" + fitness + ") must be <= " + FITNESS_RANGE)
    assert(!fitness.isNaN, "Invalid fitness  isLoop=" + isLoop + " consistentLoop=" + consistentLoop +
      " numTiles=" + numTiles + " distance=" + distance)

    fitness
  }

}
