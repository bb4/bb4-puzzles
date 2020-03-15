// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.model

import com.barrybecker4.common.geometry.ByteLocation
import org.junit.Assert.assertEquals
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite

import scala.collection.immutable.HashSet
import scala.util.Random

/**
  * @author Barry Becker
  */
class SliderSuite extends AnyFunSuite with BeforeAndAfter {

  val RAND = new Random(1)

  test("Random") {
    assertEquals("unexpected first rnd ", 985, RAND.nextInt(1000))
    assertEquals("unexpected second rnd ", 588, RAND.nextInt(1000))
    val nums: List[Int] = RAND.shuffle(Range(0, 10).toList)
    val expList = List(5, 0, 4, 3, 9, 2, 8, 1, 6, 7)
    assertEquals("lists not equal", expList, nums)
  }

  test("BoardConstruction") {
    val board = new SliderBoard(3)
    assertResult(3) { board.size }
    assertResult(new ByteLocation(2, 2)) { board.getEmptyLocation }
    assertResult(0) { board.getHamming }
  }

  test("MediumBoardConstruction") {
    val board = new SliderBoard(4)
    assertResult(4) { board.size }
    assertResult(new ByteLocation(3, 3)) { board.getEmptyLocation }
  }

  test("LargeBoardConstruction") {
    val board = new SliderBoard(5)
    assertResult( 5) { board.size }
    assertResult(new ByteLocation(4, 4)) { board.getEmptyLocation }
  }

  test("BoardEquals") {
    val board1 = new SliderBoard(4)
    val board2 = SliderBoard(board1.tiles)
    val board3 = new SliderBoard(4).shuffle(new Random(1))
    assert(board1 == board2)
    assert(board2 == board1)
    assert(board1.hashCode == board2.hashCode)
    assert(board1 != board3)
    assert(board1.hashCode != board3.hashCode)
  }

  test("BoardHash") {
    var boards = HashSet[SliderBoard]()
    val board1 = new SliderBoard(4)
    val board2 = SliderBoard(board1.tiles)
    val board3 = new SliderBoard(4).shuffle()
    val board4 = new SliderBoard(4).shuffle()
    boards += board1
    boards += board2
    boards += board3

    assertResult( 2) { boards.size }
    assert(boards.contains(board1))
    assert(boards.contains(board2))
    assert(boards.contains(board3))
    assert(!boards.contains(board4))
  }

  test("hamming") {
    val board = new SliderBoard(4).shuffle(new Random(1))
    assertResult(14) { board.getHamming }
  }
}
