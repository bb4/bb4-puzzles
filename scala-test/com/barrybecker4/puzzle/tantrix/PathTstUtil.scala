// Copyright by Barry G. Becker, 2017 - 2023. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix

import com.barrybecker4.common.geometry.ByteLocation
import com.barrybecker4.puzzle.tantrix.model.HexTiles.TILES
import com.barrybecker4.puzzle.tantrix.model.Rotation.*
import com.barrybecker4.puzzle.tantrix.model.{TilePlacement, *}
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath

/**
  * @author Barry Becker
  */
object PathTstUtil {
  val LOWER_LEFT = new ByteLocation(22, 20)
  val LOWER_RIGHT = new ByteLocation(22, 21)
  val UPPER = new ByteLocation(21, 21)
  val UPPER_LEFT = new ByteLocation(21, 20)
  val TILE1: HexTile = TILES.getTile(1)
  val TILE2: HexTile = TILES.getTile(2)
  val TILE3: HexTile = TILES.getTile(3)
  val TILE4: HexTile = TILES.getTile(4)
  val TILE5: HexTile = TILES.getTile(5)
  val TILE6: HexTile = TILES.getTile(6)
  val TILE7: HexTile = TILES.getTile(7)
  val TILE8: HexTile = TILES.getTile(8)
  val TILE9: HexTile = TILES.getTile(9)
  val TILE10: HexTile = TILES.getTile(10)
  val TILE11: HexTile = TILES.getTile(11)
  val TILE12: HexTile = TILES.getTile(12)
  val TILE13: HexTile = TILES.getTile(13)
  val TILE14: HexTile = TILES.getTile(14)
  
  val LOOP_PATH3: TantrixPath = createPath(
    TilePlacement(TILE2, LOWER_RIGHT, ANGLE_60),
    TilePlacement(TILE1, UPPER, ANGLE_0),
    TilePlacement(TILE3, LOWER_LEFT, ANGLE_120)
  )

  /** left end of the yellow path is blocked by tile 2 */
  val NON_LOOP_PATH3: TantrixPath = createPath(
    TilePlacement(TILE2, LOWER_RIGHT, ANGLE_0),
    TilePlacement(TILE1, UPPER, ANGLE_0),
    TilePlacement(TILE3, LOWER_LEFT, ANGLE_120)
  )

  val LOOP_PATH4 = new TantrixPath(
    Seq(TilePlacement(TILE1, LOWER_LEFT, ANGLE_0),
      TilePlacement(TILE3, LOWER_RIGHT, ANGLE_0),
      TilePlacement(TILE4, UPPER, ANGLE_60),
      TilePlacement(TILE2, UPPER_LEFT, ANGLE_60)
    ), PathColor.RED, 4)

  val LOOP_PATH4_OF_5 = new TantrixPath(
    Seq(TilePlacement(TILE1, LOWER_LEFT, ANGLE_0),
      TilePlacement(TILE3, LOWER_RIGHT, ANGLE_0),
      TilePlacement(TILE4, UPPER, ANGLE_60),
      TilePlacement(TILE2, UPPER_LEFT, ANGLE_60)
    ), PathColor.RED, 5)

  val LOOP_PATH4_OF_6 = new TantrixPath(
    Seq(TilePlacement(TILE1, LOWER_LEFT, ANGLE_0),
      TilePlacement(TILE3, LOWER_RIGHT, ANGLE_0),
      TilePlacement(TILE4, UPPER, ANGLE_60),
      TilePlacement(TILE2, UPPER_LEFT, ANGLE_60)
    ), PathColor.RED, 6)

  val LOOP_PATH4_OF_7 = new TantrixPath(
    Seq(TilePlacement(TILE1, LOWER_LEFT, ANGLE_0),
      TilePlacement(TILE3, LOWER_RIGHT, ANGLE_0),
      TilePlacement(TILE4, UPPER, ANGLE_60),
      TilePlacement(TILE2, UPPER_LEFT, ANGLE_60)
    ), PathColor.RED, 7)

