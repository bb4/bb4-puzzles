// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver

import com.barrybecker4.puzzle.common.solver.{AStarPuzzleSolver, IDAStarPuzzleSolver, SequentialPuzzleSolver}
import com.barrybecker4.puzzle.tantrix.model.{HexTiles, TantrixBoard, TilePlacement}
import com.barrybecker4.puzzle.tantrix.{FixedStartTantrixController, StubTantrixRefreshable, TantrixTstUtil}
import org.scalatest.funsuite.AnyFunSuite

/**
  * [[AStarPuzzleSolver]] uses the controller's heuristic ([[TantrixController.distanceFromGoal]]).
  */
class TantrixAStarSolverSuite extends AnyFunSuite {

  private val ui = new StubTantrixRefreshable

  private def applyMoves(board: TantrixBoard, moves: Seq[TilePlacement]): TantrixBoard =
    moves.foldLeft(board)(_.placeTile(_))

  test("AStar finds one-move completion from two of three tiles") {
    val start = TantrixTstUtil.place2of3Tiles_OneThenTwo
    val controller = new FixedStartTantrixController(ui, start)
    val solver = new AStarPuzzleSolver(controller)
    val moves = solver.solve
    assert(moves.isDefined)
    assert(moves.get.size == 1)
    val end = applyMoves(start, moves.get)
    assert(controller.isGoal(end))
  }

  test("AStar solves three-tile puzzle from initial single-tile board") {
    val tiles = HexTiles.TILES.createOrderedList(3)
    val start = new TantrixBoard(tiles)
    val controller = new FixedStartTantrixController(ui, start)
    val solver = new AStarPuzzleSolver(controller)
    val moves = solver.solve
    assert(moves.isDefined)
    val end = applyMoves(start, moves.get)
    assert(controller.isGoal(end))
    assert(end.unplacedTiles.isEmpty)
  }

  test("AStar completes six-tile puzzle from place3of6 fixture") {
    val start = TantrixTstUtil.place3of6UnsolvedTiles
    val controller = new FixedStartTantrixController(ui, start)
    val solver = new AStarPuzzleSolver(controller)
    val moves = solver.solve
    assert(moves.isDefined, "A* should find a solution using distanceFromGoal heuristic")
    val end = applyMoves(start, moves.get)
    assert(controller.isGoal(end))
    assert(end.unplacedTiles.isEmpty)
  }

  test("IDAStar finds same one-move completion as AStar from two of three tiles") {
    val start = TantrixTstUtil.place2of3Tiles_OneThenTwo
    val ctrl = new FixedStartTantrixController(ui, start)
    val ida = new IDAStarPuzzleSolver(ctrl)
    val moves = ida.solve
    assert(moves.isDefined)
    assert(moves.get.size == 1)
    assert(ctrl.isGoal(applyMoves(start, moves.get)))
  }

  test("AStar returns None when no legal moves from dead-end full board") {
    val start = TantrixTstUtil.place4UnsolvedTiles
    assert(start.unplacedTiles.isEmpty)
    val controller = new FixedStartTantrixController(ui, start)
    assert(controller.legalTransitions(start).isEmpty)
    val solver = new AStarPuzzleSolver(controller)
    assert(solver.solve.isEmpty)
  }

  test("AStar and sequential DFS agree solvable three-tile from single tile") {
    val tiles = HexTiles.TILES.createOrderedList(3)
    val start = new TantrixBoard(tiles)
    val ctrl = new FixedStartTantrixController(ui, start)
    val aStar = new AStarPuzzleSolver(ctrl)
    val dfs = new SequentialPuzzleSolver(new FixedStartTantrixController(new StubTantrixRefreshable, start))
    val aMoves = aStar.solve
    val dMoves = dfs.solve
    assert(aMoves.isDefined && dMoves.isDefined)
    assert(ctrl.isGoal(applyMoves(start, aMoves.get)))
    assert(ctrl.isGoal(applyMoves(start, dMoves.get)))
  }
}
