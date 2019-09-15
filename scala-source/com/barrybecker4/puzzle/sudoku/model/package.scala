package com.barrybecker4.puzzle.sudoku

package object model {
  type ValueMap = Map[(Int, Int), Set[Int]]

  class Cell(var originalValue: Int, var proposedValue: Int)

  /** static because they are the same for every board. */
  val COMPONENTS: Array[BoardComponents] = (0 to 5).map(i => BoardComponents(i)).toArray
}
