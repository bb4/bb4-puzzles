package com.barrybecker4.puzzle.redpuzzle

import com.barrybecker4.puzzle.redpuzzle.model.{OrientedPiece, PieceList}
import com.barrybecker4.search.Refreshable


class StubRefreshable extends Refreshable[PieceList, OrientedPiece] {

  override def refresh(state: PieceList, numTries: Long): Unit = {}

  override def animateTransition(transition: OrientedPiece): PieceList = null

  override def finalRefresh(path: Option[Seq[OrientedPiece]],
                            state: Option[PieceList], numTries: Long, elapsedMillis: Long): Unit = {}
}
