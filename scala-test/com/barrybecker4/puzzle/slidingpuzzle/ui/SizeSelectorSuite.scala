// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.ui

import com.barrybecker4.puzzle.slidingpuzzle.model.ShuffleStyle
import org.scalatest.funsuite.AnyFunSuite

class SizeSelectorSuite extends AnyFunSuite {

  test("every size has an easy and a hard choice") {
    assertResult(Seq("3 Tiles (easy)", "3 Tiles (hard)", "8 Tiles (easy)", "8 Tiles (hard)", "15 Tiles (easy)",
      "15 Tiles (hard)", "24 Tiles (easy)", "24 Tiles (hard)"))(SizeSelector.MENU_ITEMS.toSeq)
  }

  test("the edge length of each choice") {
    assertResult(Seq(2, 2, 3, 3, 4, 4, 5, 5))(SizeSelector.MENU_ITEMS.indices.map(SizeSelector.edgeLengthAt))
  }

  test("the easy choice of each size is the random walk and the hard one is uniformly random") {
    assertResult(ShuffleStyle.RANDOM_WALK)(SizeSelector.styleAt(4))
    assertResult(ShuffleStyle.UNIFORM_RANDOM)(SizeSelector.styleAt(5))
  }

  test("the puzzle starts as the easy 8 puzzle") {
    assertResult("8 Tiles (easy)")(SizeSelector.MENU_ITEMS(SizeSelector.DEFAULT_INDEX))
  }
}
