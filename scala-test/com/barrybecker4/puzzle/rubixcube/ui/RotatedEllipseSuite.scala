package com.barrybecker4.puzzle.rubixcube.ui

import com.barrybecker4.puzzle.rubixcube.ui.util.RotatedEllipse
import org.scalatest.funsuite.AnyFunSuite


class RotatedEllipseSuite extends AnyFunSuite {

  test("Validate normalized angles") {

    val ellipse = RotatedEllipse(Point(0, 0), 3, 1, 0)

    assert(ellipse.getNormalizedAngle(0) == 0)
    assert(ellipse.getNormalizedAngle(HALF_PI) == HALF_PI)
    assert(ellipse.getNormalizedAngle(Math.PI) == Math.PI)
    assert(ellipse.getNormalizedAngle(-Math.PI) == Math.PI)
    assert(ellipse.getNormalizedAngle(-Math.PI + 0.01) == -Math.PI + 0.01)

    assert(ellipse.getNormalizedAngle(3.0 * HALF_PI) == -HALF_PI)
    assert(ellipse.getNormalizedAngle(- 3.0 * HALF_PI) == HALF_PI)
  }

  test("Points on simple, non-rotated ellipse") {

    val ellipse = RotatedEllipse(Point(0, 0), 3, 1, 0)

    assert(ellipse.getPointAtAngle(0) == Point(3.0f, 0f))
    assert(ellipse.getPointAtAngle(HALF_PI) == Point(6.123234E-17f, 1.0f))
    assert(ellipse.getPointAtAngle(Math.PI) == Point(-3.0f, 3.6739403E-16f))
    assert(ellipse.getPointAtAngle(-Math.PI) == Point(-3.0f, 3.6739403E-16f))

    assert(ellipse.getPointAtAngle(3 * HALF_PI) == Point(6.123234E-17f, 1.0f))
    assert(ellipse.getPointAtAngle(- 3 * HALF_PI) == Point(6.123234E-17f, 1.0f)) // should be same as HALF_PI
  }


  test("Points on simple, rotated ellipse") {

    val ellipse = RotatedEllipse(Point(0, 0), 3, 1, Math.PI / 3.0)

    assert(ellipse.getPointAtAngle(0) == Point(1.5f,2.598076f))
    assert(ellipse.getPointAtAngle(HALF_PI) == Point(-0.8660254f, 0.5f))
    assert(ellipse.getPointAtAngle(Math.PI) == Point(-1.5f, -2.598076f))
    assert(ellipse.getPointAtAngle(-Math.PI) == Point(-1.5f, -2.598076f))
  }


  test("Points on simple, rotated and translated ellipse") {

    val ellipse = RotatedEllipse(Point(2, 3), 5, 1, Math.PI / 3.0)

    assert(ellipse.getPointAtAngle(0) == Point(4.5f, 7.3301272f))
    assert(ellipse.getPointAtAngle(HALF_PI) == Point(1.1339746f, 3.5f))
  }
}
