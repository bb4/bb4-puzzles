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

    val expFace: Map[(Int, Int), FaceColor] = Map(
      (1, 1) -> FaceColor.RED,
      (1, 2) -> FaceColor.RED,
      (2, 1) -> FaceColor.YELLOW,
      (2, 2) -> FaceColor.YELLOW
    )

    assert(modifiedCube.getFace(LEFT) == expFace)
  }

  test("Cube rotate FRONT 1, clockwise") {

    val cube = new Cube(2)

    // before rotating the left face should be yellow and bottom should be green
    val leftFace: Map[(Int, Int), FaceColor] = Map(
      (1, 1) -> FaceColor.YELLOW,
      (1, 2) -> FaceColor.YELLOW,
      (2, 1) -> FaceColor.YELLOW,
      (2, 2) -> FaceColor.YELLOW
    )
    val bottomFace: Map[(Int, Int), FaceColor] = Map(
      (1, 1) -> FaceColor.GREEN,
      (1, 2) -> FaceColor.GREEN,
      (2, 1) -> FaceColor.GREEN,
      (2, 2) -> FaceColor.GREEN
    )
    val rightFace: Map[(Int, Int), FaceColor] = Map(
      (1, 1) -> FaceColor.ORANGE,
      (1, 2) -> FaceColor.ORANGE,
      (2, 1) -> FaceColor.ORANGE,
      (2, 2) -> FaceColor.ORANGE
    )
    // right is orange
    assert(cube.getFace(LEFT) == leftFace)
    assert(cube.getFace(DOWN) == bottomFace)

    val cubeMove = CubeMove(FRONT, 1, CLOCKWISE)  // rotate front face clockwise
    val modifiedCube = cube.doMove(cubeMove)

    val expLeftFace: Map[(Int, Int), FaceColor] = Map(
      (1, 1) -> FaceColor.GREEN,
      (1, 2) -> FaceColor.YELLOW,
      (2, 1) -> FaceColor.GREEN,
      (2, 2) -> FaceColor.YELLOW
    )
    val expBottomFace: Map[(Int, Int), FaceColor] = Map(
      (1, 1) -> FaceColor.WHITE,
      (1, 2) -> FaceColor.GREEN,
      (2, 1) -> FaceColor.WHITE,
      (2, 2) -> FaceColor.GREEN
    )
    val expTopFace: Map[(Int, Int), FaceColor] = Map(
      (1, 1) -> FaceColor.YELLOW,
      (1, 2) -> FaceColor.BLUE,
      (2, 1) -> FaceColor.YELLOW,
      (2, 2) -> FaceColor.BLUE
    )
    val expFrontFace: Map[(Int, Int), FaceColor] = Map(
      (1, 1) -> FaceColor.RED,
      (1, 2) -> FaceColor.RED,
      (2, 1) -> FaceColor.RED,
      (2, 2) -> FaceColor.RED
    )

    assert(modifiedCube.getFace(LEFT) == expLeftFace)
    assert(modifiedCube.getFace(DOWN) == expBottomFace)
    assert(modifiedCube.getFace(UP) == expTopFace)
    assert(modifiedCube.getFace(FRONT) == expFrontFace)
  }
}
