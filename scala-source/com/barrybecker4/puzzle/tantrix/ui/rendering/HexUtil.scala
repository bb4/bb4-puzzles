// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.ui.rendering

/**
  * Constants and static methods for working with hexagons
  * @author Barry Becker
  */
object HexUtil {
  private val DEG_TO_RAD = Math.PI / 180.0
  private[rendering] val ROOT3 = Math.sqrt(3.0)
  private[rendering] val ROOT3D2 = ROOT3 / 2.0

  private[rendering] def rad(angleInDegrees: Double) = angleInDegrees * DEG_TO_RAD
}
