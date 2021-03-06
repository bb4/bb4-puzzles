package com.barrybecker4.puzzle.rubixcube.model

import org.scalatest.funsuite.AnyFunSuite


class CubeShufflerSuite extends AnyFunSuite {

  private val shuffler = CubeShuffler()

  test("Verify 2x2 cube shuffled once") {
    val cube = new Cube(2)
    assert(cube.distanceToGoal == 0)

    val shuffledCube = shuffler.shuffle(cube, 1)

    assert(shuffledCube.toString == "Cube (distanceToGoal: 8):\nHashMap((2,2,1) -> Minicube(Map(DOWN -> BLUE, RIGHT -> ORANGE, FRONT -> WHITE)), (1,2,2) -> Minicube(Map(UP -> GREEN, BACK -> RED, RIGHT -> YELLOW)), (2,2,2) -> Minicube(Map(DOWN -> BLUE, RIGHT -> ORANGE, BACK -> YELLOW)), (2,1,1) -> Minicube(Map(DOWN -> BLUE, LEFT -> RED, FRONT -> WHITE)), (1,1,2) -> Minicube(Map(UP -> GREEN, BACK -> RED, LEFT -> WHITE)), (1,1,1) -> Minicube(Map(UP -> GREEN, FRONT -> ORANGE, LEFT -> WHITE)), (2,1,2) -> Minicube(Map(DOWN -> BLUE, LEFT -> RED, BACK -> YELLOW)), (1,2,1) -> Minicube(Map(UP -> GREEN, FRONT -> ORANGE, RIGHT -> YELLOW)))")
  }


  test("Verify 2x2 cube shuffle") {
    val cube = new Cube(2)
    assert(cube.distanceToGoal == 0)

    val shuffledCube = shuffler.shuffle(cube)

    assert(shuffledCube.toString == "Cube (distanceToGoal: 23):\nHashMap((2,2,1) -> Minicube(Map(RIGHT -> BLUE, FRONT -> ORANGE, DOWN -> WHITE)), (1,2,2) -> Minicube(Map(BACK -> GREEN, UP -> RED, RIGHT -> WHITE)), (2,2,2) -> Minicube(Map(BACK -> BLUE, DOWN -> RED, RIGHT -> WHITE)), (2,1,1) -> Minicube(Map(LEFT -> GREEN, FRONT -> RED, DOWN -> YELLOW)), (1,1,2) -> Minicube(Map(UP -> BLUE, BACK -> RED, LEFT -> YELLOW)), (1,1,1) -> Minicube(Map(FRONT -> BLUE, LEFT -> ORANGE, UP -> YELLOW)), (2,1,2) -> Minicube(Map(DOWN -> GREEN, BACK -> ORANGE, LEFT -> WHITE)), (1,2,1) -> Minicube(Map(FRONT -> GREEN, RIGHT -> ORANGE, UP -> YELLOW)))")
  }

}
