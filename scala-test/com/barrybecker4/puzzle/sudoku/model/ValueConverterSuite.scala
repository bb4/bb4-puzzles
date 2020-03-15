// Copyright by Barry G. Becker, 2019. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model

import org.scalatest.funsuite.AnyFunSuite

class ValueConverterSuite extends AnyFunSuite {

  test("Convert from number to character") {
    assert(ValueConverter.getSymbol(10) == "X")
    assert(ValueConverter.getSymbol(5) == "5")
    assert(ValueConverter.getSymbol(11) == "A")
    assert(ValueConverter.getSymbol(20) == "J")
    assert(ValueConverter.getSymbol(25) == "O")
  }

  test("Convert from character to number") {
    assert(ValueConverter.getValue('X', 20) == 10)
    assert(ValueConverter.getValue('5', 20) == 5)
    assert(ValueConverter.getValue('J', 30) == 20)
    assert(ValueConverter.getValue('A', 25) == 11)
    assert(ValueConverter.getValue('O', 25) == 25)
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
