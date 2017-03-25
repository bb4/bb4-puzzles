// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model

import com.barrybecker4.puzzle.tantrix.model.PathColor.PathColor


object PathColor extends Enumeration {
  type PathColor = Value
  val RED, GREEN, BLUE, YELLOW, WHITE = Value
}

/* This should have worked, but didn't */
object PathColors {
  def apply(c1: PathColor, c2: PathColor, c3: PathColor, c4: PathColor, c5: PathColor, c6: PathColor) =
    new PathColors(Array(c1, c2, c3, c4, c5, c6))
}

/** The list of 6 edge colors starting from the right side and going counter clockwise. Immutable */
case class PathColors(colors: Array[PathColor]) {

  def apply(i: Int): PathColor = colors(i)
}