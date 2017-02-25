// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model.board

/**
  * @author Barry Becker
  */
class Cell(value: Int, numValues: Int) {
  
  setOriginalValue(value)

  /** must be a number between 1 and nn  */
  private var currentValue: Int = 0

  /** true if part of the original specification.  */
  var original: Boolean = false
  def isOriginal: Boolean = original

  /** the BigCell to which I belong   */
  private var parentBigCell: BigCell = _
  var rowCells: CellSet = _
  var colCells: CellSet = _
  var peerCells: Seq[Cell] = _

  private val candidates: Candidates = new Candidates(numValues)

  def setParent(parent: BigCell) { parentBigCell = parent }
  def getValue: Int = currentValue
  def isParent(bigCell: BigCell): Boolean = bigCell == parentBigCell


  /**
    * @param value original value
    * @return true if successfully assigned. False if assigning makes an inconsistent state
    */
  def setOriginalValue(value: Int) {
    assert(value >= 0)
    // if set to 0 initially, then it is a value that needs to be filled in.
    original = value > 0
    currentValue = value
    //if (isOriginal) {
    //  setValue(value)
    //} else true
  }

  /**
    * Once the puzzle has started, you can only assign positive values to values of cells.
    * @param value the value to set permanently in the cell (at least until cleared).
    * @return true if successfully assigned. False if assigning makes an inconsistent state
    */
  def setValue(value: Int): Boolean = {
    assert(value > 0)
    currentValue = value
    original = false

    candidates.remove(value)
    for (v <- candidates.elements)
      if (!eliminateFromPeers(v)) return false
    true
  }


  /** from http://norvig.com/sudoku.html
   * Eliminate v from all peer cells. Propagate when values or places <= 2.
   * @return False if a contradiction is detected.
   */
  def eliminateFromPeers(v: Int): Boolean = {
    if (!candidates.contains(v))
      true // already removed
    else {
      candidates.remove(v)
      /*
      if (candidates.isEmpty)
        false  // contradiction
      else if (candidates.size == 1) {
        val uniqueValue = candidates.getFirst
        if (!peerCells.forall(p => p.eliminateFromPeers(uniqueValue)))
          return false
        for (unit <- units) {
          val cellsInUnitThatHaveVAsCandidate = unit.getCellsWithCandValue(v)
          if (cellsInUnitThatHaveVAsCandidate.isEmpty)
            return false
          else if (cellsInUnitThatHaveVAsCandidate.size == 1) {
            if (!cellsInUnitThatHaveVAsCandidate(0).setValue(v))
              return false
          }
        }
        true
      }*/
      true
    }
  }

  /*
    if d not in values[s]:
        return values ## Already eliminated
    values[s] = values[s].replace(d,'')
    ## (1) If a square s is reduced to one value d2, then eliminate d2 from the peers.
    if len(values[s]) == 0:
	    return False ## Contradiction: removed last value
    elif len(values[s]) == 1:
        d2 = values[s]
        if not all(eliminate(values, s2, d2) for s2 in peers[s]):
            return False
    ## (2) If a unit u is reduced to only one place for a value d, then put it there.
    for u in units[s]:
	    dplaces = [s for s in u if d in values[s]]
      	if len(dplaces) == 0:
	         return False ## Contradiction: no place for this value
      	elif len(dplaces) == 1:
	       # d can only be in one place in unit; assign it there
            if not assign(values, dplaces[0], d):
                return False
    return values
   */


  /**
    * Set the value back to unset and add the old value to the list of candidates.
    * The value should only be added back to row/col/bigCell candidates if the value is not already set
    * for respective row/col/bigCell.
    * Clear value should be the inverse of setValue.
    */
  def clearValue() {
    if (currentValue == 0) return
    val value = currentValue
    currentValue = 0
    original = false
    candidates.add(value)
  }

  def getCandidates: Candidates = candidates

  override def equals(other: Any): Boolean = other match {
    case that: Cell => currentValue == that.currentValue
    case _ => false
  }

  override def hashCode: Int = value

  override def toString: String = "Cell value: " + getValue
}
