// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.twopails.model

import com.barrybecker4.puzzle.twopails.model.PourOperation.Action.TRANSFER
import org.junit.Assert.assertTrue
import org.scalacheck.{Gen, Prop, Test as ScalaCheckTest}
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

/**
  * Random legal-move walks stay within pail capacities.
  */
class PailsScalaCheckSuite extends AnyFunSuite {

  private val paramsGen: Gen[PailParams] =
    for {
      a <- Gen.choose(1, 40)
      b <- Gen.choose(1, 40)
      t <- Gen.choose(0, math.max(a, b))
    } yield PailParams(a, b, t)

  private def walk(params: PailParams, steps: Int, seed: Long): Pails = {
    val r = new Random(seed)
    var state = Pails(0, 0, params)
    var i = 0
    while i < steps do
      val moves = MoveGenerator(state).generateMoves
      if moves.isEmpty then return state
      state = state.applyMove(moves(r.nextInt(moves.size)), undo = false)
      i += 1
    state
  }

  test("ScalaCheck: fills stay within capacity after random legal moves") {
    val p = Prop.forAll(paramsGen, Gen.choose(0, 100), Gen.long) { (params, steps, seed) =>
      val s = walk(params, steps, seed)
      s.fill1 >= 0 && s.fill2 >= 0 &&
        s.fill1 <= params.pail1Size && s.fill2 <= params.pail2Size
    }
    assertTrue(ScalaCheckTest.check(ScalaCheckTest.Parameters.default.withMinSuccessfulTests(50), p).passed)
  }

  test("ScalaCheck: TRANSFER-only sequence conserves total liquid") {
    val p = Prop.forAll(paramsGen, Gen.long) { (params, seed) =>
      val r = new Random(seed)
      var state = Pails(params.pail1Size, params.pail2Size, params)
      val refTotal = state.fill1 + state.fill2
      var conserved = true
      for (_ <- 0 until 50 if conserved) {
        val transfers = MoveGenerator(state).generateMoves.filter(_.action == TRANSFER)
        if transfers.nonEmpty then
          val op = transfers(r.nextInt(transfers.size))
          state = state.applyMove(op, undo = false)
          conserved = state.fill1 + state.fill2 == refTotal
      }
      conserved
    }
    assertTrue(ScalaCheckTest.check(ScalaCheckTest.Parameters.default.withMinSuccessfulTests(30), p).passed)
  }
}
