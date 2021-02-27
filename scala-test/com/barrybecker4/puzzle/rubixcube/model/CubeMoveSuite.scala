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
      (1,1,1) -> Minicube(Map(UP -> BLUE, FRONT -> YELLOW, LEFT -> RED)),
      (1,1,2) -> Minicube(Map(UP -> BLUE, BACK -> WHITE, LEFT -> RED)),
      (2,1,1) -> Minicube(Map(DOWN -> GREEN, LEFT -> WHITE, FRONT -> RED)),
      (2,1,2) -> Minicube(Map(DOWN -> GREEN, LEFT -> WHITE, BACK -> ORANGE))
    )

    assert(modifiedCube.getSlice(LEFT, 1) == expSlice)
  }

  test("Cube rotate FRONT 1, clockwise") {

    val cube = new Cube(2)

    // before rotating
    val leftSlice = Map(
      (1,1,1) -> Minicube(Map(UP -> BLUE, LEFT -> WHITE, FRONT -> RED)),
      (1,1,2) -> Minicube(Map(UP -> BLUE, LEFT -> WHITE, BACK -> ORANGE)),
      (2,1,1) -> Minicube(Map(DOWN -> GREEN, LEFT -> WHITE, FRONT -> RED)),
      (2,1,2) -> Minicube(Map(DOWN -> GREEN, LEFT -> WHITE, BACK -> ORANGE))
    )
    val bottomSlice: Map[(Int, Int, Int), Minicube] = Map(
      (2,1,1) -> Minicube(Map(DOWN -> GREEN, LEFT -> WHITE, FRONT -> RED)),
      (2,1,2) -> Minicube(Map(DOWN -> GREEN, LEFT -> WHITE, BACK -> ORANGE)),
      (2,2,1) -> Minicube(Map(DOWN -> GREEN, RIGHT -> YELLOW, FRONT -> RED)),
      (2,2,2) -> Minicube(Map(DOWN -> GREEN, RIGHT -> YELLOW, BACK -> ORANGE))
    )


    assert(cube.getSlice(LEFT, 1) == leftSlice, "unexpected left slice")
    assert(cube.getSlice(UP, 2) == bottomSlice, "unexpected down slice")

    val cubeMove = CubeMove(FRONT, 1, CLOCKWISE)  // rotate front face clockwise
    val modifiedCube = cube.doMove(cubeMove)

    val expLeftSlice = Map(
      (1,2,1) -> Minicube(Map(RIGHT -> BLUE, UP -> WHITE, FRONT -> RED)),
      (1,2,2) -> Minicube(Map(UP -> BLUE, RIGHT -> YELLOW, BACK -> ORANGE)),
      (2,2,1) -> Minicube(Map(RIGHT -> BLUE, DOWN -> YELLOW, FRONT -> RED)),
      (2,2,2) -> Minicube(Map(DOWN -> GREEN, RIGHT -> YELLOW, BACK -> ORANGE))
    )
    val expBottomSlice = Map(
      (2,1,1) -> Minicube(Map(LEFT -> GREEN, DOWN -> YELLOW, FRONT -> RED)),
      (2,1,2) -> Minicube(Map(DOWN -> GREEN, LEFT -> WHITE, BACK -> ORANGE)),
      (2,2,1) -> Minicube(Map(RIGHT -> BLUE, DOWN -> YELLOW, FRONT -> RED)),
      (2,2,2) -> Minicube(Map(DOWN -> GREEN, RIGHT -> YELLOW, BACK -> ORANGE))
    )
    val expTopSlice = Map(
      (2,1,1) -> Minicube(Map(LEFT -> GREEN, DOWN -> YELLOW, FRONT -> RED)),
      (2,1,2) -> Minicube(Map(DOWN -> GREEN, LEFT -> WHITE, BACK -> ORANGE)),
      (2,2,1) -> Minicube(Map(RIGHT -> BLUE, DOWN -> YELLOW, FRONT -> RED)),
      (2,2,2) -> Minicube(Map(DOWN -> GREEN, RIGHT -> YELLOW, BACK -> ORANGE))
    )
    val expFrontSlice = Map(
      (1,1,2) -> Minicube(Map(UP -> BLUE, LEFT -> WHITE, BACK -> ORANGE)),
      (1,2,2) -> Minicube(Map(UP -> BLUE, RIGHT -> YELLOW, BACK -> ORANGE)),
      (2,1,2) -> Minicube(Map(DOWN -> GREEN, LEFT -> WHITE, BACK -> ORANGE)),
      (2,2,2) -> Minicube(Map(DOWN -> GREEN, RIGHT -> YELLOW, BACK -> ORANGE))
    )

    assert(modifiedCube.getSlice(LEFT, 2) == expLeftSlice)
    assert(modifiedCube.getSlice(UP, 2) == expBottomSlice)
    assert(modifiedCube.getSlice(UP, 2) == expTopSlice)
    assert(modifiedCube.getSlice(FRONT, 2) == expFrontSlice)
  }
}
