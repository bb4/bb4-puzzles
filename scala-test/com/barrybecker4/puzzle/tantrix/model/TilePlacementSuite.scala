// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT

package com.barrybecker4.puzzle.tantrix.model

import com.barrybecker4.common.geometry.IntLocation
import org.scalatest.FunSuite

/**
  * Created by barry on 3/25/2017.
  */
class TilePlacementSuite extends FunSuite {

  private val tiles = new HexTiles()
  private val placement = TilePlacement(tiles.getTile(1), IntLocation(2, 2), RotationEnum.ANGLE_0)

  test("TilePlacement construction") {
    assertResult(IntLocation(2, 2)) { placement.location }
    assertResult(PathColor.RED) { placement.getPathColor(0) }
    assertResult(PathColor.BLUE) { placement.getPathColor(1) }
  }

  test("Existant outgoing path locations") {
    assertResult(Map(0 -> IntLocation(2, 3), 2 -> IntLocation(1, 2))) {
      placement.getOutgoingPathLocations(PathColor.RED)
    }
    assertResult(Map(1 -> IntLocation(1, 3), 3 -> IntLocation(2, 1))) {
      placement.getOutgoingPathLocations(PathColor.BLUE)
    }
    assertResult(Map(5 -> IntLocation(3, 3), 4 -> IntLocation(3, 2))) {
      placement.getOutgoingPathLocations(PathColor.YELLOW)
    }
  }

  // negative test
  test("getOutgoingPathLocations for color that has no paths") {
    val caught = intercept[AssertionError] {
      placement.getOutgoingPathLocations(PathColor.GREEN)
    }
    assert(caught.getMessage.contains("Must always be two paths."))
  }


  test("TilePlacement rotate") {
    val rotatedPlacement = placement.rotate()

    assertResult(PathColor.YELLOW) { rotatedPlacement.getPathColor(0) }
    assertResult(PathColor.RED) { rotatedPlacement.getPathColor(1) }

    assertResult(Map(1 -> IntLocation(1, 3), 3 -> IntLocation(2, 1))) {
      rotatedPlacement.getOutgoingPathLocations(PathColor.RED)
    }
  }
}
