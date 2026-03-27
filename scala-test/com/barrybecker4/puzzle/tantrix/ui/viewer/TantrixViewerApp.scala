// Copyright by Barry G. Becker, 2023. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.ui.viewer

import com.barrybecker4.puzzle.tantrix.PathTstUtil
import com.barrybecker4.puzzle.tantrix.model.{PathColor, TantrixBoard}


/**
  * Simple class to help viewing tile arrangements
  */
@main def TantrixViewerApp(): Unit =
  val tiles = PathTstUtil.IMPERFECT_LOOP_PATH14.tiles
  val board = new TantrixBoard(tiles, PathColor.BLUE)
  new TantrixViewerFrame(board)



