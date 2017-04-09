// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.generation

import com.barrybecker4.common.geometry.Location
import com.barrybecker4.common.math.MathUtil
import com.barrybecker4.puzzle.tantrix.model.PathColor.PathColor
import com.barrybecker4.puzzle.tantrix.model._
import com.barrybecker4.puzzle.tantrix.model.analysis.fitting.PrimaryPathFitter

import scala.util.Random

/**
  * Generates random continuous primary color paths that do not necessarily match on secondary colors.
  *
  * @author Barry Becker
  */
class RandomTilePlacer private[generation](var primaryColor: PathColor) {

  /**
    * Considering each unplaced tile, find a single random placement given current configuration.
    * Valid placements must extend the primary path but not necessarily match secondary paths.
    *
    * @return a random tile placement for the current tantrix state and set of unplaced tiles.
    *         returns null if no placement is possible - such as when we have a loop, the end is blocked,
    *         or there are no more unplaced tiles.
    */
  private[generation] def generateRandomPlacement(board: TantrixBoard,
                                                  rnd: Random = new Random()): Option[TilePlacement] = {
    val unplacedTiles = rnd.shuffle(board.unplacedTiles)
    var nextMove: Option[TilePlacement] = None
    var i = 0
    while (nextMove.isEmpty && i < unplacedTiles.size) {
      val tile = unplacedTiles(i)
      i += 1
      nextMove = findPrimaryPathPlacementForTile(board, tile)
    }
    if (nextMove.isEmpty) System.out.println("no valid placements found among " +
      unplacedTiles + " to match existing tantrix of " + board.tantrix)
    nextMove
  }

  /**
    * A random placement for the specified tile which matches the primary path,
    * but not necessarily the secondary paths. The opposite end of the primary path can only
    * retouch the tantrix if it is the last tile to be placed in the random path.
    *
    * There are usually two ways to place a tile, but there are some rare cases where there can be four.
    * For example, given:
    * [tileNum=6 colors: [Y, R, B, Y, B, R] at (row=20, column=21) ANGLE_240]
    * [tileNum=2 colors: [B, Y, Y, B, R, R] at (row=20, column=20) ANGLE_60]
    * [tileNum=7 colors: [R, Y, R, Y, B, B] at (row=21, column=21) ANGLE_0]
    *
    * There are these 4 valid placements:
    * [tileNum=4 colors: [B, Y, R, B, R, Y] at (row=21, column=22) ANGLE_0],
    * [tileNum=4 colors: [B, Y, R, B, R, Y] at (row=21, column=22) ANGLE_60],
    * [tileNum=4 colors: [B, Y, R, B, R, Y] at (row=21, column=22) ANGLE_240],
    * [tileNum=4 colors: [B, Y, R, B, R, Y] at (row=21, column=22) ANGLE_300]]
    * but all of them cause retouching to the main path.
    *
    * @return a valid primary path placement.
    */
  private def findPrimaryPathPlacementForTile(board: TantrixBoard, tile: HexTile): Option[TilePlacement] = {
    val lastPlaced = board.getLastTile
    val fitter = new PrimaryPathFitter(board.tantrix, board.primaryColor)
    val outgoing: Map[Int, Location] = lastPlaced.getOutgoingPathLocations(primaryColor)
    var nextLocation: Location = null
    for (i <- outgoing.keySet) {
      if (board.getTilePlacement(outgoing(i)).isEmpty) nextLocation = outgoing(i)
    }
    // this could happen if there is a loop, or the openQueue end of the primary path is blocked.
    if (nextLocation == null) return None
    val validFits = fitter.getFittingPlacements(tile, nextLocation)
    if (validFits.isEmpty) return None
    Some(validFits(MathUtil.RANDOM.nextInt(validFits.size)))
  }
}
