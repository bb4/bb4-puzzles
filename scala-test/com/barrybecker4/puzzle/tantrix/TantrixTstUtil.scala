// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix

import com.barrybecker4.common.geometry.{ByteLocation, Location}
import com.barrybecker4.puzzle.tantrix.model.HexTiles.TILES
import com.barrybecker4.puzzle.tantrix.model.RotationEnum._
import com.barrybecker4.puzzle.tantrix.model.TantrixBoard.INITIAL_LOCATION
import com.barrybecker4.puzzle.tantrix.model.{HexTile, TantrixBoard, TilePlacement}

/**
  * @author Barry Becker
  */
object TantrixTstUtil {
  
  val THREE_TILES = TILES.createOrderedList(3)
  val FOUR_TILES = TILES.createOrderedList(4)
  val FIVE_TILES = TILES.createOrderedList(5)
  val SIX_TILES = TILES.createOrderedList(6)
  val SEVEN_TILES = TILES.createOrderedList(7)
  val TEN_TILES = TILES.createOrderedList(10)
  val FOURTEEN_TILES = TILES.createOrderedList(14)

  /** Places first tile in the middle and one of two remaining placed */
  def place2of3Tiles_OneThenTwo: TantrixBoard = {
    var board = new TantrixBoard(THREE_TILES)
    val tile2 = TilePlacement(TILES.getTile(2), loc(2, 1), ANGLE_60)
    board = new TantrixBoard(board, tile2)
    board
  }

  /** Places first tile in the middle and one of two remaining placed */
  def place2of3Tiles_OneThenThree: TantrixBoard = {
    var board = new TantrixBoard(THREE_TILES)
    val tile2 = TilePlacement(TILES.getTile(3), loc(2, 1), ANGLE_240)
    board = new TantrixBoard(board, tile2)
    board
  }

  /** Places first tile in the middle */
  def place3UnsolvedTiles: TantrixBoard = place3Unsolved(THREE_TILES)

  /** Places first tile in the middle */
  def place6UnsolvedTiles: TantrixBoard = place6Unsolved(SIX_TILES)

  def sevenTilesInAWrongRedLoop = Seq(
    TilePlacement(TILES.getTile(3), loc(3, 1), ANGLE_300),
    TilePlacement(TILES.getTile(5), loc(2, 0), ANGLE_120),
    TilePlacement(TILES.getTile(6), loc(1, 0), ANGLE_0),
    TilePlacement(TILES.getTile(4), loc(0, 0), ANGLE_120),
    TilePlacement(TILES.getTile(2), loc(0, 1), ANGLE_300),
    TilePlacement(TILES.getTile(1), loc(1, 1), ANGLE_300),
    TilePlacement(TILES.getTile(7), loc(2, 1), ANGLE_120))

  def sevenTilesInABlueLoop = Seq(
    TilePlacement(TILES.getTile(7), loc(3, 1), ANGLE_180),
    TilePlacement(TILES.getTile(2), loc(2, 0), ANGLE_120),
    TilePlacement(TILES.getTile(5), loc(1, 0), ANGLE_240),
    TilePlacement(TILES.getTile(4), loc(1, 1), ANGLE_0),
    TilePlacement(TILES.getTile(6), loc(1, 2), ANGLE_60),
    TilePlacement(TILES.getTile(3), loc(2, 2), ANGLE_120),
    TilePlacement(TILES.getTile(1), loc(2, 1), ANGLE_180))

  def place7LoopWrongColorTiles: TantrixBoard = place7LoopWrongColor(SEVEN_TILES)
  def place7SolvedTiles: TantrixBoard = place7Solved(SEVEN_TILES)

  /** Places first tile in the middle. Three unplaced tiles remain. */
  def place3of6UnsolvedTiles: TantrixBoard = {
    var board = new TantrixBoard(SIX_TILES)
    val tile2 = TilePlacement(TILES.getTile(2), loc(0, 1), ANGLE_60)
    val tile3 = TilePlacement(TILES.getTile(3), loc(-1, 2), ANGLE_180)
    board = new TantrixBoard(board, tile2)
    board = new TantrixBoard(board, tile3)
    board
  }

