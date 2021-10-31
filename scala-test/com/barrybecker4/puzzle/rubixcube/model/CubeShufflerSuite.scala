package com.barrybecker4.puzzle.rubixcube.model

import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random


class CubeShufflerSuite extends AnyFunSuite {


  test("Verify 2x2 cube shuffled once") {
    val cube = new Cube(2)
    assert(cube.distanceToGoal == 0)

    val shuffler = CubeShuffler(new Random(1))
    val shuffledCube = shuffler.shuffle(cube, 1)

    assert(shuffledCube.toString == "Cube (distanceToGoal: 20):\nHashMap((2,2,1) -> Minicube(Map(DOWN -> BLUE, RIGHT -> ORANGE, FRONT -> WHITE),(2,2,1)), (1,2,2) -> Minicube(Map(UP -> GREEN, BACK -> RED, RIGHT -> YELLOW),(1,1,2)), (2,2,2) -> Minicube(Map(DOWN -> BLUE, RIGHT -> ORANGE, BACK -> YELLOW),(2,2,2)), (2,1,1) -> Minicube(Map(DOWN -> BLUE, LEFT -> RED, FRONT -> WHITE),(2,1,1)), (1,1,2) -> Minicube(Map(UP -> GREEN, BACK -> RED, LEFT -> WHITE),(1,1,1)), (1,1,1) -> Minicube(Map(UP -> GREEN, FRONT -> ORANGE, LEFT -> WHITE),(1,2,1)), (2,1,2) -> Minicube(Map(DOWN -> BLUE, LEFT -> RED, BACK -> YELLOW),(2,1,2)), (1,2,1) -> Minicube(Map(UP -> GREEN, FRONT -> ORANGE, RIGHT -> YELLOW),(1,2,2)))")
  }


  test("Verify 2x2 cube shuffle") {
    val cube = new Cube(2)
    assert(cube.distanceToGoal == 0)

    val shuffler = CubeShuffler(new Random(1))
    val shuffledCube = shuffler.shuffle(cube)

    assert(shuffledCube.toString == "Cube (distanceToGoal: 42):\nHashMap((2,2,1) -> Minicube(Map(RIGHT -> GREEN, FRONT -> ORANGE, DOWN -> YELLOW),(1,2,2)), (1,2,2) -> Minicube(Map(UP -> GREEN, BACK -> RED, RIGHT -> YELLOW),(1,1,2)), (2,2,2) -> Minicube(Map(DOWN -> BLUE, RIGHT -> ORANGE, BACK -> YELLOW),(2,2,2)), (2,1,1) -> Minicube(Map(LEFT -> BLUE, DOWN -> ORANGE, FRONT -> WHITE),(2,2,1)), (1,1,2) -> Minicube(Map(BACK -> BLUE, LEFT -> RED, UP -> YELLOW),(2,1,2)), (1,1,1) -> Minicube(Map(FRONT -> GREEN, LEFT -> ORANGE, UP -> WHITE),(1,2,1)), (2,1,2) -> Minicube(Map(BACK -> BLUE, LEFT -> RED, DOWN -> WHITE),(2,1,1)), (1,2,1) -> Minicube(Map(FRONT -> GREEN, RIGHT -> RED, UP -> WHITE),(1,1,1)))")
  }

}
