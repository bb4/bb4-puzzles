// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.shapeassign

/**
  * The places where two cells of the loop touch even though they are not next to each other in the loop. The side of
  * one cell and the side of the other cell that touch must have the same color. Contacts are added as the loop is
  * built, and removed again when it backs up, by going back to an earlier [[count]].
  * @param capacity the greatest number of contacts
  */
class ContactList(capacity: Int) {

  private val firstCells, firstSides, secondCells, secondSides = new Array[Int](capacity)
  private var numContacts = 0

  def count: Int = numContacts
  def firstCell(contact: Int): Int = firstCells(contact)
  def firstSide(contact: Int): Int = firstSides(contact)
  def secondCell(contact: Int): Int = secondCells(contact)
  def secondSide(contact: Int): Int = secondSides(contact)

  def add(firstCell: Int, firstSide: Int, secondCell: Int, secondSide: Int): Unit = {
    firstCells(numContacts) = firstCell
    firstSides(numContacts) = firstSide
    secondCells(numContacts) = secondCell
    secondSides(numContacts) = secondSide
    numContacts += 1
  }

  /** Forget the contacts that were added after the count was the given number. */
  def rollbackTo(earlierCount: Int): Unit = numContacts = earlierCount
}
