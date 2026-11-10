// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.maze.model

import com.barrybecker4.puzzle.maze.model.Direction._
import scala.util.Random

/**
  * The three normalized directional probabilities.
  */
case class Probabilities(fwdProbability: Double, leftProbability: Double, rightProbability: Double) {
  val total: Double = fwdProbability + leftProbability + rightProbability
  private val forwardProb = fwdProbability / total
  private val leftProb = leftProbability / total
  private val rightProb = rightProbability / total

  /** @return a shuffled list of directions
    * they are ordered given the potentially skewed probabilities at the top.
    */
  def getShuffledDirections(rnd: Random): List[Direction] = {

    val roll = rnd.nextDouble()
    var directions: List[Direction] = List()

    if (roll < forwardProb) {
      directions = FORWARD +: getNextTwoDirs(List(LEFT, RIGHT), leftProb / (leftProb + rightProb), rnd)
    } else if (roll < forwardProb + leftProb) {
      directions = LEFT +: getNextTwoDirs(List(FORWARD, RIGHT), forwardProb / (forwardProb + rightProb), rnd)
    } else {
      directions = RIGHT +: getNextTwoDirs(List(FORWARD, LEFT), forwardProb / (forwardProb + leftProb), rnd)
    }
    directions
  }

  /** Determine the second direction in the list given a probability
    * @param prob probability of taking the first of the two directions as the next element
    * @return the second direction.
    */
  private def getNextTwoDirs(twoDirections: Seq[Direction], prob: Double, rnd: Random): List[Direction] = {
    val roll = rnd.nextDouble()
    if (roll < prob) List(twoDirections.tail.head, twoDirections.head)
    else List(twoDirections.head, twoDirections.tail.head)
  }
}
