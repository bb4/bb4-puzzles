package com.barrybecker4.puzzle.rubixcube.ui

case class Point(x: Float, y: Float) {

  def this(angle: Double, len: Double) =
    this((len * Math.cos(angle)).toFloat, (len * Math.sin(angle)).toFloat)

  def add(pt: Point): Point = {
    Point(pt.x + x, pt.y + y)
  }

  def length(): Double = Math.sqrt(x * x + y * y)
}
