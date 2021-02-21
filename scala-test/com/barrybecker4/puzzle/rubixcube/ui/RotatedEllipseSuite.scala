package com.barrybecker4.puzzle.rubixcube.ui

import com.barrybecker4.puzzle.rubixcube.model.Direction.{CLOCKWISE, COUNTER_CLOCKWISE}
import com.barrybecker4.puzzle.rubixcube.model.{BACK, DOWN, FRONT, LEFT, Minicube, RIGHT, UP}
import org.scalatest.funsuite.AnyFunSuite


class RotatedEllipseSuite extends AnyFunSuite {

  test("Points on simple, non-rotated ellipse") {

    val ellipse = RotatedEllipse(0, 0, 3, 1, 0)

    assert(ellipse.getPointAtAngle(0) == (3.0f, 0))
    assert(ellipse.getPointAtAngle(HALF_PI) == (6.123234E-17f, 1.0f))
    assert(ellipse.getPointAtAngle(Math.PI) == (-3.0f, 3.6739403E-16f))
    assert(ellipse.getPointAtAngle(-Math.PI) == (-3.0f, 3.6739403E-16f))
  }


  test("Points on simple, rotated ellipse") {

    val ellipse = RotatedEllipse(0, 0, 3, 1, Math.PI / 3.0)

    assert(ellipse.getPointAtAngle(0) == (1.5f,2.598076f))
    assert(ellipse.getPointAtAngle(HALF_PI) == (-0.8660254f, 0.5f))
    assert(ellipse.getPointAtAngle(Math.PI) == (-1.5f, -2.598076f))
    assert(ellipse.getPointAtAngle(-Math.PI) == (-1.5f, -2.598076f))
  }


}
