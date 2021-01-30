package com.barrybecker4.puzzle.sudoku

package object model {
  type Location = (Int, Int)
  type ValuesMap = Map[Location, Set[Int]]
  type CellMap = Map[Location, Cell]
}
