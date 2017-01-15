package com.barrybecker4.puzzle.maze.model

import com.barrybecker4.common.math.MathUtil

/**
  * The three normalized directional probabilities.
  *
  * @author Barry Becker
  */
case class Probabilities(fwdProbability: Double, leftProbability: Double, rightProbability: Double) {
  val total: Double = fwdProbability + leftProbability + rightProbability
  private val forwardProb = fwdProbability / total
  private val leftProb = leftProbability / total
  private val rightProb = rightProbability / total

  /**
    * @return a shuffled list of directions
    * they are ordered given the potentially skewed probabilities at the top.
    */
  def getShuffledDirections: List[Direction] = {
    //scala.util.Random.shuffle(Direction.VALUES)

    val rnd = MathUtil.RANDOM.nextDouble
    var directions: List[Direction] = List()

    if (rnd < forwardProb) {
      directions = FORWARD +: getNextTwoDirs(List(LEFT, RIGHT), leftProb / (leftProb + rightProb))
    }
    else if (rnd >= forwardProb && rnd < (forwardProb + leftProbability)) {
      directions = LEFT +: getNextTwoDirs(List(FORWARD, RIGHT), forwardProb / (forwardProb + rightProb))
    }
    else {
      directions = RIGHT +: getNextTwoDirs(List(FORWARD, LEFT), forwardProb / (forwardProb + leftProb))
    }
    directions
  }

  /**
    * Determine the second direction in the list given a probability
    * @param prob probability of taking the first of the two directions as the next element
    * @return the second direction.
    */
  private def getNextTwoDirs(twoDirections: Seq[Direction], prob: Double): List[Direction] = {
    val rnd = MathUtil.RANDOM.nextDouble
    if (rnd < prob) List(twoDirections.tail.head, twoDirections.head)
    else List(twoDirections.head, twoDirections.tail.head)
  }
}
