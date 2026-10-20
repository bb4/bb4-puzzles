// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.generation

import com.barrybecker4.puzzle.tantrix.model.TantrixBoard
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath

import scala.annotation.tailrec
import scala.util.Random

/**
  * Generates random continuous primary color paths that do not necessarily match on secondary colors.
  * @author Barry Becker
  */
class RandomPathGenerator(val initialBoard: TantrixBoard, rnd: Random = new Random()) {

  private val tilePlacer = new RandomTilePlacer(initialBoard.primaryColor, rnd)

  /** @return a random path.*/
  def generateRandomPath: TantrixPath = {
    // random placement can paint itself into a corner, in which case we start over.
    val board = Iterator.continually(placeRemainingTiles(initialBoard)).flatten.next()
    new TantrixPath(board.tantrix, initialBoard.primaryColor, board.numTiles, rnd)
  }

  /** Randomly place the unplaced tiles, one after the other, until there are none left.
    * @return the resulting board, or None if we got stuck before all the tiles were placed.
    */
  @tailrec
  private def placeRemainingTiles(board: TantrixBoard): Option[TantrixBoard] =
    if (board.unplacedTiles.isEmpty) Some(board)
    else tilePlacer.generateRandomPlacement(board) match
      case Some(placement) => placeRemainingTiles(board.placeTile(placement))
      case None => None
}
