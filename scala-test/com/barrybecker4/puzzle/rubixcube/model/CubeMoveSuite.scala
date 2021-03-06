package com.barrybecker4.puzzle.rubixcube.model

import com.barrybecker4.puzzle.rubixcube.model.Direction.CLOCKWISE
import org.scalatest.funsuite.AnyFunSuite
import FaceColor._
import Direction._


class CubeMoveSuite extends AnyFunSuite {

  test("Minicube rotate edge piece clockwise from front") {
    val cubeMove = CubeMove(FRONT, 2, CLOCKWISE)
    val rotatedMinicube = cubeMove.rotateMinicube((1, 2, 3), Minicube(Map(UP -> BLUE, LEFT -> RED)), 3)

    assert(rotatedMinicube == (2, 3, 3) -> Minicube(Map(RIGHT -> BLUE, UP -> RED)))
  }

  test("Minicube rotating 4 times should get back to original state") {
    val cubeMove = CubeMove(FRONT, 2, CLOCKWISE)
    val origMinicube = Minicube(Map(UP -> BLUE, LEFT -> RED))
    var rotatedMinicube = cubeMove.rotateMinicube((1, 2, 3), origMinicube, 3)
    rotatedMinicube = cubeMove.rotateMinicube(rotatedMinicube._1, rotatedMinicube._2, 3)
    rotatedMinicube = cubeMove.rotateMinicube(rotatedMinicube._1, rotatedMinicube._2, 3)
    rotatedMinicube = cubeMove.rotateMinicube(rotatedMinicube._1, rotatedMinicube._2, 3)

    assert(rotatedMinicube == (1, 2, 3) -> origMinicube)
  }

  test("Minicube rotate edge piece counter-clockwise from front") {
    val cubeMove = CubeMove(FRONT, 2, COUNTER_CLOCKWISE)
    val rotatedMinicube = cubeMove.rotateMinicube((1, 2, 3), Minicube(Map(UP -> BLUE, LEFT -> RED)), 3)

    assert(rotatedMinicube == (2, 1, 3) -> Minicube(Map(LEFT -> BLUE, DOWN -> RED)))
  }

  test("Minicube rotate edge piece clockwise from top") {
    val cubeMove = CubeMove(UP, 1, CLOCKWISE)
    val rotatedMinicube = cubeMove.rotateMinicube((1, 2, 3), Minicube(Map(UP -> BLUE, BACK -> RED)), 3)

    assert(rotatedMinicube == (1, 3, 2) -> Minicube(Map(UP -> BLUE, RIGHT -> RED)))
  }

  test("Minicube rotate edge piece counter-clockwise from top") {
    val cubeMove = CubeMove(UP, 1, COUNTER_CLOCKWISE)
    val rotatedMinicube = cubeMove.rotateMinicube((1, 2, 3), Minicube(Map(UP -> BLUE, LEFT -> RED)), 3)

    assert(rotatedMinicube == (1, 1, 2) -> Minicube(Map(UP -> BLUE, FRONT -> RED)))
  }

  test("Minicube rotate corner piece clockwise from left") {
    val cubeMove = CubeMove(LEFT, 1, CLOCKWISE)
    val rotatedMinicube =
      cubeMove.rotateMinicube((1, 3, 1), Minicube(Map(UP -> BLUE, RIGHT -> RED, FRONT -> GREEN)), 3)

    assert(rotatedMinicube == (3, 3, 1) -> Minicube(Map(FRONT -> BLUE, RIGHT -> RED, DOWN -> GREEN)))
  }

  test("Minicube rotate corner piece counte-clockwise from left") {
    val cubeMove = CubeMove(LEFT, 1, COUNTER_CLOCKWISE)
    val rotatedMinicube =
      cubeMove.rotateMinicube((1, 3, 1), Minicube(Map(UP -> BLUE, RIGHT -> RED, FRONT -> GREEN)), 3)

    assert(rotatedMinicube == (1, 3, 3) -> Minicube(Map(BACK -> BLUE, RIGHT -> RED, UP -> GREEN)))
  }

  test("Minicube rotate corner piece clockwise from front") {
    val cubeMove = CubeMove(FRONT, 1, CLOCKWISE)
    val rotatedMinicube =
      cubeMove.rotateMinicube((1, 3, 1), Minicube(Map(UP -> BLUE, RIGHT -> RED, FRONT -> GREEN)), 3)

    assert(rotatedMinicube == (3, 3, 1) -> Minicube(Map(RIGHT -> BLUE, DOWN -> RED, FRONT -> GREEN)))
  }

