package com.barrybecker4.puzzle.sudoku

package object model {
  type Location = (Int, Int)
  type ValueMap = Map[Location, Set[Int]]
}
