// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.generation

import com.barrybecker4.common.geometry.ByteLocation
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil._
import com.barrybecker4.puzzle.tantrix.model.HexTiles.TILES
import com.barrybecker4.puzzle.tantrix.model.RotationEnum._
import com.barrybecker4.puzzle.tantrix.model.{TantrixBoard, TilePlacement}
import org.junit.Assert.assertEquals
import org.scalatest.FunSuite

/**
  * @author Barry Becker
  */
class MoveGeneratorSuite extends FunSuite {

  private[generation] var generator: MoveGenerator = _
  private[generation] var board: TantrixBoard = _

  test("MoveGenerationFromTwoOfThreeTilesA") {
    board = place2of3Tiles_OneThenTwo
    generator = new MoveGenerator(board)
    val moves = generator.generateMoves
    assertEquals("Unexpected number of next moves.", 1, moves.size)
    assertEquals("Unexpected first next move.", TilePlacement(TILES.getTile(3), new ByteLocation(22, 20),ANGLE_120), moves.head)
    //assertEquals("Unexpected second next move.",
    //    new TilePlacement(TILES.getTile(3), new ByteLocation(1, 0), Rotation.ANGLE_0), moves.get(1));
  }

  test("MoveGenerationFromTwoOfThreeTilesB") {
    board = place2of3Tiles_OneThenThree
    generator = new MoveGenerator(board)
    val moves = generator.generateMoves
    //System.out.println("moves = " + moves)
    assertEquals("Unexpected number of next moves.", 1, moves.size)
  }

  test("MoveGenerationFromFirstTileOfThree") {
    board = new TantrixBoard(THREE_TILES)
    generator = new MoveGenerator(board)
    val moves = generator.generateMoves
    //System.out.println("moves = " + moves)
    assertEquals("Unexpected number of next moves.", 8, moves.size)
  }
}

