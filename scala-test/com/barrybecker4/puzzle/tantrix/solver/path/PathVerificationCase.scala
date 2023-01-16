// Copyright by Barry G. Becker, 2017 - 2023. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path

import com.barrybecker4.puzzle.tantrix.PathTstUtil
import com.barrybecker4.puzzle.tantrix.model.PathColor
import com.barrybecker4.puzzle.tantrix.model.PathColor.{BLUE, RED, YELLOW}
import com.barrybecker4.puzzle.tantrix.solver.path.PathVerificationCase


object PathVerificationCase {
  val cases: Seq[PathVerificationCase] = Seq(
    PathVerificationCase("LOOP_PATH3", PathTstUtil.LOOP_PATH3,
      0.3333333333333333, Map(RED -> 0, YELLOW -> 3, BLUE -> 0), false, Map(RED -> false, YELLOW -> true, BLUE -> false), 0),
    PathVerificationCase("NON_LOOP_PATH3", PathTstUtil.NON_LOOP_PATH3,
      0.3333333333333333, Map(RED -> 0, YELLOW -> 1, BLUE -> 0), false, Map(RED -> false, YELLOW -> false, BLUE -> false), 3.5032258064516135),
    PathVerificationCase("LOOP_PATH4", PathTstUtil.LOOP_PATH4,
      0.5, Map(RED -> 4, YELLOW -> 0, BLUE -> 2), false, Map(RED -> true, YELLOW -> false, BLUE -> false), 0),
    PathVerificationCase("NON_LOOP_PATH4", PathTstUtil.NON_LOOP_PATH4,
      0.5, Map(RED -> 2, YELLOW -> 1, BLUE -> 0), false, Map(RED -> false, YELLOW -> false, BLUE -> false), 3.326829268292683),
    PathVerificationCase("NON_LOOP_PATH5", PathTstUtil.NON_LOOP_PATH5,
      0.2, Map(RED -> 2, YELLOW -> 1, BLUE -> 0), false, Map(RED -> false, YELLOW -> false, BLUE -> false), 3.3626998843382534),
    PathVerificationCase("NON_LOOP_S_PATH5", PathTstUtil.NON_LOOP_S_PATH5,
      0.6, Map(RED -> 3, YELLOW -> 1, BLUE -> 1), false, Map(RED -> false, YELLOW -> false, BLUE -> false), 3.1623529411764713),
    PathVerificationCase("LINEAR_NON_LOOP_PATH5", PathTstUtil.LINEAR_NON_LOOP_PATH5,
      0.0, Map(RED -> 5, YELLOW -> 0, BLUE -> 0), false, Map(RED -> false, YELLOW -> false, BLUE -> false), 2.787908748502118),
    PathVerificationCase("PathTstUtil.LOOP_PATH5", PathTstUtil.LOOP_PATH5,
      0.6, Map(RED -> 5, YELLOW -> 0, BLUE -> 3), false, Map(RED -> true, YELLOW -> false, BLUE -> false), 0)
  )
}


case class PathVerificationCase(name: String, path: TantrixPath,
  compactness: Double, numFits: Map[PathColor, Int], hasInnerSpace: Boolean, hasLoop: Map[PathColor, Boolean], fitness: Double)

