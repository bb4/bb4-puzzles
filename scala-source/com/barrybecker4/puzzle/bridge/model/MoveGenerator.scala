// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge.model


/**
  * Bridge Puzzle move generator. Generates valid next moves.
  * @author Barry Becker
  */
class MoveGenerator(val board: Bridge) {

  /** @return List of next moves are all people that can move across the bridge. */
  def generateMoves: Seq[BridgeMove] =
    if (board.lightCrossed) createMoves(board.crossed, crossing = false)
    else createMoves(board.uncrossed, crossing = true)

  private def createMoves(people: List[Int], crossing: Boolean): Seq[BridgeMove] = {
    if (people.isEmpty) return Nil
    val singles = people.map(p => BridgeMove(List(p), crossing))
    val pairs = people.combinations(2).map(c => BridgeMove(c.toList, crossing)).toSeq
    // Sort by crossing time, then by slowest-first tie-break to match legacy move ordering (A* ordering).
    (singles ++ pairs).sortBy(m => (m.cost, -m.people.min))
  }
}
