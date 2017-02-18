/*
 * // Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
 */

package com.barrybecker4.puzzle.sudoku.model.board

import org.scalatest.{BeforeAndAfter, FunSuite}

/**
  * Created by barry on 2/18/2017.
  */
class CandidatesSuite extends FunSuite with BeforeAndAfter{


  /** instance under test */
  private var cell: Cell = _
  private var board: Board = _

  before {
    //MathUtil.RANDOM.setSeed(1)
  }

  test("candidate constructor with 3 values") {
    val cand = new Candidates(1, 2, 3)
    assertResult("1,2,3") { cand.toString }
  }

  test("candidate constructor with >10 values") {
    val cand = new Candidates(1, 4, 7, 9, 10, 11, 12, 14, 2, 3)
    assertResult("X,4,9,A,1,B,2,7,3,D") { cand.toString }
  }

  test("candidate intersection") {
    val cand1 = new Candidates(3, 4, 5, 9, 1)
    val cand2 = new Candidates(4, 11, 3, 7)
    assertResult("3,4") { cand1.intersect(cand2).toString }
  }

  test("candidate removal") {
    val cand1 = new Candidates(3, 4, 5, 9, 1)
    cand1.remove(5)
    assertResult("4,9,1,3") { cand1.toString }
  }

  test("candidate retainAll") {
    val cand1 = new Candidates(3, 4, 5, 9, 1)
    cand1.retainAll(new Candidates(4, 9))
    assertResult("4,9") { cand1.toString }
  }

  test("candidate containsAll true") {
    val cand1 = new Candidates(3, 4, 5, 9, 1)
    assertResult(true) { cand1.containsAll(new Candidates(1, 3, 4, 5, 9)) }
  }

  test("candidate containsAll false") {
    val cand1 = new Candidates(3, 4, 5, 9, 1)
    assertResult(false) { cand1.containsAll(new Candidates(1, 4, 5, 9)) }
  }
}

