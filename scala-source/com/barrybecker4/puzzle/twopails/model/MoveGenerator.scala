// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.twopails.model

import com.barrybecker4.puzzle.twopails.model.PourOperation.Action
import com.barrybecker4.puzzle.twopails.model.PourOperation.Action._
import com.barrybecker4.puzzle.twopails.model.PourOperation.Container._


/**
  * Two pails puzzle move generator. Generates valid next moves.
  * There are at most 6 moves which could be generated.
  *
  * @author Barry Becker
  */
class MoveGenerator(private val pails: Pails) {

  /** Next moves are all the pour operations that make sense given the current pail fill states.
    * For example, it does not make sense to empty an empty pail.
    * Similarly, you cannot transfer any liquid when both pails are completely full.
    *
    * @return List of all valid tile slides
    */
  def generateMoves: List[PourOperation] =
    Action.values.toList.flatMap {
      case EMPTY =>
        List(
          Option.when(pails.fill1 > 0)(PourOperation(EMPTY, FIRST)),
          Option.when(pails.fill2 > 0)(PourOperation(EMPTY, SECOND))).flatten
      case FILL =>
        List(
          Option.when(pails.pail1HasRoom)(PourOperation(FILL, FIRST)),
          Option.when(pails.pail2HasRoom)(PourOperation(FILL, SECOND))).flatten
      case TRANSFER =>
        List(
          Option.when(pails.fill1 > 0 && pails.pail2HasRoom)(PourOperation(TRANSFER, FIRST)),
          Option.when(pails.fill2 > 0 && pails.pail1HasRoom)(PourOperation(TRANSFER, SECOND))).flatten
    }
}
