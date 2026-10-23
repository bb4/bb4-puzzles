// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix

import com.barrybecker4.puzzle.tantrix.model.{TantrixBoard, TilePlacement}

import scala.collection.mutable

/**
  * Finds every solution below a starting state by exhaustive depth first search, using the provided move generation.
  * It is not meant for large puzzles, but it makes it possible to compare how different ways of generating moves
  * affect the solutions that can be found.
  *
  * @param generateMoves finds the moves that can be made from a state.
  */
class SolutionEnumerator(generateMoves: TantrixBoard => Seq[TilePlacement]) {

  /** The number of different states that were looked at by the last call to [[solutions]]. */
  var numStatesSearched: Int = 0

  /** @return the final boards of all solutions, without duplicates. */
  def solutions(start: TantrixBoard): Set[TantrixBoard] = {
    val seen = mutable.HashSet.empty[TantrixBoard]
    val found = mutable.HashSet.empty[TantrixBoard]
    def search(board: TantrixBoard): Unit =
      if (seen.add(board)) {
        if (board.isSolved) found += board
        else generateMoves(board).foreach(move => search(board.placeTile(move)))
      }
    search(start)
    numStatesSearched = seen.size
    found.toSet
  }
}
