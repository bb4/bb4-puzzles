// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path

import com.barrybecker4.puzzle.tantrix.model.{HexTile, HexUtil, Tantrix}
import com.barrybecker4.puzzle.tantrix.solver.verification.{ConsistencyChecker, InnerSpaceDetector}

/**
  * Evaluates the fitness of a tantrix path.
  * It gets the top score if it is a loop and all the path colors match.
  *
  * @author Barry Becker
  */
object PathEvaluator {
  /** When reached, the puzzle is solved. */
  val SOLVED_THRESH = 3.1

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

class PathEvaluator {
  /**
    * The main criteria for quality of the path is
    * 1) How close the ends of the path are to each other. Perfection achieved when we have a closed loop.
    * 2) Better if more matching secondary path colors
    * 3) Fewer inner spaces and a bbox with less area.
    *
    * @return a measure of how good the path is.
    */
  def evaluateFitness(path: TantrixPath): Double = {
    val numTiles = path.size
    val distance = path.getEndPointDistance
    val isLoop = distance == 0 && path.isLoop
    val checker = new ConsistencyChecker(path.getTilePlacements, path.primaryPathColor)
    val numFits = checker.numFittingTiles
    val allFit = numFits == numTiles
    val consistentLoop = isLoop && allFit
    var perfectLoop = false
    val compactness = determineCompactness(path)
    if (consistentLoop) {
      val tantrix = new Tantrix(path.getTilePlacements)
      val innerDetector = new InnerSpaceDetector(tantrix)
      perfectLoop = !innerDetector.hasInnerSpaces
    }
    assert(numFits <= numTiles)

    val fitness = PathEvaluator.SOLVED_THRESH -
      PathEvaluator.LOOP_PROXIMITY_WEIGHT * (numTiles - distance) / (0.1 + numTiles) -
      (if (isLoop) PathEvaluator.LOOP_WEIGHT else 0) -
      numFits.toDouble / numTiles * PathEvaluator.PATH_MATCH_WEIGHT -
      compactness * PathEvaluator.COMPACTNESS -
      (if (consistentLoop) PathEvaluator.CONSISTENT_LOOP_BONUS else 0) -
      (if (perfectLoop) PathEvaluator.PERFECT_LOOP_BONUS else 0)

    assert(fitness.isNaN, "Invalid fitness  isLoop=" + isLoop + " consistentLoop=" + consistentLoop +
      " numTiles=" + numTiles + " distance=" + distance)
    Math.max(0, fitness)
  }

  /**
    * First add all the tiles to a hash keyed on location.
    * Then for every one of the six sides of each tile, add one if the
    * neighbor is in the hash. Return (num nbrs in hash - 2(numTiles-1))/numTiles
    *
    * @param path the path to determine compactness of.
    * @return measure of path compactness between 0 and ~1
    */
  private def determineCompactness(path: TantrixPath) = {
    val locationHash = path.getTilePlacements.map(_.location).toSet
    val numTiles = path.size

    var ct = 0
    for (p <- path.getTilePlacements) {
      for (i <- 0 until HexTile.NUM_SIDES) {
          if (locationHash.contains(HexUtil.getNeighborLocation(p.location, i)))
            ct += 1
      }
    }
    (ct - 2.0 * (numTiles - 1)) / numTiles * 0.5
  }
}
