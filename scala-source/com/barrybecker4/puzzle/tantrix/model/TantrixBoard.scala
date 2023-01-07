// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model

import com.barrybecker4.common.geometry.{Box, ByteLocation, Location}
import com.barrybecker4.puzzle.tantrix.model.HexTiles.TILES
import com.barrybecker4.puzzle.tantrix.model.PathColor
import com.barrybecker4.puzzle.tantrix.model.TantrixBoard.INITIAL_LOCATION
import com.barrybecker4.puzzle.tantrix.model.analysis.fitting.TantrixTileFitter
import com.barrybecker4.puzzle.tantrix.solver.verification.SolutionVerifier


object TantrixBoard {
  /** starting position. Must be odd. */
  val INITIAL_LOCATION: Location = ByteLocation(21, 21)
}

/**
  * Immutable representation of the current state of the tantrix puzzle on a hexagonal grid.
  * @param tantrix The 'tantrix'. Map of locations to currently placed tiles.
  * @param primaryColor color of the loop path that we are searching for
  * @param unplacedTiles tiles that have not yet been placed on the tantrix
  * @param numTiles number of tiles in the puzzle
  * @author Barry Becker
  */
case class TantrixBoard(tantrix: Tantrix, primaryColor: PathColor,
                        unplacedTiles: Seq[HexTile], numTiles: Int) {

  /** Constructor that creates a new tantrix instance when placing a move.
    * If the new tile to be placed is in the edge row of the grid, then we need to increase the size of the grid
    * by one in that direction and also only render the inside cells.
    *
    * @param board current tantrix state.
    * @param placement new piece to add to the tantrix and its positioning.
    */
  def this(board: TantrixBoard, placement: TilePlacement) = {
    this(board.tantrix.placeTile(placement),
      board.primaryColor, board.unplacedTiles.filter(_ != placement.tile), board.numTiles)
  }

  /** Create a board with the first tile in the given list placed at the initial location */
  def this(initialTiles: Seq[HexTile]) = {
    this(new Tantrix(Seq(TilePlacement(initialTiles.head, INITIAL_LOCATION, Rotation.ANGLE_0))),
         TILES.getTile(initialTiles.size).primaryColor, initialTiles.drop(1), initialTiles.size)
  }

  /** Create a board with the specified tile placements (nothing unplaced).
    * @param tiles specific placements to initialize the board with.
    */
  def this(tiles: Seq[TilePlacement], primaryColor: PathColor) = {
    this(new Tantrix(tiles), primaryColor, Seq(), tiles.size)
  }

  /** Take the specified tile and place it where indicated.
    * @param placement the placement containing the new tile to place.
    * @return the new immutable tantrix instance.
    */
  def placeTile(placement: TilePlacement) = new TantrixBoard(this, placement)

  /** @return true if the puzzle is solved. TODO: Maybe move this out because it adds dependency*/
  def isSolved: Boolean = new SolutionVerifier(this).isSolved

  /** @param currentPlacement where we are now
    * @param direction   side to navigate to to find the neighbor. 0 is to the right.
    * @return the indicated neighbor of the specified tile, if any.
    */
  def getNeighbor(currentPlacement: Option[TilePlacement], direction: Int): Option[TilePlacement] =
    tantrix.getNeighbor(currentPlacement, direction)

  /** The tile fits if the primary path and all the other paths match for edges that have neighbors.
    * @param placement the tile to check for a valid fit.
    * @return true of the tile fits
    */
  def fits(placement: TilePlacement): Boolean = {
    val fitter = new TantrixTileFitter(tantrix, primaryColor)
    fitter.isFit(placement)
  }

  def getLastTile: TilePlacement = tantrix.lastTile

  /** @return the edge of the smallest square that will hold the tantrix */
  def getEdgeLength: Int = tantrix.getEdgeLength

  /** @return the position of the top left bbox corner */
  def getBoundingBox: Box = tantrix.getBoundingBox

  def getTantrixLocations: Set[Location] = tantrix.tileMap.keySet

  /** @param location get the tile placement for this location.
    * @return null of there is no placement at that location.
    */
  def getTilePlacement(location: Location): Option[TilePlacement] = tantrix(location)

  def isEmpty(loc: Location): Boolean = getTilePlacement(loc).isEmpty

  override def toString: String = { "primaryColor = " + primaryColor + "\ntantrix = " + tantrix.toString +
    "\nunplaced tiles = " + unplacedTiles.mkString(", ") }
}
