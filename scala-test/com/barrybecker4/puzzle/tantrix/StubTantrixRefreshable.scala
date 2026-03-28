// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix

import com.barrybecker4.puzzle.tantrix.model.{TantrixBoard, TilePlacement}
import com.barrybecker4.search.Refreshable

/** No-op [[Refreshable]] for headless tests of controllers and solvers. */
class StubTantrixRefreshable extends Refreshable[TantrixBoard, TilePlacement] {

  override def refresh(state: TantrixBoard, numTries: Long): Unit = ()

  override def animateTransition(transition: TilePlacement): TantrixBoard = null

  override def finalRefresh(path: Option[Seq[TilePlacement]], state: Option[TantrixBoard],
                            numTries: Long, elapsedMillis: Long): Unit = ()
}
