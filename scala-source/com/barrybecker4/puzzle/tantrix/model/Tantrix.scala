// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model

import com.barrybecker4.common.geometry.{Box, ByteLocation, Location}
import com.barrybecker4.puzzle.tantrix.model.Tantrix.createTileMap

import scala.collection.immutable

object Tantrix {
  def createTileMap(tiles: Seq[TilePlacement]): immutable.Map[Location, TilePlacement] = {
    tiles.map(tilePlacement => tilePlacement.location -> tilePlacement).toMap
  }
}

/**
  * Represents "The Tantrix". In other words the set of currently placed tiles.
  * Immutable.
  * @param tileMap current tantrix.
  * @param lastTile the last tile placed.
  * @author Barry Becker
  */
case class Tantrix(tileMap: immutable.Map[Location, TilePlacement], lastTile: TilePlacement) {

  /** @param tiles tiles in the tantrix */
  def this(tiles: Seq[TilePlacement]) = { this(createTileMap(tiles), tiles.last) }
  def this(tantrix: Tantrix, placement: TilePlacement) = { this(tantrix.tileMap, placement) }

  /**
    * Take the specified tile and place it where indicated.
    * @param placement the placement containing the new tile to place.
    * @return the new immutable tantrix instance.
    */
  def placeTile(placement: TilePlacement): Tantrix =
    new Tantrix(tileMap + (placement.location -> placement), placement)

  /** @return the placement at the specified location, if there is one, else None. */
  def apply(row: Int, col: Int): Option[TilePlacement] = tileMap.get(new ByteLocation(row, col))
  def apply(loc: Location): Option[TilePlacement] = tileMap.get(loc)

  /**
    * @param currentPlacement where we are now
    * @param direction  side to navigate to to find the neighbor. 0 is to the right.
    * @return the indicated neighbor of the specified tile (if it exists), else none.
    */
  def getNeighbor(currentPlacement: Option[TilePlacement], direction: Int): Option[TilePlacement] = {
    if (currentPlacement.isEmpty) return None
    val loc = HexUtil.getNeighborLocation(currentPlacement.get.location, direction)
    tileMap.get(loc)
  }

  def getEdgeLength: Int = getBoundingBox.getMaxDimension + 1
  def size: Int = tileMap.size
  def tiles: Iterable[TilePlacement] = tileMap.values
  def getTiles: Seq[HexTile] = tileMap.values.map(_.tile).toSeq

  /** @return the bounds of the current tantrix tiles. */
  def getBoundingBox: Box = {
    var bbox = new Box(lastTile.location)
    tileMap.keySet.foreach(pt => {
      bbox = bbox.expandBy(pt)
    })
    bbox
  }

  override def toString: String = tileMap.values.mkString(" ")
}