  val LOOP_PATH4_OF_8 = new TantrixPath(
    Seq(TilePlacement(TILE1, LOWER_LEFT, ANGLE_0),
      TilePlacement(TILE3, LOWER_RIGHT, ANGLE_0),
      TilePlacement(TILE4, UPPER, ANGLE_60),
      TilePlacement(TILE2, UPPER_LEFT, ANGLE_60)
    ), PathColor.RED, 8)

  val LOOP_PATH4_OF_9 = new TantrixPath(
    Seq(TilePlacement(TILE1, LOWER_LEFT, ANGLE_0),
      TilePlacement(TILE3, LOWER_RIGHT, ANGLE_0),
      TilePlacement(TILE4, UPPER, ANGLE_60),
      TilePlacement(TILE2, UPPER_LEFT, ANGLE_60)
    ), PathColor.RED, 9)

  val NON_LOOP_PATH4 = new TantrixPath(Seq(
    TilePlacement(TILE1, LOWER_LEFT, ANGLE_120),
    TilePlacement(TILE2, UPPER_LEFT, ANGLE_60),
    TilePlacement(TILE4, UPPER, ANGLE_60),
    TilePlacement(TILE3, LOWER_RIGHT, ANGLE_300)
  ), PathColor.RED, 4)

  val NON_LOOP_PATH4_OF_5 = new TantrixPath(Seq(
    TilePlacement(TILE1, LOWER_LEFT, ANGLE_120),
    TilePlacement(TILE2, UPPER_LEFT, ANGLE_60),
    TilePlacement(TILE4, UPPER, ANGLE_60),
    TilePlacement(TILE3, LOWER_RIGHT, ANGLE_300)
  ), PathColor.RED, 5)

  val NON_LOOP_PATH4_OF_6 = new TantrixPath(Seq(
    TilePlacement(TILE1, LOWER_LEFT, ANGLE_120),
    TilePlacement(TILE2, UPPER_LEFT, ANGLE_60),
    TilePlacement(TILE4, UPPER, ANGLE_60),
    TilePlacement(TILE3, LOWER_RIGHT, ANGLE_300)
  ), PathColor.RED, 6)

  val LINEAR_PATH4 = new TantrixPath(Seq(
    TilePlacement(TILE2, new ByteLocation(21, 19), ANGLE_60),
    TilePlacement(TILE4, new ByteLocation(21, 20), ANGLE_300),
    TilePlacement(TILE1, new ByteLocation(20, 20), ANGLE_240),
    TilePlacement(TILE3, new ByteLocation(20, 21), ANGLE_0)
  ), PathColor.RED, 4)

  val LOOP_PATH5 = new TantrixPath(Seq(
    TilePlacement(TILE1, new ByteLocation(20, 20), ANGLE_240),
    TilePlacement(TILE4, new ByteLocation(20, 21), ANGLE_60),
    TilePlacement(TILE3, new ByteLocation(21, 22), ANGLE_0),
    TilePlacement(TILE5, new ByteLocation(21, 21), ANGLE_0),
    TilePlacement(TILE2, new ByteLocation(21, 20), ANGLE_120)
  ), PathColor.RED, 5)

  val IMPERFECT_LOOP_PATH5 = new TantrixPath(Seq(
    TilePlacement(TILE1, new ByteLocation(20, 20), ANGLE_240),
    TilePlacement(TILE4, new ByteLocation(20, 21), ANGLE_60),
    TilePlacement(TILE3, new ByteLocation(21, 22), ANGLE_0),
    TilePlacement(TILE5, new ByteLocation(21, 21), ANGLE_180),
    TilePlacement(TILE2, new ByteLocation(21, 20), ANGLE_120)
  ), PathColor.RED, 5)

  // linear. Not very compact
  val NON_LOOP_PATH5 = new TantrixPath(Seq(
    TilePlacement(TILE2, new ByteLocation(19, 20), ANGLE_0),
    TilePlacement(TILE1, new ByteLocation(20, 20), ANGLE_0),
    TilePlacement(TILE4, new ByteLocation(20, 21), ANGLE_60),
    TilePlacement(TILE5, new ByteLocation(21, 21), ANGLE_240),
    TilePlacement(TILE3, new ByteLocation(22, 21), ANGLE_0)
  ), PathColor.RED, 5)

