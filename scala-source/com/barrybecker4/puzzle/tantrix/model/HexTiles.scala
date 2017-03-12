// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model

import com.barrybecker4.puzzle.tantrix.model.HexTiles.RAND
import com.barrybecker4.puzzle.tantrix.model.PathColor._

import scala.util.Random

object HexTiles {
  val RAND = new Random()
}

/**
  * The complete set of hexagonal tantrix tiles. There are 56 of them.
  *
  * @author Barry Becker
  */
class HexTiles()  {
  var i = 1
  val tiles = Seq(
    HexTile(1, YELLOW, new PathColors(RED, BLUE, RED, BLUE, YELLOW, YELLOW)),
    HexTile(2, YELLOW, new PathColors(BLUE, YELLOW, YELLOW, BLUE, RED, RED)),
    HexTile(3, YELLOW, new PathColors(BLUE, BLUE, RED, RED, YELLOW, YELLOW)),
    HexTile(4, RED, new PathColors(BLUE, YELLOW, RED, BLUE, RED, YELLOW)),
    HexTile(5, RED, new PathColors(RED, BLUE, BLUE, RED, YELLOW, YELLOW)),
    HexTile(6, BLUE, new PathColors(YELLOW, RED, BLUE, YELLOW, BLUE, RED)),
    HexTile(7, BLUE, new PathColors(RED, YELLOW, RED, YELLOW, BLUE, BLUE)),
    HexTile(8, BLUE, new PathColors(YELLOW, RED, YELLOW, RED, BLUE, BLUE)),
    HexTile(9, YELLOW, new PathColors(RED, YELLOW, BLUE, RED, BLUE, YELLOW)),
    HexTile(10, RED, new PathColors(RED, YELLOW, YELLOW, BLUE, RED, BLUE)),
    HexTile(11, RED, new PathColors(BLUE, RED, RED, YELLOW, BLUE, YELLOW)),
    HexTile(12, YELLOW, new PathColors(YELLOW, RED, RED, BLUE, YELLOW, BLUE)),
    HexTile(13, BLUE, new PathColors(YELLOW, BLUE, BLUE, YELLOW, RED, RED)),
    HexTile(14, BLUE, new PathColors(BLUE, YELLOW, YELLOW, RED, RED, BLUE)),
    HexTile(15, RED, new PathColors(RED, GREEN, GREEN, RED, YELLOW, YELLOW)),
    HexTile(16, RED, new PathColors(YELLOW, RED, RED, YELLOW, GREEN, GREEN)),
    HexTile(17, YELLOW, new PathColors(GREEN, YELLOW, YELLOW, RED, GREEN, RED)),
    HexTile(18, RED, new PathColors(RED, YELLOW, YELLOW, GREEN, RED, GREEN)),
    HexTile(19, RED, new PathColors(GREEN, RED, RED, YELLOW, GREEN, YELLOW)),
    HexTile(20, YELLOW, new PathColors(YELLOW, RED, RED, GREEN, YELLOW, GREEN)),
    HexTile(21, YELLOW, new PathColors(GREEN, YELLOW, YELLOW, RED, RED, GREEN)),
    HexTile(22, YELLOW, new PathColors(GREEN, YELLOW, YELLOW, GREEN, RED, RED)),
    HexTile(23, YELLOW, new PathColors(RED, YELLOW, YELLOW, GREEN, GREEN, RED)),
    HexTile(24, RED, new PathColors(BLUE, RED, RED, BLUE, GREEN, GREEN)),
    HexTile(25, RED, new PathColors(BLUE, RED, RED, GREEN, GREEN, BLUE)),
    HexTile(26, RED, new PathColors(GREEN, BLUE, BLUE, GREEN, RED, RED)),
    HexTile(27, RED, new PathColors(BLUE, RED, RED, GREEN, BLUE, GREEN)),
    HexTile(28, RED, new PathColors(BLUE, GREEN, GREEN, RED, RED, BLUE)),
    HexTile(29, RED, new PathColors(GREEN, RED, RED, BLUE, GREEN, BLUE)),
    HexTile(30, RED, new PathColors(RED, GREEN, GREEN, RED, BLUE, BLUE)),
    HexTile(31, YELLOW, new PathColors(RED, GREEN, GREEN, YELLOW, RED, YELLOW)),
    HexTile(32, GREEN, new PathColors(GREEN, YELLOW, RED, GREEN, RED, YELLOW)),
    HexTile(33, GREEN, new PathColors(YELLOW, GREEN, GREEN, RED, YELLOW, RED)),
    HexTile(34, GREEN, new PathColors(RED, YELLOW, GREEN, RED, GREEN, YELLOW)),
    HexTile(35, GREEN, new PathColors(YELLOW, RED, GREEN, YELLOW, GREEN, RED)),
    HexTile(36, GREEN, new PathColors(RED, GREEN, GREEN, BLUE, RED, BLUE)),
    HexTile(37, GREEN, new PathColors(GREEN, BLUE, BLUE, RED, GREEN, RED)),
    HexTile(38, GREEN, new PathColors(RED, BLUE, BLUE, GREEN, RED, GREEN)),
    HexTile(39, BLUE, new PathColors(GREEN, BLUE, RED, GREEN, RED, BLUE)),
    HexTile(40, BLUE, new PathColors(BLUE, GREEN, RED, BLUE, RED, GREEN)),  // 40
    HexTile(41, BLUE, new PathColors(BLUE, GREEN, GREEN, RED, BLUE, RED)),
    HexTile(42, BLUE, new PathColors(RED, GREEN, BLUE, RED, BLUE, GREEN)),
    HexTile(43, BLUE, new PathColors(GREEN, YELLOW, YELLOW, BLUE, BLUE, GREEN)),
    HexTile(44, YELLOW, new PathColors(YELLOW, GREEN, BLUE, YELLOW, BLUE, GREEN)),
    HexTile(45, GREEN, new PathColors(BLUE, YELLOW, YELLOW, GREEN, GREEN, BLUE)),
    HexTile(46, GREEN, new PathColors(BLUE, GREEN, GREEN, YELLOW, BLUE, YELLOW)),
    HexTile(47, GREEN, new PathColors(YELLOW, GREEN, GREEN, YELLOW, BLUE, BLUE)),
    HexTile(48, WHITE, new PathColors(BLUE, GREEN, GREEN, BLUE, YELLOW, YELLOW)),
    HexTile(49, WHITE, new PathColors(GREEN, YELLOW, YELLOW, GREEN, BLUE, BLUE)),
    HexTile(50, WHITE, new PathColors(BLUE, GREEN, YELLOW, BLUE, YELLOW, GREEN)), // 50
    HexTile(51, WHITE, new PathColors(GREEN, YELLOW, BLUE, GREEN, BLUE, YELLOW)),
    HexTile(52, WHITE, new PathColors(YELLOW, GREEN, GREEN, BLUE, YELLOW, BLUE)),
    HexTile(53, WHITE, new PathColors(GREEN, YELLOW, YELLOW, BLUE, GREEN, BLUE)),
    HexTile(54, WHITE, new PathColors(BLUE, YELLOW, YELLOW, GREEN, BLUE, GREEN)),
    HexTile(55, WHITE, new PathColors(GREEN, BLUE, BLUE, YELLOW, GREEN, YELLOW)),
    HexTile(56, WHITE, new PathColors(YELLOW, BLUE, BLUE, GREEN, YELLOW, GREEN)))

  /**
    * Get a specific tile by its tantrix number (base index of 1, not 0).
    * @param tantrixNumber the number on the back of the tile.
    */
  def getTile(tantrixNumber: Int): HexTile = tiles(tantrixNumber - 1)

  def createOrderedList(numTiles: Int): Seq[HexTile] = tiles.slice(0, numTiles)
  def createRandomList(numTiles: Int): Seq[HexTile] = RAND.shuffle(tiles.slice(0, numTiles))
}
