package com.barrybecker4.puzzle.maze.model

import com.barrybecker4.math.MathUtil
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

/**
  * Statistical checks on shuffled direction order (fixed RNG seed).
  */
class ProbabilitiesSuite extends AnyFunSuite with BeforeAndAfterEach {

  override def beforeEach(): Unit =
    MathUtil.RANDOM.setSeed(0)

  private def getDistribution(num: Int, probs: Probabilities): DistributionMap = {
    var dist = DistributionMap()
    for (_ <- 0 until num) {
      val dirs = probs.getShuffledDirections
      dist = dist.increment(dirs)
    }
    dist
  }

  test("equal probabilities") {
    val probs = Probabilities(1.0, 1.0, 1.0)
    val expDist = DistributionMap(
      forwardDist = List(31, 36, 33),
      leftDist = List(34, 33, 33),
      rightDist = List(35, 31, 34)
    )
    assert(getDistribution(100, probs) == expDist)
  }

  test("skew forward") {
    val probs = Probabilities(3.0, 1.0, 1.0)
    val expDist = DistributionMap(
      forwardDist = List(127, 18, 55),
      leftDist = List(34, 94, 72),
      rightDist = List(39, 88, 73)
    )
    assert(getDistribution(200, probs) == expDist)
  }

  test("skew left") {
    val probs = Probabilities(0.9, 1.9, 0.1)
    val expDist = DistributionMap(
      forwardDist = List(71, 20, 109),
      leftDist = List(124, 3, 73),
      rightDist = List(5, 177, 18)
    )
    assert(getDistribution(200, probs) == expDist)
  }

  test("skew right") {
    val probs = Probabilities(0.1, 0.5, 8.0)
    val expDist = DistributionMap(
      forwardDist = List(4, 161, 35),
      leftDist = List(14, 38, 148),
      rightDist = List(182, 1, 17)
    )
    assert(getDistribution(200, probs) == expDist)
  }
}
