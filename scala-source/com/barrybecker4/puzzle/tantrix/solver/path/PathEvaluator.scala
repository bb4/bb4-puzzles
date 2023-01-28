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
  /** If fitness is 0, then puzzle is solved. The worst fitness is this value. */
  val FITNESS_RANGE: Double = 10.0

  /** If more than this many tiles away from the required path length it doesn't matter */
  private val MAX_NUM_MISSING_TILES = 8

  private val UNPLACED_TILE_WEIGHT = 1.0

  /** How close are the endpoints of the primary path from forming a loop. */
  private val LOOP_PROXIMITY_WEIGHT = 0.4

  /** Weight to give if we actually have a primary path loop. */
  private val LOOP_WEIGHT = FITNESS_RANGE - 1.0

  /** Weight to give matching paths (includes secondary paths) */
  private val PATH_MATCH_WEIGHT = 0.7

  /** A measure of compactness. Avoids inner spaces. */
  private val COMPACTNESS_WEIGHT = 0.3

  private val INNER_SPACE_WEIGHT = 0.3
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
    val isLoop = path.isLoop
    val consistencyChecker = ConsistencyChecker(path.tiles, path.primaryPathColor)
    val numFits = consistencyChecker.numFittingTiles
    val allFit = numFits == numTiles
    var numInnerSpaces = 0
    val consistentLoop = isLoop && allFit
    var perfectLoop = false
    if (consistentLoop) {
      val tantrix = new Tantrix(path.tiles)
      val innerDetector = InnerSpaceDetector(tantrix)
      numInnerSpaces = innerDetector.numInnerSpaces()
      perfectLoop = numInnerSpaces == 0 && numTiles == path.desiredLength
    }

    val fitness =
      if (perfectLoop) 0.0
      else if (isLoop && numTiles == path.desiredLength) calcFitnessForImperfectLoop(path, numFits, numInnerSpaces)
      else calcFitnessForNonLoop(path, numTiles, numFits)

    assert(fitness >= 0, "Fitness (" + fitness + ") must be >= 0")
    assert(fitness <= FITNESS_RANGE, "Fitness (" + fitness + ") must be <= " + FITNESS_RANGE)
    assert(!fitness.isNaN, "Invalid fitness  isLoop=" + isLoop + " consistentLoop=" + consistentLoop +
      " numTiles=" + numTiles)

    fitness
  }

  /**
    * Heuristic function to determine the fitness of an imperfect loop path
    * @return approximate fitness
    */
  private def calcFitnessForImperfectLoop(path: TantrixPath, numFits: Int, numInnerSpaces: Int): Double = {
    val distance = path.getEndPointDistance
    assert(distance == 0, "Distance from loop end points should be 0. " + distance)

    val compactness = CompactnessCalculator().determineCompactness(path)

    val sum: Double = LOOP_WEIGHT +
        PATH_MATCH_WEIGHT * (numFits.toDouble / path.desiredLength) +
        COMPACTNESS_WEIGHT * compactness - INNER_SPACE_WEIGHT * numInnerSpaces
    val score: Double = FITNESS_RANGE - sum  // between 0 and 1
    assert(score >= 0, "Score not positive. " + score)
    score
  }

  /**
    * Heuristic function to determine the fitness of a non-loop path
    * @return approximate fitness
    */
  private def calcFitnessForNonLoop(path: TantrixPath, numTiles: Int, numFits: Int): Double = {
    val distance = path.getEndPointDistance
    val compactness = CompactnessCalculator().determineCompactness(path)

    val sum: Double =
      UNPLACED_TILE_WEIGHT * (MAX_NUM_MISSING_TILES - Math.min(path.desiredLength - numTiles, MAX_NUM_MISSING_TILES)) +
      LOOP_PROXIMITY_WEIGHT * distance / numTiles +
      PATH_MATCH_WEIGHT * (numFits.toDouble / numTiles) +
      COMPACTNESS_WEIGHT * compactness
    val score: Double = FITNESS_RANGE - sum
    assert(score >= 0, "Score not positive. " + score)
    score
  }

}
