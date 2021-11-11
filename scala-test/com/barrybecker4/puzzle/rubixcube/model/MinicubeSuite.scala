// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.model

import org.scalatest.funsuite.AnyFunSuite
import FaceColor._
import Direction._
import Orientation._
import com.barrybecker4.puzzle.rubixcube.Location

class MinicubeSuite extends AnyFunSuite {

  private val loc: Location = (1, 2, 3)

  test("Minicube rotate clockwise from front") {
    val minicube = Minicube(Map(UP -> BLUE, LEFT -> RED), loc)
    val rotatedMinicube = minicube.rotate(FRONT, CLOCKWISE)

    assert(rotatedMinicube == Minicube(Map(RIGHT -> BLUE, UP -> RED), loc))
  }

  test("Minicube rotate counter-clockwise from front") {
    val minicube = Minicube(Map(UP -> BLUE, LEFT -> RED), loc)
    val rotatedMinicube = minicube.rotate(FRONT, COUNTER_CLOCKWISE)

    assert(rotatedMinicube == Minicube(Map(LEFT -> BLUE, DOWN -> RED), loc))
  }

  test("Minicube rotate clockwise from top") {
    val minicube = Minicube(Map(UP -> BLUE, LEFT -> RED), loc)
    val rotatedMinicube = minicube.rotate(UP, CLOCKWISE)

    assert(rotatedMinicube == Minicube(Map(UP -> BLUE, BACK -> RED), loc))
  }

  test("Minicube rotate counter-clockwise from top") {
    val minicube = Minicube(Map(UP -> BLUE, LEFT -> RED), loc)
    val rotatedMinicube = minicube.rotate(UP, COUNTER_CLOCKWISE)

    assert(rotatedMinicube == Minicube(Map(UP -> BLUE, FRONT -> RED), loc))
  }

}
