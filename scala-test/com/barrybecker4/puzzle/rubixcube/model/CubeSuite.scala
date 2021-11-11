package com.barrybecker4.puzzle.rubixcube.model

import org.scalatest.funsuite.AnyFunSuite
import com.barrybecker4.puzzle.rubixcube.model.CubeSuite.RND
import Orientation._
import scala.util.Random


object CubeSuite {
  val RND = new Random(1)
}

class CubeSuite extends AnyFunSuite {


  test("Distance to goal for solved cube should be 0") {
    val cube = new Cube(2)
    assert(cube.distanceToGoal == 0)
  }

  test("Distance to goal for after 1 rotation should be 8") {
    val cube = new Cube(2)
    val move = CubeMove(UP, 2, Direction.CLOCKWISE)
    assert(cube.doMove(move).distanceToGoal == 20)
  }

  test("Distance to goal for shuffled cube of size 2 should be 22") {
    val cube = new Cube(2).shuffle(RND)
    assert(cube.distanceToGoal == 42)
  }

  test("Distance to goal for shuffled cube of size 3 should be 5") {
    val cube = new Cube(3).shuffle(RND)
    assert(cube.distanceToGoal == 50)
  }
}
