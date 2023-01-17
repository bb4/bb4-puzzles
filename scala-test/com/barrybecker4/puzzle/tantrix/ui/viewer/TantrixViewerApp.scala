package com.barrybecker4.puzzle.tantrix.ui.viewer

import com.barrybecker4.common.geometry.ByteLocation
import com.barrybecker4.puzzle.tantrix.model.{PathColor, TantrixBoard, TilePlacement}
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath
import com.barrybecker4.puzzle.tantrix.ui.viewer.TantrixViewerFrame
import com.barrybecker4.puzzle.tantrix.PathTstUtil


/**
  * Simple class to help viewing tile arrangements
  */
@main def TantrixViewerApp(): Unit =
  val tiles = PathTstUtil.LOOP_PATH5.tiles
  val board = new TantrixBoard(tiles, PathColor.YELLOW)
  new TantrixViewerFrame(board)



