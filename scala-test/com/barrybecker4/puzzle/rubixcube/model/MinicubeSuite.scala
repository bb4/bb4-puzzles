// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.model

import org.scalatest.funsuite.AnyFunSuite
import FaceColor._
import Direction._

class MinicubeSuite extends AnyFunSuite {

  test("Minicube rotate clockwise from front") {
    val minicube = Minicube(Map(TOP -> BLUE, LEFT -> RED))
    val rotatedMinicube = minicube.rotate(FRONT, CLOCKWISE)

    assert(rotatedMinicube == Minicube(Map(RIGHT -> BLUE, TOP -> RED)))
  }

  test("Minicube rotate counter-clockwise from front") {
    val minicube = Minicube(Map(TOP -> BLUE, LEFT -> RED))
    val rotatedMinicube = minicube.rotate(FRONT, COUNTER_CLOCKWISE)

    assert(rotatedMinicube == Minicube(Map(LEFT -> BLUE, BOTTOM -> RED)))
  }

  test("Minicube rotate clockwise from top") {
    val minicube = Minicube(Map(TOP -> BLUE, LEFT -> RED))
    val rotatedMinicube = minicube.rotate(TOP, CLOCKWISE)

    assert(rotatedMinicube == Minicube(Map(TOP -> BLUE, BACK -> RED)))
  }

  test("Minicube rotate counter-clockwise from top") {
    val minicube = Minicube(Map(TOP -> BLUE, LEFT -> RED))
    val rotatedMinicube = minicube.rotate(TOP, COUNTER_CLOCKWISE)

    assert(rotatedMinicube == Minicube(Map(TOP -> BLUE, FRONT -> RED)))
  }

}
