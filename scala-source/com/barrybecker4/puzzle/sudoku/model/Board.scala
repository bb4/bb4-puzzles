// Copyright by Barry G. Becker, 2017 - 2019. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model

/**
  * The Board describes the physical layout of the puzzle.
  * The number of Cells in the board is n^2 * n^2, but there are n * n big cells.
  * This implementation is based on Peter Norvig's algorithm - http://norvig.com/sudoku.html
  * @param initialData array giving initially set values
  * @author Barry Becker
  */
class Board(val initialData: Array[Array[Cell]]) {

  val edgeLength: Int = initialData.length
  val baseSize: Int = Math.sqrt(edgeLength).toInt
  private[model] val comps = COMPONENTS(baseSize)
  private[model] var valuesMap: ValueMap = _
  val numCells: Int = edgeLength * edgeLength
  reset()

  def this(initial: Array[Array[Int]]) = this(initial.map(_.map(v => new Cell(v, v))))
  def this(baseSize: Int) = this(Array.ofDim[Int](baseSize * baseSize, baseSize * baseSize))
  def copy() = new Board(initialDataCopy)

  private def initialDataCopy =
    initialData.map(_.map(c => new Cell(c.originalValue, c.proposedValue)))

  def getCell(location: (Int, Int)): Cell = initialData(location._1 - 1)(location._2 - 1)

  def reset(): Unit = {
    valuesMap = comps.initialValueMap
  }

  /** @return true if the board has been successfully solved. Solved if all candidates a single value. */
  def isSolved: Boolean = valuesMap.values.forall(_.size == 1)
  def getValues(location: (Int, Int)): Seq[Int] = valuesMap(location).toList

  def getValue(location: (Int, Int)): Int = {
    valuesMap(location) match {
      case singleValue if singleValue.size == 1 => singleValue.head
      case notSingle => 0
    }
  }

  /** Sets the original value, and update valuesMap accordingly */
  def setOriginalValue(location: (Int, Int), v: Int): Unit =
    initialData(location._1 - 1)(location._2 - 1) = new Cell(v, v)

  /** Remove specified value if it does not prevent the puzzle from being solved using just base consistency check. */
  def removeValueIfPossible(location: (Int, Int), refresh: Option[() => Unit] = None): Unit = {
    val initial = initialDataCopy
    initial(location._1 - 1)(location._2 - 1) = new Cell(0, 0)

    val b = new Board(initial)
    b.updateFromInitialData()

    if (b.isSolved) {
      initialData(location._1 - 1)(location._2 - 1) = new Cell(0, 0)
      this.valuesMap = b.valuesMap
      if (refresh.isDefined) refresh.get()
    }
  }

  /** return number of iterations it took to solve, or None if not solved */
  def solve(refresh: Option[() => Unit] = None): Option[Int] = Solver(this, refresh).solve()

  def setSolvedValues(): Unit = {
    for ((s, values) <- valuesMap) {
      if (values.size == 1) initialData(s._1 - 1)(s._2 - 1).proposedValue = values.head
    }
  }

  /** @return true if updated successfully. False if there was an inconsistency. */
  def updateFromInitialData(): Boolean = {
    for (r <- comps.digits; c <- comps.digits; v = initialData(r - 1)(c - 1).originalValue; if v > 0)
      assign(valuesMap, (r, c), v) match {
        case Some(values) => valuesMap = values
        case None => return false
      }
    true
  }

  def doRefresh(refresh: Option[() => Unit]): Unit = {
    if (refresh.isDefined) {
      setSolvedValues()
      refresh.get()
    }
  }

  /** Assign a value, d, to a square if possible.
    * Eliminate all the other values (except d) from values[s] and propagate.
    * @return Some(values), except return None if a contradiction is detected.
    */
  def assign(values: ValueMap, s: (Int, Int), d: Int): Option[ValueMap] = {
    val otherValues: Set[Int] = values(s) - d
    var newValues: Option[ValueMap] = Some(values)
    for (d2 <- otherValues) {
      newValues = eliminate(newValues.get, s, d2)
      if (newValues.isEmpty) return None
    }
    newValues
  }

  /** Eliminate d from values[s]; propagate when values or places == 1.
    * @return Some(values), except return None if a contradiction is detected.
    */
  private def eliminate(values: ValueMap, s: (Int, Int), d: Int): Option[ValueMap] = {

    if (!values(s).contains(d)) return Some(values)
    var newValues = values.updated(s, values(s) - d)
    // If a square s is reduced to one value, d2, then eliminate d2 from the peers.
    if (newValues(s).isEmpty) return None // Contradiction
    // val candidates = newValues(s)
    else if (newValues(s).size == 1) {
      val d2 = newValues(s).head
      for (s2 <- comps.peers(s)) {
        eliminate(newValues, s2, d2) match {   // Recursive call
          case Some(vals) => newValues = vals
          case None => return None // Contradiction
        }
      }
    }
    // If a unit u is reduced to only one place for a value d, then put it there.
    for (u <- comps.units(s)) {
      val dplaces = for (s <- u; if newValues(s).contains(d)) yield s
      if (dplaces.isEmpty) return None // Contradiction
      if (dplaces.size == 1) {
        // d can only be in one place in unit; assign it there
        assign(newValues, dplaces.head, d) match {
          case Some(vals) => newValues = vals
          case None => return None  // Contradiction
        }
      }
    }
    Some(newValues)
  }

  override def toString: String = BoardSerializer(this).serialize
  def toDebugString: String = BoardSerializer(this).debugSerialize
}
