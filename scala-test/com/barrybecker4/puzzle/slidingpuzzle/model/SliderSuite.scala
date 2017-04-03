// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.model


import com.barrybecker4.common.geometry.ByteLocation
import org.junit.Assert.{assertEquals, assertFalse, assertTrue}
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.collection.immutable.HashSet
import scala.util.Random

/**
  * @author Barry Becker
  */
class SliderSuite extends FunSuite with BeforeAndAfter {

  val RAND = new Random(1)

  test("Random") {
    assertEquals("unexpected first rnd ", 985, RAND.nextInt(1000))
    assertEquals("unexpected second rnd ", 588, RAND.nextInt(1000))
    val nums: List[Int] = RAND.shuffle(Range(0, 10).toList)
    val expList = List(5, 0, 4, 3, 9, 2, 8, 1, 6, 7)
    assertEquals("lists not equal", expList, nums)
  }

  test("BoardConstruction") {
    val board = new SliderBoard(3, false)
    assertEquals("Unexpected board size", 3, board.size)
    assertEquals("Unexpected empty location", new ByteLocation(2, 2), board.getEmptyLocation)
  }

  test("MediumBoardConstruction") {
    val board = new SliderBoard(4, false)
    assertEquals("Unexpected board size", 4, board.size)
    assertEquals("Unexpected empty location", new ByteLocation(3, 3), board.getEmptyLocation)
  }

  test("LargeBoardConstruction") {
    val board = new SliderBoard(5, false)
    assertEquals("Unexpected board size", 5, board.size)
    assertEquals("Unexpected empty location", new ByteLocation(4, 4), board.getEmptyLocation)
  }

  test("BoardEquals") {
    val board1 = new SliderBoard(4, false)
    val board2 = new SliderBoard(board1)
    val board3 = new SliderBoard(4, true)
    assertTrue(board1 == board2)
    assertTrue(board2 == board1)
    assertTrue(board1.hashCode == board2.hashCode)
    assertFalse(board1 == board3)
    assertFalse(board1.hashCode == board3.hashCode)
  }

  test("BoardHash") {
    var boards = HashSet[SliderBoard]()
    val board1 = new SliderBoard(4, false)
    val board2 = new SliderBoard(board1)
    val board3 = new SliderBoard(4, true)
    val board4 = new SliderBoard(4, true)
    boards += board1
    boards += board2
    boards += board3

    assertEquals("Unexpected number boards in set", 2, boards.size)
    assertTrue(boards.contains(board1))
    assertTrue(boards.contains(board2))
    assertTrue(boards.contains(board3))
    assertFalse(boards.contains(board4))
  }
}