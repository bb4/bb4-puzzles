package com.barrybecker4.puzzle.rubixcube.model

import org.scalatest.funsuite.AnyFunSuite


class CubeShufflerSuite extends AnyFunSuite {

  private val shuffler = CubeShuffler()

  test("Verify 2x2 cube shuffled once") {
    val cube = new Cube(2)
    assert(cube.distanceToGoal == 0)

    val shuffledCube = shuffler.shuffle(cube, 1)

    assert(shuffledCube.toString == "Cube (distanceToGoal: 8):\nHashMap((2,2,1) -> Minicube(Map(DOWN -> GREEN, RIGHT -> YELLOW, FRONT -> RED)), (1,2,2) -> Minicube(Map(UP -> BLUE, BACK -> WHITE, RIGHT -> ORANGE)), (2,2,2) -> Minicube(Map(DOWN -> GREEN, RIGHT -> YELLOW, BACK -> ORANGE)), (2,1,1) -> Minicube(Map(DOWN -> GREEN, LEFT -> WHITE, FRONT -> RED)), (1,1,2) -> Minicube(Map(UP -> BLUE, BACK -> WHITE, LEFT -> RED)), (1,1,1) -> Minicube(Map(UP -> BLUE, FRONT -> YELLOW, LEFT -> RED)), (2,1,2) -> Minicube(Map(DOWN -> GREEN, LEFT -> WHITE, BACK -> ORANGE)), (1,2,1) -> Minicube(Map(UP -> BLUE, FRONT -> YELLOW, RIGHT -> ORANGE)))")
  }


  test("Verify 2x2 cube shuffle") {
    val cube = new Cube(2)
    assert(cube.distanceToGoal == 0)

    val shuffledCube = shuffler.shuffle(cube)

    assert(shuffledCube.toString == "Cube (distanceToGoal: 23):\nHashMap((2,2,1) -> Minicube(Map(RIGHT -> GREEN, FRONT -> YELLOW, DOWN -> RED)), (1,2,2) -> Minicube(Map(BACK -> BLUE, UP -> WHITE, RIGHT -> RED)), (2,2,2) -> Minicube(Map(BACK -> GREEN, DOWN -> WHITE, RIGHT -> RED)), (2,1,1) -> Minicube(Map(LEFT -> BLUE, FRONT -> WHITE, DOWN -> ORANGE)), (1,1,2) -> Minicube(Map(UP -> GREEN, BACK -> WHITE, LEFT -> ORANGE)), (1,1,1) -> Minicube(Map(FRONT -> GREEN, LEFT -> YELLOW, UP -> ORANGE)), (2,1,2) -> Minicube(Map(DOWN -> BLUE, BACK -> YELLOW, LEFT -> RED)), (1,2,1) -> Minicube(Map(FRONT -> BLUE, RIGHT -> YELLOW, UP -> ORANGE)))")
  }

}
