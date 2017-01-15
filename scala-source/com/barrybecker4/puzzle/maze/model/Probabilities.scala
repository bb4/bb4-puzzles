package com.barrybecker4.puzzle.maze.model

import com.barrybecker4.common.math.MathUtil

/**
  * The three normalized directional probabilities.
  *
  * @author Barry Becker
  */
case class Probabilities(fwdProbability: Double, leftProbability: Double, rightProbability: Double) {
  val total: Double = fwdProbability + leftProbability + rightProbability
  private var forwardProb = fwdProbability / total
  private var leftProb = leftProbability / total
  private var rightProb = rightProbability / total

  /**
    * @return a shuffled list of directions
    * they are ordered given the potentially skewed probabilities at the top.
    */
  private[model] def getShuffledDirections = {
    scala.util.Random.shuffle(Direction.VALUES)
    /* This was all wrong
    val rnd = MathUtil.RANDOM.nextDouble
    var directions: List[Direction] = List()
    var originalDirections = Direction.VALUES
    val sum = forwardProb + leftProb + rightProb
    forwardProb /= sum
    leftProb /= sum
    if (rnd < forwardProb) {
      directions :+= originalDirections.head
      originalDirections = originalDirections.tail
      directions :+= getSecondDir(originalDirections, leftProb / (leftProb + rightProb))
    }
    else if (rnd >= forwardProb && rnd < (forwardProb + leftProbability)) {
      directions :+= originalDirections(1)
      originalDirections = originalDirections.drop(1)
      directions :+= getSecondDir(originalDirections, forwardProb / (forwardProb + rightProb))
    }
    else {
      directions :+= originalDirections(2)
      originalDirections = originalDirections.dropRight(1)
      directions :+= getSecondDir(originalDirections, forwardProb / (forwardProb + leftProb))
    }
    // the third direction is whatever remains
    directions :+= originalDirections.head
    originalDirections = originalDirections.tail
    directions*/
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
