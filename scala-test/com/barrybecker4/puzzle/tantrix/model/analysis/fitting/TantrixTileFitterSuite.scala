/*
 * // Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
 */

package com.barrybecker4.puzzle.tantrix.model.analysis.fitting

import scala.compiletime.uninitialized
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil._
import com.barrybecker4.puzzle.tantrix.model._
import org.scalatest.funsuite.AnyFunSuite


/**
  * @author Barry Becker
  */
class TantrixTileFitterSuite extends AnyFunSuite  {
  
  private val TILES = new HexTiles
  /** instance under test */
  private var fitter: TantrixTileFitter = uninitialized
  private var tantrix: Tantrix = uninitialized

  /**
    * Here we ask if there are fits where three should be possible.
    * 1
    * (3)   2
    */
  test("FitOnTwoWhereOnePossible") {
    tantrix = place2of3Tiles_OneThenTwo.tantrix
    fitter = new TantrixTileFitter(tantrix, PathColor.YELLOW)
    val placements = fitter.getFittingPlacements(TILES.getTile(3), loc(2, 0))
    assertResult(1) { placements.size }
  }

  /**
    * Here we ask if there are fits where three should be possible.
    * 1
    * (4)   2
    */
  test("FitOnTwoWhereNonePossible") {
    tantrix = place2of3Tiles_OneThenTwo.tantrix
    fitter = new TantrixTileFitter(tantrix, PathColor.YELLOW)
    val placements = fitter.getFittingPlacements(TILES.getTile(4), loc(2, 0))
    assertResult(0) { placements.size }
  }

  /**
    * Here we ask if there are fits at a location where no primary path connections are possible.
    * 1   (3)
    * 2
    */
  test("FitOnTwoWhereNoPrimaryMatchPossible") {
    tantrix = place2of3Tiles_OneThenTwo.tantrix
    fitter = new TantrixTileFitter(tantrix, PathColor.YELLOW)
    val placements = fitter.getFittingPlacements(TILES.getTile(3), loc(1, 2))
    assertResult(0) { placements.size }
  }

  /**
    * Its not possible to have any primary path fits on a completed loop.
    * (4)    1
    * 3    2
    */
  test("FitOnThreeLoop") {
    tantrix = place3SolvedTiles.tantrix
    fitter = new TantrixTileFitter(tantrix, PathColor.YELLOW)
    val placements = fitter.getFittingPlacements(TILES.getTile(4), loc(1, 0))
    assertResult(0) { placements.size }
  }

  /**
    * In this case it will fit because we are only checking that one primary path matches,
    * but if we were being strict and using TileFitter it would not.
    * 1
    * (2)  3
    */
  test("PlacementDoesNotFit0") {
    tantrix = place2of3Tiles_OneThenThree.tantrix
    val tile2 = TilePlacement(TILES.getTile(2), loc(2, 1), Rotation.ANGLE_0)
    fitter = new TantrixTileFitter(tantrix, PathColor.YELLOW)
    assert(fitter.isFit(tile2))
  }

  test("PlacementDoesNotFit60") {
    tantrix = place2of3Tiles_OneThenThree.tantrix
    val tile2 = TilePlacement(TILES.getTile(2), loc(2, 0), Rotation.ANGLE_60)
    fitter = new TantrixTileFitter(tantrix, PathColor.YELLOW)
    assert(!fitter.isFit(tile2))
  }

  test("PlacementFits") {
    tantrix = place2of3Tiles_OneThenThree.tantrix
    val tile2 = TilePlacement(TILES.getTile(2), loc(2, 0), Rotation.ANGLE_300)
    fitter = new TantrixTileFitter(tantrix, PathColor.YELLOW)
    assert(fitter.isFit(tile2))
  }

  test("Tile2PlacementFits") {
    tantrix = place1of3Tiles_startingWithTile2.tantrix
    val tile2 = TilePlacement(TILES.getTile(3), loc(0, 0), Rotation.ANGLE_60)
    fitter = new TantrixTileFitter(tantrix, PathColor.YELLOW)
    assert(fitter.isFit(tile2))
  }

  test("fitting placements are the same as those from the general fitter, for every tile at every border location") {
    var numChecked = 0
    var numFitting = 0
    for (numTiles <- Seq(6, 9, 12); seed <- 1 to 3) {
      val tiles = new scala.util.Random(seed).shuffle(TILES.createOrderedList(numTiles))
      // look at the states along a few random paths through the search, so there are neighbors on various sides
      var board = new TantrixBoard(tiles)
      val rnd = new scala.util.Random(seed)
      var moves = new com.barrybecker4.puzzle.tantrix.generation.MoveGenerator(board).generateMoves
      while (moves.nonEmpty) {
        val general = new TileFitter(board.tantrix.tiles, board.primaryColor)
        val specialized = new TantrixTileFitter(board.tantrix, board.primaryColor)
        val locations = board.tantrix.tiles.flatMap(tp => (0 until HexTile.NUM_SIDES).map(HexUtil.getNeighborLocation(tp.location, _))).toSet
        for (loc <- locations if board.isEmpty(loc); tile <- tiles) {
          val expected = general.getFittingPlacements(tile, loc)
          assertResult(expected, s"$tile at $loc for \n$board")(specialized.getFittingPlacements(tile, loc))
          numChecked += 1
          numFitting += expected.size
        }
        board = board.placeTile(moves(rnd.nextInt(moves.size)))
        moves = new com.barrybecker4.puzzle.tantrix.generation.MoveGenerator(board).generateMoves
      }
    }
    assert(numChecked > 1000, "should check a lot of cases, but was " + numChecked)
    assert(numFitting > 100, "should include cases where tiles do fit, but was " + numFitting)
  }
}
