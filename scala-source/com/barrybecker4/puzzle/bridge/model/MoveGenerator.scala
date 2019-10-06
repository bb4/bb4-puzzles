// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge.model


/**
  * Bridge Puzzle move generator. Generates valid next moves.
  * @author Barry Becker
  */
class MoveGenerator(var board: Bridge) {

  /** @return List of next moves are all people that can move across the bridge. */
  def generateMoves: Seq[BridgeMove] = if (board.lightCrossed) createMoves(board.crossed, crossing = false)

  else createMoves(board.uncrossed, crossing = true)

  private def createMoves(people: List[Int], crossing: Boolean): Seq[BridgeMove] = {
    var moves = List[BridgeMove]()
    val numPeople = people.size
    moves ::= BridgeMove(List(people.head), crossing)
    if (numPeople > 1) {
      for (i <- 0 until numPeople - 1) {
        for (j <- (i + 1) until numPeople)
          moves +:= BridgeMove(List(people(i), people(j)), crossing)
        moves +:= BridgeMove(List(people(i + 1)), crossing)
      }
    }
    // Putting them in order of fastest to cross first. This speeds A*, but slows sequential.
    //moves.sorted
    moves
  }
}
