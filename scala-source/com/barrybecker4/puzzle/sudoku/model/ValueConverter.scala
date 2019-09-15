// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model

/**
  * Helps with showing large numbers on the board. Two digit numbers are converted to letters.
  * @author Barry Becker
  */
object ValueConverter {

  /** Get a one character symbol for the value.
    * @param value value to get symbol for.
    * @return symbol corresponding to the specified value.
    */
  def getSymbol(value: Int): String = value match {
    case 0 => "."
    case 10 => "X"
    case i if i > 10 => (65 + i - 11).asInstanceOf[Char].toString // A=65 ASCII
    case _ => Integer.toString(value)
  }

  /** Get the integer value for the specified symbol.
    * throws IllegalArgumentException if not a valid symbol for the puzzle.
    * @param symbol symbol
    * @param maxValue maximum allowed value
    * @return integer value
    */
  def getValue(symbol: Char, maxValue: Int): Int = {
    val upperSymb = Character.toString(symbol).toUpperCase.charAt(0)
    val value = upperSymb match {
      case 'X' => 10
      case c if c >= 'A' => c - 65 + 11
      case _ =>
        try Character.toString(symbol).toInt
        catch {
          case nfe: NumberFormatException => throw new IllegalArgumentException("Invalid: " + symbol, nfe)
        }
    }
    if (value == 0 || value > maxValue) throw new IllegalArgumentException("Invalid: " + symbol)
    value
  }
}
