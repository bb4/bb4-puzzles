// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.Tantrix.model

import com.barrybecker4.puzzle.tantrix1.model.Rotation
import org.scalatest.FunSuite

/**
  * @author Barry Becker
  */
class RotationSuite extends FunSuite {

  test("RotateBy2") {
    assertResult(Rotation.ANGLE_120) { Rotation.ANGLE_0.rotateBy(2) }
  }

  test("RotateByNegative2") {
    assertResult(Rotation.ANGLE_240) { Rotation.ANGLE_0.rotateBy(-2) }
  }

  test("RotateByNothing") {
    assertResult(Rotation.ANGLE_0) { Rotation.ANGLE_0.rotateBy(0) }
  }
}
