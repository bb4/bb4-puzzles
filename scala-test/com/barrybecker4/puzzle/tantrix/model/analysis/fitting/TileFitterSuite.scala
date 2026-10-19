// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model.analysis.fitting

import com.barrybecker4.common.geometry.{IntLocation, Location}
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil.*
import com.barrybecker4.puzzle.tantrix.model.HexTiles.TILES
import com.barrybecker4.puzzle.tantrix.model.{Tantrix, TantrixBoard}
import org.scalatest.funsuite.AnyFunSuite

/**
  * TileFitter finds neighbors by searching the tiles that it is given, while TantrixTileFitter asks the tantrix.
  * They must always agree.
  */
class TileFitterSuite extends AnyFunSuite {

  private val boards: Seq[TantrixBoard] = Seq(
    new TantrixBoard(THREE_TILES),
    place2of3Tiles_OneThenTwo,
    place2of3Tiles_OneThenThree,
    place4SolvedTiles,
    place6UnsolvedTiles,
    place7SolvedTiles
  )

  /** @return all the locations in the bounding box of the tantrix, plus a margin of one all around it. */
  private def locationsAround(tantrix: Tantrix): Seq[Location] = {
    val box = tantrix.getBoundingBox
    for {
      row <- box.getMinRow - 1 to box.getMaxRow + 1
      col <- box.getMinCol - 1 to box.getMaxCol + 1
    } yield IntLocation(row, col)
  }

  test("TileFitter and TantrixTileFitter find the same fitting placements for every tile at every location") {
    for (board <- boards) {
      val tantrixFitter = new TantrixTileFitter(board.tantrix, board.primaryColor)
      val tileFitter = new TileFitter(board.tantrix.tiles, board.primaryColor)
      for (tile <- TILES.createOrderedList(14); loc <- locationsAround(board.tantrix)) {
        assertResult(tantrixFitter.getFittingPlacements(tile, loc), s"tile ${tile.tantrixNumber} at $loc") {
          tileFitter.getFittingPlacements(tile, loc)
        }
      }
    }
  }

  test("TileFitter and TantrixTileFitter agree that the tiles that are already placed fit") {
    for (board <- boards if board.tantrix.size > 1) {
      val tileFitter = new TileFitter(board.tantrix.tiles, board.primaryColor)
      val tantrixFitter = new TantrixTileFitter(board.tantrix, board.primaryColor)
      for (placement <- board.tantrix.tiles)
        assertResult(tantrixFitter.isFit(placement), s"$placement") { tileFitter.isFit(placement) }
    }
  }

  test("a tile does not fit if there are no tiles for it to touch") {
    val tileFitter = new TileFitter(Seq.empty, new TantrixBoard(THREE_TILES).primaryColor)
    val lonelyTile = new TantrixBoard(THREE_TILES).tantrix.tiles.head
    assert(!tileFitter.isFit(lonelyTile))
  }
}
