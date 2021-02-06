package com.barrybecker4.puzzle.sudoku.generation

import com.barrybecker4.puzzle.sudoku.model.Board

import scala.util.Random

object SudokuGenerator {
  val RANDOM: Random = new Random()
}

trait SudokuGenerator {

  var delay: Int = 0

  def generatePuzzleBoard(size: Int): Board

}
