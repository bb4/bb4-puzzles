// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model

/**
  * Helps with showing large numbers on the board. Two digit numbers are converted to letters.
  *
  * @author Barry Becker
  */
object ValueConverter {
  /**
    * Get a one character symbol for the value.
    *
    * @param value value to get symbol for.
    * @return symbol corresponding to the specified value.
    */
  def getSymbol(value: Int): String = value match {
    case 0 => "."
    case 10 => "X"
    case 11 => "A"
    case 12 => "B"
    case 13 => "C"
    case 14 => "D"
    case 15 => "E"
    case 16 => "F"
    case 17 => "G"
    case 18 => "H"
    case 19 => "I"
    case 20 => "J"
    case 21 => "K"
    case 22 => "L"
    case 23 => "M"
    case 24 => "N"
    case 25 => "O"
    case _ => Integer.toString(value)
  }

  /**
    * Get the integer value for the specified symbol.
    *
    * @param symbol symbol
    * @param maxValue maximum allowed value
    * @return integer value
    * @throws IllegalArgumentException if not a valid symbol for the puzzle.
    */
  def getValue(symbol: Char, maxValue: Int): Int = {
    val upperSymb = Character.toString(symbol).toUpperCase.charAt(0)
    val value = upperSymb match {
      case 'X' => 10
      case 'A' => 11
      case 'B' => 12
      case 'C' => 13
      case 'D' => 14
      case 'E' => 15
      case 'F' => 16
      case 'G' => 17
      case 'H' => 18
      case 'I' => 19
      case 'J' => 20
      case 'K' => 21
      case 'L' => 22
      case 'M' => 23
      case 'N' => 24
      case 'O' => 25
      case _ =>
        try Character.toString(symbol).toInt
        catch {
          case nfe: NumberFormatException => throw new IllegalArgumentException("Invalid: " + symbol)
        }
    }
    if (value == 0 || value > maxValue) throw new IllegalArgumentException("Invalid: " + symbol)
    value
  }
}

