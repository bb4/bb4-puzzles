package com.barrybecker4.puzzle.maze.model

import com.barrybecker4.math.MathUtil
import org.scalatest.BeforeAndAfter
import org.junit.Assert.assertEquals
import org.junit.Test
import org.scalatest.funsuite.AnyFunSuite

/**
  * @author Barry Becker
  */
class ProbabilitiesSuite extends AnyFunSuite with BeforeAndAfter  {

  /** instance under test */
  private var probs: Probabilities = _

  before {
    MathUtil.RANDOM.setSeed(0)
  }

  @Test
  @throws[Exception]
  def testEqualProbabilities(): Unit = {
    probs = Probabilities(1.0, 1.0, 1.0)
    val expDist = new DistributionMap(List(43, 28, 29), List(57, 22, 21), List(0, 50, 50))
    assertEquals("Unexpected distribution", expDist, getDistribution(100))
  }

  @Test
  @throws[Exception]
  def testSkewForwardProbabilities(): Unit = {
    probs = Probabilities(3.0, 1.0, 1.0)
    val expDist = new DistributionMap(List(115, 25, 60), List(85, 60, 55), List(0, 115, 85))
    assertEquals("Unexpected distribution", expDist, getDistribution(200))
  }

  @Test
  @throws[Exception]
  def testSkewLeftProbabilities(): Unit = {
    probs = Probabilities(0.9, 1.9, 0.1)
    val expDist = new DistributionMap(List(61, 17, 122), List(139, 2, 59), List(0, 181, 19))
    assertEquals("Unexpected distribution", expDist, getDistribution(200))
  }

  @Test
  @throws[Exception]
  def testSkewRightProbabilities(): Unit = {
    probs = Probabilities(0.1, 0.5, 8.0)
    val expDist = new DistributionMap(List(3, 172, 25), List(101, 25, 74), List(96, 3, 101))
    assertEquals("Unexpected distribution", expDist, getDistribution(200))
  }

  /**
    * @param num number of shuffled trials to run
    * @return map from direction to list of counts for the three positions that direction fell in.
    */
  private def getDistribution(num: Int) = {
    val dist = new DistributionMap
    for (i <- 0 until num) {
        val dirs = probs.getShuffledDirections
        dist.increment(dirs)
    }
    dist
  }
}
