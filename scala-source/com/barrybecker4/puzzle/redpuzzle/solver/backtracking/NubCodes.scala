// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.solver.backtracking

import com.barrybecker4.puzzle.redpuzzle.model.Nub

/**
  * Turns a nub into a small integer so that two nubs can be compared with a single `==` instead of comparing
  * a suit and an inny/outy flag.
  *
  * The code is `suitIndex * 2 + (1 if outy else 0)`. The two nubs of one suit that fit together (an inny and an outy)
  * differ only in the lowest bit, so the nub that fits a given nub is found by flipping that bit.
  * @author Barry Becker
  */
object NubCodes {

  /** The suit symbols, in the order that gives them their index. */
  private val SUIT_SYMBOLS = "SCHD"

  /** Flipping this bit turns an inny into the outy of the same suit, and the other way around. */
  private val OUTY_BIT = 1

  /** @return the integer code for the nub. */
  def encode(nub: Nub): Int =
    SUIT_SYMBOLS.indexOf(nub.getSuitSymbol) * 2 + (if (nub.isOuty) OUTY_BIT else 0)

  /** @return the code of the one nub that fits with the nub that has the given code. */
  def complement(code: Int): Int = code ^ OUTY_BIT
}