  /** Places first tile in the middle. Three unplaced tiles remain. */
  def place1of5UnsolvedTiles = new TantrixBoard(FIVE_TILES)

  /** its a yellow path but not a loop */
  private def place3Unsolved(tiles: Seq[HexTile]) = {
    var board = new TantrixBoard(tiles)
    val tile2 = TilePlacement(TILES.getTile(2), loc(2, 0), ANGLE_0)
    val tile3 = TilePlacement(TILES.getTile(3), loc(2, 1), ANGLE_180)
    board = new TantrixBoard(board, tile2)
    board = new TantrixBoard(board, tile3)
    board
  }

  /** its a blue path but not a loop */
  private def place6Unsolved(tiles: Seq[HexTile]) = {
    var board = new TantrixBoard(tiles)
    val tileList = Seq(
      TilePlacement(TILES.getTile(2), loc(0, 1), ANGLE_60),
      TilePlacement(TILES.getTile(3), loc(-1, 2), ANGLE_180),
      TilePlacement(TILES.getTile(4), loc(-1, 1), ANGLE_0),
      TilePlacement(TILES.getTile(5), loc(-1, 0), ANGLE_240),
      TilePlacement(TILES.getTile(6), loc(0, 0), ANGLE_240))

    tileList.foreach(p => board = new TantrixBoard(board, p))
    board
  }

  private def place7LoopWrongColor(tiles: Seq[HexTile]) = {
    var board = new TantrixBoard(tiles)
    sevenTilesInAWrongRedLoop.foreach(p => board = new TantrixBoard(board, p))
    board
  }

  private def place7Solved(tiles: Seq[HexTile]) = {
    var board = new TantrixBoard(tiles)
    sevenTilesInABlueLoop.foreach(p => board = new TantrixBoard(board, p))
    board
  }

  /** Places first tile in the middle, and the three tiles do not form a primary path */
  def place3NonPathTiles: TantrixBoard = {
    var board = new TantrixBoard(THREE_TILES)
    val tile2 = TilePlacement(TILES.getTile(2), loc(2, 0), ANGLE_0)
    val tile3 = TilePlacement(TILES.getTile(3), loc(2, 1), ANGLE_120)
    board = new TantrixBoard(board, tile2)
    board = new TantrixBoard(board, tile3)
    board
  }

  /** Places first tile in the middle. Not a valid primary path. */
  def place3UnsolvedTiles2: TantrixBoard = {
    var board = new TantrixBoard(THREE_TILES)
    val tile2 = TilePlacement(TILES.getTile(3), loc(2, 1), ANGLE_0)
    val tile3 = TilePlacement(TILES.getTile(2), loc(3, 2), ANGLE_0)
    board = new TantrixBoard(board, tile2)
    board = new TantrixBoard(board, tile3)
    board
  }

  /** constructor places first tile in the middle */
  def place3SolvedTiles: TantrixBoard = place3Solved(THREE_TILES)

  /** constructor places first tile in the middle */
  def place3of6SolvedTiles: TantrixBoard = place3Solved(SIX_TILES)

  private def place3Solved(tiles: Seq[HexTile]) = {
    var board = new TantrixBoard(tiles)
    val tile2 = TilePlacement(TILES.getTile(2), loc(2, 1), ANGLE_60)
    val tile3 = TilePlacement(TILES.getTile(3), loc(2, 0), ANGLE_120)
    board = new TantrixBoard(board, tile2)
    board = new TantrixBoard(board, tile3)
    println(board)
    board
  }

  /** Places second tile in the middle */
  def place1of3Tiles_startingWithTile2: TantrixBoard = 
    new TantrixBoard(Seq(TILES.getTile(2), TILES.getTile(3), TILES.getTile(1)))
  

  /** Places first tile in the middle */
  def place4UnsolvedTiles: TantrixBoard = {
    var board = new TantrixBoard(FOUR_TILES)
    val tile2 = TilePlacement(TILES.getTile(2), loc(1, 2), ANGLE_240)
    val tile3 = TilePlacement(TILES.getTile(3), loc(0, 0), ANGLE_120)
    val tile4 = TilePlacement(TILES.getTile(4), loc(1, 0), ANGLE_180)
    board = new TantrixBoard(board, tile2)
    board = new TantrixBoard(board, tile3)
    board = new TantrixBoard(board, tile4)
    board
  }