  test("Cube rotate TOP 1, clockwise") {

    val cube = new Cube(2)

    val cubeMove = CubeMove(UP, 1, CLOCKWISE)
    val modifiedCube = cube.doMove(cubeMove)

    val expSlice: Map[(Int, Int, Int), Minicube] = Map(
      (1,1,1) -> Minicube(Map(UP -> GREEN, FRONT -> ORANGE, LEFT -> WHITE)),
      (1,1,2) -> Minicube(Map(UP -> GREEN, BACK -> RED, LEFT -> WHITE)),
      (2,1,1) -> Minicube(Map(DOWN -> BLUE, LEFT -> RED, FRONT -> WHITE)),
      (2,1,2) -> Minicube(Map(DOWN -> BLUE, LEFT -> RED, BACK -> YELLOW))
    )

    assert(modifiedCube.getSlice(LEFT, 1) == expSlice)
  }

  test("Cube rotate FRONT 1, clockwise") {

    val cube = new Cube(2)

    // before rotating
    val leftSlice = Map(
      (1,1,1) -> Minicube(Map(UP -> GREEN, LEFT -> RED, FRONT -> WHITE)),
      (1,1,2) -> Minicube(Map(UP -> GREEN, LEFT -> RED, BACK -> YELLOW)),
      (2,1,1) -> Minicube(Map(DOWN -> BLUE, LEFT -> RED, FRONT -> WHITE)),
      (2,1,2) -> Minicube(Map(DOWN -> BLUE, LEFT -> RED, BACK -> YELLOW))
    )
    val downSlice: Map[(Int, Int, Int), Minicube] = Map(
      (2,1,1) -> Minicube(Map(DOWN -> BLUE, LEFT -> RED, FRONT -> WHITE)),
      (2,1,2) -> Minicube(Map(DOWN -> BLUE, LEFT -> RED, BACK -> YELLOW)),
      (2,2,1) -> Minicube(Map(DOWN -> BLUE, RIGHT -> ORANGE, FRONT -> WHITE)),
      (2,2,2) -> Minicube(Map(DOWN -> BLUE, RIGHT -> ORANGE, BACK -> YELLOW))
    )

    assert(cube.getSlice(LEFT, 1) == leftSlice, "unexpected left slice")
    assert(cube.getSlice(UP, 2) == downSlice, "unexpected down slice")

    val cubeMove = CubeMove(FRONT, 1, CLOCKWISE)  // rotate front face clockwise
    val modifiedCube = cube.doMove(cubeMove)

    val expLeftSlice = Map(
      (1,2,1) -> Minicube(Map(RIGHT -> GREEN, UP -> RED, FRONT -> WHITE)),
      (1,2,2) -> Minicube(Map(UP -> GREEN, RIGHT -> ORANGE, BACK -> YELLOW)),
      (2,2,1) -> Minicube(Map(RIGHT -> GREEN, DOWN -> ORANGE, FRONT -> WHITE)),
      (2,2,2) -> Minicube(Map(DOWN -> BLUE, RIGHT -> ORANGE, BACK -> YELLOW))
    )
    val expDownSlice = Map(
      (2,1,1) -> Minicube(Map(LEFT -> BLUE, DOWN -> ORANGE, FRONT -> WHITE)),
      (2,1,2) -> Minicube(Map(DOWN -> BLUE, LEFT -> RED, BACK -> YELLOW)),
      (2,2,1) -> Minicube(Map(RIGHT -> GREEN, DOWN -> ORANGE, FRONT -> WHITE)),
      (2,2,2) -> Minicube(Map(DOWN -> BLUE, RIGHT -> ORANGE, BACK -> YELLOW))
    )
    val expTopSlice = Map(
      (2,1,1) -> Minicube(Map(LEFT -> BLUE, DOWN -> ORANGE, FRONT -> WHITE)),
      (2,1,2) -> Minicube(Map(DOWN -> BLUE, LEFT -> RED, BACK -> YELLOW)),
      (2,2,1) -> Minicube(Map(RIGHT -> GREEN, DOWN -> ORANGE, FRONT -> WHITE)),
      (2,2,2) -> Minicube(Map(DOWN -> BLUE, RIGHT -> ORANGE, BACK -> YELLOW))
    )
    val expFrontSlice = Map(
      (1,1,2) -> Minicube(Map(UP -> GREEN, LEFT -> RED, BACK -> YELLOW)),
      (1,2,2) -> Minicube(Map(UP -> GREEN, RIGHT -> ORANGE, BACK -> YELLOW)),
      (2,1,2) -> Minicube(Map(DOWN -> BLUE, LEFT -> RED, BACK -> YELLOW)),
      (2,2,2) -> Minicube(Map(DOWN -> BLUE, RIGHT -> ORANGE, BACK -> YELLOW))
    )

    assert(modifiedCube.getSlice(LEFT, 2) == expLeftSlice, "Unexpected left slice")
    assert(modifiedCube.getSlice(UP, 2) == expDownSlice, "Unexpected bottom slice")
    assert(modifiedCube.getSlice(UP, 2) == expTopSlice, "Unexpected top slice")
    assert(modifiedCube.getSlice(FRONT, 2) == expFrontSlice, "Unexpected front slice")
  }
}
