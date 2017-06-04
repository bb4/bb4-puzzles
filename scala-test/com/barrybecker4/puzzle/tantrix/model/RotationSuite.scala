// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model

import com.barrybecker4.puzzle.tantrix.model.RotationEnum._
import org.scalatest.FunSuite

/**
  * @author Barry Becker
  */
class RotationSuite extends FunSuite {

  test("RotateBy2") {
    assertResult(ANGLE_120) { ANGLE_0.rotateBy(2) }
  }

  test("RotateByNegative2") {
    assertResult(ANGLE_240) { ANGLE_0.rotateBy(-2) }
  }

  test("RotateByNothing") {
    assertResult(ANGLE_0) { ANGLE_0.rotateBy(0) }
  }
}
