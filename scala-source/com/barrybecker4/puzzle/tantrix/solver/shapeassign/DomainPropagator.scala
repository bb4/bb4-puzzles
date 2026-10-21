// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.shapeassign

object DomainPropagator {
  /** What happened to the domains when one rule was applied. The larger numbers outrank the smaller ones. */
  private val UNCHANGED = 0
  private val CHANGED = 1
  private val FAILED = 2
  // Combining the results of rules with Math.max gives the worst one, since FAILED > CHANGED > UNCHANGED.
}

/**
  * Reduces the domains of the cells, which are the sets of options that each one could still have, using two rules.
  * 1) Where two cells touch, an option of one needs an option of the other that has the same color on the sides that touch.
  * 2) When a cell has only one tile left in its domain, no other cell can use that tile.
  * The rules are applied over and over until they change nothing, or until some cell has no options left.
  * The domains of the cells that have not been placed yet are empty, and are left alone.
  */
class DomainPropagator(numCells: Int, options: CellOptions, contacts: ContactList) {
  import DomainPropagator.*

  /** @return false if some cell is left with no options */
  def propagate(domains: Array[Long]): Boolean = {
    var status = CHANGED
    while (status == CHANGED) status = applyRulesOnce(domains)
    status != FAILED
  }

  private def applyRulesOnce(domains: Array[Long]): Int = {
    val afterContacts = matchTouchingCells(domains)
    if (afterContacts == FAILED) FAILED else Math.max(afterContacts, removeTilesThatAreTaken(domains))
  }

  private def matchTouchingCells(domains: Array[Long]): Int =
    worstStatus(0, contacts.count)(contact => matchContact(domains, contact))

  private def matchContact(domains: Array[Long], contact: Int): Int = {
    val first = contacts.firstCell(contact)
    val second = contacts.secondCell(contact)
    val firstStatus = keepOptionsMatching(domains, first, contacts.firstSide(contact), second, contacts.secondSide(contact))
    if (firstStatus == FAILED) FAILED
    else Math.max(firstStatus, keepOptionsMatching(domains, second, contacts.secondSide(contact), first, contacts.firstSide(contact)))
  }

  /** Keep only the options of the cell that have a color on its side that some option of the other cell has on its side. */
  private def keepOptionsMatching(domains: Array[Long], cell: Int, side: Int, otherCell: Int, otherSide: Int): Int = {
    val allowedColors = colorsOnSide(domains(otherCell), otherCell, otherSide)
    replaceDomain(domains, cell, optionsWithColors(domains(cell), cell, side, allowedColors))
  }

  /** @return a bit set of the colors found on the side among the options. */
  private def colorsOnSide(optionSet: Long, cell: Int, side: Int): Int =
    OptionSet.unionOver(optionSet)(option => 1L << options.colorOnSide(cell, option, side)).toInt

  private def optionsWithColors(optionSet: Long, cell: Int, side: Int, allowedColors: Int): Long =
    OptionSet.unionOver(optionSet)(option => keptIfAllowed(option, cell, side, allowedColors))

  private def keptIfAllowed(option: Int, cell: Int, side: Int, allowedColors: Int): Long =
    if ((allowedColors & (1 << options.colorOnSide(cell, option, side))) != 0) OptionSet.single(option) else 0L

  /** Store the new domain for the cell. @return how it compares with the old one. */
  private def replaceDomain(domains: Array[Long], cell: Int, newDomain: Long): Int = {
    val status = if (newDomain == 0L) FAILED else if (newDomain == domains(cell)) UNCHANGED else CHANGED
    domains(cell) = newDomain
    status
  }

  private def removeTilesThatAreTaken(domains: Array[Long]): Int =
    worstStatus(LoopCells.FIRST_NEW_CELL, numCells) { cell =>
      if (OptionSet.hasExactlyOneTile(domains(cell))) removeTileFromOthers(domains, cell) else UNCHANGED
    }

  private def removeTileFromOthers(domains: Array[Long], owner: Int): Int = {
    val taken = OptionSet.optionsOfTile(OptionSet.tilesOf(domains(owner)))
    worstStatus(LoopCells.FIRST_NEW_CELL, numCells)(other => if (other != owner) removeOptions(domains, other, taken) else UNCHANGED)
  }

  private def removeOptions(domains: Array[Long], cell: Int, removed: Long): Int =
    if ((domains(cell) & removed) == 0L) UNCHANGED
    else replaceDomain(domains, cell, domains(cell) & ~removed)

  /** Apply the rule to each number from the first until the last, stopping if it fails. @return the worst thing that happened. */
  private inline def worstStatus(first: Int, until: Int)(inline rule: Int => Int): Int = {
    var status = UNCHANGED
    var number = first
    while (status != FAILED && number < until) { status = Math.max(status, rule(number)); number += 1 }
    status
  }
}
