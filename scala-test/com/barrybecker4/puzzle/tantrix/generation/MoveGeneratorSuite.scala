// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.generation

import scala.compiletime.uninitialized
import com.barrybecker4.common.geometry.ByteLocation
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil._
import com.barrybecker4.puzzle.tantrix.generation.MoveGenerator.GrowthMode
import com.barrybecker4.puzzle.tantrix.model.HexTiles.TILES
import com.barrybecker4.puzzle.tantrix.model.Rotation._
import com.barrybecker4.puzzle.tantrix.model.{TantrixBoard, TilePlacement}
import org.junit.Assert.assertEquals
import org.scalatest.funsuite.AnyFunSuite

/**
  * @author Barry Becker
  */
class MoveGeneratorSuite extends AnyFunSuite {

  private[generation] var generator: MoveGenerator = uninitialized
  private[generation] var board: TantrixBoard = uninitialized

  test("MoveGenerationFromTwoOfThreeTilesA") {
    board = place2of3Tiles_OneThenTwo
    generator = new MoveGenerator(board)
    val moves = generator.generateMoves
    assertEquals("Unexpected number of next moves.", 1, moves.size)
    assertEquals("Unexpected first next move.",
      TilePlacement(TILES.getTile(3), new ByteLocation(22, 20),ANGLE_120), moves.head)
    //assertEquals("Unexpected second next move.",
    //    new TilePlacement(TILES.getTile(3), new ByteLocation(1, 0), Rotation.ANGLE_0), moves.get(1));
  }

  test("MoveGenerationFromTwoOfThreeTilesB") {
    board = place2of3Tiles_OneThenThree
    generator = new MoveGenerator(board)
    val moves = generator.generateMoves
    assertEquals("Unexpected number of next moves.", 1, moves.size)
  }

  test("MoveGenerationFromFirstTileOfThree") {
    board = new TantrixBoard(THREE_TILES)
    generator = new MoveGenerator(board)
    val moves = generator.generateMoves
    assertEquals("Unexpected number of next moves.", 8, moves.size)
  }

  test("single ended generation from the first tile of three only places tiles at one location") {
    board = new TantrixBoard(THREE_TILES)
    val allMoves = new MoveGenerator(board).generateMoves
    val moves = new MoveGenerator(board, growth = GrowthMode.ONE_END).generateMoves
    assert(moves.nonEmpty)
    assert(moves.size < allMoves.size)
    assert(moves.forall(allMoves.contains))
    assertEquals("All moves should be at the same location.", 1, moves.map(_.location).distinct.size)
  }

  test("single ended generation continues from the end of the path where the last tile was placed") {
    board = place2of3Tiles_OneThenTwo
    val moves = new MoveGenerator(board, growth = GrowthMode.ONE_END).generateMoves
    assertEquals(new MoveGenerator(board).generateMoves, moves)
  }

  test("pruned moves are a subset of the single ended moves") {
    board = place3of6UnsolvedTiles
    val singleEnded = new MoveGenerator(board, growth = GrowthMode.ONE_END).generateMoves
    val pruned = new MoveGenerator(board, growth = GrowthMode.ONE_END, prune = true).generateMoves
    assert(pruned.forall(singleEnded.contains))
  }

  test("no moves are generated when the loop is closed") {
    val solved = place3SolvedTiles
    assert(new MoveGenerator(solved, growth = GrowthMode.ONE_END, prune = true).generateMoves.isEmpty)
  }

  test("scarcer end generation only places tiles at one of the two open ends, the one with the fewest moves") {
    board = place3of6UnsolvedTiles
    val allMoves = new MoveGenerator(board).generateMoves
    val moves = new MoveGenerator(board, GrowthMode.SCARCER_END).generateMoves
    assert(moves.nonEmpty)
    assert(moves.forall(allMoves.contains))
    assertEquals("All moves should be at the same location.", 1, moves.map(_.location).distinct.size)
    val countsByLocation = allMoves.groupBy(_.location).view.mapValues(_.size)
    assertEquals(countsByLocation.values.min, moves.size)
  }

  test("scarcer end generation has no moves when one of the open ends cannot be filled") {
    // the path cannot be continued from the end where only tile 3 fits, if tile 3 is not available
    board = place3of6UnsolvedTiles
    val withoutTiles = new TantrixBoard(board.tantrix, board.primaryColor, Seq(TILES.getTile(5)), board.numTiles)
    val movesByLocation = new MoveGenerator(withoutTiles).generateMoves.groupBy(_.location)
    val scarcer = new MoveGenerator(withoutTiles, GrowthMode.SCARCER_END).generateMoves
    if (movesByLocation.size < 2) assert(scarcer.isEmpty, "an open end with no moves is a dead end")
    else assert(scarcer.nonEmpty)
  }

  test("scarcer end generation from a single tile only uses one of its open sides") {
    board = new TantrixBoard(THREE_TILES)
    val moves = new MoveGenerator(board, GrowthMode.SCARCER_END).generateMoves
    assert(moves.nonEmpty)
    assertEquals(1, moves.map(_.location).distinct.size)
  }
}
