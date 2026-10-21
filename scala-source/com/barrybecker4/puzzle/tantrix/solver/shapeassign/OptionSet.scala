// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.shapeassign

/**
  * A set of the ways that a tile could be placed in a cell, held in the bits of a long. An option is a tile, and which of
  * the (up to two) rotations of it have the primary path in the right place. Option number 2t is the first rotation
  * of tile t, and 2t+1 is the second one, which only straight tiles have. That limits the loop to 32 tiles.
  */
object OptionSet {
  val MAX_TILES = 32

  /** A tile has up to two rotations that put its primary path in the right place: the straight tiles have two, and the others one. */
  val OPTIONS_PER_TILE = 2

  /** The number of bits in a long. */
  val MAX_OPTIONS: Int = OPTIONS_PER_TILE * MAX_TILES

  /** The bits for both of the options of a tile, when the tile is identified by its first option. */
  private val BOTH_OPTIONS_OF_TILE = 3L

  /** The bits of the first option of every tile: 0, 2, 4, and so on. */
  private val FIRST_OPTION_BITS = 0x5555555555555555L

  def optionOf(tile: Int, rotationNumber: Int): Int = OPTIONS_PER_TILE * tile + rotationNumber
  def tileOf(option: Int): Int = option / OPTIONS_PER_TILE
  def single(option: Int): Long = 1L << option
  def firstOption(options: Long): Int = java.lang.Long.numberOfTrailingZeros(options)
  def withoutFirst(options: Long): Long = options & (options - 1)
  def size(options: Long): Int = java.lang.Long.bitCount(options)

  /** @return true if the test is true for some option in the set. The options are tested lowest first, until one passes. */
  inline def exists(options: Long)(inline test: Int => Boolean): Boolean = {
    var remaining = options
    var found = false
    while (!found && remaining != 0L) { found = test(firstOption(remaining)); remaining = withoutFirst(remaining) }
    found
  }

  /** @return the union of the sets that the function gives for each of the options in the set of options. */
  inline def unionOver(options: Long)(inline setFor: Int => Long): Long = {
    var union = 0L
    var remaining = options
    while (remaining != 0L) { union |= setFor(firstOption(remaining)); remaining = withoutFirst(remaining) }
    union
  }

  /** @return the union of the sets that the function gives for each number from 0 until the count. */
  inline def unionOverRange(count: Int)(inline setFor: Int => Long): Long = {
    var union = 0L
    var number = 0
    while (number < count) { union |= setFor(number); number += 1 }
    union
  }

  /** @return true if there is more than one option in the set. */
  def hasChoice(options: Long): Boolean = size(options) > 1

  /** @return a set with a bit for each tile that has an option in the set, which is the bit of its first option.
    * Moving the bits of the second options down one puts them on top of the bits of the first options.
    */
  def tilesOf(options: Long): Long = (options | (options >>> 1)) & FIRST_OPTION_BITS

  def hasExactlyOneTile(options: Long): Boolean = {
    val tiles = tilesOf(options)
    tiles != 0L && (tiles & (tiles - 1)) == 0L
  }

  /** @param tileBit the bit from [[tilesOf]] for the tile. @return both of the options of that tile. */
  def optionsOfTile(tileBit: Long): Long = BOTH_OPTIONS_OF_TILE << java.lang.Long.numberOfTrailingZeros(tileBit)
}