  /** Places first tile in the middle */
  def place4SolvedTiles: TantrixBoard = {
    var board = new TantrixBoard(FOUR_TILES)
    val tile2 = TilePlacement(TILES.getTile(2), loc(1, 2), ANGLE_240)
    val tile3 = TilePlacement(TILES.getTile(3), loc(0, 0), ANGLE_180)
    val tile4 = TilePlacement(TILES.getTile(4), loc(0, 1), ANGLE_60)
    board = new TantrixBoard(board, tile2)
    board = new TantrixBoard(board, tile3)
    board = new TantrixBoard(board, tile4)
    board
  }

  /** There are 10 tiles that form a red loop, and there are two empty spaces within the loop.  */
  def place10LoopWithInnerSpace: TantrixBoard = {
    var board = new TantrixBoard(TEN_TILES)
    val tiles = Seq(
      TilePlacement(TILES.getTile(5), loc(0, 0), ANGLE_120),
      TilePlacement(TILES.getTile(6), loc(-1, 0), ANGLE_0),
      TilePlacement(TILES.getTile(2), loc(-2, 0), ANGLE_0),
      TilePlacement(TILES.getTile(3), loc(-1, 1), ANGLE_300),
      TilePlacement(TILES.getTile(10), loc(-2, 1), ANGLE_0),
      TilePlacement(TILES.getTile(4), loc(-2, 2), ANGLE_60),
      TilePlacement(TILES.getTile(8), loc(-1, 3), ANGLE_60),
      TilePlacement(TILES.getTile(9), loc(0, 2), ANGLE_60),
      TilePlacement(TILES.getTile(7), loc(1, 2), ANGLE_60)
    )
    tiles.foreach(tile => board = new TantrixBoard(board, tile))
    board
  }

  /** There are 9 tiles that almost form a red loop. One more will complete it. */
  def place9AlmostLoop: TantrixBoard = {
    var board = new TantrixBoard(FOURTEEN_TILES)
    val tiles = Seq(
      TilePlacement(TILES.getTile(5), loc(0, 0), ANGLE_120),
      TilePlacement(TILES.getTile(6), loc(-1, 0), ANGLE_0),
      TilePlacement(TILES.getTile(2), loc(-2, 0), ANGLE_0),
      TilePlacement(TILES.getTile(3), loc(-1, 1), ANGLE_300),
      TilePlacement(TILES.getTile(10), loc(-2, 1), ANGLE_0),
      TilePlacement(TILES.getTile(4), loc(-2, 2), ANGLE_60),
      TilePlacement(TILES.getTile(8), loc(-1, 3), ANGLE_60),
      TilePlacement(TILES.getTile(9), loc(0, 2), ANGLE_60)
    )
    tiles.foreach(tile => board = new TantrixBoard(board, tile))
    board
  }

  /** There are 9 tiles that almost form a red loop. One more will complete it. */
  def placeJumbled9: TantrixBoard = {
    var board = new TantrixBoard(FOURTEEN_TILES)
    val tiles = Seq(
      TilePlacement(TILES.getTile(9), loc(0, 0), ANGLE_120),
      TilePlacement(TILES.getTile(3), loc(-1, 0), ANGLE_60),
      TilePlacement(TILES.getTile(6), loc(-2, 0), ANGLE_0),
      TilePlacement(TILES.getTile(5), loc(-1, 1), ANGLE_300),
      TilePlacement(TILES.getTile(8), loc(-2, 1), ANGLE_0),
      TilePlacement(TILES.getTile(2), loc(-2, 2), ANGLE_60),
      TilePlacement(TILES.getTile(10), loc(-1, 3), ANGLE_60),
      TilePlacement(TILES.getTile(4), loc(0, 2), ANGLE_60)
    )
    tiles.foreach(tile => board = new TantrixBoard(board, tile))
    board
  }

  def loc(row: Int, col: Int): Location =
    new ByteLocation(row, col).incrementOnCopy(INITIAL_LOCATION).incrementOnCopy(-1, -1)
}

class TantrixTstUtil private() {
}