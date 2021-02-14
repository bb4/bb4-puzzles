package com.barrybecker4.puzzle.rubixcube.model

import org.scalatest.funsuite.AnyFunSuite


class CubeShufflerSuite extends AnyFunSuite {

  private val shuffler = CubeShuffler()

  test("Verify 2x2 cube shuffled once") {
    val cube = new Cube(2)
    assert(cube.distanceToGoal == 0)

    val shuffledCube = shuffler.shuffle(cube, 1)

    assert(shuffledCube.toString == "Cube (distanceToGoal: 8):\nHashMap((2,2,1) -> Minicube(Map(BOTTOM -> GREEN, RIGHT -> WHITE, FRONT -> RED)), (1,2,2) -> Minicube(Map(TOP -> BLUE, BACK -> YELLOW, RIGHT -> ORANGE)), (2,2,2) -> Minicube(Map(BOTTOM -> GREEN, RIGHT -> WHITE, BACK -> ORANGE)), (2,1,1) -> Minicube(Map(BOTTOM -> GREEN, LEFT -> YELLOW, FRONT -> RED)), (1,1,2) -> Minicube(Map(TOP -> BLUE, BACK -> YELLOW, LEFT -> RED)), (1,1,1) -> Minicube(Map(TOP -> BLUE, FRONT -> WHITE, LEFT -> RED)), (2,1,2) -> Minicube(Map(BOTTOM -> GREEN, LEFT -> YELLOW, BACK -> ORANGE)), (1,2,1) -> Minicube(Map(TOP -> BLUE, FRONT -> WHITE, RIGHT -> ORANGE)))")
  }


  test("Verify 2x2 cube shuffle") {
    val cube = new Cube(2)
    assert(cube.distanceToGoal == 0)

    val shuffledCube = shuffler.shuffle(cube)

    assert(shuffledCube.toString == "Cube (distanceToGoal: 23):\nHashMap((2,2,1) -> Minicube(Map(RIGHT -> GREEN, FRONT -> WHITE, BOTTOM -> RED)), (1,2,2) -> Minicube(Map(BACK -> BLUE, TOP -> YELLOW, RIGHT -> RED)), (2,2,2) -> Minicube(Map(BACK -> GREEN, BOTTOM -> YELLOW, RIGHT -> RED)), (2,1,1) -> Minicube(Map(LEFT -> BLUE, FRONT -> YELLOW, BOTTOM -> ORANGE)), (1,1,2) -> Minicube(Map(TOP -> GREEN, BACK -> YELLOW, LEFT -> ORANGE)), (1,1,1) -> Minicube(Map(FRONT -> GREEN, LEFT -> WHITE, TOP -> ORANGE)), (2,1,2) -> Minicube(Map(BOTTOM -> BLUE, BACK -> WHITE, LEFT -> RED)), (1,2,1) -> Minicube(Map(FRONT -> BLUE, RIGHT -> WHITE, TOP -> ORANGE)))")
  }

}
