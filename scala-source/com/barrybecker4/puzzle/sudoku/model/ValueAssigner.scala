package com.barrybecker4.puzzle.sudoku.model

/**
  * The fundamental operation is trying to assign a value to a position and eliminate that value from
  * all possible peers using constraint propagation as described in https://norvig.com/sudoku.html
  *
  * @param comps the board components appropriate for this size puzzle
  */
case class ValueAssigner(comps: BoardComponents) {

  /** Assign a value to a location if possible.
    * Eliminate all the other values (except value) from values[location] and propagate.
    * @param location the location to assign the value
    * @param value the value to assign to specified location
    * @param valuesMap a valid initial state for the map
    * @return Some(values), or None if the assignment cannot be done legally (a contradiction is detected).
    */
  def assign(location: Location, value: Int, valuesMap: ValuesMap): Option[ValuesMap] = {
    val otherValues: Set[Int] = valuesMap(location) - value
    otherValues.foldLeft(Option(valuesMap)) { (acc, v) =>
      acc.flatMap(m => eliminate(v, location, m))
    }
  }

  /** Eliminate value from specified location. Propagate when values or places == 1.
    * @return Some(valueMap), or None if a contradiction is detected.
    */
  private def eliminate(value: Int, location: Location, valuesMap: ValuesMap): Option[ValuesMap] = {

    if (!valuesMap(location).contains(value))
      return Some(valuesMap) // already removed, do nothing
    var newValuesMap = valuesMap.updated(location, valuesMap(location) - value)

    val candidates = newValuesMap(location)

    if (candidates.isEmpty)
      return None // Contradiction

    else if (candidates.size == 1) {
      propagateSingletonToPeers(candidates.head, location, newValuesMap) match {
        case Some(m) => newValuesMap = m
        case None => return None
      }
    }

    eliminateFromUnits(value, location, newValuesMap)
  }

  private def propagateSingletonToPeers(singleValue: Int, location: Location, valuesMap: ValuesMap): Option[ValuesMap] =
    comps.peers(location).foldLeft(Option(valuesMap)) { (acc, loc) =>
      acc.flatMap(m => eliminate(singleValue, loc, m))
    }

  /** For each unit containing `location`, if `value` can appear in only one square, assign it there. */
  private def eliminateFromUnits(value: Int, location: Location, valuesMap: ValuesMap): Option[ValuesMap] =
    comps.units(location).foldLeft(Option(valuesMap)) { (acc, unit) =>
      acc.flatMap { newValuesMap =>
        val possibleLocs = unit.filter(loc => newValuesMap(loc).contains(value))
        if (possibleLocs.isEmpty) None // it can't go anywhere - a contradiction
        else if (possibleLocs.size == 1) assign(possibleLocs.head, value, newValuesMap)
        else Some(newValuesMap)
      }
    }
}
