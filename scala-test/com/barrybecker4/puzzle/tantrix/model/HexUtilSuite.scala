// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model

import com.barrybecker4.common.geometry.IntLocation
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil.*
import com.barrybecker4.puzzle.tantrix.model.HexUtilSuite.TOL
import org.scalactic.{Equality, TolerantNumerics}
import org.scalatest.funsuite.AnyFunSuite

/**
  * @author Barry Becker
  */
object HexUtilSuite {
  private val TOL = 0.001
}

class HexUtilSuite extends AnyFunSuite {

  private var tantrix: Tantrix = _
  implicit val doubleEq: Equality[Double] = TolerantNumerics.tolerantDoubleEquality(TOL)


  test("GetValidNeighborLocation") {
    tantrix = new TantrixBoard(THREE_TILES).tantrix
    assertResult(IntLocation(21, 22)) { HexUtil.getNeighborLocation(TantrixBoard.INITIAL_LOCATION, 0) }
    assertResult(IntLocation(20, 21)) { HexUtil.getNeighborLocation(TantrixBoard.INITIAL_LOCATION, 1) }
    assertResult(IntLocation(20, 20)) { HexUtil.getNeighborLocation(TantrixBoard.INITIAL_LOCATION, 2) }
    assertResult(IntLocation(21, 20)) { HexUtil.getNeighborLocation(TantrixBoard.INITIAL_LOCATION, 3) }
    assertResult(IntLocation(22, 20)) { HexUtil.getNeighborLocation(TantrixBoard.INITIAL_LOCATION, 4) }
    assertResult(IntLocation(22, 21)) { HexUtil.getNeighborLocation(TantrixBoard.INITIAL_LOCATION, 5) }
  }

  test("Invalid neighbor location") {
    tantrix = new TantrixBoard(THREE_TILES).tantrix
    val caught = intercept[IllegalArgumentException] {
      HexUtil.getNeighborLocation(TantrixBoard.INITIAL_LOCATION, 6)
    }
    assert(caught.getMessage == "Unexpected direction: 6")
  }

  test("GetDistanceBetweenDiag") {
    assert(1.8027 === HexUtil.distanceBetween(IntLocation(19, 21), IntLocation(20, 22)), "for distance")
  }

  test("GetDistanceBetweenSameRow") {
    assert(1.0 === HexUtil.distanceBetween(IntLocation(19, 21), IntLocation(19, 22)), "for distance")
  }

  test("GetDistanceBetweenSameCol") {
    assert(2.0 === HexUtil.distanceBetween(IntLocation(19, 20), IntLocation(19, 22)), "for distance")
  }

  test("GetDistanceBetweenSameSpace") {
    assert(0.0 === HexUtil.distanceBetween(IntLocation(20, 21), IntLocation(20, 21)), "for distance")
  }
}