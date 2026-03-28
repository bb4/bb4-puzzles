// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix

import com.barrybecker4.puzzle.tantrix.model.{TantrixBoard, TilePlacement}
import com.barrybecker4.search.Refreshable

/**
  * [[TantrixController]] that always returns a fixed starting board (e.g. for deterministic search tests).
  */
final class FixedStartTantrixController(
    refreshable: Refreshable[TantrixBoard, TilePlacement],
    private val startBoard: TantrixBoard
) extends TantrixController(refreshable) {

  override def initialState: TantrixBoard = startBoard
}
