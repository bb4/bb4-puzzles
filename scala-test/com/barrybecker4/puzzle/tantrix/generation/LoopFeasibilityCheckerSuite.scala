// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.generation

import com.barrybecker4.puzzle.tantrix.generation.MoveGenerator.GrowthMode
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil.*
import com.barrybecker4.puzzle.tantrix.model.HexTiles.TILES
import com.barrybecker4.puzzle.tantrix.model.{PathColor, TantrixBoard}
import com.barrybecker4.puzzle.tantrix.SolutionEnumerator
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

/**
  * The checker may only rule out states that cannot reach a solution. That is verified by comparing the solutions
  * found with and without it.
  */
class LoopFeasibilityCheckerSuite extends AnyFunSuite {

  private def legacyMoves(board: TantrixBoard) = new MoveGenerator(board).generateMoves
  private def singleEndedMoves(board: TantrixBoard) = new MoveGenerator(board, GrowthMode.ONE_END).generateMoves
  private def scarcerEndMoves(board: TantrixBoard) = new MoveGenerator(board, GrowthMode.SCARCER_END).generateMoves
  private def prunedScarcerEndMoves(board: TantrixBoard) =
    new MoveGenerator(board, GrowthMode.SCARCER_END, prune = true).generateMoves
  private def prunedMoves(board: TantrixBoard) =
    new MoveGenerator(board, GrowthMode.ONE_END, prune = true).generateMoves

  private def shuffledStart(numTiles: Int, seed: Int) =
    new TantrixBoard(new Random(seed).shuffle(TILES.createOrderedList(numTiles)))

  test("a path turns by the amount the tile curves, and in the opposite direction when entering from the other side") {
    assertResult(0)(LoopFeasibilityChecker.turn(0, 3))  // straight
    assertResult(0)(LoopFeasibilityChecker.turn(4, 1))
    assertResult(1)(LoopFeasibilityChecker.turn(0, 4))  // wide curve
    assertResult(-1)(LoopFeasibilityChecker.turn(4, 0))
    assertResult(2)(LoopFeasibilityChecker.turn(0, 5))  // tight curve
    assertResult(-2)(LoopFeasibilityChecker.turn(5, 0))
    assertResult(-2)(LoopFeasibilityChecker.turn(0, 1))
    assertResult(2)(LoopFeasibilityChecker.turn(1, 0))
    assertResult(2)(LoopFeasibilityChecker.turn(5, 4))  // the sides wrap around
  }

  test("the amount a tile turns does not depend on which primary color is used, only on how the sides are spaced") {
    // tile 1 has yellow on the last two sides (tight curve), blue on sides 1 and 3 (wide curve) and red on 0 and 2.
    val tile = TILES.getTile(1)
    assertResult(2)(LoopFeasibilityChecker.turnMagnitude(tile, PathColor.YELLOW))
    assertResult(1)(LoopFeasibilityChecker.turnMagnitude(tile, PathColor.BLUE))
    assertResult(1)(LoopFeasibilityChecker.turnMagnitude(tile, PathColor.RED))
    // tile 6 has the blue path on sides 2 and 4, yellow on 0 and 3 (straight), and red on 1 and 5
    assertResult(0)(LoopFeasibilityChecker.turnMagnitude(TILES.getTile(6), PathColor.YELLOW))
  }

  test("a solved board is feasible, and its tiles have enough turn to go around in a circle") {
    val solved = place7SolvedTiles
    assert(solved.isSolved)
    val magnitudes = solved.tantrix.tiles.toSeq.map(t => LoopFeasibilityChecker.turnMagnitude(t.tile, solved.primaryColor))
    assert(magnitudes.sum >= 6, "a loop needs at least one full circle of turns, which is 6 steps of 60 degrees")
    assert(new LoopFeasibilityChecker(solved).isFeasible)
  }

  test("a loop that is closed while there are tiles left is not feasible, and neither is an unclosed path without tiles left") {
    val solved = place7SolvedTiles
    val extra = TILES.getTile(8)
    val closedWithLeftover = new TantrixBoard(solved.tantrix, solved.primaryColor, Seq(extra), solved.numTiles + 1)
    assert(!new LoopFeasibilityChecker(closedWithLeftover).isFeasible)
    assert(!new LoopFeasibilityChecker(place4UnsolvedTiles).isFeasible)
  }

