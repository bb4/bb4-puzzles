// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.generation

import com.barrybecker4.puzzle.tantrix.model.TantrixBoard
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath

import scala.util.Random

/**
  * Generates random continuous primary color paths that do not necessarily match on secondary colors.
  *
  * @author Barry Becker
  */
class RandomPathGenerator(var initialBoard: TantrixBoard, rnd: Random = new Random()) {

  private val tilePlacer = new RandomTilePlacer(initialBoard.primaryColor, rnd)

  /** @return a random path.*/
  def generateRandomPath: TantrixPath = {
    var currentBoard: TantrixBoard = initialBoard
    var foundPath = false
    do {
      currentBoard = initialBoard
      var hasPlacement = true
      while (currentBoard.unplacedTiles.nonEmpty && hasPlacement) {
        val placement = tilePlacer.generateRandomPlacement(currentBoard)
        if (placement.isEmpty)
          hasPlacement = false
        else
          currentBoard = currentBoard.placeTile(placement.get)
      }
      foundPath = currentBoard.unplacedTiles.isEmpty
    } while (!foundPath)

    new TantrixPath(currentBoard.tantrix, initialBoard.primaryColor, rnd)
  }
}
