package com.barrybecker4.puzzle.sudoku.model

import org.junit.Assert.{assertEquals, assertTrue}
import org.scalatest.funsuite.AnyFunSuite


class ValueAssignerSuite extends AnyFunSuite {

  private val assigner2 = ValueAssigner(BoardComponents.COMPONENTS(2))
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
    val newValuesMap = assigner2.assign((1, 2), 2, initialValues)

    assertTrue(newValuesMap.isDefined)
    assertEquals("Unexpected",
      "HashMap((2,2) -> Set(3, 4), (4,1) -> Set(1, 2, 3, 4), (1,2) -> Set(2), (1,4) -> Set(4), (1,1) -> Set(1), (2,4) -> Set(1, 2), (3,2) -> Set(1, 3, 4), (3,1) -> Set(1, 2, 3, 4), (3,3) -> Set(1, 2, 4), (4,3) -> Set(1, 2, 4), (2,3) -> Set(1, 2), (1,3) -> Set(3), (3,4) -> Set(1, 2), (2,1) -> Set(3, 4), (4,2) -> Set(1, 3, 4), (4,4) -> Set(3))",
      newValuesMap.get.toString)
  }

  test("test for size=2 pos=(2, 1) assign valid 3") {
    val newValuesMap = assigner2.assign((2, 1), 3, initialValues)

    assertTrue(newValuesMap.isDefined)
    assertEquals("Unexpected",
      "HashMap((2,2) -> Set(2, 4), (4,1) -> Set(2, 4), (1,2) -> Set(2, 4), (1,4) -> Set(1, 2, 4), (1,1) -> Set(1), (2,4) -> Set(1, 2, 4), (3,2) -> Set(3), (3,1) -> Set(2, 4), (3,3) -> Set(1, 2, 4), (4,3) -> Set(2, 4), (2,3) -> Set(1, 2, 4), (1,3) -> Set(3), (3,4) -> Set(1, 2, 4), (2,1) -> Set(3), (4,2) -> Set(1), (4,4) -> Set(3))",
      newValuesMap.get.toString)
  }

  test("test for size=2 pos=(2, 1) assign valid 4") {
    val newValuesMap = assigner2.assign((2, 1), 4, initialValues)

    assertTrue(newValuesMap.isDefined)
    assertEquals("Unexpected",
      "HashMap((2,2) -> Set(3), (4,1) -> Set(2), (1,2) -> Set(2), (1,4) -> Set(4), (1,1) -> Set(1), (2,4) -> Set(1, 2), (3,2) -> Set(1, 4), (3,1) -> Set(3), (3,3) -> Set(1, 2, 4), (4,3) -> Set(1, 4), (2,3) -> Set(1, 2), (1,3) -> Set(3), (3,4) -> Set(1, 2), (2,1) -> Set(4), (4,2) -> Set(1, 4), (4,4) -> Set(3))",
      newValuesMap.get.toString)
  }

  test("test for size=2 pos=(2, 1) assign a few valid entries") {
    var newValuesMap = assigner2.assign((2, 1), 3, initialValues)
    newValuesMap = assigner2.assign((2, 4), 2, newValuesMap.get)
    newValuesMap = assigner2.assign((3, 2), 3, newValuesMap.get)
    newValuesMap = assigner2.assign((3, 4), 1, newValuesMap.get)

    assertTrue(newValuesMap.isDefined)
    assertEquals("Unexpected",
      "HashMap((2,2) -> Set(4), (4,1) -> Set(2, 4), (1,2) -> Set(2), (1,4) -> Set(4), (1,1) -> Set(1), (2,4) -> Set(2), (3,2) -> Set(3), (3,1) -> Set(2, 4), (3,3) -> Set(2, 4), (4,3) -> Set(2, 4), (2,3) -> Set(1), (1,3) -> Set(3), (3,4) -> Set(1), (2,1) -> Set(3), (4,2) -> Set(1), (4,4) -> Set(3))",
      newValuesMap.get.toString)
  }
  
  test("test for size=3 difficult") {
    var valuesMap = Map(
      (4,1) -> Set(4), 
      (5,2) -> Set(1), 
      (1,1) -> Set(1, 6, 9, 2),
      (5,9) -> Set(6),
      (2,9) -> Set(1, 7, 3, 4),
      (5,6) -> Set(3),
      (2,2) -> Set(9, 3, 4),
      (5,4) -> Set(9, 8, 4),
      (4,8) -> Set(1, 9, 7),
      (8,5) -> Set(6, 2, 8),
      (3,4) -> Set(6, 9, 8, 4),
      (2,1) -> Set(8),
      (9,9) -> Set(5, 1, 2, 8, 4),
      (9,3) -> Set(1, 2, 8),
      (7,8) -> Set(1, 4),
      (7,5) -> Set(2, 3, 8, 4), (4,7) -> Set(3), (7,9) -> Set(9), (6,1) -> Set(5, 6, 9, 7),
      (2,5) -> Set(5),
      (4,2) -> Set(9, 2, 8), (4,4) -> Set(1, 6, 9, 8), (2,6) -> Set(6, 7, 4),
      (8,4) -> Set(1, 6, 7, 8), (9,7) -> Set(7), (1,8) -> Set(1, 6, 9, 7, 4), (9,1) -> Set(5, 1, 2, 3),
      (1,2) -> Set(9, 2, 4), (6,5) -> Set(6, 9, 4), (6,2) -> Set(5, 9), (8,2) -> Set(5, 9, 2, 8),
      (1,4) -> Set(3), (4,5) -> Set(6, 9, 8), (5,8) -> Set(5, 9, 4), (4,6) -> Set(5), (6,7) -> Set(1, 9, 4),
      (4,9) -> Set(1, 2, 7), (2,7) -> Set(1, 6, 9, 4), (9,4) -> Set(1, 6, 8, 4), (1,9) -> Set(1, 7, 8, 4),
      (8,1) -> Set(5, 1, 9, 2, 7), (1,6) -> Set(6, 2, 7, 8, 4), (7,7) -> Set(1, 2, 8, 4), (3,8) -> Set(6, 9, 4),
      (8,3) -> Set(4),
      (2,4) -> Set(6, 9, 7, 4),  // should have 5!
      (3,7) -> Set(5), (9,2) -> Set(5, 2, 3, 8), (6,3) -> Set(3),
      (8,8) -> Set(3), (8,9) -> Set(5, 1, 2, 8), (9,8) -> Set(5, 1, 6, 4), (6,4) -> Set(2), (2,8) -> Set(2),
      (3,2) -> Set(7), (9,6) -> Set(9),
      (1,5) -> Set(6, 9, 2, 8, 4),
      (8,6) -> Set(1, 6, 2, 7, 8),
      (3,1) -> Set(6, 9, 2, 3), (7,1) -> Set(1, 2, 7, 3), (7,2) -> Set(6), (3,9) -> Set(3, 8, 4),
      (3,3) -> Set(6, 9, 2), (6,8) -> Set(8), (8,7) -> Set(1, 6, 2, 8), (6,9) -> Set(5, 1, 7, 4),
      (4,3) -> Set(6, 9, 2, 7, 8), (1,7) -> Set(1, 6, 9, 8, 4), (9,5) -> Set(6, 2, 3, 8, 4),
      (5,1) -> Set(5, 9, 2), (3,6) -> Set(6, 2, 8, 4), (2,3) -> Set(1, 6, 9), (3,5) -> Set(1),
      (7,3) -> Set(1, 2, 7, 8), (1,3) -> Set(5), (5,5) -> Set(7), (5,7) -> Set(9, 2, 4),
      (5,3) -> Set(9, 2, 8), (7,4) -> Set(5), (6,6) -> Set(1, 6, 4), (7,6) -> Set(1, 2, 7, 8, 4)
    )

    val assigner3 = ValueAssigner(BoardComponents.COMPONENTS(3))
    var newValuesMap = assigner3.assign((2, 5), 5, valuesMap)
    newValuesMap = assigner3.assign((5, 6), 3, newValuesMap.get)
    newValuesMap = assigner3.assign((4, 3), 6, newValuesMap.get)
    newValuesMap = assigner3.assign((6, 1), 7, newValuesMap.get)

    assertEquals("Unexpected",
      "HashMap((4,1) -> Set(4), (5,2) -> Set(1), (1,1) -> Set(1, 6, 9, 2), (5,9) -> Set(6), (2,9) -> Set(1, 7, 3, 4), (5,6) -> Set(3), (2,2) -> Set(9, 3, 4), (5,4) -> Set(9, 8, 4), (4,8) -> Set(1, 9, 7), (8,5) -> Set(6, 2, 8), (3,4) -> Set(6, 9, 8, 4), (2,1) -> Set(8), (9,9) -> HashSet(5, 1, 2, 8, 4), (9,3) -> Set(1, 2, 8), (7,8) -> Set(1, 4), (7,5) -> Set(2, 3, 8, 4), (4,7) -> Set(3), (7,9) -> Set(9), (6,1) -> Set(7), (2,5) -> Set(5), (4,2) -> Set(9, 2, 8), (4,4) -> Set(1, 9, 8), (2,6) -> Set(6, 7, 4), (8,4) -> Set(1, 6, 7, 8), (9,7) -> Set(7), (1,8) -> HashSet(1, 6, 9, 7, 4), (9,1) -> Set(5, 1, 2, 3), (1,2) -> Set(9, 2, 4), (6,5) -> Set(6, 9, 4), (6,2) -> Set(5, 9), (8,2) -> Set(5, 9, 2, 8), (1,4) -> Set(3), (4,5) -> Set(9, 8), (5,8) -> Set(5, 9, 4), (4,6) -> Set(5), (6,7) -> Set(1, 9, 4), (4,9) -> Set(1, 2, 7), (2,7) -> Set(1, 6, 9, 4), (9,4) -> Set(1, 6, 8, 4), (1,9) -> Set(1, 7, 8, 4), (8,1) -> HashSet(5, 1, 9, 2), (1,6) -> HashSet(6, 2, 7, 8, 4), (7,7) -> Set(1, 2, 8, 4), (3,8) -> Set(6, 9, 4), (8,3) -> Set(4), (2,4) -> Set(6, 9, 7, 4), (3,7) -> Set(5), (9,2) -> Set(5, 2, 3, 8), (6,3) -> Set(3), (8,8) -> Set(3), (8,9) -> Set(5, 1, 2, 8), (9,8) -> Set(5, 1, 6, 4), (6,4) -> Set(2), (2,8) -> Set(2), (3,2) -> Set(7), (9,6) -> Set(9), (1,5) -> HashSet(6, 9, 2, 8, 4), (8,6) -> HashSet(1, 6, 2, 7, 8), (3,1) -> Set(6, 9, 2, 3), (7,1) -> Set(1, 2, 3), (7,2) -> Set(6), (3,9) -> Set(3, 8, 4), (3,3) -> Set(9, 2), (6,8) -> Set(8), (8,7) -> Set(1, 6, 2, 8), (6,9) -> Set(5, 1, 4), (4,3) -> HashSet(6), (1,7) -> HashSet(1, 6, 9, 8, 4), (9,5) -> HashSet(6, 2, 3, 8, 4), (5,1) -> Set(5, 9, 2), (3,6) -> Set(6, 2, 8, 4), (2,3) -> Set(1, 9), (3,5) -> Set(1), (7,3) -> Set(7), (1,3) -> Set(5), (5,5) -> Set(7), (5,7) -> Set(9, 2, 4), (5,3) -> Set(9, 2, 8), (7,4) -> Set(5), (6,6) -> Set(1, 6, 4), (7,6) -> HashSet(1, 2, 8, 4))",
      newValuesMap.get.toString)

    newValuesMap = assigner3.assign((1, 1), 1, newValuesMap.get)
    newValuesMap = assigner3.assign((1, 6), 7, newValuesMap.get)

    println(newValuesMap.get.filter({case (k, v) => v.size > 1}).toString)
  }

  test("test assign for size=2 pos=(1, 2) when there is a contradiction") {

    val newValuesMap = assigner2.assign((1, 2), 1, initialValues)
    assertTrue(newValuesMap.isEmpty)
  }

}
