package com.barrybecker4.puzzle.rubixcube.ui

case class Point(x: Float, y: Float) {

  def this(angle: Double, len: Double) =
    this((len * Math.cos(angle)).toFloat, (len * Math.sin(angle)).toFloat)

  def add(pt: Point): Point = Point(pt.x + x, pt.y + y)

  def multiply(s: Float): Point = Point(s * x, s * y)

  def scale(s: Point): Point = Point(s.x * x, s.y * y)

  def length(): Double = Math.sqrt(x * x + y * y)
}
