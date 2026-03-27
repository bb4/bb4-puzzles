// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.common.model

import org.scalatest.funsuite.AnyFunSuite

class PuzzleNodeSuite extends AnyFunSuite {

  test("asMoveList is empty for root node") {
    val root = PuzzleNode[Int, String](0)
    assertResult(Seq.empty)(root.asMoveList)
  }

  test("asMoveList collects moves from start to leaf in order") {
    val root = PuzzleNode[Int, String](0)
    val n1 = PuzzleNode(1, Some("a"), Some(root))
    val n2 = PuzzleNode(2, Some("b"), Some(n1))
    assertResult(Seq("a", "b"))(n2.asMoveList)
  }
}
