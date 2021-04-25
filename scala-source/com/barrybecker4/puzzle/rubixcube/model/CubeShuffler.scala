package com.barrybecker4.puzzle.rubixcube.model

import scala.util.Random
import Orientation.PRIMARY_ORIENTATIONS
import Direction._
import CubeShuffler.RND


object CubeShuffler {
  val RND = new Random(1)
}

/** While it can take an infinite number of rotations to reach perfect randomness, in practive
  * about 20 is enough for a standard cube.
  * https://math.stackexchange.com/questions/816055/minimum-number-of-random-moves-needed-to-uniformly-scramble-a-rubiks-cube
  * https://theconversation.com/how-hard-is-it-to-scramble-rubiks-cube-129916
  */
case class CubeShuffler(rnd: Random = RND) {

  // should increase for better shuffling
  private val randomRotationsForSize: Array[Int] = Array(0, 0, 3, 6, 10, 20)

  def shuffle(cube: Cube, numRotations: Int = -1): Cube = {
    var shuffledCube = cube
    val size = cube.size
    val num = if (numRotations < 0) randomRotationsForSize(size) else numRotations

    for (i <- 1 to num) {
      val randomMove = CubeMove(PRIMARY_ORIENTATIONS(rnd.nextInt(PRIMARY_ORIENTATIONS.length)), rnd.nextInt(size) + 1, CLOCKWISE)
      shuffledCube = shuffledCube.doMove(randomMove)
    }
    shuffledCube
  }
}
