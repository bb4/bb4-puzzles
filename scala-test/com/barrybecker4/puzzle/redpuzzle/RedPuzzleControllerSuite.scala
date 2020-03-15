package com.barrybecker4.puzzle.redpuzzle

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.common.i18n.MessageContext
import com.barrybecker4.puzzle.redpuzzle.model.{Direction, OrientedPiece, PieceList, PieceLists}
import org.scalatest.funsuite.AnyFunSuite
import PieceLists.RED_INITIAL_PIECES_9
import scala.util.Random
import com.barrybecker4.common.testsupport.strip


class RedPuzzleControllerSuite extends AnyFunSuite {

  AppContext.injectMessageContext(new MessageContext("com.barrybecker4.puzzle.redpuzzle.ui.message"))
  val controller = new RedPuzzleController(new StubRefreshable)

  test("isGoal when is") {
    assert(controller.isGoal(new PieceList(RED_INITIAL_PIECES_9)))
  }

  test("isGoal when is not") {
    val pieces = RED_INITIAL_PIECES_9.take(3).map(new OrientedPiece(_)).toList
    assert(!controller.isGoal(PieceList(pieces, 9)))
  }

  test("distance from goal when 0") {
    assertResult(0) {controller.distanceFromGoal(new PieceList(RED_INITIAL_PIECES_9)) }
  }

  test("distance from goal when non-0") {
    assertResult(6) {
      val pieces = RED_INITIAL_PIECES_9.take(3).map(new OrientedPiece(_)).toList
      controller.distanceFromGoal(PieceList(pieces, 9))
    }
  }

  test("legal transitions") {
    val pieces = RED_INITIAL_PIECES_9.take(3).map(new OrientedPiece(_)).toList
    val position = PieceList(pieces, 9)
    assertResult(3) {
      controller.legalTransitions(position).length
    }
  }

  test("legal transitions after shuffle") {
    val rnd = new Random(1)
    val pieces = rnd.shuffle(RED_INITIAL_PIECES_9.take(3).toList).map(new OrientedPiece(_))
    val position = PieceList(pieces, 9)
    assertResult(4) {
      controller.legalTransitions(position).length
    }
  }

  test("transition when piece fits") {
    val pieces = RED_INITIAL_PIECES_9.take(3).map(new OrientedPiece(_)).toList
    val position = PieceList(pieces, 9)
    assertResult(strip("""PieceList: (4 pieces)
       |Piece 1 (orientation=TOP): TOP:outy Suit(S);RIGHT:outy Suit(D);BOTTOM:inny Suit(H);LEFT:inny Suit(D);
       |Piece 2 (orientation=TOP): TOP:outy Suit(C);RIGHT:outy Suit(H);BOTTOM:inny Suit(D);LEFT:inny Suit(C);
       |Piece 3 (orientation=TOP): TOP:outy Suit(H);RIGHT:outy Suit(S);BOTTOM:inny Suit(S);LEFT:inny Suit(C);
       |Piece 6 (orientation=TOP): TOP:outy Suit(H);RIGHT:outy Suit(D);BOTTOM:inny Suit(D);LEFT:inny Suit(H);
       |""")) {
      controller.transition(position, new OrientedPiece(RED_INITIAL_PIECES_9(5))).toString
    }
  }

  test("transition when piece does not fit") {
    val pieces = RED_INITIAL_PIECES_9.take(3).map(new OrientedPiece(_)).toList
    val position = PieceList(pieces, 9)

    assertThrows[AssertionError] {
      controller.transition(position, OrientedPiece(RED_INITIAL_PIECES_9(5), Direction.RIGHT)).toString
    }
  }
}
