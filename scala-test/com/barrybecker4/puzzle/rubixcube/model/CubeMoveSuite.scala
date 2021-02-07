package com.barrybecker4.puzzle.rubixcube.model

import com.barrybecker4.puzzle.rubixcube.model.Direction.CLOCKWISE
import org.scalatest.funsuite.AnyFunSuite
import FaceColor._
import Direction._


class CubeMoveSuite extends AnyFunSuite {

  test("Minicube rotate edge piece clockwise from front") {
    val cubeMove = CubeMove(FRONT, 2, CLOCKWISE)
    val rotatedMinicube = cubeMove.rotateMinicube((1, 2, 3), Minicube(Map(TOP -> BLUE, LEFT -> RED)), 3)

    assert(rotatedMinicube == (2, 3, 3) -> Minicube(Map(RIGHT -> BLUE, TOP -> RED)))
  }

  test("Minicube rotate edge piece counter-clockwise from front") {
    val cubeMove = CubeMove(FRONT, 2, COUNTER_CLOCKWISE)
    val rotatedMinicube = cubeMove.rotateMinicube((1, 2, 3), Minicube(Map(TOP -> BLUE, LEFT -> RED)), 3)

    assert(rotatedMinicube == (2, 1, 3) -> Minicube(Map(LEFT -> BLUE, BOTTOM -> RED)))
  }


  test("Minicube rotate edge piece clockwise from top") {
    val cubeMove = CubeMove(TOP, 1, CLOCKWISE)
    val rotatedMinicube = cubeMove.rotateMinicube((1, 2, 3), Minicube(Map(TOP -> BLUE, BACK -> RED)), 3)

    assert(rotatedMinicube == (1, 3, 2) -> Minicube(Map(TOP -> BLUE, RIGHT -> RED)))
  }

  test("Minicube rotate edge piece counter-clockwise from top") {
    val cubeMove = CubeMove(TOP, 1, COUNTER_CLOCKWISE)
    val rotatedMinicube = cubeMove.rotateMinicube((1, 2, 3), Minicube(Map(TOP -> BLUE, LEFT -> RED)), 3)

    assert(rotatedMinicube == (1, 1, 2) -> Minicube(Map(TOP -> BLUE, FRONT -> RED)))
  }

  test("Minicube rotate corner piece clockwise from left") {
    val cubeMove = CubeMove(LEFT, 1, CLOCKWISE)
    val rotatedMinicube =
      cubeMove.rotateMinicube((1, 3, 1), Minicube(Map(TOP -> BLUE, RIGHT -> RED, FRONT -> GREEN)), 3)

    assert(rotatedMinicube == (3, 3, 1) -> Minicube(Map(FRONT -> BLUE, RIGHT -> RED, BOTTOM -> GREEN)))
  }

  test("Minicube rotate corner piece counte-clockwise from left") {
    val cubeMove = CubeMove(LEFT, 1, COUNTER_CLOCKWISE)
    val rotatedMinicube =
      cubeMove.rotateMinicube((1, 3, 1), Minicube(Map(TOP -> BLUE, RIGHT -> RED, FRONT -> GREEN)), 3)

    assert(rotatedMinicube == (1, 3, 3) -> Minicube(Map(BACK -> BLUE, RIGHT -> RED, TOP -> GREEN)))
  }

  test("Minicube rotate corner piece clockwise from front") {
    val cubeMove = CubeMove(FRONT, 1, CLOCKWISE)
    val rotatedMinicube =
      cubeMove.rotateMinicube((1, 3, 1), Minicube(Map(TOP -> BLUE, RIGHT -> RED, FRONT -> GREEN)), 3)

    assert(rotatedMinicube == (3, 3, 1) -> Minicube(Map(RIGHT -> BLUE, BOTTOM -> RED, FRONT -> GREEN)))
  }

}