  test("pruning finds exactly the same solutions as searching every move at either end of the path") {
    for (numTiles <- 3 to 6; seed <- 1 to 5) {
      val start = shuffledStart(numTiles, seed)
      val legacy = new SolutionEnumerator(legacyMoves).solutions(start)
      val singleEnded = new SolutionEnumerator(singleEndedMoves).solutions(start)
      val pruned = new SolutionEnumerator(prunedMoves).solutions(start)
      val scarcerEnd = new SolutionEnumerator(scarcerEndMoves).solutions(start)
      val prunedScarcerEnd = new SolutionEnumerator(prunedScarcerEndMoves).solutions(start)
      assert(legacy.nonEmpty, s"expected solutions for $numTiles tiles with seed $seed")
      assertResult(legacy, s"single ended solutions for $numTiles tiles with seed $seed")(singleEnded)
      assertResult(legacy, s"pruned solutions for $numTiles tiles with seed $seed")(pruned)
      assertResult(legacy, s"scarcer end solutions for $numTiles tiles with seed $seed")(scarcerEnd)
      assertResult(legacy, s"pruned scarcer end solutions for $numTiles tiles with seed $seed")(prunedScarcerEnd)
    }
  }

  test("pruning finds exactly the same solutions as single ended search for larger puzzles") {
    for (numTiles <- Seq(7, 8); seed <- 1 to 3) {
      val start = shuffledStart(numTiles, seed)
      val singleEnded = new SolutionEnumerator(singleEndedMoves).solutions(start)
      val pruned = new SolutionEnumerator(prunedMoves).solutions(start)
      val prunedScarcerEnd = new SolutionEnumerator(prunedScarcerEndMoves).solutions(start)
      assert(singleEnded.nonEmpty, s"expected solutions for $numTiles tiles with seed $seed")
      assertResult(singleEnded, s"pruned solutions for $numTiles tiles with seed $seed")(pruned)
      assertResult(singleEnded, s"pruned scarcer end solutions for $numTiles tiles with seed $seed")(prunedScarcerEnd)
    }
  }

  test("every state on the way to a solution is feasible") {
    for (seed <- 1 to 3) {
      val start = shuffledStart(7, seed)
      val solutions = new SolutionEnumerator(singleEndedMoves).solutions(start)
      assert(solutions.nonEmpty)
      // Follow the unpruned moves to each solution; any state with a solution below it must pass.
      def hasSolutionBelow(board: TantrixBoard): Boolean =
        solutions.contains(board) || singleEndedMoves(board).exists(m => hasSolutionBelow(board.placeTile(m)))
      def check(board: TantrixBoard): Unit =
        if (hasSolutionBelow(board)) {
          assert(new LoopFeasibilityChecker(board).isFeasible, s"Solvable state was ruled out:\n$board")
          singleEndedMoves(board).foreach(m => check(board.placeTile(m)))
        }
      singleEndedMoves(start).foreach(m => check(start.placeTile(m)))
    }
  }

  test("pruning greatly reduces the number of states that need to be searched") {
    val start = shuffledStart(8, 1)
    val singleEnded = new SolutionEnumerator(singleEndedMoves)
    val pruned = new SolutionEnumerator(prunedMoves)
    singleEnded.solutions(start)
    pruned.solutions(start)
    assert(pruned.numStatesSearched * 10 < singleEnded.numStatesSearched,
      s"pruned=${pruned.numStatesSearched} single ended=${singleEnded.numStatesSearched}")
  }

  test("the ends of the path found incrementally after a move are the same as finding them again from scratch") {
    var numMoves = 0
    for (numTiles <- Seq(5, 8, 11); seed <- 1 to 3) {
      var board = shuffledStart(numTiles, seed)
      val rnd = new Random(seed)
      var moves = new MoveGenerator(board).generateMoves
      while (moves.nonEmpty) {
        val endsBefore = LoopFeasibilityChecker.findOpenEnds(board)
        for (move <- moves) {
          val incremental = LoopFeasibilityChecker.endsAfterPlacing(board, endsBefore, move)
          val fromScratch = LoopFeasibilityChecker.findOpenEnds(board.placeTile(move))
          assertResult(fromScratch.toSet, s"ends after $move on\n$board")(incremental.toSet)
          assertResult(fromScratch.size)(incremental.size)
          numMoves += 1
        }
        board = board.placeTile(moves(rnd.nextInt(moves.size)))
        moves = new MoveGenerator(board).generateMoves
      }
    }
    assert(numMoves > 200, "should check a lot of moves, but was " + numMoves)
  }

