// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.verification

import com.barrybecker4.puzzle.tantrix.model.{HexTile, TantrixBoard, TilePlacement}

/**
  * Used to determine whether or not a given tantrix is a loop.
  *
  * @param board the tantrix state to test for solution.
  * @author Barry Becker
  */
case class LoopDetector(var board: TantrixBoard) {

  /**
    * True if loop of the primary color.
    * Does not check the consistency of secondary paths.
    *
    * @return true if solved.
    */
  def hasLoop: Boolean = {
    if (board.unplacedTiles.nonEmpty) false
    else {
      var numVisited = 0
      val lastTilePlaced = board.getLastTile
      var currentTile: Option[TilePlacement] = Some(lastTilePlaced)
      var previousTile: Option[TilePlacement] = None
      var nextTile: Option[TilePlacement] = None
      var done = false
      while (!done) {
        nextTile = findNeighborTile(currentTile, previousTile)
        previousTile = currentTile
        currentTile = nextTile
        numVisited += 1
        done = currentTile.isEmpty || currentTile.contains(lastTilePlaced)
      }
      numVisited == board.numTiles && currentTile.contains(lastTilePlaced)
    }
  }

  /**
    * Loop through the edges until we find the primary color.
    * If it does not direct us back to where we came from then go that way.
    *
    * @param currentPlacement where we are now, if any
    * @param previousTile     where we were, if any
    * @return the next tile in the path if there is one, else `None`
    */
  private def findNeighborTile(currentPlacement: Option[TilePlacement],
                               previousTile: Option[TilePlacement]): Option[TilePlacement] =
    currentPlacement match
      case None => None
      case Some(cur) =>
        var result: Option[TilePlacement] = None
        var i = 0
        while (i < HexTile.NUM_SIDES && result.isEmpty) {
          val color = cur.getPathColor(i)
          if (color == board.primaryColor) {
            result = board.getNeighbor(Some(cur), i).flatMap { nbrTile =>
              Option.when(!previousTile.contains(nbrTile) && nbrTile.getPathColor(i + 3) == color)(nbrTile)
            }
          }
          i += 1
        }
        result
}
