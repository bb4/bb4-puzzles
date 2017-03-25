// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model

import java.util.NoSuchElementException

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
case class Tantrix(tileMap: Map[Location, TilePlacement], lastTile: TilePlacement) {

  /**
    * Take the specified tile and place it where indicated.
    * @param placement the placement containing the new tile to place.
    * @return the new immutable tantrix instance.
    */
  def placeTile(placement: TilePlacement): Tantrix =
    new Tantrix(tileMap + (placement.location -> placement), placement)

  /** @param tiles tiles in the tantrix */
  def this(tiles: Seq[TilePlacement]) { this(createTileMap(tiles), tiles.last) }

  def this(tantrix: Tantrix, placement: TilePlacement) = { this(tantrix.tileMap, placement) }

  /** @return the placement at the specified location.*/
  def apply(row: Int, col: Int): TilePlacement = {
    try{tileMap(new ByteLocation(row, col))}
    catch {
      case e: NoSuchElementException =>
        throw new IllegalStateException("could not find " + row +"," + col + " among " + tileMap.keys.mkString(", "), e)
    }
  }
  def apply(loc: Location): TilePlacement = {
    try{tileMap(loc)}
    catch {
      case e: NoSuchElementException =>
        throw new IllegalStateException("could not find " + loc + " among " + tileMap.keys.mkString(", "), e)
    }
  }

  /**
    * @param currentPlacement where we are now  (Option?)
    * @param direction        side to navigate to to find the neighbor. 0 is to the right.
    * @return the indicated neighbor of the specified tile.
    */
  def getNeighbor(currentPlacement: TilePlacement, direction: Int): Option[TilePlacement] = {
    if (currentPlacement == null) return None
    val loc = HexUtil.getNeighborLocation(currentPlacement.location, direction)
    Some(tileMap(loc))
  }

  def getEdgeLength: Int = getBoundingBox.getMaxDimension + 1
  def size = tileMap.size
  def tiles: Iterable[TilePlacement] = tileMap.values
  def getTiles: Seq[HexTile] = tileMap.values.map(_.tile).toSeq

  /** @return the bounds of the current tantrix tiles. */
  def getBoundingBox: Box = {
    val bbox = new Box(lastTile.location)
    tileMap.keySet.foreach(bbox.expandBy)
    bbox
  }

  override def toString: String = tileMap.values.mkString(" ")
}
