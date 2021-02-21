package com.barrybecker4.puzzle.rubixcube.ui.util

import com.barrybecker4.puzzle.rubixcube.ui.{HALF_PI, Point}

/**
  * @param offset  (h, k) in the standard formula
  * @param xRadius  a in the standard formula
  * @param yRadius  b in the standard formula
  * @param rotation the amount to rotate the ellipse in radians in a counter-clockwise direction
  */
case class RotatedEllipse(offset: Point, xRadius: Double, yRadius: Double, rotation: Double) {

  private val aa = xRadius * xRadius
  private val bb = yRadius * yRadius
  private val ab = xRadius * yRadius

  /**
    * @param theta must be in the range -PI to PI
    * @return the point on the ellipse
    */
  def getPointAtAngle(theta: Double): Point = {
    val sign = if (theta > -HALF_PI && theta < HALF_PI) 1 else -1
    val isTopOrBottom = theta == HALF_PI || theta == -HALF_PI
    val tanTheta = Math.tan(theta)

    val x =
      if (isTopOrBottom) 0
      else {
        val denom = Math.sqrt(bb + aa * tanTheta * tanTheta)
        assert(denom != 0)
        sign * ab / denom
      }

    val y = if (isTopOrBottom) Math.sin(theta) * yRadius else x * tanTheta

    val rotatedPoint = rotatePointOnCanonicalEllipse(x, y)

    // now shift it
    (rotatedPoint._1 + offset._1, rotatedPoint._2 + offset._2)
  }

  private def rotatePointOnCanonicalEllipse(x: Double, y: Double): Point = {
    var currentAngle = if (x == 0) HALF_PI else Math.atan(y / x)
    if (x < 0) {
      currentAngle += Math.PI
    }
    val len = Math.sqrt(x * x + y * y)
    val rx = len * Math.cos(currentAngle + rotation)
    val ry = len * Math.sin(currentAngle + rotation)
    assert(!rx.isNaN && !ry.isNaN, "rx = " + rx + " ry = " + ry)
    (rx.toFloat, ry.toFloat)
  }

}
