// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path

import com.barrybecker4.puzzle.tantrix.model.Tantrix
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

  private val compactnessCalculator = CompactnessCalculator()

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
    val fitness = calcFitness(path)
    assert(!fitness.isNaN, s"Invalid fitness for isLoop=${path.isLoop} numTiles=${path.size}")
    assert(fitness >= 0, s"Fitness ($fitness) must be >= 0")
    assert(fitness <= FITNESS_RANGE, s"Fitness ($fitness) must be <= $FITNESS_RANGE")
    fitness
  }

  private def calcFitness(path: TantrixPath): Double = {
    val numTiles = path.size
    val isLoop = path.isLoop
    val numFits = ConsistencyChecker(path.tiles, path.primaryPathColor).numFittingTiles
    val numInnerSpaces = if (isLoop && numFits == numTiles) countInnerSpaces(path) else 0
    val isPerfectLoop = isLoop && numFits == numTiles && numInnerSpaces == 0 && numTiles == path.desiredLength

    if (isPerfectLoop) 0.0
    else if (isLoop && numTiles == path.desiredLength) calcFitnessForImperfectLoop(path, numFits, numInnerSpaces)
    else calcFitnessForNonLoop(path, numTiles, numFits)
  }

  /** @return the number of empty hexes that are enclosed by the tiles of the path */
  private def countInnerSpaces(path: TantrixPath): Int =
    InnerSpaceDetector(new Tantrix(path.tiles)).numInnerSpaces()

  /**
    * Heuristic function to determine the fitness of an imperfect loop path
    * @return approximate fitness
    */
  private def calcFitnessForImperfectLoop(path: TantrixPath, numFits: Int, numInnerSpaces: Int): Double = {
    val distance = path.getEndPointDistance
    assert(distance == 0, "Distance from loop end points should be 0. " + distance)

    val compactness = compactnessCalculator.determineCompactness(path)

    toFitness(
      LOOP_WEIGHT +
        PATH_MATCH_WEIGHT * (numFits.toDouble / path.desiredLength) +
        COMPACTNESS_WEIGHT * compactness - INNER_SPACE_WEIGHT * numInnerSpaces
    )
  }

  /**
    * Heuristic function to determine the fitness of a non-loop path
    * @return approximate fitness
    */
  private def calcFitnessForNonLoop(path: TantrixPath, numTiles: Int, numFits: Int): Double = {
    val distance = path.getEndPointDistance
    val compactness = compactnessCalculator.determineCompactness(path)
    val numMissingTiles = Math.min(path.desiredLength - numTiles, MAX_NUM_MISSING_TILES)

    toFitness(
      UNPLACED_TILE_WEIGHT * (MAX_NUM_MISSING_TILES - numMissingTiles) +
        LOOP_PROXIMITY_WEIGHT * distance / numTiles +
        PATH_MATCH_WEIGHT * (numFits.toDouble / numTiles) +
        COMPACTNESS_WEIGHT * compactness
    )
  }

  /** @param goodness larger is better. The larger it is, the closer the fitness is to 0.
    * @return fitness, where smaller is better
    */
  private def toFitness(goodness: Double): Double = {
    val score = FITNESS_RANGE - goodness
    assert(score >= 0, "Score not positive. " + score)
    score
  }
}
