package com.barrybecker4.puzzle.rubixcube.model

import org.scalatest.funsuite.AnyFunSuite


class CubeShufflerSuite extends AnyFunSuite {

  private val shuffler = CubeShuffler()

  test("Verify 2x2 cube shuffled once") {
    val cube = new Cube(2)
    assert(cube.distanceToGoal == 0)

    val shuffledCube = shuffler.shuffle(cube, 1)

    assert(shuffledCube.toString == "Cube (distanceToGoal: 8):\nHashMap((2,2,1) -> Minicube(Map(BOTTOM -> GREEN, RIGHT -> WHITE, FRONT -> RED)), (1,2,2) -> Minicube(Map(TOP -> BLUE, RIGHT -> YELLOW, BACK -> RED)), (2,2,2) -> Minicube(Map(BOTTOM -> GREEN, RIGHT -> WHITE, BACK -> ORANGE)), (2,1,1) -> Minicube(Map(BOTTOM -> GREEN, LEFT -> YELLOW, FRONT -> RED)), (1,1,2) -> Minicube(Map(TOP -> BLUE, BACK -> YELLOW, LEFT -> RED)), (1,1,1) -> Minicube(Map(TOP -> BLUE, FRONT -> WHITE, LEFT -> RED)), (2,1,2) -> Minicube(Map(BOTTOM -> GREEN, LEFT -> YELLOW, BACK -> ORANGE)), (1,2,1) -> Minicube(Map(TOP -> BLUE, FRONT -> YELLOW, RIGHT -> RED)))")
  }


  test("Verify 2x2 cube shuffle") {
    val cube = new Cube(2)
    assert(cube.distanceToGoal == 0)

    val shuffledCube = shuffler.shuffle(cube)

    assert(shuffledCube.toString == "Cube (distanceToGoal: 17):\nHashMap((2,2,1) -> Minicube(Map(FRONT -> BLUE, BOTTOM -> YELLOW, RIGHT -> RED)), (1,2,2) -> Minicube(Map(TOP -> BLUE, RIGHT -> YELLOW, BACK -> RED)), (2,2,2) -> Minicube(Map(RIGHT -> BLUE, BOTTOM -> YELLOW, BACK -> RED)), (2,1,1) -> Minicube(Map(BOTTOM -> BLUE, LEFT -> YELLOW, FRONT -> ORANGE)), (1,1,2) -> Minicube(Map(LEFT -> BLUE, TOP -> YELLOW, BACK -> RED)), (1,1,1) -> Minicube(Map(TOP -> BLUE, LEFT -> YELLOW, FRONT -> RED)), (2,1,2) -> Minicube(Map(BOTTOM -> BLUE, LEFT -> YELLOW, BACK -> RED)), (1,2,1) -> Minicube(Map(RIGHT -> BLUE, TOP -> YELLOW, FRONT -> RED)))")
  }

}