  val LINEAR_NON_LOOP_PATH5 = new TantrixPath(Seq(
    TilePlacement(TILE2, new ByteLocation(20, 18), ANGLE_120),
    TilePlacement(TILE4, new ByteLocation(20, 19), ANGLE_60),
    TilePlacement(TILE5, new ByteLocation(21, 20), ANGLE_120),
    TilePlacement(TILE1, new ByteLocation(22, 20), ANGLE_0),
    TilePlacement(TILE3, new ByteLocation(22, 21), ANGLE_60)
  ), PathColor.RED, 5)
  
  val NON_LOOP_S_PATH5 = new TantrixPath(Seq(
    TilePlacement(TILE5, new ByteLocation(19, 20), ANGLE_0),
    TilePlacement(TILE2, new ByteLocation(19, 21), ANGLE_300),
    TilePlacement(TILE1, new ByteLocation(20, 20), ANGLE_60),
    TilePlacement(TILE3, new ByteLocation(20, 19), ANGLE_180),
    TilePlacement(TILE4, new ByteLocation(21, 20), ANGLE_240)
  ), PathColor.RED, 5)
  
  val COMPACT_NON_LOOP_PATH5 = new TantrixPath(Seq(
    TilePlacement(TILE2, new ByteLocation(19, 20), ANGLE_0),
    TilePlacement(TILE1, new ByteLocation(20, 20), ANGLE_0),
    TilePlacement(TILE4, new ByteLocation(20, 21), ANGLE_60),
    TilePlacement(TILE5, new ByteLocation(21, 21), ANGLE_240),
    TilePlacement(TILE3, new ByteLocation(22, 21), ANGLE_0)
  ), PathColor.RED, 5)

  val LOOP_PATH6 = new TantrixPath(Seq(
    TilePlacement(TILE3, new ByteLocation(18, 20), ANGLE_240),
    TilePlacement(TILE2, new ByteLocation(19, 21), ANGLE_120),
    TilePlacement(TILE1, new ByteLocation(20, 21), ANGLE_60),
    TilePlacement(TILE5, new ByteLocation(21, 21), ANGLE_0),
    TilePlacement(TILE4, new ByteLocation(20, 20), ANGLE_120),
    TilePlacement(TILE6, new ByteLocation(19, 20), ANGLE_180)
  ), PathColor.BLUE, 6)

  val NON_LOOP_PATH6 = new TantrixPath(Seq(
    TilePlacement(TILE3, new ByteLocation(18, 20), ANGLE_300),
    TilePlacement(TILE2, new ByteLocation(19, 21), ANGLE_120),
    TilePlacement(TILE1, new ByteLocation(20, 21), ANGLE_60),
    TilePlacement(TILE5, new ByteLocation(21, 21), ANGLE_0),
    TilePlacement(TILE4, new ByteLocation(20, 20), ANGLE_120),
    TilePlacement(TILE6, new ByteLocation(19, 20), ANGLE_180)
  ), PathColor.BLUE, 6)

  val LOOP_PATH7 = new TantrixPath(Seq(
    TilePlacement(TILE2, new ByteLocation(19, 22), ANGLE_0),
    TilePlacement(TILE5, new ByteLocation(19, 23), ANGLE_120),
    TilePlacement(TILE4, new ByteLocation(20, 22), ANGLE_240),
    TilePlacement(TILE6, new ByteLocation(21, 22), ANGLE_300),
    TilePlacement(TILE3, new ByteLocation(21, 21), ANGLE_0),
    TilePlacement(TILE1, new ByteLocation(20, 21), ANGLE_60),
    TilePlacement(TILE7, new ByteLocation(19, 21), ANGLE_60)
  ), PathColor.BLUE, 7)

