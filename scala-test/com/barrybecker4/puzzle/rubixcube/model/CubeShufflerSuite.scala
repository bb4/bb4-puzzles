package com.barrybecker4.puzzle.rubixcube.model

import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random


class CubeShufflerSuite extends AnyFunSuite {


  test("Verify 2x2 cube shuffled once") {
    val cube = new Cube(2)
    assert(cube.distanceToGoal == 0)

    val shuffler = CubeShuffler(new Random(1))
    val shuffledCube = shuffler.shuffle(cube, 1)

    assert(shuffledCube.toString == "Cube (distanceToGoal: 8):\nHashMap((2,2,1) -> Minicube(Map(DOWN -> BLUE, RIGHT -> ORANGE, FRONT -> WHITE),(2,2,1)), (1,2,2) -> Minicube(Map(UP -> GREEN, BACK -> RED, RIGHT -> YELLOW),(1,1,2)), (2,2,2) -> Minicube(Map(DOWN -> BLUE, RIGHT -> ORANGE, BACK -> YELLOW),(2,2,2)), (2,1,1) -> Minicube(Map(DOWN -> BLUE, LEFT -> RED, FRONT -> WHITE),(2,1,1)), (1,1,2) -> Minicube(Map(UP -> GREEN, BACK -> RED, LEFT -> WHITE),(1,1,1)), (1,1,1) -> Minicube(Map(UP -> GREEN, FRONT -> ORANGE, LEFT -> WHITE),(1,2,1)), (2,1,2) -> Minicube(Map(DOWN -> BLUE, LEFT -> RED, BACK -> YELLOW),(2,1,2)), (1,2,1) -> Minicube(Map(UP -> GREEN, FRONT -> ORANGE, RIGHT -> YELLOW),(1,2,2)))")
  }


  test("Verify 2x2 cube shuffle") {
    val cube = new Cube(2)
    assert(cube.distanceToGoal == 0)

    val shuffler = CubeShuffler(new Random(1))
    val shuffledCube = shuffler.shuffle(cube)

    assert(shuffledCube.toString == "Cube (distanceToGoal: 22):\nHashMap((2,2,1) -> Minicube(Map(FRONT -> GREEN, DOWN -> ORANGE, RIGHT -> YELLOW),(1,2,2)), (1,2,2) -> Minicube(Map(UP -> BLUE, BACK -> RED, RIGHT -> WHITE),(2,1,1)), (2,2,2) -> Minicube(Map(DOWN -> GREEN, RIGHT -> RED, BACK -> YELLOW),(1,1,2)), (2,1,1) -> Minicube(Map(DOWN -> GREEN, FRONT -> RED, LEFT -> WHITE),(1,1,1)), (1,1,2) -> Minicube(Map(BACK -> BLUE, UP -> ORANGE, LEFT -> YELLOW),(2,2,2)), (1,1,1) -> Minicube(Map(UP -> BLUE, LEFT -> RED, FRONT -> YELLOW),(2,1,2)), (2,1,2) -> Minicube(Map(DOWN -> GREEN, BACK -> ORANGE, LEFT -> WHITE),(1,2,1)), (1,2,1) -> Minicube(Map(UP -> BLUE, FRONT -> ORANGE, RIGHT -> WHITE),(2,2,1)))")
  }

}
