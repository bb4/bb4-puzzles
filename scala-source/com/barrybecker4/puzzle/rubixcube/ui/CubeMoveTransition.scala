package com.barrybecker4.puzzle.rubixcube.ui

import com.barrybecker4.puzzle.rubixcube.model.CubeMove

/**
  * The amount that the specified move is applied
  * @param percentDone a number in [0, 100] where 100 means 100% applied
  */
case class CubeMoveTransition(move: CubeMove, percentDone: Double)
