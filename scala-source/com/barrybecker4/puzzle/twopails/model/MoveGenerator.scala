// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.twopails.model

import com.barrybecker4.puzzle.twopails.model.PourOperation.Action
import com.barrybecker4.puzzle.twopails.model.PourOperation.Action._
import com.barrybecker4.puzzle.twopails.model.PourOperation.Container._

import scala.collection.mutable.ListBuffer


/**
  * Two pails puzzle move generator. Generates valid next moves.
  * There are at most 6 moves which could be generated.
  *
  * @author Barry Becker
  */
class MoveGenerator(var pails: Pails) {
  /**
    * Next moves are all the pour operations that make sense given the current pail fill states.
    * For example, it does not make sense to empty and empty pail.
    * Similarly, you cannot transfer any liquid when both pails are completely full.
    *
    * @return List of all valid tile slides
    */
  def generateMoves: List[PourOperation] = {
    val moves = ListBuffer[PourOperation]()

    for (action <- Action.values) {
      action match {
        case EMPTY =>
          if (pails.fill1 > 0)
            moves.append(new PourOperation(EMPTY, FIRST))
          if (pails.fill2 > 0)
            moves.append(new PourOperation(EMPTY, SECOND))
        case FILL =>
          if (pails.pail1HasRoom)
            moves.append(new PourOperation(FILL, FIRST))
          if (pails.pail2HasRoom)
            moves.append(new PourOperation(FILL, SECOND))
        case TRANSFER =>
          if (pails.fill1 > 0 && pails.pail2HasRoom)
            moves.append(new PourOperation(TRANSFER, FIRST))
          if (pails.fill2 > 0 && pails.pail1HasRoom)
            moves.append(new PourOperation(TRANSFER, SECOND))
      }
    }
    moves.toList
  }
}
