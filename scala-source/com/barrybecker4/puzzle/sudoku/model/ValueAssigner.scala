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
  def assign(location: Location, value: Int, valuesMap: ValueMap): Option[ValueMap] = {
    val otherValues: Set[Int] = valuesMap(location) - value
    var newValues: Option[ValueMap] = Some(valuesMap)
    for (v <- otherValues) {
      newValues = eliminate(v, location, newValues.get)
      if (newValues.isEmpty) return None
    }
    newValues
  }

  /** Eliminate value from specified location. Propagate when values or places == 1.
    * @return Some(valueMap), or None if a contradiction is detected.
    */
  private def eliminate(value: Int, location: (Int, Int), valuesMap: ValueMap): Option[ValueMap] = {

    if (!valuesMap(location).contains(value))
      return Some(valuesMap) // already removed, no nothing
    var newValuesMap = valuesMap.updated(location, valuesMap(location) - value)

    val candidates = newValuesMap(location)

    if (candidates.isEmpty)
      return None // Contradiction
    else if (candidates.size == 1) {
      // If a location is reduced to one value, then eliminate it from its peers.
      val v = candidates.head
      //println(s"reduced to $v at $location, so eliminating $v from all peers")
      for (loc <- comps.peers(location)) {
        //println(s"eliminating $v from $loc")
        eliminate(v, loc, newValuesMap) match {   // Recursive call
          case Some(valsMap) => newValuesMap = valsMap
          case None => return None // Contradiction
        }
      }
    }
    // If a unit is reduced to only one possible place for value, then put it there and recurse.
    for (unit <- comps.units(location)) {
      val possibleLocs = for (loc <- unit; if newValuesMap(loc).contains(value)) yield loc
      //println(s"possible locs = $possibleLocs  are in unit: $unit")

      if (possibleLocs.isEmpty)
        return None // it can't go anywhere - a contradiction
      if (possibleLocs.size == 1) {
        // value can only be in one place in the unit; assign it there.
        assign(possibleLocs.head, value, newValuesMap) match { // recursive call
          case Some(valsMap) => newValuesMap = valsMap
          case None => return None  // Contradiction
        }
      }
    }
    Some(newValuesMap)
  }
}
