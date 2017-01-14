package com.barrybecker4.puzzle.maze.model

import com.barrybecker4.common.math.MathUtil

/**
  * The three normalized directional probabilities.
  *
  * @author Barry Becker
  */
case class Probabilities(fwdProb: Double, leftProb: Double, rightProb: Double) {
  val total: Double = fwdProb + leftProb + rightProb
  private var forwardProbability = fwdProb / total
  private var leftProbability = leftProb / total
  private var rightProbability = rightProb / total

  /**
    * return a shuffled list of directions
    * they are ordered given the potentially skewed probabilities at the top.
    */
  private[model] def getShuffledDirections = {
    val rnd = MathUtil.RANDOM.nextDouble
    var directions: List[Direction] = List()
    var originalDirections = Direction.VALUES
    val sum = forwardProbability + leftProbability + rightProbability
    forwardProbability /= sum
    leftProbability /= sum
    if (rnd < forwardProbability) {
      directions :+= originalDirections.head
      originalDirections = originalDirections.tail
      directions :+= getSecondDir(originalDirections, leftProbability / (leftProbability + rightProbability))
    }
    else if (rnd >= forwardProbability && rnd < (forwardProbability + leftProb)) {
      directions :+= originalDirections(1)
      originalDirections = originalDirections.drop(1)
      directions :+= getSecondDir(originalDirections, forwardProbability / (forwardProbability + rightProbability))
    }
    else {
      directions :+= originalDirections(2)
      originalDirections = originalDirections.dropRight(1)
      directions :+= getSecondDir(originalDirections, forwardProbability / (forwardProbability + leftProbability))
    }
    // the third direction is whatever remains
    directions :+= originalDirections.head
    originalDirections = originalDirections.tail
    directions
  }

  /**
    * Determine the second direction in the list given a probability
    *
    * @return the second direction.
    */
  private def getSecondDir(twoDirections: Seq[Direction], p1: Double): Direction = {
    val rnd = MathUtil.RANDOM.nextDouble
    if (rnd < p1) twoDirections.tail.head else twoDirections.head
  }
}
