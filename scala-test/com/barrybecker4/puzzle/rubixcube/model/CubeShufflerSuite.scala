package com.barrybecker4.puzzle.rubixcube.model

import org.scalatest.funsuite.AnyFunSuite


class CubeShufflerSuite extends AnyFunSuite {

  private val shuffler = CubeShuffler()

  test("Verify 2x2 cube shuffled once") {
    val cube = new Cube(2)
    assert(cube.distanceToGoal == 0)

    val shuffledCube = shuffler.shuffle(cube, 1)

    assert(shuffledCube.toString == "Cube (distanceToGoal: 8):\nHashMap((2,2,1) -> Minicube(Map(DOWN -> GREEN, RIGHT -> WHITE, FRONT -> RED)), (1,2,2) -> Minicube(Map(UP -> BLUE, BACK -> YELLOW, RIGHT -> ORANGE)), (2,2,2) -> Minicube(Map(DOWN -> GREEN, RIGHT -> WHITE, BACK -> ORANGE)), (2,1,1) -> Minicube(Map(DOWN -> GREEN, LEFT -> YELLOW, FRONT -> RED)), (1,1,2) -> Minicube(Map(UP -> BLUE, BACK -> YELLOW, LEFT -> RED)), (1,1,1) -> Minicube(Map(UP -> BLUE, FRONT -> WHITE, LEFT -> RED)), (2,1,2) -> Minicube(Map(DOWN -> GREEN, LEFT -> YELLOW, BACK -> ORANGE)), (1,2,1) -> Minicube(Map(UP -> BLUE, FRONT -> WHITE, RIGHT -> ORANGE)))")
  }


  test("Verify 2x2 cube shuffle") {
    val cube = new Cube(2)
    assert(cube.distanceToGoal == 0)

    val shuffledCube = shuffler.shuffle(cube)

    assert(shuffledCube.toString == "Cube (distanceToGoal: 23):\nHashMap((2,2,1) -> Minicube(Map(RIGHT -> GREEN, FRONT -> WHITE, DOWN -> RED)), (1,2,2) -> Minicube(Map(BACK -> BLUE, UP -> YELLOW, RIGHT -> RED)), (2,2,2) -> Minicube(Map(BACK -> GREEN, DOWN -> YELLOW, RIGHT -> RED)), (2,1,1) -> Minicube(Map(LEFT -> BLUE, FRONT -> YELLOW, DOWN -> ORANGE)), (1,1,2) -> Minicube(Map(UP -> GREEN, BACK -> YELLOW, LEFT -> ORANGE)), (1,1,1) -> Minicube(Map(FRONT -> GREEN, LEFT -> WHITE, UP -> ORANGE)), (2,1,2) -> Minicube(Map(DOWN -> BLUE, BACK -> WHITE, LEFT -> RED)), (1,2,1) -> Minicube(Map(FRONT -> BLUE, RIGHT -> WHITE, UP -> ORANGE)))")
  }

}
