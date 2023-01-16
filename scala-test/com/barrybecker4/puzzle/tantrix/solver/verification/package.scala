package com.barrybecker4.puzzle.tantrix.solver

import com.barrybecker4.puzzle.tantrix.PathTstUtil
import com.barrybecker4.puzzle.tantrix.model.PathColor
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath
import com.barrybecker4.puzzle.tantrix.solver.verification.PathVerificationCase
import PathColor._




package object verification {

  case class PathVerificationCase(name: String, path: TantrixPath,
    compactness: Double, numFits: Map[PathColor, Int], hasInnerSpace: Boolean, hasLoop: Boolean)

  val pathVerificationCases: Seq[PathVerificationCase] = Seq(
    PathVerificationCase("LOOP_PATH3", PathTstUtil.LOOP_PATH3,
      0.3333333333333333, Map(RED -> 0, YELLOW -> 3, BLUE -> 0), false, true),
    PathVerificationCase("NON_LOOP_PATH3", PathTstUtil.NON_LOOP_PATH3,
      0.3333333333333333, Map(RED -> 0, YELLOW -> 1, BLUE -> 0), false, false),
    PathVerificationCase("LOOP_PATH4", PathTstUtil.LOOP_PATH4,
      0.5, Map(RED -> 4, YELLOW -> 0, BLUE -> 2), false, true),
    PathVerificationCase("NON_LOOP_PATH4", PathTstUtil.NON_LOOP_PATH4,
      0.5, Map(RED -> 2, YELLOW -> 1, BLUE -> 0), false, false),
    PathVerificationCase("NON_LOOP_PATH5", PathTstUtil.NON_LOOP_PATH5,
      0.2, Map(RED -> 2, YELLOW -> 1, BLUE -> 0), false, false),
    PathVerificationCase("NON_LOOP_S_PATH5", PathTstUtil.NON_LOOP_S_PATH5,
      0.6, Map(RED -> 3, YELLOW -> 1, BLUE -> 1), false, false),
    PathVerificationCase("LINEAR_NON_LOOP_PATH5", PathTstUtil.LINEAR_NON_LOOP_PATH5,
      0.0, Map(RED -> 5, YELLOW -> 0, BLUE -> 0), false, false),
    PathVerificationCase("PathTstUtil.LOOP_PATH5", PathTstUtil.LOOP_PATH5,
      0.6, Map(RED -> 5, YELLOW -> 0, BLUE -> 3), false, true)
  )

}