  val NON_LOOP_PATH7 = new TantrixPath(Seq(
    TilePlacement(TILE3, new ByteLocation(21, 20), ANGLE_60),
    TilePlacement(TILE6, new ByteLocation(20, 20), ANGLE_0),
    TilePlacement(TILE5, new ByteLocation(19, 20), ANGLE_240),
    TilePlacement(TILE2, new ByteLocation(19, 21), ANGLE_0),
    TilePlacement(TILE7, new ByteLocation(19, 22), ANGLE_300),
    TilePlacement(TILE4, new ByteLocation(20, 21), ANGLE_240),
    TilePlacement(TILE1, new ByteLocation(21, 21), ANGLE_240)
  ), PathColor.BLUE, 7)

  val NON_LOOP_SPACE_PATH7 = new TantrixPath(Seq(
    TilePlacement(TILE3, new ByteLocation(20, 19), ANGLE_60),
    TilePlacement(TILE6, new ByteLocation(19, 20), ANGLE_120),
    TilePlacement(TILE2, new ByteLocation(19, 21), ANGLE_0),
    TilePlacement(TILE7, new ByteLocation(19, 22), ANGLE_300),
    TilePlacement(TILE4, new ByteLocation(20, 21), ANGLE_60),
    TilePlacement(TILE1, new ByteLocation(21, 21), ANGLE_0),
    TilePlacement(TILE5, new ByteLocation(21, 20), ANGLE_240)
  ), PathColor.BLUE, 7)

  val NON_LOOP_WIGGLE_PATH7 = new TantrixPath(Seq(
    TilePlacement(TILE4, new ByteLocation(19, 20), ANGLE_0),
    TilePlacement(TILE7, new ByteLocation(19, 19), ANGLE_60),
    TilePlacement(TILE2, new ByteLocation(20, 19), ANGLE_120),
    TilePlacement(TILE3, new ByteLocation(21, 20), ANGLE_60),
    TilePlacement(TILE5, new ByteLocation(20, 20), ANGLE_180),
    TilePlacement(TILE1, new ByteLocation(21, 21), ANGLE_300),
    TilePlacement(TILE6, new ByteLocation(21, 22), ANGLE_300)
  ), PathColor.BLUE, 7)

  val LOOP_SPACE_PATH8 = new TantrixPath(Seq(
    TilePlacement(TILE2, new ByteLocation(19, 21), ANGLE_0),
    TilePlacement(TILE1, new ByteLocation(20, 21), ANGLE_0),
    TilePlacement(TILE3, new ByteLocation(20, 22), ANGLE_60),
    TilePlacement(TILE9, new ByteLocation(21, 22), ANGLE_60),
    TilePlacement(TILE7, new ByteLocation(22, 21), ANGLE_60),
    TilePlacement(TILE4, new ByteLocation(22, 20), ANGLE_240),
    TilePlacement(TILE8, new ByteLocation(21, 20), ANGLE_240),
    TilePlacement(TILE5, new ByteLocation(20, 20), ANGLE_60)
  ), PathColor.RED, 8)

  // Has primary path loop, but many other edges do not match.
  val IMPERFECT_LOOP_PATH9 = new TantrixPath(Seq(
    TilePlacement(TILE5, new ByteLocation(19, 20), ANGLE_0),
    TilePlacement(TILE2, new ByteLocation(20, 20), ANGLE_0),
    TilePlacement(TILE3, new ByteLocation(19, 21), ANGLE_0),
    TilePlacement(TILE9, new ByteLocation(20, 21), ANGLE_60),
    TilePlacement(TILE1, new ByteLocation(20, 22), ANGLE_300),
    TilePlacement(TILE7, new ByteLocation(21, 22), ANGLE_0),
    TilePlacement(TILE6, new ByteLocation(21, 21), ANGLE_0),
    TilePlacement(TILE4, new ByteLocation(21, 20), ANGLE_60),
    TilePlacement(TILE8, new ByteLocation(20, 19), ANGLE_300)
  ), PathColor.YELLOW, 9)

