package com.barrybecker4.puzzle.sudoku.model

case class BoardSerializer(board: Board) {

  def serialize: String = {
    val b = for (r <- board.comps.digits) yield
      for (c <- board.comps.digits; v = board.getCell((r, c))) yield v.proposedValue
    "\n" + b.map(_.map(ValueConverter.getSymbol).mkString("Array(", ", ", "),")).mkString("\n")
  }

  def debugSerialize: String = {
    val b = for (r <- board.comps.digits) yield
      for (c <- board.comps.digits; v = board.getCell((r, c)).candidateValues) yield v
    b.map(_.map(_.map(ValueConverter.getSymbol)).mkString("[", ",", "]")).mkString("\n")
  }
}
