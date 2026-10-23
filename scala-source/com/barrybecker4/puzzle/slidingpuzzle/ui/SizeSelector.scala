// Copyright by Barry G. Becker, 2017 - 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.ui

import com.barrybecker4.puzzle.slidingpuzzle.model.ShuffleStyle

import java.awt.Choice

/**
  * The choices for the size of the puzzle. Each size comes in two variations, easy (a random walk shuffle that leaves
  * the tiles close to solved) and hard (uniformly random tiles). The easy one comes first.
  * @author Barry Becker
  */
object SizeSelector {
  /** The names of the sizes, smallest first. The smallest is a 2 by 2 board (3 tiles). */
  private val SIZE_NAMES = Array("3 Tiles", "8 Tiles", "15 Tiles", "24 Tiles")
  private val MIN_EDGE_LENGTH = 2
  private val STYLES = ShuffleStyle.values
  private val STYLE_NAMES = Map(ShuffleStyle.RANDOM_WALK -> "easy", ShuffleStyle.UNIFORM_RANDOM -> "hard")

  /** The choices shown, like "8 Tiles (easy)" and "8 Tiles (hard)" */
  val MENU_ITEMS: Array[String] = SIZE_NAMES.flatMap(size => STYLES.map(style => s"$size (${STYLE_NAMES(style)})"))

  /** The choice that is selected when the puzzle starts: the 8 puzzle, easy. */
  val DEFAULT_INDEX = 2

  /** @return the edge length of the puzzle for the menu item at the index */
  def edgeLengthAt(index: Int): Int = index / STYLES.length + MIN_EDGE_LENGTH

  /** @return how the tiles are mixed up for the menu item at the index */
  def styleAt(index: Int): ShuffleStyle = STYLES(index % STYLES.length)
}

final class SizeSelector() extends Choice {

  for (item <- SizeSelector.MENU_ITEMS) { add(item) }
  select(SizeSelector.DEFAULT_INDEX)

  /** @return the puzzle size (edge length) for what was selected.*/
  def getSelectedSize: Int = SizeSelector.edgeLengthAt(getSelectedIndex)

  /** @return how to shuffle the puzzle for what was selected.*/
  def getSelectedStyle: ShuffleStyle = SizeSelector.styleAt(getSelectedIndex)
}
