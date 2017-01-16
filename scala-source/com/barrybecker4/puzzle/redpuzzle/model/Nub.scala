// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.model

/**
  * That little thing on the edge of a piece that allows it to connect to another.
  * It can either be an inny or an outty, like your belly button.
  *
  * @author Barry Becker
  */
object Nub {

  private val SPADE = Suit('S')
  private val CLUB = Suit('C')
  private val HEART = Suit('H')
  private val DIAMOND = Suit('D')

  val INNY_SPADE = new Nub(SPADE, false)
  val OUTY_SPADE = new Nub(SPADE, true)
  val INNY_CLUB = new Nub(CLUB, false)
  val OUTY_CLUB = new Nub(CLUB, true)
  val INNY_HEART = new Nub(HEART, false)
  val OUTY_HEART = new Nub(HEART, true)
  val INNY_DIAMOND = new Nub(DIAMOND, false)
  val OUTY_DIAMOND = new Nub(DIAMOND, true)

  /**
    * Determines the shape of the nub.
    * @param symbol the character symbol associated with this Suit
    */
  case class Suit private(symbol: Char)
}


case class Nub(suit: Nub.Suit, isOuty: Boolean) {

  /** @return the char symbol used to represent this nub's suit. */
  def getSuitSymbol: Char = suit.symbol

  /**
    * Check to see if this nub fits with another one.
    *
    * @param nub other nub to try and fit with.
    * @return true if the nubs fit together.
    */
  def fitsWith (nub: Nub): Boolean = {
    val suitMatch: Boolean = this.suit == nub.suit
    val nubMatch: Boolean = this.isOuty != nub.isOuty
    suitMatch && nubMatch
  }

  /**
    * @return nice readable string representation of a nub
    */
  override def toString: String = (if (isOuty) "outy" else "inny") + ' ' + suit
}
