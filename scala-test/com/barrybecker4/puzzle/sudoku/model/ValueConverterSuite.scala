// Copyright by Barry G. Becker, 2019. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model

import org.scalatest.FunSuite

class ValueConverterSuite extends FunSuite {

  test("Convert from number to character") {
    assert(ValueConverter.getSymbol(10) == "X")
    assert(ValueConverter.getSymbol(5) == "5")
    assert(ValueConverter.getSymbol(11) == "A")
    assert(ValueConverter.getSymbol(20) == "J")
    assert(ValueConverter.getSymbol(25) == "O")
  }

  test("Convert from character to number") {
    assert(ValueConverter.getValue('X'.asInstanceOf[Char], 20) == 10)
    assert(ValueConverter.getValue('5'.asInstanceOf[Char], 20) == 5)
    assert(ValueConverter.getValue('A'.asInstanceOf[Char], 25) == 11)
    assert(ValueConverter.getValue('O'.asInstanceOf[Char], 25) == 25)
  }

  test("Convert from character to number when too big") {
    assertThrows[IllegalArgumentException] {
      ValueConverter.getValue('X'.asInstanceOf[Char], 9)
    }
  }

  test("Convert from character to number when 0") {
    assertThrows[IllegalArgumentException] {
      ValueConverter.getValue('0'.asInstanceOf[Char], 9)
    }
  }
}
