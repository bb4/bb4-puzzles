// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix

import com.barrybecker4.math.MathUtil
import com.barrybecker4.search.Refreshable
import com.barrybecker4.puzzle.common.ui.AbstractPuzzleController

import java.util.concurrent.ConcurrentHashMap
import com.barrybecker4.puzzle.tantrix.generation.MoveGenerator
import com.barrybecker4.puzzle.tantrix.generation.MoveGenerator.GrowthMode
import com.barrybecker4.puzzle.tantrix.model.HexTiles.TILES
import com.barrybecker4.puzzle.tantrix.model.{TantrixBoard, TilePlacement}
import com.barrybecker4.puzzle.tantrix.solver.Algorithm.SIMPLE_SEQUENTIAL
import com.barrybecker4.puzzle.tantrix.solver.path.{PathEvaluator, TantrixPath}


/**
  * The controller allows the solver to do its thing by providing the PuzzleController api.
  *
  * The generic solvers (sequential and concurrent) expect the first class param
  * to represent the state of a board, and the TilePlacement (second param)
  * to represent a move. The way a move is applied is simply to add the piece to the
  * end of the tantrix on the board.
  *
  */
object TantrixController {
  private val MIN_NUM_TILES = 3
}

class TantrixController(ui: Refreshable[TantrixBoard, TilePlacement])
  extends AbstractPuzzleController[TantrixBoard, TilePlacement](ui) {

  algorithm = SIMPLE_SEQUENTIAL
  private var numTiles = TantrixController.MIN_NUM_TILES
  private val evaluator = new PathEvaluator

  /** Memoize PathEvaluator-based heuristic per board state for the current solve (thread-safe for concurrent A*). */
  private val distanceFromGoalCache = new ConcurrentHashMap[TantrixBoard, Integer]

  override def startSolving(): Unit = {
    distanceFromGoalCache.clear()
    super.startSolving()
  }

  def setNumTiles(numTiles: Int): Unit = {
    this.numTiles = numTiles
  }

  def initialState: TantrixBoard = {
    new TantrixBoard(TILES.createRandomList(numTiles))
  }

  /**
    * @return true if there is a loop of the primary color and all the
    *         secondary color path connections match.
    */
  def isGoal(position: TantrixBoard): Boolean = position.isSolved

  /** The path is only extended at the end with the fewest choices, and moves that cannot lead to a solution are left out.
    * This finds the same solutions as trying every move at either end, but searches far fewer states.
    */
  def legalTransitions(position: TantrixBoard): Seq[TilePlacement] =
    new MoveGenerator(position, growthMode, prune = true).generateMoves

  /** Which end of the path to add tiles to. Every mode finds the same solutions. */
  protected def growthMode: GrowthMode = GrowthMode.SCARCER_END

  def transition(position: TantrixBoard, move: TilePlacement): TantrixBoard = position.placeTile(move)

  /**
    * Informed estimate for [[com.barrybecker4.puzzle.common.solver.AStarPuzzleSolver]] and related search.
    * Uses [[com.barrybecker4.puzzle.tantrix.solver.path.PathEvaluator]] on the primary path derived from
    * the board so ordering matches puzzle progress (loops, consistency, compactness), not just tile count
    * and bbox spread.
    */
  override def distanceFromGoal(position: TantrixBoard): Int =
    distanceFromGoalCache
      .computeIfAbsent(
        position,
        p => {
          val path = new TantrixPath(p.tantrix, p.primaryColor, p.numTiles, MathUtil.RANDOM)
          val fitness = evaluator.evaluateFitness(path)
          Integer.valueOf((10.0 * Math.max(0, fitness)).toInt)
        }
      )
      .intValue()
}
