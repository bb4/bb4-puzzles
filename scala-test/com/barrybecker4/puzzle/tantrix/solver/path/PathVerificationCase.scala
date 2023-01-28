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
      1.333333333333334),
    PathVerificationCase("LOOP_PATH4", PathTstUtil.LOOP_PATH4, true,
      1, Map(RED -> 4, YELLOW -> 0, BLUE -> 2), false, Map(RED -> true, YELLOW -> false, BLUE -> false), 0,
      0),
    PathVerificationCase("LOOP_PATH4_OF_5", PathTstUtil.LOOP_PATH4_OF_5, true,
      1, Map(RED -> 4, YELLOW -> 0, BLUE -> 2), false, Map(RED -> true, YELLOW -> false, BLUE -> false), 0,
      2.0),
    PathVerificationCase("LOOP_PATH4_OF_6", PathTstUtil.LOOP_PATH4_OF_6, true,
      1, Map(RED -> 4, YELLOW -> 0, BLUE -> 2), false, Map(RED -> true, YELLOW -> false, BLUE -> false), 0,
      3.0 ),
    PathVerificationCase("LOOP_PATH4_OF_7", PathTstUtil.LOOP_PATH4_OF_7, true,
      1, Map(RED -> 4, YELLOW -> 0, BLUE -> 2), false, Map(RED -> true, YELLOW -> false, BLUE -> false), 0,
      4.0),
    PathVerificationCase("LOOP_PATH4_OF_8", PathTstUtil.LOOP_PATH4_OF_8, true,
      1, Map(RED -> 4, YELLOW -> 0, BLUE -> 2), false, Map(RED -> true, YELLOW -> false, BLUE -> false), 0,
      5.0),
    PathVerificationCase("LOOP_PATH4_OF_9", PathTstUtil.LOOP_PATH4_OF_9, true,
      1, Map(RED -> 4, YELLOW -> 0, BLUE -> 2), false, Map(RED -> true, YELLOW -> false, BLUE -> false), 0,
      6.0),
    PathVerificationCase("NON_LOOP_PATH4", PathTstUtil.NON_LOOP_PATH4, false,
      1, Map(RED -> 2, YELLOW -> 1, BLUE -> 0), false, Map(RED -> false, YELLOW -> false, BLUE -> false), 1,
      1.25),
    PathVerificationCase("NON_LOOP_PATH4_OF_5", PathTstUtil.NON_LOOP_PATH4_OF_5, false,
      1, Map(RED -> 2, YELLOW -> 1, BLUE -> 0), false, Map(RED -> false, YELLOW -> false, BLUE -> false), 1,
      2.250000000000001),
    PathVerificationCase("NON_LOOP_PATH4_OF_6", PathTstUtil.NON_LOOP_PATH4_OF_6, false,
      1, Map(RED -> 2, YELLOW -> 1, BLUE -> 0), false, Map(RED -> false, YELLOW -> false, BLUE -> false), 1,
      3.250000000000001),
    PathVerificationCase("LINEAR_PATH4", PathTstUtil.LINEAR_PATH4, false,
      0, Map(RED -> 4, YELLOW -> 0, BLUE -> 0), false, Map(RED -> false, YELLOW -> false, BLUE -> false), 2.692582403567252,
      1.0307417596432753 ),
    PathVerificationCase("LOOP_PATH5", PathTstUtil.LOOP_PATH5, true,
      1, Map(RED -> 5, YELLOW -> 0, BLUE -> 3), false, Map(RED -> true, YELLOW -> false, BLUE -> false), 0,
      0),
    PathVerificationCase("IMPERFECT_LOOP_PATH5", PathTstUtil.IMPERFECT_LOOP_PATH5, true,
      1, Map(RED -> 2, YELLOW -> 0, BLUE -> 0), false, Map(RED -> true, YELLOW -> false, BLUE -> false), 0,
      0.41999999999999993),
    PathVerificationCase("NON_LOOP_PATH5", PathTstUtil.NON_LOOP_PATH5, false,
      0.3333333333333333, Map(RED -> 2, YELLOW -> 1, BLUE -> 0), false, Map(RED -> false, YELLOW -> false, BLUE -> false), 3.3541019662496847,
      1.3516718427000267),
    PathVerificationCase("NON_LOOP_S_PATH5", PathTstUtil.NON_LOOP_S_PATH5, false,
      1, Map(RED -> 3, YELLOW -> 1, BLUE -> 1), false, Map(RED -> false, YELLOW -> false, BLUE -> false), 2.0,
      1.1199999999999992),
    PathVerificationCase("LINEAR_NON_LOOP_PATH5", PathTstUtil.LINEAR_NON_LOOP_PATH5, false,
      0, Map(RED -> 5, YELLOW -> 0, BLUE -> 0), false, Map(RED -> false, YELLOW -> false, BLUE -> false), 3.605551275463989,
      1.0115558979628823),
    PathVerificationCase("LOOP_PATH6", PathTstUtil.LOOP_PATH6, true,
      1, Map(RED -> 2, YELLOW -> 3, BLUE -> 6), false, Map(RED -> false, YELLOW -> false, BLUE -> true), 0,
      0),
    PathVerificationCase("LOOP_PATH7", PathTstUtil.LOOP_PATH7, true,
      0.8333333333333334, Map(RED -> 3, YELLOW -> 3, BLUE -> 7), false, Map(RED -> false, YELLOW -> false, BLUE -> true), 0,
      0),
    PathVerificationCase("NON_LOOP_PATH7", PathTstUtil.NON_LOOP_PATH7, false,
      0.8333333333333334, Map(RED -> 2, YELLOW -> 2, BLUE -> 5), false, Map(RED -> false, YELLOW -> false, BLUE -> false), 1,
      1.192857142857143),
    PathVerificationCase("NON_LOOP_SPACE_PATH7", PathTstUtil.NON_LOOP_SPACE_PATH7, false,
      0.3333333333333333, Map(RED -> 0, YELLOW -> 2, BLUE -> 5), false, Map(RED -> false, YELLOW -> false, BLUE -> false), 1.118033988749895,
      1.336112343500007 ),
    PathVerificationCase("NON_LOOP_WIGGLE_PATH7", PathTstUtil.NON_LOOP_WIGGLE_PATH7, false,
      0.6666666666666666, Map(RED -> 3, YELLOW -> 4, BLUE -> 7), false, Map(RED -> false, YELLOW -> false, BLUE -> false), 2.8284271247461903,
      0.9383755928716475),
    PathVerificationCase("LOOP_SPACE_PATH8", PathTstUtil.LOOP_SPACE_PATH8, true,
      0.42857142857142855, Map(RED -> 6, YELLOW -> 1, BLUE -> 0), false, Map(RED -> true, YELLOW -> false, BLUE -> false), 0,
      0.34642857142857153),
    PathVerificationCase("IMPERFECT_LOOP_PATH9", PathTstUtil.IMPERFECT_LOOP_PATH9, true,
      1, Map(RED -> 2, YELLOW -> 3, BLUE -> 0), false, Map(RED -> false, YELLOW -> true, BLUE -> false), 0,
      0.4666666666666668),
    PathVerificationCase("LOOP_PATH14", PathTstUtil.LOOP_PATH14, true,
      0.9285714285714286, Map(RED -> 8, YELLOW -> 8, BLUE -> 14), false, Map(RED -> false, YELLOW -> false, BLUE -> true), 0,
      0),
    PathVerificationCase("IMPERFECT_LOOP_PATH14", PathTstUtil.IMPERFECT_LOOP_PATH14, true,
      0.9285714285714286, Map(RED -> 5, YELLOW -> 5, BLUE -> 11), false, Map(RED -> false, YELLOW -> false, BLUE -> true), 0,
      0.17142857142857082)
  )
}


case class PathVerificationCase(name: String, path: TantrixPath, isLoop:Boolean,
  compactness: Double, numFits: Map[PathColor, Int], hasInnerSpace: Boolean, hasLoop: Map[PathColor, Boolean], endPointDistance: Double, fitness: Double)