  val LOOP_PATH14 = new TantrixPath(Seq(
    TilePlacement(TILE7, new ByteLocation(18, 19), ANGLE_0),
    TilePlacement(TILE9, new ByteLocation(19, 20), ANGLE_240),
    TilePlacement(TILE5, new ByteLocation(19, 21), ANGLE_120),
    TilePlacement(TILE8, new ByteLocation(20, 20), ANGLE_120),
    TilePlacement(TILE4, new ByteLocation(20, 21), ANGLE_0),
    TilePlacement(TILE13, new ByteLocation(20, 22), ANGLE_120),
    TilePlacement(TILE2, new ByteLocation(21, 22), ANGLE_60),
    TilePlacement(TILE3, new ByteLocation(22, 21), ANGLE_60),
    TilePlacement(TILE12, new ByteLocation(21, 21), ANGLE_0),
    TilePlacement(TILE6, new ByteLocation(21, 20), ANGLE_240),
    TilePlacement(TILE11, new ByteLocation(20, 19), ANGLE_300),
    TilePlacement(TILE1, new ByteLocation(20, 18), ANGLE_300),
    TilePlacement(TILE14, new ByteLocation(19, 18), ANGLE_0),
    TilePlacement(TILE10, new ByteLocation(19, 19), ANGLE_240),
  ), PathColor.BLUE, 14)

  val IMPERFECT_LOOP_PATH14 = new TantrixPath(Seq(
    TilePlacement(TILE7, new ByteLocation(18, 19), ANGLE_0),
    TilePlacement(TILE1, new ByteLocation(19, 20), ANGLE_300),
    TilePlacement(TILE5, new ByteLocation(19, 21), ANGLE_120),
    TilePlacement(TILE8, new ByteLocation(20, 20), ANGLE_120),
    TilePlacement(TILE4, new ByteLocation(20, 21), ANGLE_0),
    TilePlacement(TILE13, new ByteLocation(20, 22), ANGLE_120),
    TilePlacement(TILE2, new ByteLocation(21, 22), ANGLE_60),
    TilePlacement(TILE3, new ByteLocation(22, 21), ANGLE_60),
    TilePlacement(TILE12, new ByteLocation(21, 21), ANGLE_0),
    TilePlacement(TILE6, new ByteLocation(21, 20), ANGLE_240),
    TilePlacement(TILE11, new ByteLocation(20, 19), ANGLE_300),
    TilePlacement(TILE9, new ByteLocation(20, 18), ANGLE_240),
    TilePlacement(TILE14, new ByteLocation(19, 18), ANGLE_0),
    TilePlacement(TILE10, new ByteLocation(19, 19), ANGLE_240),
    ), PathColor.BLUE, 14)
  

  def createPathList: List[TantrixPath] = {
    // for each of the 7 permuted paths, we expect that tile 2 will be the middle/pivot tile.
    val lowerLeft = new ByteLocation(22, 20)
    val lowerRight = new ByteLocation(22, 21)
    val pivot = TilePlacement(TILES.getTile(1), new ByteLocation(21, 21), ANGLE_0)
    val tile2 = TILES.getTile(2)
    val tile3 = TILES.getTile(3)
    List(createPath(TilePlacement(tile2, lowerLeft, ANGLE_0), pivot, TilePlacement(tile3, lowerRight, ANGLE_240)), createPath(TilePlacement(tile2, lowerLeft, ANGLE_300), pivot, TilePlacement(tile3, lowerRight, ANGLE_180)), createPath(TilePlacement(tile2, lowerLeft, ANGLE_300), pivot, TilePlacement(tile3, lowerRight, ANGLE_240)), // complete loop!
      createPath(TilePlacement(tile3, lowerLeft, ANGLE_120), pivot, TilePlacement(tile2, lowerRight, ANGLE_60)), createPath(TilePlacement(tile3, lowerLeft, ANGLE_120), pivot, TilePlacement(tile2, lowerRight, ANGLE_0)), createPath(TilePlacement(tile3, lowerLeft, ANGLE_180), pivot, TilePlacement(tile2, lowerRight, ANGLE_60)), // complete loop!
      createPath(TilePlacement(tile3, lowerLeft, ANGLE_180), pivot, TilePlacement(tile2, lowerRight, ANGLE_0)))
  }

  def createPath(placements: TilePlacement*) = new TantrixPath(placements, PathColor.YELLOW, placements.length)
}
