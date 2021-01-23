package com.barrybecker4.puzzle.sudoku.model

import org.junit.Assert.{assertEquals, assertTrue}
import org.scalatest.funsuite.AnyFunSuite


class ValueAssignerSuite extends AnyFunSuite {

  private val assigner = ValueAssigner(BoardComponents.COMPONENTS(2))
  private val initialValues = Map(
    (1, 1) -> Set(1),
    (1, 2) -> Set(2, 3, 4),
    (2, 1) -> Set(2, 3, 4),
    (2, 2) -> Set(2, 3, 4),

    (1, 3) -> Set(3),
    (1, 4) -> Set(1, 2, 4),
    (2, 3) -> Set(1, 2, 4),
    (2, 4) -> Set(1, 2, 4),

    (3, 1) -> Set(1, 2, 3, 4),
    (3, 2) -> Set(1, 2, 3, 4),
    (4, 1) -> Set(1, 2, 3, 4),
    (4, 2) -> Set(1, 2, 3, 4),

    (3, 3) -> Set(1, 2, 4),
    (3, 4) -> Set(1, 2, 4),
    (4, 3) -> Set(1, 2, 4),
    (4, 4) -> Set(3),
  )


  test("test for size=2 pos=(1, 2) assign valid 2") {
    val newValuesMap = assigner.assign((1, 2), 2, initialValues)

    assertTrue(newValuesMap.isDefined)
    assertEquals("Unexpected",
      "HashMap((2,2) -> Set(3, 4), (4,1) -> Set(1, 2, 3, 4), (1,2) -> Set(2), (1,4) -> Set(4), (1,1) -> Set(1), (2,4) -> Set(1, 2), (3,2) -> Set(1, 3, 4), (3,1) -> Set(1, 2, 3, 4), (3,3) -> Set(1, 2, 4), (4,3) -> Set(1, 2, 4), (2,3) -> Set(1, 2), (1,3) -> Set(3), (3,4) -> Set(1, 2), (2,1) -> Set(3, 4), (4,2) -> Set(1, 3, 4), (4,4) -> Set(3))",
      newValuesMap.get.toString)
  }

  test("test for size=2 pos=(2, 1) assign valid 3") {
    val newValuesMap = assigner.assign((2, 1), 3, initialValues)

    assertTrue(newValuesMap.isDefined)
    assertEquals("Unexpected",
      "HashMap((2,2) -> Set(2, 4), (4,1) -> Set(2, 4), (1,2) -> Set(2, 4), (1,4) -> Set(1, 2, 4), (1,1) -> Set(1), (2,4) -> Set(1, 2, 4), (3,2) -> Set(3), (3,1) -> Set(2, 4), (3,3) -> Set(1, 2, 4), (4,3) -> Set(2, 4), (2,3) -> Set(1, 2, 4), (1,3) -> Set(3), (3,4) -> Set(1, 2, 4), (2,1) -> Set(3), (4,2) -> Set(1), (4,4) -> Set(3))",
      newValuesMap.get.toString)
  }

  test("test for size=2 pos=(2, 1) assign valid 4") {
    val newValuesMap = assigner.assign((2, 1), 4, initialValues)

    assertTrue(newValuesMap.isDefined)
    assertEquals("Unexpected",
      "HashMap((2,2) -> Set(3), (4,1) -> Set(2), (1,2) -> Set(2), (1,4) -> Set(4), (1,1) -> Set(1), (2,4) -> Set(1, 2), (3,2) -> Set(1, 4), (3,1) -> Set(3), (3,3) -> Set(1, 2, 4), (4,3) -> Set(1, 4), (2,3) -> Set(1, 2), (1,3) -> Set(3), (3,4) -> Set(1, 2), (2,1) -> Set(4), (4,2) -> Set(1, 4), (4,4) -> Set(3))",
      newValuesMap.get.toString)
  }

  test("test for size=2 pos=(2, 1) assign a few valid entries") {
    var newValuesMap = assigner.assign((2, 1), 3, initialValues)
    newValuesMap = assigner.assign((2, 4), 2, newValuesMap.get)
    newValuesMap = assigner.assign((3, 2), 3, newValuesMap.get)
    newValuesMap = assigner.assign((3, 4), 1, newValuesMap.get)

    assertTrue(newValuesMap.isDefined)
    assertEquals("Unexpected",
      "HashMap((2,2) -> Set(4), (4,1) -> Set(2, 4), (1,2) -> Set(2), (1,4) -> Set(4), (1,1) -> Set(1), (2,4) -> Set(2), (3,2) -> Set(3), (3,1) -> Set(2, 4), (3,3) -> Set(2, 4), (4,3) -> Set(2, 4), (2,3) -> Set(1), (1,3) -> Set(3), (3,4) -> Set(1), (2,1) -> Set(3), (4,2) -> Set(1), (4,4) -> Set(3))",
      newValuesMap.get.toString)
  }

  test("test assign for size=2 pos=(1, 2) when there is a contradiction") {

    val newValuesMap = assigner.assign((1, 2), 1, initialValues)
    assertTrue(newValuesMap.isEmpty)
  }

}
