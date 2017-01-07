// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge.model

import scala.collection.Seq


/**
  * Bridge Puzzle move generator. Generates valid next moves.
  *
  * @author Barry Becker
  */
class MoveGenerator(var board: Bridge) {

  /**
    * Next moves are all people that can move across the bridge..
    * @return List of all valid tile slides
    */
  def generateMoves: Seq[BridgeMove] = if (board.isLightCrossed) createMoves(board.getCrossedPeople, crossing = false)

  else createMoves(board.getUncrossedPeople, crossing = true)

  private def createMoves(people: List[Int], crossing: Boolean) = {
    var moves = List[BridgeMove]()
    val numPeople = people.size
    moves ::= new BridgeMove(List(people.head), crossing)
    if (numPeople > 1) {
      var i = 0
      while (i < numPeople - 1) {
        var j = i + 1
        while (j < numPeople) {
          moves ::= new BridgeMove(List(people(i), people(j)), crossing)
          j += 1; j - 1
        }
        moves ::= new BridgeMove(List(people(i + 1)), crossing)
        i += 1; i - 1
      }
    }
    // Put them in order of fastest to cross first. This speeds A*, but slows sequential a lot.
    // moves.sorted
    moves
  }
}
