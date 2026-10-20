// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.shapeassign

import org.scalatest.funsuite.AnyFunSuite

class ContactListSuite extends AnyFunSuite {

  test("contacts are remembered in the order they were added") {
    val contacts = new ContactList(4)
    contacts.add(3, 1, 0, 4)
    contacts.add(5, 2, 2, 5)
    assertResult(2)(contacts.count)
    assertResult((5, 2, 2, 5))((contacts.firstCell(1), contacts.firstSide(1), contacts.secondCell(1), contacts.secondSide(1)))
  }

  test("rolling back forgets the contacts added since") {
    val contacts = new ContactList(4)
    contacts.add(3, 1, 0, 4)
    val before = contacts.count
    contacts.add(5, 2, 2, 5)
    contacts.rollbackTo(before)
    assertResult(1)(contacts.count)
    contacts.add(6, 0, 1, 3)
    assertResult(6)(contacts.firstCell(1))
  }
}
