// Copyright by Barry G. Becker, 2017 - 2023. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path

import com.barrybecker4.puzzle.tantrix.PathTstUtil
import com.barrybecker4.puzzle.tantrix.model.PathColor
import com.barrybecker4.puzzle.tantrix.model.PathColor.{BLUE, RED, YELLOW}
import com.barrybecker4.puzzle.tantrix.solver.path.PathVerificationCase


object PathVerificationCase {

  val cases: Seq[PathVerificationCase] = Seq(
    PathVerificationCase("LOOP_PATH3", PathTstUtil.LOOP_PATH3, true,
      1, Map(RED -> 0, YELLOW -> 3, BLUE -> 0), false, Map(RED -> false, YELLOW -> true, BLUE -> false), 0,
      0),
    PathVerificationCase("NON_LOOP_PATH3", PathTstUtil.NON_LOOP_PATH3, false,
      1, Map(RED -> 0, YELLOW -> 1, BLUE -> 0), false, Map(RED -> false, YELLOW -> false, BLUE -> false), 1,
      1.3698924731182798),
    PathVerificationCase("LOOP_PATH4", PathTstUtil.LOOP_PATH4, true,
      1, Map(RED -> 4, YELLOW -> 0, BLUE -> 2), false, Map(RED -> true, YELLOW -> false, BLUE -> false), 0,
      0),
    PathVerificationCase("NON_LOOP_PATH4", PathTstUtil.NON_LOOP_PATH4, false,
      1, Map(RED -> 2, YELLOW -> 1, BLUE -> 0), false, Map(RED -> false, YELLOW -> false, BLUE -> false), 1,
      1.2268292682926827),
    PathVerificationCase("LINEAR_PATH4", PathTstUtil.LINEAR_PATH4, false,
      0, Map(RED -> 4, YELLOW -> 0, BLUE -> 0), false, Map(RED -> false, YELLOW -> false, BLUE -> false), 2.692582403567252,
      0.802981775348738 ),
    PathVerificationCase("LOOP_PATH5", PathTstUtil.LOOP_PATH5, true,
      1, Map(RED -> 5, YELLOW -> 0, BLUE -> 3), false, Map(RED -> true, YELLOW -> false, BLUE -> false), 0,
      0),
    PathVerificationCase("IMPERFECT_LOOP_PATH5", PathTstUtil.IMPERFECT_LOOP_PATH5, true,
      1, Map(RED -> 2, YELLOW -> 0, BLUE -> 0), false, Map(RED -> true, YELLOW -> false, BLUE -> false), 0,
      1.0999999999999996),
    PathVerificationCase("NON_LOOP_PATH5", PathTstUtil.NON_LOOP_PATH5, false,
      0.3333333333333333, Map(RED -> 2, YELLOW -> 1, BLUE -> 0), false, Map(RED -> false, YELLOW -> false, BLUE -> false), 3.3541019662496847,
      1.336033217671587),
    PathVerificationCase("NON_LOOP_S_PATH5", PathTstUtil.NON_LOOP_S_PATH5, false,
      1, Map(RED -> 3, YELLOW -> 1, BLUE -> 1), false, Map(RED -> false, YELLOW -> false, BLUE -> false), 2.0,
      1.0823529411764712),
    PathVerificationCase("LINEAR_NON_LOOP_PATH5", PathTstUtil.LINEAR_NON_LOOP_PATH5, false,
      0, Map(RED -> 5, YELLOW -> 0, BLUE -> 0), false, Map(RED -> false, YELLOW -> false, BLUE -> false), 3.605551275463989,
      0.7879087485021179),
    PathVerificationCase("LOOP_PATH6", PathTstUtil.LOOP_PATH6, true,
      1, Map(RED -> 2, YELLOW -> 3, BLUE -> 6), false, Map(RED -> false, YELLOW -> false, BLUE -> true), 0,
      0),
    PathVerificationCase("LOOP_PATH7", PathTstUtil.LOOP_PATH7, true,
      0.8333333333333334, Map(RED -> 3, YELLOW -> 3, BLUE -> 7), false, Map(RED -> false, YELLOW -> false, BLUE -> true), 0,
      0),
    PathVerificationCase("NON_LOOP_PATH7", PathTstUtil.NON_LOOP_PATH7, false,
      0.8333333333333334, Map(RED -> 2, YELLOW -> 2, BLUE -> 5), false, Map(RED -> false, YELLOW -> false, BLUE -> false), 1,
      1.0767940979208577),
    PathVerificationCase("NON_LOOP_SPACE_PATH7", PathTstUtil.NON_LOOP_SPACE_PATH7, false,
      0.3333333333333333, Map(RED -> 0, YELLOW -> 2, BLUE -> 5), false, Map(RED -> false, YELLOW -> false, BLUE -> false), 1.118033988749895,
      1.171806746283539),
    PathVerificationCase("NON_LOOP_WIGGLE_PATH7", PathTstUtil.NON_LOOP_WIGGLE_PATH7, false,
      0.6666666666666666, Map(RED -> 3, YELLOW -> 4, BLUE -> 7), false, Map(RED -> false, YELLOW -> false, BLUE -> false), 2.8284271247461903,
      0.7471556613957002),
    PathVerificationCase("LOOP_SPACE_PATH8", PathTstUtil.LOOP_SPACE_PATH8, true,
      0.42857142857142855, Map(RED -> 6, YELLOW -> 1, BLUE -> 0), false, Map(RED -> true, YELLOW -> false, BLUE -> false), 0,
      0.8642857142857148),
    PathVerificationCase("IMPERFECT_LOOP_PATH9", PathTstUtil.IMPERFECT_LOOP_PATH9, true,
      1, Map(RED -> 2, YELLOW -> 3, BLUE -> 0), false, Map(RED -> false, YELLOW -> true, BLUE -> false), 0,
      1.166666666666667),
    PathVerificationCase("LOOP_PATH14", PathTstUtil.LOOP_PATH14, true,
      0.9285714285714286, Map(RED -> 8, YELLOW -> 8, BLUE -> 14), false, Map(RED -> false, YELLOW -> false, BLUE -> true), 0,
      0)
  )
}


case class PathVerificationCase(name: String, path: TantrixPath, isLoop:Boolean,
  compactness: Double, numFits: Map[PathColor, Int], hasInnerSpace: Boolean, hasLoop: Map[PathColor, Boolean], endPointDistance: Double, fitness: Double)

