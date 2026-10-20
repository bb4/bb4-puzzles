// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.shapeassign

object TileAssigner {
  /** The number of options that are tried for the cells of one shape before giving up on it. */
  val DEFAULT_BUDGET = 3000
}

/**
  * Chooses the tile for each cell once the whole shape of the loop is known. It repeatedly picks the cell with the fewest options
  * and tries each one, reducing the options of the other cells after each choice.
  */
class TileAssigner(numCells: Int, propagator: DomainPropagator, matcher: DistinctTileMatcher,
                   budget: Int = TileAssigner.DEFAULT_BUDGET) {

  private var optionsLeftToTry = 0

  /** @param domains the options for every cell, which are not changed
    * @return domains that have just one option for every cell, if there is a way to choose them.
    */
  def assign(domains: Array[Long]): Option[Array[Long]] = {
    optionsLeftToTry = budget
    search(domains)
  }

  private def search(domains: Array[Long]): Option[Array[Long]] = {
    val cell = cellWithFewestOptions(domains)
    if (cell < 0) Some(domains) else tryOptionsOf(domains, cell)
  }

  /** @return the cell that has the fewest options, not counting the ones with only one, or -1 if they all have just one */
  private def cellWithFewestOptions(domains: Array[Long]): Int = {
    val undecided = (1 until numCells).filter(cell => OptionSet.size(domains(cell)) > 1)
    if (undecided.isEmpty) -1 else undecided.minBy(cell => OptionSet.size(domains(cell)))
  }

  /** Try the options of the cell, lowest first, until one works or the budget of options to try is used up. */
  private def tryOptionsOf(domains: Array[Long], cell: Int): Option[Array[Long]] =
    Iterator.iterate(domains(cell))(OptionSet.withoutFirst).takeWhile(_ != 0L)
      .takeWhile(_ => optionsLeftToTry > 0)
      .map(options => tryOption(domains, cell, OptionSet.firstOption(options)))
      .collectFirst { case Some(decided) => decided }

  private def tryOption(domains: Array[Long], cell: Int, option: Int): Option[Array[Long]] = {
    optionsLeftToTry -= 1
    val chosen = domains.clone()
    chosen(cell) = OptionSet.single(option)
    if (propagator.propagate(chosen) && matcher.canAssignDistinctTiles(chosen, numCells - 1)) search(chosen) else None
  }
}