  test("the feasibility is the same whether or not the ends of the path are provided") {
    for (seed <- 1 to 3) {
      var board = shuffledStart(9, seed)
      val rnd = new Random(seed)
      var moves = new MoveGenerator(board).generateMoves
      while (moves.nonEmpty) {
        for (move <- moves) {
          val next = board.placeTile(move)
          assertResult(new LoopFeasibilityChecker(next).isFeasible)(
            new LoopFeasibilityChecker(next, LoopFeasibilityChecker.endsAfterPlacing(board,
              LoopFeasibilityChecker.findOpenEnds(board), move)).isFeasible)
        }
        board = board.placeTile(moves(rnd.nextInt(moves.size)))
        moves = new MoveGenerator(board).generateMoves
      }
    }
  }

  test("the turns of the path found incrementally after a move are the same as walking the path again") {
    var numChecked = 0
    for (numTiles <- Seq(6, 9, 12); seed <- 1 to 4) {
      var board = shuffledStart(numTiles, seed)
      val rnd = new Random(seed)
      var moves = new MoveGenerator(board).generateMoves
      while (moves.nonEmpty) {
        val endsBefore = LoopFeasibilityChecker.findOpenEnds(board)
        val turnsBefore = LoopFeasibilityChecker.turnsOfPath(board, endsBefore)
        for (move <- moves; before <- turnsBefore) {
          val next = board.placeTile(move)
          val nextEnds = LoopFeasibilityChecker.endsAfterPlacing(board, endsBefore, move)
          val expected = LoopFeasibilityChecker.turnsOfPath(next, nextEnds)
          val incremental = LoopFeasibilityChecker.turnsAfterPlacing(board, endsBefore, before, move)
          // A closed loop has no ends to walk from, and the checker does not use the turns of one.
          if (nextEnds.size == 2) {
            // The sign depends on which way the path is walked, which does not matter.
            assertResult(expected.map(Math.abs), s"turns after $move on\n$board")(incremental.map(Math.abs))
            numChecked += 1
          }
        }
        board = board.placeTile(moves(rnd.nextInt(moves.size)))
        moves = new MoveGenerator(board).generateMoves
      }
    }
    assert(numChecked > 200, "should check a lot of moves, but was " + numChecked)
  }

  test("the feasibility is the same whether or not the turns of the path are provided") {
    for (seed <- 1 to 4) {
      var board = shuffledStart(10, seed)
      val rnd = new Random(seed)
      var moves = new MoveGenerator(board).generateMoves
      while (moves.nonEmpty) {
        val endsBefore = LoopFeasibilityChecker.findOpenEnds(board)
        val turnsBefore = LoopFeasibilityChecker.turnsOfPath(board, endsBefore)
        for (move <- moves) {
          val next = board.placeTile(move)
          val nextEnds = LoopFeasibilityChecker.endsAfterPlacing(board, endsBefore, move)
          val turns = turnsBefore.flatMap(LoopFeasibilityChecker.turnsAfterPlacing(board, endsBefore, _, move))
          assertResult(new LoopFeasibilityChecker(next, nextEnds).isFeasible, s"$move on\n$board")(
            new LoopFeasibilityChecker(next, nextEnds, turns).isFeasible)
        }
        board = board.placeTile(moves(rnd.nextInt(moves.size)))
        moves = new MoveGenerator(board).generateMoves
      }
    }
  }

  test("pruned moves are those that pass the full check, even when the check of the empty spaces is skipped") {
    var numMoves = 0
    for (numTiles <- Seq(8, 12, 16); seed <- 1 to 4; growth <- GrowthMode.values) {
      var board = shuffledStart(numTiles, seed)
      val rnd = new Random(seed)
      var moves = new MoveGenerator(board, growth).generateMoves
      while (moves.nonEmpty) {
        val expected = moves.filter(move => new LoopFeasibilityChecker(board.placeTile(move)).isFeasible)
        assertResult(expected.toSet, s"pruned moves of $growth on\n$board")(
          new MoveGenerator(board, growth, prune = true).generateMoves.toSet)
        numMoves += moves.size
        board = board.placeTile(moves(rnd.nextInt(moves.size)))
        moves = new MoveGenerator(board, growth).generateMoves
      }
    }
    assert(numMoves > 500, "should check a lot of moves, but was " + numMoves)
  }

  test("filling a cell keeps the empty space together if the empty cells around it are in one run") {
    // a single tile has exits in two directions, and the cell next to it has the tile on one side only
    val board = shuffledStart(3, 1)
    val ends = LoopFeasibilityChecker.findOpenEnds(board)
    assert(ends.nonEmpty)
    val cell = com.barrybecker4.puzzle.tantrix.model.HexUtil.getNeighborLocation(ends.head.placement.location, ends.head.exits.head)
    assert(LoopFeasibilityChecker.keepsEmptySpaceTogether(board, cell))
  }
}
