package com.barrybecker4.puzzle.rubixcube.model

import com.barrybecker4.puzzle.rubixcube.model.Direction.CLOCKWISE
import org.scalatest.funsuite.AnyFunSuite
import FaceColor._
import Direction._


class CubeMoveSuite extends AnyFunSuite {

  test("Minicube rotate clockwise from front") {
    val cubeMove = CubeMove(FRONT, 2, CLOCKWISE)
    val rotatedMinicube = cubeMove.rotateMinicube((1, 2, 3), Minicube(Map(TOP -> BLUE, LEFT -> RED)), 3)

    assert(rotatedMinicube == (2, 3, 3) -> Minicube(Map(RIGHT -> BLUE, TOP -> RED)))
  }

  test("Minicube rotate counter-clockwise from front") {
    val cubeMove = CubeMove(FRONT, 2, COUNTER_CLOCKWISE)
    val rotatedMinicube = cubeMove.rotateMinicube((1, 2, 3), Minicube(Map(TOP -> BLUE, LEFT -> RED)), 3)

    assert(rotatedMinicube == (2, 1, 3) -> Minicube(Map(LEFT -> BLUE, BOTTOM -> RED)))
  }


  test("Minicube rotate clockwise from top") {
    val cubeMove = CubeMove(TOP, 1, CLOCKWISE)
    val rotatedMinicube = cubeMove.rotateMinicube((1, 2, 3), Minicube(Map(TOP -> BLUE, LEFT -> RED)), 3)

    assert(rotatedMinicube == (1, 1, 2) -> Minicube(Map(TOP -> BLUE, BACK -> RED)))
  }

  test("Minicube rotate counter-clockwise from top") {
    val cubeMove = CubeMove(TOP, 1, COUNTER_CLOCKWISE)
    val rotatedMinicube = cubeMove.rotateMinicube((1, 2, 3), Minicube(Map(TOP -> BLUE, LEFT -> RED)), 3)

    assert(rotatedMinicube == (1, 3, 2) -> Minicube(Map(TOP -> BLUE, FRONT -> RED)))
  }
}
