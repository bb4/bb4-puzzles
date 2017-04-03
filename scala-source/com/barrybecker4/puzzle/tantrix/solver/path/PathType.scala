/*
 * // Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
 */

package com.barrybecker4.puzzle.tantrix.solver.path

/**
  * There are 3 types of path shapes on a tantrix tile
  */
object PathType extends Enumeration {
  type PathType = Value
  val TIGHT_CURVE, WIDE_CURVE, STRAIGHT = Value
}
