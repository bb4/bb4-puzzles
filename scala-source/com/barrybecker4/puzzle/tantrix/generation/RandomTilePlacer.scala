// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.generation

import com.barrybecker4.common.geometry.Location
import com.barrybecker4.puzzle.tantrix.model.PathColor
import com.barrybecker4.puzzle.tantrix.model._
import com.barrybecker4.puzzle.tantrix.model.analysis.fitting.PrimaryPathFitter

import scala.util.Random

/**
  * Generates random continuous primary color paths that do not necessarily match on secondary colors.
  * @author Barry Becker
  */
class RandomTilePlacer(var primaryColor: PathColor, rnd: Random = new Random()) {

  /** Considering each unplaced tile, find a single random placement given current configuration.
    * Valid placements must extend the primary path but not necessarily match secondary paths.
    *
    * @return a random tile placement for the current tantrix state and set of unplaced tiles.
    *         returns None if no placement is possible - such as when we have a loop, the end is blocked,
    *         or there are no more unplaced tiles.
    */
  private[generation] def generateRandomPlacement(board: TantrixBoard): Option[TilePlacement] =
    rnd.shuffle(board.unplacedTiles).iterator
      .flatMap(tile => findPrimaryPathPlacementForTile(board, tile))
      .nextOption()

  /** A random placement for the specified tile which matches the primary path,
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
    * [tileNum=4 colors: [B, Y, R, B, R, Y] at (row=21, column=22) ANGLE_300]
    * but all of them cause retouching to the main path.
    *
    * @return a valid primary path placement.
    */
  private def findPrimaryPathPlacementForTile(board: TantrixBoard, tile: HexTile): Option[TilePlacement] =
    findOpenPathEnd(board).flatMap { location =>
      val validFits = new PrimaryPathFitter(board.tantrix, board.primaryColor).getFittingPlacements(tile, location)
      Option.when(validFits.nonEmpty)(validFits(rnd.nextInt(validFits.size)))
    }

  /** @return where the primary path leaves the last placed tile and is still open (no tile there yet).
    *         There is no such place if there is a loop, or the open end of the primary path is blocked.
    */
  private def findOpenPathEnd(board: TantrixBoard): Option[Location] =
    board.getLastTile.getOutgoingPathLocations(primaryColor).valuesIterator
      .filter(location => board.isEmpty(location))
      .toList.lastOption
}
