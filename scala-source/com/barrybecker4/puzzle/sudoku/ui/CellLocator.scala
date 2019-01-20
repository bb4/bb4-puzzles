// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.ui

import java.awt.Point

import com.barrybecker4.common.geometry.Location
import com.barrybecker4.puzzle.sudoku.model.Board

/**
  * Locates cell coordinates given a point location on the screen.
  * @author Barry Becker
  */
trait CellLocator {

  def board: Board
  def getCellCoordinates(point: Point): Location
}
